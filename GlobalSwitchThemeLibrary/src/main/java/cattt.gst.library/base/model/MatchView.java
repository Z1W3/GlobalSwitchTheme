package cattt.gst.library.base.model;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.view.View;

import cattt.gst.library.base.model.em.Visibility;

public interface MatchView {

    void setTextColorByInstanceofView(View view, @ColorInt int color);
    void setTextHintColorByInstanceofView(View view, @ColorInt int color);
    void setBackground(View view, BitmapDrawable drawable);
    void setBackgroundColor(View view, @ColorInt int color);
    void setImageByInstanceofView(View view, BitmapDrawable drawable);

    Handler getMatchingViewHandler();

    void setMatchViewVisibility(@Visibility int visibility);
}
