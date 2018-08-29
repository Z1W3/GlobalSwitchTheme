package cattt.gst.library.base.model.enums;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({MatchType.TYPE_BACKGROUND_DRAWABLE, MatchType.TYPE_TEXT_COLOR, MatchType.TYPE_IMAGE_DRAWABLE, MatchType.TYPE_BACKGROUND_COLOR, MatchType.TYPE_HINT_COLOR})
@Retention(RetentionPolicy.SOURCE)
public @interface MatchTypeClub {
}
