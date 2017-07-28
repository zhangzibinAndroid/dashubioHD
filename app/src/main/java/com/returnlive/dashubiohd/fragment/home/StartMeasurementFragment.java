package com.returnlive.dashubiohd.fragment.home;


import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.klw.singleleadsdk.OnCallBack;
import com.klw.singleleadsdk.SingleLeadUtil;
import com.klw.singleleadsdk.ble.JPBlePressureData;
import com.klw.singleleadsdk.entity.Data;
import com.returnlive.dashubiohd.R;
import com.returnlive.dashubiohd.base.BaseFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 作者： 张梓彬
 * 日期： 2017/7/13 0013
 * 时间： 上午 10:15
 * 描述： 开始测量
 */
public class StartMeasurementFragment extends BaseFragment {
    private static final String TAG = "StartMeasurementFragment";
    private TextView mTvData;
    private ListView mDeviceList;
    private Button mBtnScan;
    private SingleLeadUtil singleLeadUtil;
    private List<BluetoothDevice> deviceList = new ArrayList<>();
    private MyAdapter myAdapter;

    public StartMeasurementFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_start_measurement, container, false);
        findView(view);
        initView();
        setListener();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        singleLeadUtil = new SingleLeadUtil(getActivity(), new OnCallBack() {
            @Override
            public void onStatusCallBack(final int status) {
                String msg = "";
                //蓝牙状态返回
                switch (status) {
                    case OnCallBack.DEVICE_CONNECTED:
                        msg = "DEVICE_CONNECTED";
                        break;
                    case OnCallBack.DEVICE_DISCONNECTED:
                        msg = "DEVICE_DISCONNECTED";
                        break;
                    case OnCallBack.NO_BLE_ADAPTER:
                        msg = "NO_BLE_ADAPTER";
                        break;
                    case OnCallBack.NOT_SUPPORT_BLE:
                        msg = "NOT_SUPPORT_BLE";
                        break;
                    case OnCallBack.SEARCH_DEVICES_FAILED:
                        msg = "SEARCH_DEVICES_FAILED";
                        break;
                }
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                Log.e("Tag", msg);
            }

            @Override
            public void onDataCallBack(Data data) {
                //数据返回
                mTvData.append(formatDate(System.currentTimeMillis()) + " /////" + data.getUuid() + "：\n" + bytesToHexString(data.getVal()) + "\n");
                switch (data.getType()){
                    case Data.TYPE_TEST_DATA:
                        data.getData();//普通测量数据


                       /* Log.e(TAG, "心率: "+datazzb.rhythm );
                        Log.e(TAG, "脉搏: "+datazzb.pulse );
                        Log.e(TAG, "收缩压差: "+datazzb.sysdif );
                        Log.e(TAG, "收缩压: "+datazzb.sys );
                        Log.e(TAG, "舒张压: "+datazzb.dias );
                        Log.e(TAG, "平均压: "+datazzb.mean );
                        Log.e(TAG, "血氧: "+datazzb.oxygen );
                        Log.e(TAG, "呼吸: "+datazzb.resp );
                        Log.e(TAG, "ST段数值: "+datazzb.st );*/
                        break;
                    case Data.TYPE_TEST_END:
                        JPBlePressureData datazzb = data.getPressureData();//测量结束，JPBleNormalData中取得体征值
                        Log.e(TAG, "心率: "+datazzb.rhythm );
                        Log.e(TAG, "脉搏: "+datazzb.pulse );
                        Log.e(TAG, "收缩压差: "+datazzb.sysdif );
                        Log.e(TAG, "收缩压: "+datazzb.sys );
                        Log.e(TAG, "舒张压: "+datazzb.dias );
                        Log.e(TAG, "平均压: "+datazzb.mean );
                        Log.e(TAG, "血氧: "+datazzb.oxygen );
                        Log.e(TAG, "呼吸: "+datazzb.resp );
                        Log.e(TAG, "ST段数值: "+datazzb.st );
                        break;
                    case Data.TYPE_WAVE_1_DATA:
                        data.getWaveData();//波形1数据
                        Log.e(TAG, "波形1数据: "+data.getWaveData() );

                        break;
                    case Data.TYPE_WAVE_2_DATA:
                        data.getWaveData();//波形2数据
                        Log.e(TAG, "波形2数据: "+data.getWaveData() );
                        break;
                }
            }

            @Override
            public void onDeviceFound(BluetoothDevice device) {
                //设备发现
                if (deviceList.contains(device))
                    return;
                deviceList.add(device);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        myAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    @Override
    public void onDestroy() {
        /**
         * 断开连接
         */
        singleLeadUtil.disconnect();
        super.onDestroy();
    }

    private void initView() {
        myAdapter = new MyAdapter();
        mDeviceList.setAdapter(myAdapter);
    }

    private void setListener() {
        //扫描
        mBtnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deviceList.clear();
                singleLeadUtil.scanLeDevice(true);
            }
        });

        mDeviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //匹配
                singleLeadUtil.pairDevice(deviceList.get(position));
            }
        });
    }

    private void findView(View view) {
        mTvData = (TextView) view.findViewById(R.id.tv_data);
        mDeviceList = (ListView) view.findViewById(R.id.device_list);
        mBtnScan = (Button) view.findViewById(R.id.btn_scan);
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return deviceList.size();
        }

        @Override
        public Object getItem(int position) {
            return deviceList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            BluetoothDevice bluetoothDevice = (BluetoothDevice) getItem(position);
            TextView textView = new TextView(getActivity());
            textView.setTextSize(14);
            textView.setText(bluetoothDevice.getName() + " " + bluetoothDevice.getAddress());
            return textView;
        }
    }

    public String bytesToHexString(byte[] byteArray) {
        StringBuffer re = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            re.append(to16(byteArray[i]));
        }

        return re.toString();
    }

    public String to16(int b) {
        String hexString = Integer.toHexString(b);
        int lenth = hexString.length();
        if (lenth == 1) {
            hexString = "0" + hexString;
        }
        if (lenth > 2) {
            hexString = hexString.substring(lenth - 2, lenth);
        }
        return hexString;
    }

    public String formatDate(long oldString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String dateString = dateFormat.format(oldString);
        return dateString;
    }

}
