package cattt.gst.library.base.model.em;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import cattt.gst.library.base.model.emdata.State;

@IntDef({State.STATE_FRONT, State.STATE_BACK})
@Retention(RetentionPolicy.CLASS)
public @interface PageState {}
