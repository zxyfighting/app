package com.faw.hq.dmp.spark.imp.qmapp.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @program: qmapp
 * @description
 * @author: ZhangXiuYun
 * @create: 2019-11-21 17:43
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginData {
    private String aid;                       //用户登录账号
    private String phone;                     //用户手机号
    private Boolean loginSuccess;             //登录是否成功

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getLoginSuccess() {
        return loginSuccess;
    }

    public void setLoginSuccess(Boolean loginSuccess) {
        this.loginSuccess = loginSuccess;
    }
    public static String getHive(){
        StringBuilder sb = new StringBuilder();
        sb.append("\\N;").append("\\N;").append("\\N;");
        return sb.toString();
    }
    public String jsonToLine() {
        StringBuilder sb = new StringBuilder();
        if (aid == null || aid.trim().length()==0) {
            sb.append("\\N;");
        } else {
            sb.append(aid).append(";");
        }
        if (phone == null || phone.trim().length()==0) {
            sb.append("\\N;");
        } else {
            sb.append(phone).append(";");
        }
        if (loginSuccess == null ) {
            sb.append("\\N;");
        } else {
            sb.append(aid).append(";");
        }
        return sb.toString();
    }
}
