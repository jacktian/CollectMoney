package com.yzdsmart.Collectmoney.galley.preview;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.bean.GalleyInfo;
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
import cn.bingoogolapple.photopicker.activity.BGAPhotoViewActivity;
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout;

/**
 * Created by YZD on 2016/9/21.
 */

public class GalleyPreviewActivity extends BaseActivity implements BGASortableNinePhotoLayout.Delegate, GalleyPreviewContract.GalleyPreviewView {
    @Nullable
    @BindViews({R.id.left_title, R.id.title_logo, R.id.title_right_operation_layout})
    List<View> hideViews;
    @Nullable
    @BindViews({R.id.right_title})
    List<View> showViews;
    @Nullable
    @BindView(R.id.title_left_operation)
    ImageView titleLeftOpeIV;
    @Nullable
    @BindView(R.id.center_title)
    TextView centerTitleTV;
    @Nullable
    @BindView(R.id.right_title)
    TextView rightTitleTV;
    @Nullable
    @BindView(R.id.snpl_delete_photos)
    BGASortableNinePhotoLayout mPhotosSnpl;
    @Nullable
    @BindView(R.id.delete_galley)
    Button deleteGalley;
//    @Nullable
//    @BindView(R.id.delete_galley_layout)
//    FrameLayout deleteGalleyLayout;

    private Integer identityType;//0 个人 1 商铺
    private Integer userType;//0 自身 1 好友
    private String custCode;
    private List<GalleyInfo> galleyInfoList;
    private ArrayList<String> deleteGalleys;
    private List<Integer> deleteFileList;

    private boolean isGalleyOperated = false;

    private static final Integer PERSONAL_GALLEY_OPERATION_CODE = 1000;
    private static final Integer SHOP_GALLEY_OPERATION_CODE = 1001;

    private static final String PERSONAL_GALLEY_ACTION_CODE = "2102";
    private static final String SHOP_GALLEY_ACTION_CODE = "5101";
    private static final String PERSONAL_GALLEY_DELETE_ACTION_CODE = "4102";
    private static final String SHOP_GALLEY_DELETE_ACTION_CODE = "4201";
    private static final String PERSONAL_UPLOAD_ACTION_CODE = "2102";//上传个人相册
    private static final String SHOP_UPLOAD_ACTION_CODE = "5101";//上传商铺相册

    private static final int REQUEST_CODE_CHOOSE_PHOTO = 1;
    private static final int REQUEST_CODE_PHOTO_PREVIEW = 2;

    private GalleyPreviewContract.GalleyPreviewPresenter mPresenter;

    private Handler mHandler = new Handler();
    private Runnable deletePersonalGalleySuccessRunnable;
    private Runnable deleteShopGalleySuccessRunnable;

