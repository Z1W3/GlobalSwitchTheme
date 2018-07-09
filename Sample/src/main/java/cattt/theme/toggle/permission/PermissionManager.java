package cattt.theme.toggle.permission;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class PermissionManager {
    /**
     * 自动弹窗激活权限
     */
    public static final int AT_CODE_REQUEST_PERMISSION_CHANNEL = 520;
    /**
     * 手动前往激活权限
     */
    public static final int PT_CODE_REQUEST_PERMISSION_CHANNEL = 521;

    private Activity activity;
    private String[] permissions;

    public PermissionManager(@NonNull Activity activity, String[] permissions) {
        this.activity = activity;
        this.permissions = permissions;
    }

    /**
     * if {@code true}, Need to apply for activation permission.
     * if {@code false}, has been fully activated permission.
     * @return
     */
    public boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for(String permission : permissions){
                if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(activity, permission)) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 通过主动弹窗激活权限
     */
    public void startRequestPermissionByAT() {
        ActivityCompat.requestPermissions(activity, permissions, AT_CODE_REQUEST_PERMISSION_CHANNEL);
    }

    /**
     * 通过手动跳转APP设置页面激活权限
     */
    public void startRequestPermissionByPT() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivityForResult(intent, PT_CODE_REQUEST_PERMISSION_CHANNEL);
    }

}
