package org.zywx.wbpalmstar.plugin.uexgaodemap.VO;

import java.io.Serializable;

public class DownloadBaseVO implements Serializable{
    private static final long serialVersionUID = 6520813785208070891L;
    private int status;
    private long size;
    private int completeCode;
    private String url;
    private String version;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getCompleteCode() {
        return completeCode;
    }

    public void setCompleteCode(int completeCode) {
        this.completeCode = completeCode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
