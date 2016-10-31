package com.yzdsmart.Dingdingwen.chat.image_preview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.R;

import java.io.File;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by jacks on 2016/8/6.
 */
public class ImagePreviewActivity extends BaseActivity {
    @Nullable
    @BindViews({R.id.left_title, R.id.center_title, R.id.title_right_operation_layout})
    List<View> hideViews;
    @Nullable
    @BindView(R.id.title_left_operation)
    ImageView titleLeftOpeIV;
    @Nullable
    @BindView(R.id.image_preview)
    ImageView imagePreview;
    @Nullable
    @BindView(R.id.isOri)
    CheckBox isOriCB;

    private String path;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        path = getIntent().getStringExtra("path");

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow_white));

        showImage();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_image_preview;
    }

    @Optional
    @OnClick({R.id.send_image, R.id.title_left_operation_layout})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.send_image:
                Intent intent = new Intent();
                intent.putExtra("path", path);
                intent.putExtra("isOri", isOriCB.isChecked());
                setResult(RESULT_OK, intent);
                break;
            default:
                break;
        }
        closeActivity();
    }

    private void showImage() {
        if (path.equals("")) return;
        File file = new File(path);
        if (file.exists() && file.length() > 0) {
            isOriCB.setText(getString(R.string.chat_image_preview_ori) + "(" + getFileSize(file.length()) + ")");
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
                float scaleX = (float) reqWidth / (float) (width / inSampleSize);
                float scaleY = (float) reqHeight / (float) (height / inSampleSize);
                Matrix mat = new Matrix();
                mat.postScale(scaleX, scaleY);
                Bitmap bitmap = BitmapFactory.decodeFile(path, options);
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
                imagePreview.setImageBitmap(Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mat, true));
            } catch (IOException e) {
                showSnackbar(getString(R.string.chat_image_preview_load_err));
            }
        } else {
            finish();
        }
    }

    private String getFileSize(long size) {
        StringBuilder strSize = new StringBuilder();
        if (size < 1024) {
            strSize.append(size).append("B");
        } else if (size < 1024 * 1024) {
            strSize.append(size / 1024).append("K");
        } else {
            strSize.append(size / 1024 / 1024).append("M");
        }
        return strSize.toString();
    }
}
