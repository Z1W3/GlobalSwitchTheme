package cattt.gst.library.base;

import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import cattt.gst.library.base.model.em.Visibility;
import cattt.gst.library.base.model.emdata.State;
import cattt.gst.library.base.model.emdata.ViewType;
import cattt.gst.library.utils.bitmap.File2Bitmap;

abstract public class BaseGlobalThemeFragment extends Fragment implements GlobalThemeable, OnGlobalThemeListener {
    @PageState
    private int state = State.STATE_BACK;

    //TODO private Unbinder unbinder;


    /**
     * 获取布局ID
     */
    protected abstract int getContentViewLayoutID();

    /**
     * 界面初始化
     */
    protected abstract void init();

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
            final View view = getView().findViewById(id);
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
     * 界面初始化后
     */
    protected void afterInit() {
        GlobalThemeMonitor.get().addOnGlobalThemeListener(this);
    }

    /**
     * onDestroy之前准备
     */
    protected void beforeOnDestroyView() {
        GlobalThemeMonitor.get().removeOnGlobalThemeListener(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getContentViewLayoutID() != 0) {
            return inflater.inflate(getContentViewLayoutID(), container, false);
        } else {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        state = State.STATE_FRONT;
        //TODO unbinder = ButterKnife.bind(this, view);
        init();
        afterInit();
    }

    @Override
    public void onDestroyView() {
        state = State.STATE_BACK;
        beforeOnDestroyView();
        super.onDestroyView();
        //TODO unbinder.unbind();
    }

    protected void startObjectAnimator(@NonNull ImageView imageButton) {
        Drawable draw = imageButton.getDrawable();
        if (draw instanceof Animatable) {
            ((Animatable) draw).start();
        }
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
