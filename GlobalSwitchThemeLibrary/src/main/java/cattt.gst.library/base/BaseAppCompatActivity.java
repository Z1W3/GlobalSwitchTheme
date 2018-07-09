package cattt.gst.library.base;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import cattt.gst.library.base.model.em.Visibility;

abstract public class BaseAppCompatActivity extends AppCompatActivity {

    /**
     * 获取布局ID
     *
     * @return 布局id
     */
    abstract protected int getContentViewLayoutID();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        onBeforeInitView();
        if (getContentViewLayoutID() != 0) {
            setContentView(getContentViewLayoutID());
            onInitView(savedInstanceState);
            onAfterInitView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //TODO 友盟日志 MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //TODO 友盟日志 MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        onBeforeDestroy();
        super.onDestroy();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        //TODO  ButterKnife.bind(this);
    }

    /**
     * 子类可以重写决定是否使用透明状态栏
     */
    protected boolean translucentStatusBar() {
        return true;
    }

    /**
     * 设置状态栏颜色
     */
    protected void initSystemBarTint() {
        Window window = getWindow();
        if (translucentStatusBar()) {
            // 设置状态栏全透明
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
            }
            return;
        }
        // 沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0以上使用原生方法
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(setStatusBarColor());
        }
    }

    /**
     * 子类可以重写改变状态栏颜色
     */
    protected int setStatusBarColor() {
        return android.R.color.holo_blue_dark;
    }

    public void setViewVisible(final View view, @Visibility final int visible, long delayMillis) {
        if (view != null) {
            view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (view != null) {
                        view.setVisibility(visible);
                    }
                }
            }, delayMillis);
        }
    }

    protected void onBeforeInitView() {

    }

    /**
     * 初始化布局以及View控件
     */
    protected void onInitView(Bundle savedInstanceState){

    }


    protected void onAfterInitView() {

    }

    protected void onBeforeDestroy() {

    }
}
