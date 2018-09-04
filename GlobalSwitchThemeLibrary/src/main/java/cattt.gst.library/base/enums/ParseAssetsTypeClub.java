package cattt.gst.library.base.enums;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({
        ParseAssetsType.COLORS,
        ParseAssetsType.SELECTORS,
        ParseAssetsType.BACKGROUNDS
})
@Retention(RetentionPolicy.SOURCE)
public @interface ParseAssetsTypeClub {}