    private Handler mUploadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            switch (identityType) {
                case 0:
                    mPresenter.uploadGalley(PERSONAL_UPLOAD_ACTION_CODE, SharedPreferencesUtils.getString(GalleyPreviewActivity.this, "cust_code", "") + bundle.getInt("index") + ".png", bundle.getString("image"), SharedPreferencesUtils.getString(GalleyPreviewActivity.this, "cust_code", ""));
                    break;
                case 1:
                    mPresenter.uploadShopImage(SHOP_UPLOAD_ACTION_CODE, SharedPreferencesUtils.getString(GalleyPreviewActivity.this, "baza_code", "") + bundle.getInt("index") + ".png", bundle.getString("image"), SharedPreferencesUtils.getString(GalleyPreviewActivity.this, "baza_code", ""));
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        galleyInfoList = new ArrayList<GalleyInfo>();
        deleteGalleys = new ArrayList<String>();
        deleteFileList = new ArrayList<Integer>();

        identityType = getIntent().getExtras().getInt("identity");
        custCode = getIntent().getExtras().getString("cust_code");
//        galleyInfoList = getIntent().getExtras().getParcelableArrayList("galleys");

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);

        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow_white));
        switch (identityType) {
            case 0:
                userType = getIntent().getExtras().getInt("type");
                switch (userType) {
                    case 0:
                        centerTitleTV.setText("我的相册");
                        rightTitleTV.setText("选择");
                        ButterKnife.apply(showViews, BUTTERKNIFEVISIBLE);
//                        ButterKnife.apply(deleteGalleyLayout, BUTTERKNIFEVISIBLE);
                        ButterKnife.apply(deleteGalley, BUTTERKNIFEVISIBLE);
                        break;
                    case 1:
                        centerTitleTV.setText("好友相册");
                        mPhotosSnpl.setIsPlusSwitchOpened(false);
                        ButterKnife.apply(deleteGalley, BUTTERKNIFEGONE);
                        break;
                }
                break;
            case 1:
                centerTitleTV.setText("商铺相册");
                rightTitleTV.setText("选择");
                ButterKnife.apply(showViews, BUTTERKNIFEVISIBLE);
//                ButterKnife.apply(deleteGalleyLayout, BUTTERKNIFEVISIBLE);
                ButterKnife.apply(deleteGalley, BUTTERKNIFEVISIBLE);
                break;
        }

        new GalleyPreviewPresenter(this, this);

        switch (identityType) {
            case 0:
                mPresenter.getPersonalGalley(PERSONAL_GALLEY_ACTION_CODE, "000000", custCode);
                break;
            case 1:
                mPresenter.getShopGalley(SHOP_GALLEY_ACTION_CODE, "000000", SharedPreferencesUtils.getString(GalleyPreviewActivity.this, "baza_code", ""));
                break;
        }

        deletePersonalGalleySuccessRunnable = new Runnable() {
            @Override
            public void run() {
                hideProgressDialog();
                mPresenter.getPersonalGalley(PERSONAL_GALLEY_ACTION_CODE, "000000", SharedPreferencesUtils.getString(GalleyPreviewActivity.this, "cust_code", ""));
            }
        };

        deleteShopGalleySuccessRunnable = new Runnable() {
            @Override
            public void run() {
                hideProgressDialog();
                mPresenter.getShopGalley(SHOP_GALLEY_ACTION_CODE, "000000", SharedPreferencesUtils.getString(GalleyPreviewActivity.this, "baza_code", ""));
            }
        };

        mPhotosSnpl.init(this);
        // 设置拖拽排序控件的代理
        mPhotosSnpl.setDelegate(this);
        mPhotosSnpl.setDeleteDrawableResId(0);

