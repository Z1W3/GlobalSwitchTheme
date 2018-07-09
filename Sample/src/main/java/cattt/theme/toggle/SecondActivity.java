package cattt.theme.toggle;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import cattt.gst.library.base.BaseSwitchThemeActivity;

public class SecondActivity extends BaseSwitchThemeActivity {


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_second;
    }

    @Override
    protected int[] getViewResourcesPendingChangeTheme() {
        return new int[]{
                R.id.wallpaper1,
                R.id.image1,
                R.id.image2,
                R.id.image3,
                R.id.image4,
                R.id.toolbar_title,
                R.id.text,
                R.id.app_compat_text,
                R.id.edit_text,
                R.id.app_compat_edit,
                R.id.button,
                R.id.app_compat_button
        };
    }

    @Override
    protected void onInitView(Bundle savedInstanceState) {
        super.onInitView(savedInstanceState);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_title));
    }

}