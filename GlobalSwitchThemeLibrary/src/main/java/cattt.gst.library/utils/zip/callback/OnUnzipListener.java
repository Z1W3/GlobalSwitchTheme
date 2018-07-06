package cattt.gst.library.utils.zip.callback;

import java.io.File;

public interface OnUnzipListener {
    void onUnzipProgress(File target, File out, int percentDone);
    void onUnzipComplete(File target, File out);
    void onUnzipFailed(File target, File out, Throwable ex);
}
