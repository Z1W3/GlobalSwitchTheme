package cattt.assets.theme.library.base.parse;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Vector;

import cattt.assets.theme.library.base.callback.OnAssetsListener;
import cattt.assets.theme.library.base.model.BackgroundsBean;
import cattt.assets.theme.library.base.model.ColorsBean;
import cattt.assets.theme.library.base.model.SelectorsBean;

public class ParseAssetsMonitor {
    private Handler mHandler = new Handler(Looper.getMainLooper());

    private ArrayList<OnAssetsListener> mListeners = new ArrayList<>();

    private ParseAssetsMonitor() {}

    public void addOnAssetsListener(@NonNull final OnAssetsListener listener) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (listener != null) {
                    mListeners.add(listener);
                }
            }
        });
    }

    public void removeOnAssetsListener(@NonNull final OnAssetsListener listener) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (listener != null) {
                    mListeners.remove(listener);
                }
            }
        });
    }

    public void onColorAssets(final Vector<ColorsBean> beans) {
        if (beans == null) {
            return;
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (beans == null) {
                    return;
                }
                for (OnAssetsListener listener : mListeners) {
                    listener.onColorAssets(beans);
                }
            }
        });
    }

    public void onSelectorAssets(final Vector<SelectorsBean> beans) {
        if (beans == null) {
            return;
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (beans == null) {
                    return;
                }
                for (OnAssetsListener listener : mListeners) {
                    listener.onSelectorAssets(beans);
                }
            }
        });
    }

    public void onBackgroundAssets(final Vector<BackgroundsBean> beans) {
        if (beans == null) {
            return;
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (beans == null) {
                    return;
                }
                for (OnAssetsListener listener : mListeners) {
                    listener.onBackgroundAssets(beans);
                }
            }
        });
    }

    public static ParseAssetsMonitor get() {
        return Helper.INSTANCE;
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

    private static final class Helper {
        private static final ParseAssetsMonitor INSTANCE = new ParseAssetsMonitor();
    }

}
