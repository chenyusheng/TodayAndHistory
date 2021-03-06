package ysheng.todayandhistory.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ysheng.todayandhistory.Util.Util_BasicJSON;

/**
 * Created by redlimit-web01 on 2016/11/9.
 */

public class HistoryDetail implements Serializable {

    /**
     * e_id : 11527
     * title : 孙中山改组中华革命党
     * content :     在97年前的今天，1919年10月10日 (农历八月十七)，孙中山改组中华革命党。
     * 1919年10月10日（距今97年），孙中山改组中华革命党为中国国民党，并公布《中国国民党规约》。规定：“从前所有中华革命党总章及各支部通则，一律废止；所有印章图记，一律照本规约所定，改用中国国民党名义，以昭统一。”国民党之前再加上“中国”二字，以区别于民初的旧国民党。旧国民党由五党团合并而成，中国国民党则直接由中华革命党改组而来。 新党章第四条规定：“凡中华革命党党员，皆得为本党党员，以中华革命党证书，领取本党证书。”13日，原中华革命党本部事务主任居正呈请任命中国国民党各部主任，孙中山以总理身份当即批令委居正为总务主任，谢持为党务主任，廖仲恺为财政主任。中国国民党以巩固共和，实行三民主义为宗旨。为广泛吸收党员，中国国民党放弃了中华革命党的秘密组织形式，转为公开。同时，新党章放宽了入党条件，规定凡赞成党的宗旨，经党员两人介绍，交纳党费10元者即可入党；放弃了中华革命党所规定的入党须按指模，并宣誓服从孙中山个人等带有帮会性质的苛到条件，并大量吸收青年人党。
     * 1913年二次革命失败后，袁世凯下令解散国民党。1914年，流亡日本的孙中山在东京组织成立中华革命党，继续积极从事反对袁世凯称帝的斗争活动。1916年护国战争结束，黎元洪继任总统，国会恢复。孙中山将中华革命党总部由东京迁至上海，并鉴于形势变化，认为“破坏既终，建议设始，革命名义已不复存在”，遂下令各地支部停止一切活动。1918年6月护法运动失败，孙中山总结教训，认识到南北军阀“如一丘之貉”，“救亡之策，必先事吾党之扩张，故亟重订党章，以促使党务发达”。此后，孙中山积极着手改组中华革命党。
     * 10月10日，中华革命党正式改组为中国国民党。《中国国民党规约》总纲规定：“本党以巩固共和、实行三民主义为宗旨”；“凡中华民国成年男女，与本党宗旨相同者，由党员二人介绍，并具自愿书于本党，由本党以给证书，始得为本党党员”；“凡中华革命党党员，皆得为本党党员”。中国国民党的组织制度为总理制，设总理1人，代表全党总揽党务。党本部设立总务部、党务部、财务部，孙中山以总理身份指定居正、谢持、廖仲恺分任3部主任。本部设在上海，下设总支部、支部、分部。党名加“中国”两字，以区别于原国民党。
     * <p>
     * <p>
     * picNo : 2
     * picUrl : [{"pic_title":"孙中山手绘的国民党党徽","id":1,"url":"http://images.juheapi.com/history/11527_1.jpg"},{"pic_title":"中国国民党总务主任居正","id":2,"url":"http://images.juheapi.com/history/11527_2.jpg"}]
     */
    private final static long serialVersionUID = 2L;
    private String e_id;
    private String title;
    private String content;
    private String picNo;

    /**
     * pic_title : 孙中山手绘的国民党党徽
     * id : 1
     * url : http://images.juheapi.com/history/11527_1.jpg
     */

    public HistoryDetail(JSONObject jsonObject) {
        if (jsonObject == null) {
            return;
        }
        e_id = Util_BasicJSON.getString(jsonObject, "e_id", null);
        title = Util_BasicJSON.getString(jsonObject, "title", null);
        content = Util_BasicJSON.getString(jsonObject, "content", null);
        picNo = Util_BasicJSON.getString(jsonObject, "picNo", null);
        JSONArray pics = Util_BasicJSON.getJsonArray(jsonObject, "picUrl", null);
        picUrl = new ArrayList<>();
        if (pics != null) {
            for (int i = 0; i < pics.length(); i++) {
                picUrl.add(new PicUrlBean(Util_BasicJSON.getJsonObject(pics, i, null)));
            }
        }
    }

    private List<PicUrlBean> picUrl;

    public String getE_id() {
        return e_id;
    }

    public void setE_id(String e_id) {
        this.e_id = e_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPicNo() {
        return picNo;
    }

    public void setPicNo(String picNo) {
        this.picNo = picNo;
    }

    public List<PicUrlBean> getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(List<PicUrlBean> picUrl) {
        this.picUrl = picUrl;
    }

    public static class PicUrlBean implements Serializable {
        private String pic_title;
        private int id;
        private String url;

        public PicUrlBean(JSONObject jsonObject) {
            if (jsonObject == null)
                return;
            pic_title = Util_BasicJSON.getString(jsonObject, "pic_title", null);
            url = Util_BasicJSON.getString(jsonObject, "url", null);
            id = Util_BasicJSON.getInt(jsonObject, "id", 0);
        }

        public String getPic_title() {
            return pic_title;
        }

        public void setPic_title(String pic_title) {
            this.pic_title = pic_title;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
