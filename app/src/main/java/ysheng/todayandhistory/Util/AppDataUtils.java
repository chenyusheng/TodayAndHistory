package ysheng.todayandhistory.Util;

import android.content.Context;
import android.widget.Toast;

import com.blankj.utilcode.util.CacheUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import ysheng.todayandhistory.model.History;
import ysheng.todayandhistory.model.HistoryDetail;

public class AppDataUtils {

    static AppDataUtils instance;

    CacheUtils cacheUtils = CacheUtils.getInstance();//缓存工具
    List<History> historyDetailList;
    LinkedHashMap<String, History> readedMap;

    private AppDataUtils() {
//        throw new UnsupportedOperationException("cannot be instantiated");
    }

    static public AppDataUtils getInstance() {
        if (instance == null) {
            instance = new AppDataUtils();
        }
        return instance;
    }

    //阅读记录 使用map可以避免重复记录,使用linkhashmap可以保持数据的顺序，同时效率比hashmap高
    public LinkedHashMap<String, History> getReadedMap() {
        if (readedMap == null) {
            readedMap = (LinkedHashMap<String, History>) cacheUtils.getSerializable("ReadedColletedLinkedHashMap", new LinkedHashMap<String, History>());
        }
        return readedMap;
    }

    public void addOneReadedRecord(History h) {
        if (getReadedMap() != null) {
            LogUtils.e("添加阅读记录：" + h.getTitle());
            if (readedMap.containsKey(h.getE_id())) {
                LogUtils.e("已经有记录，删除");
                readedMap.remove(h.getE_id());
            }
            readedMap.put(h.getE_id(), h);
            cacheUtils.put("ReadedColletedLinkedHashMap", readedMap);//保存缓存
        }
    }

    public ArrayList<History> getAllReadedMap2List() {
        ArrayList<History> historys = new ArrayList<>();
        if (getReadedMap() != null) {
            Iterator<History> i = readedMap.values().iterator();
            while (i.hasNext()) {
                historys.add(0, i.next());
            }
//            historys.addAll(readedMap.values());
        }
        return historys;
    }


    public List<History> getHistoryDetailList() {
        if (historyDetailList == null) {
            historyDetailList = (List<History>) cacheUtils.getSerializable("HistoryColleted2", new ArrayList<HistoryDetail>());
        }
        return historyDetailList;
    }

    //添加一个收藏
    public void colloetOneHistory(History historyDetail) {
        if (getHistoryDetailList() == null) {
            historyDetailList = new ArrayList<History>();
        }
        historyDetailList.add(0,historyDetail);
        cacheUtils.put("HistoryColleted2", (Serializable) historyDetailList);//保存缓存
    }

    //    移除一个收藏
    public void removeOneColleted(History h) {
        if (getHistoryDetailList() != null && getHistoryDetailList().size() > 0) {
            for (int i = 0; i < historyDetailList.size(); i++) {
                if (historyDetailList.get(i).getE_id().equals(h.getE_id())) {
                    //找到要移除的目标
                    historyDetailList.remove(i);
                    return;
                }
            }
        }
    }

    //是否已经收藏
    public boolean isColleted(History historyDetail) {
        if (getHistoryDetailList() != null && getHistoryDetailList().size() > 0) {
            for (int i = 0; i < historyDetailList.size(); i++) {
                if (historyDetailList.get(i).getE_id().equals(historyDetail.getE_id())) {
                    return true;
                }
            }
        }
        return false;
    }

}