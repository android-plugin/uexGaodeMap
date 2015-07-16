package org.zywx.wbpalmstar.plugin.uexgaodemap.bubblelayout;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.Marker;

import org.zywx.wbpalmstar.engine.universalex.EUExUtil;
import org.zywx.wbpalmstar.plugin.uexgaodemap.VO.BubbleLayoutBaseVO;
import org.zywx.wbpalmstar.plugin.uexgaodemap.util.NinePatchUtils;

public class CustomBubbleMarkerLayout implements AMap.InfoWindowAdapter{
    private BubbleLayoutBaseVO mData;
    private Context mContext;

    public CustomBubbleMarkerLayout(Context context, BubbleLayoutBaseVO data) {
        this.mData = data;
        this.mContext = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View infoWindow = ((Activity)mContext).getLayoutInflater().inflate(
                EUExUtil.getResLayoutID("plugin_uexgaodemap_custom_info_window"), null);
        infoWindow.setLayoutParams(new ViewGroup.LayoutParams(mData.getWidth(),
                ViewGroup.LayoutParams.WRAP_CONTENT));
        //infoWindow.getLayoutParams().width = mData.getWidth();
        render(marker, infoWindow);
        return infoWindow;
    }

    public void render(Marker marker, View view) {
        LinearLayout bg = (LinearLayout) view.findViewById(
                EUExUtil.getResIdID("plugin_uexgaodemap_custom_info_bg"));
        try {
            bg.setBackgroundDrawable(NinePatchUtils.decodeDrawable(mContext, mData.getBgImg()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        String title = mData.getTitleContent();
        TextView titleUi = ((TextView) view.findViewById(
                EUExUtil.getResIdID("plugin_uexgaodemap_title")));
        if (title != null) {
            titleUi.setTextColor(mData.getTitleColor());
            titleUi.setTextSize(mData.getTitleFontSize());
            titleUi.setText(title);
        } else {
            titleUi.setText("");
        }
        String snippet = mData.getTextContent();
        TextView snippetUi = ((TextView) view.findViewById(
                EUExUtil.getResIdID("plugin_uexgaodemap_snippet")));
        if (snippet != null) {
            snippetUi.setTextColor(mData.getTextColor());
            snippetUi.setTextSize(mData.getTextFontSize());
            snippetUi.setText(snippet);
        } else {
            snippetUi.setText("");
        }
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
