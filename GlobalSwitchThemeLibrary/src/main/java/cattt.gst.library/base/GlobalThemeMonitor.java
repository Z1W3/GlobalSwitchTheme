package cattt.gst.library.base;

import android.os.Handler;
import android.os.Message;

import java.util.Vector;

import cattt.gst.library.base.callback.OnGlobalThemeListener;

public class GlobalThemeMonitor {
    private static final int MSG_CODE_SWITCH = 10000;

    private Vector<OnGlobalThemeListener> mListeners = new Vector<>();

    private MainHandler handler = new MainHandler(this);

    protected GlobalThemeMonitor() {
    }

    protected void addOnGlobalThemeListener(OnGlobalThemeListener listener) {
        if (listener != null) {
            mListeners.add(listener);
        }
    }

    protected void removeOnGlobalThemeListener(OnGlobalThemeListener listener) {
        if (listener != null) {
            mListeners.remove(listener);
        }
    }

    protected void onSwitchResourcesOfMessage() {
        handler.obtainMessage(MSG_CODE_SWITCH).sendToTarget();
    }


    private void onSwitchResources() {
        if (mListeners != null) {
            for (OnGlobalThemeListener listener : mListeners) {
                listener.onSwitchResources();
            }
        }
    }

    private static class MainHandler extends Handler {
        private GlobalThemeMonitor monitor;

        private MainHandler(GlobalThemeMonitor monitor) {
            this.monitor = monitor;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_CODE_SWITCH:
                    monitor.onSwitchResources();
                    break;
            }

        }
    }

    private static final class Helper {
        private static final GlobalThemeMonitor INSTANCE = new GlobalThemeMonitor();
    }

    protected static GlobalThemeMonitor get(){
        return Helper.INSTANCE;
    }
}
