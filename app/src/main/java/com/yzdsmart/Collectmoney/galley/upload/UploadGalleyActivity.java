package com.yzdsmart.Collectmoney.galley.upload;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.utils.SharedPreferencesUtils;
import com.yzdsmart.Collectmoney.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerPreviewActivity;
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout;

/**
 * Created by jacks on 2016/9/17.
 */
public class UploadGalleyActivity extends BaseActivity implements UploadGalleyContract.UploadImageView, BGASortableNinePhotoLayout.Delegate {
    @Nullable
    @BindViews({R.id.left_title, R.id.center_title, R.id.title_right_operation})
    List<View> hideViews;
    @Nullable
    @BindView(R.id.title_left_operation)
    ImageView titleLeftOpeIV;
    @Nullable
    @BindView(R.id.snpl_add_photos)
    BGASortableNinePhotoLayout mPhotosSnpl;

    private Integer type;//0 上传个人相册 1 上传商铺相册

    private Integer pathSize;
    private Integer alreadyUploaded;

    private static final String PERSONAL_UPLOAD_ACTION_CODE = "2102";//上传相册
    private static final String SHOP_UPLOAD_ACTION_CODE = "5101";//上传商铺相册

    private static final int REQUEST_CODE_CHOOSE_PHOTO = 1;
    private static final int REQUEST_CODE_PHOTO_PREVIEW = 2;

    private UploadGalleyContract.UploadImagePresenter mPresenter;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            switch (type) {
                case 0:
                    mPresenter.uploadGalley(PERSONAL_UPLOAD_ACTION_CODE, SharedPreferencesUtils.getString(UploadGalleyActivity.this, "cust_code", "") + bundle.getInt("index") + ".png", bundle.getString("image"), SharedPreferencesUtils.getString(UploadGalleyActivity.this, "cust_code", ""));
                    break;
                case 1:
                    mPresenter.uploadShopImage(SHOP_UPLOAD_ACTION_CODE, SharedPreferencesUtils.getString(UploadGalleyActivity.this, "baza_code", "") + bundle.getInt("index") + ".png", bundle.getString("image"), SharedPreferencesUtils.getString(UploadGalleyActivity.this, "baza_code", ""));
                    break;
            }
        }
    };

    private Handler handler = new Handler();
    private Runnable closeRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        type = getIntent().getExtras().getInt("type");

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow));

        new UploadGalleyPresenter(this, this);

        mPhotosSnpl.init(this);
        // 设置拖拽排序控件的代理
        mPhotosSnpl.setDelegate(this);

        closeRunnable = new Runnable() {
            @Override
            public void run() {
                hideProgressDialog();
                setResult(RESULT_OK);
                closeActivity();
            }
        };
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_upload_galley;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.unRegisterSubscribe();
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(closeRunnable);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_PHOTO) {
            mPhotosSnpl.setData(BGAPhotoPickerActivity.getSelectedImages(data));
        } else if (requestCode == REQUEST_CODE_PHOTO_PREVIEW) {
            mPhotosSnpl.setData(BGAPhotoPickerPreviewActivity.getSelectedImages(data));
        }
        pathSize = mPhotosSnpl.getData().size();
    }

    @Optional
    @OnClick({R.id.title_left_operation_layout, R.id.upload_galley})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                closeActivity();
                break;
            case R.id.upload_galley:
                alreadyUploaded = 0;
                for (int i = 0; i < mPhotosSnpl.getData().size(); i++) {
                    new Thread(new FormatImageRunnable(i, mPhotosSnpl.getData().get(i))).start();
                }
                break;
        }
    }

    @Override
    public void onClickAddNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, ArrayList<String> models) {
        // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话就没有拍照功能
        File takePhotoDir = new File(Environment.getExternalStorageDirectory(), "CollectMoney");
        startActivityForResult(BGAPhotoPickerActivity.newIntent(this, takePhotoDir, mPhotosSnpl.getMaxItemCount(), mPhotosSnpl.getData(), true), REQUEST_CODE_CHOOSE_PHOTO);
    }

    @Override
    public void onClickDeleteNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        mPhotosSnpl.removeItem(position);
    }

    @Override
    public void onClickNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        startActivityForResult(BGAPhotoPickerPreviewActivity.newIntent(this, mPhotosSnpl.getMaxItemCount(), models, models, position, false), REQUEST_CODE_PHOTO_PREVIEW);
    }

    @Override
    public void setPresenter(UploadGalleyContract.UploadImagePresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onUploadImageSuccess() {
        alreadyUploaded++;
        if (pathSize == alreadyUploaded) {
            showProgressDialog(R.drawable.success, getResources().getString(R.string.upload_success));
            handler.postDelayed(closeRunnable, 500);
        }
    }

    class FormatImageRunnable implements Runnable {
        private Integer index;
        private String path;

        public FormatImageRunnable(Integer index, String path) {
            this.index = index;
            this.path = path;
        }

        @Override
        public void run() {
            byte[] bytes = Utils.decodeBitmap(path);
            String image = new String(android.util.Base64.encode(bytes, android.util.Base64.DEFAULT));
            Message msg = new Message();
            Bundle bundle = new Bundle();
            bundle.putInt("index", index);
            bundle.putString("image", image);
            msg.setData(bundle);
            mHandler.sendMessage(msg);
        }
    }
}
