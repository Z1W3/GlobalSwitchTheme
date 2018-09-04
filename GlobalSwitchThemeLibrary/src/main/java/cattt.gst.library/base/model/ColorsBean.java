package cattt.gst.library.base.model;

public class ColorsBean {
    private String id;
    private int textColor;
    private int backgroundColor;
    private int hintColor;

    public String getId() {
        return id;
    }

    public void setId(final String mId) {
        this.id = mId;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(final int color) {
        this.textColor = color;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(final int color) {
        this.backgroundColor = color;
    }

    public int getHintColor() {
        return hintColor;
    }

    public void setHintColor(final int color) {
        this.hintColor = color;
    }
}
