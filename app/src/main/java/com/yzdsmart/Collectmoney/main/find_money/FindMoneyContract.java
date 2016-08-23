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
         * 搜索附近商家成功，返回在地图上显示
         *
         * @param optionsList
         */
        void findMoneySuccess(List<MarkerOptions> optionsList);
    }

    interface FindMoneyPresenter extends BasePresenter {
        /**
         * 搜索附近商家
         *
         * @param ak
         * @param geo_table_id
         * @param keyWord
         * @param qLocation
         * @param radius
         * @param pageSize
         * @param pageIndex
         * @param m_code
         * @param filter
         */
        void findMoney(String ak, Integer geo_table_id, String keyWord, String qLocation, Integer radius, Integer pageSize, Integer pageIndex, String m_code, String filter);

        /**
         * 取消网络请求
         */
        void unRegisterSubscribe();
    }
}
