package ysheng.todayandhistory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.IntentUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;
import com.tencent.bugly.crashreport.CrashReport;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ysheng.todayandhistory.Activity.Activity_AboutUs;
import ysheng.todayandhistory.Activity.Activity_Collets;
import ysheng.todayandhistory.Activity.Activity_Records;
import ysheng.todayandhistory.Activity.BaseActivity;
import ysheng.todayandhistory.Activity.DetailActivity;
import ysheng.todayandhistory.Util.Debug_AdLog;
import ysheng.todayandhistory.Util.Util_BasicJSON;
import ysheng.todayandhistory.Util.Util_NetTool;
import ysheng.todayandhistory.adapter.ListAdapter_History;
import ysheng.todayandhistory.model.History;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    ListView list_layout;
    SwipeRefreshLayout refresh_layout;
    ListAdapter_History listAdapter_history;
    List<History> historyList;
    Context context;
    View rootView;
    LayoutInflater mInflater;
    final static public String key = "3ceaad1dd59f61232e38691d22ad85f7";
    int month, day, year;

    private TextView btnMyCollet;
    private TextView btnMyRecord;
    private TextView btnCheckUpdate;
    private TextView btnAboutUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_main);
        mInflater = getLayoutInflater();
        rootView = this.getWindow().getDecorView();
        list_layout = (ListView) findViewById(R.id.list_layout);
        list_layout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ToastUtils.showShort(context, historyList.get(position).getTitle());
                Intent mIntent = new Intent(context, DetailActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable(key, historyList.get(position));
                mIntent.putExtras(mBundle);
                startActivity(mIntent);
            }
        });

        //初始化侧滑栏
        initDrawlayout(savedInstanceState);
        refresh_layout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        refresh_layout.setColorSchemeColors(getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorPrimaryDark));
        //        refresh_layout.setColorSchemeResources(R.color.colorAccent,R.color.colorPrimary,R.color.colorPrimaryDark);
        refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(month, day);
            }
        });
        refresh_layout.setRefreshing(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDetail();
            }
        });
        historyList = new ArrayList<>();
        listAdapter_history = new ListAdapter_History(this, historyList);
        list_layout.setAdapter(listAdapter_history);

        final Calendar c = Calendar.getInstance();
        month = c.get(Calendar.MONTH);
        year = c.get(Calendar.YEAR);
        day = c.get(Calendar.DAY_OF_MONTH);

        getData(month, day);
    }

    @Override
    public boolean isSupportSwipeBack() {
        return false;
    }

    void initDrawlayout(Bundle savedInstanceState) {
        SlidingRootNavBuilder slidingRootNavBuilder = new SlidingRootNavBuilder(this);
        View drawlayout = mInflater.inflate(R.layout.layout_drawlayout, null);
        slidingRootNavBuilder.withMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withDragDistance(140) //Horizontal translation of a view. Default == 180dp
                .withRootViewScale(0.7f) //Content view's scale will be interpolated between 1f and 0.7f. Default == 0.65f;
                .withRootViewElevation(10) //Content view's elevation will be interpolated between 0 and 10dp. Default == 8.
                .withRootViewYTranslation(4) //Content view's translationY will be interpolated between 0 and 4. Default == 0
                .withMenuView(drawlayout)
//                .withMenuLayout(R.layout.layout_drawlayout)
                .inject();
        btnMyCollet = (TextView) drawlayout.findViewById(R.id.btn_my_collet);
        btnMyRecord = (TextView) drawlayout.findViewById(R.id.btn_my_record);
        btnCheckUpdate = (TextView) drawlayout.findViewById(R.id.btn_check_update);
        btnAboutUs = (TextView) drawlayout.findViewById(R.id.btn_about_us);
        btnMyCollet.setOnClickListener(this);
        btnMyRecord.setOnClickListener(this);
        btnCheckUpdate.setOnClickListener(this);
        btnAboutUs.setOnClickListener(this);
//        加载更新信息
        loadUpgradeInfo();
    }

    void showDetail() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_history_detail, null, false);
        final DatePicker pick_date = (DatePicker) view.findViewById(R.id.pick_date);
        pick_date.init(year, month, day, null);
        Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        builder.setCancelable(true);
        final AlertDialog alertDialog = builder.create();
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                popWindow.dismiss();
                alertDialog.dismiss();
                month = pick_date.getMonth();
                day = pick_date.getDayOfMonth();
                getData(month, day);
            }
        });
        alertDialog.show();
    }

    void getData(int month, int day) {
        try {
            if (historyList != null && historyList.size() > 0) {
                list_layout.smoothScrollToPosition(0);
            }
//            if (!refresh_layout.isRefreshing()) {
//                refresh_layout.setRefreshing(true);
//            }
            Util_NetTool.getNet(String.format("http://v.juhe.cn/todayOnhistory/queryEvent.php?date=%d/%d&key=%s", month + 1, day, key), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (call.isCanceled())
                        return;
                    if (response.isSuccessful()) {
                        String result = response.body().string();
                        Debug_AdLog.e("result = " + result);
                        JSONObject jsonObject = Util_BasicJSON.toJsonObject(result);
                        int err_code = Util_BasicJSON.getInt(jsonObject, "error_code", 404);
                        if (err_code == 0) {
                            historyList.clear();
                            JSONArray jsonArray = Util_BasicJSON.getJsonArray(jsonObject, "result", null);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                historyList.add(new History(Util_BasicJSON.getJsonObject(jsonArray, i, null)));
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (refresh_layout.isRefreshing()) {
                                        refresh_layout.setRefreshing(false);
                                    }
                                    if (historyList.size() == 0) {
                                        com.blankj.utilcode.util.ToastUtils.showLongSafe("这一天并没有什么大事发生，请重新选择");
                                        showDetail();
                                    }
                                    listAdapter_history.notifyDataSetChanged();
                                }
                            });

                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_my_collet:
                Intent intent2 = new Intent(this, Activity_Collets.class);
                this.startActivity(intent2);
                break;
            case R.id.btn_my_record:
                Intent intent3 = new Intent(this, Activity_Records.class);
                this.startActivity(intent3);
                break;
            case R.id.btn_check_update:
                Beta.checkUpgrade();
                break;
            case R.id.btn_about_us:
                Intent intent = new Intent(this, Activity_AboutUs.class);
                this.startActivity(intent);
                break;
        }
    }


    private void loadUpgradeInfo() {
        if (btnCheckUpdate == null)
            return;

        /***** 获取升级信息 *****/
        UpgradeInfo upgradeInfo = Beta.getUpgradeInfo();

        if (upgradeInfo == null) {
            LogUtils.e("无升级信息");
            return;
        }
        StringBuilder info = new StringBuilder();
        info.append("id: ").append(upgradeInfo.id).append("\n");
        info.append("标题: ").append(upgradeInfo.title).append("\n");
        info.append("升级说明: ").append(upgradeInfo.newFeature).append("\n");
        info.append("versionCode: ").append(upgradeInfo.versionCode).append("\n");
        info.append("versionName: ").append(upgradeInfo.versionName).append("\n");
        info.append("发布时间: ").append(upgradeInfo.publishTime).append("\n");
        info.append("安装包Md5: ").append(upgradeInfo.apkMd5).append("\n");
        info.append("安装包下载地址: ").append(upgradeInfo.apkUrl).append("\n");
        info.append("安装包大小: ").append(upgradeInfo.fileSize).append("\n");
        info.append("弹窗间隔（ms）: ").append(upgradeInfo.popInterval).append("\n");
        info.append("弹窗次数: ").append(upgradeInfo.popTimes).append("\n");
        info.append("发布类型（0:测试 1:正式）: ").append(upgradeInfo.publishType).append("\n");
        info.append("弹窗类型（1:建议 2:强制 3:手工）: ").append(upgradeInfo.upgradeType).append("\n");
        info.append("图片地址：").append(upgradeInfo.imageUrl);

        btnCheckUpdate.setTextColor(getResources().getColor(R.color.red));
        btnCheckUpdate.setText("有新版本");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.equals(KeyEvent.KEYCODE_BACK)) {
            ToastUtils.showShortSafe("即将退出");
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
