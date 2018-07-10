package cattt.gst.library.base.model;

import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.view.View;


public interface MatchView {
    void setTextColorByInstanceofView(View view, @ColorInt int color);
    void setTextHintColorByInstanceofView(View view, @ColorInt int color);
    void setBackground(View view, BitmapDrawable drawable);
    void setBackgroundColor(View view, @ColorInt int color);
    void setImageByInstanceofView(View view, BitmapDrawable drawable);

    Handler getMatchingViewHandler();
}
