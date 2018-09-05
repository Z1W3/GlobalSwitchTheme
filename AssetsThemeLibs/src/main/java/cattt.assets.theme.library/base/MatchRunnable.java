package cattt.assets.theme.library.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.ViewCompat;
import android.util.ArrayMap;
import android.view.View;
import android.view.ViewTreeObserver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import cattt.assets.theme.library.base.callback.OnViewThemeListener;
import cattt.assets.theme.library.base.enums.ParseAssetsType;
import cattt.assets.theme.library.base.enums.ParseAssetsTypeClub;
import cattt.assets.theme.library.base.model.BackgroundsBean;
import cattt.assets.theme.library.base.model.ColorsBean;
import cattt.assets.theme.library.base.model.SelectorsBean;
import cattt.assets.theme.library.base.model.StateDrawableBean;
import cattt.assets.theme.library.utils.bitmap.File2Bitmap;

public class MatchRunnable implements Runnable {
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Context mContext;
    private boolean mTerminate;
    private ArrayMap<String, View> mIdsMap;
    @ParseAssetsTypeClub
    private int mToken;
    private Vector<ColorsBean> mColorsBeans;
    private Vector<SelectorsBean> mSelectorsBean;
    private Vector<BackgroundsBean> mBackgroundsBean;
    private OnViewThemeListener mListener;

    protected MatchRunnable(final Context context, final ArrayMap<String, View> idsMap, @ParseAssetsTypeClub final int token, OnViewThemeListener listener) {
        this.mContext = context;
        this.mIdsMap = idsMap;
        this.mToken = token;
        this.mListener = listener;
    }

    @Override
    public void run() {
        if (mToken == ParseAssetsType.COLORS) {
            matchColorsBeanForId(mColorsBeans);
        }

        if (mToken == ParseAssetsType.SELECTORS) {
            matchSelectorsBeanForId(mSelectorsBean);
        }

        if (mToken == ParseAssetsType.BACKGROUNDS) {
            matchBackgroundsBeanForId(mBackgroundsBean);
        }
    }

    private void matchColorsBeanForId(final Vector<ColorsBean> beans) {
        for (final ColorsBean bean : beans) {
            if (mTerminate) {
                return;
            }
            final View view = mIdsMap.get(bean.getId());
            if (view != null && !mTerminate) {
                final int textColor = bean.getTextColor();
                final int hintColor = bean.getHintColor();
                final int backgroundColor = bean.getBackgroundColor();
                if (!mTerminate) {
                    if (textColor != 0 || hintColor != 0) {
                        onChangedFontColor(view, textColor, hintColor);
                    }
                    if (backgroundColor != 0) {
                        final Drawable colorDrawable = new ColorDrawable(backgroundColor);
                        onChangedBackground(view, colorDrawable);
                    }
                }
            }
        }
    }

    private void matchSelectorsBeanForId(final Vector<SelectorsBean> beans) {
        for (final SelectorsBean bean : beans) {
            if (mTerminate) {
                return;
            }
            final View view = mIdsMap.get(bean.getId());
            if (view != null && !mTerminate) {
                final ArrayList<StateDrawableBean> stateDrawables = bean.getStateDrawables();
                final StateListDrawable drawableSets = new StateListDrawable();
                for (final StateDrawableBean stateDrawable : stateDrawables) {
                    if (mTerminate) {
                        return;
                    }
                    final File drawFile = stateDrawable.getDrawable();
                    final Bitmap bitmap = pollingBitmap(view, drawFile);
                    final Drawable draw = new BitmapDrawable(mContext.getResources(), bitmap);
                    drawableSets.addState(stateDrawable.getStateSets(), draw);
                }
                if (!mTerminate) {
                    onChangedBackground(view, drawableSets);
                }
            }
        }
    }

    private void matchBackgroundsBeanForId(final Vector<BackgroundsBean> beans) {
        for (final BackgroundsBean bean : beans) {
            if (mTerminate) {
                return;
            }
            final View view = mIdsMap.get(bean.getId());
            if (view != null && !mTerminate) {
                final File drawFile = bean.getDrawable();
                if (!mTerminate) {
                    final Bitmap bitmap = pollingBitmap(view, drawFile);
                    final Drawable drawable = new BitmapDrawable(mContext.getResources(), bitmap);
                    onChangedBackground(view, drawable);
                }
            }
        }
    }

    private Bitmap pollingBitmap(final View view, final File drawFile) {
        do {
            final int width = view.getWidth();
            final int height = view.getHeight();
            if (width != 0 && height != 0) {
                try {
                    return getBitmap(drawFile, width, height);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(10L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        while (/*View initialization completed*/!ViewCompat.isLaidOut(view) && !mTerminate);
        return null;
    }

    private Bitmap getBitmap(final File drawFile, int width, int height) throws IOException {
        return File2Bitmap.decodeFile(drawFile, width, height);
    }

    public void setColorsBeans(final Vector<ColorsBean> beans) {
        this.mColorsBeans = beans;
    }

    public void setSelectorsBean(final Vector<SelectorsBean> beans) {
        this.mSelectorsBean = beans;
    }

    public void setBackgroundsBean(final Vector<BackgroundsBean> beans) {
        this.mBackgroundsBean = beans;
    }

    public MatchRunnable terminate() {
        mTerminate = true;
        return this;
    }

    public MatchRunnable pending() {
        mTerminate = false;
        return this;
    }

    private void onChangedBackground(final View view, final Drawable drawable) {
        if (mListener != null && !mTerminate) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (!mTerminate) {
                        mListener.onChangedBackground(view, drawable);
                    }
                }
            });
        }
    }

    private void onChangedFontColor(final View view, final int textColor, final int hintColor) {
        if (mListener != null && !mTerminate) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (!mTerminate) {
                        mListener.onChangedFontColor(view, textColor, hintColor);
                    }
                }
            });
        }
    }
}
