package cattt.gst.library.base.model;

import java.util.ArrayList;

public class SelectorsBean {
    private String id;
    private ArrayList<StateDrawableBean> stateDrawables;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public ArrayList<StateDrawableBean> getStateDrawables() {
        return stateDrawables;
    }

    public void setStateDrawables(final ArrayList<StateDrawableBean> stateDrawables) {
        this.stateDrawables = stateDrawables;
    }
}
