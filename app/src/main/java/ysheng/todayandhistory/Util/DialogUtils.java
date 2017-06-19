package ysheng.todayandhistory.Util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ProgressBar;

import ysheng.todayandhistory.R;

public class DialogUtils {

    private DialogUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    //获取一个加载的dialog
    public static AlertDialog getLoadingDialog(Activity activity){
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_loading, null, false);
        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
//        progressBar.sta
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(view);
        builder.setCancelable(true);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        return alertDialog;
    }

}