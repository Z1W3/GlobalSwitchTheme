package cattt.gst.library.base.model.em;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import cattt.gst.library.base.model.emdata.WindowFocusState;


@IntDef({WindowFocusState.STATE_FRONT, WindowFocusState.STATE_BACK})
@Retention(RetentionPolicy.SOURCE)
public @interface IWindowFocusState {
}
