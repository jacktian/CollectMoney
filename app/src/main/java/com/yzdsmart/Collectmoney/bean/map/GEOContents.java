package com.yzdsmart.Collectmoney.bean.map;

import java.util.Arrays;

/**
 * Created by YZD on 2016/7/5.
 */
public class GEOContents {
    private Integer sq_state;
    private Integer uid;
    private String province;
    private Integer geotable_id;
    private String district;
    private Long create_time;
    private String city;
    private Double[] location;
    private String address;
    private String title;
    private Integer coord_type;
    private String direction;
    private Integer type;
    private Long distance;
    private Integer weight;
    private String shop_name;

    public Integer getSq_state() {
        return sq_state;
    }

    public void setSq_state(Integer sq_state) {
        this.sq_state = sq_state;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public Integer getGeotable_id() {
        return geotable_id;
    }

    public void setGeotable_id(Integer geotable_id) {
        this.geotable_id = geotable_id;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public Long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Long create_time) {
        this.create_time = create_time;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Double[] getLocation() {
        return location;
    }

    public void setLocation(Double[] location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCoord_type() {
        return coord_type;
    }

    public void setCoord_type(Integer coord_type) {
        this.coord_type = coord_type;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getDistance() {
        return distance;
    }

    public void setDistance(Long distance) {
        this.distance = distance;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    @Override
    public String toString() {
        return "{" +
                "sq_state:" + sq_state +
                ", uid:" + uid +
                ", province:'" + province + '\'' +
                ", geotable_id:" + geotable_id +
                ", district:'" + district + '\'' +
                ", create_time:" + create_time +
                ", city:'" + city + '\'' +
                ", location:" + Arrays.toString(location) +
                ", address:'" + address + '\'' +
                ", title:'" + title + '\'' +
                ", coord_type:" + coord_type +
                ", direction:'" + direction + '\'' +
                ", type:" + type +
                ", distance:" + distance +
                ", weight:" + weight +
                ", shop_name:" + shop_name +
                '}';
    }
}
