package org.zywx.wbpalmstar.plugin.uexgaodemap.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.text.TextUtils;
import android.webkit.URLUtil;

import com.amap.api.maps.model.LatLng;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.zywx.wbpalmstar.base.BUtility;
import org.zywx.wbpalmstar.engine.DataHelper;
import org.zywx.wbpalmstar.engine.EBrowserView;
import org.zywx.wbpalmstar.plugin.uexgaodemap.JsConst;
import org.zywx.wbpalmstar.plugin.uexgaodemap.VO.CustomBubbleVO;
import org.zywx.wbpalmstar.plugin.uexgaodemap.bean.MarkerBean;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class GaodeUtils {
    public static Bitmap downloadImageFromNetwork(String url) {
        InputStream is = null;
        Bitmap bitmap = null;
        try {
            HttpGet httpGet = new HttpGet(url);
            BasicHttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 30000);
            HttpConnectionParams.setSoTimeout(httpParams, 30000);
            HttpResponse httpResponse = new DefaultHttpClient(httpParams)
                    .execute(httpGet);
            int responseCode = httpResponse.getStatusLine().getStatusCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                is = httpResponse.getEntity().getContent();
                byte[] data = transStreamToBytes(is, 4096);
                if (data != null) {
                    bitmap = BitmapFactory
                            .decodeByteArray(data, 0, data.length);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }

    public static Bitmap getImage(Context ctx, String imgUrl) {
        if (imgUrl == null || imgUrl.length() == 0) {
            return null;
        }
        Bitmap bitmap = null;
        InputStream is = null;
        try {
            if (URLUtil.isNetworkUrl(imgUrl)) {
                bitmap = downloadImageFromNetwork(imgUrl);
            } else {
                bitmap = BUtility.getLocalImg(ctx,imgUrl);
            }
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }

    public static byte[] transStreamToBytes(InputStream is, int buffSize) {
        if (is == null) {
            return null;
        }
        if (buffSize <= 0) {
            throw new IllegalArgumentException(
                    "buffSize can not less than zero.....");
        }
        byte[] data = null;
        byte[] buffer = new byte[buffSize];
        int actualSize = 0;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            while ((actualSize = is.read(buffer)) != -1) {
                baos.write(buffer, 0, actualSize);
            }
            data = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    public static List<MarkerBean> getAddMarkersData(EBrowserView ebrw, String json) {
        List<MarkerBean> list = new ArrayList<MarkerBean>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++){
                MarkerBean bean = getMarkerData(ebrw, jsonArray.getString(i));
                if (bean != null && bean.getPosition() != null){
                    list.add(bean);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return list;
    }

    public static MarkerBean getMarkerData(EBrowserView ebrw, String json) {
        MarkerBean bean = new MarkerBean();
        try {
            JSONObject object = new JSONObject(json);
            String id = object.getString(JsConst.ID);
            bean.setId(id);
            if (object.has(JsConst.LONGITUDE) && object.has(JsConst.LATITUDE)){
                float longitude = Float.valueOf(object.getString(JsConst.LONGITUDE));
                float latitude = Float.valueOf(object.getString(JsConst.LATITUDE));
                bean.setPosition(new LatLng(latitude, longitude));
            }
            if (object.has(JsConst.ICON)){
                String icon = object.getString(JsConst.ICON);
                String path = BUtility.makeRealPath(
                        BUtility.makeUrl(ebrw.getCurrentUrl(), icon),
                        ebrw.getCurrentWidget().m_widgetPath,
                        ebrw.getCurrentWidget().m_wgtType);
                bean.setIcon(path);
            }
            if (object.has(JsConst.CUSTOM_BUBBLE)){
                String data = object.getString(JsConst.CUSTOM_BUBBLE);
                CustomBubbleVO dataVO = DataHelper.gson.fromJson(data, CustomBubbleVO.class);
                if (dataVO != null){
                    if (!TextUtils.isEmpty(dataVO.getData().getBgImg())){
                        String path = BUtility.makeRealPath(
                                BUtility.makeUrl(ebrw.getCurrentUrl(), dataVO.getData().getBgImg()),
                                ebrw.getCurrentWidget().m_widgetPath,
                                ebrw.getCurrentWidget().m_wgtType);
                        dataVO.getData().setBgImg(path);
                    }
                    bean.setCustomBubble(dataVO);
                }
            }else{
                if (object.has(JsConst.BUBBLE)){
                    try {
                        JSONObject bubbleObject = object.getJSONObject(JsConst.BUBBLE);
                        String title = bubbleObject.getString(JsConst.TITLE);
                        bean.setTitle(title);
                        if (bubbleObject.has(JsConst.BGIMG)){
                            String bgImg = bubbleObject.getString(JsConst.BGIMG);
                            bean.setBgImg(bgImg);
                        }
                        if (bubbleObject.has(JsConst.SUBTITLE)){
                            String subTitle = bubbleObject.getString(JsConst.SUBTITLE);
                            bean.setSubTitle(subTitle);
                        }
                        bean.setHasBubble(true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        bean.setHasBubble(false);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return bean;
    }

    public static String getCacheDir() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File fExternalStorageDirectory = Environment.getExternalStorageDirectory();
            File dir = new File(fExternalStorageDirectory,
                    "/widgetone/offlineMap/gaodemap");
            boolean result = false;
            if (!dir.exists()){
                result = dir.mkdirs();
            }else {
                result = true;
            }
            if (result) return dir.toString() + File.separator;
            else return null;
        } else {
            return null;
        }
    }
}
