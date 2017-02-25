package com.yzdsmart.Dingdingwen.game_details;

import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.ui.divideritemdecoration.HorizontalDividerItemDecoration;
import com.umeng.analytics.MobclickAgent;
import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.Constants;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.http.response.GameTaskRequestResponse;
import com.yzdsmart.Dingdingwen.qr_scan.QRScannerActivity;
import com.yzdsmart.Dingdingwen.utils.SharedPreferencesUtils;
import com.yzdsmart.Dingdingwen.utils.Utils;
import com.yzdsmart.Dingdingwen.views.CustomRoundProgress;
import com.yzdsmart.Dingdingwen.views.SlideLockView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;
import butterknife.Optional;

/**
 * Created by YZD on 2017/2/24.
 */

public class GameDetailsActivity extends BaseActivity implements GameDetailsContract.GameDetailsView {
    @Nullable
    @BindViews({R.id.left_title, R.id.title_logo, R.id.center_title, R.id.title_right_operation_layout})
    List<View> hideViews;
    @Nullable
    @BindView(R.id.title_left_operation)
    ImageView titleLeftOpeIV;
    @Nullable
    @BindView(R.id.current_task_name)
    TextView currentTaskNameTV;
    @Nullable
    @BindView(R.id.current_task_time)
    TextView currentTaskTimeTV;
    @Nullable
    @BindView(R.id.task_items)
    RecyclerView taskItemsRV;
    @Nullable
    @BindView(R.id.total_time_stub)
    ViewStub totalTimeStub;
    TextView countTimerTV;
    @Nullable
    @BindView(R.id.lock_progress)
    CustomRoundProgress lockProgressCRP;
    @Nullable
    @BindView(R.id.count_layout)
    RelativeLayout countLayoutRL;
    @Nullable
    @BindView(R.id.unlock_btn)
    SlideLockView unlockBtnSLV;

    private static final String TAG = "GameDetailsActivity";

    private String gameCode;

    private GameDetailsContract.GameDetailsPresenter mPresenter;

    private LinearLayoutManager mLinearLayoutManager;
    private Paint dividerPaint;
    private List<GameTaskRequestResponse.DataBean.TaskListsBean> tasksList;
    private GameTasksAdapter gameTasksAdapter;

    private Handler mHandler = new Handler();
    private Runnable lockRunnable = null;
    private static final Integer DEFAULT_MAX_PROCESS = 100;
    //锁屏进度
    private Integer lockProgress = 0;
    //判断是否离开锁屏按钮
    private Boolean isLockPressed = true;

    private Boolean isScreenLocked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tasksList = new ArrayList<GameTaskRequestResponse.DataBean.TaskListsBean>();

