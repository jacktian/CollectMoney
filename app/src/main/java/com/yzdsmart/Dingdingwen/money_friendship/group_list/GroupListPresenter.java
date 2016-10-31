package com.yzdsmart.Dingdingwen.money_friendship.group_list;

import android.content.Context;

import com.tencent.TIMGroupCacheInfo;
import com.yzdsmart.Collectmoney.tecent_im.event.GroupEvent;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by YZD on 2016/8/30.
 */
public class GroupListPresenter implements GroupListContract.GroupListPresenter, Observer {
    private Context context;
    private GroupListContract.GroupListView mView;
    private GroupListModel mModel;

    public GroupListPresenter(Context context, GroupListContract.GroupListView mView) {
        this.context = context;
        this.mView = mView;
        mModel = new GroupListModel();
        mView.setPresenter(this);

        GroupEvent.getInstance().addObserver(this);
    }

    /**
     * This method is called if the specified {@code Observable} object's
     * {@code notifyObservers} method is called (because the {@code Observable}
     * object has been updated.
     *
     * @param observable the {@link Observable} object.
     * @param data       the data passed to {@link Observable#notifyObservers(Object)}.
     */
    @Override
    public void update(Observable observable, Object data) {
        if (observable instanceof GroupEvent) {
            if (data instanceof GroupEvent.NotifyCmd) {
                GroupEvent.NotifyCmd cmd = (GroupEvent.NotifyCmd) data;
                switch (cmd.type) {
                    case DEL:
                        mView.delGroup((String) cmd.data);
                        break;
                    case ADD:
                        mView.addGroup((TIMGroupCacheInfo) cmd.data);
                        break;
                    case UPDATE:
                        mView.updateGroup((TIMGroupCacheInfo) cmd.data);
                        break;
                }
            }
        }
    }

    @Override
    public void unRegisterObserver() {
        GroupEvent.getInstance().deleteObserver(this);
    }
}
