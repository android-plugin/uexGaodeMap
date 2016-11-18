package org.zywx.wbpalmstar.plugin.uexgaodemap;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;

import org.zywx.wbpalmstar.base.ACEImageLoader;
import org.zywx.wbpalmstar.base.listener.ImageLoaderListener;
import org.zywx.wbpalmstar.engine.EBrowserActivity;
import org.zywx.wbpalmstar.engine.universalex.EUExUtil;
import org.zywx.wbpalmstar.plugin.uexgaodemap.VO.BubbleLayoutBaseVO;
import org.zywx.wbpalmstar.plugin.uexgaodemap.VO.CustomBubbleVO;
import org.zywx.wbpalmstar.plugin.uexgaodemap.bean.InfoWindowMarkerBean;
import org.zywx.wbpalmstar.plugin.uexgaodemap.bean.MarkerBean;
import org.zywx.wbpalmstar.plugin.uexgaodemap.bubblelayout.CustomBubbleMarkerLayout;
import org.zywx.wbpalmstar.plugin.uexgaodemap.util.OnCallBackListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class GaodeMapMarkerMgr extends GaodeMapBaseMgr implements OnMarkerClickListener, OnInfoWindowClickListener, InfoWindowAdapter {

    private static final String TAG = "GaodeMapMarkerMgr";
    private static final int MSG_ADD_MARK = 0;
    private static final String TAG_ID = "id";
    private HashMap<String, Marker> mMarkers = new HashMap<String, Marker>();
    private HashMap<String, InfoWindowAdapter> mLayouts = new
            HashMap<String, InfoWindowAdapter>();
    private List<LatLng> mOverlays;
    private BaseHandler mHandler;


    public GaodeMapMarkerMgr(Context cxt, AMap map,
                             OnCallBackListener listener, List<LatLng> markers) {
        super(cxt, map, listener);
        this.mOverlays = markers;
        this.mHandler = new BaseHandler(Looper.getMainLooper());
    }

    public List<String> addMarkers(List<MarkerBean> list){
        if (list == null || list.size() < 1){
            return null;
        }
        List<String> ids = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            final MarkerBean bean = list.get(i);
            final MarkerOptions option = new MarkerOptions();
            option.position(bean.getPosition());
            if (bean.getCustomBubble() != null){
                CustomBubbleVO data = bean.getCustomBubble();
                switch (data.getType()){
                    case 1:
                    default:
                        BubbleLayoutBaseVO item = data.getData();
                        CustomBubbleMarkerLayout layout = new
                                CustomBubbleMarkerLayout(mContext, item);
                        mLayouts.put(bean.getId(), layout);
                        option.title(item.getTitleContent());
                        break;
                }
            }else{
                if(bean.isHasBubble()){
                    if (!TextUtils.isEmpty(bean.getTitle())){
                        option.title(bean.getTitle());
                    }
                    if (!TextUtils.isEmpty(bean.getSubTitle())){
                        option.snippet(bean.getSubTitle());
                    }
                }
            }
            if (!TextUtils.isEmpty(bean.getIcon())){
//                Bitmap bm = getBitmap(bean.getIcon());
//                BitmapDescriptor bd = BitmapDescriptorFactory.fromBitmap(bm);
//                option.icon(bd);
//                addMark(bean.getId(), option);

                //getBitmap(bean, option);
                AddMarkImageAsyncTask task = new AddMarkImageAsyncTask(bean, option);
                task.execute();
            }else{
                addMark(bean.getId(), option);
            }
            ids.add(bean.getId());
        }
        return ids;
    }

    public void addMultiInfoWindow(List<InfoWindowMarkerBean> list){
        if (list == null || list.size() < 1){
            return;
        }

        for (int i = 0; i < list.size(); i++) {
            final InfoWindowMarkerBean bean = list.get(i);
            MarkerOptions option = new MarkerOptions();
            option.position(bean.getPosition());

            View view = ((EBrowserActivity)mContext).getLayoutInflater().inflate(EUExUtil.getResLayoutID("plugin_uexgaodemap_info_window"), null);
            TextView tvTitle = (TextView) view.findViewById(EUExUtil.getResIdID("plugin_uexgaodemap_info_window_title"));
            TextView tvSubTitle= (TextView) view.findViewById(EUExUtil.getResIdID("plugin_uexgaodemap_info_window_subTitle"));
            tvTitle.setText(bean.getTitle());
            tvTitle.setTextColor(bean.getTitleColor());
            tvTitle.setTextSize(bean.getTitleSize());
            if (!TextUtils.isEmpty(bean.getSubTitle())) {
                tvSubTitle.setText(bean.getSubTitle());
                tvSubTitle.setTextColor(bean.getSubTitleColor());
                tvSubTitle.setTextSize(bean.getSubTitleSize());
                tvSubTitle.setVisibility(View.VISIBLE);
            }

            BitmapDescriptor descriptor = BitmapDescriptorFactory.fromView(view);
            option.icon(descriptor);
            addMark(bean.getId(), option);
        }
    }
    public class BaseHandler extends Handler {

        public BaseHandler(Looper loop) {
            super(loop);
        }

        public void handleMessage(Message msg) {
            if (msg == null) return;
            switch (msg.what){
                case MSG_ADD_MARK:
                    if (msg.getData() != null){
                        String id = msg.getData().getString(TAG_ID);
                        MarkerOptions option = (MarkerOptions) msg.obj;
                        addMark(id, option);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private class AddMarkImageAsyncTask extends AsyncTask<Void, Void, Bitmap>{

        MarkerBean bean;
        MarkerOptions option;

        public AddMarkImageAsyncTask(MarkerBean data, MarkerOptions option) {
            this.bean = data;
            this.option = option;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            return ACEImageLoader.getInstance().getBitmapSync(bean.getIcon());
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                BitmapDescriptor bd = BitmapDescriptorFactory.fromBitmap(bitmap);
                option.icon(bd);
            }
            addMark(bean.getId(), option);
        }
    }


    private void addMark(String id, MarkerOptions option) {
        if (option != null && map != null && !mMarkers.containsKey(id)){
            Marker marker = map.addMarker(option);
            mMarkers.put(id, marker);
            addToBoundsList(marker.getPosition());
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        String id = getMarkerId(marker.getId());
        if (mListener != null){
            mListener.onMarkerClicked(id);
        }
        boolean isShowInfo = marker.isInfoWindowShown();
        if (!isShowInfo){
            map.setInfoWindowAdapter(mLayouts.get(id));
        }
        return false;
    }

    private String getMarkerId(String markerID){
        String id = null;
        Iterator it = mMarkers.keySet().iterator();
        while (it.hasNext()){
            final String key = it.next().toString();
            Marker marker = mMarkers.get(key);
            if (markerID.equals(marker.getId())){
                id = key;
                break;
            }
        }
        return id;
    }

    public void updateMarker(MarkerBean bean) {
        final Marker marker = mMarkers.get(bean.getId());
        if (marker == null) return;
        final boolean isShowInfoWindow = marker.isInfoWindowShown();
        if (!TextUtils.isEmpty(bean.getIcon())){
            ACEImageLoader.getInstance().getBitmap(bean.getIcon(), new ImageLoaderListener() {
                @Override
                public void onLoaded(Bitmap bitmap) {
                    if (bitmap!=null){
                        BitmapDescriptor bd = BitmapDescriptorFactory.fromBitmap(bitmap);
                        marker.setIcon(bd);
                    }
                }
            });
        }
        if (bean.getPosition() != null){
            marker.setPosition(bean.getPosition());
        }
        if(bean.isHasBubble()){
            if (isShowInfoWindow){
                marker.hideInfoWindow();
            }
            if (!TextUtils.isEmpty(bean.getTitle())){
                marker.setTitle(bean.getTitle());
            }
            if (!TextUtils.isEmpty(bean.getSubTitle())){
                marker.setSnippet(bean.getSubTitle());
            }
            if (isShowInfoWindow){
                marker.showInfoWindow();
            }
        }
    }

    public void showBubble(String id){
        Marker marker = mMarkers.get(id);
        if (marker != null){
            marker.showInfoWindow();
        }
    }

    public void hideBubble(){
        Iterator it = mMarkers.keySet().iterator();
        while (it.hasNext()){
            final String key = it.next().toString();
            Marker marker = mMarkers.get(key);
            if (marker != null){
                marker.hideInfoWindow();
            }
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        String id = getMarkerId(marker.getId());
        if (mListener != null){
            mListener.onBubbleClicked(id);
        }
    }

    public void removeMarker(String id) {
        if (TextUtils.isEmpty(id)){
            removeAllMarker();
        }else{
            remove(id);
        }
    }

    private void remove(String id){
        Marker marker = mMarkers.get(id);
        if (marker != null){
            removeFromBoundsList(marker.getPosition());
            marker.remove();
            removeMarkerFromList(id);
        }
    }

    private void addToBoundsList(LatLng latLng){
        if (latLng != null && !mOverlays.contains(latLng)){
            mOverlays.add(latLng);
        }
    }

    private void removeFromBoundsList(LatLng latLng){
        if (latLng != null && mOverlays.contains(latLng)){
            mOverlays.remove(latLng);
        }
    }

    private void removeMarkerFromList(String id) {
        Iterator<String> iterator = mMarkers.keySet().iterator();
        while (iterator.hasNext()){
            final String item = iterator.next();
            if (id.equals(item)){
                iterator.remove();
            }
        }
    }

    private void removeAllMarker(){
        List<String> list = new ArrayList<String>();
        Iterator<String> iterator = mMarkers.keySet().iterator();
        while (iterator.hasNext()){
            String id = iterator.next();
            list.add(id);
        }
        for (int i = 0; i < list.size(); i ++){
            remove(list.get(i));
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    public void clearAll() {
        mMarkers.clear();
    }
}