        if (null != savedInstanceState) {
            gameCode = savedInstanceState.getString("gameCode");
        } else {
            gameCode = getIntent().getExtras().getString("gameCode");
        }

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow_white));

        new GameDetailsPresenter(this, this);

        MobclickAgent.openActivityDurationTrack(false);

        unlockBtnSLV.setmLockListener(new SlideLockView.OnLockListener() {
            @Override
            public void onOpenLockSuccess() {
                isScreenLocked = false;
                ButterKnife.apply(countLayoutRL, BUTTERKNIFEVISIBLE);
                ButterKnife.apply(titleLeftOpeIV, BUTTERKNIFEVISIBLE);
                ButterKnife.apply(unlockBtnSLV, BUTTERKNIFEGONE);
            }
        });

        lockRunnable = new Runnable() {
            @Override
            public void run() {
                lockProgress += 2;
                lockProgressCRP.setProgress(lockProgress);
                if (DEFAULT_MAX_PROCESS == lockProgress) {
                    isScreenLocked = true;
                    ButterKnife.apply(titleLeftOpeIV, BUTTERKNIFEGONE);
                    ButterKnife.apply(countLayoutRL, BUTTERKNIFEGONE);
                    lockProgressCRP.setVisibility(View.GONE);
                    mHandler.removeCallbacks(this);
                    lockProgress = 0;
                    lockProgressCRP.setProgress(lockProgress);
                    ButterKnife.apply(unlockBtnSLV, BUTTERKNIFEVISIBLE);
                } else {
                    mHandler.postDelayed(this, 1);
                }
            }
        };

        mLinearLayoutManager = new LinearLayoutManager(this);
        dividerPaint = new Paint();
        dividerPaint.setStrokeWidth(1);
        dividerPaint.setColor(getResources().getColor(R.color.light_grey));
        dividerPaint.setAntiAlias(true);
        dividerPaint.setPathEffect(new DashPathEffect(new float[]{25.0f, 25.0f}, 0));
        HorizontalDividerItemDecoration dividerItemDecoration = new HorizontalDividerItemDecoration.Builder(this).paint(dividerPaint).build();

        gameTasksAdapter = new GameTasksAdapter(this);
        taskItemsRV.setHasFixedSize(true);
        taskItemsRV.setLayoutManager(mLinearLayoutManager);
        taskItemsRV.addItemDecoration(dividerItemDecoration);
        taskItemsRV.setAdapter(gameTasksAdapter);

        if (!Utils.isNetUsable(this)) {
            showSnackbar(getResources().getString(R.string.net_unusable));
            return;
        }
        mPresenter.getGameTasks(Constants.GAME_TASK_LIST_ACTION_CODE, "000000", SharedPreferencesUtils.getString(GameDetailsActivity.this, "cust_code", ""), gameCode, SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_game_details;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    public void onPause() {
        MobclickAgent.onPageEnd(TAG); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.unRegisterSubscribe();
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacks(lockRunnable);
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("gameCode", gameCode);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (isScreenLocked) {
                    showSnackbar("请先解锁");
                    return true;
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Optional
    @OnClick({R.id.title_left_operation_layout, R.id.scan_btn, R.id.close_btn})
    void onClick(View view) {
        Bundle bundle;
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
            case R.id.close_btn:
                closeActivity();
                break;
            case R.id.scan_btn:
                bundle = new Bundle();
                bundle.putInt("scanType", 0);
                openActivity(QRScannerActivity.class, bundle, 0);
                closeActivity();
                break;
        }
    }

    @Optional
    @OnTouch(R.id.lock_btn)
    boolean startLock(View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startLock();
                break;
            case MotionEvent.ACTION_MOVE:
                //手指移动离开点名按钮
                if (event.getX() < 0 || Utils.px2dp(this, event.getX()) > 50 || event.getY() < 0 || Utils.px2dp(this, event.getY()) > 50) {
                    isLockPressed = false;
                    submitLock();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isLockPressed) {
                    submitLock();
                }
                break;
        }
        return false;
    }

    /**
     * 开始锁定
     */
    private void startLock() {
        isLockPressed = true;
        lockProgressCRP.setVisibility(View.VISIBLE);
        mHandler.postDelayed(lockRunnable, 1);
    }

    /**
     * 手指离开锁定按钮
     */
    private void submitLock() {
        lockProgressCRP.setVisibility(View.GONE);
        mHandler.removeCallbacks(lockRunnable);
        lockProgress = 0;
        lockProgressCRP.setProgress(lockProgress);
    }

    @Override
    public void setPresenter(GameDetailsContract.GameDetailsPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onGameScanQRCode(boolean flag, String msg, GameTaskRequestResponse.DataBean gameData) {
        if (gameData.getGameSumTime().length() > 0) {
            totalTimeStub.inflate();
            countTimerTV = (TextView) findViewById(R.id.count_timer);
            countTimerTV.setText(gameData.getGameSumTime());
            tasksList.clear();
            tasksList.addAll(gameData.getTaskLists());
            gameTasksAdapter.clearList();
            gameTasksAdapter.appendList(tasksList);
        } else {
            if (gameData.getTaskingCode().length() > 0) {
                currentTaskNameTV.setText(gameData.getTaskingName());
                tasksList.clear();
                tasksList.addAll(gameData.getTaskLists());
                gameTasksAdapter.clearList();
                gameTasksAdapter.appendList(tasksList);
            }
        }
    }
}
