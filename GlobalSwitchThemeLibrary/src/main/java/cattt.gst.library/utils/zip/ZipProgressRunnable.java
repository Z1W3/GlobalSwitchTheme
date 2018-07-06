package cattt.gst.library.utils.zip;


import net.lingala.zip4j.progress.ProgressMonitor;
public class ZipProgressRunnable implements Runnable {
    private ProgressMonitor mProgressMonitor;
    private UnzipProgressMonitor mUnzipProgressMonitor;
    private boolean terminate;

    protected ZipProgressRunnable(ProgressMonitor mProgressMonitor, UnzipProgressMonitor mUnzipProgressMonitor) {
        this.mProgressMonitor = mProgressMonitor;
        this.mUnzipProgressMonitor = mUnzipProgressMonitor;
    }

    private void sendUnzipProgress(int percentDone) {
        mUnzipProgressMonitor.onUnzipProgressOfMessage(percentDone);
    }

    private void sendUnzipComplete() {
        mUnzipProgressMonitor.onUnzipCompleteOfMessage();
    }

    private void sendUnzipFailed(Throwable ex) {
        mUnzipProgressMonitor.onUnzipFailedOfMessage(ex);
    }

    @Override
    public void run() {
        try {
            int percentDone;
            Long workCompleted = 0L;
            while (!terminate && mProgressMonitor.getTotalWork() > 0L) {
                workCompleted += mProgressMonitor.getWorkCompleted();
                percentDone = (int) (workCompleted * 100L / mProgressMonitor.getTotalWork());
                if (percentDone > 100) {
                    percentDone = 100;
                    terminate = true;
                }
                sendUnzipProgress(percentDone);
                Thread.sleep(100L);
            }
            sendUnzipComplete();
        } catch (Exception ex) {
            sendUnzipFailed(ex);
        }
    }
}
