package cattt.gst.library.base.model;

import android.graphics.drawable.BitmapDrawable;
import android.view.View;

public class MatchViewData{
    private int resId;
    private int color;
    private BitmapDrawable drawable;

    public MatchViewData(int resId, int color){
        this.resId = resId;
        this.color = color;
    }

    public MatchViewData(int resId, BitmapDrawable drawable){
        this.resId = resId;
        this.drawable = drawable;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public BitmapDrawable getDrawable() {
        return drawable;
    }

    public void setDrawable(BitmapDrawable drawable) {
        this.drawable = drawable;
    }
}
