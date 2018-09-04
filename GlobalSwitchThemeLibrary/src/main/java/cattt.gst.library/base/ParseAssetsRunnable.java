package cattt.gst.library.base;


import java.io.File;


abstract public class ParseAssetsRunnable implements Runnable {

    private File mTarget;

    public ParseAssetsRunnable(File target) {
        this.mTarget = target;
    }

    @Override
    public void run() {

    }
}
