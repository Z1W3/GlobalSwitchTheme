package cattt.gst.library.base.model.em;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import cattt.gst.library.base.model.emdata.MatchType;

@IntDef({MatchType.TYPE_BACKGROUND_DRAWABLE, MatchType.TYPE_TEXT_COLOR, MatchType.TYPE_IMAGE_DRAWABLE, MatchType.TYPE_BACKGROUND_COLOR, MatchType.TYPE_HINT_COLOR})
@Retention(RetentionPolicy.SOURCE)
public @interface MatchViewType {
}
