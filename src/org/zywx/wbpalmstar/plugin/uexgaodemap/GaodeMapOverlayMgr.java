package org.zywx.wbpalmstar.plugin.uexgaodemap;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Arc;
import com.amap.api.maps.model.ArcOptions;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.GroundOverlay;
import com.amap.api.maps.model.GroundOverlayOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Polygon;
import com.amap.api.maps.model.PolygonOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import org.zywx.wbpalmstar.base.cache.ImageLoadTask;
import org.zywx.wbpalmstar.base.cache.ImageLoadTask$ImageLoadTaskCallback;
import org.zywx.wbpalmstar.base.cache.ImageLoaderManager;
import org.zywx.wbpalmstar.plugin.uexgaodemap.bean.ArcBean;
import org.zywx.wbpalmstar.plugin.uexgaodemap.bean.CircleBean;
import org.zywx.wbpalmstar.plugin.uexgaodemap.bean.GroundBean;
import org.zywx.wbpalmstar.plugin.uexgaodemap.bean.PolygonBean;
import org.zywx.wbpalmstar.plugin.uexgaodemap.bean.PolylineBean;
import org.zywx.wbpalmstar.plugin.uexgaodemap.overlay.ArcOverlay;
import org.zywx.wbpalmstar.plugin.uexgaodemap.overlay.BaseOverlay;
import org.zywx.wbpalmstar.plugin.uexgaodemap.overlay.CircleOverlay;
import org.zywx.wbpalmstar.plugin.uexgaodemap.overlay.GroundNOverlay;
import org.zywx.wbpalmstar.plugin.uexgaodemap.overlay.PolygonOverlay;
import org.zywx.wbpalmstar.plugin.uexgaodemap.overlay.PolylineOverlay;
import org.zywx.wbpalmstar.plugin.uexgaodemap.util.GaodeUtils;
import org.zywx.wbpalmstar.plugin.uexgaodemap.util.OnCallBackListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class GaodeMapOverlayMgr extends GaodeMapBaseMgr {

	private HashMap<String, BaseOverlay> mOverlays = new HashMap<String, BaseOverlay>();
	private ImageLoaderManager manager;
	private List<LatLng> mBoundsOverlays;

	public GaodeMapOverlayMgr(Context mContext, AMap map, OnCallBackListener mListener, List<LatLng> overlays) {
		super(mContext, map, mListener);
		manager = ImageLoaderManager.initImageLoaderManager(mContext);
		this.mBoundsOverlays = overlays;
	}

	public void addArc(ArcBean bean) {
		if (bean == null || mOverlays.containsKey(bean.getId()))
			return;
		ArcOverlay arcOverlay = new ArcOverlay();
		ArcOptions options = bean.getData();
		if (options != null) {
			Arc arc = map.addArc(options);
			if (arc != null) {
				arcOverlay.setArc(arc);
				mOverlays.put(bean.getId(), arcOverlay);
				if (mBoundsOverlays != null) {
					LatLng start = options.getStart();
					LatLng center = options.getPassed();
					LatLng end = options.getEnd();
					addToBoundsList(start);
					addToBoundsList(center);
					addToBoundsList(end);
				}
			}
		}
	}

	private void addToBoundsList(LatLng latLng) {
		if (latLng != null && !mBoundsOverlays.contains(latLng)) {
			mBoundsOverlays.add(latLng);
		}
	}

	@SuppressWarnings("unused")
	private void removeFromBoundsList(LatLng latLng) {
		if (latLng != null && mBoundsOverlays.contains(latLng)) {
			mOverlays.remove(latLng);
		}
	}

	public void addPolylines(PolylineBean bean) {
		if (bean == null || mOverlays.containsKey(bean.getId()))
			return;
		PolylineOverlay polylineOverlay = new PolylineOverlay();
		PolylineOptions option = bean.getData();
		if (option != null) {
			Polyline polyline = map.addPolyline(option);
			if (polyline != null) {
				polylineOverlay.setPolyline(polyline);
				mOverlays.put(bean.getId(), polylineOverlay);
			}
		}
	}

	public void removeOverlay(String id) {
		if (TextUtils.isEmpty(id)) {
			removeAllOverlays();
		} else {
			remove(id);
		}
	}

	private void remove(String id) {
		BaseOverlay overlay = mOverlays.get(id);
		if (overlay != null) {
			overlay.clearOverlay();
			removeFromList(id);
		}
	}

	private void removeFromList(String id) {
		Iterator<String> iterator = mOverlays.keySet().iterator();
		while (iterator.hasNext()) {
			final String item = iterator.next();
			if (id.equals(item)) {
				iterator.remove();
			}
		}
	}

	private void removeAllOverlays() {
		List<String> list = new ArrayList<String>();
		Iterator<String> iterator = mOverlays.keySet().iterator();
		while (iterator.hasNext()) {
			String id = iterator.next();
			list.add(id);
		}
		for (int i = 0; i < list.size(); i++) {
			remove(list.get(i));
		}
	}

	public void addCircle(CircleBean bean) {
		if (bean == null) {
			return;
		}
		if (mOverlays.containsKey(bean.getId())) {
			remove(bean.getId());
			mOverlays.remove(bean.getId());
		}
		CircleOverlay circleOverlay = new CircleOverlay();
		CircleOptions option = bean.getData();
		if (option != null) {
			Circle circle = map.addCircle(option);
			if (circle != null) {
				circleOverlay.setCircle(circle);
				mOverlays.put(bean.getId(), circleOverlay);
			}
		}
	}

	public void addPolygon(PolygonBean bean) {
		if (bean == null || mOverlays.containsKey(bean.getId()))
			return;
		PolygonOverlay polygonOverlay = new PolygonOverlay();
		PolygonOptions option = bean.getData();
		if (option != null) {
			Polygon polygon = map.addPolygon(option);
			if (polygon != null) {
				polygonOverlay.setPolygon(polygon);
				mOverlays.put(bean.getId(), polygonOverlay);
			}
		}
	}

	public void addGround(final GroundBean bean) {
		if (bean == null || mOverlays.containsKey(bean.getId()))
			return;
		final GroundNOverlay groundOverlay = new GroundNOverlay();
		final GroundOverlayOptions option = bean.getData();
		if (option != null && !TextUtils.isEmpty(bean.getImageUrl())) {
			manager.asyncLoad(new ImageLoadTask(bean.getImageUrl()) {
				@Override
				protected Bitmap doInBackground() {
					return GaodeUtils.getImage(mContext, filePath);
				}
			}.addCallback(new ImageLoadTask$ImageLoadTaskCallback() {
				public void onImageLoaded(ImageLoadTask task, Bitmap bitmap) {
					if (bitmap != null) {
						BitmapDescriptor bd = BitmapDescriptorFactory.fromBitmap(bitmap);
						option.image(bd);
					}
					GroundOverlay ground = map.addGroundOverlay(option);
					if (ground != null) {
						groundOverlay.setGroud(ground);
						mOverlays.put(bean.getId(), groundOverlay);
					}
				}
			}));
		}
	}

	@SuppressWarnings("rawtypes")
	public void clean() {
		Iterator it = mOverlays.keySet().iterator();
		while (it.hasNext()) {
			final String id = it.next().toString();
			BaseOverlay overlay = mOverlays.get(id);
			if (overlay != null) {
				overlay.clearOverlay();
			}
		}
	}

	public void clearAll() {
		mOverlays.clear();
	}
}
