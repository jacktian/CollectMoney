package com.yzdsmart.Dingdingwen.views.city_picker;

import android.content.Context;
import android.content.res.AssetManager;
import android.view.View;

import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.views.city_picker.model.CityModel;
import com.yzdsmart.Dingdingwen.views.city_picker.model.DistrictModel;
import com.yzdsmart.Dingdingwen.views.city_picker.model.ProvinceModel;
import com.yzdsmart.Dingdingwen.views.city_picker.utils.XmlParserHandler;
import com.yzdsmart.Dingdingwen.views.time_picker.adapters.ArrayWheelAdapter;
import com.yzdsmart.Dingdingwen.views.time_picker.adapters.NumericWheelAdapter;
import com.yzdsmart.Dingdingwen.views.time_picker.config.PickerConfig;
import com.yzdsmart.Dingdingwen.views.time_picker.utils.PickerContants;
import com.yzdsmart.Dingdingwen.views.time_picker.wheel.OnWheelChangedListener;
import com.yzdsmart.Dingdingwen.views.time_picker.wheel.WheelView;

import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by jzxiang on 16/4/20.
 */
public class CityWheel {
    Context mContext;

    /**
     * 所有省
     */
    protected String[] mProvinceDatas;

    /**
     * key - 省 value - 市
     */
    protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();

    /**
     * key - 市 values - 区
     */
    protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

    /**
     * 当前省的名称
     */
    protected String mCurrentProvinceName;

    /**
     * 当前市的名称
     */
    protected String mCurrentCityName;

    /**
     * 当前区的名称
     */
    protected String mCurrentDistrictName = "";

    WheelView province, city, district;
    ArrayWheelAdapter mProvinceAdapter, mCityAdapter, mDistrictAdapter;

    PickerConfig mPickerConfig;

