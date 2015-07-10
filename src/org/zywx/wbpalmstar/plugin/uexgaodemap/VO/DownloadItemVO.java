package org.zywx.wbpalmstar.plugin.uexgaodemap.VO;

import com.amap.api.maps.offlinemap.OfflineMapCity;
import com.amap.api.maps.offlinemap.OfflineMapProvince;

import org.zywx.wbpalmstar.plugin.uexgaodemap.JsConst;

import java.io.Serializable;

public class DownloadItemVO extends DownloadBaseVO implements Serializable{
    private static final long serialVersionUID = -4608942561405325414L;
    private String name;
    private int type;

    public DownloadItemVO() {
    }

    public DownloadItemVO(OfflineMapCity data) {
        this.name = data.getCity();
        this.type = JsConst.TYPE_CITY;
        setStatus(data.getState());
        setSize(data.getSize());
        setCompleteCode(data.getcompleteCode());
        setUrl(data.getUrl());
        setVersion(data.getVersion());
    }

    public DownloadItemVO(OfflineMapProvince data) {
        this.name = data.getProvinceName();
        this.type = JsConst.TYPE_PROVINCE;
        setStatus(data.getState());
        setSize(data.getSize());
        setCompleteCode(data.getcompleteCode());
        setUrl(data.getUrl());
        setVersion(data.getVersion());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
