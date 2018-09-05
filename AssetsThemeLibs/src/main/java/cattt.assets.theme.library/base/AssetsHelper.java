package cattt.assets.theme.library.base;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.ArrayMap;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Vector;
import java.util.concurrent.Executors;

import cattt.assets.theme.library.base.callback.OnAssetsListener;
import cattt.assets.theme.library.base.callback.OnViewThemeListener;
import cattt.assets.theme.library.base.enums.ParseAssetsType;
import cattt.assets.theme.library.base.model.BackgroundsBean;
import cattt.assets.theme.library.base.model.ColorsBean;
import cattt.assets.theme.library.base.model.SelectorsBean;
import cattt.assets.theme.library.base.parse.ParseAssetsMonitor;

public class AssetsHelper implements OnAssetsListener, OnViewThemeListener {
    private static final long MATCH_DELAY = 64L;

    private Context mContext;
    private AssetsHandler mHandler;
    private MatchRunnable mColorsRunnable, mSelectorsRunnable, mBackgroundsRunnable;

    @Override
    public void onColorAssets(Vector<ColorsBean> beans) {
        sendMessagesOfHandler(AssetsHandler.MSG_CODE_TERMINATE_MATCH_COLORS, 0L);
        obtainMessagesOfHandler(AssetsHandler.MSG_CODE_START_MATCH_COLORS, beans, MATCH_DELAY);
    }

    @Override
    public void onSelectorAssets(Vector<SelectorsBean> beans) {
        sendMessagesOfHandler(AssetsHandler.MSG_CODE_TERMINATE_MATCH_SELECTORS, 0L);
        obtainMessagesOfHandler(AssetsHandler.MSG_CODE_START_MATCH_SELECTORS, beans, MATCH_DELAY);
    }

    @Override
    public void onBackgroundAssets(Vector<BackgroundsBean> beans) {
        sendMessagesOfHandler(AssetsHandler.MSG_CODE_TERMINATE_MATCH_BACKGROUNDS, 0L);
        obtainMessagesOfHandler(AssetsHandler.MSG_CODE_START_MATCH_BACKGROUNDS, beans, MATCH_DELAY);
    }

    @Override
    public void onChangedFontColor(View view, int textColor, int hintColor) {
        if (view == null) {
            return;
        }
        setTextColor(view, textColor);
        setHintTextColor(view, hintColor);
    }

    @Override
    public void onChangedBackground(View view, Drawable drawable) {
        if (view == null) {
            return;
        }
        view.setBackground(drawable);
    }

    public AssetsHelper(@NonNull Activity activity, @NonNull int... ids) {
        this.mContext = activity.getApplicationContext();
        this.mHandler = new AssetsHandler(this);
        final ArrayMap<String, View> map = loadingIdsMapData(activity, ids);
        initMatchRunnable(mContext, map, this);
        ParseAssetsMonitor.get().addOnAssetsListener(this);
    }

    public AssetsHelper(@NonNull Fragment fragment, @NonNull int... ids) {
        this.mContext = fragment.getContext().getApplicationContext();
        this.mHandler = new AssetsHandler(this);
        final ArrayMap<String, View> map = loadingIdsMapData(fragment, ids);
        initMatchRunnable(mContext, map, this);
        ParseAssetsMonitor.get().addOnAssetsListener(this);
    }

    private void setTextColor(View view, int textColor) {
        if (textColor == 0) {
            return;
        }
        if (view instanceof TextView) {
            ((TextView) view).setTextColor(textColor);
        } else if (view instanceof Toolbar) {
            ((Toolbar) view).setTitleTextColor(textColor);
        }
    }

    private void setHintTextColor(View view, int hintColor) {
        if (view instanceof EditText) {
            ((EditText) view).setHintTextColor(hintColor);
        }
    }

    private void initMatchRunnable(Context context, ArrayMap<String, View> map, OnViewThemeListener listener) {
        this.mColorsRunnable = new MatchRunnable(context, map, ParseAssetsType.COLORS, listener);
        this.mSelectorsRunnable = new MatchRunnable(context, map, ParseAssetsType.SELECTORS, listener);
        this.mBackgroundsRunnable = new MatchRunnable(context, map, ParseAssetsType.BACKGROUNDS, listener);
    }

    public void destroyAssetsHelper() {
        ParseAssetsMonitor.get().removeOnAssetsListener(this);
        destroyAssetsHandler();
        destroyColorsRunnable();
        destroySelectorsRunnable();
        destroyBackgroundsRunnable();
    }

    private ArrayMap<String, View> loadingIdsMapData(Fragment fragment, int[] ids) {
        final ArrayMap<String, View> map = new ArrayMap<>();
        for (final int id : ids) {
            final View view = getViewByResourcesId(fragment, id);
            if (view != null) {
                map.put(getEntryNameFromResourcesId(mContext.getResources(), id), view);
            }
        }
        return map;
    }

    private ArrayMap<String, View> loadingIdsMapData(Activity activity, int[] ids) {
        final ArrayMap<String, View> map = new ArrayMap<>();
        for (final int id : ids) {
            final View view = getViewByResourcesId(activity, id);
            if (view != null) {
                map.put(getEntryNameFromResourcesId(mContext.getResources(), id), view);
            }
        }
        return map;
    }

