package cattt.gst.library.base;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.ArrayMap;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import java.io.IOException;
import java.util.Vector;

import cattt.gst.library.base.callback.OnGlobalThemeListener;
import cattt.gst.library.base.model.GTData;
import cattt.gst.library.base.model.GlobalThemeable;
import cattt.gst.library.base.model.em.PageState;
import cattt.gst.library.base.model.emdata.State;
import cattt.gst.library.base.model.emdata.ViewType;
import cattt.gst.library.base.model.em.Visibility;
import cattt.gst.library.utils.bitmap.File2Bitmap;

abstract public class BaseGlobalThemeActivity extends AppCompatActivity implements GlobalThemeable, OnGlobalThemeListener {

    @PageState
    private int state = State.STATE_BACK;

    /**
     * 获取布局ID
     *
     * @return 布局id
     */
    abstract protected int getContentViewLayoutID();


    /**
     * @return new int[] { R.id.xxx }
     */
    abstract protected int[] getViewResourcesPendingChangeTheme();

    /**
     * 判断ResourceId是否是空
     *
     * @param ids
     * @return
     */
    @Override
    public boolean isEmptyResId(int[] ids) {
        return !(ids != null && ids.length > 0);
    }

    /**
     * 判断是否是前端页面
     *
     * @return
     */
    @Override
    public boolean isFrontPage() {
        return this.state == State.STATE_FRONT ? true : false;
    }

    /**
     * View是否允许加载
     *
     * @param ids
     * @return
     */
    @Override
    public boolean isAllowLoadingGlobalThemeOfView(int[] ids) {
        return isFrontPage() && !isEmptyResId(ids);
    }

    /**
     * 判断全局的主题内容是否为空
     *
     * @param map
     * @return
     */
    @Override
    public boolean isEmptyGlobalThemeResourcesMap(ArrayMap<String, Vector<GTData>> map) {
        return !(map != null && map.size() > 0);
    }

    /**
     * 全局主题更换事件
     *
     * @param map
     */
    @Override
    public void onGlobalThemeResources(ArrayMap<String, Vector<GTData>> map) {
        if (isEmptyGlobalThemeResourcesMap(map)) {
            return;
        }
        getArrayMapOfGlobalThemeResources().clear();
        getArrayMapOfGlobalThemeResources().putAll(map);
        if (isAllowLoadingGlobalThemeOfView(getViewResourcesPendingChangeTheme())) {
            performToggleGlobalThemeResources(getViewResourcesPendingChangeTheme());
        }
    }

    @Override
    public ArrayMap<String, Vector<GTData>> getArrayMapOfGlobalThemeResources() {
        return SingleGlobalThemeArrayMap.get().getGlobalThemeResourcesMap();
    }

    /**
     * 执行替换全局主题
     *
     * @param ids
     */
    @Override
    public void performToggleGlobalThemeResources(int[] ids) {
        for (int id : ids) {
            String name = resId2EntryName(id);
            final Vector<GTData> datas = getArrayMapOfGlobalThemeResources().get(name);
            if (!(datas != null && datas.size() > 0)) {
                continue;
            }
            final View view = findViewById(id);
            for (GTData mGTData : datas) {
                switch (mGTData.getType()) {
                    case ViewType.TYPE_BACKGROUND_DRAWABLE:
                        try {
                            view.setBackground(new BitmapDrawable(getResources(), File2Bitmap.decodeFile(mGTData.getContent(), view)));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case ViewType.TYPE_BACKGROUND_COLOR:
                        view.setBackgroundColor(Color.parseColor(mGTData.getContent()));
                        break;
                    case ViewType.TYPE_IMAGE:
                        try {
                            this.setImageByInstanceofView(view, new BitmapDrawable(getResources(), File2Bitmap.decodeFile(mGTData.getContent(), view)));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case ViewType.TYPE_TEXT_COLOR:
                        this.setTextColorByInstanceofView(view, Color.parseColor(mGTData.getContent()));
                        break;
                    case ViewType.TYPE_HINT_COLOR:
                        this.setTextHintColorByInstanceofView(view, Color.parseColor(mGTData.getContent()));
                        break;
                }
            }
        }
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
    public void setImageByInstanceofView(View view, Drawable drawable) {
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

    /**
     * 将资源ID转换成资源昵称
     *
     * @param resId
     * @return
     */
    @Override
    public String resId2EntryName(@IdRes int resId) {
        if (!"id".equals(getResources().getResourceTypeName(resId))) {
            throw new IllegalArgumentException(String.format("Unable to convert resId[%d], Need resource type is R.id.x.", resId));
        }
        return getResources().getResourceEntryName(resId);
    }

    /**
     * 界面初始化前期准备
     */
    protected void beforeInit() {

    }

    /**
     * 界面初始化后
     */
    protected void afterInit() {
        GlobalThemeMonitor.get().addOnGlobalThemeListener(this);
    }

    /**
     * onDestroy之前准备
     */
    protected void beforeOnDestroy() {
        GlobalThemeMonitor.get().removeOnGlobalThemeListener(this);
    }

    /**
     * 初始化布局以及View控件
     */
    abstract protected void initView(Bundle savedInstanceState);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSystemBarTint();
        beforeInit();
        if (getContentViewLayoutID() != 0) {
            setContentView(getContentViewLayoutID());
            initView(savedInstanceState);
            afterInit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        state = State.STATE_FRONT;
        //TODO 友盟日志 MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        state = State.STATE_BACK;
        //TODO 友盟日志 MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        beforeOnDestroy();
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
}
