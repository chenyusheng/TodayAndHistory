package ysheng.todayandhistory.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;

import java.util.List;

import ysheng.todayandhistory.R;
import ysheng.todayandhistory.Util.AppDataUtils;
import ysheng.todayandhistory.View.ParallaxRecyclerView;
import ysheng.todayandhistory.adapter.Record_Adapter;
import ysheng.todayandhistory.listener.ItemClickListener;
import ysheng.todayandhistory.model.History;

public class Activity_Records extends BaseActivity {

    ParallaxRecyclerView recyclerView;
    Record_Adapter adapter;
    List<History> historyList;
    Activity activity;
    final static public String key = "3ceaad1dd59f61232e38691d22ad85f7";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_item_layout);
        activity = this;
        historyList = AppDataUtils.getInstance().getAllReadedMap2List();
        if (historyList == null || historyList.size() == 0) {
            ToastUtils.showShortSafe("暂无阅读记录");
        } else {
            adapter = new Record_Adapter(historyList);
            adapter.setItemClickListener(new ItemClickListener() {
                @Override
                public void onItemClick(History history) {
                    Intent intent = new Intent(Activity_Records.this, DetailActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable(key, history);
                    intent.putExtras(mBundle);
                    activity.startActivity(intent);
                }
            });
            recyclerView = (ParallaxRecyclerView) findViewById(R.id.recyclerLayout);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);

        }
    }
}
