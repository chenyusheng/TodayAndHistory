package ysheng.todayandhistory.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;

import java.util.List;

import de.greenrobot.event.EventBus;
import ysheng.todayandhistory.R;
import ysheng.todayandhistory.Util.AppDataUtils;
import ysheng.todayandhistory.View.ParallaxRecyclerView;
import ysheng.todayandhistory.adapter.Record_Adapter;
import ysheng.todayandhistory.listener.ItemClickListener;
import ysheng.todayandhistory.model.History;

public class Activity_Collets extends BaseActivity {

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
        EventBus.getDefault().register(this);
        historyList = AppDataUtils.getInstance().getHistoryDetailList();
        if (historyList == null || historyList.size() == 0) {
            ToastUtils.showShortSafe("暂无收藏数据");
        } else {
            adapter = new Record_Adapter(historyList);
            adapter.setItemClickListener(new ItemClickListener() {
                @Override
                public void onItemClick(History history) {
                    Intent intent = new Intent(Activity_Collets.this, DetailActivity.class);
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

    public void onEvent(String s) {
        if ("removeCollet".equals(s)) {
            LogUtils.e("更新数据");
            historyList = AppDataUtils.getInstance().getHistoryDetailList();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
