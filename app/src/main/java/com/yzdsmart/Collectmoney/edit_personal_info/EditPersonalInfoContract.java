package com.yzdsmart.Collectmoney.edit_personal_info;

import com.yzdsmart.Collectmoney.BasePresenter;
import com.yzdsmart.Collectmoney.BaseView;
import com.yzdsmart.Collectmoney.http.response.CustDetailInfoRequestResponse;

/**
 * Created by YZD on 2016/9/24.
 */

public interface EditPersonalInfoContract {
    interface EditPersonalInfoView extends BaseView<EditPersonalInfoPresenter> {
        /**
         * 获取用户详细信息
         *
         * @param response
         */
        void onGetCustDetailInfo(CustDetailInfoRequestResponse response);

        /**
         * 设置用户详细信息
         */
        void onSetCustDetailInfo();
    }

    interface EditPersonalInfoPresenter extends BasePresenter {
        /**
         * 获取用户详细信息
         *
         * @param actioncode
         * @param submitCode
         * @param custCode
         * @return
         */
        void getCustDetailInfo(String actioncode, String submitCode, String custCode);

        /**
         * 设置用户详细信息
         *
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
        void setCustDetailInfo(String submitCode, String custCode, String cName, String cNickName, String cSex, String cBirthday, String cTel, String cIdNo, String cNation, Double cHeight, Double cWeight, String cProfession, String cAddress, String cProv, String cCity, String cDist, String cCountry, String cRemark);

        void unRegisterSubscribe();
    }
}
