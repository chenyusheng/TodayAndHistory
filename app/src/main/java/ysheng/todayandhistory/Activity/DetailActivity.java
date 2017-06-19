package ysheng.todayandhistory.Activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ysheng.todayandhistory.R;
import ysheng.todayandhistory.Util.Debug_AdLog;
import ysheng.todayandhistory.Util.ToastUtils;
import ysheng.todayandhistory.Util.Util_BasicJSON;
import ysheng.todayandhistory.Util.Util_NetTool;
import ysheng.todayandhistory.View.ZoomImageView;
import ysheng.todayandhistory.model.History;
import ysheng.todayandhistory.model.HistoryDetail;

import static ysheng.todayandhistory.R.id.pic_layout;
import static ysheng.todayandhistory.R.id.tv_content;
import static ysheng.todayandhistory.R.id.tv_date;
import static ysheng.todayandhistory.R.id.tv_title;


public class DetailActivity extends AppCompatActivity {
    private AppBarLayout mAppBar;
    private CollapsingToolbarLayout mToolbarLayout;
    private ImageView mIvBg;
    private Toolbar mToolbar;
    private TextView mTvTitle;
    private TextView mTvDate;
    private TextView mTvContent;
    private LinearLayout mPicLayout;

    final static public String key = "3ceaad1dd59f61232e38691d22ad85f7";
    Context context;
    View rootView;
    LayoutInflater mInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        context = this;
        mInflater = getLayoutInflater();
        rootView = this.getWindow().getDecorView();

        mAppBar = (AppBarLayout) findViewById(R.id.app_bar);
        mToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mIvBg = (ImageView) findViewById(R.id.iv_bg);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTvTitle = (TextView) findViewById(tv_title);
        mTvDate = (TextView) findViewById(tv_date);
        mTvContent = (TextView) findViewById(tv_content);
        mPicLayout = (LinearLayout) findViewById(pic_layout);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        setSupportActionBar(mToolbar);
        History h = (History) getIntent().getSerializableExtra(key);
        mToolbarLayout.setTitle(h == null ? "历史上今天" : h.getTitle());
        mTvTitle.setText(h == null ? "历史上今天" : h.getTitle());
        mTvDate.setText(h.getDate());
        getOneDetail(h);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    void showDetail(HistoryDetail historyDetail, History history) {
        mTvTitle.setText(historyDetail.getTitle());
        mTvDate.setText(history.getDate());
        mTvContent.setText(Html.fromHtml(historyDetail.getContent()));

        for (int i = 0; i < historyDetail.getPicUrl().size(); i++) {
            if (i == 0) {
                Glide.with(this).load(historyDetail.getPicUrl().get(0).getUrl()).fitCenter().into(mIvBg);
            }
            View img = getLayoutInflater().inflate(R.layout.layout_pic_item, null);
            ImageView imageView = (ImageView) img.findViewById(R.id.imageView);
            final String url = historyDetail.getPicUrl().get(i).getUrl();
            Debug_AdLog.e("url = " + url);
//            Glide.with(this).load(historyDetail.getPicUrl().get(i).getUrl()).override(DensityUtils.px2dip(this,200),DensityUtils.px2dip(this,200)).centerCrop().into(imageView);
            Glide.with(this).load(url).fitCenter().into(imageView);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showBigPic(url);
                }
            });
            mPicLayout.addView(img);
        }
        if (historyDetail.getPicUrl().size() == 0) {
            Glide.with(this).load(R.mipmap.header).fitCenter().into(mIvBg);
        }
    }

    void showBigPic(String url){
        View view = LayoutInflater.from(this).inflate(R.layout.layout_pic_detail, null, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        ZoomImageView photoView = (ZoomImageView) view.findViewById(R.id.iv_pic);
        Glide.with(this).load(url).fitCenter().into(photoView);
        builder.setView(view);
        builder.setCancelable(true);
        final AlertDialog alertDialog = builder.create();

        final Dialog dialog = new Dialog(this, R.style.AlphaDialog);
        dialog.setContentView(view);
        dialog.show();
        view.findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                popWindow.dismiss();
                alertDialog.dismiss();
                dialog.dismiss();
            }
        });
//        alertDialog.show();
    }

    void getOneDetail(final History history) {
        try {
            Util_NetTool.getNet(String.format("http://v.juhe.cn/todayOnhistory/queryDetail.php?e_id=%s&key=%s", history.getE_id(), key), new Callback() {
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
                            JSONArray jsonArray = Util_BasicJSON.getJsonArray(jsonObject, "result", null);
                            if (jsonArray != null && jsonArray.length() > 0) {
                                final JSONObject detail = Util_BasicJSON.getJsonObject(jsonArray, 0, null);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        showDetail(new HistoryDetail(detail), history);
                                    }
                                });
                            }
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtils.showShort(context, "没有更详细的了");
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