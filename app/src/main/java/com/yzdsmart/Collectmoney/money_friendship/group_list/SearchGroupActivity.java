package com.yzdsmart.Collectmoney.money_friendship.group_list;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.tencent.TIMGroupDetailInfo;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.tecent_im.adapters.ProfileSummaryAdapter;
import com.yzdsmart.Collectmoney.tecent_im.bean.GroupProfile;
import com.yzdsmart.Collectmoney.tecent_im.bean.ProfileSummary;

import java.util.ArrayList;
import java.util.List;

public class SearchGroupActivity extends Activity implements View.OnKeyListener, SearchGroupContract.SearchGroupView {

    private List<ProfileSummary> list = new ArrayList<>();
    private ProfileSummaryAdapter adapter;
    private EditText searchInput;
    private ListView listView;

    private SearchGroupContract.SearchGroupPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_group);
        searchInput = (EditText) findViewById(R.id.inputSearch);
        listView = (ListView) findViewById(R.id.list);
        adapter = new ProfileSummaryAdapter(this, R.layout.tecent_item_profile_summary, list);
        listView.setAdapter(adapter);

        new SearchGroupPresenter(this, this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                list.get(position).onClick(SearchGroupActivity.this);
            }
        });
        TextView tvCancel = (TextView) findViewById(R.id.cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        searchInput.setOnKeyListener(this);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() != KeyEvent.ACTION_UP) {   // 忽略其它事件
            return false;
        }

        switch (keyCode) {
            case KeyEvent.KEYCODE_ENTER:
                list.clear();
                adapter.notifyDataSetChanged();
                String key = searchInput.getText().toString();
                if (key.equals("")) return true;
                mPresenter.searchGroupByName(key);
                return true;
            default:
                return false;
        }
    }

    /**
     * 显示群资料
     *
     * @param groupInfos 群资料信息列表
     */
    @Override
    public void showGroupInfo(List<TIMGroupDetailInfo> groupInfos) {
        list.clear();
        for (TIMGroupDetailInfo item : groupInfos) {
            list.add(new GroupProfile(item));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setPresenter(SearchGroupContract.SearchGroupPresenter presenter) {
        mPresenter = presenter;
    }
}
