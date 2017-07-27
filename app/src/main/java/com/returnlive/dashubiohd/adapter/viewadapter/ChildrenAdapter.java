package com.returnlive.dashubiohd.adapter.viewadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.returnlive.dashubiohd.R;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者： 张梓彬
 * 日期： 2017/7/26 0026
 * 时间： 上午 11:47
 * 描述： 子女
 */

public class ChildrenAdapter extends RecyclerView.Adapter<ChildrenAdapter.ViewHolder> {
    private List<String> list = new ArrayList<>();
    private static final String TAG = "DiseaseAdapter";
    private Context context;
    private OnDelectClickListener onDelectClickListener;

    public ChildrenAdapter(Context context) {
        this.context = context;
    }

    public void addData(List<String> strList){
        list.clear();
        list.addAll(strList);
    }

    public void delectListData(int position){
        list.remove(position);
        notifyDataSetChanged();
    }

    public List getList(){
        return list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected_disease_grid, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        AutoUtils.autoSize(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String diseaseName = list.get(position);
        holder.diseaseNameTv.setText(diseaseName);
        holder.imgDelete.setTag(position);
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDelectClickListener.OnDelectClick(v, (Integer) v.getTag());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.disease_name_tv)
        TextView diseaseNameTv;
        @BindView(R.id.img_delete)
        ImageView imgDelete;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public static interface OnDelectClickListener{
        void OnDelectClick(View view, int position);
    }

    public void setOnDelectClickListener(OnDelectClickListener onDelectClickListener){
        this.onDelectClickListener = onDelectClickListener;
    }
}
