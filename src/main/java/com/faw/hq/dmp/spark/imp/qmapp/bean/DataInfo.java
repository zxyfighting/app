package com.faw.hq.dmp.spark.imp.qmapp.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @program: qmapp
 * @description
 * @author: ZhangXiuYun
 * @create: 2019-11-21 17:57
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataInfo {
    private DeviceInfo deviceInfo;
    private AddressInfo addressInfo;
    private LoginData loginData;
    private UserInfo userInfo;
    private VisitData visitData;

    public DeviceInfo getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(DeviceInfo deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    public AddressInfo getAddressInfo() {
        return addressInfo;
    }

    public void setAddressInfo(AddressInfo addressInfo) {
        this.addressInfo = addressInfo;
    }

    public LoginData getLoginData() {
        return loginData;
    }

    public void setLoginData(LoginData loginData) {
        this.loginData = loginData;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public VisitData getVisitData() {
        return visitData;
    }

    public void setVisitData(VisitData visitData) {
        this.visitData = visitData;
    }
    public static String getHive() {

        return DeviceInfo.getHive() + AddressInfo.getHive() + LoginData.getHive()+
                UserInfo.getHive()+VisitData.getHive();
    }
    //将数据转为字符串拼接
    public String jsonToLine() {
        StringBuilder sb = new StringBuilder();
        if (deviceInfo==null) {
            sb.append(DeviceInfo.getHive());
        } else {
            sb.append(deviceInfo.jsonToLine());
        }
        if (addressInfo==null) {
            sb.append(AddressInfo.getHive());
        } else {
            sb.append(addressInfo.jsonToLine());
        }
        if (loginData==null) {
            sb.append(LoginData.getHive());
        } else {
            sb.append(loginData.jsonToLine());
        }
        if (userInfo==null){
            sb.append(UserInfo.getHive());
        }
        else{
            sb.append(userInfo.jsonToLine());
        }
        if (visitData==null){
            sb.append(VisitData.getHive());
        }
        else{
            sb.append(visitData.jsonToLine());
        }

        return sb.toString();
    }

}
