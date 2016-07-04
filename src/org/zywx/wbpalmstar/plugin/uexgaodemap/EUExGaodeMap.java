package org.zywx.wbpalmstar.plugin.uexgaodemap;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.maps.model.ArcOptions;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.GroundOverlayOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.PolygonOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.offlinemap.OfflineMapStatus;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.Cinema;
import com.amap.api.services.poisearch.Dining;
import com.amap.api.services.poisearch.Hotel;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.Scenic;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.zywx.wbpalmstar.base.BUtility;
import org.zywx.wbpalmstar.engine.DataHelper;
import org.zywx.wbpalmstar.engine.EBrowserView;
import org.zywx.wbpalmstar.engine.universalex.EUExBase;
import org.zywx.wbpalmstar.plugin.uexgaodemap.VO.AvailableCityVO;
import org.zywx.wbpalmstar.plugin.uexgaodemap.VO.AvailableProvinceVO;
import org.zywx.wbpalmstar.plugin.uexgaodemap.VO.CustomButtonDisplayResultVO;
import org.zywx.wbpalmstar.plugin.uexgaodemap.VO.CustomButtonResultVO;
import org.zywx.wbpalmstar.plugin.uexgaodemap.VO.CustomButtonVO;
import org.zywx.wbpalmstar.plugin.uexgaodemap.VO.DownloadItemVO;
import org.zywx.wbpalmstar.plugin.uexgaodemap.VO.DownloadResultVO;
import org.zywx.wbpalmstar.plugin.uexgaodemap.VO.DownloadStatusVO;
import org.zywx.wbpalmstar.plugin.uexgaodemap.VO.UpdateResultVO;
import org.zywx.wbpalmstar.plugin.uexgaodemap.VO.VisibleBoundsVO;
import org.zywx.wbpalmstar.plugin.uexgaodemap.VO.VisibleVO;
import org.zywx.wbpalmstar.plugin.uexgaodemap.bean.ArcBean;
import org.zywx.wbpalmstar.plugin.uexgaodemap.bean.BoundBaseBean;
import org.zywx.wbpalmstar.plugin.uexgaodemap.bean.CircleBean;
import org.zywx.wbpalmstar.plugin.uexgaodemap.bean.CircleBoundBean;
import org.zywx.wbpalmstar.plugin.uexgaodemap.bean.GroundBean;
import org.zywx.wbpalmstar.plugin.uexgaodemap.bean.MarkerBean;
import org.zywx.wbpalmstar.plugin.uexgaodemap.bean.PolygonBean;
import org.zywx.wbpalmstar.plugin.uexgaodemap.bean.PolygonBoundBean;
import org.zywx.wbpalmstar.plugin.uexgaodemap.bean.PolylineBean;
import org.zywx.wbpalmstar.plugin.uexgaodemap.bean.RectangleBoundBean;
import org.zywx.wbpalmstar.plugin.uexgaodemap.bean.SearchBean;
import org.zywx.wbpalmstar.plugin.uexgaodemap.result.PoiItemVO;
import org.zywx.wbpalmstar.plugin.uexgaodemap.result.ResultVO;
import org.zywx.wbpalmstar.plugin.uexgaodemap.util.GaodeUtils;
import org.zywx.wbpalmstar.plugin.uexgaodemap.util.OnCallBackListener;

import java.util.ArrayList;
import java.util.List;

public class EUExGaodeMap extends EUExBase implements OnCallBackListener {
	private static final String TAG = "EUExGaodeMap";
	private static final String BUNDLE_DATA = "data";
	private static final int MSG_OPEN = 1;
	private static final int MSG_CLOSE = 2;
	private static final int MSG_HIDE_MAP = 3;
	private static final int MSG_SHOW_MAP = 4;
	private static final int MSG_SET_MAP_TYPE = 5;
	private static final int MSG_SET_TRAFFIC_ENABLED = 6;
	private static final int MSG_SET_CENTER = 7;
	private static final int MSG_SET_ZOOM_LEVEL = 8;
	private static final int MSG_ZOOM_IN = 9;
	private static final int MSG_ZOOM_OUT = 10;
	private static final int MSG_ROTATE = 11;
	private static final int MSG_OVERLOOK = 12;
	private static final int MSG_SET_ZOOM_ENABLE = 13;
	private static final int MSG_SET_ROTATE_ENABLE = 14;
	private static final int MSG_SET_COMPASS_ENABLE = 15;
	private static final int MSG_SET_SCROLL_ENABLE = 16;
	private static final int MSG_SET_OVERLOOK_ENABLE = 17;
	private static final int MSG_ADD_MARKERS_OVERLAY = 18;
	private static final int MSG_SET_MARKER_OVERLAY = 19;
	private static final int MSG_SHOW_BUBBLE = 20;
	private static final int MSG_HIDE_BUBBLE = 21;
	private static final int MSG_ADD_DOT_OVERLAY = 22;
	private static final int MSG_ADD_POLYLINE_OVERLAY = 23;
	private static final int MSG_ADD_ARC_OVERLAY = 24;
	private static final int MSG_ADD_CIRCLE_OVERLAY = 25;
	private static final int MSG_ADD_POLYGON_OVERLAY = 26;
	private static final int MSG_ADD_GROUND_OVERLAY = 27;
	private static final int MSG_ADD_TEXT_OVERLAY = 28;
	private static final int MSG_REMOVE_MAKERS_OVERLAY = 29;
	private static final int MSG_POI_SEARCH_IN_CITY = 30;
	private static final int MSG_POI_NEARBY_SEARCH = 31;
	private static final int MSG_POI_BOUND_SEARCH = 32;
	private static final int MSG_BUS_LINE_SEARCH = 33;
	private static final int MSG_REMOVE_BUS_LINE = 34;
	private static final int MSG_PRE_BUS_LINE_NODE = 35;
	private static final int MSG_NEXT_BUS_LINE_NODE = 36;
	private static final int MSG_SEARCH_ROUTE_PLAN = 37;
	private static final int MSG_REMOVE_ROUTE_PLAN = 38;
	private static final int MSG_PRE_ROUTE_NODE = 39;
	private static final int MSG_NEXT_ROUTE_NODE = 40;
	private static final int MSG_GEOCODE = 41;
	private static final int MSG_REVERSE_GEOCODE = 42;
	private static final int MSG_GET_CURRENT_LOCATION = 43;
	private static final int MSG_START_LOCATION = 44;
	private static final int MSG_STOP_LOCATION = 45;
	private static final int MSG_SET_MY_LOCATION_ENABLE = 46;
	private static final int MSG_SET_USER_TRACKING_MODE = 47;
	private static final int MSG_REMOVE_OVERLAY = 48;
	private static final int MSG_POI_SEARCH_DETAIL = 49;
	private static final long serialVersionUID = 4361331124195620438L;
	private static final int MSG_SET_SCALE_VISIBLE = 50;
	private static final int MSG_SET_MY_LOCATION_BUTTON_VISIBLE = 51;
	private static final int MSG_SET_ZOOM_VISIBLE = 52;
	private static final int MSG_REMOVE_MARKERS_OVERLAYS = 53;
	private static final int MSG_REMOVE_OVERLAYS = 54;
	private static final int MSG_SET_OVERLAY_VISIBLE_BOUNDS = 55;
	private static final int MSG_SET_MARKER_VISIBLE_BOUNDS = 56;
	private static final int MSG_CLEAR = 57;
	private static final int MSG_DOWNLOAD = 58;
	private static final int MSG_PAUSE = 59;
	private static final int MSG_GET_AVAILABLE_CITY_LIST = 60;
	private static final int MSG_GET_AVAILABLE_PROVINCE_LIST = 61;
	private static final int MSG_GET_DOWNLOAD_LIST = 62;
	private static final int MSG_GET_DOWNLOADING_LIST = 63;
	private static final int MSG_IS_UPDATE = 64;
	private static final int MSG_DELETE = 65;
	private static final int MSG_RESTART = 66;
	private static final int MSG_STOP_DOWNLOAD = 67;
	private static final int MSG_SET_CUSTOM_BUTTON = 68;
	private static final int MSG_REMOVE_CUSTOM_BUTTON = 69;
	private static final int MSG_SHOW_CUSTOM_BUTTONS = 70;
	private static final int MSG_HIDE_CUSTOM_BUTTONS = 71;
	private static final int MSG_GET_CENTER = 72;

	private boolean isScrollWithWeb = false;

	private AMapBasicFragment basicFragment;

	public EUExGaodeMap(Context context, EBrowserView eBrowserView) {
		super(context, eBrowserView);
		MapsInitializer.sdcardDir = GaodeUtils.getCacheDir();
	}

	@Override
	protected boolean clean() {
		return false;
	}

	public static void onActivityCreate(Context context) {
		((Activity) context).getWindow().setFormat(PixelFormat.TRANSLUCENT);
	}

