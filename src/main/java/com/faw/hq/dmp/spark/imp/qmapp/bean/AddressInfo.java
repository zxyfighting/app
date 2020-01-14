package com.faw.hq.dmp.spark.imp.qmapp.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @program: qmapp
 * @description
 * @author: ZhangXiuYun
 * @create: 2019-11-21 17:40
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressInfo {
    private Double longitude;                 //经度
    private Double latitude;                  //维度

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    public static String getHive(){
        StringBuilder sb = new StringBuilder();
        sb.append("\\N;").append("\\N;");
        return sb.toString();
    }
    public String jsonToLine() {
        StringBuilder sb = new StringBuilder();
        if (latitude==null) {
            sb.append("\\N;");
        } else {
            sb.append(latitude).append(";");
        }
        if (longitude==null) {
            sb.append("\\N;");
        } else {
            sb.append(longitude).append(";");
        }
        return sb.toString();
    }

}
