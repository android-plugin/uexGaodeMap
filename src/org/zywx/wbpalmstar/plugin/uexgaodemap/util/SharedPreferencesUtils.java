package org.zywx.wbpalmstar.plugin.uexgaodemap.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;

import org.zywx.wbpalmstar.engine.DataHelper;
import org.zywx.wbpalmstar.plugin.uexgaodemap.VO.DownloadItemVO;

import java.util.ArrayList;
import java.util.List;

public class SharedPreferencesUtils {

	private static final String TAG_DATA = "data";
	private static final String TAG_DOWNLOADING_LIST = "gaode_downloading_list";

	public static List<DownloadItemVO> getLocalDownloadingList(Context context) {
		SharedPreferences sp = context.getSharedPreferences(TAG_DATA, Context.MODE_PRIVATE);
		String downloadData = sp.getString(TAG_DOWNLOADING_LIST, "");
		List<DownloadItemVO> list = new ArrayList<DownloadItemVO>();
		if (!TextUtils.isEmpty(downloadData)) {
			list = DataHelper.gson.fromJson(downloadData, new TypeToken<List<DownloadItemVO>>() {
			}.getType());
		}
		return list;
	}

	public static void saveLocalDownloadingList(Context context, List<DownloadItemVO> list) {
		SharedPreferences sp = context.getSharedPreferences(TAG_DATA, Context.MODE_PRIVATE);
		SharedPreferences.Editor et = sp.edit();
		String data = DataHelper.gson.toJson(list);
		et.putString(TAG_DOWNLOADING_LIST, data);
		et.commit();
	}
}
