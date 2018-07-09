package cattt.gst.library.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.ArrayMap;
import android.view.View;

import java.util.Vector;
import java.util.concurrent.Executors;

import cattt.gst.library.base.callback.OnGlobalThemeListener;
import cattt.gst.library.base.model.GTData;
import cattt.gst.library.base.model.GlobalThemeable;
import cattt.gst.library.base.model.MatchViewData;
import cattt.gst.library.base.model.emdata.ViewType;
import cattt.gst.library.utils.bitmap.File2Bitmap;
import cattt.gst.library.utils.logger.Log;

public class GlobalThemeWorker implements GlobalThemeable, OnGlobalThemeListener {
    private static Log logger = Log.getLogger(GlobalThemeWorker.class);

    public static final int MSG_CODE_BACKGROUND_DRAWABLE = 10000;
    public static final int MSG_CODE_BACKGROUND_COLOR = 10001;
    public static final int MSG_CODE_IMAGE_DRAWABLE = 10002;
    public static final int MSG_CODE_TEXT_COLOR = 10003;
    public static final int MSG_CODE_HINT_COLOR = 10004;

    private MatchingViewRunnable mMatchingViewRunnable;
    private BaseSwitchThemeActivity activity;

    protected GlobalThemeWorker(BaseSwitchThemeActivity activity) {
        this.activity = activity;
        mMatchingViewRunnable = new MatchingViewRunnable(this, activity);
    }

    private BaseSwitchThemeFragment fragment;

    protected GlobalThemeWorker(BaseSwitchThemeFragment fragment) {
        this.fragment = fragment;
        mMatchingViewRunnable = new MatchingViewRunnable(this, fragment);
    }

    public void register(){
        GlobalThemeMonitor.get().addOnGlobalThemeListener(this);
    }


    public void unregister(){
        GlobalThemeMonitor.get().removeOnGlobalThemeListener(this);
    }

    /**
     * 全局主题更换事件
     */
    @Override
    public void onSwitchResources() {
        if (isEmptyGlobalThemeResourcesMap()) {
            return;
        }
        checkAllowLoadingGlobalThemeOfView();
    }

    @Override
    public ArrayMap<String, Vector<GTData>> getArrayMapOfGlobalThemeResources() {
        return SingleGlobalThemeArrayMap.get().getGlobalThemeResourcesMap();
    }

    @Override
    public String resId2EntryName(int resId) {
        Context context = null;
        if (activity != null) {
            context = activity.getApplicationContext();
        }
        if (fragment != null) {
            context = fragment.getContext().getApplicationContext();
        }

        if (context == null) {
            throw new NullPointerException("context cannot be null.");
        }

        if (!"id".equals(context.getResources().getResourceTypeName(resId))) {
            throw new IllegalArgumentException(String.format("Unable to convert resId[%d], Need resource type is R.id.x.", resId));
        }
        return context.getResources().getResourceEntryName(resId);
    }

    @Override
    public int[] getResIdPendingChangeTheme() {
        if (activity != null) {
            return activity.getViewResourcesPendingChangeTheme();
        }
        if (fragment != null) {
            return fragment.getViewResourcesPendingChangeTheme();
        }
        return new int[0];
    }

    @Override
    public boolean isEmptyResId() {
        return !(getResIdPendingChangeTheme() != null && getResIdPendingChangeTheme().length > 0);
    }

    @Override
    public boolean isEmptyGlobalThemeResourcesMap() {
        return getArrayMapOfGlobalThemeResources().isEmpty();
    }

    @Override
    public boolean isAllowLoadingGlobalThemeOfView() {
        if (activity != null) {
            return activity.isWindowFocus() && !isEmptyResId();
        }
        if (fragment != null) {
            return fragment.isWindowFocus() && !isEmptyResId() && fragment.getView() != null;
        }
        return false;
    }

    @Override
    public void performSwitchThemeByAsync() {
        Executors.newFixedThreadPool(3).execute(mMatchingViewRunnable);
    }

    @Override
    public void checkAllowLoadingGlobalThemeOfView() {
        if (isAllowLoadingGlobalThemeOfView()) {
            performSwitchThemeByAsync();
        }
    }


    private static class MatchingViewRunnable implements Runnable {
        private GlobalThemeable source;
        private BaseSwitchThemeActivity activity;

