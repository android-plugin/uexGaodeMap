package org.zywx.wbpalmstar.plugin.uexgaodemap.VO;

import com.amap.api.maps.offlinemap.OfflineMapCity;

import java.io.Serializable;

public class AvailableCityVO extends DownloadBaseVO implements Serializable{
    private static final long serialVersionUID = 7601329482687674794L;
    private String city;


    public AvailableCityVO(OfflineMapCity data) {
        this.city = data.getCity();
        setStatus(data.getState());
        setSize(data.getSize());
        setCompleteCode(data.getcompleteCode());
        setUrl(data.getUrl());
        setVersion(data.getVersion());
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
