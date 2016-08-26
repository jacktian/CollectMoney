package com.yzdsmart.Collectmoney.main.find_money;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.MoneyApp;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.bean.Shop;
import com.yzdsmart.Collectmoney.bean.ShopParcelable;
import com.yzdsmart.Collectmoney.bean.map.GEOContents;
import com.yzdsmart.Collectmoney.bean.map.POIContent;
import com.yzdsmart.Collectmoney.http.RequestListener;
import com.yzdsmart.Collectmoney.listener.HttpRequestListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YZD on 2016/8/23.
 */
public class FindMoneyPresenter implements FindMoneyContract.FindMoneyPresenter {
    private Context context;
    private FindMoneyContract.FindMoneyView mView;
    private FindMoneyModel mModel;

    public FindMoneyPresenter(Context context, FindMoneyContract.FindMoneyView mView) {
        this.context = context;
        this.mView = mView;
        mModel = new FindMoneyModel();
        mView.setPresenter(this);
    }

    @Override
    public void getShopList(String submitCode, String coor, Integer pageIndex, Integer pageSize) {
        mModel.getShopList(submitCode, coor, pageIndex, pageSize, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                List<Shop> shopList = (List<Shop>) result;
                if (null != shopList) {
                    // 构建MarkerOption，用于在地图上添加Marker
                    MarkerOptions options;
                    List<MarkerOptions> optionsList = new ArrayList<MarkerOptions>();
                    for (Shop shop : shopList) {
                        //给marker加上标签
                        Bundle bundle = new Bundle();
                        ShopParcelable parcelable = new ShopParcelable(shop);
                        bundle.putParcelable("shop", parcelable);
                        String coor = shop.getCoor();
                        // 定义Maker坐标点
                        LatLng point = new LatLng(Double.valueOf(coor.split(",")[1]), Double.valueOf(coor.split(",")[0]));
                        options = new MarkerOptions().position(point).extraInfo(bundle).icon(BitmapDescriptorFactory.fromBitmap(setNumToIcon(shop.getReleGold())));
                        optionsList.add(options);
                    }
                    mView.onGetShopList(optionsList);
                }
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).showSnackbar(err);
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
        BitmapDrawable bd = (BitmapDrawable) MoneyApp.getAppInstance().getResources().getDrawable(
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
//                bd = (BitmapDrawable) MoneyApp.getAppInstance().getResources().getDrawable(R.mipmap.shop_marker_1);
//                break;
//            case 2:
//                bd = (BitmapDrawable) MoneyApp.getAppInstance().getResources().getDrawable(R.mipmap.shop_marker_2);
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
