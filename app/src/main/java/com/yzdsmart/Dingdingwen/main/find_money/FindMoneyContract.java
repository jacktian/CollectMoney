package com.yzdsmart.Dingdingwen.main.find_money;

import com.amap.api.maps.model.MarkerOptions;
import com.yzdsmart.Dingdingwen.BasePresenter;
import com.yzdsmart.Dingdingwen.BaseView;

import java.util.List;

/**
 * Created by YZD on 2016/8/23.
 */
public class FindMoneyContract {
    interface FindMoneyView extends BaseView<FindMoneyPresenter> {
        /**
         * 获取周边商铺列表
         *
         * @param optionsList
         */
        void onGetShopList(List<MarkerOptions> optionsList);

        /**
         * 开始扫描
         */
        void startRadarScan();

        /**
         * 停止扫描
         */
        void stopRadarScan();
    }

    interface FindMoneyPresenter extends BasePresenter {
        /**
         * 获取周边店铺
         *
         * @param submitCode
         * @param coor
         * @param range
         * @param pageIndex
         * @param pageSize
         */
        void getShopList(String submitCode, String coor, Integer range, Integer pageIndex, Integer pageSize, String authorization);

        /**
         * 上传坐标
         *
         * @param submitCode
         * @param custCode
         * @param coor
         */
        void uploadCoor(String submitCode, String custCode, String coor, String authorization);

        /**
         * 获取当前用户周边用户
         *
         * @param submitCode
         * @param custCode
         * @param coor
         * @param pageIndex
         * @param pageSize
         */
        void getPersonNearby(String submitCode, String custCode, String coor, Integer pageIndex, Integer pageSize, String authorization);

        /**
         * 取消网络请求
         */
        void unRegisterSubscribe();
    }
}
