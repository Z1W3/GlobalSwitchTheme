package cattt.theme.toggle;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import cattt.assets.theme.library.base.AssetsHelper;

public class DemoActivity1 extends AppCompatActivity {
    private AssetsHelper mHelper;
    private int[] mIds = new int[]{
            R.id.toolbar_title,
            R.id.wallpaper1,
            R.id.image1,
            R.id.image2,
            R.id.image3,
            R.id.image4,
            R.id.text,
            R.id.app_compat_text,
            R.id.edit_text,
            R.id.app_compat_edit,
            R.id.button,
            R.id.app_compat_button
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo1);
        mHelper = new AssetsHelper(this, mIds);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_title));
    }

    @Override
    protected void onDestroy() {
        mHelper.destroyAssetsHelper();
        super.onDestroy();
    }
}