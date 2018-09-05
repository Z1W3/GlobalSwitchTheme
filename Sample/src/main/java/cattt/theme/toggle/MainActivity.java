package cattt.theme.toggle;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import cattt.assets.theme.library.base.AssetsHelper;
import cattt.assets.theme.library.base.parse.ParseAssetsHelper;
import cattt.assets.theme.library.utils.file.FileSafeCode;
import cattt.assets.theme.library.utils.toast.ToastUtils;
import cattt.assets.theme.library.utils.zip.ZipArchive;
import cattt.assets.theme.library.utils.zip.callback.OnUnzipListener;
import cattt.theme.toggle.utils.download.DownloadUtils;
import cattt.theme.toggle.utils.permission.PermissionManager;

public class MainActivity extends AppCompatActivity {
    private DownloadUtils mDownloadUtils;
    private File resourcesZipFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/resources.zip");
    private File outDir;
    // 要申请的权限
    private static final String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET
    };

    private PermissionManager mPermissionManager;
    private AssetsHelper mHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mHelper = new AssetsHelper(this, R.id.wallpaper2, R.id.toolbar_title, R.id.button1, R.id.button2);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_title));
        mDownloadUtils = new DownloadUtils(getApplicationContext());
        mDownloadUtils.registerReceiver(receiver);
        outDir = new File(getApplicationContext().getFilesDir().getAbsolutePath());
        mPermissionManager = new PermissionManager(this, PERMISSIONS);
        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DemoActivity1.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DemoActivity2.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        if (mPermissionManager.checkPermission()) {
            mPermissionManager.startRequestPermissionByAT();
        }
    }

    @Override
    protected void onDestroy() {
        mHelper.destroyAssetsHelper();
        mDownloadUtils.unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_download) {
            if (!resourcesZipFile.exists()) {
                ToastUtils.show(this, "正在下载", Toast.LENGTH_SHORT);
//                mDownloadUtils.downloadFile("https://raw.githubusercontent.com/LuckWei/GlobalSwitchTheme/master/sampleZipResources/resources.zip", "resources.zip");
                mDownloadUtils.downloadFile("https://raw.githubusercontent.com/LuckWei/GlobalSwitchTheme/add_selector_match_image_function/sampleZipResources/resources.zip", "resources.zip");
            } else {
                ToastUtils.show(this, "文件已经存在", Toast.LENGTH_SHORT);
            }
            return true;
        }
        if (id == R.id.action_switch) {
            try {
                new ZipArchive().unzip(resourcesZipFile.getPath(), outDir.getPath(), new OnUnzipListener() {
                    @Override
                    public void onUnzipProgress(File target, File out, int percentDone) {
                        ToastUtils.show(getApplicationContext(), "解压缩进度 " + percentDone + "%", Toast.LENGTH_SHORT);
                        Log.e("TT", String.format("onUnzipProgress target = %s, out = %s, percentDone = %d", target.getPath(), out.getPath(), percentDone));
                    }

                    @Override
                    public void onUnzipComplete(File target, File out) {
                        ToastUtils.show(getApplicationContext(), "解压缩完成", Toast.LENGTH_SHORT);
                        Log.e("TT", String.format("onUnzipComplete target = %s, out = %s", target.getPath(), out.getPath()));
                        final String path = target.getPath();
                        final String substring = path.substring(path.lastIndexOf("/"), path.lastIndexOf("."));
                        final File file = new File(out.getPath() + substring);
                        try {
                            ParseAssetsHelper.startAsyncParseAssetsXml(getApplicationContext(), FileSafeCode.getSha1(target), file);
                            target.delete();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onUnzipFailed(File target, File out, Throwable ex) {
                        ToastUtils.show(getApplicationContext(), "解压缩失败", Toast.LENGTH_SHORT);
                        target.delete();
                        Log.e("TT", String.format("onUnzipFailed target = %s, out = %s", target.getAbsolutePath(), out.getAbsolutePath()), ex);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ToastUtils.show(context, "下载完了", Toast.LENGTH_SHORT);
        }
    };


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionManager.AT_CODE_REQUEST_PERMISSION_CHANNEL) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    showDialogTipUserGoToAppSetting();
                } else {
                    Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private AlertDialog hintPermissionDialog;

    // 提示用户去应用设置界面手动开启权限
    private void showDialogTipUserGoToAppSetting() {
        hintPermissionDialog = new AlertDialog.Builder(this)
                .setTitle("权限不可用")
                .setMessage("请在-应用设置-权限-中，激活顶呱刮相应权限")
                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPermissionManager.startRequestPermissionByPT();
                    }
                })
                .setCancelable(false).show();
    }

    //
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PermissionManager.PT_CODE_REQUEST_PERMISSION_CHANNEL) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                if (mPermissionManager.checkPermission()) {
                    showDialogTipUserGoToAppSetting();
                } else {
                    if (hintPermissionDialog != null && hintPermissionDialog.isShowing()) {
                        hintPermissionDialog.dismiss();
                    }
                    Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}