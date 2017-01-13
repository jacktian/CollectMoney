package com.yzdsmart.Dingdingwen.views.gender_picker;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.views.gender_picker.config.GenderPickerConfig;
import com.yzdsmart.Dingdingwen.views.time_picker.adapters.ArrayWheelAdapter;
import com.yzdsmart.Dingdingwen.views.time_picker.wheel.OnWheelChangedListener;
import com.yzdsmart.Dingdingwen.views.time_picker.wheel.WheelView;

/**
 * Created by jzxiang on 16/4/20.
 */
public class GenderWheel implements OnWheelChangedListener {
    Context mContext;

    protected String[] mGenderDatas;

    private WheelView gender;
    private ArrayWheelAdapter mGenderAdapter;

    private String defaultGender = "男";
    private String mCurrentGender;

    private GenderPickerConfig mPickerConfig;

    public GenderWheel(View view, GenderPickerConfig pickerConfig) {
        mPickerConfig = pickerConfig;
        mContext = view.getContext();
        initGenderDatas(mContext);
        initialize(view);
    }

    public void initialize(View view) {
        initView(view);
        initGender();
    }

    void initView(View view) {
        gender = (WheelView) view.findViewById(R.id.gender);
        gender.addChangingListener(this);
    }

    void initGender() {
        int provinceDefault = -1;
        if (!TextUtils.isEmpty(defaultGender) && mGenderDatas.length > 0) {
            for (int i = 0; i < mGenderDatas.length; i++) {
                if (mGenderDatas[i].contains(defaultGender)) {
                    provinceDefault = i;
                    break;
                }
            }
        }
        mGenderAdapter = new ArrayWheelAdapter<String>(mContext, mGenderDatas);
        mGenderAdapter.setConfig(mPickerConfig);
        gender.setViewAdapter(mGenderAdapter);
        //获取所设置的省的位置，直接定位到该位置
        if (-1 != provinceDefault) {
            gender.setCurrentItem(provinceDefault);
            mCurrentGender = defaultGender;
        }
    }

    public String getCurrentGender() {
        return mCurrentGender;
    }

    /**
     * 解析省市区的XML数据
     */

    protected void initGenderDatas(Context context) {
        mGenderDatas = context.getResources().getStringArray(R.array.gender_array);
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (gender == wheel) {
            mCurrentGender = mGenderDatas[gender.getCurrentItem()];
        }
    }
}
