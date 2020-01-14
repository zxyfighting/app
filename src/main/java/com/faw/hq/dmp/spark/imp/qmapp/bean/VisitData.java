package com.faw.hq.dmp.spark.imp.qmapp.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @program: qmapp
 * @description
 * @author: ZhangXiuYun
 * @create: 2019-11-21 17:45
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
public class VisitData {
    private String pageUrl;
    private String pageType;
    private String pageParam;
    private String pageLabel;
    private String refData;


    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getPageType() {
        return pageType;
    }

    public void setPageType(String pageType) {
        this.pageType = pageType;
    }

    public String getPageParam() {
        return pageParam;
    }

    public void setPageParam(String pageParam) {
        this.pageParam = pageParam;
    }

    public String getPageLabel() {
        return pageLabel;
    }

    public void setPageLabel(String pageLabel) {
        this.pageLabel = pageLabel;
    }

    public String getRefData() {
        return refData;
    }

    public void setRefData(String refData) {
        this.refData = refData;
    }

    public static String getHive(){
        StringBuilder sb = new StringBuilder();
        sb.append("\\N;").append("\\N;").append("\\N;").append("\\N;").append("\\N");
        return sb.toString();
    }
    public String jsonToLine() {
        StringBuilder sb = new StringBuilder();
        if (pageUrl==null||pageUrl.trim().length()==0) {
            sb.append("\\N;");
        } else {
            sb.append(pageUrl).append(";");
        }
        if (pageType==null||pageType.trim().length()==0) {
            sb.append("\\N;");
        } else {
            sb.append(pageType).append(";");
        }
        if (pageParam==null||pageParam.trim().length()==0) {
            sb.append("\\N;");
        } else {
            sb.append(pageParam).append(";");
        }
        if (pageLabel==null||pageLabel.trim().length()==0) {
            sb.append("\\N;");
        } else {
            sb.append(pageLabel).append(";");
        }
        if (refData==null||refData.trim().length()==0 ) {
            sb.append("\\N");
        } else {
            sb.append(refData);
        }
        return sb.toString();
    }

}
