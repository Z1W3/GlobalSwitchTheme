# GlobalSwitchTheme

### 简介
通过网络获取主题内容,异步的方式更换View的图片、文字、背景图、背景色<br>
<img width="720" height="480" src="https://github.com/LuckWei/GlobalSwitchTheme/blob/master/gif/sample.gif" />

## 注意点
1.图片的目录结构以及color.xml内的格式不可更改，更改后app无法解析<br>
2.图片的命名需要与R.mId.xxx的 "xxx"保持一致<br>
3.color的name命名需要与R.mId.xxx的 "xxx"保持一致<br>

<img width="350" height="100" src="https://github.com/LuckWei/GlobalSwitchTheme/blob/master/image/d1.png" /><br>
<img width="350" height="100" src="https://github.com/LuckWei/GlobalSwitchTheme/blob/master/image/d2.png" /><br>
<img width="350" height="100" src="https://github.com/LuckWei/GlobalSwitchTheme/blob/master/image/d3.png" /><br>
<img width="400" height="350" src="https://github.com/LuckWei/GlobalSwitchTheme/blob/master/image/d4.png" /><br>

### 实现
你的Activity需要继承BaseSwitchThemeActivity,通过添加View id匹配相应的image/color
```java
  public class MainActivity extends BaseSwitchThemeActivity{
  
    @Override
    protected int[] getViewResourcesPendingChangeTheme() {
        //You View mId in the current page
        return new int[]{R.mId.wallpaper2, R.mId.toolbar_title, R.mId.button, R.mId.app_compat_button, R.mId.app_compat_text};
    }
  }
```

#### 开启解析资源
ParseResourcesRunnable 为解析类，需要在子线程中执行

```java
Executors.newFixedThreadPool(3).execute(new ParseResourcesRunnable(out));

```

解析资源与解压配合使用
```java
new ZipArchive().unzip(resourcesZipFile.getAbsolutePath(), outDir.getAbsolutePath(), new OnUnzipListener() {
                    @Override
                    public void onUnzipProgress(File target, File out, int percentDone) {
                        ToastUtils.show(getApplicationContext(), "解压缩进度 " + percentDone + "%", Toast.LENGTH_SHORT);
                    }

                    @Override
                    public void onUnzipComplete(File target, File out) {
                        ToastUtils.show(getApplicationContext(), "解压缩完成", Toast.LENGTH_SHORT);
                        target.delete();//删除下载zip文件
                        Executors.newFixedThreadPool(1).execute(new ParseResourcesRunnable(out));
                    }

                    @Override
                    public void onUnzipFailed(File target, File out, Throwable ex) {
                        ToastUtils.show(getApplicationContext(), "解压缩失败", Toast.LENGTH_SHORT);
                        target.delete();
                    }
                });

```
