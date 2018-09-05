package cattt.assets.theme.library.utils.zip;


import android.os.Handler;
import android.os.Message;

import java.io.File;

import cattt.assets.theme.library.utils.zip.callback.OnUnzipListener;

public class UnzipProgressMonitor {
    private static final int MSG_CODE_UNZIP_PROGRESS = 10000;
    private static final int MSG_CODE_UNZIP_COMPLETE = 10001;
    private static final int MSG_CODE_UNZIP_FAILED = 10002;

    private MainHandler handler = new MainHandler(this);
    private OnUnzipListener listener;
    private File target;
    private File out;

    protected UnzipProgressMonitor(File target, File out, OnUnzipListener listener) {
        this.target = target;
        this.out = out;
        this.listener = listener;
    }

    private void onUnzipProgress(int percentDone) {
        if (listener != null) {
            listener.onUnzipProgress(target, out, percentDone);
        }
    }

    private void onComplete() {
        if (listener != null) {
            listener.onUnzipComplete(target, out);
        }
    }

    private void onFailed(Throwable ex) {
        if (listener != null) {
            listener.onUnzipFailed(target, out, ex);
        }
    }

    protected void onUnzipProgressOfMessage(int percentDone) {
        handler.obtainMessage(MSG_CODE_UNZIP_PROGRESS, percentDone).sendToTarget();
    }

    protected void onUnzipCompleteOfMessage() {
        handler.sendEmptyMessage(MSG_CODE_UNZIP_COMPLETE);
    }

    protected void onUnzipFailedOfMessage(Throwable ex) {
        handler.obtainMessage(MSG_CODE_UNZIP_FAILED, ex).sendToTarget();
    }


    private static class MainHandler extends Handler {
        private UnzipProgressMonitor monitor;

        private MainHandler(UnzipProgressMonitor monitor) {
            this.monitor = monitor;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_CODE_UNZIP_COMPLETE:
                    monitor.onComplete();
                    break;
                case MSG_CODE_UNZIP_FAILED:
                    monitor.onFailed((Throwable) msg.obj);
                    break;
                case MSG_CODE_UNZIP_PROGRESS:
                    monitor.onUnzipProgress((int) msg.obj);
                    break;
            }

        }
    }
}
