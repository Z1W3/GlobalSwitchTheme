package cattt.gst.library.utils.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.support.v4.view.ViewCompat;
import android.view.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import cattt.gst.library.utils.logger.Log;

public class File2Bitmap {
    private static Log logger = Log.getLogger(File2Bitmap.class);

    /**
     * @deprecated
     */
    public static Bitmap decodeFile(String filePath) throws IOException {
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

    public static Bitmap decodeFile(String filePath, View view) throws IOException {
        logger.e("$$$$$$$$$    start time = %d, ThreadID = %d", System.currentTimeMillis(), Thread.currentThread().getId());
        Bitmap b;
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
        try {
            //矩阵
            Rect rect = new Rect();
            BitmapFactory.decodeStream(fis, rect, o);
        } finally {
            fis.close();
        }

        //Decode with inSampleSize
        try {
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            int scale = computeScale(o, view);
            logger.e("decodeFile() -> scale = %d, ThreadID = %d" ,scale, Thread.currentThread().getId());
            o2.inSampleSize = scale;
            fis = new FileInputStream(f);
            b = BitmapFactory.decodeStream(fis, null, o2);
        } finally {
            fis.close();
        }
        logger.e("$$$$$$$$$    end time = %d, ThreadID = %d", System.currentTimeMillis(), Thread.currentThread().getId());
        return b;
    }

    /**
     * Calculate zoom ratio
     */
    private static int computeScale(BitmapFactory.Options options, View view) {
        int scaleSize = 1;
        int width = view.getWidth();
        int height = view.getHeight();
        while (!ViewCompat.isLaidOut(view)) {
            width = view.getWidth();
            height = view.getHeight();
            try {
                Thread.sleep(30L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (options.outWidth > width
                || options.outHeight > height) {
            int widthScale = Math.round((float) options.outWidth / (float) width);
            int heightScale = Math.round((float) options.outHeight / (float) height);
            if (widthScale >= heightScale) {
                scaleSize = widthScale;
            } else {
                scaleSize = heightScale;
            }
        }
        return scaleSize;
    }
}
