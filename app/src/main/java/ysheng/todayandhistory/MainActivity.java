package ysheng.todayandhistory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;

import com.tencent.bugly.crashreport.CrashReport;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ysheng.todayandhistory.Activity.DetailActivity;
import ysheng.todayandhistory.Util.Debug_AdLog;
import ysheng.todayandhistory.Util.Util_BasicJSON;
import ysheng.todayandhistory.Util.Util_NetTool;
import ysheng.todayandhistory.adapter.ListAdapter_History;
import ysheng.todayandhistory.model.History;

public class MainActivity extends AppCompatActivity {

    ListView list_layout;
    SwipeRefreshLayout refresh_layout;
    ListAdapter_History listAdapter_history;
    List<History> historyList;
    Context context;
    View rootView;
    LayoutInflater mInflater;
    final static public String key = "3ceaad1dd59f61232e38691d22ad85f7";
    int month,day,year;
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
                Intent mIntent = new Intent(context,DetailActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable(key,historyList.get(position));
                mIntent.putExtras(mBundle);
                startActivity(mIntent);
            }
        });


        refresh_layout = (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        refresh_layout.setColorSchemeColors(R.color.colorAccent,R.color.colorPrimary,R.color.colorPrimaryDark);
        //        refresh_layout.setColorSchemeResources(R.color.colorAccent,R.color.colorPrimary,R.color.colorPrimaryDark);
        refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(month,day);
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
        month= c.get(Calendar.MONTH);
        year = c.get(Calendar.YEAR);
        day =  c.get(Calendar.DAY_OF_MONTH);

        getData(month, day);
    }

    void showDetail(){
        View view = LayoutInflater.from(this).inflate(R.layout.layout_history_detail, null, false);
        final DatePicker pick_date = (DatePicker) view.findViewById(R.id.pick_date);
        pick_date.init(year,month,day,null);
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
                getData(month,day);
            }
        });
        alertDialog.show();
    }

    void getData(int month ,int day) {
        try {
            if(historyList!=null&&historyList.size()>0){
                list_layout.smoothScrollToPosition(0);
            }
//            if (!refresh_layout.isRefreshing()) {
//                refresh_layout.setRefreshing(true);
//            }
            Util_NetTool.getNet(String.format("http://v.juhe.cn/todayOnhistory/queryEvent.php?date=%d/%d&key=%s", month+1, day, key), new Callback() {
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
                                    if(historyList.size()==0){
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

}
