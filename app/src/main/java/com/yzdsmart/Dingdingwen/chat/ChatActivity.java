package com.yzdsmart.Dingdingwen.chat;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tencent.TIMConversationType;
import com.tencent.TIMMessage;
import com.tencent.TIMMessageDraft;
import com.tencent.TIMMessageStatus;
import com.umeng.analytics.MobclickAgent;
import com.yzdsmart.Dingdingwen.BaseActivity;
import com.yzdsmart.Dingdingwen.Constants;
import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.chat.image_preview.ImagePreviewActivity;
import com.yzdsmart.Dingdingwen.money_friendship.group_list.profile.GroupProfileActivity;
import com.yzdsmart.Dingdingwen.tecent_im.adapters.ChatAdapter;
import com.yzdsmart.Dingdingwen.tecent_im.bean.CustomMessage;
import com.yzdsmart.Dingdingwen.tecent_im.bean.FileMessage;
import com.yzdsmart.Dingdingwen.tecent_im.bean.FriendProfile;
import com.yzdsmart.Dingdingwen.tecent_im.bean.FriendshipInfo;
import com.yzdsmart.Dingdingwen.tecent_im.bean.GroupInfo;
import com.yzdsmart.Dingdingwen.tecent_im.bean.ImageMessage;
import com.yzdsmart.Dingdingwen.tecent_im.bean.Message;
import com.yzdsmart.Dingdingwen.tecent_im.bean.MessageFactory;
import com.yzdsmart.Dingdingwen.tecent_im.bean.TextMessage;
import com.yzdsmart.Dingdingwen.tecent_im.bean.VideoMessage;
import com.yzdsmart.Dingdingwen.tecent_im.bean.VoiceMessage;
import com.yzdsmart.Dingdingwen.tecent_im.event.RefreshEvent;
import com.yzdsmart.Dingdingwen.tecent_im.utils.FileUtil;
import com.yzdsmart.Dingdingwen.tecent_im.utils.MediaUtil;
import com.yzdsmart.Dingdingwen.tecent_im.utils.RecorderUtil;
import com.yzdsmart.Dingdingwen.tecent_im.views.ChatInput;
import com.yzdsmart.Dingdingwen.tecent_im.views.VoiceSendingView;
import com.yzdsmart.Dingdingwen.utils.SharedPreferencesUtils;
import com.yzdsmart.Dingdingwen.views.photo_picker.picker.activity.BGAPhotoPickerPreviewActivity;
import com.yzdsmart.Dingdingwen.views.photo_picker.picker.util.BGAImageCaptureManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by YZD on 2016/8/28.
 */
public class ChatActivity extends BaseActivity implements ChatContract.ChatView {
    @Nullable
    @BindViews({R.id.left_title, R.id.title_logo})
    List<View> hideViews;
    @Nullable
    @BindView(R.id.center_title)
    TextView centerTitleTV;
    @Nullable
    @BindView(R.id.title_left_operation)
    ImageView titleLeftOpeIV;
    @Nullable
    @BindView(R.id.title_right_operation)
    ImageView titleRightOpeIV;
    @Nullable
    @BindView(R.id.title_right_operation_layout)
    FrameLayout titleRightOpeLayout;
    @Nullable
    @BindView(R.id.list)
    ListView listView;
    @Nullable
    @BindView(R.id.input_panel)
    ChatInput input;
    @Nullable
    @BindView(R.id.voice_sending)
    VoiceSendingView voiceSendingView;

    private static final String TAG = "ChatActivity";

    private ChatContract.ChatPresenter mPresenter;

    private List<Message> messageList;
    private ChatAdapter adapter;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int IMAGE_STORE = 200;
    private static final int FILE_CODE = 300;
    private static final int IMAGE_PREVIEW = 400;
    private Uri fileUri;
    private String identify;
    private TIMConversationType type;
    private String titleStr;
    private RecorderUtil recorder = new RecorderUtil();
    private Handler handler = new Handler();

    /**
     * 拍照的请求码
     */
    private static final int REQUEST_CODE_TAKE_PHOTO = 1;
    /**
     * 预览照片的请求码
     */
    private static final int REQUEST_CODE_PREVIEW = 2;
    private BGAImageCaptureManager mImageCaptureManager;
    private File takePhotoDir = new File(Environment.getExternalStorageDirectory(), "DingDingWen");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        messageList = new ArrayList<>();

        // 获取拍照图片保存目录
        if (takePhotoDir != null) {
            mImageCaptureManager = new BGAImageCaptureManager(this, takePhotoDir);
        }

