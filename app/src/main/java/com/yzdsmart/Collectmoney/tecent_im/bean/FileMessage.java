package com.yzdsmart.Collectmoney.tecent_im.bean;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.tencent.TIMFileElem;
import com.tencent.TIMMessage;
import com.tencent.TIMValueCallBack;
import com.yzdsmart.Collectmoney.App;
import com.yzdsmart.Collectmoney.R;
import com.yzdsmart.Collectmoney.tecent_im.adapters.ChatAdapter;
import com.yzdsmart.Collectmoney.tecent_im.utils.FileUtil;

/**
 * 文件消息
 */
public class FileMessage extends Message {


    public FileMessage(TIMMessage message) {
        this.message = message;
    }

    public FileMessage(String filePath) {
        message = new TIMMessage();
        TIMFileElem elem = new TIMFileElem();
        elem.setPath(filePath);
        elem.setFileName(filePath.substring(filePath.lastIndexOf("/") + 1));
        message.addElement(elem);
    }

    @Override
    public void showMessage(ChatAdapter.ViewHolder viewHolder, Context context) {

    }

    /**
     * 获取消息摘要
     */
    @Override
    public String getSummary() {
        return App.getAppInstance().getString(R.string.summary_file);
    }

    /**
     * 保存消息或消息文件
     */
    @Override
    public void save() {
        if (message == null) return;
        final TIMFileElem e = (TIMFileElem) message.getElement(0);
        e.getFile(new TIMValueCallBack<byte[]>() {
            @Override
            public void onError(int i, String s) {
                Log.e(TAG, "getFile failed. code: " + i + " errmsg: " + s);
            }

            @Override
            public void onSuccess(byte[] bytes) {
                String[] str = e.getFileName().split("/");
                String filename = str[str.length - 1];
                if (FileUtil.createFile(bytes, filename, Environment.DIRECTORY_DOWNLOADS)) {
                    Toast.makeText(App.getAppInstance(), App.getAppInstance().getString(R.string.save_success), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(App.getAppInstance(), App.getAppInstance().getString(R.string.save_fail), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
