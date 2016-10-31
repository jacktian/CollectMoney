package com.yzdsmart.Dingdingwen.main.find_money;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
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
//    private ArrayList<BitmapDescriptor> coinGif;

    public FindMoneyPresenter(Context context, FindMoneyContract.FindMoneyView mView) {
        this.context = context;
        this.mView = mView;
        mModel = new FindMoneyModel();
        mView.setPresenter(this);
//        coinGif = new ArrayList<BitmapDescriptor>();
    }

    @Override
    public void getShopList(String submitCode, String coor, Integer range, Integer pageIndex, Integer pageSize) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getShopList(submitCode, coor, range, pageIndex, pageSize, new RequestListener() {
                    @Override
                    public void onSuccess(Object result) {
//                        coinGif.add(BitmapDescriptorFactory.fromResource(R.mipmap.coin_1));
//                        coinGif.add(BitmapDescriptorFactory.fromResource(R.mipmap.coin_2));
//                        coinGif.add(BitmapDescriptorFactory.fromResource(R.mipmap.coin_3));
//                        coinGif.add(BitmapDescriptorFactory.fromResource(R.mipmap.coin_4));
//                        coinGif.add(BitmapDescriptorFactory.fromResource(R.mipmap.coin_5));
//                        coinGif.add(BitmapDescriptorFactory.fromResource(R.mipmap.coin_6));
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
                                View packageView = ((BaseActivity) context).getLayoutInflater().inflate(R.layout.red_package_icon, null);
                                TextView amountTV = (TextView) packageView.findViewById(R.id.package_amount);
                                amountTV.setText("" + shop.getReleGold());
                                options = new MarkerOptions().position(point).extraInfo(bundle).icon(BitmapDescriptorFactory.fromView(packageView));
//                                options = new MarkerOptions().position(point).extraInfo(bundle).icons(coinGif).period(10);//动画速度
                                optionsList.add(options);
                            }
                            mView.onGetShopList(optionsList);
                        }
                    }

                    @Override
                    public void onError(String err) {
                        ((BaseActivity) context).hideProgressDialog();
                        ((BaseActivity) context).showSnackbar(err);
                    }

                    @Override
                    public void onComplete() {
                        ((BaseActivity) context).hideProgressDialog();
                    }
                }
        );
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
    public void getPersonNearby(String submitCode, String custCode, String coor, Integer pageIndex, Integer pageSize) {
        mModel.getPersonNearby(submitCode, custCode, coor, pageIndex, pageSize, new RequestListener() {
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
}
