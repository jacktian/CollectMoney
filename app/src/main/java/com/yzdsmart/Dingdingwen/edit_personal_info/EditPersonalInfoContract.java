package com.yzdsmart.Dingdingwen.edit_personal_info;

import com.yzdsmart.Dingdingwen.BasePresenter;
import com.yzdsmart.Dingdingwen.BaseView;
import com.yzdsmart.Dingdingwen.http.response.CustDetailInfoRequestResponse;
import com.yzdsmart.Dingdingwen.http.response.CustInfoRequestResponse;

/**
 * Created by YZD on 2016/9/24.
 */

public interface EditPersonalInfoContract {
    interface EditPersonalInfoView extends BaseView<EditPersonalInfoPresenter> {
        /**
         * 获取用户信息
         *
         * @param response
         */
        void onGetCustInfo(CustInfoRequestResponse response);

        /**
         * 获取用户详细信息
         *
         * @param response
         */
        void onGetCustDetailInfo(CustDetailInfoRequestResponse response);

        /**
         * 设置用户详细信息
         *
         * @param editItem
         */
        void onSetCustDetailInfo(Integer editItem);
    }

    interface EditPersonalInfoPresenter extends BasePresenter {
        /**
         * 获取用户信息
         *
         * @param submitcode
         * @param custCode
         */
        void getCustInfo(String submitcode, String custCode);

        /**
         * 获取用户详细信息
         *
         * @param actioncode
         * @param submitCode
         * @param custCode
         * @param selfCustCode
         * @return
         */
        void getCustDetailInfo(String actioncode, String submitCode, String custCode, String selfCustCode);

        /**
         * 设置用户详细信息
         *
         * @param editItem
         * @param submitCode
         * @param custCode
         * @param cName
         * @param cNickName
         * @param cSex
         * @param cBirthday
         * @param cTel
         * @param cIdNo
         * @param cNation
         * @param cHeight
         * @param cWeight
         * @param cProfession
         * @param cAddress
         * @param cProv
         * @param cCity
         * @param cDist
         * @param cCountry
         * @param cRemark
         */
        void setCustDetailInfo(Integer editItem, String submitCode, String custCode, String cName, String cNickName, String cSex, String cBirthday, String cTel, String cIdNo, String cNation, Double cHeight, Double cWeight, String cProfession, String cAddress, String cProv, String cCity, String cDist, String cCountry, String cRemark);

        void unRegisterSubscribe();
    }
}
