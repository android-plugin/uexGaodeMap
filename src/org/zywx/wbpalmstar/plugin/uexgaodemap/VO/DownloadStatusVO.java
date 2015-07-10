package org.zywx.wbpalmstar.plugin.uexgaodemap.VO;

import java.io.Serializable;

public class DownloadStatusVO implements Serializable{
    private static final long serialVersionUID = 6772116065977600736L;
    private int status;
    private int completeCode;
    private String name;

    public DownloadStatusVO(int status, int completeCode, String name) {
        this.status = status;
        this.completeCode = completeCode;
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCompleteCode() {
        return completeCode;
    }

    public void setCompleteCode(int completeCode) {
        this.completeCode = completeCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
