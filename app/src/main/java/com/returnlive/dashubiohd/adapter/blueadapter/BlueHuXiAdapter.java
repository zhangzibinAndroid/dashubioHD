package com.returnlive.dashubiohd.adapter.blueadapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.breathhome_ble_sdk.bean.BluetoothDeviceBean;
import com.returnlive.dashubiohd.R;
import com.returnlive.dashubiohd.base.MyBaseAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 作者： 张梓彬
 * 日期： 2017/8/3 0003
 * 时间： 下午 12:24
 * 描述： 蓝牙适配器
 */

public class BlueHuXiAdapter extends MyBaseAdapter<BluetoothDeviceBean> {
    public BlueHuXiAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_text_bluetooth, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        BluetoothDeviceBean bluetoothDevice = (BluetoothDeviceBean) getItem(position);
        viewHolder.tvBluetoothName.setText("蓝牙名称："+bluetoothDevice.getName() + "   蓝牙地址：" + bluetoothDevice.getAddress());
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_bluetooth_name)
        TextView tvBluetoothName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