//        deleteGalleys.clear();
//        for (GalleyInfo galleyInfo : galleyInfoList) {
//            deleteGalleys.add(galleyInfo.getImageFileUrl());
//        }
//        mPhotosSnpl.setData(deleteGalleys);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_galley_preview;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.unRegisterSubscribe();
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacks(deletePersonalGalleySuccessRunnable);
        mHandler.removeCallbacks(deleteShopGalleySuccessRunnable);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (PERSONAL_GALLEY_OPERATION_CODE == requestCode && RESULT_OK == resultCode) {
            mPresenter.getPersonalGalley(PERSONAL_GALLEY_ACTION_CODE, "000000", SharedPreferencesUtils.getString(this, "cust_code", ""));
        } else if (SHOP_GALLEY_OPERATION_CODE == requestCode && RESULT_OK == resultCode) {
            mPresenter.getShopGalley(SHOP_GALLEY_ACTION_CODE, "000000", SharedPreferencesUtils.getString(this, "baza_code", ""));
        } else if (REQUEST_CODE_CHOOSE_PHOTO == requestCode && RESULT_OK == resultCode) {
            ArrayList<String> imageUrls = BGAPhotoPickerActivity.getSelectedImages(data);
            for (int i = 0; i < imageUrls.size(); i++) {
                new Thread(new FormatImageRunnable(i, imageUrls.get(i))).start();
            }
        }
    }

    @Optional
    @OnClick({R.id.title_left_operation_layout, R.id.right_title, R.id.delete_galley})
    void onClick(View view) {
        Bundle bundle;
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                closeActivity();
                break;
            case R.id.right_title:
                if (mPhotosSnpl.getData().size() <= 0) return;
                isGalleyOperated = !isGalleyOperated;
                if (isGalleyOperated) {
                    rightTitleTV.setText("取消");
                    mPhotosSnpl.setDeleteDrawableResId(R.mipmap.bga_pp_ic_delete);
                    mPhotosSnpl.setIsPlusSwitchOpened(false);
                    deleteGalley.setEnabled(true);
                } else {
                    rightTitleTV.setText("选择");
                    mPhotosSnpl.setDeleteDrawableResId(0);
                    deleteGalley.setEnabled(false);
                    mPhotosSnpl.setIsPlusSwitchOpened(true);
                }
                mPhotosSnpl.refresh();
                break;
            case R.id.delete_galley:
                rightTitleTV.setText("选择");
                isGalleyOperated = false;
                mPhotosSnpl.setDeleteDrawableResId(0);
                deleteGalley.setEnabled(false);
                mPhotosSnpl.setIsPlusSwitchOpened(true);
                deleteFileList.clear();
                for (String path : mPhotosSnpl.getData()) {
                    for (GalleyInfo galleyInfo : galleyInfoList) {
                        if (galleyInfo.getImageFileUrl().equals(path)) {
                            deleteFileList.add(galleyInfo.getFileId());
                        }
                    }
                }
                switch (identityType) {
                    case 0:
                        mPresenter.deletePersonalGalley(PERSONAL_GALLEY_DELETE_ACTION_CODE, "000000", SharedPreferencesUtils.getString(this, "cust_code", ""), deleteFileList);
                        break;
                    case 1:
                        mPresenter.deleteShopGalley(SHOP_GALLEY_DELETE_ACTION_CODE, "000000", SharedPreferencesUtils.getString(this, "baza_code", ""), deleteFileList);
                        break;
                }
                break;
        }
    }

    @Override
    public void onClickAddNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, ArrayList<String> models) {
        // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话就没有拍照功能
        File takePhotoDir = new File(Environment.getExternalStorageDirectory(), "CollectMoney");
        switch (identityType) {
            case 0:
                startActivityForResult(BGAPhotoPickerActivity.newIntent(this, takePhotoDir, 9, null, true), REQUEST_CODE_CHOOSE_PHOTO);
                break;
            case 1:
                startActivityForResult(BGAPhotoPickerActivity.newIntent(this, takePhotoDir, 5, null, true), REQUEST_CODE_CHOOSE_PHOTO);
                break;
        }
    }

    @Override
    public void onClickDeleteNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        System.out.println(position + "--------" + model + "--------" + models);
        mPhotosSnpl.removeItem(position);
        if (mPhotosSnpl.getData().size() <= 0) {
            rightTitleTV.setText("选择");
            isGalleyOperated = false;
            mPhotosSnpl.setDeleteDrawableResId(0);
            deleteGalley.setEnabled(false);
            mPhotosSnpl.setIsPlusSwitchOpened(true);
        }
    }

    @Override
    public void onClickNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        startActivityForResult(BGAPhotoViewActivity.newIntent(this, Integer.MAX_VALUE, models, models, position, false), REQUEST_CODE_PHOTO_PREVIEW);
    }

    @Override
    public void onGetPersonalGalley(List<GalleyInfo> galleyInfos) {
        galleyInfoList.clear();
        deleteGalleys.clear();
        galleyInfoList.addAll(galleyInfos);
        for (GalleyInfo galleyInfo : galleyInfoList) {
            deleteGalleys.add(galleyInfo.getImageFileUrl());
        }
        mPhotosSnpl.removeAllViews();
        mPhotosSnpl.setData(deleteGalleys);
    }

    @Override
    public void onDeletePersonalGalley() {
        showProgressDialog(R.drawable.success, getResources().getString(R.string.delete_success));
        mHandler.postDelayed(deletePersonalGalleySuccessRunnable, 500);
    }

    @Override
    public void onGetShopGalley(List<GalleyInfo> galleyInfos) {
        galleyInfoList.clear();
        deleteGalleys.clear();
        galleyInfoList.addAll(galleyInfos);
        for (GalleyInfo galleyInfo : galleyInfoList) {
            deleteGalleys.add(galleyInfo.getImageFileUrl());
        }
        mPhotosSnpl.removeAllViews();
        mPhotosSnpl.setData(deleteGalleys);
    }

    @Override
    public void onDeleteShopGalley() {
        showProgressDialog(R.drawable.success, getResources().getString(R.string.delete_success));
        mHandler.postDelayed(deleteShopGalleySuccessRunnable, 500);
    }

    @Override
    public void onUploadGalley() {
        switch (identityType) {
            case 0:
                mPresenter.getPersonalGalley(PERSONAL_GALLEY_ACTION_CODE, "000000", custCode);
                break;
            case 1:
                mPresenter.getShopGalley(SHOP_GALLEY_ACTION_CODE, "000000", SharedPreferencesUtils.getString(GalleyPreviewActivity.this, "baza_code", ""));
                break;
        }
    }

    @Override
    public void setPresenter(GalleyPreviewContract.GalleyPreviewPresenter presenter) {
        mPresenter = presenter;
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
            mUploadHandler.sendMessage(msg);
        }
    }
}