    OnWheelChangedListener provinceListener = new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            updateProvince();
        }
    };
    OnWheelChangedListener cityListener = new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            updateCity();
        }
    };
    OnWheelChangedListener districtListener = new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            updateDistrict();
        }
    };

    public CityWheel(View view, PickerConfig pickerConfig) {
        mPickerConfig = pickerConfig;

        mContext = view.getContext();
        initialize(view);
    }

    public void initialize(View view) {
        initProvinceDatas(mContext);
        initView(view);
        initProvince();
        initCity();
        initDistrict();
    }


    void initView(View view) {
        province = (WheelView) view.findViewById(R.id.province);
        city = (WheelView) view.findViewById(R.id.city);
        district = (WheelView) view.findViewById(R.id.day);

        province.addChangingListener(provinceListener);
        province.addChangingListener(cityListener);
        province.addChangingListener(districtListener);
        city.addChangingListener(cityListener);
        city.addChangingListener(districtListener);
        district.addChangingListener(districtListener);
    }

    void initProvince() {

        mProvinceAdapter = new ArrayWheelAdapter<String>(mContext, mProvinceDatas);
        mProvinceAdapter.setConfig(mPickerConfig);
        province.setViewAdapter(mProvinceAdapter);
        //获取所设置的省的位置，直接定位到该位置
        if (-1 != provinceDefault) {
            mViewProvince.setCurrentItem(provinceDefault);
        }
        province.setCurrentItem(mRepository.getDefaultCalendar().year - minYear);
    }

    void initCity() {
        updateMonths();
        int curYear = getCurrentYear();
        int minMonth = mRepository.getMinMonth(curYear);
        month.setCurrentItem(mRepository.getDefaultCalendar().month - minMonth);
        month.setCyclic(mPickerConfig.cyclic);
    }

    void initDistrict() {
        updateDays();
        int curYear = getCurrentYear();
        int curMonth = getCurrentMonth();

        int minDay = mRepository.getMinDay(curYear, curMonth);
        day.setCurrentItem(mRepository.getDefaultCalendar().day - minDay);
        day.setCyclic(mPickerConfig.cyclic);
    }

    void updateProvince() {
        if (month.getVisibility() == View.GONE)
            return;

        int curYear = getCurrentYear();
        int minMonth = mRepository.getMinMonth(curYear);
        int maxMonth = mRepository.getMaxMonth(curYear);
        mMonthAdapter = new NumericWheelAdapter(mContext, minMonth, maxMonth, PickerContants.FORMAT, mPickerConfig.mMonth);
        mMonthAdapter.setConfig(mPickerConfig);
        month.setViewAdapter(mMonthAdapter);

        if (mRepository.isMinYear(curYear)) {
            month.setCurrentItem(0, false);
        }
    }

    void updateCity() {
        if (day.getVisibility() == View.GONE)
            return;

        int curYear = getCurrentYear();
        int curMonth = getCurrentMonth();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + year.getCurrentItem());
        calendar.set(Calendar.MONTH, curMonth);

        int maxDay = mRepository.getMaxDay(curYear, curMonth);
        int minDay = mRepository.getMinDay(curYear, curMonth);
        mDayAdapter = new NumericWheelAdapter(mContext, minDay, maxDay, PickerContants.FORMAT, mPickerConfig.mDay);
        mDayAdapter.setConfig(mPickerConfig);
        day.setViewAdapter(mDayAdapter);

        if (mRepository.isMinMonth(curYear, curMonth)) {
            day.setCurrentItem(0, true);
        }

        int dayCount = mDayAdapter.getItemsCount();
        if (day.getCurrentItem() >= dayCount) {
            day.setCurrentItem(dayCount - 1, true);
        }
    }

    void updateDistrict() {
        if (hour.getVisibility() == View.GONE)
            return;

        int curYear = getCurrentYear();
        int curMonth = getCurrentMonth();
        int curDay = getCurrentDay();

        int minHour = mRepository.getMinHour(curYear, curMonth, curDay);
        int maxHour = mRepository.getMaxHour(curYear, curMonth, curDay);

        mHourAdapter = new NumericWheelAdapter(mContext, minHour, maxHour, PickerContants.FORMAT, mPickerConfig.mHour);
        mHourAdapter.setConfig(mPickerConfig);
        hour.setViewAdapter(mHourAdapter);

        if (mRepository.isMinDay(curYear, curMonth, curDay))
            hour.setCurrentItem(0, false);
    }

    public String getCurrentProvince() {
        return year.getCurrentItem() + mRepository.getMinYear();
    }

    public String getCurrentCity() {
        int curYear = getCurrentYear();
        return month.getCurrentItem() + +mRepository.getMinMonth(curYear);
    }

    public String getCurrentDistrict() {
        return district.getCurrentItem();
    }


    /**
     * 解析省市区的XML数据
     */

    protected void initProvinceDatas(Context context) {
        List<ProvinceModel> provinceList = null;
        AssetManager asset = context.getAssets();
        try {
            InputStream input = asset.open("province_data.xml");
            // 创建一个解析xml的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // 解析xml
            SAXParser parser = spf.newSAXParser();
            XmlParserHandler handler = new XmlParserHandler();
            parser.parse(input, handler);
            input.close();
            // 获取解析出来的数据
            provinceList = handler.getDataList();
            //*/ 初始化默认选中的省、市、区
            if (provinceList != null && !provinceList.isEmpty()) {
                mCurrentProvinceName = provinceList.get(0).getName();
                List<CityModel> cityList = provinceList.get(0).getCityList();
                if (cityList != null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getName();
                    List<DistrictModel> districtList = cityList.get(0).getDistrictList();
                    mCurrentDistrictName = districtList.get(0).getName();
                }
            }
            mProvinceDatas = new String[provinceList.size()];
            for (int i = 0; i < provinceList.size(); i++) {
                // 遍历所有省的数据
                mProvinceDatas[i] = provinceList.get(i).getName();
                List<CityModel> cityList = provinceList.get(i).getCityList();
                String[] cityNames = new String[cityList.size()];
                for (int j = 0; j < cityList.size(); j++) {
                    // 遍历省下面的所有市的数据
                    cityNames[j] = cityList.get(j).getName();
                    List<DistrictModel> districtList = cityList.get(j).getDistrictList();
                    String[] distrinctNameArray = new String[districtList.size()];
                    DistrictModel[] distrinctArray = new DistrictModel[districtList.size()];
                    for (int k = 0; k < districtList.size(); k++) {
                        // 遍历市下面所有区/县的数据
                        DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(),
                                districtList.get(k).getZipcode());
                        distrinctArray[k] = districtModel;
                        distrinctNameArray[k] = districtModel.getName();
                    }
                    // 市-区/县的数据，保存到mDistrictDatasMap
                    mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
                }
                // 省-市的数据，保存到mCitisDatasMap
                mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }
    }

}
