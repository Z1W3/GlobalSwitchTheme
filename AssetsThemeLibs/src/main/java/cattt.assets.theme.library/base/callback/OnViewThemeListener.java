package cattt.assets.theme.library.base.callback;

import android.graphics.drawable.Drawable;
import android.view.View;

public interface OnViewThemeListener {

    void onChangedFontColor(View view, int textColor, int hintColor);

    void onChangedBackground(View view, Drawable drawable);
}
