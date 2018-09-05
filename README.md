# GlobalSwitchTheme

### 简介
通过网络获取主题内容,异步的方式更换View的图片、文字、背景图、背景色<br>
<img width="720" height="480" src="https://github.com/LuckWei/GlobalSwitchTheme/blob/master/gif/sample.gif" />

## 注意点
1.zip包中目录结构以及*.xml内的格式不可更改，更改后app无法解析<br>

<img width="350" height="100" src="https://github.com/LuckWei/GlobalSwitchTheme/blob/master/image/d1.png" /><br>
<img width="350" height="100" src="https://github.com/LuckWei/GlobalSwitchTheme/blob/master/image/d2.png" /><br>
<img width="350" height="100" src="https://github.com/LuckWei/GlobalSwitchTheme/blob/master/image/d3.png" /><br>
<img width="400" height="350" src="https://github.com/LuckWei/GlobalSwitchTheme/blob/master/image/d4.png" /><br>
<img width="400" height="350" src="https://github.com/LuckWei/GlobalSwitchTheme/blob/master/image/d5.png" /><br>

### 建立ID关联
```java
AssetsHelper mHelper = new AssetsHelper(this, R.id.wallpaper2, R.id.toolbar_title, R.id.button1, R.id.button2);
```
### 使用完毕要销毁
```java
mHelper.destroyAssetsHelper();
```

### Activity实现
```java
  public class MainActivity extends Activity{
    private AssetsHelper mHelper;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //添加ids
        mHelper = new AssetsHelper(this, R.id.wallpaper2, R.id.toolbar_title, R.id.button1, R.id.button2);
    }
    
    @Override
    protected void onDestroy() {
        mHelper.destroyAssetsHelper();
        super.onDestroy();
    }
  }
```


### Fragment实现
```java
  public class MainFragment extends Fragment{
    private AssetsHelper mHelper;

    private int[] mIds = new int[]{
            R.id.wallpaper2,
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
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_demo2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHelper = new AssetsHelper(this, mIds);
        TextView textView = getView().findViewById(R.id.numberTv);
        textView.setText("当前第" + number + "页");
    }

    @Override
    public void onDestroyView() {
        mHelper.destroyAssetsHelper();
        super.onDestroyView();
    }
  }
```

#### 开启解析资源
```java
ParseAssetsHelper.startAsyncParseAssetsXml(getApplicationContext(), FileSafeCode.getSha1(target), file);
```

解压zip与解析资源类配合使用
```java
new ZipArchive().unzip(resourcesZipFile.getPath(), outDir.getPath(), new OnUnzipListener() {
                    @Override
                    public void onUnzipProgress(File target, File out, int percentDone) {
                        ToastUtils.show(getApplicationContext(), "解压缩进度 " + percentDone + "%", Toast.LENGTH_SHORT);
                    }

                    @Override
                    public void onUnzipComplete(File target, File out) {
                        ToastUtils.show(getApplicationContext(), "解压缩完成", Toast.LENGTH_SHORT);
                        final String path = target.getPath();
                        final String substring = path.substring(path.lastIndexOf("/"), path.lastIndexOf("."));
                        final File file = new File(out.getPath() + substring);
                        try {
                            //开始解析资源XML内容
                            ParseAssetsHelper.startAsyncParseAssetsXml(getApplicationContext(), FileSafeCode.getSha1(target), file);
                            target.delete(); //删除下载的zip包
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
                    }
                });

```
