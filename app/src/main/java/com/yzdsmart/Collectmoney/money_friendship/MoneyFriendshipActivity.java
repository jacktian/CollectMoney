package com.yzdsmart.Collectmoney.money_friendship;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.bean.Friendship;
import com.yzdsmart.Collectmoney.friend_future.FriendFutureActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by YZD on 2016/8/18.
 */
public class MoneyFriendshipActivity extends BaseActivity {
    @Nullable
    @BindViews({R.id.left_title, R.id.center_title})
    List<View> hideViews;
    @Nullable
    @BindView(R.id.title_left_operation)
    ImageView titleLeftOpeIV;
    @Nullable
    @BindView(R.id.title_right_operation)
    ImageView titleRightOpeIV;
    @Nullable
    @BindView(R.id.friend_profile_list)
    UltimateRecyclerView friendListRV;

    private LinearLayoutManager mLinearLayoutManager;
    private List<Friendship> friendshipList;
    private FriendshipAdapter friendshipAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        friendshipList=new ArrayList<Friendship>();

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow));
        titleRightOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.user_add_icon));

        mLinearLayoutManager = new LinearLayoutManager(this);
        friendshipAdapter=new FriendshipAdapter(this);
        friendListRV.setHasFixedSize(true);
        friendListRV.setLayoutManager(mLinearLayoutManager);
        friendListRV.setAdapter(friendshipAdapter);
        List<Friendship> list=new ArrayList<Friendship>();
        list.add(new Friendship("file:///android_asset/album_pic.png","韩梅梅",2,4));
        list.add(new Friendship("file:///android_asset/album_pic.png","沙漏",3,1));
        list.add(new Friendship("file:///android_asset/album_pic.png","轻语",4,3));
        list.add(new Friendship("file:///android_asset/album_pic.png","漂流瓶",5,5));
        list.add(new Friendship("file:///android_asset/album_pic.png","李明",6,2));
        list.add(new Friendship("file:///android_asset/album_pic.png","汤姆",7,3));
        friendshipList.addAll(list);
        friendshipAdapter.appendList(friendshipList);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_money_friendship;
    }

    @Optional
    @OnClick({R.id.title_left_operation_layout, R.id.title_right_operation_layout})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_left_operation_layout:
                closeActivity();
                break;
            case R.id.title_right_operation_layout:
                openActivity(FriendFutureActivity.class);
                break;
        }
    }

}
