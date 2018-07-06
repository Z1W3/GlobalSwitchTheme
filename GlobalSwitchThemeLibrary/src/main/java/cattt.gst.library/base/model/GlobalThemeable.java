package cattt.gst.library.base.model;

import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.util.ArrayMap;
import android.view.View;

import java.util.Vector;

public interface GlobalThemeable {
    ArrayMap<String, Vector<GTData>> getArrayMapOfGlobalThemeResources();

    String resId2EntryName(int resId);

    boolean isFrontPage();

    boolean isEmptyResId(int[] ids);

    boolean isEmptyGlobalThemeResourcesMap(ArrayMap<String, Vector<GTData>> map);

    boolean isAllowLoadingGlobalThemeOfView(int[] ids);

    void performToggleGlobalThemeResources(int[] ids);

    void setTextColorByInstanceofView(View view, @ColorInt int color);
    void setTextHintColorByInstanceofView(View view, @ColorInt int color);

    void setImageByInstanceofView(View view, Drawable drawable);

}
