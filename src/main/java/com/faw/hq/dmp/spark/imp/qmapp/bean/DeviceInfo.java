package com.faw.hq.dmp.spark.imp.qmapp.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @program: qmapp
 * @description
 * @author: ZhangXiuYun
 * @create: 2019-11-21 17:34
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceInfo {
    private String deviceId;                  //设备号
    private String deviceBrand;               //终端品牌
    private String deviceModel;               //终端型号
    private String os;                        //操作系统
    private String osVersion;                 //操作系统版本
    private String appList;        //客户已安装的应用列表

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceBrand() {
        return deviceBrand;
    }

    public void setDeviceBrand(String deviceBrand) {
        this.deviceBrand = deviceBrand;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getAppList() {
        return appList;
    }

    public void setAppList(String appList) {
        this.appList = appList;
    }

    public static String getHive(){
        StringBuilder sb = new StringBuilder();
        sb.append("\\N;").append("\\N;").append("\\N;").append("\\N;").append("\\N;").append("\\N;");
        return sb.toString();
    }
    public String jsonToLine() {
        StringBuilder sb = new StringBuilder();
        if (deviceId == null || deviceId.trim().length()==0) {
            sb.append("\\N;");
        } else {
            sb.append(deviceId).append(";");
        }
        if (deviceBrand == null || deviceBrand.trim().length()==0) {
            sb.append("\\N;");
        } else {
            sb.append(deviceBrand).append(";");
        }
        if (deviceModel == null ||deviceModel.trim().length()==0) {
            sb.append("\\N;");
        } else {
            sb.append(deviceModel).append(";");
        }
        if (os == null || os.trim().length()==0) {
            sb.append("\\N;");
        } else {
            sb.append(os).append(";");
        }
        if (osVersion == null ||osVersion.trim().length()==0) {
            sb.append("\\N;");
        } else {
            sb.append(osVersion).append(";");
        }
        if (appList == null ||appList.trim().length()==0) {
            sb.append("\\N;");
        } else {
            sb.append(appList).append(";");
        }
        return sb.toString();
    }


}