    private void terminateMatchColors() {
        if (mColorsRunnable != null) {
            mColorsRunnable.terminate();
        }
    }

    private void terminateMatchSelectors() {
        if (mSelectorsRunnable != null) {
            mSelectorsRunnable.terminate();
        }
    }

    private void terminateMatchBackgrounds() {
        if (mBackgroundsRunnable != null) {
            mBackgroundsRunnable.terminate();
        }
    }

    private void destroyColorsRunnable() {
        if (mColorsRunnable != null) {
            mColorsRunnable.terminate();
            mColorsRunnable = null;
        }
    }

    private void destroySelectorsRunnable() {
        if (mSelectorsRunnable != null) {
            mSelectorsRunnable.terminate();
            mSelectorsRunnable = null;
        }
    }

    private void destroyBackgroundsRunnable() {
        if (mBackgroundsRunnable != null) {
            mBackgroundsRunnable.terminate();
            mBackgroundsRunnable = null;
        }
    }

    private void sendMessagesOfHandler(int what, long delayMillis) {
        if (mHandler != null) {
            mHandler.sendEmptyMessageDelayed(what, delayMillis);
        }
    }

    private void obtainMessagesOfHandler(final int what, final Object obj, final long delayMillis) {
        if (mHandler != null) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mHandler != null && obj != null) {
                        mHandler.obtainMessage(what, obj).sendToTarget();
                    }
                }
            }, delayMillis);
        }
    }

    private void removeMessagesOfHandler(int what) {
        if (mHandler != null) {
            mHandler.removeMessages(what);
        }
    }

    private void destroyAssetsHandler() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }

    private void startMatchColorsThread(Vector<ColorsBean> beans) {
        if (mColorsRunnable != null) {
            mColorsRunnable.setColorsBeans(beans);
            Executors.newCachedThreadPool().execute(mColorsRunnable.pending());
        }
    }

    private void startMatchSelectorsThread(Vector<SelectorsBean> beans) {
        if (mSelectorsRunnable != null) {
            mSelectorsRunnable.setSelectorsBean(beans);
            Executors.newCachedThreadPool().execute(mSelectorsRunnable.pending());
        }
    }

    private void startMatchBackgroundsThread(Vector<BackgroundsBean> beans) {
        if (mBackgroundsRunnable != null) {
            mBackgroundsRunnable.setBackgroundsBean(beans);
            Executors.newCachedThreadPool().execute(mBackgroundsRunnable.pending());
        }
    }

    private static View getViewByResourcesId(@NonNull Activity activity, int resId) {
        return activity.findViewById(resId);
    }

    private static View getViewByResourcesId(@NonNull Fragment fragment, int resId) {
        return fragment.getView() != null ? fragment.getView().findViewById(resId) : null;
    }

    private static String getEntryNameFromResourcesId(@NonNull Resources res, int resId) {
        if (res == null) {
            throw new NullPointerException("Resources param cannot be null.");
        }
        if (!"id".equals(res.getResourceTypeName(resId))) {
            throw new IllegalArgumentException(String.format("Unable to convert resId[%d], Need resource type is R.id.x.", resId));
        }
        return res.getResourceEntryName(resId);
    }

    private static class AssetsHandler extends Handler {
        private static final int MSG_CODE_START_MATCH_COLORS = 10000;
        private static final int MSG_CODE_START_MATCH_SELECTORS = 10001;
        private static final int MSG_CODE_START_MATCH_BACKGROUNDS = 10002;
        private static final int MSG_CODE_TERMINATE_MATCH_COLORS = 10003;
        private static final int MSG_CODE_TERMINATE_MATCH_SELECTORS = 10004;
        private static final int MSG_CODE_TERMINATE_MATCH_BACKGROUNDS = 10005;
        private AssetsHelper mRoot;

        public AssetsHandler(AssetsHelper root) {
            super(Looper.getMainLooper());
            this.mRoot = root;
        }

        @Override
        public void handleMessage(final Message msg) {
            switch (msg.what) {
                case MSG_CODE_START_MATCH_COLORS:
                    mRoot.startMatchColorsThread((Vector<ColorsBean>) msg.obj);
                    break;
                case MSG_CODE_START_MATCH_SELECTORS:
                    mRoot.startMatchSelectorsThread((Vector<SelectorsBean>) msg.obj);
                    break;
                case MSG_CODE_START_MATCH_BACKGROUNDS:
                    mRoot.startMatchBackgroundsThread((Vector<BackgroundsBean>) msg.obj);
                    break;
                case MSG_CODE_TERMINATE_MATCH_COLORS:
                    mRoot.removeMessagesOfHandler(MSG_CODE_START_MATCH_COLORS);
                    mRoot.terminateMatchColors();
                    break;
                case MSG_CODE_TERMINATE_MATCH_SELECTORS:
                    mRoot.removeMessagesOfHandler(MSG_CODE_START_MATCH_SELECTORS);
                    mRoot.terminateMatchSelectors();
                    break;
                case MSG_CODE_TERMINATE_MATCH_BACKGROUNDS:
                    mRoot.removeMessagesOfHandler(MSG_CODE_START_MATCH_BACKGROUNDS);
                    mRoot.terminateMatchBackgrounds();
                    break;
            }
        }
    }
}