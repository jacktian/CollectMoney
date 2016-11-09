package com.yzdsmart.Dingdingwen.galley;

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

import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.Constants;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.bean.GalleyInfo;
import com.yzdsmart.Dingdingwen.utils.SharedPreferencesUtils;
import com.yzdsmart.Dingdingwen.utils.Utils;
import com.yzdsmart.Dingdingwen.views.photo_picker.picker.activity.BGAPhotoPickerActivity;
import com.yzdsmart.Dingdingwen.views.photo_picker.picker.activity.BGAPhotoViewActivity;
import com.yzdsmart.Dingdingwen.views.photo_picker.picker.widget.BGASortableNinePhotoLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by YZD on 2016/9/21.
 */

public class GalleyActivity extends BaseActivity implements BGASortableNinePhotoLayout.Delegate, GalleyContract.GalleyPreviewView {
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

    private Integer identityType;//0 个人 1 商铺
    private Integer userType;//0 自身 1 好友
    private String custCode;
    private List<GalleyInfo> galleyInfoList;
    private ArrayList<String> galleys;
    private List<Integer> deleteFileIdList;
    private List<String> deleteFilePathList;

    private boolean isGalleyOperated = false;

    private GalleyContract.GalleyPreviewPresenter mPresenter;

    private Handler mHandler = new Handler();
    private Runnable deletePersonalGalleySuccessRunnable;
    private Runnable deleteShopGalleySuccessRunnable;

