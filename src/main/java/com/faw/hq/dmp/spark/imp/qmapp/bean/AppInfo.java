package com.faw.hq.dmp.spark.imp.qmapp.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @program: qmapp
 * @description
 * @author: ZhangXiuYun
 * @create: 2019-11-21 18:01
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppInfo {
    private String dataType;
    private String reportTime;
    private DataInfo data;

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getReportTime() {
        return reportTime;
    }

    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

    public DataInfo getData() {
        return data;
    }

    public void setData(DataInfo data) {
        this.data = data;
    }

    //将数据转为字符串拼接
    public String jsonToLine() {
        StringBuilder sb = new StringBuilder();
        if (dataType==null||dataType.length()==0) {
            sb.append("\\N;");
        } else {
            sb.append(dataType).append(";");
        }
        if (reportTime==null||reportTime.length()==0) {
            sb.append("\\N;");
        } else {
            sb.append(reportTime).append(";");
        }
        if (data==null){
            sb.append(DataInfo.getHive()).append(";").append("APP");
        }
        else{
            sb.append(data.jsonToLine()).append(";").append("APP");
        }

        return sb.toString();
    }
}
