package org.zywx.wbpalmstar.plugin.uexgaodemap;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.OnMapLoadedListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.overlay.PoiOverlay;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.Query;

import org.zywx.wbpalmstar.base.BUtility;
import org.zywx.wbpalmstar.base.view.BaseFragment;
import org.zywx.wbpalmstar.engine.universalex.EUExUtil;
import org.zywx.wbpalmstar.plugin.uexgaodemap.VO.CustomButtonDisplayResultVO;
import org.zywx.wbpalmstar.plugin.uexgaodemap.VO.CustomButtonResultVO;
import org.zywx.wbpalmstar.plugin.uexgaodemap.VO.CustomButtonVO;
import org.zywx.wbpalmstar.plugin.uexgaodemap.VO.VisibleBoundsVO;
import org.zywx.wbpalmstar.plugin.uexgaodemap.VO.VisibleVO;
import org.zywx.wbpalmstar.plugin.uexgaodemap.bean.ArcBean;
import org.zywx.wbpalmstar.plugin.uexgaodemap.bean.BoundBaseBean;
import org.zywx.wbpalmstar.plugin.uexgaodemap.bean.CircleBean;
import org.zywx.wbpalmstar.plugin.uexgaodemap.bean.CircleBoundBean;
import org.zywx.wbpalmstar.plugin.uexgaodemap.bean.CustomButtonBean;
import org.zywx.wbpalmstar.plugin.uexgaodemap.bean.GroundBean;
import org.zywx.wbpalmstar.plugin.uexgaodemap.bean.MarkerBean;
import org.zywx.wbpalmstar.plugin.uexgaodemap.bean.PolygonBean;
import org.zywx.wbpalmstar.plugin.uexgaodemap.bean.PolygonBoundBean;
import org.zywx.wbpalmstar.plugin.uexgaodemap.bean.PolylineBean;
import org.zywx.wbpalmstar.plugin.uexgaodemap.bean.RectangleBoundBean;
import org.zywx.wbpalmstar.plugin.uexgaodemap.bean.SearchBean;
import org.zywx.wbpalmstar.plugin.uexgaodemap.util.GaodeUtils;
import org.zywx.wbpalmstar.plugin.uexgaodemap.util.OnCallBackListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class AMapBasicFragment extends BaseFragment implements OnMapLoadedListener, LocationSource, AMap.OnMapClickListener, AMap.OnMapLongClickListener {
	public static final String TAG = "AMapBasicFragment";
	private MapView mapView;
	private AMap aMap;
	private UiSettings settings;
	public OnCallBackListener mListener;
	private double[] mCenter = null;
	private GaodeMapMarkerMgr markerMgr;
	private GaodeMapOverlayMgr overlayMgr;
	private LocationManagerProxy mLocationMgr;
	private OnLocationChangedListener mLocationChangedListener;
	private GaodeLocationListener mLocationListener;
	private GeocodeSearch geocodeSearch;
	private boolean isShowOverlay = false;
	private List<LatLng> mOverlays;
	private FrameLayout mContent;
	private HashMap<String, CustomButtonBean> mButtons;
	private View overlayView;

	public AMapBasicFragment() {
	}

	public AMapBasicFragment(OnCallBackListener mListener, double[] mCenter) {
		this.mListener = mListener;
		this.mCenter = mCenter;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(EUExUtil.getResLayoutID("plugin_uexgaodemap_basic_layout"), container, false);
		overlayView = view.findViewById(EUExUtil.getResIdID("overlay_view"));
		/*
		 * 设置离线地图存储目录，在下载离线地图或初始化地图设置; 使用过程中可自行设置, 若自行设置了离线地图存储的路径，
		 * 则需要在离线地图下载和使用地图页面都进行路径设置
		 */
		// Demo中为了其他界面可以使用下载的离线地图，使用默认位置存储，屏蔽了自定义设置
		// MapsInitializer.sdcardDir =OffLineMapUtils.getSdCacheDir(this);
		mContent = (FrameLayout) view.findViewById(EUExUtil.getResIdID("plugin_uexgaodemap_bg_content"));
		mButtons = new HashMap<String, CustomButtonBean>();
		mapView = (MapView) view.findViewById(EUExUtil.getResIdID("plugin_uexgaodemap_basic_map"));
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		init();
		aMap.setOnMapLoadedListener(this);
		aMap.setOnMapClickListener(this);
		aMap.setOnMapLongClickListener(this);
		markerMgr = new GaodeMapMarkerMgr(this.getActivity(), aMap, mListener, mOverlays);
		overlayMgr = new GaodeMapOverlayMgr(this.getActivity(), aMap, mListener, mOverlays);
		aMap.setOnMarkerClickListener(markerMgr);
		aMap.setOnInfoWindowClickListener(markerMgr);
		return view;
	}

	public void setOverlayVisibleBounds(VisibleBoundsVO data) {
		if (mOverlays != null && mOverlays.size() > 0) {
			LatLngBounds.Builder builder = LatLngBounds.builder();
			for (int i = 0; i < mOverlays.size(); i++) {
				LatLng item = mOverlays.get(i);
				builder.include(item);
			}
			aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), data.getPadding()));
		}
	}

	public void setMarkerVisibleBounds(VisibleBoundsVO data) {
		List<Marker> list = aMap.getMapScreenMarkers();
		if (list != null && list.size() > 0) {
			LatLngBounds.Builder builder = LatLngBounds.builder();
			for (int i = 0; i < list.size(); i++) {
				LatLng item = list.get(i).getPosition();
				builder.include(item);
			}
			aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), data.getPadding()));
		}
	}

	/**
	 * 初始化AMap对象
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
		}
		if (settings == null) {
			settings = aMap.getUiSettings();
		}
		if (mOverlays == null) {
			mOverlays = new ArrayList<LatLng>();
		}
		aMap.setLocationSource(this);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	public void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	public void onPause() {
		super.onPause();
		mapView.onPause();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	public void onDestroy() {
		if (aMap != null) {
			aMap.clear();
		}
		overlayMgr.clean();
		markerMgr.clearAll();
		stopLocation();
		super.onDestroy();
		mapView.onDestroy();
	}

	public void readyToDestroy() {
		if (overlayView != null) {
			overlayView.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onMapLoaded() {
		if (mListener != null) {
			mListener.onMapLoaded();
		}
		if (mCenter != null) {
			setCenter(mCenter[0], mCenter[1]);
			mCenter = null;
		}
	}

	public void setCenter(double longitude, double latitude) {
		if (aMap != null) {
			LatLng latLng = new LatLng(latitude, longitude);
			aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
		}
	}

	public void getCenter() {
		if (aMap != null) {
			LatLng latLng = aMap.getCameraPosition().target;
			mListener.cbGetCenter(latLng);
		}
	}

	/**
	 *
	 * @param level
	 *            value range 3~20
	 */
	public void setZoomLevel(float level) {
		if (aMap != null) {
			if (level < aMap.getMinZoomLevel()) {
				level = aMap.getMinZoomLevel();
			} else if (level > aMap.getMaxZoomLevel()) {
				level = aMap.getMaxZoomLevel();
			}
			aMap.moveCamera(CameraUpdateFactory.zoomTo(level));
		}

	}

	public void zoomIn() {
		if (aMap != null) {
			aMap.moveCamera(CameraUpdateFactory.zoomIn());
		}
	}

	public void zoomOut() {
		if (aMap != null) {
			aMap.moveCamera(CameraUpdateFactory.zoomOut());
		}
	}

	/**
	 * set map type
	 * 
	 * @param type
	 *            1-normal, 2-satellite, 3-night
	 */
	public void setMapType(int type) {
		if (aMap != null) {
			aMap.setMapType(type);
		}
	}

	/**
	 *
	 * @param type
	 *            0-enable, 1-unable
	 */
	public void setTrafficEnabled(int type) {
		if (aMap != null) {
			aMap.setTrafficEnabled(type == JsConst.ENABLE ? true : false);
		}
	}

	public void rotate(float angle) {
		if (aMap != null) {
			aMap.moveCamera(CameraUpdateFactory.changeBearing(angle));
		}
	}

	public void setRotateEnable(int type) {
		if (settings != null) {
			settings.setRotateGesturesEnabled(type == JsConst.ENABLE);
			settings.setTiltGesturesEnabled(type == JsConst.ENABLE);
		}
	}

	public void setCompassEnable(int type) {
		if (settings != null) {
			settings.setCompassEnabled(type == JsConst.ENABLE);
		}
	}

	public void setScrollEnable(int type) {
		if (settings != null) {
			settings.setScrollGesturesEnabled(type == JsConst.ENABLE);
		}
	}

	public void setOverlookEnable(int type) {
		if (settings != null) {
			settings.setTiltGesturesEnabled(type == JsConst.ENABLE);
		}
	}

	public void overlook(float angle) {
		if (aMap != null) {
			aMap.moveCamera(CameraUpdateFactory.changeTilt(angle));
		}
	}

	public void setZoomEnable(int type) {
		if (aMap != null) {
			settings.setZoomGesturesEnabled(type == JsConst.ENABLE);
		}
	}

	public void setScaleVisible(VisibleVO vo) {
		if (aMap != null) {
			settings.setScaleControlsEnabled(vo.isVisible());
		}
	}

	public void setMyLocationButtonVisible(VisibleVO vo) {
		if (aMap != null) {
			settings.setMyLocationButtonEnabled(vo.isVisible());
		}
	}

	public void setZoomVisible(VisibleVO vo) {
		if (aMap != null) {
			settings.setZoomControlsEnabled(vo.isVisible());
		}
	}

	public void addMarkersOverlay(List<MarkerBean> list) {
		if (markerMgr == null) {
			return;
		}
		markerMgr.addMarkers(list);
	}

	public void updateMarkersOverlay(MarkerBean bean) {
		if (markerMgr == null) {
			return;
		}
		markerMgr.updateMarker(bean);
	}

	public void removeMarkersOverlay(String id) {
		if (markerMgr == null) {
			return;
		}
		markerMgr.removeMarker(id);
	}

	public void showBubble(String id) {
		if (markerMgr == null) {
			return;
		}
		markerMgr.showBubble(id);
	}

	public void hideBubble() {
		if (markerMgr == null) {
			return;
		}
		markerMgr.hideBubble();
	}

	public void addPolylineOverlay(PolylineBean bean) {
		overlayMgr.addPolylines(bean);
	}

	public void removeOverlay(String id) {
		overlayMgr.removeOverlay(id);
	}

	public void addArcOverlay(ArcBean bean) {
		overlayMgr.addArc(bean);
	}

	public void addCircleOverlay(CircleBean bean) {
		overlayMgr.addCircle(bean);
	}

	public void addPolygonOverlay(PolygonBean bean) {
		overlayMgr.addPolygon(bean);
	}

	public void addGroundOverlay(GroundBean bean) {
		overlayMgr.addGround(bean);
	}

	public void getCurrentLocation() {
		if (mLocationMgr == null) {
			mLocationMgr = LocationManagerProxy.getInstance(this.getActivity());
		}
		if (mLocationListener == null) {
			mLocationListener = new GaodeLocationListener();
		}
		mLocationListener.setType(JsConst.GET_LOCATION);
		mLocationMgr.requestLocationData(LocationProviderProxy.AMapNetwork, -1, 0, mLocationListener);
	}

	@Override
	public void onMapClick(LatLng latLng) {
		if (mListener != null && latLng != null) {
			mListener.onMapClick(latLng);
		}
	}

	@Override
	public void onMapLongClick(LatLng latLng) {
		if (mListener != null && latLng != null) {
			mListener.onMapLongClick(latLng);
		}
	}

	public void removeOverlays(List<String> list) {
		if (list == null || list.size() == 0) {
			removeOverlay(null);
		} else {
			for (int i = 0; i < list.size(); i++) {
				removeOverlay(list.get(i));
			}
		}
	}

	public void removeMarkersOverlays(List<String> list) {
		if (list == null || list.size() == 0) {
			removeMarkersOverlay(null);
		} else {
			for (int i = 0; i < list.size(); i++) {
				removeMarkersOverlay(list.get(i));
			}
		}
	}

	public void clear() {
		if (aMap != null) {
			aMap.clear();
			markerMgr.clearAll();
			overlayMgr.clearAll();
		}
	}

	public void setCustomButton(final CustomButtonVO dataVO) {
		CustomButtonResultVO resultVO = new CustomButtonResultVO();
		resultVO.setId(dataVO.getId());
		if (!isAlreadyAdded(dataVO.getId())) {
			FrameLayout.LayoutParams lpParams = new FrameLayout.LayoutParams(dataVO.getWidth(), dataVO.getHeight());
			lpParams.leftMargin = dataVO.getX();
			lpParams.topMargin = dataVO.getY();
			Button btn = new Button(this.getActivity());
			btn.setLayoutParams(lpParams);
			btn.setPadding(0, 0, 0, 0);
			if (!TextUtils.isEmpty(dataVO.getTitle())) {
				btn.setText(dataVO.getTitle());
			}
			if (dataVO.getTextSize() > 0) {
				btn.setTextSize(dataVO.getTextSize());
			}
			if (!TextUtils.isEmpty(dataVO.getTitleColor())) {
				btn.setTextColor(BUtility.parseColor(dataVO.getTitleColor()));
			}
			if (!TextUtils.isEmpty(dataVO.getBgImage())) {
				Drawable bg = new BitmapDrawable(GaodeUtils.getImage(this.getActivity(), dataVO.getBgImage()));
				int version = Build.VERSION.SDK_INT;
				if (version < 16) {
					btn.setBackgroundDrawable(bg);
				} else {
					btn.setBackground(bg);
				}
			}
			mContent.addView(btn);
			btn.setVisibility(View.GONE);
			btn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (mListener != null) {
						EUExGaodeMap gaodeMap = mButtons.get(dataVO.getId()).getGaodeMap();
						mListener.onButtonClick(dataVO.getId(), gaodeMap);
					}
				}
			});
			CustomButtonBean button = new CustomButtonBean();
			button.setButton(btn);
			mButtons.put(dataVO.getId(), button);
			resultVO.setIsSuccess(true);
		} else {
			resultVO.setIsSuccess(false);
		}
		if (mListener != null) {
			mListener.cbSetCustomButton(resultVO);
		}
	}

	private boolean isAlreadyAdded(String id) {
		if (mButtons != null && mButtons.size() > 0 && !TextUtils.isEmpty(id)) {
			Iterator<String> it = mButtons.keySet().iterator();
			while (it.hasNext()) {
				String itemId = it.next();
				if (itemId.equals(id)) {
					return true;
				}
			}
		}
		return false;
	}

	public void deleteCustomButton(String id) {
		CustomButtonResultVO resultVO = new CustomButtonResultVO();
		resultVO.setId(id);
		resultVO.setIsSuccess(false);
		if (mButtons != null && mButtons.size() > 0 && !TextUtils.isEmpty(id)) {
			Iterator<String> it = mButtons.keySet().iterator();
			while (it.hasNext()) {
				final String itemId = it.next();
				if (itemId.equals(id)) {
					Button btn = mButtons.get(itemId).getButton();
					mContent.removeView(btn);
					it.remove();
					resultVO.setIsSuccess(true);
				}
			}
		}
		if (mListener != null) {
			mListener.cbRemoveCustomButton(resultVO);
		}
	}

	public void showCustomButtons(List<String> ids, EUExGaodeMap gaodeMap) {
		CustomButtonDisplayResultVO resultVO = new CustomButtonDisplayResultVO();
		List<String> successIds = new ArrayList<String>();
		List<String> failedIds = new ArrayList<String>();
		for (int i = 0; i < ids.size(); i++) {
			final String id = ids.get(i);
			if (isAlreadyAdded(id)) {
				Button btn = mButtons.get(id).getButton();
				mButtons.get(id).setGaodeMap(gaodeMap);
				if (btn != null && btn.getVisibility() == View.GONE) {
					btn.setVisibility(View.VISIBLE);
					successIds.add(id);
				} else {
					failedIds.add(id);
				}
			} else {
				failedIds.add(id);
			}
		}
		if (mListener != null) {
			resultVO.setFailedIds(failedIds);
			resultVO.setSuccessfulIds(successIds);
			mListener.cbShowCustomButtons(resultVO);
		}
	}

	public void hideCustomButtons(List<String> ids) {
		CustomButtonDisplayResultVO resultVO = new CustomButtonDisplayResultVO();
		List<String> successIds = new ArrayList<String>();
		List<String> failedIds = new ArrayList<String>();
		if (ids == null) {
			ids = getAllButtons();
		}
		for (int i = 0; i < ids.size(); i++) {
			final String id = ids.get(i);
			if (isAlreadyAdded(id)) {
				Button btn = mButtons.get(id).getButton();
				if (btn != null && btn.getVisibility() == View.VISIBLE) {
					btn.setVisibility(View.GONE);
					successIds.add(id);
				} else {
					failedIds.add(id);
				}
			} else {
				failedIds.add(id);
			}
		}
		if (mListener != null) {
			resultVO.setFailedIds(failedIds);
			resultVO.setSuccessfulIds(successIds);
			mListener.cbHideCustomButtons(resultVO);
		}
	}

	private List<String> getAllButtons() {
		List<String> list = new ArrayList<String>();
		Iterator<String> it = mButtons.keySet().iterator();
		while (it.hasNext()) {
			final String itemId = it.next();
			list.add(itemId);
		}
		return list;
	}

	private class GaodeLocationListener implements AMapLocationListener {
		int type = JsConst.INVALID;

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}

		@Override
		public void onLocationChanged(AMapLocation aMapLocation) {
			Log.i(TAG, "onLocationChanged");
			try {
				if (aMapLocation != null && aMapLocation.getAMapException() != null && aMapLocation.getAMapException().getErrorCode() == 0) {
					switch (type) {
					case JsConst.SHOW_LOCATION:
					case JsConst.CONTINUED:
						if (mLocationChangedListener != null) {
							mLocationChangedListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
						}
						if (mListener != null) {
							mListener.onReceiveLocation(aMapLocation);
						}
						break;
					case JsConst.GET_LOCATION:
						if (mListener != null) {
							mListener.cbGetCurrentLocation(aMapLocation);
						}
						break;
					}
				}
			} catch (Exception e) {
				// e.printStackTrace();
			}
		}

		@Override
		public void onLocationChanged(Location location) {

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

		@Override
		public void onProviderEnabled(String provider) {

		}

		@Override
		public void onProviderDisabled(String provider) {

		}
	}

	public void startLocation(long minTime, float minDistance) {
		if (mLocationMgr == null) {
			mLocationMgr = LocationManagerProxy.getInstance(this.getActivity());
		}
		if (mLocationListener == null) {
			mLocationListener = new GaodeLocationListener();
		}
		mLocationListener.setType(JsConst.CONTINUED);
		/*
		 * 第一个参数是定位provider，第二个参数时间最短是2000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
		 */
		mLocationMgr.requestLocationData(LocationProviderProxy.AMapNetwork, minTime, minDistance, mLocationListener);
	}

	public void stopLocation() {
		if (mLocationMgr != null) {
			if (mLocationListener != null) {
				mLocationMgr.removeUpdates(mLocationListener);
			}
			mLocationMgr.destroy();
		}
		mLocationMgr = null;
	}

	public void setMyLocationEnable(int type) {
		Log.i(TAG, "setMyLocationEnable-type = " + type);
		if (aMap != null) {
			aMap.setMyLocationEnabled(type == JsConst.ENABLE);
			if (type == JsConst.ENABLE) {
				if (mLocationListener == null) {
					mLocationListener = new GaodeLocationListener();
				}
				if (mLocationListener.getType() != JsConst.CONTINUED) {
					if (mLocationMgr == null)
						mLocationMgr = LocationManagerProxy.getInstance(this.getActivity());
					mLocationListener.setType(JsConst.SHOW_LOCATION);
					mLocationMgr.requestLocationData(LocationProviderProxy.AMapNetwork, -1, 0, mLocationListener);
				}
			}
		}
	}

	@Override
	public void activate(OnLocationChangedListener onLocationChangedListener) {
		Log.i(TAG, "activate");
		this.mLocationChangedListener = onLocationChangedListener;
	}

	@Override
	public void deactivate() {
		Log.i(TAG, "deactivate");
	}

	public void setMyLocationType(int type) {
		if (aMap != null) {
			aMap.setMyLocationType(type);
		}
	}

	public void geocode(GeocodeQuery query) {
		if (this.getActivity() == null) {
			return;
		}
		if (aMap != null) {
			if (geocodeSearch == null) {
				geocodeSearch = new GeocodeSearch(this.getActivity());
				geocodeSearch.setOnGeocodeSearchListener(geocodeSearchListener);
			}
			geocodeSearch.getFromLocationNameAsyn(query);
		}
	}

	public void reGeocode(RegeocodeQuery query) {
		if (this.getActivity() == null) {
			return;
		}
		if (aMap != null) {
			if (geocodeSearch == null) {
				geocodeSearch = new GeocodeSearch(this.getActivity());
				geocodeSearch.setOnGeocodeSearchListener(geocodeSearchListener);
			}
			geocodeSearch.getFromLocationAsyn(query);
		}
	}

	GeocodeSearch.OnGeocodeSearchListener geocodeSearchListener = new GeocodeSearch.OnGeocodeSearchListener() {
		@Override
		public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int errorCode) {
			if (mListener != null) {
				mListener.cbReverseGeocode(regeocodeResult, errorCode);
			}
		}

		@Override
		public void onGeocodeSearched(GeocodeResult geocodeResult, int errorCode) {
			if (mListener != null) {
				mListener.cbGeocode(geocodeResult, errorCode);
			}
		}
	};

	public void poiSearch(SearchBean bean) {
		isShowOverlay = false;
		if (aMap != null) {
			isShowOverlay = bean.isShowMarker();
			Query query = new Query(bean.getSearchKey(), bean.getPoiTypeSet(), bean.getCity());
			query.setPageNum(bean.getPageNumber());
			query.setPageSize(bean.getPageSize());
			query.setLimitDiscount(bean.isShowDiscount());
			query.setLimitGroupbuy(bean.isShowGroupbuy());
			PoiSearch search = new PoiSearch(this.getActivity(), query);
			if (bean.getSearchBound() != null) {
				PoiSearch.SearchBound bound = null;
				BoundBaseBean boundBaseBean = bean.getSearchBound();
				if (boundBaseBean.getType().equals(BoundBaseBean.TYPE_CIRCLE)) {
					CircleBoundBean circle = (CircleBoundBean) boundBaseBean;
					bound = new PoiSearch.SearchBound(circle.getCenter(), circle.getRadiusInMeters(), circle.isDistanceSort());
				} else if (boundBaseBean.getType().equals(BoundBaseBean.TYPE_RECTANGLE)) {
					RectangleBoundBean rectangle = (RectangleBoundBean) boundBaseBean;
					bound = new PoiSearch.SearchBound(rectangle.getLowerLeft(), rectangle.getUpperRight());
				} else if (boundBaseBean.getType().equals(BoundBaseBean.TYPE_POLYGON)) {
					PolygonBoundBean polygon = (PolygonBoundBean) boundBaseBean;
					bound = new PoiSearch.SearchBound(polygon.getList());
				}
				search.setBound(bound);
			}
			search.setLanguage(bean.getLanguage());
			search.setOnPoiSearchListener(onPoiSearchListener);
			search.searchPOIAsyn();
		}
	}

	public void poiSearchDetail(String poiId) {
		PoiSearch search = new PoiSearch(this.getActivity(), null);
		search.setOnPoiSearchListener(onPoiSearchListener);
		search.searchPOIDetailAsyn(poiId);
	}

	PoiSearch.OnPoiSearchListener onPoiSearchListener = new PoiSearch.OnPoiSearchListener() {
		@Override
		public void onPoiSearched(PoiResult poiResult, int errorCode) {
			if (mListener != null) {
				mListener.cbPoiSearch(poiResult, errorCode);
			}
			if (errorCode == 0 && poiResult != null && isShowOverlay && aMap != null) {
				ArrayList<PoiItem> poiItems = poiResult.getPois();
				// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
				if (poiItems != null && poiItems.size() > 0) {
					aMap.clear();// 清理之前的图标
					PoiOverlay poiOverlay = new PoiOverlay(aMap, poiItems);
					poiOverlay.removeFromMap();
					poiOverlay.addToMap();
					poiOverlay.zoomToSpan();
					isShowOverlay = false;
				}
			}
		}

		@Override
		public void onPoiItemDetailSearched(PoiItemDetail poiItemDetail, int errorCode) {
			if (errorCode == 0 && poiItemDetail != null && mListener != null) {
				mListener.cbPoiSearchDetail(poiItemDetail, errorCode);
			}
		}
	};
}
