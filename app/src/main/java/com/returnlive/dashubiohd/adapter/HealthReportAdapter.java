package com.returnlive.dashubiohd.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.returnlive.dashubiohd.R;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者： 张梓彬
 * 日期： 2017/7/20 0020
 * 时间： 上午 11:08
 * 描述： 健康报告适配器
 */

public class HealthReportAdapter extends BaseExpandableListAdapter {
    public Map<String, List<String>> groupDataset = new HashMap<>();
    private Context context;
    private LayoutInflater inflater;
    private String[] parentList;
    private static final String TAG = "HealthReportFragment";
    private int doChildView = 1;
    private Map<String, ArrayList<String>> childernDataset;

    public HealthReportAdapter(Context context, String[] parentList, Map<String, ArrayList<String>> childernDataset) {
        this.context = context;
        this.parentList = parentList;
        this.childernDataset = childernDataset;
        for (int i = 0; i < parentList.length; i++) {
            groupDataset.put(parentList[i], childernDataset.get("children" + i));
            Log.e("ZZZ", "parentList[i]==: "+parentList[i] );
        }
        doChildView = 1;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    //  获得父项的数量
    @Override
    public int getGroupCount() {
        Log.d(TAG, "获得父项的数量: " + groupDataset.size());
        doChildView = 1;
        return groupDataset.size();
    }

    //  获得某个父项的子项数目
    @Override
    public int getChildrenCount(int groupPosition) {
        Log.d(TAG, "获得某个父项的子项数目: " + groupDataset.get(parentList[groupPosition]).size());
        return groupDataset.get(parentList[groupPosition]).size();
    }

    //  获得某个父项
    @Override
    public Object getGroup(int groupPosition) {
        Log.d(TAG, "获得某个父项: " + groupDataset.get(parentList[groupPosition]));
        return groupDataset.get(parentList[groupPosition]);
    }

    //  获得某个子项
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        Log.d(TAG, "获得某个子项: " + groupDataset.get(parentList[groupPosition]).get(childPosition));
        return groupDataset.get(parentList[groupPosition]).get(childPosition);
    }

    //  获得某个父项的id
    @Override
    public long getGroupId(int groupPosition) {
        Log.d(TAG, "获得某个父项的id: " + groupPosition);
        doChildView = 1;
        return groupPosition;
    }

    //  获得某个父项的某个子项的id
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        Log.d(TAG, "获得某个父项的某个子项的id: " + childPosition);
        return childPosition;
    }

    //  按函数的名字来理解应该是是否具有稳定的id，这个方法目前一直都是返回false，没有去改动过
    @Override
    public boolean hasStableIds() {
        Log.d(TAG, "hasStableIds: ");
        return false;
    }

    //  获得父项显示的view
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Log.d(TAG, "getGroupView: ");
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_group, null);
        }

        convertView.setTag(R.layout.item_group, groupPosition);
        convertView.setTag(R.layout.item_child, -1);
        TextView text = (TextView) convertView.findViewById(R.id.tv_group);
        text.setText(parentList[groupPosition] + "年");
        return convertView;
    }

    //  获得子项显示的view
    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView != null) {
            convertView = null;
        }
        convertView = inflater.inflate(R.layout.item_children_flow, null);
        final ChildViewHolder childHolder = new ChildViewHolder();
        convertView = inflater.inflate(R.layout.item_children_flow, null);
        childHolder.month_tagflowlayout = (TagFlowLayout) convertView.findViewById(R.id.month_tagflowlayout);
        convertView.setTag(childHolder);
        if (doChildView==1){
            Log.e("ZZZ", "groupPosition: "+groupPosition );
            Log.e("ZZZ", "childPosition: "+childPosition );
            TagAdapter tagAdapter = new TagAdapter(groupDataset.get(parentList[groupPosition])) {
            @Override
            public View getView(FlowLayout parent, int position, Object o) {
                TextView propertySingleNameTv = (TextView) LayoutInflater.from(context).inflate(R.layout.item_textview_children, parent, false);
                propertySingleNameTv.setText(groupDataset.get(parentList[groupPosition]).get(position) + "月");
                return propertySingleNameTv;
            }
        };


        childHolder.month_tagflowlayout.setAdapter(tagAdapter);
        childHolder.month_tagflowlayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                return false;
            }
        });
            doChildView = 2;
        }

        return convertView;

    }

    //  子项是否可选中，如果需要设置子项的点击事件，需要返回true
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    public final class ChildViewHolder {
        //private ToggleButton queToggle;
        private TagFlowLayout month_tagflowlayout;
    }

}
