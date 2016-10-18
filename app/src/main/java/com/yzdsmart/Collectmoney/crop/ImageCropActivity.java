package com.yzdsmart.Collectmoney.crop;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.soundcloud.android.crop.Crop;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.utils.SharedPreferencesUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

/**
 * Created by YZD on 2016/8/24.
 */
public class ImageCropActivity extends BaseActivity implements View.OnClickListener, ImageCropContract.ImageCropView {
    @Nullable
    @BindViews({R.id.left_title, R.id.title_logo, R.id.title_right_operation})
    List<View> hideViews;
    @Nullable
    @BindView(R.id.title_left_operation)
    ImageView titleLeftOpeIV;
    @Nullable
    @BindView(R.id.center_title)
    TextView centerTitleTV;
    @Nullable
    @BindView(R.id.title_left_operation_layout)
    FrameLayout titleLeftOpeLayout;
    @Nullable
    @BindView(R.id.result_image)
    ImageView resultImageIV;

    Button takePhoto, selectFromGalley;

    private static final String UPLOAD_ACTION_CODE = "2101";//上传头像

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    Uri photoUri;
    private AlertDialog.Builder builder;
    private Dialog dialog;

    private ImageCropContract.ImageCropPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        centerTitleTV.setText("上传照片");
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow_white));
        titleLeftOpeLayout.setOnClickListener(this);
        resultImageIV.setOnClickListener(this);

        new ImageCropPresenter(this, this);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_image_crop;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        } else if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri uri;
            if (result != null && result.getData() != null) {
                uri = result.getData();
            } else {
                uri = photoUri;
            }
            if (null != uri) {
                beginCrop(uri);
            }
        }
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().withMaxSize(100, 100).start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            Bitmap bitmap = null;
            byte[] bytes = null;
            ContentResolver contentResolver = getContentResolver();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, Crop.getOutput(result));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (bitmap != null) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                bytes = byteArrayOutputStream.toByteArray();
            }
            bitmap.recycle();//释放bitmap
            String image = new String(android.util.Base64.encode(bytes, android.util.Base64.DEFAULT));

            mPresenter.uploadPortrait(UPLOAD_ACTION_CODE, SharedPreferencesUtils.getString(this, "im_account", "") + ".png", image, SharedPreferencesUtils.getString(this, "im_account", ""));

            resultImageIV.setImageURI(Crop.getOutput(result));
        } else if (resultCode == Crop.RESULT_ERROR) {
            showSnackbar(Crop.getError(result).getMessage());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.unRegisterSubscribe();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                closeActivity();
                break;
            case R.id.result_image:
                builder = new AlertDialog.Builder(this);
                View dialogView = LayoutInflater.from(this).inflate(R.layout.image_crop_dialog, null, false);
                builder.setView(dialogView);
                takePhoto = ButterKnife.findById(dialogView, R.id.take_photo);
                selectFromGalley = ButterKnife.findById(dialogView, R.id.select_from_galley);
                takePhoto.setOnClickListener(this);
                selectFromGalley.setOnClickListener(this);
                dialog = builder.show();
                break;
            case R.id.take_photo:
                resultImageIV.setImageDrawable(null);
                dialog.dismiss();
                builder = null;
                // 利用系统自带的相机应用:拍照
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
                String filename = timeStampFormat.format(new Date());
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, filename);
                photoUri = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                break;
            case R.id.select_from_galley:
                resultImageIV.setImageDrawable(null);
                dialog.dismiss();
                builder = null;
                Crop.pickImage(this);
                break;
        }
    }

    @Override
    public void setPresenter(ImageCropContract.ImageCropPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onUploadPortraitSuccess(String faceUrl) {
        mPresenter.setFaceUrl(faceUrl);
        closeActivity();
    }
}
