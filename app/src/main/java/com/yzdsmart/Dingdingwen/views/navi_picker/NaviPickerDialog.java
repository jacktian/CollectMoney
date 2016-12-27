package com.yzdsmart.Dingdingwen.views.navi_picker;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yzdsmart.Dingdingwen.R;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Tom.Cai
 * @Function: 自定义对话框
 * @Date: 2013-10-28
 * @Time: 下午12:37:43
 */
public class NaviPickerDialog extends Dialog {
    private Context context;

    private TextView negativeButton, positiveButton;
    private TextView title;
    private LinearLayout lay_apps;

    private List<AppInfo> apps;
    private String msg = "选择您需要打开的应用";
    private String msg_default = "您的手机中暂没有安装支持导航工具，我们将打开浏览器进行web导航，我们任建议您下载百度或高德地图进行导航";
    private String positiveStr = "继续导航";
    private String negativeStr = "取消";

    private View.OnClickListener mapItemClickListener;
    private View.OnClickListener defaultClickListener;

    /**
     * @param context
     * @param mapItemClickListener App图标点击监听，启动app进行导航
     * @param defaultClickListener 启动web进行导航
     */
    public NaviPickerDialog(Context context, View.OnClickListener mapItemClickListener, View.OnClickListener defaultClickListener) {
        super(context, R.style.navi_picker_dialog);
        this.context = context;
        this.mapItemClickListener = mapItemClickListener;
        this.defaultClickListener = defaultClickListener;
        initApps();
        setMsgDialog();
    }

    private void initApps() {
        apps = APPUtil.getMapApps(context);
        //只显示前5个应用
        if (apps != null && apps.size() > 5) {
            apps = apps.subList(0, 5);
        }
    }

    private void setMsgDialog() {
        View mView;
        if (apps != null && apps.size() != 0) {
            mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_navi_picker, null);

            title = (TextView) mView.findViewById(R.id.title);
            negativeButton = (TextView) mView.findViewById(R.id.negativeButton);
            if (title != null) title.setText(msg);
            if (negativeButton != null) negativeButton.setText(negativeStr);
            if (negativeButton != null) negativeButton.setOnClickListener(deflistener);

            LinkedList<TextView> views = new LinkedList<TextView>();
            lay_apps = (LinearLayout) mView.findViewById(R.id.lay_apps);
            lay_apps.setOrientation(LinearLayout.VERTICAL);
            for (int i = 0; i < apps.size(); i++) {
                AppInfo app = apps.get(i);
                //定义左右边距15
                LinearLayout.LayoutParams para = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                para.setMargins(15, 0, 15, 0); // left,top,right, bottom
                para.gravity = Gravity.CENTER;

                TextView textView = new TextView(context);
                textView.setCompoundDrawablesWithIntrinsicBounds(null, app.getAppIcon(), null, null);    //设置图标
                textView.setText(app.getAppName());                                //设置文字
                textView.setTextAppearance(context, R.style.navi_picker_small_dark);    //设置风格
                textView.setLayoutParams(para);                                    //设置边距
                textView.setGravity(Gravity.CENTER_HORIZONTAL);                    //设置图标文字水平居中
                textView.setSingleLine(true);                                    //设置单行显示
                textView.setEllipsize(TruncateAt.END);                            //设置超出长度显示省略…
                textView.setMaxEms(6);                                            //设置最大长度
                textView.setTag(app.getPackageName());                            //设置包名为tag
                textView.setOnClickListener(mapItemClickListener);                        //设置监听

                views.add(textView);

                if (views.size() == 3 || i == apps.size() - 1) {
                    LinearLayout.LayoutParams para_lay = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                    para_lay.setMargins(0, 30, 0, 0); // left,top,right, bottom
                    para_lay.gravity = Gravity.CENTER;
                    LinearLayout row = new LinearLayout(context);
                    row.setOrientation(LinearLayout.HORIZONTAL);
                    row.setGravity(Gravity.CENTER_HORIZONTAL);
                    row.setLayoutParams(para_lay);
                    for (TextView view : views) {
                        row.addView(view);
                    }
                    lay_apps.addView(row);
                    views.clear();
                }
            }
        } else {
            mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_navi_picker_default, null);
            title = (TextView) mView.findViewById(R.id.title);
            positiveButton = (TextView) mView.findViewById(R.id.positiveButton);
            negativeButton = (TextView) mView.findViewById(R.id.negativeButton);
            if (title != null) title.setText(msg_default);
            if (positiveButton != null) positiveButton.setText(positiveStr);
            if (negativeButton != null) negativeButton.setText(negativeStr);
            if (positiveButton != null) positiveButton.setOnClickListener(defaultClickListener);
            if (negativeButton != null) negativeButton.setOnClickListener(deflistener);
        }
        super.setContentView(mView);
    }

    @Override
    public void show() {
        super.show();
        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        /////////获取屏幕宽度
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        /////////设置高宽
        lp.width = (int) (screenWidth * 0.85); // 宽度
        dialogWindow.setAttributes(lp);
    }

    /**
     * 默认的监听器
     */
    private View.OnClickListener deflistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            NaviPickerDialog.this.dismiss();
        }
    };
}
