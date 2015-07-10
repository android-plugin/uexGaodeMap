package org.zywx.wbpalmstar.plugin.uexgaodemap.VO;

import com.amap.api.maps.offlinemap.OfflineMapCity;
import com.amap.api.maps.offlinemap.OfflineMapProvince;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AvailableProvinceVO extends DownloadBaseVO implements Serializable{
    private static final long serialVersionUID = -4295869011822841418L;
    private String province;
    private List<AvailableCityVO> cityList;

    public AvailableProvinceVO(OfflineMapProvince data) {
        this.province = data.getProvinceName();
        setStatus(data.getState());
        setSize(data.getSize());
        setCompleteCode(data.getcompleteCode());
        setUrl(data.getUrl());
        setVersion(data.getVersion());
        List<AvailableCityVO> list = new ArrayList<AvailableCityVO>();
        List<OfflineMapCity> temp = data.getCityList();
        for (int i = 0; i < temp.size(); i++){
            list.add(new AvailableCityVO(temp.get(i)));
        }
        this.cityList = list;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public List<AvailableCityVO> getCityList() {
        return cityList;
    }

    public void setCityList(List<AvailableCityVO> cityList) {
        this.cityList = cityList;
    }
}
