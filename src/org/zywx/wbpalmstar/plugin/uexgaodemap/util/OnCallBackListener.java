package org.zywx.wbpalmstar.plugin.uexgaodemap.util;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;

import org.zywx.wbpalmstar.plugin.uexgaodemap.EUExGaodeMap;
import org.zywx.wbpalmstar.plugin.uexgaodemap.VO.AvailableCityVO;
import org.zywx.wbpalmstar.plugin.uexgaodemap.VO.AvailableProvinceVO;
import org.zywx.wbpalmstar.plugin.uexgaodemap.VO.CustomButtonDisplayResultVO;
import org.zywx.wbpalmstar.plugin.uexgaodemap.VO.CustomButtonResultVO;
import org.zywx.wbpalmstar.plugin.uexgaodemap.VO.DownloadItemVO;
import org.zywx.wbpalmstar.plugin.uexgaodemap.VO.DownloadResultVO;
import org.zywx.wbpalmstar.plugin.uexgaodemap.VO.DownloadStatusVO;
import org.zywx.wbpalmstar.plugin.uexgaodemap.VO.UpdateResultVO;

import java.io.Serializable;
import java.util.List;

public interface OnCallBackListener extends Serializable{
    public void onMapLoaded();
    public void onMarkerClicked(String id);
    public void onBubbleClicked(String id);
    public void onReceiveLocation(AMapLocation location);
    public void onMapClick(LatLng point);
    public void onMapLongClick(LatLng point);
    public void cbGetCurrentLocation(AMapLocation location);
    public void cbGeocode(GeocodeResult geocodeResult, int errorCode);
    public void cbReverseGeocode(RegeocodeResult regeocodeResult, int errorCode);
    public void cbPoiSearch(PoiResult result, int errorCode);
    public void cbDownload(DownloadResultVO data);
    public void onDownload(DownloadStatusVO data);
    public void cbDelete(DownloadResultVO data);

    public void cbGetDownloadingList(List<DownloadItemVO> data);
    public void cbGetDownloadList(List<DownloadItemVO> data);
    public void cbGetAvailableCityList(List<AvailableCityVO> data);

    public void cbGetAvailableProvinceList(List<AvailableProvinceVO> data);

    public void cbIsUpdate(UpdateResultVO data);

    public void cbSetCustomButton(CustomButtonResultVO data);
    public void cbRemoveCustomButton(CustomButtonResultVO data);

    public void cbShowCustomButtons(CustomButtonDisplayResultVO data);
    public void cbHideCustomButtons(CustomButtonDisplayResultVO data);

    public void onButtonClick(String id, EUExGaodeMap gaodeMap);

}
