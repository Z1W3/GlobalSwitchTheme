package cattt.theme.toggle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.util.concurrent.Executors;

import cattt.gst.library.base.BaseGlobalThemeActivity;
import cattt.gst.library.base.ParseResourcesRunnable;
import cattt.gst.library.utils.toast.ToastUtils;
import cattt.gst.library.utils.zip.ZipArchive;
import cattt.gst.library.utils.zip.callback.OnUnzipListener;

public class MainActivity extends BaseGlobalThemeActivity {

    private DownloadUtils mDownloadUtils;
    private File resourcesZipFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/resources.zip");
    private File outDir;


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_main;
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
    protected void initView(Bundle savedInstanceState) {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_title));
        mDownloadUtils = new DownloadUtils(getApplicationContext());
        mDownloadUtils.registerReceiver(receiver);
        outDir = new File(getApplicationContext().getFilesDir().getAbsolutePath());
    }

    @Override
    protected void onDestroy() {
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
                mDownloadUtils.downloadFile("http://hcb-cdn.oss-cn-beijing.aliyuncs.com/resources.zip", "resources.zip");
            } else {
                ToastUtils.show(this, "文件已经存在", Toast.LENGTH_SHORT);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ToastUtils.show(context, "下载完了", Toast.LENGTH_SHORT);
            try {
                new ZipArchive().unzip(resourcesZipFile.getAbsolutePath(), outDir.getAbsolutePath(), new OnUnzipListener() {
                    @Override
                    public void onUnzipProgress(File target, File out, int percentDone) {
                        ToastUtils.show(getApplicationContext(), "解压缩进度 " + percentDone + "%", Toast.LENGTH_SHORT);
                        Log.e("TT", String.format("onUnzipProgress target = %s, out = %s, percentDone = %d", target.getAbsolutePath(), out.getAbsolutePath(), percentDone));
                    }

                    @Override
                    public void onUnzipComplete(File target, File out) {
                        ToastUtils.show(getApplicationContext(), "解压缩完成", Toast.LENGTH_SHORT);
                        Log.e("TT", String.format("onUnzipComplete target = %s, out = %s", target.getAbsolutePath(), out.getAbsolutePath()));
                        target.delete();
                        Executors.newFixedThreadPool(3).execute(new ParseResourcesRunnable(out));
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
    };
}