package cattt.gst.library.base;

import android.os.Handler;
import android.os.Message;
import android.util.ArrayMap;

import java.util.Vector;

import cattt.gst.library.base.callback.OnGlobalThemeListener;
import cattt.gst.library.base.model.GTData;

public class GlobalThemeMonitor {
    private static final int MSG_CODE_RESOURCES = 10000;

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

    protected void onGlobalThemeResourcesOfMessage(ArrayMap<String, Vector<GTData>> map) {
        handler.obtainMessage(MSG_CODE_RESOURCES, map).sendToTarget();
    }


    private void onGlobalThemeResources(ArrayMap<String, Vector<GTData>> map) {
        if (mListeners != null) {
            for (OnGlobalThemeListener listener : mListeners) {
                listener.onGlobalThemeResources(map);
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
                case MSG_CODE_RESOURCES:
                    monitor.onGlobalThemeResources((ArrayMap<String, Vector<GTData>>) msg.obj);
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
