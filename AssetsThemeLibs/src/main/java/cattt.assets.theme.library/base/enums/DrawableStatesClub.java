package cattt.assets.theme.library.base.enums;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@StringDef({
        DrawableStates.NORMAL,
        DrawableStates.STATE_ABOVE_ANCHOR,
        DrawableStates.STATE_ACCELERATED,
        DrawableStates.STATE_ACTIVATED,
        DrawableStates.STATE_ACTIVE,
        DrawableStates.STATE_CHECKABLE,
        DrawableStates.STATE_CHECKED,
        DrawableStates.STATE_DRAG_CAN_ACCEPT,
        DrawableStates.STATE_DRAG_HOVERED,
        DrawableStates.STATE_EMPTY,
        DrawableStates.STATE_ENABLED,
        DrawableStates.STATE_EXPANDED,
        DrawableStates.STATE_FIRST,
        DrawableStates.STATE_FOCUSED,
        DrawableStates.STATE_HOVERED,
        DrawableStates.STATE_LAST,
        DrawableStates.STATE_LONG_PRESSABLE,
        DrawableStates.STATE_MIDDLE,
        DrawableStates.STATE_MULTILINE,
        DrawableStates.STATE_PRESSED,
        DrawableStates.STATE_SELECTED,
        DrawableStates.STATE_SINGLE,
        DrawableStates.STATE_WINDOW_FOCUSED})
@Retention(RetentionPolicy.SOURCE)
public @interface DrawableStatesClub {
}
