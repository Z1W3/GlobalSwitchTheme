package cattt.gst.library.base.model;

import android.graphics.drawable.BitmapDrawable;
import android.view.View;

public class MatchViewData {
    private View view;
    private int color;
    private BitmapDrawable drawable;

    public MatchViewData(View view, int color) {
        this.view = view;
        this.color = color;
    }

    public MatchViewData(View view, BitmapDrawable drawable) {
        this.view = view;
        this.drawable = drawable;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
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
