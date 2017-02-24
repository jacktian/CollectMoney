package com.yzdsmart.Dingdingwen.http.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YZD on 2017/2/24.
 */

public class GameTaskRequestResponse {

    /**
     * ActionStatus : OK
     * ErrorCode : 0
     * ErrorInfo :
     * Data : {"GameName":"新项目","TaskingCode":"","TaskingName":"","TaskingBeginDateTime":"","GameSumTime":"2小时51分钟55秒","TaskLists":[{"TaskCode":"RenWu3","TaskName":"第三个任务","BeginDateTime":"2017-02-23 10:40:04","GameTime":"2小时51分钟55秒","GameStatus":"完成"}]}
     */

    private String ActionStatus;
    private int ErrorCode;
    private String ErrorInfo;
    private DataBean Data;

    public String getActionStatus() {
        return ActionStatus;
    }

    public void setActionStatus(String ActionStatus) {
        this.ActionStatus = ActionStatus;
    }

    public int getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(int ErrorCode) {
        this.ErrorCode = ErrorCode;
    }

    public String getErrorInfo() {
        return ErrorInfo;
    }

    public void setErrorInfo(String ErrorInfo) {
        this.ErrorInfo = ErrorInfo;
    }

    public DataBean getData() {
        return Data;
    }

    public void setData(DataBean Data) {
        this.Data = Data;
    }

    public static class DataBean implements Parcelable {
        /**
         * GameName : 新项目
         * TaskingCode :
         * TaskingName :
         * TaskingBeginDateTime :
         * GameSumTime : 2小时51分钟55秒
         * TaskLists : [{"TaskCode":"RenWu3","TaskName":"第三个任务","BeginDateTime":"2017-02-23 10:40:04","GameTime":"2小时51分钟55秒","GameStatus":"完成"}]
         */

        private String GameName;
        private String TaskingCode;
        private String TaskingName;
        private String TaskingBeginDateTime;
        private String GameSumTime;
        private List<TaskListsBean> TaskLists;

        public String getGameName() {
            return GameName;
        }

        public void setGameName(String GameName) {
            this.GameName = GameName;
        }

        public String getTaskingCode() {
            return TaskingCode;
        }

        public void setTaskingCode(String TaskingCode) {
            this.TaskingCode = TaskingCode;
        }

        public String getTaskingName() {
            return TaskingName;
        }

        public void setTaskingName(String TaskingName) {
            this.TaskingName = TaskingName;
        }

        public String getTaskingBeginDateTime() {
            return TaskingBeginDateTime;
        }

        public void setTaskingBeginDateTime(String TaskingBeginDateTime) {
            this.TaskingBeginDateTime = TaskingBeginDateTime;
        }

        public String getGameSumTime() {
            return GameSumTime;
        }

        public void setGameSumTime(String GameSumTime) {
            this.GameSumTime = GameSumTime;
        }

        public List<TaskListsBean> getTaskLists() {
            return TaskLists;
        }

        public void setTaskLists(List<TaskListsBean> TaskLists) {
            this.TaskLists = TaskLists;
        }

        public static class TaskListsBean {
            /**
             * TaskCode : RenWu3
             * TaskName : 第三个任务
             * BeginDateTime : 2017-02-23 10:40:04
             * GameTime : 2小时51分钟55秒
             * GameStatus : 完成
             */

            private String TaskCode;
            private String TaskName;
            private String BeginDateTime;
            private String GameTime;
            private String GameStatus;

            public String getTaskCode() {
                return TaskCode;
            }

            public void setTaskCode(String TaskCode) {
                this.TaskCode = TaskCode;
            }

            public String getTaskName() {
                return TaskName;
            }

            public void setTaskName(String TaskName) {
                this.TaskName = TaskName;
            }

            public String getBeginDateTime() {
                return BeginDateTime;
            }

            public void setBeginDateTime(String BeginDateTime) {
                this.BeginDateTime = BeginDateTime;
            }

            public String getGameTime() {
                return GameTime;
            }

            public void setGameTime(String GameTime) {
                this.GameTime = GameTime;
            }

            public String getGameStatus() {
                return GameStatus;
            }

            public void setGameStatus(String GameStatus) {
                this.GameStatus = GameStatus;
            }
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.GameName);
            dest.writeString(this.TaskingCode);
            dest.writeString(this.TaskingName);
            dest.writeString(this.TaskingBeginDateTime);
            dest.writeString(this.GameSumTime);
            dest.writeList(this.TaskLists);
        }

        public DataBean() {
        }

        protected DataBean(Parcel in) {
            this.GameName = in.readString();
            this.TaskingCode = in.readString();
            this.TaskingName = in.readString();
            this.TaskingBeginDateTime = in.readString();
            this.GameSumTime = in.readString();
            this.TaskLists = new ArrayList<TaskListsBean>();
            in.readList(this.TaskLists, TaskListsBean.class.getClassLoader());
        }

        public static final Parcelable.Creator<DataBean> CREATOR = new Parcelable.Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel source) {
                return new DataBean(source);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };
    }
}
