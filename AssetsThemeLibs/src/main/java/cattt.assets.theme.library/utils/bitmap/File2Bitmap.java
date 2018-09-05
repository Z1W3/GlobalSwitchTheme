package cattt.assets.theme.library.utils.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import cattt.assets.theme.library.utils.logger.Log;

public class File2Bitmap {
    private static Log logger = Log.getLogger(File2Bitmap.class);


    /**
     * @deprecated
     */
    public static Bitmap decodeFile(final String filePath) throws IOException {
        Bitmap b;
        int IMAGE_MAX_SIZE = 2048;

        File f = new File(filePath);
        if (f == null) {
            return null;
        }
        //Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        //设置为true,表示解析Bitmap对象，该对象不占内存
        o.inJustDecodeBounds = true;
        //首先，如果对图片质量要求不高，可以设置图片的压缩格式为RGB_565
        o.inPreferredConfig = Bitmap.Config.RGB_565;

        FileInputStream fis = new FileInputStream(f);
        //矩阵
        Rect rect = new Rect();
        BitmapFactory.decodeStream(fis, rect, o);
        fis.close();

        int scale = 1;
        if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
            scale = (int) Math.pow(2, (int) Math.round(Math.log(IMAGE_MAX_SIZE / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
        }

        //Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        fis = new FileInputStream(f);
        b = BitmapFactory.decodeStream(fis, null, o2);
        fis.close();
        return b;
    }


    public static Bitmap decodeFile(final String path, final int width, final int height) throws IOException {
        if (TextUtils.isEmpty(path)) {
            throw new NullPointerException("Path param cannot be empty");
        }
        return decodeFile(new File(path), width, height);
    }

    public static Bitmap decodeFile(final File file, final int width, final int height) throws IOException {
        //Decode image size
        final BitmapFactory.Options o = new BitmapFactory.Options();
        //设置为true,表示解析Bitmap对象，该对象不占内存
        o.inJustDecodeBounds = true;
        //首先，如果对图片质量要求不高，可以设置图片的压缩格式为RGB_565
        o.inPreferredConfig = Bitmap.Config.RGB_565;

        FileInputStream fis = null;
        try {
            //矩阵
            final Rect rect = new Rect();
            fis = new FileInputStream(file);
            BitmapFactory.decodeStream(fis, rect, o);
        } finally {
            if (fis != null) {
                fis.close();
                fis = null;
            }
        }
        //Decode with inSampleSize
        try {
            final BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = computeScale(o, width, height);
            fis = new FileInputStream(file);
            return BitmapFactory.decodeStream(fis, null, o2);
        } finally {
            if (fis != null) {
                fis.close();
                fis = null;
            }
        }
    }

    public static Bitmap decodeFile(final String path, final View view) throws IOException {
        if (TextUtils.isEmpty(path)) {
            throw new NullPointerException("Path param cannot be empty");
        }
        return decodeFile(new File(path), view);
    }

    public static Bitmap decodeFile(final File file, final View view) throws IOException {
        //Decode image size
        final BitmapFactory.Options o = new BitmapFactory.Options();
        //设置为true,表示解析Bitmap对象，该对象不占内存
        o.inJustDecodeBounds = true;
        //首先，如果对图片质量要求不高，可以设置图片的压缩格式为RGB_565
        o.inPreferredConfig = Bitmap.Config.RGB_565;

        FileInputStream fis = null;
        try {
            //矩阵
            final Rect rect = new Rect();
            fis = new FileInputStream(file);
            BitmapFactory.decodeStream(fis, rect, o);
        } finally {
            if (fis != null) {
                fis.close();
                fis = null;
            }
        }
        //Decode with inSampleSize
        try {
            final BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = computeScale(o, view);
            fis = new FileInputStream(file);
            return BitmapFactory.decodeStream(fis, null, o2);
        } finally {
            if (fis != null) {
                fis.close();
                fis = null;
            }
        }
    }

    /**
     * Calculate zoom ratio
     */
    private static int computeScale(BitmapFactory.Options options, View view) {
        int scaleSize = 1;
        int width = view.getWidth();
        int height = view.getHeight();
        boolean result = false;
        while (!result) {
            result = ViewCompat.isLaidOut(view);
            width = view.getWidth();
            height = view.getHeight();
            try {
                Thread.sleep(10L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (options.outWidth > width || options.outHeight > height) {
            final int widthScale = Math.round((float) options.outWidth / (float) width);
            final int heightScale = Math.round((float) options.outHeight / (float) height);
            if (widthScale >= heightScale) {
                scaleSize = widthScale;
            } else {
                scaleSize = heightScale;
            }
        }
        return scaleSize;
    }

    /**
     * Calculate zoom ratio
     */
    private static int computeScale(final BitmapFactory.Options options, final int width, final int height) {
        int scaleSize = 1;
        if (options.outWidth > width || options.outHeight > height) {
            final int widthScale = Math.round((float) options.outWidth / (float) width);
            final int heightScale = Math.round((float) options.outHeight / (float) height);
            if (widthScale >= heightScale) {
                scaleSize = widthScale;
            } else {
                scaleSize = heightScale;
            }
        }
        return scaleSize;
    }
}
