package cattt.gst.library.base.model;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.view.View;

public interface MatchView {

    void setTextColorByInstanceofView(int resId, @ColorInt int color);
    void setTextHintColorByInstanceofView(int resId, @ColorInt int color);
    void setBackground(int resId, BitmapDrawable drawable);
    void setBackgroundColor(int resId, @ColorInt int color);
    void setImageByInstanceofView(int resId, BitmapDrawable drawable);

    Handler getMatchingViewHandler();
}
