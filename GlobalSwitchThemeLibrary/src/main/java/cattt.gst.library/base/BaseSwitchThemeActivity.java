package cattt.gst.library.base;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import cattt.gst.library.utils.logger.Log;

abstract public class BaseSwitchThemeActivity extends BaseAppCompatActivity implements MatchView {
    private static Log logger = Log.getLogger(BaseSwitchThemeActivity.class);
    private MatchViewHandler handler = new MatchViewHandler(this);
    private GlobalThemeWorker mWorker = new GlobalThemeWorker(this);

    @IWindowFocusState
    private int state = WindowFocusState.STATE_BACK;


    /**
     * 判断是否是前端页面
     *
     * @return
     */
    public boolean isWindowFocus() {
        return this.state == WindowFocusState.STATE_FRONT ? true : false;
    }

    /**
     * @return new int[] { R.id.xxx }
     */
    abstract protected int[] getViewResourcesPendingChangeTheme();


    @Override
    public void setBackground(View view, BitmapDrawable drawable) {
        view.setBackground(drawable);
    }

    @Override
    public void setBackgroundColor(View view, @ColorInt int color) {
        view.setBackgroundColor(color);
    }


    @Override
    public void setTextHintColorByInstanceofView(View view, int color) {
        if (view instanceof EditText) {
            ((EditText) view).setHintTextColor(color);
        } else if (view instanceof AppCompatEditText) {
            ((AppCompatEditText) view).setHintTextColor(color);
        }
    }

    @Override
    public void setTextColorByInstanceofView(View view, int color) {
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
    public void setImageByInstanceofView(View view, BitmapDrawable drawable) {
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
    protected void onInitView(Bundle savedInstanceState) {
        super.onInitView(savedInstanceState);
        state = WindowFocusState.STATE_FRONT;
    }

    @Override
    protected void onAfterInitView() {
        super.onAfterInitView();
        mWorker.register();
        mWorker.performSwitchThemeByAsync();
    }

    @Override
    protected void onBeforeDestroy() {
        super.onBeforeDestroy();
        state = WindowFocusState.STATE_BACK;
        mWorker.unregister();
    }
}
