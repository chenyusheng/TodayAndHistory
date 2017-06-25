package ysheng.todayandhistory.Util;

import android.content.Context;
import android.widget.Toast;

import com.blankj.utilcode.util.CacheUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ysheng.todayandhistory.model.History;
import ysheng.todayandhistory.model.HistoryDetail;

public class AppDataUtils {

    static AppDataUtils instance;

    CacheUtils cacheUtils = CacheUtils.getInstance();//缓存工具
    List<HistoryDetail> historyDetailList;

    private AppDataUtils() {
//        throw new UnsupportedOperationException("cannot be instantiated");
    }

    static public AppDataUtils getInstance() {
        if (instance == null) {
            instance = new AppDataUtils();
        }
        return instance;
    }

    public List<HistoryDetail> getHistoryDetailList() {
        if (historyDetailList == null) {
            historyDetailList = (List<HistoryDetail>) cacheUtils.getSerializable("HistoryColleted", new ArrayList<HistoryDetail>());
        }
        return historyDetailList;
    }

    public void setHistoryDetailList(List<HistoryDetail> historyDetailList) {

        this.historyDetailList = historyDetailList;
    }

    //添加一个收藏
    public void colloetOneHistory(HistoryDetail historyDetail) {
        if (getHistoryDetailList() == null) {
            historyDetailList = new ArrayList<HistoryDetail>();
        }
        historyDetailList.add(historyDetail);
        cacheUtils.put("HistoryColleted", (Serializable) historyDetailList);//保存缓存
    }
//    移除一个收藏
    public void removeOneColleted(HistoryDetail h){
        if (getHistoryDetailList() != null&&getHistoryDetailList().size()>0) {
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
    public boolean isColleted(HistoryDetail historyDetail) {
        if (getHistoryDetailList() != null && getHistoryDetailList().size() > 0) {
            Debug_AdLog.e(getHistoryDetailList().size()+" \n historyDetail = "+historyDetail.getTitle() );
            for (int i = 0; i < historyDetailList.size(); i++) {
                if (historyDetailList.get(i).getE_id().equals(historyDetail.getE_id())) {
                    return true;
                }
            }
        }
        return false;
    }

}