	public void open(String[] params) {
		if (params == null || params.length < 1) {
			errorCallback(0, 0, "error params!");
			return;
		}
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_OPEN;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void openMsg(final String[] params) {
		if (getAMapActivity() != null) {
			close(null);
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					openAMap(params);
				}
			}, 500);
		} else {
			openAMap(params);
		}

	}

	@SuppressWarnings("deprecation")
	private void openAMap(final String[] params) {
		String json = params[0];
		int left = 0;
		int top = 0;
		int width = -1;
		int height = -1;
		double[] latlng = null;
		try {
			JSONObject jsonObject = new JSONObject(json);
			double l = Double.valueOf(jsonObject.getString(JsConst.LEFT));
			left = (int) l;
			double t = Double.valueOf(jsonObject.getString(JsConst.TOP));
			top = (int) t;
			double w = Double.valueOf(jsonObject.getString(JsConst.WIDTH));
			width = (int) w;
			double h = Double.valueOf(jsonObject.getString(JsConst.HEIGHT));
			height = (int) h;
			if (jsonObject.has(JsConst.LONGITUDE) && jsonObject.has(JsConst.LATITUDE)) {
				double longitude = Double.valueOf(jsonObject.getString(JsConst.LONGITUDE));
				double latitude = Double.valueOf(jsonObject.getString(JsConst.LATITUDE));
				latlng = new double[2];
				latlng[0] = longitude;
				latlng[1] = latitude;
			}
			if (jsonObject.has(JsConst.IS_SCROLL_WITH_WEB)) {
				isScrollWithWeb = Boolean.valueOf(jsonObject.getString(JsConst.IS_SCROLL_WITH_WEB));
			}
		} catch (JSONException e) {
			e.printStackTrace();
			left = 0;
			top = 0;
			width = -1;
			height = -1;
		}
		basicFragment = new AMapBasicFragment(EUExGaodeMap.this, latlng);

		if (isScrollWithWeb) {
			android.widget.AbsoluteLayout.LayoutParams lp = new android.widget.AbsoluteLayout.LayoutParams(width, height, left, top);
			addFragmentToWebView(basicFragment, lp, TAG);
		} else {
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(width, height);
			lp.leftMargin = left;
			lp.topMargin = top;
			addFragmentToCurrentWindow(basicFragment, lp, TAG);
		}
		callBackPluginJs(JsConst.CALLBACK_OPEN, "");
	}

	public void close(String[] params) {
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_CLOSE;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void closeMsg() {
		if (getAMapActivity() == null) {
			return;
		}
		getAMapActivity().readyToDestroy();
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				removeAMapView(getAMapActivity());
			}
		}, 30);
	}

	public void hideMap(String[] params) {
		if (params == null || params.length < 1) {
			errorCallback(0, 0, "error params!");
			return;
		}
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_HIDE_MAP;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void hideMapMsg(String[] params) {
		String json = params[0];
		try {
			new JSONObject(json);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void showMap(String[] params) {
		if (params == null || params.length < 1) {
			errorCallback(0, 0, "error params!");
			return;
		}
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_SHOW_MAP;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void showMapMsg(String[] params) {
		String json = params[0];
		try {
			new JSONObject(json);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void setMapType(String[] params) {
		if (params == null || params.length < 1) {
			errorCallback(0, 0, "error params!");
			return;
		}
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_SET_MAP_TYPE;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void setMapTypeMsg(String[] params) {
		if (getAMapActivity() == null) {
			return;
		}
		String json = params[0];
		int mapType = 1;
		try {
			JSONObject jsonObject = new JSONObject(json);
			mapType = Integer.valueOf(jsonObject.getString(JsConst.TYPE));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		AMapBasicFragment activity = getAMapActivity();
		activity.setMapType(mapType);
	}

	public void setTrafficEnabled(String[] params) {
		if (params == null || params.length < 1) {
			errorCallback(0, 0, "error params!");
			return;
		}
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_SET_TRAFFIC_ENABLED;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void setTrafficEnabledMsg(String[] params) {
		if (getAMapActivity() == null) {
			return;
		}
		String json = params[0];
		int trafficType = 1;
		try {
			JSONObject jsonObject = new JSONObject(json);
			trafficType = Integer.valueOf(jsonObject.getString(JsConst.TYPE));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		getAMapActivity().setTrafficEnabled(trafficType);
	}

	public void setCenter(String[] params) {
		if (params == null || params.length < 1) {
			errorCallback(0, 0, "error params!");
			return;
		}
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_SET_CENTER;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void setCenterMsg(String[] params) {
		if (getAMapActivity() == null) {
			return;
		}
		String json = params[0];
		double longitude = 0l;
		double latitude = 0l;
		try {
			JSONObject jsonObject = new JSONObject(json);
			longitude = Double.valueOf(jsonObject.getString(JsConst.LONGITUDE));
			latitude = Double.valueOf(jsonObject.getString(JsConst.LATITUDE));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		AMapBasicFragment activity = getAMapActivity();
		activity.setCenter(longitude, latitude);
	}

	public void getCenter(String[] params) {
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_GET_CENTER;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void getCenterMsg(String[] params) {
		if (getAMapActivity() == null) {
			return;
		}
		AMapBasicFragment activity = getAMapActivity();
		activity.getCenter();
	}

	public void setZoomLevel(String[] params) {
		if (params == null || params.length < 1) {
			errorCallback(0, 0, "error params!");
			return;
		}
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_SET_ZOOM_LEVEL;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void setZoomLevelMsg(String[] params) {
		if (getAMapActivity() == null) {
			return;
		}
		String json = params[0];
		float level = 1f;
		try {
			JSONObject jsonObject = new JSONObject(json);
			level = Float.valueOf(jsonObject.getString(JsConst.LEVEL));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		AMapBasicFragment activity = getAMapActivity();
		activity.setZoomLevel(level);
	}

	public void zoomIn(String[] params) {
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_ZOOM_IN;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void zoomInMsg() {
		if (getAMapActivity() == null) {
			return;
		}
		AMapBasicFragment activity = getAMapActivity();
		activity.zoomIn();
	}

	public void zoomOut(String[] params) {
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_ZOOM_OUT;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void zoomOutMsg() {
		if (getAMapActivity() == null) {
			return;
		}
		AMapBasicFragment activity = getAMapActivity();
		activity.zoomOut();
	}

	public void rotate(String[] params) {
		if (params == null || params.length < 1) {
			errorCallback(0, 0, "error params!");
			return;
		}
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_ROTATE;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void rotateMsg(String[] params) {
		if (getAMapActivity() == null) {
			return;
		}
		String json = params[0];
		float angle = 0f;
		try {
			JSONObject jsonObject = new JSONObject(json);
			angle = Float.valueOf(jsonObject.getString(JsConst.ANGLE));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		getAMapActivity().rotate(angle);
	}

	public void overlook(String[] params) {
		if (params == null || params.length < 1) {
			errorCallback(0, 0, "error params!");
			return;
		}
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_OVERLOOK;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void overlookMsg(String[] params) {
		if (getAMapActivity() == null) {
			return;
		}
		String json = params[0];
		float angle = 0f;
		try {
			JSONObject jsonObject = new JSONObject(json);
			angle = Float.valueOf(jsonObject.getString(JsConst.ANGLE));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		getAMapActivity().overlook(angle);
	}

	public void setZoomEnable(String[] params) {
		if (params == null || params.length < 1) {
			errorCallback(0, 0, "error params!");
			return;
		}
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_SET_ZOOM_ENABLE;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void setZoomEnableMsg(String[] params) {
		if (getAMapActivity() == null) {
			return;
		}
		String json = params[0];
		int type = 0;
		try {
			JSONObject jsonObject = new JSONObject(json);
			type = Integer.valueOf(jsonObject.getString(JsConst.TYPE));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		getAMapActivity().setZoomEnable(type);
	}

	public void setRotateEnable(String[] params) {
		if (params == null || params.length < 1) {
			errorCallback(0, 0, "error params!");
			return;
		}
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_SET_ROTATE_ENABLE;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void setRotateEnableMsg(String[] params) {
		if (getAMapActivity() == null) {
			return;
		}
		String json = params[0];
		int type = 0;
		try {
			JSONObject jsonObject = new JSONObject(json);
			type = Integer.valueOf(jsonObject.getString(JsConst.TYPE));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		getAMapActivity().setRotateEnable(type);
	}

	public void setCompassEnable(String[] params) {
		if (params == null || params.length < 1) {
			errorCallback(0, 0, "error params!");
			return;
		}
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_SET_COMPASS_ENABLE;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void setCompassEnableMsg(String[] params) {
		if (getAMapActivity() == null) {
			return;
		}
		String json = params[0];
		int type = 0;
		try {
			JSONObject jsonObject = new JSONObject(json);
			type = Integer.valueOf(jsonObject.getString(JsConst.TYPE));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		getAMapActivity().setCompassEnable(type);
	}

	public void setScrollEnable(String[] params) {
		if (params == null || params.length < 1) {
			errorCallback(0, 0, "error params!");
			return;
		}
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_SET_SCROLL_ENABLE;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void setScrollEnableMsg(String[] params) {
		if (getAMapActivity() == null) {
			return;
		}
		String json = params[0];
		int type = 0;
		try {
			JSONObject jsonObject = new JSONObject(json);
			type = Integer.valueOf(jsonObject.getString(JsConst.TYPE));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		getAMapActivity().setScrollEnable(type);
	}

	public void setOverlookEnable(String[] params) {
		if (params == null || params.length < 1) {
			errorCallback(0, 0, "error params!");
			return;
		}
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_SET_OVERLOOK_ENABLE;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void setOverlookEnableMsg(String[] params) {
		if (getAMapActivity() == null) {
			return;
		}
		String json = params[0];
		int type = 0;
		try {
			JSONObject jsonObject = new JSONObject(json);
			type = Integer.valueOf(jsonObject.getString(JsConst.TYPE));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		getAMapActivity().setOverlookEnable(type);
	}

	public void addMarkersOverlay(String[] params) {
		if (params == null || params.length < 1) {
			errorCallback(0, 0, "error params!");
			return;
		}
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_ADD_MARKERS_OVERLAY;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void addMarkersOverlayMsg(String[] params) {
		String json = params[0];
		List<MarkerBean> list = GaodeUtils.getAddMarkersData(mBrwView, json);
		if (getAMapActivity() != null) {
			getAMapActivity().addMarkersOverlay(list);
		}
	}

	public void setMarkerOverlay(String[] params) {
		if (params == null || params.length < 1) {
			errorCallback(0, 0, "error params!");
			return;
		}
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_SET_MARKER_OVERLAY;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void setMarkerOverlayMsg(String[] params) {
		String json = params[0];
		MarkerBean bean = GaodeUtils.getMarkerData(mBrwView, json);
		if (getAMapActivity() != null) {
			getAMapActivity().updateMarkersOverlay(bean);
		}
	}

	public void showBubble(String[] params) {
		if (params == null || params.length < 1) {
			errorCallback(0, 0, "error params!");
			return;
		}
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_SHOW_BUBBLE;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void showBubbleMsg(String[] params) {
		String json = params[0];
		String id = null;
		try {
			JSONObject jsonObject = new JSONObject(json);
			id = jsonObject.getString(JsConst.ID);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (!TextUtils.isEmpty(id) && getAMapActivity() != null) {
			getAMapActivity().showBubble(id);
		}
	}

	public void hideBubble(String[] params) {
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_HIDE_BUBBLE;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void hideBubbleMsg(String[] params) {
		if (getAMapActivity() != null) {
			getAMapActivity().hideBubble();
		}
	}

	public void addDotOverlay(String[] params) {
		if (params == null || params.length < 1) {
			errorCallback(0, 0, "error params!");
			return;
		}
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_ADD_DOT_OVERLAY;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void addDotOverlayMsg(String[] params) {
		String json = params[0];
		try {
			new JSONObject(json);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void addPolylineOverlay(String[] params) {
		if (params == null || params.length < 1) {
			errorCallback(0, 0, "error params!");
			return;
		}
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_ADD_POLYLINE_OVERLAY;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void addPolylineOverlayMsg(String[] params) {
		String json = params[0];
		PolylineBean bean = null;
		try {
			JSONObject jsonObject = new JSONObject(json);
			String id = jsonObject.getString(JsConst.ID);
			bean = new PolylineBean();
			bean.setId(id);
			PolylineOptions option = new PolylineOptions();
			if (jsonObject.has(JsConst.FILLCOLOR)) {
				String color = jsonObject.getString(JsConst.FILLCOLOR);
				option.color(BUtility.parseColor(color));
			}
			if (jsonObject.has(JsConst.LINEWIDTH)) {
				float lineWidth = Float.valueOf(jsonObject.getString(JsConst.LINEWIDTH));
				option.width(lineWidth);
			}
			String jsonProperty = jsonObject.getString(JsConst.PROPERTY);
			JSONArray property = new JSONArray(jsonProperty);
			for (int i = 0; i < property.length(); i++) {
				JSONObject item = property.optJSONObject(i);
				float longitude = Float.valueOf(item.getString(JsConst.LONGITUDE));
				float latitude = Float.valueOf(item.getString(JsConst.LATITUDE));
				LatLng latLng = new LatLng(latitude, longitude);
				option.add(latLng);
			}
			bean.setData(option);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (bean != null && getAMapActivity() != null) {
			getAMapActivity().addPolylineOverlay(bean);
		}
	}

	public void addArcOverlay(String[] params) {
		if (params == null || params.length < 1) {
			errorCallback(0, 0, "error params!");
			return;
		}
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_ADD_ARC_OVERLAY;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void addArcOverlayMsg(String[] params) {
		String json = params[0];
		ArcBean bean = null;
		try {
			JSONObject jsonObject = new JSONObject(json);
			bean = new ArcBean();
			String id = jsonObject.getString(JsConst.ID);
			bean.setId(id);
			ArcOptions option = new ArcOptions();
			if (jsonObject.has(JsConst.STROKECOLOR)) {
				String strokeColor = jsonObject.getString(JsConst.STROKECOLOR);
				option.strokeColor(BUtility.parseColor(strokeColor));
			}
			if (jsonObject.has(JsConst.LINEWIDTH)) {
				String lineWidth = jsonObject.getString(JsConst.LINEWIDTH);
				option.strokeWidth(Float.valueOf(lineWidth));
			}
			JSONObject startJson = jsonObject.getJSONObject(JsConst.START);
			LatLng start = new LatLng(Float.valueOf(startJson.getString(JsConst.LATITUDE)), Float.valueOf(startJson.getString(JsConst.LONGITUDE)));
			JSONObject centerJson = jsonObject.getJSONObject(JsConst.CENTER);
			LatLng center = new LatLng(Float.valueOf(centerJson.getString(JsConst.LATITUDE)), Float.valueOf(centerJson.getString(JsConst.LONGITUDE)));
			JSONObject endJson = jsonObject.getJSONObject(JsConst.END);
			LatLng end = new LatLng(Float.valueOf(endJson.getString(JsConst.LATITUDE)), Float.valueOf(endJson.getString(JsConst.LONGITUDE)));
			option.point(start, center, end);
			bean.setData(option);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (bean != null && getAMapActivity() != null) {
			getAMapActivity().addArcOverlay(bean);
		}
	}

	public void addCircleOverlay(String[] params) {
		if (params == null || params.length < 1) {
			errorCallback(0, 0, "error params!");
			return;
		}
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_ADD_CIRCLE_OVERLAY;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void addCircleOverlayMsg(String[] params) {
		String json = params[0];
		CircleBean bean = null;
		try {
			JSONObject jsonObject = new JSONObject(json);
			String id = jsonObject.getString(JsConst.ID);
			CircleOptions option = new CircleOptions();
			String longitude = jsonObject.getString(JsConst.LONGITUDE);
			String latitude = jsonObject.getString(JsConst.LATITUDE);
			LatLng latLng = new LatLng(Float.valueOf(latitude), Float.valueOf(longitude));
			option.center(latLng);
			String radius = jsonObject.getString(JsConst.RADIUS);
			option.radius(Double.valueOf(radius));
			bean = new CircleBean();
			bean.setId(id);
			if (jsonObject.has(JsConst.STROKECOLOR)) {
				String strokeColor = jsonObject.getString(JsConst.STROKECOLOR);
				option.strokeColor(BUtility.parseColor(strokeColor));
			}
			if (jsonObject.has(JsConst.LINEWIDTH)) {
				String lineWidth = jsonObject.getString(JsConst.LINEWIDTH);
				option.strokeWidth(Float.valueOf(lineWidth));
			}
			if (jsonObject.has(JsConst.FILLCOLOR)) {
				String color = jsonObject.getString(JsConst.FILLCOLOR);
				option.fillColor(BUtility.parseColor(color));
			}
			bean.setData(option);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (bean != null && getAMapActivity() != null) {
			getAMapActivity().addCircleOverlay(bean);
		}
	}

	public void addPolygonOverlay(String[] params) {
		if (params == null || params.length < 1) {
			errorCallback(0, 0, "error params!");
			return;
		}
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_ADD_POLYGON_OVERLAY;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void addPolygonOverlayMsg(String[] params) {
		String json = params[0];
		PolygonBean bean = null;
		try {
			JSONObject jsonObject = new JSONObject(json);
			PolygonOptions option = new PolygonOptions();
			String id = jsonObject.getString(JsConst.ID);
			JSONArray dataArray = new JSONArray(jsonObject.getString(JsConst.PROPERTY));
			for (int i = 0; i < dataArray.length(); i++) {
				JSONObject item = dataArray.optJSONObject(i);
				float longitude = Float.valueOf(item.getString(JsConst.LONGITUDE));
				float latitude = Float.valueOf(item.getString(JsConst.LATITUDE));
				LatLng latLng = new LatLng(latitude, longitude);
				option.add(latLng);
			}
			bean = new PolygonBean();
			bean.setId(id);
			if (jsonObject.has(JsConst.STROKECOLOR)) {
				String strokeColor = jsonObject.getString(JsConst.STROKECOLOR);
				option.strokeColor(BUtility.parseColor(strokeColor));
			}
			if (jsonObject.has(JsConst.LINEWIDTH)) {
				String lineWidth = jsonObject.getString(JsConst.LINEWIDTH);
				option.strokeWidth(Float.valueOf(lineWidth));
			}
			if (jsonObject.has(JsConst.FILLCOLOR)) {
				String color = jsonObject.getString(JsConst.FILLCOLOR);
				option.fillColor(BUtility.parseColor(color));
			}
			bean.setData(option);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (bean != null && getAMapActivity() != null) {
			getAMapActivity().addPolygonOverlay(bean);
		}
	}

	public void addGroundOverlay(String[] params) {
		if (params == null || params.length < 1) {
			errorCallback(0, 0, "error params!");
			return;
		}
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_ADD_GROUND_OVERLAY;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void addGroundOverlayMsg(String[] params) {
		String json = params[0];
		GroundBean bean = null;
		try {
			JSONObject jsonObject = new JSONObject(json);
			String id = jsonObject.getString(JsConst.ID);
			String imgUrl = jsonObject.getString(JsConst.IMAGEURL);
			GroundOverlayOptions option = new GroundOverlayOptions();
			String property = jsonObject.getString(JsConst.PROPERTY);
			JSONArray propertyArray = new JSONArray(property);
			if (propertyArray.length() == 1) {
				JSONObject item = propertyArray.optJSONObject(0);
				float longitude = Float.valueOf(item.getString(JsConst.LONGITUDE));
				float latitude = Float.valueOf(item.getString(JsConst.LATITUDE));
				LatLng latLng = new LatLng(latitude, longitude);
				float width = Float.valueOf(item.getString(JsConst.IMAGEWIDTH));
				if (jsonObject.has(JsConst.IMAGEHEIGHT)) {
					float height = Float.valueOf(item.getString(JsConst.IMAGEHEIGHT));
					option.position(latLng, width, height);
				} else {
					option.position(latLng, width);
				}
			} else if (propertyArray.length() == 2) {
				JSONObject item = propertyArray.optJSONObject(0);
				float longitude = Float.valueOf(item.getString(JsConst.LONGITUDE));
				float latitude = Float.valueOf(item.getString(JsConst.LATITUDE));
				LatLng latLng = new LatLng(latitude, longitude);

				JSONObject item1 = propertyArray.optJSONObject(1);
				float longitude1 = Float.valueOf(item1.getString(JsConst.LONGITUDE));
				float latitude1 = Float.valueOf(item1.getString(JsConst.LATITUDE));
				LatLng latLng1 = new LatLng(latitude1, longitude1);

				LatLngBounds latLngBounds = new LatLngBounds(latLng, latLng1);
				option.positionFromBounds(latLngBounds);
			}
			bean = new GroundBean();
			bean.setId(id);
			bean.setImageUrl(imgUrl);
			if (jsonObject.has(JsConst.TRANSPARENCY)) {
				float trans = Float.valueOf(jsonObject.getString(JsConst.TRANSPARENCY));
				option.transparency(trans);
			}
			bean.setData(option);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (bean != null && getAMapActivity() != null) {
			getAMapActivity().addGroundOverlay(bean);
		}
	}

	public void addTextOverlay(String[] params) {
		if (params == null || params.length < 1) {
			errorCallback(0, 0, "error params!");
			return;
		}
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_ADD_TEXT_OVERLAY;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void addTextOverlayMsg(String[] params) {
		String json = params[0];
		try {
			new JSONObject(json);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void removeMarkersOverlay(String[] params) {
		if (params == null || params.length < 1) {
			errorCallback(0, 0, "error params!");
			return;
		}
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_REMOVE_MAKERS_OVERLAY;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void removeMarkersOverlayMsg(String[] params) {
		String json = params[0];
		String id = null;
		try {
			JSONObject jsonObject = new JSONObject(json);
			id = jsonObject.getString(JsConst.ID);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (!TextUtils.isEmpty(id) && getAMapActivity() != null) {
			getAMapActivity().removeMarkersOverlay(id);
		}
	}

	public void poiSearch(String[] params) {
		if (params == null || params.length < 1) {
			errorCallback(0, 0, "error params!");
			return;
		}
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_POI_SEARCH_IN_CITY;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void poiSearchMsg(String[] params) {
		String json = params[0];
		SearchBean bean = null;
		try {
			JSONObject jsonObject = new JSONObject(json);

			if (jsonObject.has(JsConst.SEARCHKEY) || jsonObject.has(JsConst.POITYPESET)) {
				bean = new SearchBean();
				if (jsonObject.has(JsConst.SEARCHKEY)) {
					String searchKey = jsonObject.getString(JsConst.SEARCHKEY);
					bean.setSearchKey(searchKey);
				}
				if (jsonObject.has(JsConst.POITYPESET)) {
					String poiTypeSet = jsonObject.getString(JsConst.POITYPESET);
					bean.setPoiTypeSet(poiTypeSet);
				}
				if (jsonObject.has(JsConst.CITY)) {
					String city = jsonObject.getString(JsConst.CITY);
					bean.setCity(city);
				}
				if (jsonObject.has(JsConst.PAGENUM)) {
					int pageNum = Integer.valueOf(jsonObject.getString(JsConst.PAGENUM));
					bean.setPageNumber(pageNum);
				}
				if (jsonObject.has(JsConst.PAGESIZE)) {
					int pageSize = Integer.valueOf(jsonObject.getString(JsConst.PAGESIZE));
					bean.setPageSize(pageSize);
				}
				if (jsonObject.has(JsConst.LANGUAGE)) {
					String language = jsonObject.getString(JsConst.LANGUAGE);
					bean.setLanguage(language);
				}
				if (jsonObject.has(JsConst.ISSHOWMARKER)) {
					bean.setShowMarker(Boolean.valueOf(jsonObject.getString(JsConst.ISSHOWMARKER)));
				}
				if (jsonObject.has(JsConst.ISSHOWDISCOUNT)) {
					bean.setShowDiscount(Boolean.valueOf(jsonObject.getString(JsConst.ISSHOWDISCOUNT)));
				}
				if (jsonObject.has(JsConst.ISSHOWGROUPBUY)) {
					bean.setShowGroupbuy(Boolean.valueOf(jsonObject.getString(JsConst.ISSHOWGROUPBUY)));
				}
				if (jsonObject.has(JsConst.SEARCHBOUND)) {
					JSONObject boundJson = jsonObject.getJSONObject(JsConst.SEARCHBOUND);
					String type = boundJson.getString(JsConst.TYPE);
					if (type.equals(BoundBaseBean.TYPE_CIRCLE)) {
						JSONObject circle_dataInfo = boundJson.getJSONObject(JsConst.DATAINFO);
						JSONObject centerJson = circle_dataInfo.getJSONObject(JsConst.CENTER);
						double longitude = Double.valueOf(centerJson.getString(JsConst.LONGITUDE));
						double latitude = Double.valueOf(centerJson.getString(JsConst.LATITUDE));
						LatLonPoint center = new LatLonPoint(latitude, longitude);
						CircleBoundBean circle = new CircleBoundBean(BoundBaseBean.TYPE_CIRCLE);
						circle.setCenter(center);
						if (circle_dataInfo.has(JsConst.RADIUS)) {
							int radius = Integer.valueOf(circle_dataInfo.getString(JsConst.RADIUS));
							circle.setRadiusInMeters(radius);
						}
						if (circle_dataInfo.has(JsConst.ISDISTANCESORT)) {
							boolean isDistanceSort = Boolean.valueOf(circle_dataInfo.getString(JsConst.ISDISTANCESORT));
							circle.setDistanceSort(isDistanceSort);
						}
						bean.setSearchBound(circle);
					} else if (type.equals(BoundBaseBean.TYPE_RECTANGLE)) {
						JSONObject rectangle_dataInfo = boundJson.getJSONObject(JsConst.DATAINFO);
						JSONObject lowerLeftJson = rectangle_dataInfo.getJSONObject(JsConst.LOWERLEFT);
						double llLongitude = Double.valueOf(lowerLeftJson.getString(JsConst.LONGITUDE));
						double llLatitude = Double.valueOf(lowerLeftJson.getString(JsConst.LATITUDE));
						LatLonPoint ll = new LatLonPoint(llLatitude, llLongitude);
						JSONObject upperRightJson = rectangle_dataInfo.getJSONObject(JsConst.UPPERRIGHT);
						double urLongitude = Double.valueOf(upperRightJson.getString(JsConst.LONGITUDE));
						double urLatitude = Double.valueOf(upperRightJson.getString(JsConst.LATITUDE));
						LatLonPoint ur = new LatLonPoint(urLatitude, urLongitude);
						RectangleBoundBean rectangle = new RectangleBoundBean(BoundBaseBean.TYPE_RECTANGLE);
						rectangle.setLowerLeft(ll);
						rectangle.setUpperRight(ur);
						bean.setSearchBound(rectangle);
					} else if (type.equals(BoundBaseBean.TYPE_POLYGON)) {
						JSONArray dataArray = boundJson.getJSONArray(JsConst.DATAINFO);
						PolygonBoundBean polygon = new PolygonBoundBean(BoundBaseBean.TYPE_POLYGON);
						List<LatLonPoint> list = new ArrayList<LatLonPoint>();
						for (int i = 0; i < dataArray.length(); i++) {
							JSONObject item = new JSONObject(dataArray.optString(i));
							double itemLng = Double.valueOf(item.getString(JsConst.LONGITUDE));
							double itemLat = Double.valueOf(item.getString(JsConst.LATITUDE));
							LatLonPoint point = new LatLonPoint(itemLat, itemLng);
							list.add(point);
						}
						polygon.setList(list);
						bean.setSearchBound(polygon);
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (bean != null && getAMapActivity() != null) {
			getAMapActivity().poiSearch(bean);
		} else {
			errorCallback(0, 0, "error params!");
		}
	}

	public void poiNearbySearch(String[] params) {
		if (params == null || params.length < 1) {
			errorCallback(0, 0, "error params!");
			return;
		}
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_POI_NEARBY_SEARCH;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void poiNearbySearchMsg(String[] params) {
		String json = params[0];
		try {
			new JSONObject(json);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void poiBoundSearch(String[] params) {
		if (params == null || params.length < 1) {
			errorCallback(0, 0, "error params!");
			return;
		}
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_POI_BOUND_SEARCH;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void poiBoundSearchMsg(String[] params) {
		String json = params[0];
		try {
			new JSONObject(json);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void busLineSearch(String[] params) {
		if (params == null || params.length < 1) {
			errorCallback(0, 0, "error params!");
			return;
		}
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_BUS_LINE_SEARCH;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void busLineSearchMsg(String[] params) {
		String json = params[0];
		try {
			new JSONObject(json);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONObject jsonResult = new JSONObject();
		try {
			jsonResult.put("", "");
		} catch (JSONException e) {
		}
		callBackPluginJs(JsConst.CALLBACK_BUS_LINE_SEARCH, jsonResult.toString());
	}

	public void removeBusLine(String[] params) {
		if (params == null || params.length < 1) {
			errorCallback(0, 0, "error params!");
			return;
		}
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_REMOVE_BUS_LINE;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void removeBusLineMsg(String[] params) {
		String json = params[0];
		try {
			new JSONObject(json);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void preBusLineNode(String[] params) {
		if (params == null || params.length < 1) {
			errorCallback(0, 0, "error params!");
			return;
		}
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_PRE_BUS_LINE_NODE;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void preBusLineNodeMsg(String[] params) {
		String json = params[0];
		try {
			new JSONObject(json);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void nextBusLineNode(String[] params) {
		if (params == null || params.length < 1) {
			errorCallback(0, 0, "error params!");
			return;
		}
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_NEXT_BUS_LINE_NODE;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void nextBusLineNodeMsg(String[] params) {
		String json = params[0];
		try {
			new JSONObject(json);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void searchRoutePlan(String[] params) {
		if (params == null || params.length < 1) {
			errorCallback(0, 0, "error params!");
			return;
		}
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_SEARCH_ROUTE_PLAN;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void searchRoutePlanMsg(String[] params) {
		String json = params[0];
		try {
			new JSONObject(json);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void removeRoutePlan(String[] params) {
		if (params == null || params.length < 1) {
			errorCallback(0, 0, "error params!");
			return;
		}
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_REMOVE_ROUTE_PLAN;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void removeRoutePlanMsg(String[] params) {
		String json = params[0];
		try {
			new JSONObject(json);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void preRouteNode(String[] params) {
		if (params == null || params.length < 1) {
			errorCallback(0, 0, "error params!");
			return;
		}
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_PRE_ROUTE_NODE;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void preRouteNodeMsg(String[] params) {
		String json = params[0];
		try {
			new JSONObject(json);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void nextRouteNode(String[] params) {
		if (params == null || params.length < 1) {
			errorCallback(0, 0, "error params!");
			return;
		}
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_NEXT_ROUTE_NODE;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void nextRouteNodeMsg(String[] params) {
		String json = params[0];
		try {
			new JSONObject(json);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void geocode(String[] params) {
		if (params == null || params.length < 1) {
			errorCallback(0, 0, "error params!");
			return;
		}
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_GEOCODE;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void geocodeMsg(String[] params) {
		String json = params[0];
		GeocodeQuery query = null;
		try {
			JSONObject jsonObject = new JSONObject(json);
			String address = jsonObject.getString(JsConst.ADDRESS);
			// String address = "'武汉市'|'光谷'";
			String city = null;
			if (jsonObject.has(JsConst.CITY)) {
				city = jsonObject.getString(JsConst.CITY);
			}
			query = new GeocodeQuery(address, city);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (query != null && getAMapActivity() != null) {
			getAMapActivity().geocode(query);
		}
	}

	public void reverseGeocode(String[] params) {
		if (params == null || params.length < 1) {
			errorCallback(0, 0, "error params!");
			return;
		}
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_REVERSE_GEOCODE;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void reverseGeocodeMsg(String[] params) {
		String json = params[0];
		RegeocodeQuery query = null;
		try {
			JSONObject jsonObject = new JSONObject(json);
			double longitude = Double.valueOf(jsonObject.getString(JsConst.LONGITUDE));
			double latitude = Double.valueOf(jsonObject.getString(JsConst.LATITUDE));
			LatLonPoint point = new LatLonPoint(latitude, longitude);
			float radius = 1000f;
			if (jsonObject.has(JsConst.RADIUS)) {
				radius = Float.valueOf(jsonObject.getString(JsConst.RADIUS));
			}
			int type = 0;
			if (jsonObject.has(JsConst.TYPE)) {
				type = Integer.valueOf(jsonObject.getString(JsConst.TYPE));
			}
			String latLonType = GeocodeSearch.AMAP;
			if (type != 0) {
				latLonType = GeocodeSearch.GPS;
			}
			query = new RegeocodeQuery(point, radius, latLonType);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (query != null && getAMapActivity() != null) {
			getAMapActivity().reGeocode(query);
		}
	}

	public void getCurrentLocation(String[] params) {
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_GET_CURRENT_LOCATION;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void getCurrentLocationMsg() {
		if (getAMapActivity() != null) {
			getAMapActivity().getCurrentLocation();
		}
	}

	public void startLocation(String[] params) {
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_START_LOCATION;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void startLocationMsg(String[] params) {
		long minTime = 2000;
		float minDistance = 10;
		if (params != null && params.length > 0) {
			try {
				JSONObject jsonObject = new JSONObject(params[0]);
				if (jsonObject.has(JsConst.MINTIME)) {
					minTime = Long.valueOf(jsonObject.getString(JsConst.MINTIME));
				}
				if (jsonObject.has(JsConst.MINDISTANCE)) {
					minDistance = Float.valueOf(jsonObject.getString(JsConst.MINDISTANCE));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		if (getAMapActivity() != null) {
			getAMapActivity().startLocation(minTime, minDistance);
		}
	}

	public void stopLocation(String[] params) {
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_STOP_LOCATION;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void stopLocationMsg() {
		if (getAMapActivity() != null) {
			getAMapActivity().stopLocation();
		}
	}

	public void setMyLocationEnable(String[] params) {
		if (params == null || params.length < 1) {
			errorCallback(0, 0, "error params!");
			return;
		}
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_SET_MY_LOCATION_ENABLE;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void setMyLocationEnableMsg(String[] params) {
		String json = params[0];
		int type = -1;
		try {
			JSONObject jsonObject = new JSONObject(json);
			type = Integer.valueOf(jsonObject.getString(JsConst.TYPE));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (type > -1 && getAMapActivity() != null) {
			getAMapActivity().setMyLocationEnable(type);
		}
	}

	public void setUserTrackingMode(String[] params) {
		if (params == null || params.length < 1) {
			errorCallback(0, 0, "error params!");
			return;
		}
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_SET_USER_TRACKING_MODE;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void setUserTrackingModeMsg(String[] params) {
		String json = params[0];
		int type = 1;
		try {
			JSONObject jsonObject = new JSONObject(json);
			String data = jsonObject.getString(JsConst.TYPE);
			type = Integer.valueOf(data);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (getAMapActivity() != null) {
			getAMapActivity().setMyLocationType(type);
		}
	}

	public void removeOverlay(String[] params) {
		if (params == null || params.length < 1) {
			errorCallback(0, 0, "error params!");
			return;
		}
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_REMOVE_OVERLAY;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void removeOverlayMsg(String[] params) {
		String json = params[0];
		String id = null;
		try {
			JSONObject jsonObject = new JSONObject(json);
			id = jsonObject.getString(JsConst.ID);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (!TextUtils.isEmpty(id) && getAMapActivity() != null) {
			getAMapActivity().removeOverlay(id);
		}
	}

	public void poiSearchDetail(String[] params) {
		if (params == null || params.length < 1) {
			errorCallback(0, 0, "error params!");
			return;
		}
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_POI_SEARCH_DETAIL;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void poiSearchDetailMsg(String[] params) {
		String json = params[0];
		PoiItemVO poiItemVO = DataHelper.gson.fromJson(json, new TypeToken<PoiItemVO>() {
		}.getType());
		String id = poiItemVO.getId();
		if (!TextUtils.isEmpty(id) && getAMapActivity() != null) {
			getAMapActivity().poiSearchDetail(id);
		}
	}

	public void setScaleVisible(String[] params) {
		if (params == null || params.length < 1) {
			errorCallback(0, 0, "error params!");
			return;
		}
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_SET_SCALE_VISIBLE;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void setScaleVisibleMsg(String[] params) {
		String json = params[0];
		VisibleVO vo = DataHelper.gson.fromJson(json, VisibleVO.class);
		if (getAMapActivity() != null) {
			getAMapActivity().setScaleVisible(vo);
		}
	}

	public void setMyLocationButtonVisible(String[] params) {
		if (params == null || params.length < 1) {
			errorCallback(0, 0, "error params!");
			return;
		}
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_SET_MY_LOCATION_BUTTON_VISIBLE;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void setMyLocationButtonVisibleMsg(String[] params) {
		String json = params[0];
		VisibleVO vo = DataHelper.gson.fromJson(json, VisibleVO.class);
		if (getAMapActivity() != null) {
			getAMapActivity().setMyLocationButtonVisible(vo);
		}
	}

	public void setZoomVisible(String[] params) {
		if (params == null || params.length < 1) {
			errorCallback(0, 0, "error params!");
			return;
		}
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_SET_ZOOM_VISIBLE;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void setZoomVisibleMsg(String[] params) {
		String json = params[0];
		VisibleVO vo = DataHelper.gson.fromJson(json, VisibleVO.class);
		if (getAMapActivity() != null) {
			getAMapActivity().setZoomVisible(vo);
		}
	}

	public void removeMarkersOverlays(String[] params) {
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_REMOVE_MARKERS_OVERLAYS;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void removeMarkersOverlaysMsg(String[] params) {
		List<String> list = null;
		if (params != null && params.length > 0) {
			String json = params[0];
			list = DataHelper.gson.fromJson(json, new TypeToken<List<String>>() {
			}.getType());
		}
		if (getAMapActivity() != null) {
			getAMapActivity().removeMarkersOverlays(list);
		}
	}

	public void removeOverlays(String[] params) {
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_REMOVE_OVERLAYS;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void removeOverlaysMsg(String[] params) {
		List<String> list = null;
		if (params != null && params.length > 0) {
			String json = params[0];
			list = DataHelper.gson.fromJson(json, new TypeToken<List<String>>() {
			}.getType());
		}
		if (getAMapActivity() != null) {
			getAMapActivity().removeOverlays(list);
		}
	}

	public void setOverlayVisibleBounds(String[] params) {
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_SET_OVERLAY_VISIBLE_BOUNDS;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void setOverlayVisibleBoundsMsg(String[] params) {
		VisibleBoundsVO data = new VisibleBoundsVO();
		if (params != null && params.length > 0) {
			data = DataHelper.gson.fromJson(params[0], VisibleBoundsVO.class);
		}
		if (getAMapActivity() != null) {
			getAMapActivity().setOverlayVisibleBounds(data);
		}
	}

	public void setMarkerVisibleBounds(String[] params) {
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_SET_MARKER_VISIBLE_BOUNDS;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void setMarkerVisibleBoundsMsg(String[] params) {
		VisibleBoundsVO data = new VisibleBoundsVO();
		if (params != null && params.length > 0) {
			data = DataHelper.gson.fromJson(params[0], VisibleBoundsVO.class);
		}
		if (getAMapActivity() != null) {
			getAMapActivity().setMarkerVisibleBounds(data);
		}
	}

	public void clear(String[] params) {
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_CLEAR;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void clearMsg() {
		if (getAMapActivity() != null) {
			getAMapActivity().clear();
		}
	}

	public void download(String[] params) {
		if (params == null || params.length < 1) {
			errorCallback(0, 0, "error params!");
			return;
		}
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_DOWNLOAD;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void downloadMsg(String[] params) {
		String json = params[0];
		List<DownloadItemVO> list = new ArrayList<DownloadItemVO>();
		try {
			JSONArray jsonArray = new JSONArray(json);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				DownloadItemVO data = new DownloadItemVO();
				String name = null;
				if (jsonObject.has(JsConst.TAG_CITY)) {
					if (!TextUtils.isEmpty(jsonObject.getString(JsConst.TAG_CITY))) {
						name = jsonObject.getString(JsConst.TAG_CITY);
						data.setType(JsConst.TYPE_CITY);
					}
				}
				if (TextUtils.isEmpty(name) && jsonObject.has(JsConst.TAG_PROVINCE)) {
					if (!TextUtils.isEmpty(jsonObject.getString(JsConst.TAG_PROVINCE))) {
						name = jsonObject.getString(JsConst.TAG_PROVINCE);
						data.setType(JsConst.TYPE_PROVINCE);
					}
				}
				if (!TextUtils.isEmpty(name)) {
					data.setName(name);
					data.setStatus(OfflineMapStatus.LOADING);
					list.add(data);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			errorCallback(0, 0, "error params!");
		}
		for (int i = 0; i < list.size(); i++) {
			GaodeMapOfflineManager.getInstance(mContext, this).download(list.get(i));
		}
	}

	public void pause(String[] params) {
		if (params == null || params.length < 1) {
			errorCallback(0, 0, "error params!");
			return;
		}
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_PAUSE;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void pauseMsg(String[] params) {
		List<String> list = null;
		if (params != null && params.length > 0) {
			String json = params[0];
			list = DataHelper.gson.fromJson(json, new TypeToken<List<String>>() {
			}.getType());
		}
		GaodeMapOfflineManager.getInstance(mContext, this).pause(list);
	}

	public void getAvailableCityList(String[] params) {
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_GET_AVAILABLE_CITY_LIST;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void getAvailableCityListMsg() {
		GaodeMapOfflineManager.getInstance(mContext, this).getAvailableCityList();
	}

	public void isUpdate(String[] params) {
		if (params == null || params.length < 1) {
			errorCallback(0, 0, "error params!");
			return;
		}
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_IS_UPDATE;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void isUpdateMsg(String[] params) {
		String json = params[0];
		try {
			JSONObject jsonObject = new JSONObject(json);
			DownloadItemVO data = new DownloadItemVO();
			String name = null;
			if (jsonObject.has(JsConst.TAG_CITY)) {
				if (!TextUtils.isEmpty(jsonObject.getString(JsConst.TAG_CITY))) {
					name = jsonObject.getString(JsConst.TAG_CITY);
					data.setType(JsConst.TYPE_CITY);
				}
			}
			if (TextUtils.isEmpty(name) && jsonObject.has(JsConst.TAG_PROVINCE)) {
				if (!TextUtils.isEmpty(jsonObject.getString(JsConst.TAG_PROVINCE))) {
					name = jsonObject.getString(JsConst.TAG_PROVINCE);
					data.setType(JsConst.TYPE_PROVINCE);
				}
			}
			if (!TextUtils.isEmpty(name)) {
				data.setName(name);
				data.setStatus(OfflineMapStatus.CHECKUPDATES);
				GaodeMapOfflineManager.getInstance(mContext, this).isUpdate(data);
			} else {
				errorCallback(0, 0, "error params!");
			}
		} catch (JSONException e) {
			e.printStackTrace();
			errorCallback(0, 0, "error params!");
		}
	}

	public void delete(String[] params) {
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_DELETE;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void deleteMsg(String[] params) {
		List<String> list = null;
		if (params != null && params.length > 0) {
			String json = params[0];
			list = DataHelper.gson.fromJson(json, new TypeToken<List<String>>() {
			}.getType());
		}
		GaodeMapOfflineManager.getInstance(mContext, this).deleteList(list);
	}

	public void restart(String[] params) {
		if (params == null || params.length < 1) {
			errorCallback(0, 0, "error params!");
			return;
		}
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_RESTART;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void restartMsg(String[] params) {
		List<String> list = null;
		if (params != null && params.length > 0) {
			String json = params[0];
			list = DataHelper.gson.fromJson(json, new TypeToken<List<String>>() {
			}.getType());
		}
		GaodeMapOfflineManager.getInstance(mContext, this).restart(list);
	}

	public void stopDownload(String[] params) {
		if (params == null || params.length < 1) {
			errorCallback(0, 0, "error params!");
			return;
		}
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_STOP_DOWNLOAD;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void stopDownloadMsg(String[] params) {
		String json = params[0];
		DownloadItemVO data = DataHelper.gson.fromJson(json, DownloadItemVO.class);
		if (data != null && !TextUtils.isEmpty(data.getName())) {
			GaodeMapOfflineManager.getInstance(mContext, this).stop(data.getName());
		} else {
			errorCallback(0, 0, "error params!");
		}
		// callBackPluginJs(JsConst.CALLBACK_STOP_DOWNLOAD,
		// jsonResult.toString());
	}

	public void getAvailableProvinceList(String[] params) {
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_GET_AVAILABLE_PROVINCE_LIST;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void getAvailableProvinceListMsg() {
		GaodeMapOfflineManager.getInstance(mContext, this).getAvailableProvinceList();
	}

	public void getDownloadList(String[] params) {
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_GET_DOWNLOAD_LIST;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void getDownloadListMsg() {
		GaodeMapOfflineManager.getInstance(mContext, this).getDownloadList();
	}

	public void getDownloadingList(String[] params) {
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_GET_DOWNLOADING_LIST;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void getDownloadingListMsg() {
		GaodeMapOfflineManager.getInstance(mContext, this).getDownloadingList();
	}

	public void setCustomButton(String[] params) {
		if (params == null || params.length < 1) {
			errorCallback(0, 0, "error params!");
			return;
		}
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_SET_CUSTOM_BUTTON;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void setCustomButtonMsg(String[] params) {
		String json = params[0];
		CustomButtonVO dataVo = DataHelper.gson.fromJson(json, CustomButtonVO.class);
		if (!TextUtils.isEmpty(dataVo.getBgImage())) {
			String path = dataVo.getBgImage();
			String desPath = BUtility.makeRealPath(BUtility.makeUrl(mBrwView.getCurrentUrl(), path), mBrwView.getCurrentWidget().m_widgetPath, mBrwView.getCurrentWidget().m_wgtType);
			dataVo.setBgImage(desPath);
		}
		if (dataVo != null && getAMapActivity() != null) {
			getAMapActivity().setCustomButton(dataVo);
		}
	}

	public void deleteCustomButton(String[] params) {
		if (params == null || params.length < 1) {
			errorCallback(0, 0, "error params!");
			return;
		}
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_REMOVE_CUSTOM_BUTTON;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void deleteCustomButtonMsg(String[] params) {
		String id = params[0];
		if (!TextUtils.isEmpty(id) && getAMapActivity() != null) {
			getAMapActivity().deleteCustomButton(id);
		}
	}

	public void showCustomButtons(String[] params) {
		if (params == null || params.length < 1) {
			errorCallback(0, 0, "error params!");
			return;
		}
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_SHOW_CUSTOM_BUTTONS;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void showCustomButtonsMsg(String[] params) {
		String json = params[0];
		List<String> ids = DataHelper.gson.fromJson(json, new TypeToken<List<String>>() {
		}.getType());
		if (ids != null && ids.size() > 0 && getAMapActivity() != null) {
			getAMapActivity().showCustomButtons(ids, this);
		}
	}

	public void hideCustomButtons(String[] params) {
		Message msg = new Message();
		msg.obj = this;
		msg.what = MSG_HIDE_CUSTOM_BUTTONS;
		Bundle bd = new Bundle();
		bd.putStringArray(BUNDLE_DATA, params);
		msg.setData(bd);
		mHandler.sendMessage(msg);
	}

	private void hideCustomButtonsMsg(String[] params) {
		List<String> ids = null;
		if (params.length > 0) {
			String json = params[0];
			ids = DataHelper.gson.fromJson(json, new TypeToken<List<String>>() {
			}.getType());
		}
		if (getAMapActivity() != null) {
			getAMapActivity().hideCustomButtons(ids);
		}
	}

	@Override
	public void onHandleMessage(Message message) {
		if (message == null) {
			return;
		}
		Bundle bundle = message.getData();
		switch (message.what) {

		case MSG_OPEN:
			openMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_CLOSE:
			closeMsg();
			break;
		case MSG_HIDE_MAP:
			hideMapMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_SHOW_MAP:
			showMapMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_SET_MAP_TYPE:
			setMapTypeMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_SET_TRAFFIC_ENABLED:
			setTrafficEnabledMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_SET_CENTER:
			setCenterMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_GET_CENTER:
			getCenterMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_SET_ZOOM_LEVEL:
			setZoomLevelMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_ZOOM_IN:
			zoomInMsg();
			break;
		case MSG_ZOOM_OUT:
			zoomOutMsg();
			break;
		case MSG_ROTATE:
			rotateMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_OVERLOOK:
			overlookMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_SET_ZOOM_ENABLE:
			setZoomEnableMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_SET_ROTATE_ENABLE:
			setRotateEnableMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_SET_COMPASS_ENABLE:
			setCompassEnableMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_SET_SCROLL_ENABLE:
			setScrollEnableMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_SET_OVERLOOK_ENABLE:
			setOverlookEnableMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_ADD_MARKERS_OVERLAY:
			addMarkersOverlayMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_SET_MARKER_OVERLAY:
			setMarkerOverlayMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_SHOW_BUBBLE:
			showBubbleMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_HIDE_BUBBLE:
			hideBubbleMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_ADD_DOT_OVERLAY:
			addDotOverlayMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_ADD_POLYLINE_OVERLAY:
			addPolylineOverlayMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_ADD_ARC_OVERLAY:
			addArcOverlayMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_ADD_CIRCLE_OVERLAY:
			addCircleOverlayMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_ADD_POLYGON_OVERLAY:
			addPolygonOverlayMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_ADD_GROUND_OVERLAY:
			addGroundOverlayMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_ADD_TEXT_OVERLAY:
			addTextOverlayMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_REMOVE_MAKERS_OVERLAY:
			removeMarkersOverlayMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_POI_SEARCH_IN_CITY:
			poiSearchMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_POI_NEARBY_SEARCH:
			poiNearbySearchMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_POI_BOUND_SEARCH:
			poiBoundSearchMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_BUS_LINE_SEARCH:
			busLineSearchMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_REMOVE_BUS_LINE:
			removeBusLineMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_PRE_BUS_LINE_NODE:
			preBusLineNodeMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_NEXT_BUS_LINE_NODE:
			nextBusLineNodeMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_SEARCH_ROUTE_PLAN:
			searchRoutePlanMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_REMOVE_ROUTE_PLAN:
			removeRoutePlanMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_PRE_ROUTE_NODE:
			preRouteNodeMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_NEXT_ROUTE_NODE:
			nextRouteNodeMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_GEOCODE:
			geocodeMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_REVERSE_GEOCODE:
			reverseGeocodeMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_GET_CURRENT_LOCATION:
			getCurrentLocationMsg();
			break;
		case MSG_START_LOCATION:
			startLocationMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_STOP_LOCATION:
			stopLocationMsg();
			break;
		case MSG_SET_MY_LOCATION_ENABLE:
			setMyLocationEnableMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_SET_USER_TRACKING_MODE:
			setUserTrackingModeMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_REMOVE_OVERLAY:
			removeOverlayMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_POI_SEARCH_DETAIL:
			poiSearchDetailMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_SET_SCALE_VISIBLE:
			setScaleVisibleMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_SET_MY_LOCATION_BUTTON_VISIBLE:
			setMyLocationButtonVisibleMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_SET_ZOOM_VISIBLE:
			setZoomVisibleMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_REMOVE_MARKERS_OVERLAYS:
			removeMarkersOverlaysMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_REMOVE_OVERLAYS:
			removeOverlaysMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_SET_OVERLAY_VISIBLE_BOUNDS:
			setOverlayVisibleBoundsMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_SET_MARKER_VISIBLE_BOUNDS:
			setMarkerVisibleBoundsMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_CLEAR:
			clearMsg();
			break;
		case MSG_DOWNLOAD:
			downloadMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_PAUSE:
			pauseMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_GET_AVAILABLE_CITY_LIST:
			getAvailableCityListMsg();
			break;
		case MSG_IS_UPDATE:
			isUpdateMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_DELETE:
			deleteMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_RESTART:
			restartMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_STOP_DOWNLOAD:
			stopDownloadMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_GET_AVAILABLE_PROVINCE_LIST:
			getAvailableProvinceListMsg();
			break;
		case MSG_GET_DOWNLOAD_LIST:
			getDownloadListMsg();
			break;
		case MSG_GET_DOWNLOADING_LIST:
			getDownloadingListMsg();
			break;
		case MSG_SET_CUSTOM_BUTTON:
			setCustomButtonMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_REMOVE_CUSTOM_BUTTON:
			deleteCustomButtonMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_SHOW_CUSTOM_BUTTONS:
			showCustomButtonsMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		case MSG_HIDE_CUSTOM_BUTTONS:
			hideCustomButtonsMsg(bundle.getStringArray(BUNDLE_DATA));
			break;
		default:
			super.onHandleMessage(message);
		}
	}

	private void callBackPluginJs(String methodName, String jsonData) {
		String js = SCRIPT_HEADER + "if(" + methodName + "){" + methodName + "('" + jsonData + "');}";
		Log.i(TAG, "callBackPluginJs->js = " + js);
		onCallback(js);
	}

	private void callBackPluginJs(EUExGaodeMap brw, String methodName, String jsonData) {
		String js = SCRIPT_HEADER + "if(" + methodName + "){" + methodName + "('" + jsonData + "');}";
		Log.i(TAG, "callBackPluginJs->js = " + js);
		brw.onCallback(js);
	}

	@SuppressWarnings("unused")
	private void addView2CurrentWindow(View child, RelativeLayout.LayoutParams parms) {
		int l = parms.leftMargin;
		int t = parms.topMargin;
		int w = parms.width;
		int h = parms.height;
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(w, h);
		lp.gravity = Gravity.NO_GRAVITY;
		lp.leftMargin = l;
		lp.topMargin = t;
		adptLayoutParams(parms, lp);
		mBrwView.addViewToCurrentWindow(child, lp);
	}

	@Override
	public void onMapLoaded() {
		callBackPluginJs(JsConst.ON_MAP_LOADED_LISTENER, "");
	}

	@Override
	public void onMarkerClicked(String id) {
		JSONObject json = new JSONObject();
		try {
			json.put(JsConst.ID, id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		callBackPluginJs(JsConst.ON_MARKER_CLICK_LISTENER, json.toString());
	}

	@Override
	public void onBubbleClicked(String id) {
		JSONObject json = new JSONObject();
		try {
			json.put(JsConst.ID, id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		callBackPluginJs(JsConst.ON_MARKER_BUBBLE_CLICK_LISTENER, json.toString());
	}

	@Override
	public void onReceiveLocation(AMapLocation location) {
		JSONObject json = new JSONObject();
		try {
			json.put(JsConst.LONGITUDE, location.getLongitude());
			json.put(JsConst.LATITUDE, location.getLatitude());
			json.put(JsConst.TIMESTAMP, location.getTime());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		callBackPluginJs(JsConst.ON_RECEIVE_LOCATION, json.toString());
	}

	@Override
	public void onMapClick(LatLng point) {
		callBackPluginJs(JsConst.ON_MAP_CLICK_LISTENER, DataHelper.gson.toJson(point));
	}

	@Override
	public void onMapLongClick(LatLng point) {
		callBackPluginJs(JsConst.ON_MAP_LONG_CLICK_LISTENER, DataHelper.gson.toJson(point));
	}

	@Override
	public void cbGetCurrentLocation(AMapLocation location) {
		JSONObject json = new JSONObject();
		try {
			json.put(JsConst.LONGITUDE, location.getLongitude());
			json.put(JsConst.LATITUDE, location.getLatitude());
			json.put(JsConst.TIMESTAMP, location.getTime());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		callBackPluginJs(JsConst.CALLBACK_GET_CURRENT_LOCATION, json.toString());
	}

	@Override
	public void cbGeocode(GeocodeResult result, int errorCode) {
		JSONObject jsonResult = new JSONObject();
		try {
			jsonResult.put(JsConst.ERRORCODE, errorCode);
			if (result != null && result.getGeocodeAddressList() != null && result.getGeocodeAddressList().size() > 0) {
				GeocodeAddress address = result.getGeocodeAddressList().get(0);
				if (address != null && address.getLatLonPoint() != null) {
					jsonResult.put(JsConst.LONGITUDE, address.getLatLonPoint().getLongitude());
					jsonResult.put(JsConst.LATITUDE, address.getLatLonPoint().getLatitude());
					jsonResult.put(JsConst.ADDRESS, address.getFormatAddress());
					jsonResult.put(JsConst.CITY, address.getCity());
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		callBackPluginJs(JsConst.CALLBACK_GEOCODE, jsonResult.toString());
	}

	@Override
	public void cbReverseGeocode(RegeocodeResult result, int errorCode) {
		JSONObject jsonResult = new JSONObject();
		try {
			jsonResult.put(JsConst.ERRORCODE, errorCode);
			if (result != null && result.getRegeocodeAddress() != null) {
				jsonResult.put(JsConst.ADDRESS, result.getRegeocodeAddress().getFormatAddress());
				jsonResult.put(JsConst.LATITUDE, result.getRegeocodeQuery().getPoint().getLatitude());
				jsonResult.put(JsConst.LONGITUDE, result.getRegeocodeQuery().getPoint().getLongitude());
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		callBackPluginJs(JsConst.CALLBACK_REVERSE_GEOCODE, jsonResult.toString());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void cbPoiSearch(PoiResult result, int errorCode) {
		ResultVO resultVO = new ResultVO<PoiItemVO>();
		resultVO.setErrorCode(errorCode);
		if (result != null && result.getPois() != null && result.getPois().size() > 0) {
			List<PoiItem> items = result.getPois();
			List<PoiItemVO> list = new ArrayList<PoiItemVO>();
			for (int i = 0; i < items.size(); i++) {
				list.add(new PoiItemVO(items.get(i)));
			}
			resultVO.setData(list);
		}
		callBackPluginJs(JsConst.CALLBACK_POI_SEARCH, DataHelper.gson.toJson(resultVO));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void cbPoiSearchDetail(PoiItemDetail result, int errorCode) {
		ResultVO resultVO = null;
		PoiItemDetail.DeepType type = result.getDeepType();
		if (type != null) {
			switch (type) {
			case DINING:// 餐饮深度信息类型；
				resultVO = new ResultVO<Dining>();
				resultVO.setData(result.getDining());
				break;
			case HOTEL:// 酒店深度信息类型；
				resultVO = new ResultVO<Hotel>();
				resultVO.setData(result.getHotel());
				break;
			case CINEMA:// 影院深度信息类型；
				resultVO = new ResultVO<Cinema>();
				resultVO.setData(result.getCinema());
				break;
			case SCENIC:// 景点深度信息类型。
				resultVO = new ResultVO<Scenic>();
				resultVO.setData(result.getScenic());
				break;
			case UNKNOWN:// 未知深度信息类型；
				break;
			}
			if (resultVO != null) {
				resultVO.setType(type + "");
			}
		} else if ((result.getGroupbuys() != null && result.getGroupbuys().size() > 0) || (result.getDiscounts() != null && result.getDiscounts().size() > 0)) {
			resultVO = new ResultVO();
			if (result.getDiscounts() != null && result.getDiscounts().size() > 0) {
				resultVO.setDiscount(result.getDiscounts());
			}
			if (result.getGroupbuys() != null && result.getGroupbuys().size() > 0) {
				resultVO.setGroupbuy(result.getGroupbuys());
			}
		} else {
			resultVO = new ResultVO<PoiItemDetail>();
			resultVO.setData(result);
		}
		if (resultVO != null) {
			resultVO.setErrorCode(errorCode);
			callBackPluginJs(JsConst.CALLBACK_POI_SEARCH_DETAIL, DataHelper.gson.toJson(resultVO));
		}
	}

	@Override
	public void cbDownload(DownloadResultVO data) {
		String jsonResult = DataHelper.gson.toJson(data);
		callBackPluginJs(JsConst.CALLBACK_DOWNLOAD, jsonResult);
	}

	@Override
	public void onDownload(DownloadStatusVO data) {
		String jsonResult = DataHelper.gson.toJson(data);
		callBackPluginJs(JsConst.ON_DOWNLOAD, jsonResult);
	}

	@Override
	public void cbDelete(DownloadResultVO data) {
		String jsonResult = DataHelper.gson.toJson(data);
		callBackPluginJs(JsConst.CALLBACK_DELETE, jsonResult);
	}

	@Override
	public void cbGetDownloadingList(List<DownloadItemVO> data) {
		String jsonResult = DataHelper.gson.toJson(data);
		callBackPluginJs(JsConst.CALLBACK_GET_DOWNLOADING_LIST, jsonResult);
	}

	@Override
	public void cbGetDownloadList(List<DownloadItemVO> data) {
		String jsonResult = DataHelper.gson.toJson(data);
		callBackPluginJs(JsConst.CALLBACK_GET_DOWNLOAD_LIST, jsonResult);
	}

	@Override
	public void cbGetAvailableCityList(List<AvailableCityVO> data) {
		String jsonResult = DataHelper.gson.toJson(data);
		callBackPluginJs(JsConst.CALLBACK_GET_AVAILABLE_CITY_LIST, jsonResult);
	}

	@Override
	public void cbGetAvailableProvinceList(List<AvailableProvinceVO> data) {
		String jsonResult = DataHelper.gson.toJson(data);
		callBackPluginJs(JsConst.CALLBACK_GET_AVAILABLE_PROVINCE_LIST, jsonResult);
	}

	@Override
	public void cbIsUpdate(UpdateResultVO data) {
		String jsonResult = DataHelper.gson.toJson(data);
		callBackPluginJs(JsConst.CALLBACK_IS_UPDATE, jsonResult);
	}

	@Override
	public void cbSetCustomButton(CustomButtonResultVO data) {
		String json = DataHelper.gson.toJson(data);
		callBackPluginJs(JsConst.CALLBACK_SET_CUSTOM_BUTTON, json);
	}

	@Override
	public void cbRemoveCustomButton(CustomButtonResultVO data) {
		String json = DataHelper.gson.toJson(data);
		callBackPluginJs(JsConst.CALLBACK_REMOVE_CUSTOM_BUTTON, json);
	}

	@Override
	public void cbShowCustomButtons(CustomButtonDisplayResultVO data) {
		String json = DataHelper.gson.toJson(data);
		callBackPluginJs(JsConst.CALLBACK_SHOW_CUSTOM_BUTTONS, json);
	}

	@Override
	public void cbHideCustomButtons(CustomButtonDisplayResultVO data) {
		String json = DataHelper.gson.toJson(data);
		callBackPluginJs(JsConst.CALLBACK_HIDE_CUSTOM_BUTTONS, json);
	}

	@Override
	public void onButtonClick(String id, EUExGaodeMap gaodeMap) {
		callBackPluginJs(gaodeMap, JsConst.ON_BUTTON_CLICK, id);
	}

	@Override
	public void cbGetCenter(LatLng latLng) {

		double latitude = latLng.latitude;
		double longitude = latLng.longitude;

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("latitude", latitude);
			jsonObject.put("longitude", longitude);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		callBackPluginJs(JsConst.CB_GET_CENTER, jsonObject.toString());
	}

	private AMapBasicFragment getAMapActivity() {
		return basicFragment;
	}

	private void removeAMapView(AMapBasicFragment fragment) {
		if (isScrollWithWeb) {
			removeFragmentFromWebView(TAG);
		} else {
			removeFragmentFromWindow(fragment);
		}
	}

	@SuppressWarnings("unused")
	private String getActivityTag() {
		return AMapBasicFragment.TAG;
	}

}
