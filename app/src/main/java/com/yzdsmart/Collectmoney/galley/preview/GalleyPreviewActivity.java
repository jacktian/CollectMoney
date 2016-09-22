package com.yzdsmart.Collectmoney.galley.preview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.bean.GalleyInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout;

/**
 * Created by YZD on 2016/9/21.
 */

public class GalleyPreviewActivity extends BaseActivity implements BGASortableNinePhotoLayout.Delegate {
    @Nullable
    @BindViews({R.id.left_title, R.id.title_logo, R.id.title_right_operation_layout})
    List<View> hideViews;
    @Nullable
    @BindView(R.id.title_left_operation)
    ImageView titleLeftOpeIV;
    @Nullable
    @BindView(R.id.center_title)
    TextView centerTitleTV;
    @Nullable
    @BindView(R.id.snpl_add_photos)
    BGASortableNinePhotoLayout mPhotosSnpl;

    private Integer type;
    private List<GalleyInfo> galleyInfoList;
    private ArrayList<String> deleteGalleys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        deleteGalleys = new ArrayList<String>();

        type = getIntent().getExtras().getInt("type");
        galleyInfoList = getIntent().getExtras().getParcelableArrayList("galleys");

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);

        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow));
        switch (type) {
            case 0:
                centerTitleTV.setText("个人相册");
                break;
            case 1:
                centerTitleTV.setText("好友相册");
                break;
        }

        mPhotosSnpl.init(this);
        // 设置拖拽排序控件的代理
        mPhotosSnpl.setDelegate(this);
        mPhotosSnpl.setDeleteDrawableResId(0);

        deleteGalleys.clear();
        for (GalleyInfo galleyInfo : galleyInfoList) {
            deleteGalleys.add(galleyInfo.getImageFileUrl());
        }
        mPhotosSnpl.setData(deleteGalleys);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_galley_preview;
    }

    @Optional
    @OnClick({R.id.title_left_operation_layout})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                closeActivity();
                break;
        }
    }

    @Override
    public void onClickAddNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, ArrayList<String> models) {

    }

    @Override
    public void onClickDeleteNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {

    }

    @Override
    public void onClickNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {

    }
}
