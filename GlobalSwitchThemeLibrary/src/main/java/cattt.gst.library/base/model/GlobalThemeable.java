package cattt.gst.library.base.model;

import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.util.ArrayMap;
import android.view.View;

import java.util.Vector;

public interface GlobalThemeable {
    ArrayMap<String, Vector<GTData>> getArrayMapOfGlobalThemeResources();

    String resId2EntryName(int resId);

    int[] getResIdPendingChangeTheme();

    boolean isEmptyResId();

    boolean isEmptyGlobalThemeResourcesMap();

    boolean isAllowLoadingGlobalThemeOfView();

    void performSwitchThemeByAsync();

    void checkAllowLoadingGlobalThemeOfView();
}
