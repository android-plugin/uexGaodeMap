package org.zywx.wbpalmstar.plugin.uexgaodemap.VO;

import java.io.Serializable;

public class DownloadResultVO implements Serializable{
    private static final long serialVersionUID = 5622587699620078059L;
    private String name;
    private int errorCode;
    private String errorStr;

    public DownloadResultVO() {
    }

    public DownloadResultVO(int errorCode, String errorStr, String city) {
        this.errorCode = errorCode;
        this.errorStr = errorStr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorStr() {
        return errorStr;
    }

    public void setErrorStr(String errorStr) {
        this.errorStr = errorStr;
    }
}
