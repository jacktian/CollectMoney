package com.yzdsmart.Collectmoney.personal_coin_list;

import com.yzdsmart.Collectmoney.BasePresenter;
import com.yzdsmart.Collectmoney.BaseView;
import com.yzdsmart.Collectmoney.bean.GetCoinsLog;

import java.util.List;

/**
 * Created by YZD on 2016/9/5.
 */
public interface PersonalCoinsContract {
    interface PersonalCoinsView extends BaseView<PersonalCoinsPresenter> {
        void onGetCoinsLog(List<GetCoinsLog> logList);
    }

    interface PersonalCoinsPresenter extends BasePresenter {
        /**
         * 用户获取金币日志列表
         *
         * @param action
         * @param submitCode
         * @param custCode
         * @param pageIndex
         * @param pageSize
         */
        void getCoinsLog(String action, String submitCode, String custCode, Integer pageIndex, Integer pageSize);

        void unRegisterSubscribe();
    }
}
