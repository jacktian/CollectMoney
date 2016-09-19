package com.yzdsmart.Collectmoney.main.find_money;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.yzdsmart.Collectmoney.App;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.http.RequestListener;
import com.yzdsmart.Collectmoney.http.response.ShopListRequestResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YZD on 2016/8/23.
 */
public class FindMoneyPresenter implements FindMoneyContract.FindMoneyPresenter {
    private Context context;
    private FindMoneyContract.FindMoneyView mView;
    private FindMoneyModel mModel;
    private ArrayList<BitmapDescriptor> coinGif;

    public FindMoneyPresenter(Context context, FindMoneyContract.FindMoneyView mView) {
        this.context = context;
        this.mView = mView;
        mModel = new FindMoneyModel();
        mView.setPresenter(this);
        coinGif = new ArrayList<BitmapDescriptor>();
    }

    @Override
    public void getShopList(String submitCode, String coor, Integer pageIndex, Integer pageSize, final Integer type) {
        switch (type) {
            case 0:
                mView.startRadarScan();
                break;
            case 1:
                ((BaseActivity) context).showProgressDialog(R.drawable.loading,context.getResources().getString(R.string.loading));
                break;
        }
        mModel.getShopList(submitCode, coor, pageIndex, pageSize, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                coinGif.add(BitmapDescriptorFactory.fromResource(R.mipmap.coin_1));
                coinGif.add(BitmapDescriptorFactory.fromResource(R.mipmap.coin_2));
                coinGif.add(BitmapDescriptorFactory.fromResource(R.mipmap.coin_3));
                coinGif.add(BitmapDescriptorFactory.fromResource(R.mipmap.coin_4));
                coinGif.add(BitmapDescriptorFactory.fromResource(R.mipmap.coin_5));
                coinGif.add(BitmapDescriptorFactory.fromResource(R.mipmap.coin_6));
                List<ShopListRequestResponse> shopList = (List<ShopListRequestResponse>) result;
                if (null != shopList) {
                    // 构建MarkerOption，用于在地图上添加Marker
                    MarkerOptions options;
                    List<MarkerOptions> optionsList = new ArrayList<MarkerOptions>();
                    for (ShopListRequestResponse shop : shopList) {
                        //给marker加上标签
                        Bundle bundle = new Bundle();
                        bundle.putString("bazaCode", shop.getCode());
                        String coor = shop.getCoor();
                        // 定义Maker坐标点
                        LatLng point = new LatLng(Double.valueOf(coor.split(",")[1]), Double.valueOf(coor.split(",")[0]));
//                        options = new MarkerOptions().position(point).extraInfo(bundle).icon(BitmapDescriptorFactory.fromBitmap(setNumToIcon(shop.getReleGold())));
                        options = new MarkerOptions().position(point).extraInfo(bundle).icons(coinGif).period(10);//动画速度
                        optionsList.add(options);
                    }
                    mView.onGetShopList(optionsList);
                }
            }

            @Override
            public void onError(String err) {
                switch (type) {
                    case 0:
                        mView.stopRadarScan();
                        break;
                    case 1:
                        ((BaseActivity) context).hideProgressDialog();
                        break;
                }
                ((BaseActivity) context).showSnackbar(err);
            }

            @Override
            public void onComplete() {
                switch (type) {
                    case 0:
                        mView.stopRadarScan();
                        break;
                    case 1:
                        ((BaseActivity) context).hideProgressDialog();
                        break;
                }
            }
        });
    }

    @Override
    public void uploadCoor(String submitCode, String custCode, String coor) {
        mModel.uploadCoor(submitCode, custCode, coor, new RequestListener() {
            @Override
            public void onSuccess(Object result) {

            }

            @Override
            public void onError(String err) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void getPersonBearby(String submitCode, String custCode, String coor, Integer pageIndex, Integer pageSize) {
        mModel.getPersonBearby(submitCode, custCode, coor, pageIndex, pageSize, new RequestListener() {
            @Override
            public void onSuccess(Object result) {

            }

            @Override
            public void onError(String err) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void unRegisterSubscribe() {
        mModel.unRegisterSubscribe();
    }

    /**
     * 往图片添加数字
     * 设置marker图标
     */
    private Bitmap setNumToIcon(int num) {
        BitmapDrawable bd = (BitmapDrawable) App.getAppInstance().getResources().getDrawable(
                R.mipmap.icon_gcoding);
        Bitmap bitmap = bd.getBitmap().copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setTextSize(20);
        float margin;
        int widthX = 11;
        int heightY = 0;
        if (num < 10) {
            margin = bitmap.getWidth() / 1.6f;
        } else {
            margin = bitmap.getWidth() / 2;
        }
        canvas.drawText(String.valueOf(num),
                (margin - widthX),
                ((bitmap.getHeight() / 2) + heightY), paint);

        return bitmap;
    }

//    /**
//     * 往图片添加数字
//     * 设置marker图标
//     */
//    private Bitmap setNumToIcon(int num, int markerNum) {
//        BitmapDrawable bd = null;
//        switch (markerNum) {
//            case 1:
//                bd = (BitmapDrawable) App.getAppInstance().getResources().getDrawable(R.mipmap.shop_marker_1);
//                break;
//            case 2:
//                bd = (BitmapDrawable) App.getAppInstance().getResources().getDrawable(R.mipmap.shop_marker_2);
//                break;
//        }
//        Bitmap bitmap = bd.getBitmap().copy(Bitmap.Config.ARGB_8888, true);
//        Canvas canvas = new Canvas(bitmap);
//
//        Paint paint = new Paint();
//        paint.setColor(Color.RED);
//        paint.setAntiAlias(true);
//        paint.setTextSize(18);
//        float margin;
//        int widthX = 11;
//        int heightY = 0;
//        if (num < 10) {
//            margin = bitmap.getWidth() / 1.5f;
//        } else {
//            margin = bitmap.getWidth() / 2;
//        }
//        canvas.drawText(String.valueOf(num),
//                (margin - widthX),
//                ((bitmap.getHeight() / 2) + heightY), paint);
//
//        return bitmap;
//    }
//
//    private Bitmap getViewBitmap(View viewContent) {
//        viewContent.setDrawingCacheEnabled(true);
//        viewContent.measure(
//                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//        viewContent.layout(0, 0,
//                viewContent.getMeasuredWidth(),
//                viewContent.getMeasuredHeight());
//        viewContent.buildDrawingCache();
//        Bitmap cacheBitmap = viewContent.getDrawingCache();
//        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
//        return bitmap;
//    }
}
