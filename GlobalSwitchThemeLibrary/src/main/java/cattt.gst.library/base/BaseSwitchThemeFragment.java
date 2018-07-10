package cattt.gst.library.base;

import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import cattt.gst.library.base.model.MatchView;
import cattt.gst.library.base.model.em.IWindowFocusState;
import cattt.gst.library.base.model.emdata.WindowFocusState;

abstract public class BaseSwitchThemeFragment extends BaseFragment implements MatchView {
    private MatchViewHandler handler = new MatchViewHandler(this);
    private GlobalThemeWorker mWorker = new GlobalThemeWorker(this);

    @IWindowFocusState
    private int state = WindowFocusState.STATE_BACK;

    /**
     * @return new int[] { R.id.xxx }
     */
    abstract protected int[] getViewResourcesPendingChangeTheme();

    /**
     * 判断是否是前端页面
     *
     * @return
     */
    public boolean isWindowFocus() {
        return this.state == WindowFocusState.STATE_FRONT ? true : false;
    }

    @Override
    public void setBackground(int resId, BitmapDrawable drawable) {
        getView().findViewById(resId).setBackground(drawable);
    }

    @Override
    public void setBackgroundColor(int resId, @ColorInt int color) {
        getView().findViewById(resId).setBackgroundColor(color);
    }

    @Override
    public void setTextHintColorByInstanceofView(int resId, int color) {
        View view = getView().findViewById(resId);
        if (view instanceof EditText) {
            ((EditText) view).setHintTextColor(color);
        } else if (view instanceof AppCompatEditText) {
            ((AppCompatEditText) view).setHintTextColor(color);
        }
    }

    @Override
    public void setTextColorByInstanceofView(int resId, int color) {
        View view = getView().findViewById(resId);
        if (view instanceof TextView) {
            ((TextView) view).setTextColor(color);
        } else if (view instanceof EditText) {
            ((EditText) view).setTextColor(color);
        } else if (view instanceof Button) {
            ((Button) view).setTextColor(color);
        } else if (view instanceof AppCompatTextView) {
            ((AppCompatTextView) view).setTextColor(color);
        } else if (view instanceof AppCompatButton) {
            ((AppCompatButton) view).setTextColor(color);
        } else if (view instanceof AppCompatEditText) {
            ((AppCompatEditText) view).setTextColor(color);
        } else if (view instanceof Toolbar) {
            ((Toolbar) view).setTitleTextColor(color);
        }
    }

    @Override
    public void setImageByInstanceofView(int resId, BitmapDrawable drawable) {
        View view = getView().findViewById(resId);
        if (view instanceof ImageView) {
            ((ImageView) view).setImageDrawable(drawable);
        } else if (view instanceof ImageButton) {
            ((ImageButton) view).setImageDrawable(drawable);
        } else if (view instanceof AppCompatImageView) {
            ((AppCompatImageView) view).setImageDrawable(drawable);
        } else if (view instanceof AppCompatImageButton) {
            ((AppCompatImageButton) view).setImageDrawable(drawable);
        }
    }

    @Override
    public Handler getMatchingViewHandler() {
        return handler;
    }

    @Override
    protected void onInit() {
        super.onInit();
        state = WindowFocusState.STATE_FRONT;
    }

    @Override
    protected void onAfterInit() {
        super.onAfterInit();
        mWorker.register();
        mWorker.performSwitchThemeByAsync();
    }

    @Override
    protected void onBeforeDestroyView() {
        super.onBeforeDestroyView();
        state = WindowFocusState.STATE_BACK;
        mWorker.unregister();
    }
}