        if (null != savedInstanceState) {
            identify = savedInstanceState.getString("identify");
            type = (TIMConversationType) savedInstanceState.getSerializable("type");
        } else {
            identify = getIntent().getStringExtra("identify");
            type = (TIMConversationType) getIntent().getSerializableExtra("type");
        }

        ButterKnife.apply(hideViews, BUTTERKNIFEGONE);
        titleLeftOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.left_arrow_white));

        input.setChatView(this);

        new ChatPresenter(this, this, identify, type);

        adapter = new ChatAdapter(this, R.layout.tecent_item_message, messageList);
        listView.setAdapter(adapter);
        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        input.setInputMode(ChatInput.InputMode.NONE);
                        break;
                }
                return false;
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            private int firstItem;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && firstItem == 0) {
                    //如果拉到顶端读取更多消息
                    mPresenter.getMessage(messageList.size() > 0 ? messageList.get(0).getMessage() : null);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                firstItem = firstVisibleItem;
            }
        });
        registerForContextMenu(listView);
        switch (type) {
            case C2C:
                titleRightOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.personal_icon));
                if (FriendshipInfo.getInstance().isFriend(identify)) {
//                    titleRightOpeLayout.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent = new Intent(ChatActivity.this, ProfileActivity.class);
//                            intent.putExtra("identify", identify);
//                            startActivity(intent);
//                        }
//                    });
                    FriendProfile profile = FriendshipInfo.getInstance().getProfile(identify);
                    centerTitleTV.setText(titleStr = profile == null ? identify : profile.getName());
                } else {
//                    titleRightOpeLayout.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent person = new Intent(ChatActivity.this, AddFriendActivity.class);
//                            person.putExtra("id", identify);
//                            person.putExtra("name", identify);
//                            startActivity(person);
//                        }
//                    });
                    centerTitleTV.setText(titleStr = identify);
                }
                titleRightOpeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toFriendDetail(identify);
                    }
                });
                break;
            case Group:
                titleRightOpeIV.setImageDrawable(getResources().getDrawable(R.mipmap.group_head_icon));
                titleRightOpeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ChatActivity.this, GroupProfileActivity.class);
                        intent.putExtra("identify", identify);
                        startActivity(intent);
                    }
                });
                centerTitleTV.setText(GroupInfo.getInstance().getGroupName(identify));
                break;
        }

        MobclickAgent.openActivityDurationTrack(false);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_chat;
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
        //退出聊天界面时输入框有内容，保存草稿
        if (input.getText().length() > 0) {
            TextMessage message = new TextMessage(input.getText());
            mPresenter.saveDraft(message.getMessage());
        } else {
            mPresenter.saveDraft(null);
        }
        RefreshEvent.getInstance().onRefresh();
        mPresenter.readMessages();
        MediaUtil.getInstance().stop();
    }

    @Override
    protected void onDestroy() {
        mPresenter.unRegisterObserver();
        handler.removeCallbacks(resetTitle);
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("identify", identify);
        outState.putSerializable("type", type);
        super.onSaveInstanceState(outState);
    }

    /**
     * 显示消息
     *
     * @param message
     */
    @Override
    public void showMessage(TIMMessage message) {
        if (message == null) {
            adapter.notifyDataSetChanged();
        } else {
            Message mMessage = MessageFactory.getMessage(message);
            if (mMessage != null) {
                if (mMessage instanceof CustomMessage) {
                    CustomMessage.Type messageType = ((CustomMessage) mMessage).getType();
                    switch (messageType) {
                        case TYPING:
                            centerTitleTV.setText(getString(R.string.chat_typing));
                            handler.removeCallbacks(resetTitle);
                            handler.postDelayed(resetTitle, 3000);
                            break;
                        default:
                            break;
                    }
                } else {
                    if (messageList.size() == 0) {
                        mMessage.setHasTime(null);
                    } else {
                        mMessage.setHasTime(messageList.get(messageList.size() - 1).getMessage());
                    }
                    messageList.add(mMessage);
                    adapter.notifyDataSetChanged();
                    listView.setSelection(adapter.getCount() - 1);
                }
            }
        }
    }

    /**
     * 显示消息
     *
     * @param messages
     */
    @Override
    public void showMessage(List<TIMMessage> messages) {
        int newMsgNum = 0;
        for (int i = 0; i < messages.size(); ++i) {
            Message mMessage = MessageFactory.getMessage(messages.get(i));
            if (mMessage == null || messages.get(i).status() == TIMMessageStatus.HasDeleted)
                continue;
            if (mMessage instanceof CustomMessage && (((CustomMessage) mMessage).getType() == CustomMessage.Type.TYPING ||
                    ((CustomMessage) mMessage).getType() == CustomMessage.Type.INVALID)) continue;
            ++newMsgNum;
            if (i != messages.size() - 1) {
                mMessage.setHasTime(messages.get(i + 1));
                messageList.add(0, mMessage);
            } else {
                mMessage.setHasTime(null);
                messageList.add(0, mMessage);
            }
        }
        adapter.notifyDataSetChanged();
        listView.setSelection(newMsgNum);
    }

    /**
     * 清除所有消息，等待刷新
     */
    @Override
    public void clearAllMessage() {
        messageList.clear();
    }

    /**
     * 发送消息成功
     *
     * @param message 返回的消息
     */
    @Override
    public void onSendMessageSuccess(TIMMessage message) {
        showMessage(message);
    }

    /**
     * 发送消息失败
     *
     * @param code 返回码
     * @param desc 返回描述
     */
    @Override
    public void onSendMessageFail(int code, String desc, TIMMessage message) {
        long id = message.getMsgUniqueId();
        for (Message msg : messageList) {
            if (msg.getMessage().getMsgUniqueId() == id) {
                switch (code) {
                    case 80001:
                        //发送内容包含敏感词
                        msg.setDesc(getString(R.string.chat_content_bad));
                        adapter.notifyDataSetChanged();
                        break;
                }
            }
        }
    }

    /**
     * 发送图片消息
     */
    @Override
    public void sendImage() {
        Intent intent_album = new Intent("android.intent.action.GET_CONTENT");
        intent_album.setType("image/*");
        startActivityForResult(intent_album, IMAGE_STORE);
    }

    /**
     * 发送照片消息
     */
    @Override
    public void sendPhoto() {
//        Intent intent_photo = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (intent_photo.resolveActivity(getPackageManager()) != null) {
//            File tempFile = FileUtil.getTempFile(FileUtil.FileType.IMG);
//            if (tempFile != null) {
//                fileUri = Uri.fromFile(tempFile);
//            }
//            intent_photo.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
//            startActivityForResult(intent_photo, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
//        }

        try {
            startActivityForResult(mImageCaptureManager.getTakePictureIntent(), REQUEST_CODE_TAKE_PHOTO);
        } catch (Exception e) {

        }
    }

    /**
     * 发送文本消息
     */
    @Override
    public void sendText() {
        Message message = new TextMessage(input.getText());
        mPresenter.sendMessage(message.getMessage());
        input.setText("");
    }

    /**
     * 发送文件
     */
    @Override
    public void sendFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, FILE_CODE);
    }

    /**
     * 开始发送语音消息
     */
    @Override
    public void startSendVoice() {
        voiceSendingView.setVisibility(View.VISIBLE);
        voiceSendingView.showRecording();
        recorder.startRecording();
    }

    /**
     * 结束发送语音消息
     */
    @Override
    public void endSendVoice() {
        voiceSendingView.release();
        voiceSendingView.setVisibility(View.GONE);
        recorder.stopRecording();
        if (recorder.getTimeInterval() < 1) {
            showSnackbar(getResources().getString(R.string.chat_audio_too_short));
        } else {
            Message message = new VoiceMessage(recorder.getTimeInterval(), recorder.getFilePath());
            mPresenter.sendMessage(message.getMessage());
        }
    }

    /**
     * 发送小视频消息
     *
     * @param fileName 文件名
     */
    @Override
    public void sendVideo(String fileName) {
        Message message = new VideoMessage(fileName);
        mPresenter.sendMessage(message.getMessage());
    }

    /**
     * 正在发送
     */
    @Override
    public void sending() {
        if (type == TIMConversationType.C2C) {
            Message message = new CustomMessage(CustomMessage.Type.TYPING);
            mPresenter.sendOnlineMessage(message.getMessage());
        }
    }

    /**
     * 显示草稿
     */
    @Override
    public void showDraft(TIMMessageDraft draft) {
        input.getText().append(TextMessage.getString(draft.getElems(), this));
    }

    @Override
    public void setPresenter(ChatContract.ChatPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        Message message = messageList.get(info.position);
        menu.add(0, 1, Menu.NONE, getString(R.string.chat_del));
        if (message.isSendFail()) {
            menu.add(0, 2, Menu.NONE, getString(R.string.chat_resend));
        }
        if (message instanceof ImageMessage || message instanceof FileMessage) {
            menu.add(0, 3, Menu.NONE, getString(R.string.chat_save));
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Message message = messageList.get(info.position);
        switch (item.getItemId()) {
            case 1:
                message.remove();
                messageList.remove(info.position);
                adapter.notifyDataSetChanged();
                break;
            case 2:
                messageList.remove(message);
                mPresenter.sendMessage(message.getMessage());
                break;
            case 3:
                message.save();
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK && fileUri != null) {
                showImagePreview(fileUri.getPath());
            }
        } else if (requestCode == IMAGE_STORE) {
            if (resultCode == RESULT_OK && data != null) {
//                showImagePreview(FileUtil.getFilePath(this, data.getData()));
                ArrayList<String> photos = new ArrayList<>();
                photos.add(FileUtil.getFilePath(this, data.getData()));
                startActivityForResult(BGAPhotoPickerPreviewActivity.newIntent(this, 1, photos, photos, 0, true), REQUEST_CODE_PREVIEW);
            }
        } else if (requestCode == FILE_CODE) {
            if (resultCode == RESULT_OK) {
                sendFile(FileUtil.getFilePath(this, data.getData()));
            }
        } else if (requestCode == IMAGE_PREVIEW) {
            if (resultCode == RESULT_OK) {
                boolean isOri = data.getBooleanExtra("isOri", false);
                String path = data.getStringExtra("path");
                File file = new File(path);
                if (file.exists() && file.length() > 0) {
                    if (file.length() > 1024 * 1024 * 10) {
                        showSnackbar(getString(R.string.chat_file_too_large));
                    } else {
                        Message message = new ImageMessage(path, isOri);
                        mPresenter.sendMessage(message.getMessage());
                    }
                } else {
                    showSnackbar(getString(R.string.chat_file_not_exist));
                }
            }
        } else if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_TAKE_PHOTO) {
            ArrayList<String> photos = new ArrayList<>();
            photos.add(mImageCaptureManager.getCurrentPhotoPath());
            startActivityForResult(BGAPhotoPickerPreviewActivity.newIntent(this, 1, photos, photos, 0, true), REQUEST_CODE_PREVIEW);
        } else if (resultCode == RESULT_CANCELED && requestCode == REQUEST_CODE_TAKE_PHOTO) {
            // 从拍照预览界面返回，删除之前拍的照片
            mImageCaptureManager.deletePhotoFile();
        } else if (resultCode == RESULT_CANCELED && requestCode == REQUEST_CODE_PREVIEW) {
            if (BGAPhotoPickerPreviewActivity.getIsFromTakePhoto(data)) {
                // 从拍照预览界面返回，删除之前拍的照片
                mImageCaptureManager.deletePhotoFile();
            }
        } else if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_PREVIEW) {
            if (BGAPhotoPickerPreviewActivity.getSelectedImages(data).size() <= 0) return;
            String path = BGAPhotoPickerPreviewActivity.getSelectedImages(data).get(0);
            File file = new File(path);
            if (file.exists()) {
                if (file.length() > 1024 * 1024 * 10) {
                    showSnackbar(getString(R.string.chat_file_too_large));
                } else {
                    Message message = new ImageMessage(path, false);
                    mPresenter.sendMessage(message.getMessage());
                }
            } else {
                showSnackbar(getString(R.string.chat_file_not_exist));
            }
        }
    }

    private void showImagePreview(String path) {
        if (path == null) return;
        Intent intent = new Intent(this, ImagePreviewActivity.class);
        intent.putExtra("path", path);
        startActivityForResult(intent, IMAGE_PREVIEW);
    }

    private void sendFile(String path) {
        if (path == null) return;
        File file = new File(path);
        if (file.exists()) {
            if (file.length() > 1024 * 1024 * 10) {
                showSnackbar(getString(R.string.chat_file_too_large));
            } else {
                Message message = new FileMessage(path);
                mPresenter.sendMessage(message.getMessage());
            }
        } else {
            showSnackbar(getString(R.string.chat_file_not_exist));
        }

    }

    /**
     * 跳转到好友详情页
     *
     * @param identify
     */
    public void toFriendDetail(String identify) {
        mPresenter.getCustCode(identify, "000000", Constants.GET_CUST_CODE_ACTION_CODE, SharedPreferencesUtils.getString(this, "ddw_token_type", "") + " " + SharedPreferencesUtils.getString(this, "ddw_access_token", ""));
    }

    /**
     * 将标题设置为对象名称
     */
    private Runnable resetTitle = new Runnable() {
        @Override
        public void run() {
            centerTitleTV.setText(titleStr);
        }
    };
}
