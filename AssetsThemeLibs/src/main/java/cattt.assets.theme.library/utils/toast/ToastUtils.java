package cattt.assets.theme.library.utils.toast;

import android.content.Context;
import android.support.annotation.IntDef;
import android.widget.Toast;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ToastUtils {
    private static Toast mToast;

    @IntDef({Toast.LENGTH_SHORT, Toast.LENGTH_LONG})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Duration {}


    public static void show(Context context, String content, @Duration int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(context, content, duration);
        } else {
            mToast.setText(content);
            mToast.setDuration(duration);
        }
        mToast.show();
    }
}
