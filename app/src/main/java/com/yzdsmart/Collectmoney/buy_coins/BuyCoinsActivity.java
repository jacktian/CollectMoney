package com.yzdsmart.Collectmoney.buy_coins;

import android.app.Dialog;
import android.content.Context;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.ui.divideritemdecoration.HorizontalDividerItemDecoration;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.bean.BuyCoinsLog;
import com.yzdsmart.Collectmoney.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by YZD on 2016/9/4.
 */
public class BuyCoinsActivity extends BaseActivity implements BuyCoinsContract.BuyCoinsView {
    @Nullable
    @BindViews({R.id.left_title, R.id.title_logo, R.id.title_right_operation_layout})
    List<View> hideViews;
    @Nullable
    @BindView(R.id.center_title)
    TextView centerTitleTV;
    @Nullable
    @BindView(R.id.title_left_operation)
    ImageView titleLeftOpeIV;
    @Nullable
    @BindView(R.id.coin_counts)
    EditText coinCountsET;
    @Nullable
    @BindView(R.id.coin_list)
    RecyclerView coinListRV;

    private static final String BUY_COIN_CODE = "66";
    private static final String BUY_COIN_LOG_CODE = "688";

    private Integer pageIndex = 1;
    private static final Integer PAGE_SIZE = 10;

    private LinearLayoutManager mLinearLayoutManager;
    private Paint dividerPaint;
    private List<BuyCoinsLog> logList;
    private BuyCoinsLogAdapter buyCoinsLogAdapter;

    private BuyCoinsContract.BuyCoinsPresenter mPresenter;

    private Handler mHandler = new Handler();
    private Runnable closeRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        logList = new ArrayList<BuyCoinsLog>();

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow));
        centerTitleTV.setText("购买金币");

        new BuyCoinsPresenter(this, this);

        closeRunnable = new Runnable() {
            @Override
            public void run() {
                closeActivity();
            }
        };

        mLinearLayoutManager = new LinearLayoutManager(this);
        dividerPaint = new Paint();
        dividerPaint.setStrokeWidth(1);
        dividerPaint.setColor(getResources().getColor(R.color.light_grey));
        dividerPaint.setAntiAlias(true);
        dividerPaint.setPathEffect(new DashPathEffect(new float[]{25.0f, 25.0f}, 0));
        HorizontalDividerItemDecoration dividerItemDecoration = new HorizontalDividerItemDecoration.Builder(this).paint(dividerPaint).build();

        buyCoinsLogAdapter = new BuyCoinsLogAdapter(this);
        coinListRV.setHasFixedSize(true);
        coinListRV.setLayoutManager(mLinearLayoutManager);
        coinListRV.addItemDecoration(dividerItemDecoration);
        coinListRV.setAdapter(buyCoinsLogAdapter);

        mPresenter.buyCoinsLog(BUY_COIN_LOG_CODE, "000000", SharedPreferencesUtils.getString(this, "baza_code", ""), pageIndex, PAGE_SIZE);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_buy_coins;
    }

    @Optional
    @OnClick({R.id.title_left_operation_layout, R.id.buy_coin})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                closeActivity();
                break;
            case R.id.buy_coin:
                if (!requiredVerify(coinCountsET)) {
                    coinCountsET.setError(getResources().getString(R.string.buy_coin_coin_count_required));
                    return;
                }
                showMoveDialog(this, Integer.valueOf(coinCountsET.getText().toString()));
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.unRegisterSubscribe();
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacks(closeRunnable);
        super.onDestroy();
    }

    @Override
    public void setPresenter(BuyCoinsContract.BuyCoinsPresenter presenter) {
        mPresenter = presenter;
    }

    private Dialog buyCoinDialog;
    private TextView payCoin;
    private Button payButton;

    private void showMoveDialog(Context context, final Integer coinCounts) {
        buyCoinDialog = new Dialog(context, R.style.buy_coin_popup);
        buyCoinDialog.setContentView(R.layout.buy_coin_pay);
        payButton = (Button) buyCoinDialog.findViewById(R.id.pay_btn);
        payCoin = (TextView) buyCoinDialog.findViewById(R.id.pay_coin);
        payCoin.setText("" + coinCounts);
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.buyCoins(BUY_COIN_CODE, "000000", SharedPreferencesUtils.getString(BuyCoinsActivity.this, "baza_code", ""), coinCounts);
                buyCoinDialog.dismiss();
            }
        });
        Window window = buyCoinDialog.getWindow();
        window.setGravity(Gravity.TOP | Gravity.RIGHT);
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setAttributes(lp);
        buyCoinDialog.show();
    }

    @Override
    public void onBuyCoins(boolean flag, String msg) {
        showSnackbar(msg);
        if (!flag) {
            return;
        }
        mHandler.postDelayed(closeRunnable, 1500);
    }

    @Override
    public void onBuyCoinsLog(List<BuyCoinsLog> logList) {
        this.logList.clear();
        this.logList.addAll(logList);
        buyCoinsLogAdapter.appendList(this.logList);
    }
}
