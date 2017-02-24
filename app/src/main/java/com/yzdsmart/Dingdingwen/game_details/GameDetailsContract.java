package com.yzdsmart.Dingdingwen.game_details;

import com.yzdsmart.Dingdingwen.BasePresenter;
import com.yzdsmart.Dingdingwen.BaseView;
import com.yzdsmart.Dingdingwen.http.response.GameTaskRequestResponse;

/**
 * Created by YZD on 2017/2/24.
 */

public interface GameDetailsContract {
    interface GameDetailsView extends BaseView<GameDetailsPresenter> {

        /**
         * 游戏活动任务
         *
         * @param flag
         * @param msg
         * @param data
         */
        void onGameScanQRCode(boolean flag, String msg, GameTaskRequestResponse.DataBean data);
    }

    interface GameDetailsPresenter extends BasePresenter {
        /**
         * 游戏活动任务
         *
         * @param action
         * @param submitCode
         * @param custCode
         * @param gameCode
         * @param authorization
         */
        void getGameTasks(String action, String submitCode, String custCode, String gameCode, String authorization);

        void unRegisterSubscribe();
    }
}
