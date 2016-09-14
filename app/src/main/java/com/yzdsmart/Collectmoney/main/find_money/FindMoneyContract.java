package com.yzdsmart.Collectmoney.main.find_money;

import com.baidu.mapapi.map.MarkerOptions;
import com.yzdsmart.Collectmoney.BasePresenter;
import com.yzdsmart.Collectmoney.BaseView;

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
         * @param pageIndex
         * @param pageSize
         * @param type       0 扫描商铺 1 商场附近商铺
         */
        void getShopList(String submitCode, String coor, Integer pageIndex, Integer pageSize, Integer type);

        /**
         * 上传坐标
         *
         * @param submitCode
         * @param custCode
         * @param coor
         */
        void uploadCoor(String submitCode, String custCode, String coor);

        /**
         * 获取当前用户周边用户
         *
         * @param submitCode
         * @param custCode
         * @param coor
         * @param pageIndex
         * @param pageSize
         */
        void getPersonBearby(String submitCode, String custCode, String coor, Integer pageIndex, Integer pageSize);

        /**
         * 取消网络请求
         */
        void unRegisterSubscribe();
    }
}
