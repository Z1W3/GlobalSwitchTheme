package cattt.gst.library.base;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.Vector;

import cattt.gst.library.base.callback.OnGlobalThemeListener;

public class GlobalThemeMonitor {
    private Vector<OnGlobalThemeListener> mListeners = new Vector<>();

    private MainHandler mHandler = new MainHandler(this);

    protected GlobalThemeMonitor() {
    }

    protected void addOnGlobalThemeListener(final OnGlobalThemeListener listener) {
        if (listener == null) {
            return;
        }
        obtainMessageOfHandler(MainHandler.MSG_CODE_ADD_LISTENER, listener);
    }

    protected void removeOnGlobalThemeListener(final OnGlobalThemeListener listener) {
        if (listener == null) {
            return;
        }
        obtainMessageOfHandler(MainHandler.MSG_CODE_REMOVE_LISTENER, listener);
    }

    protected void onSwitchResourcesOfMessage() {
        obtainMessageOfHandler(MainHandler.MSG_CODE_SWITCH, null);
    }

    private void onSwitchResources() {
        if (mListeners == null) {
            return;
        }
        for (OnGlobalThemeListener listener : mListeners) {
            listener.onSwitchResources();
        }
    }

    private void obtainMessageOfHandler(int what, Object obj) {
        mHandler.obtainMessage(what, obj).sendToTarget();
    }

    private static class MainHandler extends Handler {
        private static final int MSG_CODE_SWITCH = 10000;
        private static final int MSG_CODE_ADD_LISTENER = 10001;
        private static final int MSG_CODE_REMOVE_LISTENER = 10002;
        private GlobalThemeMonitor mRoot;

        private MainHandler(GlobalThemeMonitor root) {
            super(Looper.getMainLooper());
            this.mRoot = root;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_CODE_SWITCH:
                    mRoot.onSwitchResources();
                    break;
                case MSG_CODE_ADD_LISTENER:
                    mRoot.mListeners.add((OnGlobalThemeListener) msg.obj);
                    break;
                case MSG_CODE_REMOVE_LISTENER:
                    mRoot.mListeners.remove(msg.obj);
                    break;
            }

        }
    }

    private static final class Helper {
        private static final GlobalThemeMonitor INSTANCE = new GlobalThemeMonitor();
    }

    protected static GlobalThemeMonitor get() {
        return Helper.INSTANCE;
    }
}
