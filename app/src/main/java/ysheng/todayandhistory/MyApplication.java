package ysheng.todayandhistory;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MyApplication extends Application {
    private static MyApplication instance;
    public static MyApplication getInstance() {
        return instance;
    }
     @Override public void onCreate() {
         super.onCreate();
         instance = this;
         TypefaceProvider.registerDefaultIconSets();
         //初始化工具集
         Utils.init(this);
         LogUtils.Builder builder = new LogUtils.Builder();
         builder.setBorderSwitch(true);
         builder.setLogSwitch(true);//是否输出
         builder.setConsoleSwitch(true);
         builder.setGlobalTag("生哥哥");
         builder.setLogHeadSwitch(true);


        //初始化bugly
         Context context = getApplicationContext();
// 获取当前包名
         String packageName = context.getPackageName();
// 获取当前进程名
         String processName = getProcessName(android.os.Process.myPid());
// 设置是否为上报进程
         CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
         strategy.setUploadProcess(processName == null || processName.equals(packageName));
         Bugly.setAppChannel(this,"test");

         Bugly.init(getApplicationContext(), "d5024eba93", true,strategy);
     }
    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }
 }