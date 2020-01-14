package com.faw.hq.dmp.spark.imp.qmapp.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @program: qmapp
 * @description
 * @author: ZhangXiuYun
 * @create: 2019-11-21 17:44
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfo {
    private String phoneNo;

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public static String getHive() {
        StringBuilder sb = new StringBuilder();
        sb.append("\\N;");
        return sb.toString();
    }

    public String jsonToLine() {
        StringBuilder sb = new StringBuilder();
        if (phoneNo == null || phoneNo.length()==0) {
            sb.append("\\N;");
        } else {
            sb.append(phoneNo).append(";");
        }
        return sb.toString();
    }
}