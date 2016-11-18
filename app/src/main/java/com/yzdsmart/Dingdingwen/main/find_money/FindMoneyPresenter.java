package com.yzdsmart.Dingdingwen.main.find_money;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.http.response.ShopListRequestResponse;
import com.yzdsmart.Dingdingwen.main.MainActivity;

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
    public void getShopList(String submitCode, String coor, Integer range, Integer pageIndex, Integer pageSize, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getShopList(submitCode, coor, range, pageIndex, pageSize, authorization, new RequestListener() {
                    @Override
                    public void onSuccess(Object result) {
                        List<ShopListRequestResponse> shopList = (List<ShopListRequestResponse>) result;
                        if (null != shopList) {
                            // 构建MarkerOption，用于在地图上添加Marker
                            MarkerOptions options;
                            List<MarkerOptions> optionsList = new ArrayList<MarkerOptions>();
                            for (ShopListRequestResponse shop : shopList) {
                                String coor = shop.getCoor();
                                // 定义Maker坐标点
                                LatLng point = new LatLng(Double.valueOf(coor.split(",")[1]), Double.valueOf(coor.split(",")[0]));
                                View packageView = ((BaseActivity) context).getLayoutInflater().inflate(R.layout.red_package_icon, null);
                                TextView amountTV = (TextView) packageView.findViewById(R.id.package_amount);
                                amountTV.setText("" + shop.getReleGold());
                                options = new MarkerOptions().position(point).snippet(shop.getCode()).icon(BitmapDescriptorFactory.fromView(packageView));
                                optionsList.add(options);
                            }
                            mView.onGetShopList(optionsList);
                        }
                    }

                    @Override
                    public void onError(String err) {
                        ((BaseActivity) context).hideProgressDialog();
                        ((BaseActivity) context).showSnackbar(err);
                        if (err.contains("401 Unauthorized")) {
                            MainActivity.getInstance().updateAccessToken();
                        }
                    }

                    @Override
                    public void onComplete() {
                        ((BaseActivity) context).hideProgressDialog();
                    }
                }
        );
    }

    @Override
    public void uploadCoor(String submitCode, String custCode, String coor, String authorization) {
        mModel.uploadCoor(submitCode, custCode, coor, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {

            }

            @Override
            public void onError(String err) {
                if (err.contains("401 Unauthorized")) {
                    MainActivity.getInstance().updateAccessToken();
                }
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void getPersonNearby(String submitCode, String custCode, String coor, Integer pageIndex, Integer pageSize, String authorization) {
        mModel.getPersonNearby(submitCode, custCode, coor, pageIndex, pageSize, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {

            }

            @Override
            public void onError(String err) {
                if (err.contains("401 Unauthorized")) {
                    MainActivity.getInstance().updateAccessToken();
                }
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
