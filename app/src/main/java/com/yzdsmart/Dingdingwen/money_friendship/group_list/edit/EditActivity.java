package com.yzdsmart.Dingdingwen.money_friendship.group_list.edit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.TIMCallBack;
import com.umeng.analytics.MobclickAgent;
import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.R;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

/**
 * 修改文本页面
 */
public class EditActivity extends BaseActivity implements TIMCallBack {
    @Nullable
    @BindViews({R.id.left_title, R.id.title_logo})
    List<View> hideViews;
    @Nullable
    @BindView(R.id.title_left_operation)
    ImageView titleLeftOpeIV;
    @Nullable
    @BindView(R.id.title_right_operation)
    ImageView titleRightOpeIV;
    @Nullable
    @BindView(R.id.center_title)
    TextView centerTitleTV;
    @Nullable
    @BindView(R.id.title_left_operation_layout)
    FrameLayout titleLeftOpeLayout;
    @Nullable
    @BindView(R.id.title_right_operation_layout)
    FrameLayout titleRightOpeLayout;

    private static final String TAG = "EditActivity";

    private static EditInterface editAction;
    public final static String RETURN_EXTRA = "result";
    private static String defaultString;
    private EditText input;
    private static int lenLimit;

    /**
     * 启动修改文本界面
     *
     * @param context    fragment context
     * @param title      界面标题
     * @param defaultStr 默认文案
     * @param reqCode    请求码，用于识别返回结果
     * @param action     操作回调
     */
    public static void navToEdit(Fragment context, String title, String defaultStr, int reqCode, EditInterface action) {
        Intent intent = new Intent(context.getActivity(), EditActivity.class);
        intent.putExtra("title", title);
        context.startActivityForResult(intent, reqCode);
        defaultString = defaultStr;
        editAction = action;
    }


    /**
     * 启动修改文本界面
     *
     * @param context    activity context
     * @param title      界面标题
     * @param defaultStr 默认文案
     * @param reqCode    请求码，用于识别返回结果
     * @param action     操作回调
     */
    public static void navToEdit(Activity context, String title, String defaultStr, int reqCode, EditInterface action) {
        Intent intent = new Intent(context, EditActivity.class);
        intent.putExtra("title", title);
        context.startActivityForResult(intent, reqCode);
        defaultString = defaultStr;
        editAction = action;
    }


    /**
     * 启动修改文本界面
     *
     * @param context    fragment context
     * @param title      界面标题
     * @param defaultStr 默认文案
     * @param reqCode    请求码，用于识别返回结果
     * @param action     操作回调
     * @param limit      输入长度限制
     */
    public static void navToEdit(Fragment context, String title, String defaultStr, int reqCode, EditInterface action, int limit) {
        Intent intent = new Intent(context.getActivity(), EditActivity.class);
        intent.putExtra("title", title);
        context.startActivityForResult(intent, reqCode);
        defaultString = defaultStr;
        editAction = action;
        lenLimit = limit;
    }


    /**
     * 启动修改文本界面
     *
     * @param context    activity context
     * @param title      界面标题
     * @param defaultStr 默认文案
     * @param reqCode    请求码，用于识别返回结果
     * @param action     操作回调
     * @param limit      输入长度限制
     */
    public static void navToEdit(Activity context, String title, String defaultStr, int reqCode, EditInterface action, int limit) {
        Intent intent = new Intent(context, EditActivity.class);
        intent.putExtra("title", title);
        context.startActivityForResult(intent, reqCode);
        defaultString = defaultStr;
        editAction = action;
        lenLimit = limit;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        centerTitleTV.setText(getIntent().getStringExtra("title"));
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow_white));
        titleRightOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.confirm_icon));

        input = (EditText) findViewById(R.id.editContent);
        if (defaultString != null) {
            input.setText(defaultString);
            input.setSelection(defaultString.length());
        }
        if (lenLimit != 0) {
            input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(lenLimit)});
        }

        titleLeftOpeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeActivity();
            }
        });
        titleRightOpeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editAction.onEdit(input.getText().toString(), EditActivity.this);
            }
        });

        MobclickAgent.openActivityDurationTrack(false);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_edit;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        defaultString = null;
        editAction = null;
        lenLimit = 0;
    }

    @Override
    public void onError(int i, String s) {
        Toast.makeText(this, getResources().getString(R.string.edit_error), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess() {
        Intent intent = new Intent();
        intent.putExtra(RETURN_EXTRA, input.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    public interface EditInterface {
        void onEdit(String text, TIMCallBack callBack);
    }
}
