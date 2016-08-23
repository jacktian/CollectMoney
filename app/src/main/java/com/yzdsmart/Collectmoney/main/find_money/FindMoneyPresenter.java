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
import com.yzdsmart.Collectmoney.MoneyApp;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.bean.map.GEOContents;
import com.yzdsmart.Collectmoney.bean.map.POIContent;
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
    //周边检索
    //检索到的位置列表信息
    private List<GEOContents> poiGEOs = null;
    private List<MarkerOptions> optionsList = null;

    public FindMoneyPresenter(Context context, FindMoneyContract.FindMoneyView mView) {
        this.context = context;
        this.mView = mView;
        mModel = new FindMoneyModel();
        mView.setPresenter(this);
    }

    @Override
    public void findMoney(String ak, Integer geo_table_id, String keyWord, String qLocation, Integer radius, Integer pageSize, Integer pageIndex, String m_code, String filter) {
        mModel.findMoney(ak, geo_table_id, keyWord, qLocation, radius, pageSize, pageIndex, m_code, filter, new HttpRequestListener() {

            @Override
            public void onSuccess(Object result) {
                POIContent poiContent = (POIContent) result;
                if (0 == poiContent.getStatus()) {
                    optionsList = new ArrayList<MarkerOptions>();
                    poiGEOs = poiContent.getContents();
                    // 构建MarkerOption，用于在地图上添加Marker
                    MarkerOptions options;
                    ArrayList<BitmapDescriptor> markerGifList;
                    for (int i = 0; i < poiGEOs.size(); i++) {
                        //给marker加上标签
                        Bundle bundle = new Bundle();
                        bundle.putInt("index", i);
                        // 定义Maker坐标点
                        LatLng point = new LatLng(poiGEOs.get(i).getLocation()[1], poiGEOs.get(i).getLocation()[0]);
                        markerGifList = new ArrayList<BitmapDescriptor>();
                        markerGifList.add(BitmapDescriptorFactory.fromBitmap(setNumToIcon((i + 1), 1)));
                        markerGifList.add(BitmapDescriptorFactory.fromBitmap(setNumToIcon((i + 1), 2)));
                        options = new MarkerOptions().position(point).extraInfo(bundle).icons(markerGifList);
                        optionsList.add(options);
                    }
                    mView.findMoneySuccess(optionsList);
                }
            }

            @Override
            public void onError(Object err) {

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
    private Bitmap setNumToIcon(int num, int markerNum) {
        BitmapDrawable bd = null;
        switch (markerNum) {
            case 1:
                bd = (BitmapDrawable) MoneyApp.getAppInstance().getResources().getDrawable(R.mipmap.shop_marker_1);
                break;
            case 2:
                bd = (BitmapDrawable) MoneyApp.getAppInstance().getResources().getDrawable(R.mipmap.shop_marker_2);
                break;
        }
        Bitmap bitmap = bd.getBitmap().copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        paint.setTextSize(18);
        float margin;
        int widthX = 11;
        int heightY = 0;
        if (num < 10) {
            margin = bitmap.getWidth() / 1.5f;
        } else {
            margin = bitmap.getWidth() / 2;
        }
        canvas.drawText(String.valueOf(num),
                (margin - widthX),
                ((bitmap.getHeight() / 2) + heightY), paint);

        return bitmap;
    }

    private Bitmap getViewBitmap(View viewContent) {
        viewContent.setDrawingCacheEnabled(true);
        viewContent.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        viewContent.layout(0, 0,
                viewContent.getMeasuredWidth(),
                viewContent.getMeasuredHeight());
        viewContent.buildDrawingCache();
        Bitmap cacheBitmap = viewContent.getDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
        return bitmap;
    }
}
