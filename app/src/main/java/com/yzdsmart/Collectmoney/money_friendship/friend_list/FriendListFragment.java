package com.yzdsmart.Collectmoney.money_friendship.friend_list;

import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.EditText;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.marshalchen.ultimaterecyclerview.ui.divideritemdecoration.HorizontalDividerItemDecoration;
import com.yzdsmart.Collectmoney.BaseFragment;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.bean.Friendship;
import com.yzdsmart.Collectmoney.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by jacks on 2016/8/29.
 */
public class FriendListFragment extends BaseFragment implements FriendListContract.FriendListView {
    @Nullable
    @BindView(R.id.search_filter)
    EditText searchFilterET;
    @Nullable
    @BindView(R.id.friend_profile_list)
    UltimateRecyclerView friendProfileListRV;

    private Long timeStampNow = 0l;//上次拉取的时间戳，默认:0
    private Integer startIndex = 0;//下页拉取的起始位置
    private Integer currentStandardSequence = 0;//上次拉取标配关系链的Sequence, 默认:0
    private static final Integer PAGE_SIZE = 10;//每页获取的数量

    private FriendListContract.FriendListPresenter mPresenter;

    private LinearLayoutManager mLinearLayoutManager;
    private List<Friendship> friendshipList;
    private FriendListAdapter friendListAdapter;
    private Paint dividerPaint;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        friendshipList = new ArrayList<Friendship>();

        new FriendListPresenter(getActivity(), this);

        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        dividerPaint = new Paint();
        dividerPaint.setStrokeWidth(1);
        dividerPaint.setColor(getResources().getColor(R.color.light_grey));
        dividerPaint.setAntiAlias(true);
        dividerPaint.setPathEffect(new DashPathEffect(new float[]{25.0f, 25.0f}, 0));

        friendListAdapter = new FriendListAdapter(getActivity());
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_friend_list;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        HorizontalDividerItemDecoration dividerItemDecoration = new HorizontalDividerItemDecoration.Builder(getActivity()).paint(dividerPaint).build();
        friendProfileListRV.setHasFixedSize(true);
        friendProfileListRV.setLayoutManager(mLinearLayoutManager);
        friendProfileListRV.addItemDecoration(dividerItemDecoration);
        friendProfileListRV.setAdapter(friendListAdapter);
        //给每一个item设置前置的栏目条，
        StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(friendListAdapter);
        friendProfileListRV.addItemDecoration(headersDecor);

        List<Friendship> list = new ArrayList<Friendship>();
        list.add(new Friendship("", "", "", "", "艾伦", "file:///android_asset/album_pic.png", null, null, 1, 2));
        list.add(new Friendship("", "", "", "", "约翰", "file:///android_asset/album_pic.png", null, null, 2, 2));
        list.add(new Friendship("", "", "", "", "比尔", "file:///android_asset/album_pic.png", null, null, 5, 1));
        list.add(new Friendship("", "", "", "", "嗣位", "file:///android_asset/album_pic.png", null, null, 5, 1));
        list.add(new Friendship("", "", "", "", "布朗", "file:///android_asset/album_pic.png", null, null, 3, 5));
        list.add(new Friendship("", "", "", "", "韩梅梅", "file:///android_asset/album_pic.png", null, null, 2, 4));
        list.add(new Friendship("", "", "", "", "汉斯", "file:///android_asset/album_pic.png", null, null, 3, 2));
        list.add(new Friendship("", "", "", "", "沙漏", "file:///android_asset/album_pic.png", null, null, 3, 1));
        list.add(new Friendship("", "", "", "", "轻语", "file:///android_asset/album_pic.png", null, null, 4, 3));
        list.add(new Friendship("", "", "", "", "漂流瓶", "file:///android_asset/album_pic.png", null, null, 5, 5));
        list.add(new Friendship("", "", "", "", "李明", "file:///android_asset/album_pic.png", null, null, 6, 2));
        list.add(new Friendship("", "", "", "", "汤姆", "file:///android_asset/album_pic.png", null, null, 7, 3));
        list.add(new Friendship("", "", "", "", "木樨", "file:///android_asset/album_pic.png", null, null, 5, 5));
        list.add(new Friendship("", "", "", "", "杰克", "file:///android_asset/album_pic.png", null, null, 1, 2));
        list.add(new Friendship("", "", "", "", "提姆", "file:///android_asset/album_pic.png", null, null, 4, 1));
        friendshipList.addAll(list);
        friendListAdapter.appendList(friendshipList);

        mPresenter.getFriendsList("000000", SharedPreferencesUtils.getString(getActivity(), "cust_code", ""), timeStampNow, startIndex, currentStandardSequence, PAGE_SIZE);
    }

    @Override
    public void onStop() {
        super.onStop();
        mPresenter.unRegisterSubscribe();
    }

    @Override
    public void onGetFriendsList(List<Friendship> friends) {
        friendListAdapter.appendList(friends);
    }

    @Override
    public void setPresenter(FriendListContract.FriendListPresenter presenter) {
        mPresenter = presenter;
    }
}
