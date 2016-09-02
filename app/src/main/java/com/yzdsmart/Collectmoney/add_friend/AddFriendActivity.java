package com.yzdsmart.Collectmoney.add_friend;

import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.ui.divideritemdecoration.HorizontalDividerItemDecoration;
import com.tencent.TIMUserProfile;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.tecent_im.bean.FriendProfile;
import com.yzdsmart.Collectmoney.tecent_im.bean.ProfileSummary;
import com.yzdsmart.Collectmoney.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.Optional;

/**
 * Created by YZD on 2016/9/2.
 */
public class AddFriendActivity extends BaseActivity implements AddFriendContract.AddFriendView {
    @Nullable
    @BindViews({R.id.center_title, R.id.title_right_operation_layout})
    List<View> hideViews;
    @Nullable
    @BindView(R.id.title_left_operation)
    ImageView titleLeftOpeIV;
    @Nullable
    @BindView(R.id.left_title)
    TextView leftTitleTV;
    @Nullable
    @BindView(R.id.profile_list)
    RecyclerView profileListRV;
    @Nullable
    @BindView(R.id.search_filter)
    EditText searchFilterET;

    private List<ProfileSummary> profileSummaryList;

    private AddFriendAdapter addFriendAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private Paint dividerPaint;

    private AddFriendContract.AddFriendPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        profileSummaryList = new ArrayList<ProfileSummary>();

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow));
        leftTitleTV.setText(getResources().getString(R.string.add_new_friend));

        mLinearLayoutManager = new LinearLayoutManager(this);
        addFriendAdapter = new AddFriendAdapter(this);
        dividerPaint = new Paint();
        dividerPaint.setStrokeWidth(1);
        dividerPaint.setColor(getResources().getColor(R.color.light_grey));
        dividerPaint.setAntiAlias(true);
        dividerPaint.setPathEffect(new DashPathEffect(new float[]{25.0f, 25.0f}, 0));
        profileListRV.setHasFixedSize(true);
        profileListRV.setLayoutManager(mLinearLayoutManager);
        HorizontalDividerItemDecoration dividerItemDecoration = new HorizontalDividerItemDecoration.Builder(this).paint(dividerPaint).build();
        profileListRV.addItemDecoration(dividerItemDecoration);
        profileListRV.setAdapter(addFriendAdapter);

        new AddFriendPresenter(this, this);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_add_friend;
    }

    @Optional
    @OnClick({R.id.title_left_operation_layout})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                closeActivity();
                break;
        }
    }

    @Optional
    @OnEditorAction({R.id.search_filter})
    boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            String searchFilter = searchFilterET.getText().toString();
            profileSummaryList.clear();
            addFriendAdapter.clearList();
            if (SharedPreferencesUtils.getString(this, "im_account", "").equals("yzd" + searchFilter))
                return false;
            mPresenter.searchFriendById("yzd" + searchFilter);
            return true;
        }
        return false;
    }

    @Override
    public void setPresenter(AddFriendContract.AddFriendPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showUserInfo(List<TIMUserProfile> users) {
        if (null == users) {
            return;
        }
        for (TIMUserProfile item : users) {
            if (needAdd(item.getIdentifier()))
                profileSummaryList.add(new FriendProfile(item));
        }
        addFriendAdapter.appendList(profileSummaryList);
    }

    private boolean needAdd(String id) {
        for (ProfileSummary item : profileSummaryList) {
            if (item.getIdentify().equals(id)) return false;
        }
        return true;
    }
}