    private Handler mUploadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (!Utils.isNetUsable(GalleyActivity.this)) {
                showSnackbar(getResources().getString(R.string.net_unusable));
                return;
            }
            Bundle bundle = msg.getData();
            switch (identityType) {
                case 0:
                    mPresenter.uploadGalley(Constants.PERSONAL_GALLEY_UPLOAD_ACTION_CODE, SharedPreferencesUtils.getString(GalleyActivity.this, "cust_code", "") + bundle.getInt("index") + ".png", bundle.getString("image"), SharedPreferencesUtils.getString(GalleyActivity.this, "cust_code", ""));
                    break;
                case 1:
                    mPresenter.uploadShopImage(Constants.SHOP_GALLEY_UPLOAD_ACTION_CODE, SharedPreferencesUtils.getString(GalleyActivity.this, "baza_code", "") + bundle.getInt("index") + ".png", bundle.getString("image"), SharedPreferencesUtils.getString(GalleyActivity.this, "baza_code", ""));
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        galleyInfoList = new ArrayList<GalleyInfo>();
        galleys = new ArrayList<String>();
        deleteFileIdList = new ArrayList<Integer>();
        deleteFilePathList = new ArrayList<String>();

        identityType = getIntent().getExtras().getInt("identity");
        userType = getIntent().getExtras().getInt("type");
        custCode = getIntent().getExtras().getString("cust_code");
        mPhotosSnpl.setMaxItemCount(Integer.MAX_VALUE);
        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);

        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow_white));
        switch (identityType) {
            case 0:
                switch (userType) {
                    case 0:
                        centerTitleTV.setText("我的相册");
                        rightTitleTV.setText("选择");
                        ButterKnife.apply(showViews, BUTTERKNIFEVISIBLE);
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
                switch (userType) {
                    case 0:
                        centerTitleTV.setText("我的商铺相册");
                        rightTitleTV.setText("选择");
                        ButterKnife.apply(showViews, BUTTERKNIFEVISIBLE);
                        ButterKnife.apply(deleteGalley, BUTTERKNIFEVISIBLE);
                        break;
                    case 1:
                        centerTitleTV.setText("店铺相册");
                        mPhotosSnpl.setIsPlusSwitchOpened(false);
                        ButterKnife.apply(deleteGalley, BUTTERKNIFEGONE);
                        break;
                }

                break;
        }

        new GalleyPresenter(this, this);

        if (Utils.isNetUsable(this)) {
            switch (identityType) {
                case 0:
                    mPresenter.getPersonalGalley(Constants.GET_PERSONAL_GALLEY_ACTION_CODE, "000000", custCode);
                    break;
                case 1:
                    mPresenter.getShopGalley(Constants.GET_SHOP_GALLEY_ACTION_CODE, "000000", custCode);
                    break;
            }
        } else {
            showSnackbar(getResources().getString(R.string.net_unusable));
        }

        deletePersonalGalleySuccessRunnable = new Runnable() {
            @Override
            public void run() {
                hideProgressDialog();
                if (!Utils.isNetUsable(GalleyActivity.this)) {
                    showSnackbar(getResources().getString(R.string.net_unusable));
                    return;
                }
                mPresenter.getPersonalGalley(Constants.GET_PERSONAL_GALLEY_ACTION_CODE, "000000", SharedPreferencesUtils.getString(GalleyActivity.this, "cust_code", ""));
            }
        };

        deleteShopGalleySuccessRunnable = new Runnable() {
            @Override
            public void run() {
                hideProgressDialog();
                if (!Utils.isNetUsable(GalleyActivity.this)) {
                    showSnackbar(getResources().getString(R.string.net_unusable));
                    return;
                }
                mPresenter.getShopGalley(Constants.GET_SHOP_GALLEY_ACTION_CODE, "000000", SharedPreferencesUtils.getString(GalleyActivity.this, "baza_code", ""));
            }
        };

        mPhotosSnpl.init(this);
        // 设置拖拽排序控件的代理
        mPhotosSnpl.setDelegate(this);
        mPhotosSnpl.setDeleteDrawableResId(0);
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
        if (Constants.PERSONAL_GALLEY_OPERATION_CODE == requestCode && RESULT_OK == resultCode) {
            if (!Utils.isNetUsable(this)) {
                showSnackbar(getResources().getString(R.string.net_unusable));
                return;
            }
            mPresenter.getPersonalGalley(Constants.GET_PERSONAL_GALLEY_ACTION_CODE, "000000", SharedPreferencesUtils.getString(this, "cust_code", ""));
        } else if (Constants.SHOP_GALLEY_OPERATION_CODE == requestCode && RESULT_OK == resultCode) {
            if (!Utils.isNetUsable(this)) {
                showSnackbar(getResources().getString(R.string.net_unusable));
                return;
            }
            mPresenter.getShopGalley(Constants.GET_SHOP_GALLEY_ACTION_CODE, "000000", SharedPreferencesUtils.getString(this, "baza_code", ""));
        } else if (Constants.REQUEST_CODE_CHOOSE_PHOTO == requestCode && RESULT_OK == resultCode) {
            ArrayList<String> imageUrls = BGAPhotoPickerActivity.getSelectedImages(data);
            for (int i = 0; i < imageUrls.size(); i++) {
                new Thread(new FormatImageRunnable(i, imageUrls.get(i))).start();
            }
        }
    }

    @Optional
    @OnClick({R.id.title_left_operation_layout, R.id.right_title, R.id.delete_galley})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                closeActivity();
                break;
            case R.id.right_title:
                if (mPhotosSnpl.getData().size() <= 0) return;
                isGalleyOperated = !isGalleyOperated;
                deleteFilePathList.clear();
                deleteFileIdList.clear();
                if (isGalleyOperated) {
                    rightTitleTV.setText("取消");
                    mPhotosSnpl.setGalleyOperated(true);
                    mPhotosSnpl.setDeleteDrawableResId(R.mipmap.bga_pp_ic_cb_normal);
                } else {
                    rightTitleTV.setText("选择");
                    mPhotosSnpl.setGalleyOperated(false);
                    mPhotosSnpl.setDeleteDrawableResId(0);
                    deleteGalley.setEnabled(false);
                }
                mPhotosSnpl.refresh();
                break;
            case R.id.delete_galley:
                if (!Utils.isNetUsable(this)) {
                    showSnackbar(getResources().getString(R.string.net_unusable));
                    return;
                }
                rightTitleTV.setText("选择");
                isGalleyOperated = false;
                mPhotosSnpl.setGalleyOperated(false);
                deleteGalley.setEnabled(false);
                deleteFileIdList.clear();
                for (String path : deleteFilePathList) {
                    for (GalleyInfo galleyInfo : galleyInfoList) {
                        if (galleyInfo.getImageFileUrl().equals(path)) {
                            deleteFileIdList.add(galleyInfo.getFileId());
                        }
                    }
                }
                switch (identityType) {
                    case 0:
                        if (!Utils.isNetUsable(this)) {
                            showSnackbar(getResources().getString(R.string.net_unusable));
                            return;
                        }
                        mPresenter.deletePersonalGalley(Constants.PERSONAL_GALLEY_DELETE_ACTION_CODE, "000000", SharedPreferencesUtils.getString(this, "cust_code", ""), deleteFileIdList);
                        break;
                    case 1:
                        if (!Utils.isNetUsable(this)) {
                            showSnackbar(getResources().getString(R.string.net_unusable));
                            return;
                        }
                        mPresenter.deleteShopGalley(Constants.SHOP_GALLEY_DELETE_ACTION_CODE, "000000", SharedPreferencesUtils.getString(this, "baza_code", ""), deleteFileIdList);
                        break;
                }
                mPhotosSnpl.setDeleteDrawableResId(0);
                deleteFilePathList.clear();
                break;
        }
    }

    @Override
    public void onClickAddNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, ArrayList<String> models) {
        if (isGalleyOperated) return;
        // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话就没有拍照功能
        File takePhotoDir = new File(Environment.getExternalStorageDirectory(), "CollectMoney");
        switch (identityType) {
            case 0:
                startActivityForResult(BGAPhotoPickerActivity.newIntent(this, takePhotoDir, 9, null, true), Constants.REQUEST_CODE_CHOOSE_PHOTO);
                break;
            case 1:
                startActivityForResult(BGAPhotoPickerActivity.newIntent(this, takePhotoDir, 5, null, true), Constants.REQUEST_CODE_CHOOSE_PHOTO);
                break;
        }
    }

    @Override
    public void onClickDeleteNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        if (deleteFilePathList.contains(model)) {
            deleteFilePathList.remove(model);
            mPhotosSnpl.toggleDeleteRes(model, R.mipmap.bga_pp_ic_cb_normal);
        } else {
            deleteFilePathList.add(model);
            mPhotosSnpl.toggleDeleteRes(model, R.mipmap.bga_pp_ic_cb_checked);
        }
        if (deleteFilePathList.size() <= 0) {
            deleteGalley.setEnabled(false);
        } else {
            deleteGalley.setEnabled(true);
        }
    }

    @Override
    public void onClickNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        if (isGalleyOperated) return;
        startActivityForResult(BGAPhotoViewActivity.newIntent(this, Integer.MAX_VALUE, models, models, position - 1, false), Constants.REQUEST_CODE_PHOTO_PREVIEW);
    }

    @Override
    public void onGetPersonalGalley(List<GalleyInfo> galleyInfos) {
        galleyInfoList.clear();
        galleys.clear();
        mPhotosSnpl.removeAllViews();
        if (galleyInfos.size() <= 0) return;
        galleyInfoList.addAll(galleyInfos);
        for (GalleyInfo galleyInfo : galleyInfoList) {
            galleys.add(galleyInfo.getImageFileUrl());
        }
        mPhotosSnpl.setData(galleys);
    }

    @Override
    public void onDeletePersonalGalley() {
        rightTitleTV.setText("选择");
        isGalleyOperated = false;
        mPhotosSnpl.setGalleyOperated(false);
        deleteGalley.setEnabled(false);
        mPhotosSnpl.setDeleteDrawableResId(0);
        showProgressDialog(R.drawable.success, getResources().getString(R.string.delete_success));
        mHandler.postDelayed(deletePersonalGalleySuccessRunnable, 500);
    }

    @Override
    public void onGetShopGalley(List<GalleyInfo> galleyInfos) {
        galleyInfoList.clear();
        galleys.clear();
        galleyInfoList.addAll(galleyInfos);
        for (GalleyInfo galleyInfo : galleyInfoList) {
            galleys.add(galleyInfo.getImageFileUrl());
        }
        mPhotosSnpl.removeAllViews();
        mPhotosSnpl.setData(galleys);
    }

    @Override
    public void onDeleteShopGalley() {
        rightTitleTV.setText("选择");
        isGalleyOperated = false;
        mPhotosSnpl.setGalleyOperated(false);
        deleteGalley.setEnabled(false);
        mPhotosSnpl.setDeleteDrawableResId(0);
        showProgressDialog(R.drawable.success, getResources().getString(R.string.delete_success));
        mHandler.postDelayed(deleteShopGalleySuccessRunnable, 500);
    }

    @Override
    public void onDeleteGalleyFail() {
        rightTitleTV.setText("选择");
        isGalleyOperated = false;
        mPhotosSnpl.setGalleyOperated(false);
        deleteGalley.setEnabled(false);
        mPhotosSnpl.setDeleteDrawableResId(0);
    }

    @Override
    public void onUploadGalley() {
        if (!Utils.isNetUsable(GalleyActivity.this)) {
            showSnackbar(getResources().getString(R.string.net_unusable));
            return;
        }
        switch (identityType) {
            case 0:
                mPresenter.getPersonalGalley(Constants.GET_PERSONAL_GALLEY_ACTION_CODE, "000000", custCode);
                break;
            case 1:
                mPresenter.getShopGalley(Constants.GET_SHOP_GALLEY_ACTION_CODE, "000000", SharedPreferencesUtils.getString(GalleyActivity.this, "baza_code", ""));
                break;
        }
    }

    @Override
    public void setPresenter(GalleyContract.GalleyPreviewPresenter presenter) {
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
