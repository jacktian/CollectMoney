package com.yzdsmart.Collectmoney.friend_future;

import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.ui.divideritemdecoration.HorizontalDividerItemDecoration;
import com.yzdsmart.Collectmoney.BaseActivity;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.bean.FriendFuture;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by YZD on 2016/8/20.
 */
public class FriendFutureActivity extends BaseActivity {
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
    @BindView(R.id.friend_future_list)
    RecyclerView friendFutureRV;

    private LinearLayoutManager mLinearLayoutManager;
    private List<FriendFuture> friendFutureList;
    private FriendFutureAdapter friendFutureAdapter;
    private Paint dividerPaint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        friendFutureList = new ArrayList<FriendFuture>();

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow));
        leftTitleTV.setText(getResources().getString(R.string.future_search_new_friend));

        mLinearLayoutManager = new LinearLayoutManager(this);
        dividerPaint = new Paint();
        dividerPaint.setStrokeWidth(1);
        dividerPaint.setColor(getResources().getColor(R.color.light_grey));
        dividerPaint.setAntiAlias(true);
        dividerPaint.setPathEffect(new DashPathEffect(new float[]{25.0f, 25.0f}, 0));
        HorizontalDividerItemDecoration dividerItemDecoration = new HorizontalDividerItemDecoration.Builder(this).paint(dividerPaint).build();

        friendFutureAdapter = new FriendFutureAdapter(this);
        friendFutureRV.setHasFixedSize(true);
        friendFutureRV.setLayoutManager(mLinearLayoutManager);
        friendFutureRV.addItemDecoration(dividerItemDecoration);
        friendFutureRV.setAdapter(friendFutureAdapter);

        List<FriendFuture> list = new ArrayList<FriendFuture>();
        list.add(new FriendFuture("file:///android_asset/album_pic.png", "艾伦", 1));
        list.add(new FriendFuture("file:///android_asset/album_pic.png", "嗣位", 0));
        list.add(new FriendFuture("file:///android_asset/album_pic.png", "木樨", 0));
        list.add(new FriendFuture("file:///android_asset/album_pic.png", "提姆", 1));
        list.add(new FriendFuture("file:///android_asset/album_pic.png", "韩梅梅", 0));

        friendFutureList.addAll(list);
        friendFutureAdapter.appenList(friendFutureList);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_friend_future;
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
}
