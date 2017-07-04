package ysheng.todayandhistory.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ysheng.todayandhistory.MainActivity;
import ysheng.todayandhistory.R;
import ysheng.todayandhistory.listener.ItemClickListener;
import ysheng.todayandhistory.model.History;

/**
 * Created by shengbro on 2017/6/26.
 */

public class Record_Adapter extends RecyclerView.Adapter<Record_Adapter.ViewHolder> {

    List<History> historyList;
    ItemClickListener listener;

    public Record_Adapter(List<History> histories) {
        historyList = histories;
    }

    public void setItemClickListener(ItemClickListener l) {
        this.listener = l;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_record, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (historyList != null) {
            holder.title.setText(historyList.get(position).getTitle());
            holder.time.setText(historyList.get(position).getDate());
            if (listener != null) {
                holder.item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (historyList != null && historyList.size() > position) {
                            listener.onItemClick(historyList.get(position));
                        }
                    }
                });
            }

        }

    }


    @Override
    public int getItemCount() {
        if (historyList == null) {
            return 0;
        } else {
            return historyList.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, time;
        View item;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            time = (TextView) itemView.findViewById(R.id.time);
            item = itemView;
        }
    }
}
