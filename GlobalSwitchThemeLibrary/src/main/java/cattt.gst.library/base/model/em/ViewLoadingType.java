package cattt.gst.library.base.model.em;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import cattt.gst.library.base.model.emdata.ViewType;

@IntDef({ViewType.TYPE_BACKGROUND_DRAWABLE, ViewType.TYPE_TEXT_COLOR, ViewType.TYPE_IMAGE_DRAWABLE, ViewType.TYPE_BACKGROUND_COLOR, ViewType.TYPE_HINT_COLOR})
@Retention(RetentionPolicy.SOURCE)
public @interface ViewLoadingType {
}
