package cattt.gst.library.base.model;

import java.io.File;

public class StateDrawableBean {
    private int[] stateSets;
    private File drawable;

    public int[] getStateSets() {
        return stateSets;
    }

    public void setStateSets(final int[] stateSets) {
        this.stateSets = stateSets;
    }

    public File getDrawable() {
        return drawable;
    }

    public void setDrawable(final File drawable) {
        this.drawable = drawable;
    }
}
