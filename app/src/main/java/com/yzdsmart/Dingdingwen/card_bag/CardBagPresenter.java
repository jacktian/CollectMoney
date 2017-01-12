package com.yzdsmart.Dingdingwen.card_bag;

import android.content.Context;

import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.bean.BankCard;
import com.yzdsmart.Dingdingwen.http.RequestListener;
import com.yzdsmart.Dingdingwen.main.MainActivity;

import java.util.List;

/**
 * Created by YZD on 2016/12/13.
 */

public class CardBagPresenter implements CardBagContract.CardBagPresenter {
    private Context context;
    private CardBagContract.CardBagView mView;
    private CardBagModel mModel;

    public CardBagPresenter(Context context, CardBagContract.CardBagView mView) {
        this.context = context;
        this.mView = mView;
        mModel = new CardBagModel();
        mView.setPresenter(this);
    }

    @Override
    public void getBankCardList(String submitCode, String custCode, String authorization) {
        ((BaseActivity) context).showProgressDialog(R.drawable.loading, context.getResources().getString(R.string.loading));
        mModel.getBankCardList(submitCode, custCode, authorization, new RequestListener() {
            @Override
            public void onSuccess(Object result) {
                List<BankCard> bankCards = (List<BankCard>) result;
                mView.onGetBankCardList(bankCards);
            }

            @Override
            public void onError(String err) {
                ((BaseActivity) context).hideProgressDialog();
//                ((BaseActivity) context).showSnackbar(context.getResources().getString(R.string.error_get_bank_card_list));
                if (err.contains("401 Unauthorized")) {
                    MainActivity.getInstance().updateAccessToken();
                }
            }

            @Override
            public void onComplete() {
                ((BaseActivity) context).hideProgressDialog();
            }
        });
    }

    @Override
    public void unRegisterSubscribe() {
        mModel.unRegisterSubscribe();
    }
}
