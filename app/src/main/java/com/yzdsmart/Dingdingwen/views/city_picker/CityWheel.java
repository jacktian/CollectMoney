package com.yzdsmart.Dingdingwen.views.city_picker;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.view.View;

import com.yzdsmart.Dingdingwen.R;
import com.yzdsmart.Dingdingwen.views.city_picker.model.CityModel;
import com.yzdsmart.Dingdingwen.views.city_picker.model.DistrictModel;
import com.yzdsmart.Dingdingwen.views.city_picker.model.ProvinceModel;
import com.yzdsmart.Dingdingwen.views.city_picker.utils.XmlParserHandler;
import com.yzdsmart.Dingdingwen.views.time_picker.adapters.ArrayWheelAdapter;
import com.yzdsmart.Dingdingwen.views.time_picker.config.CityPickerConfig;
import com.yzdsmart.Dingdingwen.views.time_picker.wheel.OnWheelChangedListener;
import com.yzdsmart.Dingdingwen.views.time_picker.wheel.WheelView;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by jzxiang on 16/4/20.
 */
public class CityWheel implements OnWheelChangedListener {
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

    /**
     * 第一次默认的显示省份，一般配合定位，使用
     */
    private String defaultProvinceName = "江苏";

    /**
     * 第一次默认得显示城市，一般配合定位，使用
     */
    private String defaultCityName = "常州";

    /**
     * 第一次默认得显示，一般配合定位，使用
     */
    private String defaultDistrict = "新北区";

    private WheelView province, city, district;
    private ArrayWheelAdapter mProvinceAdapter, mCityAdapter, mDistrictAdapter;

    private CityPickerConfig mPickerConfig;

    public CityWheel(View view, CityPickerConfig pickerConfig) {
        mPickerConfig = pickerConfig;
        mContext = view.getContext();
        initProvinceDatas(mContext);
        initialize(view);
    }

    public void initialize(View view) {
        initView(view);
        initProvince();
    }

    void initView(View view) {
        province = (WheelView) view.findViewById(R.id.province);
        city = (WheelView) view.findViewById(R.id.city);
        district = (WheelView) view.findViewById(R.id.district);

        province.addChangingListener(this);
        city.addChangingListener(this);
        district.addChangingListener(this);
    }

    void initProvince() {
        int provinceDefault = -1;
        if (!TextUtils.isEmpty(defaultProvinceName) && mProvinceDatas.length > 0) {
            for (int i = 0; i < mProvinceDatas.length; i++) {
                if (mProvinceDatas[i].contains(defaultProvinceName)) {
                    provinceDefault = i;
                    break;
                }
            }
        }
        mProvinceAdapter = new ArrayWheelAdapter<String>(mContext, mProvinceDatas);
        mProvinceAdapter.setConfig(mPickerConfig);
        province.setViewAdapter(mProvinceAdapter);
        //获取所设置的省的位置，直接定位到该位置
        if (-1 != provinceDefault) {
            province.setCurrentItem(provinceDefault);
        }
        updateCity();
        updateDistrict();
    }

    void updateCity() {
        int pCurrent = province.getCurrentItem();
        mCurrentProvinceName = mProvinceDatas[pCurrent];
        String[] cities = mCitisDatasMap.get(mCurrentProvinceName);
        if (cities == null) {
            cities = new String[]{""};
        }
        int cityDefault = -1;
        if (!TextUtils.isEmpty(defaultCityName) && cities.length > 0) {
            for (int i = 0; i < cities.length; i++) {
                if (cities[i].contains(defaultCityName)) {
                    cityDefault = i;
                    break;
                }
            }
        }
        mCityAdapter = new ArrayWheelAdapter<String>(mContext, cities);
        mCityAdapter.setConfig(mPickerConfig);
        city.setViewAdapter(mCityAdapter);
        if (-1 != cityDefault) {
            city.setCurrentItem(cityDefault);
            mCurrentCityName = defaultCityName;
        } else {
            city.setCurrentItem(0);
            mCurrentCityName = mCitisDatasMap.get(mCurrentProvinceName)[0];
        }
        updateDistrict();
    }

    void updateDistrict() {
        int pCurrent = city.getCurrentItem();
        mCurrentCityName = mCitisDatasMap.get(mCurrentProvinceName)[pCurrent];
        String[] areas = mDistrictDatasMap.get(mCurrentCityName);
        if (areas == null) {
            areas = new String[]{""};
        }
        int districtDefault = -1;
        if (!TextUtils.isEmpty(defaultDistrict) && areas.length > 0) {
            for (int i = 0; i < areas.length; i++) {
                if (areas[i].contains(defaultDistrict)) {
                    districtDefault = i;
                    break;
                }
            }
        }
        mDistrictAdapter = new ArrayWheelAdapter<String>(mContext, areas);
        mDistrictAdapter.setConfig(mPickerConfig);
        district.setViewAdapter(mDistrictAdapter);
        if (-1 != districtDefault) {
            district.setCurrentItem(districtDefault);
            //获取默认设置的区
            mCurrentDistrictName = defaultDistrict;
        } else {
            district.setCurrentItem(0);
            //获取第一个区名称
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[0];
        }
    }

    public String getCurrentProvince() {
        return mCurrentProvinceName;
    }

    public String getCurrentCity() {
        return mCurrentCityName;
    }

    public String getCurrentDistrict() {
        return mCurrentDistrictName;
    }

    /**
     * 解析省市区的XML数据
     */

    protected void initProvinceDatas(Context context) {
        List<ProvinceModel> provinceList;
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

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (province == wheel) {
            updateCity();
        } else if (city == wheel) {
            updateDistrict();
        } else if (district == wheel) {
            mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
        }
    }
}
