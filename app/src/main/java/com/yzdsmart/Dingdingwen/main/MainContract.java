package com.yzdsmart.Dingdingwen.main;

import com.amap.api.maps.model.MarkerOptions;
import com.tencent.TIMConversation;
import com.tencent.TIMGroupPendencyItem;
import com.tencent.TIMMessage;
import com.yzdsmart.Dingdingwen.BasePresenter;
import com.yzdsmart.Dingdingwen.BaseView;
import com.yzdsmart.Dingdingwen.bean.CoinType;
import com.yzdsmart.Dingdingwen.bean.MarketShop;
import com.yzdsmart.Dingdingwen.http.response.MarketsInfoRequestResponse;

import java.util.List;

/**
 * Created by YZD on 2016/8/30.
 */
public interface MainContract {
    interface MainView extends BaseView<MainPresenter> {
        /**
         * TLS账号登陆成功
         */
        void chatLoginSuccess();

        /**
         * im sdk登录成功
         */
        void imSDKLoginSuccess();

        /**
         * 腾讯云通信下线
         */
        void onIMOffline();

        /**
         * 初始化界面或刷新界面
         */
        void initConversations(List<TIMConversation> conversationList);

        /**
         * 更新最新消息显示
         *
         * @param message 最后一条消息
         */
        void updateConversation(TIMMessage message);

        /**
         * 获取群管理最后一条系统消息的回调
         *
         * @param message     最后一条消息
         * @param unreadCount 未读数
         */
        void onGetGroupManageLastMessage(TIMGroupPendencyItem message, long unreadCount);

        /**
         * 重新注册app
         */
        void reRegisterApp();

        void onAppRegister(boolean flag);

        /**
         * 获取 refresh token 和 access token
         */
        void getRefreshToken();

        /**
         * 刷新 refresh token 和 access token
         */
        void refreshAccessToken();

        /**
         * 获取个人金币列表（背包功能）
         *
         * @param coinTypes
         */
        void onGetBackgroundBag(List<CoinType> coinTypes);

        void onDismissBackgroundBag();

        void updateAccessToken();

        /**
         * 获取周边商铺列表
         *
         * @param optionsList
         */
        void onGetShopList(List<MarkerOptions> optionsList);

        /**
         * 获取综合体列表
         *
         * @param flag
         * @param marketsInfo
         */
        void onGetMarketsInfo(boolean flag, List<MarketsInfoRequestResponse.DataBean> marketsInfo);

        /**
         * 综合体商铺列表
         *
         * @param marketShops
         */
        void onGetMarketShops(List<MarketShop> marketShops);
    }

    interface MainPresenter extends BasePresenter {
        /**
         * 用户聊天登录
         *
         * @param im_name
         * @param im_pwd
         */
        void chatLogin(String im_name, String im_pwd);

        /**
         * im sdk 登录
         */
        void imSDKLogin();

        /**
         * 获取会话
         */
        void getConversation();

        /**
         * 获取群管理最有一条消息,和未读消息数
         * 包括：加群等已决和未决的消息
         */
        void getGroupManageLastMessage();

        void unRegisterObserver();

        /**
         * 注册应用
         *
         * @param appCode
         * @param appId
         * @param appSecret
         */
        void appRegister(String appCode, String appId, String appSecret);

        /**
         * 获取refresh token 和 access token
         *
         * @param grantType
         * @param userName
         * @param password
         */
        void getRefreshToken(String grantType, String userName, String password);

        /**
         * 刷新 access token
         *
         * @param grantType
         * @param refreshToken
         */
        void refreshAccessToken(String grantType, String refreshToken);

        /**
         * 获取个人金币列表（背包功能）
         *
         * @param action
         * @param actiontype
         * @param submitCode
         * @param custCode
         * @param authorization
         */
        void personalBackgroundBag(String action, String actiontype, String submitCode, String custCode, String authorization);

        /**
         * 获取商铺金币列表（背包功能）
         *
         * @param action
         * @param submitCode
         * @param bazaCode
         * @param authorization
         */
        void shopBackgroundBag(String action, String submitCode, String bazaCode, String authorization);

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
         * 获取综合体列表
         *
         * @param action
         * @param submitCode
         * @param custCode
         * @param authorization
         */
        void getMarketsInfo(String action, String submitCode, String custCode, String authorization);

        /**
         * 综合体商铺列表
         *
         * @param submitCode
         * @param complexKeyword
         * @param storeyKeyword
         * @param pageIndex
         * @param pageSize
         * @param authorization
         */
        void getMarketShops(String submitCode, String complexKeyword, String storeyKeyword, Integer pageIndex, Integer pageSize, String authorization);

        /**
         * 取消网络请求
         */
        void unRegisterSubscribe();
    }
}
