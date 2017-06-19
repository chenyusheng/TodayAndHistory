package ysheng.todayandhistory.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ysheng.todayandhistory.R;
import ysheng.todayandhistory.model.History;


public class ListAdapter_History extends BaseAdapter {

    private Context mContext;
    private List<History> mListData;
    private LayoutInflater mLayoutInflater;
    private final int KEY_DATA = R.mipmap.ic_launcher;
    private final int KEY_POISTION = R.mipmap.ic_launcher;

    public ListAdapter_History(Context context, List<History> listData) {
        mContext = context.getApplicationContext();
        mListData = listData;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mListData.size();
    }

    @Override
    public History getItem(int position) {
        return mListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        TextView title;
        TextView date;
        TextView num_tv;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.history_item_view, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.title_tv);
            holder.date = (TextView) convertView.findViewById(R.id.date_tv);
            holder.num_tv = (TextView) convertView.findViewById(R.id.num_tv);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title.setText(mListData.get(position).getTitle());
        holder.date.setText(mListData.get(position).getDate());
        holder.num_tv.setText("历史事件 "+(position+1));
        return convertView;
    }

}
