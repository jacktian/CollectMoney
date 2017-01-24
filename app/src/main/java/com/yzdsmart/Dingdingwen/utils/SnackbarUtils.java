package com.yzdsmart.Dingdingwen.utils;

import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * Created by YZD on 2017/1/24.
 */

public class SnackbarUtils {
    private static WeakReference<Snackbar> snackbarWeakReference;

    /**
     * 显示短时snackbar
     *
     * @param parent        父视图(CoordinatorLayout或者DecorView)
     * @param text          文本
     * @param textColor     文本颜色
     * @param bgColor       背景色
     * @param textAlignment 事件文本显示位置 0 居左 1 居中 2 居右
     */
    public static void showShortSnackbar(View parent, CharSequence text, @ColorInt int textColor, @ColorInt int bgColor, int textAlignment) {
        showSnackbar(parent, text, Snackbar.LENGTH_SHORT, textColor, bgColor, null, -1, textAlignment, null);
    }

    /**
     * 显示短时snackbar
     *
     * @param parent          父视图(CoordinatorLayout或者DecorView)
     * @param text            文本
     * @param textColor       文本颜色
     * @param bgColor         背景色
     * @param actionText      事件文本
     * @param actionTextColor 事件文本颜色
     * @param textAlignment   事件文本显示位置 0 居左 1 居中 2 居右
     * @param listener        监听器
     */
    public static void showShortSnackbar(View parent, CharSequence text, @ColorInt int textColor, @ColorInt int bgColor,
                                         CharSequence actionText, int actionTextColor, int textAlignment, View.OnClickListener listener) {
        showSnackbar(parent, text, Snackbar.LENGTH_SHORT, textColor, bgColor,
                actionText, actionTextColor, textAlignment, listener);
    }

    /**
     * 显示长时snackbar
     *
     * @param parent        视图(CoordinatorLayout或者DecorView)
     * @param text          文本
     * @param textColor     文本颜色
     * @param bgColor       背景色
     * @param textAlignment 事件文本显示位置 0 居左 1 居中 2 居右
     */
    public static void showLongSnackbar(View parent, CharSequence text, @ColorInt int textColor, @ColorInt int bgColor, int textAlignment) {
        showSnackbar(parent, text, Snackbar.LENGTH_LONG, textColor, bgColor, null, -1, textAlignment, null);
    }

    /**
     * 显示长时snackbar
     *
     * @param parent          视图(CoordinatorLayout或者DecorView)
     * @param text            文本
     * @param textColor       文本颜色
     * @param bgColor         背景色
     * @param actionText      事件文本
     * @param actionTextColor 事件文本颜色
     * @param textAlignment   事件文本显示位置 0 居左 1 居中 2 居右
     * @param listener        监听器
     */
    public static void showLongSnackbar(View parent, CharSequence text, @ColorInt int textColor, @ColorInt int bgColor,
                                        CharSequence actionText, int actionTextColor, int textAlignment, View.OnClickListener listener) {
        showSnackbar(parent, text, Snackbar.LENGTH_LONG, textColor, bgColor,
                actionText, actionTextColor, textAlignment, listener);
    }


    /**
     * 显示自定义时长snackbar
     *
     * @param parent        父视图(CoordinatorLayout或者DecorView)
     * @param text          文本
     * @param textColor     文本颜色
     * @param bgColor       背景色
     * @param textAlignment 事件文本显示位置 0 居左 1 居中 2 居右
     */
    public static void showIndefiniteSnackbar(View parent, CharSequence text, @ColorInt int textColor, @ColorInt int bgColor, int textAlignment) {
        showSnackbar(parent, text, Snackbar.LENGTH_INDEFINITE, textColor, bgColor, null, -1, textAlignment, null);
    }

    /**
     * 显示自定义时长snackbar
     *
     * @param parent          父视图(CoordinatorLayout或者DecorView)
     * @param text            文本
     * @param textColor       文本颜色
     * @param bgColor         背景色
     * @param actionText      事件文本
     * @param actionTextColor 事件文本颜色
     * @param textAlignment   事件文本显示位置 0 居左 1 居中 2 居右
     * @param listener        监听器
     */
    public static void showIndefiniteSnackbar(View parent, CharSequence text, @ColorInt int textColor, @ColorInt int bgColor,
                                              CharSequence actionText, int actionTextColor, int textAlignment, View.OnClickListener listener) {
        showSnackbar(parent, text, Snackbar.LENGTH_INDEFINITE, textColor, bgColor,
                actionText, actionTextColor, textAlignment, listener);
    }

    /**
     * 设置snackbar文字和背景颜色
     *
     * @param parent          父视图(CoordinatorLayout或者DecorView)
     * @param text            文本
     * @param duration        显示时长
     * @param textColor       文本颜色
     * @param bgColor         背景色
     * @param actionText      事件文本
     * @param actionTextColor 事件文本颜色
     * @param textAlignment   事件文本显示位置 0 居左 1 居中 2 居右
     * @param listener        监听器
     */
    private static void showSnackbar(View parent, CharSequence text,
                                     int duration,
                                     @ColorInt int textColor, @ColorInt int bgColor,
                                     CharSequence actionText, int actionTextColor, int textAlignment,
                                     View.OnClickListener listener) {
        snackbarWeakReference = new WeakReference<>(Snackbar.make(parent, text, duration));
        Snackbar snackbar = snackbarWeakReference.get();
        View view = snackbar.getView();
        snackbar.setActionTextColor(textColor);
        view.setBackgroundColor(bgColor);
        TextView snackTV = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        if (null != snackTV) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                switch (textAlignment) {
                    case 0:
                        snackTV.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                        break;
                    case 1:
                        snackTV.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        break;
                    case 2:
                        snackTV.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                        break;
                }
            } else {
                switch (textAlignment) {
                    case 0:
                        snackTV.setGravity(Gravity.LEFT);
                        break;
                    case 1:
                        snackTV.setGravity(Gravity.CENTER_HORIZONTAL);
                        break;
                    case 2:
                        snackTV.setGravity(Gravity.RIGHT);
                        break;
                }
            }
        }
        if (actionText != null && actionText.length() > 0 && listener != null) {
            snackbar.setActionTextColor(actionTextColor);
            snackbar.setAction(actionText, listener);
        }
        snackbar.show();
    }

    /**
     * 为snackbar添加布局
     * <p>
     * <p>在show...Snackbar之后调用</p>
     *
     * @param layoutId 布局文件
     * @param index    位置(the position at which to add the child or -1 to add last)
     */
    public static void addView(int layoutId, int index) {
        Snackbar snackbar = snackbarWeakReference.get();
        if (snackbar != null) {
            View view = snackbar.getView();
            Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) view;
            View child = LayoutInflater.from(view.getContext()).inflate(layoutId, null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER_VERTICAL;
            layout.addView(child, index, params);
        }
    }

    /**
     * 取消snackbar显示
     */
    public static void dismissSnackbar() {
        if (snackbarWeakReference != null && snackbarWeakReference.get() != null) {
            snackbarWeakReference.get().dismiss();
            snackbarWeakReference = null;
        }
    }
}