        public MatchingViewRunnable(GlobalThemeable source, BaseSwitchThemeActivity activity) {
            this.source = source;
            this.activity = activity;
        }

        private BaseSwitchThemeFragment fragment;

        public MatchingViewRunnable(GlobalThemeable source, BaseSwitchThemeFragment fragment) {
            this.source = source;
            this.fragment = fragment;
        }

        @Override
        public void run() {
            for (int id : source.getResIdPendingChangeTheme()) {
                String name = source.resId2EntryName(id);
                final Vector<GTData> datas = source.getArrayMapOfGlobalThemeResources().get(name);
                if (!(datas != null && datas.size() > 0)) {
                    continue;
                }
                View view = null;
                if (activity != null) {
                    view = activity.findViewById(id);
                }
                if (fragment != null) {
                    view = fragment.getView().findViewById(id);
                }
                if (view == null) {
                    continue;
                }
                for (GTData mGTData : datas) {
                    switch (mGTData.getType()) {
                        case ViewType.TYPE_BACKGROUND_DRAWABLE:
                            try {
                                Bitmap bitmap =  File2Bitmap.decodeFile(mGTData.getContent(), view);

                                if (activity != null) {
                                    activity.getMatchingViewHandler().obtainMessage(MSG_CODE_BACKGROUND_DRAWABLE, new MatchViewData(id, new BitmapDrawable(activity.getApplicationContext().getResources(), bitmap))).sendToTarget();
                                }
                                if (fragment != null) {
                                    fragment.getMatchingViewHandler().obtainMessage(MSG_CODE_BACKGROUND_DRAWABLE, new MatchViewData(id, new BitmapDrawable(fragment.getContext().getApplicationContext().getResources(), bitmap))).sendToTarget();
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            break;
                        case ViewType.TYPE_BACKGROUND_COLOR:
                            try {
                                if (activity != null) {
                                    activity.getMatchingViewHandler().obtainMessage(MSG_CODE_BACKGROUND_COLOR, new MatchViewData(id, Color.parseColor(mGTData.getContent()))).sendToTarget();
                                }
                                if (fragment != null) {
                                    fragment.getMatchingViewHandler().obtainMessage(MSG_CODE_BACKGROUND_COLOR, new MatchViewData(id, Color.parseColor(mGTData.getContent()))).sendToTarget();
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            break;
                        case ViewType.TYPE_IMAGE_DRAWABLE:
                            try {
                                Bitmap bitmap =  File2Bitmap.decodeFile(mGTData.getContent(), view);
                                if (activity != null) {
                                    activity.getMatchingViewHandler().obtainMessage(MSG_CODE_IMAGE_DRAWABLE, new MatchViewData(id, new BitmapDrawable(activity.getApplicationContext().getResources(), bitmap))).sendToTarget();
                                }
                                if (fragment != null) {
                                    fragment.getMatchingViewHandler().obtainMessage(MSG_CODE_IMAGE_DRAWABLE, new MatchViewData(id, new BitmapDrawable(fragment.getContext().getApplicationContext().getResources(), bitmap))).sendToTarget();
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            break;
                        case ViewType.TYPE_TEXT_COLOR:
                            try {
                                if (activity != null) {
                                    activity.getMatchingViewHandler().obtainMessage(MSG_CODE_TEXT_COLOR, new MatchViewData(id, Color.parseColor(mGTData.getContent()))).sendToTarget();
                                }
                                if (fragment != null) {
                                    fragment.getMatchingViewHandler().obtainMessage(MSG_CODE_TEXT_COLOR, new MatchViewData(id, Color.parseColor(mGTData.getContent()))).sendToTarget();
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            break;
                        case ViewType.TYPE_HINT_COLOR:
                            try {
                                if (activity != null) {
                                    activity.getMatchingViewHandler().obtainMessage(MSG_CODE_HINT_COLOR, new MatchViewData(id, Color.parseColor(mGTData.getContent()))).sendToTarget();
                                }
                                if (fragment != null) {
                                    fragment.getMatchingViewHandler().obtainMessage(MSG_CODE_HINT_COLOR, new MatchViewData(id, Color.parseColor(mGTData.getContent()))).sendToTarget();
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                            break;
                    }
                }
            }
        }
    }
}
