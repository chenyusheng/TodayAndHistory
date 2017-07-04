package ysheng.todayandhistory.model;

import org.json.JSONObject;

import java.io.Serializable;

import ysheng.todayandhistory.Util.Util_BasicJSON;

/**
 * Created by redlimit-web01 on 2016/11/9.
 */

public class History implements Serializable {

    /**
     * day : 1/1
     * date : 前45年01月01日
     * title : 罗马共和国开始使用儒略历
     * e_id : 1
     */

    private final static long serialVersionUID = 1L;
    private String date;
    private String day;
    private String title;
    private String e_id;

    public History(String json) {
        this(Util_BasicJSON.toJsonObject(json));
    }

    public History(JSONObject jsonObject) {
        if (jsonObject == null) {
            return;
        }
        day = Util_BasicJSON.getString(jsonObject, "day", null);
        date = Util_BasicJSON.getString(jsonObject, "date", null);
        title = Util_BasicJSON.getString(jsonObject, "title", null);
        e_id = Util_BasicJSON.getString(jsonObject, "e_id", null);
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getE_id() {
        return e_id;
    }

    public void setE_id(String e_id) {
        this.e_id = e_id;
    }
}
