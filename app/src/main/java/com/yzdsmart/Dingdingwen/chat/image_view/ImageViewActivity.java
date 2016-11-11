package com.yzdsmart.Dingdingwen.chat.image_view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.umeng.analytics.MobclickAgent;
import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.tecent_im.utils.FileUtil;

import java.io.IOException;

import butterknife.BindView;

/**
 * 图片显示
 * Created by YZD on 2016/8/7.
 */
public class ImageViewActivity extends BaseActivity {
    @Nullable
    @BindView(R.id.view_image_view)
    ImageView viewImageView;

    private static final String TAG = "ImageViewActivity";

    private String currentImageName;
    private Bitmap viewImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentImageName = getIntent().getStringExtra("filename");
        viewImage = getImage(FileUtil.getCacheFilePath(currentImageName));
        if (null != viewImage) {
            viewImageView.setImageBitmap(viewImage);
        }

        MobclickAgent.openActivityDurationTrack(false);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_image_view;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    private Bitmap getImage(String path) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int reqWidth, reqHeight, width = options.outWidth, height = options.outHeight;
        if (width > height) {
            reqWidth = getWindowManager().getDefaultDisplay().getWidth();
            reqHeight = (reqWidth * height) / width;
        } else {
            reqHeight = getWindowManager().getDefaultDisplay().getHeight();
            reqWidth = (width * reqHeight) / height;
        }
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        try {
            options.inSampleSize = inSampleSize;
            options.inJustDecodeBounds = false;
            Matrix mat = new Matrix();
            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            if (bitmap == null) {
                showSnackbar(getString(R.string.file_not_found));
                return null;
            }
            ExifInterface ei = new ExifInterface(path);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    mat.postRotate(90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    mat.postRotate(180);
                    break;
            }
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mat, true);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return false;
    }
}
