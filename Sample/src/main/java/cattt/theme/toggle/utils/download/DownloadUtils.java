package cattt.theme.toggle.utils.download;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.Uri;

public class DownloadUtils {
    //下载器
    private DownloadManager mDownloadManager;
    //上下文
    private Context mContext;
    //下载的ID
    private long downloadId;

    public DownloadUtils(Context context) {
        this.mContext = context;
        mDownloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    //下载apk
    public void downloadFile(String url, String name) {
        //创建下载任务
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        //移动网络情况下是否允许漫游
        request.setAllowedOverRoaming(false);
        //在通知栏中显示，默认就是显示的
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setTitle("正在下载");
        request.setDescription("Files Downloading");
        request.setVisibleInDownloadsUi(true);
        //设置下载的路径
        request.setDestinationInExternalPublicDir("", name);
        //获取

        //将下载请求加入下载队列，加入下载队列后会给该任务返回一个long型的id，通过该id可以取消任务，重启任务、获取下载的文件等等
        downloadId = mDownloadManager.enqueue(request);
    }

    public void registerReceiver(BroadcastReceiver receiver) {
        mContext.registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    public void unregisterReceiver(BroadcastReceiver receiver) {
        mContext.unregisterReceiver(receiver);
    }
}
