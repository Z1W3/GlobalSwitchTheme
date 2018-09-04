package cattt.gst.library.base;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import cattt.gst.library.base.enums.Visibility;

abstract public class BaseFragment extends Fragment {


    //TODO private Unbinder unbinder;


    /**
     * 获取布局ID
     */
    protected abstract int getContentViewLayoutID();


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
        //TODO unbinder = ButterKnife.bind(this, view);
        onBeforeInit();
        onInit();
        onAfterInit();
    }

    @Override
    public void onDestroyView() {
        onBeforeDestroyView();
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


    protected void onBeforeInit() {

    }

    /**
     * 界面初始化
     */
    protected void onInit(){

    }

    protected void onAfterInit() {

    }

    protected void onBeforeDestroyView() {

    }
}
