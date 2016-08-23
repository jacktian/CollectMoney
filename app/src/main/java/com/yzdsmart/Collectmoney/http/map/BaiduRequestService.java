package com.yzdsmart.Collectmoney.http.map;

import com.yzdsmart.Collectmoney.bean.map.POIContent;
import com.yzdsmart.Collectmoney.http.Url;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by YZD on 2016/4/27.
 */
public interface BaiduRequestService {

    /**
     * @param ak          用户key
     * @param geotable_id Geotable主键
     * @param keyWord     检索关键字
     * @param location    检索中心点
     * @param radius      检索范围
     * @param page_size   分页数量
     * @param page_index  分页索引 当前页标，从0开始
     * @param mcode       手机查询码
     * @param filter      检索条件
     * @return
     */
    @GET(Url.BAIDU_POI_NEARBY)
    Observable<POIContent> findMoney(@Query("ak") String ak, @Query("geotable_id") Integer geotable_id, @Query("q") String keyWord, @Query("location") String location, @Query("radius") Integer radius, @Query("page_size") Integer page_size, @Query("page_index") Integer page_index, @Query("mcode") String mcode, @Query("filter") String filter);
}
