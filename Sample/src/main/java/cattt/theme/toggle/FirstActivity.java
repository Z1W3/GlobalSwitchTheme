package cattt.theme.toggle;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class FirstActivity extends AppCompatActivity {
    private int[] a = new int[]{
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_title));
    }
}