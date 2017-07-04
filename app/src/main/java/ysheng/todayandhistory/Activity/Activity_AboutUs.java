package ysheng.todayandhistory.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import ysheng.todayandhistory.R;

public class Activity_AboutUs extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_about_us);
        TextView textView = (TextView) findViewById(R.id.tv_content);
        String s = "作者：yusheng"
                + "\n项目 GitHub 地址：https://github.com/chenyusheng/TodayAndHistory"
                + "\n简书：http://www.jianshu.com/u/4da8b3aed6d2"
                + "\n邮箱：ysheng8525@qq.com"
                + "\n"
                + "\n这是一个练手的项目，作者将进行不定期的更新。\n主要功能可以查看在历史上的今天曾经发生过什么大事件。" +
                "\n也可以指定任意一个您感兴趣的日期。\n对于比较有意义的历史事件也可以进行收藏 or 取消收藏。";
        textView.setText(s);
    }
}
