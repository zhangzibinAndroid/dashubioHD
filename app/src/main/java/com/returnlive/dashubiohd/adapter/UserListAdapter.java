package com.returnlive.dashubiohd.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.returnlive.dashubiohd.R;
import com.returnlive.dashubiohd.base.MyBaseAdapter;
import com.returnlive.dashubiohd.bean.UserListBean;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者： 张梓彬
 * 日期： 2017/7/17 0017
 * 时间： 下午 12:43
 * 描述： 用户列表适配器
 */

public class UserListAdapter extends MyBaseAdapter<UserListBean.UserListDataBean>{
    private OnButtonClickListener mOnButtonClickListener;
    private String name;
    public UserListAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_user_list, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        UserListBean.UserListDataBean bean = list.get(position);
        viewHolder.btnItemLogin.setTag(bean.getName());

        viewHolder.tvItemName.setText(bean.getName());
        String sex = "";
        if (bean.getSex().equals("0")){
            sex = "无";
        }else if (bean.getSex().equals("1")){
            sex = "男";
        }else {
            sex = "女";
        }
        viewHolder.tvItemSex.setText(sex);
        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.btnItemLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnButtonClickListener != null) {
                    //注意这里使用getTag方法获取position
                    mOnButtonClickListener.onButtonClick(v, (String) finalViewHolder.btnItemLogin.getTag());
                }
            }
        });

        return convertView;
    }



    public static interface OnButtonClickListener {
        void onButtonClick(View view,String name);
    }

    public void setOnButtonClickListener(OnButtonClickListener listener) {
        this.mOnButtonClickListener = listener;

    }

    static class ViewHolder {
        @BindView(R.id.img_item_user)
        ImageView imgItemUser;
        @BindView(R.id.tv_item_name)
        TextView tvItemName;
        @BindView(R.id.tv_item_sex)
        TextView tvItemSex;
        @BindView(R.id.tv_item_times)
        TextView tvItemTimes;
        @BindView(R.id.btn_item_login)
        Button btnItemLogin;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
