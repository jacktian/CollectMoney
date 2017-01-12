package com.yzdsmart.Dingdingwen.views.city_picker;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.views.time_picker.config.CityPickerConfig;
import com.yzdsmart.Dingdingwen.views.time_picker.data.Type;
import com.yzdsmart.Dingdingwen.views.time_picker.listener.OnCitySetListener;

/**
 * Created by jzxiang on 16/4/19.
 */
public class CityPickerDialog extends DialogFragment implements View.OnClickListener {
    CityPickerConfig mPickerConfig;
    private CityWheel mCityWheel;

    private static CityPickerDialog newIntance(CityPickerConfig pickerConfig) {
        CityPickerDialog cityPickerDialog = new CityPickerDialog();
        cityPickerDialog.initialize(pickerConfig);
        return cityPickerDialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Activity activity = getActivity();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    @Override
    public void onResume() {
        super.onResume();
        int height = getResources().getDimensionPixelSize(R.dimen.picker_height);

        Window window = getDialog().getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, height);//Here!
        window.setGravity(Gravity.BOTTOM);
    }

    private void initialize(CityPickerConfig pickerConfig) {
        mPickerConfig = pickerConfig;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.TimePick_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(initView());
        return dialog;
    }

    View initView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.city_picker_layout, null);
        TextView cancel = (TextView) view.findViewById(R.id.tv_cancel);
        cancel.setOnClickListener(this);
        TextView sure = (TextView) view.findViewById(R.id.tv_sure);
        sure.setOnClickListener(this);
        TextView title = (TextView) view.findViewById(R.id.tv_title);
        View toolbar = view.findViewById(R.id.toolbar);

        cancel.setText(mPickerConfig.mCancelString);
        cancel.setTextColor(mPickerConfig.mCancelTVColor);
        sure.setText(mPickerConfig.mSureString);
        sure.setTextColor(mPickerConfig.mSureTVColor);
        toolbar.setBackgroundColor(mPickerConfig.mThemeColor);
        title.setText(mPickerConfig.mTitleString);
        title.setTextColor(mPickerConfig.mTitleTVColor);

        mCityWheel = new CityWheel(view, mPickerConfig);
        return view;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_cancel) {
            dismiss();
        } else if (i == R.id.tv_sure) {
            sureClicked();
        }
    }

    /*
    * @desc This method is called when onClick method is invoked by sure button. A Calendar instance is created and 
    *       initialized. 
    * @param none
    * @return none
    */
    void sureClicked() {
        if (mPickerConfig.mCallBack != null) {
            mPickerConfig.mCallBack.onCitySet(this, mCityWheel.getCurrentProvince(), mCityWheel.getCurrentCity(), mCityWheel.getCurrentDistrict());
        }
        dismiss();
    }

    public static class Builder {
        CityPickerConfig mPickerConfig;

        public Builder() {
            mPickerConfig = new CityPickerConfig();
        }

        public Builder setType(Type type) {
            mPickerConfig.mType = type;
            return this;
        }

        public Builder setThemeColor(int color) {
            mPickerConfig.mThemeColor = color;
            return this;
        }

        public Builder setCancelStringId(String left) {
            mPickerConfig.mCancelString = left;
            return this;
        }

        public Builder setCancelTextColor(int color) {
            mPickerConfig.mCancelTVColor = color;
            return this;
        }

        public Builder setSureStringId(String right) {
            mPickerConfig.mSureString = right;
            return this;
        }

        public Builder setSureTextColor(int color) {
            mPickerConfig.mSureTVColor = color;
            return this;
        }

        public Builder setTitleStringId(String title) {
            mPickerConfig.mTitleString = title;
            return this;
        }

        public Builder setTitleTextColor(int color) {
            mPickerConfig.mTitleTVColor = color;
            return this;
        }

        public Builder setToolBarTextColor(int color) {
            mPickerConfig.mToolBarTVColor = color;
            return this;
        }

        public Builder setWheelItemTextNormalColor(int color) {
            mPickerConfig.mWheelTVNormalColor = color;
            return this;
        }

        public Builder setWheelItemTextSelectorColor(int color) {
            mPickerConfig.mWheelTVSelectorColor = color;
            return this;
        }

        public Builder setWheelItemTextSize(int size) {
            mPickerConfig.mWheelTVSize = size;
            return this;
        }

        public Builder setCyclic(boolean cyclic) {
            mPickerConfig.cyclic = cyclic;
            return this;
        }

        public Builder setCallBack(OnCitySetListener listener) {
            mPickerConfig.mCallBack = listener;
            return this;
        }

        public CityPickerDialog build() {
            return newIntance(mPickerConfig);
        }

    }

}
