package com.returnlive.dashubiohd.fragment.home;


import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.returnlive.dashubiohd.R;
import com.returnlive.dashubiohd.activity.HomeActivity;
import com.returnlive.dashubiohd.adapter.blueadapter.BlueAdapter;
import com.returnlive.dashubiohd.application.DashuHdApplication;
import com.returnlive.dashubiohd.base.BaseFragment;
import com.returnlive.dashubiohd.constant.InterfaceUrl;
import com.returnlive.dashubiohd.ecg_single.OnCallBack;
import com.returnlive.dashubiohd.ecg_single.SingleLeadUtil;
import com.returnlive.dashubiohd.ecg_single.ble.JPBleNormalData;
import com.returnlive.dashubiohd.ecg_single.entity.Data;
import com.returnlive.dashubiohd.view.EcgPathOne;
import com.returnlive.dashubiohd.view.EcgPathSecond;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * 作者： 张梓彬
 * 日期： 2017/7/13 0013
 * 时间： 上午 10:15
 * 描述： 开始测量
 */
public class StartMeasurementFragment extends BaseFragment {
    private static final String TAG = "StartMeasurementFragment";
    @BindView(R.id.lay_multi_parameter_monitor)
    AutoLinearLayout layMultiParameterMonitor;
    @BindView(R.id.lay_urine_detector)
    AutoLinearLayout layUrineDetector;
    @BindView(R.id.lay_dry_biochemical_analyzer)
    AutoLinearLayout layDryBiochemicalAnalyzer;
    @BindView(R.id.lay_respiratory_monitor)
    AutoLinearLayout layRespiratoryMonitor;
    private Unbinder unbinder;
    private AlertDialog dialog;
    private SingleLeadUtil singleLeadUtil;
    private List<BluetoothDevice> deviceList = new ArrayList<>();
    private BlueAdapter blueAdapter;
    private boolean isBuleConnect = false;
    public DashuHdApplication myApplication;


    public StartMeasurementFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_start_measurement, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        isBuleConnect = false;
        myApplication = new DashuHdApplication();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.lay_multi_parameter_monitor, R.id.lay_urine_detector, R.id.lay_dry_biochemical_analyzer, R.id.lay_respiratory_monitor})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lay_multi_parameter_monitor:
                showBlueToathDialog();
                break;
            case R.id.lay_urine_detector:
                break;
            case R.id.lay_dry_biochemical_analyzer:
                showBlueToathDialogGanShi();
                break;
            case R.id.lay_respiratory_monitor:
//                showHuXiDialog();
                break;
        }
    }


    private void showBlueToathDialog() {
        AlertDialog.Builder blueToathDialog = new AlertDialog.Builder(getActivity());
        View view = View.inflate(getActivity(), R.layout.dialog_search_buletooth, null);
        ViewHolderBlueToath viewHolderBlueToath = new ViewHolderBlueToath(view);
        blueToathDialog.setView(view);
        blueAdapter = new BlueAdapter(getActivity());
        viewHolderBlueToath.lvSearchBluetooth.setAdapter(blueAdapter);
        viewHolderBlueToath.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deviceList.clear();
                singleLeadUtil.scanLeDevice(true);
            }
        });


        viewHolderBlueToath.lvSearchBluetooth.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //匹配
                singleLeadUtil.pairDevice(deviceList.get(position));
                isBuleConnect = true;
                dialog.dismiss();
                showMultiParameterMonitorDialog();
            }
        });
        dialog = blueToathDialog.show();
    }

    private void showBlueToathDialogGanShi() {
        AlertDialog.Builder blueToathDialog = new AlertDialog.Builder(getActivity());
        View view = View.inflate(getActivity(), R.layout.dialog_search_buletooth, null);
        ViewHolderGanShi viewHolderGanShi = new ViewHolderGanShi(view);
        viewHolderGanShi.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        blueToathDialog.setView(view);
        dialog = blueToathDialog.show();


    }

    static class ViewHolderGanShi {
        @BindView(R.id.lv_search_bluetooth)
        ListView lvSearchBluetooth;
        @BindView(R.id.btn_search)
        Button btnSearch;

        ViewHolderGanShi(View view) {
            ButterKnife.bind(this, view);
        }
    }


    //多参数监测仪
    private void showMultiParameterMonitorDialog() {
        AlertDialog.Builder multiParameterMonitorDialog = new AlertDialog.Builder(getActivity());
        View view = View.inflate(getActivity(), R.layout.dialog_xindian, null);
        final ViewHolderMultiParameterMonitor viewHolder = new ViewHolderMultiParameterMonitor(view);
        viewHolder.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleLeadUtil.disconnect();//点击取消断开蓝夜连接
                dialog.dismiss();
            }
        });
        viewHolder.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //保存数据
                singleLeadUtil.disconnect();//点击取消断开蓝夜连接
                dialog.dismiss();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        saveMultiParameterMonitorData(viewHolder.tvHeartRateColon.getText().toString(),
                                viewHolder.tvPulse.getText().toString(),
                                viewHolder.tvSystolicBloodPressureDifference.getText().toString(),
                                viewHolder.tvSystolicBloodPressure.getText().toString(),
                                viewHolder.tvDiastolicBloodPressure.getText().toString(),
                                viewHolder.tvTheAveragePressure.getText().toString(),
                                viewHolder.tvOxygen.getText().toString(),
                                viewHolder.tvBreathingRate.getText().toString(),
                                viewHolder.tvStSegmentNumerical.getText().toString());
                    }
                }).start();


            }
        });
        multiParameterMonitorDialog.setView(view);
        dialog = multiParameterMonitorDialog.show();
        setOnDataCallBackListener(new OnDataCallBackListener() {
            @Override
            public void OnDataCallBack(ArrayList<Float> ecg_list_one) {
                viewHolder.ecg_path.addDATA(ecg_list_one);
            }
        });

        setOnDataCallBackListenerSecond(new OnDataCallBackListenerSecond() {
            @Override
            public void OnDataCallBack(ArrayList<Float> ecg_list_two) {
                viewHolder.ecg_path_second.addDATA(ecg_list_two);
            }
        });

        setOnTextDataCallBackListener(new OnTextDataCallBackListener() {
            @Override
            public void OnTextDataCallBack(String rhythm, String pulse, String sysdif, String sys, String dias, String mean, String oxygen, String resp, String st) {
                viewHolder.tvHeartRateColon.setText(rhythm);
                viewHolder.tvPulse.setText(pulse);
                viewHolder.tvSystolicBloodPressureDifference.setText(sysdif);
                viewHolder.tvSystolicBloodPressure.setText(sys);
                viewHolder.tvDiastolicBloodPressure.setText(dias);
                viewHolder.tvTheAveragePressure.setText(mean);
                viewHolder.tvOxygen.setText(oxygen);
                viewHolder.tvBreathingRate.setText(resp);
                viewHolder.tvStSegmentNumerical.setText(st);
            }
        });


    }


    //多参数检测仪数据保存接口
    private void saveMultiParameterMonitorData(String rhythm, String pulse, String sysdif, String sys, String dias, String mean, String oxygen, String resp, String st) {
        OkHttpUtils.post().url(InterfaceUrl.MULTIPARAMETERMONITORDATA_URL + sessonWithCode + "/mid/" + HomeActivity.mid)
                .addParams("rhythm", rhythm)
                .addParams("pulse", pulse)
                .addParams("sysdif", sysdif)
                .addParams("sys", sys)
                .addParams("dias", dias)
                .addParams("mean", mean)
                .addParams("oxygen", oxygen)
                .addParams("resp", resp)
                .addParams("st", st)

                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(TAG, "onError: " + e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                Log.e(TAG, "response: ");

            }
        });
    }


    static class ViewHolderMultiParameterMonitor {
        @BindView(R.id.tv_heart_rate_colon)
        TextView tvHeartRateColon;
        @BindView(R.id.tv_pulse)
        TextView tvPulse;
        @BindView(R.id.tv_systolic_blood_pressure_difference)
        TextView tvSystolicBloodPressureDifference;
        @BindView(R.id.tv_systolic_blood_pressure)
        TextView tvSystolicBloodPressure;
        @BindView(R.id.tv_diastolic_blood_pressure)
        TextView tvDiastolicBloodPressure;
        @BindView(R.id.tv_the_average_pressure)
        TextView tvTheAveragePressure;
        @BindView(R.id.tv_oxygen)
        TextView tvOxygen;
        @BindView(R.id.tv_breathing_rate)
        TextView tvBreathingRate;
        @BindView(R.id.tv_st_segment_numerical)
        TextView tvStSegmentNumerical;
        @BindView(R.id.btn_cancel)
        Button btnCancel;
        @BindView(R.id.btn_save)
        Button btnSave;
        @BindView(R.id.ecg_path)
        EcgPathOne ecg_path;
        @BindView(R.id.ecg_path_second)
        EcgPathSecond ecg_path_second;

        ViewHolderMultiParameterMonitor(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ViewHolderBlueToath {
        @BindView(R.id.lv_search_bluetooth)
        ListView lvSearchBluetooth;
        @BindView(R.id.btn_search)
        Button btnSearch;

        ViewHolderBlueToath(View view) {
            ButterKnife.bind(this, view);
        }
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
                        msg = "设备已连接";
                        break;
                    case OnCallBack.DEVICE_DISCONNECTED:
                        msg = "设备已断开";
                        break;
                    case OnCallBack.NO_BLE_ADAPTER:
                        msg = "没有适配器";
                        break;
                    case OnCallBack.NOT_SUPPORT_BLE:
                        msg = "不支持蓝牙";
                        break;
                    case OnCallBack.SEARCH_DEVICES_FAILED:
                        msg = "搜索蓝牙失败";
                        break;
                }

                final String finalMsg = msg;
                Toast.makeText(getActivity(), finalMsg, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDataCallBack(Data data) {
                //数据返回
                switch (data.getType()) {
                    case Data.TYPE_TEST_DATA:
                        JPBleNormalData mdata = data.getData();//普通测量数据
                        int rhythm = 0;
                        try {
                            rhythm = mdata.rhythm;
                        } catch (Exception e) {
                        }

                        int pulse = 0;
                        try {
                            pulse = mdata.pulse;
                        } catch (Exception e) {
                        }

                        int sysdif = 0;
                        try {
                            sysdif = mdata.sysdif;
                        } catch (Exception e) {
                        }

                        int sys = 0;
                        try {
                            sys = mdata.sys;
                        } catch (Exception e) {
                        }

                        int dias = 0;
                        try {
                            dias = mdata.dias;
                        } catch (Exception e) {

                        }


                        int mean = 0;
                        try {
                            mean = mdata.mean;
                        } catch (Exception e) {
                        }

                        int oxygen = 0;
                        try {
                            oxygen = mdata.oxygen;
                        } catch (Exception e) {
                        }

                        int resp = 0;
                        try {
                            resp = mdata.resp;
                        } catch (Exception e) {
                        }

                        float st = 0;
                        try {
                            st = mdata.st;
                        } catch (Exception e) {
                        }

                        onTextDataCallBackListener.OnTextDataCallBack(rhythm + "", pulse + "", sysdif + "", sys + "", dias + "", mean + "", oxygen + "", resp + "", st + "");


                        break;
                    case Data.TYPE_TEST_END:
                        /*JPBlePressureData datazzb = data.getPressureData();//测量结束，JPBleNormalData中取得体征值
                        Log.e("ZZZ", "心率: " + datazzb.rhythm);
                        Log.e("ZZZ", "脉搏: " + datazzb.pulse);
                        Log.e("ZZZ", "收缩压差: " + datazzb.sysdif);
                        Log.e("ZZZ", "收缩压: " + datazzb.sys);
                        Log.e("ZZZ", "舒张压: " + datazzb.dias);
                        Log.e("ZZZ", "平均压: " + datazzb.mean);
                        Log.e("ZZZ", "血氧: " + datazzb.oxygen);
                        Log.e("ZZZ", "呼吸: " + datazzb.resp);
                        Log.e("ZZZ", "ST段数值: " + datazzb.st);*/

                        JPBleNormalData datazzb = data.getData();//普通测量数据

                        try {
                            Log.e(TAG, "心率: " + datazzb.rhythm);
                        } catch (Exception e) {
                            Log.e(TAG, "心率: " + 0);
                        }


                        try {
                            Log.e(TAG, "脉搏: " + datazzb.pulse);
                        } catch (Exception e) {
                            Log.e(TAG, "脉搏: " + 0);
                        }

                        try {
                            Log.e(TAG, "收缩压差: " + datazzb.sysdif);
                        } catch (Exception e) {
                            Log.e(TAG, "收缩压差: " + 0);
                        }

                        try {
                            Log.e(TAG, "收缩压: " + datazzb.sys);
                        } catch (Exception e) {
                            Log.e(TAG, "收缩压: " + 0);
                        }

                        try {
                            Log.e(TAG, "舒张压: " + datazzb.dias);
                        } catch (Exception e) {
                            Log.e(TAG, "舒张压: " + 0);

                        }

                        try {
                            Log.e(TAG, "平均压: " + datazzb.mean);
                        } catch (Exception e) {
                            Log.e(TAG, "平均压: " + 0);
                        }


                        try {
                            Log.e(TAG, "血氧: " + datazzb.oxygen);
                        } catch (Exception e) {
                            Log.e(TAG, "血氧: " + 0);
                        }

                        try {
                            Log.e(TAG, "呼吸: " + datazzb.resp);
                        } catch (Exception e) {
                            Log.e(TAG, "呼吸: " + 0);
                        }

                        try {
                            Log.e(TAG, "ST段数值: " + datazzb.st);
                        } catch (Exception e) {
                            Log.e(TAG, "ST段数值: " + 0);
                        }
                        break;
                    case Data.TYPE_WAVE_1_DATA:
                        onDataCallBackListener.OnDataCallBack(data.getWaveData());

                        break;
                    case Data.TYPE_WAVE_2_DATA:
                        onDataCallBackListenerSecond.OnDataCallBack(data.getWaveData());
                        break;
                }
            }

            @Override
            public void onDeviceFound(BluetoothDevice device) {
                //设备发现
                if (deviceList.contains(device))
                    return;
                deviceList.add(device);
                blueAdapter.addAllDataToMyadapter(deviceList);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        blueAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }


    private OnDataCallBackListener onDataCallBackListener;
    private OnDataCallBackListenerSecond onDataCallBackListenerSecond;
    private OnTextDataCallBackListener onTextDataCallBackListener;

    public static interface OnDataCallBackListener {
        void OnDataCallBack(ArrayList<Float> ecg_list_one);
    }

    public void setOnDataCallBackListener(OnDataCallBackListener onDataCallBackListener) {
        this.onDataCallBackListener = onDataCallBackListener;
    }


    public static interface OnDataCallBackListenerSecond {
        void OnDataCallBack(ArrayList<Float> ecg_list_two);
    }

    public void setOnDataCallBackListenerSecond(OnDataCallBackListenerSecond onDataCallBackListenerSecond) {
        this.onDataCallBackListenerSecond = onDataCallBackListenerSecond;
    }


    public static interface OnTextDataCallBackListener {
        void OnTextDataCallBack(String rhythm, String pulse, String sysdif, String sys, String dias, String mean, String oxygen, String resp, String st);
    }

    public void setOnTextDataCallBackListener(OnTextDataCallBackListener onTextDataCallBackListener) {
        this.onTextDataCallBackListener = onTextDataCallBackListener;

    }

  /*  private List<BluetoothDeviceBean> devicelist;                            //存放搜索到的蓝牙设备
    private String IMEI = "B652276134";                                        //IMEI号,用户绑定的设备IMEI号
    private StringBuffer sb;                                                //用作存放蓝牙设备的指令
    private HolderBean holder;
    private GetBleCurrentVersionTask getBleCurrentVersionTask;
    private SearchBleDeviceTask mySearchBleDeviceTask;
    private MessageManager myMessageManager;            //蓝牙信息管理器
    private BluetoothController mBluetoothController;
    private BleVersionMsgBean bleVersionMsg;
    private MyBleStateBroadcast myBleStateBroadcast;                        //自定义蓝牙状态监听
    private SystemBleBroadcast systemBleBroadcast;

    private void showHuXiDialog() {
        AlertDialog.Builder huxiDialog = new AlertDialog.Builder(getActivity());
        View view = View.inflate(getActivity(), R.layout.dialog_huxi, null);
        final ViewHolderHuXiDialog viewHolderHuXiDialog = new ViewHolderHuXiDialog(view);
        viewHolderHuXiDialog.btnBluetoothDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBluetoothController.stopConnectBLe();
            }
        });

        viewHolderHuXiDialog.btnBluetoothConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (0 != devicelist.size()) {
                    for (int i = 0; i < devicelist.size(); i++) {
                        if (devicelist.get(i).getName().equals(IMEI)) {
                            mBluetoothController.connect(devicelist.get(i));
                            break;
                        }
                    }
                } else {
                    startSearchBle();
                }
            }
        });

        viewHolderHuXiDialog.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (null != searchIntent) {
                    getActivity().stopService(searchIntent);
                }
                if (mySearchBleDeviceTask != null && mySearchBleDeviceTask.getStatus() != AsyncTask.Status.FINISHED) {
                    mySearchBleDeviceTask.cancel(true);

                }
                if (getBleCurrentVersionTask != null && getBleCurrentVersionTask.getStatus() != AsyncTask.Status.FINISHED) {
                    getBleCurrentVersionTask.cancel(true);
                }
                if (myMessageManager != null) {
                    myMessageManager.destroyMessageManager();
                }
                getActivity().unregisterReceiver(myBleStateBroadcast);
//                getActivity().unregisterReceiver(systemBleBroadcast);
            }
        });


        viewHolderHuXiDialog.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        huxiDialog.setView(view);
        dialog = huxiDialog.show();
        initDatas(viewHolderHuXiDialog.edt_imei.getText().toString());

        setGetDataListener(new GetDataListener() {
            @Override
            public void getData(String text) {
                viewHolderHuXiDialog.tvHuxiData.setText(text);
            }
        });

        setBuleToothIsConnect(new BuleToothIsConnect() {
            @Override
            public void getbuleToothIsConnect(String isConnect) {
                viewHolderHuXiDialog.btnBluetoothIsconnect.setText(isConnect);
            }
        });
    }


    private void initDatas(String imei) {
        getTheNewsVersion(imei);                                                //获取最新版本号
        devicelist = new ArrayList<BluetoothDeviceBean>();
        sb = new StringBuffer();
        holder = new HolderBean();
        holder.setDetectedNo(33);
        holder.setSaleChannel(7200000);
        holder.setBirthdate("1988-3-15");
        holder.setPef(659);
        holder.setFev1((float) 4.56);
        holder.setFvc((float) 3.56);
        holder.setGender(1);
        holder.setHeight(183);
        holder.setWeight(78);
        holder.setDevieceNo("B652276134");

    }

    private void getTheNewsVersion(final String imei) {
        getBleCurrentVersionTask = (GetBleCurrentVersionTask) new GetBleCurrentVersionTask(getActivity()).execute();
        getBleCurrentVersionTask.setAsyncResponse(new AsyncResponse<ReturnBean<BleVersionMsgBean>>() {

            @Override
            public void onDataReceivedFailed() {
                // TODO Auto-generated method stub
                BreathHomeLog.e("Error");
            }


            @Override
            public void onDataReceivedSuccess(ReturnBean<BleVersionMsgBean> returnBean) {
                bleVersionMsg = returnBean.getObject();
                initTools();                            //初始化工具
                initBroadcast(imei);                        //定义广播接收
            }

            @Override
            public void onDataReceivedAndDefaultData(
                    ReturnBean<BleVersionMsgBean> returnBean) {
                bleVersionMsg = returnBean.getObject();
                initTools();                            //初始化工具
                initBroadcast(imei);                        //定义广播接收

            }
        });
    }


    private void initBroadcast(final String imei) {
        myBleStateBroadcast = new MyBleStateBroadcast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantUtils.ACTION_UPDATE_DEVICE_LIST);
        intentFilter.addAction(ConstantUtils.ACTION_CONNECTED_ONE_DEVICE);
        intentFilter.addAction(ConstantUtils.ACTION_RECEIVE_MESSAGE_FROM_DEVICE);
        intentFilter.addAction(ConstantUtils.ACTION_STOP_CONNECT);
        getActivity().registerReceiver(myBleStateBroadcast, intentFilter);
        myBleStateBroadcast.setMyBroadcastResponse(new BroadcastResponse() {

            @Override
            public void onSearchBleSuccess(BluetoothDeviceBean bleDevice) {
                //搜索到蓝牙设备
                boolean found = false;//记录该条记录是否在list中
                for (BluetoothDeviceBean device : devicelist) {
                    if (device.getAddress().equals(bleDevice.getAddress())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    devicelist.add(bleDevice);
                    String tmp = imei;
                    Log.e(TAG, "tmp: " + tmp);
                    if (!StringUtils.isEmpty(tmp)) {
                        IMEI = tmp;
                    } else {
                        Toast.makeText(getActivity(), R.string.hint_null_imei, Toast.LENGTH_SHORT).show();
                        return;
                    }


                    if (bleDevice.getName().equals(IMEI)) {
                        mBluetoothController.stopScanBLE();
                        mBluetoothController.connect(bleDevice);
                    }

                } else {//断开后重新连接


                }
            }

            @Override
            public void onReceiveBleMsg(String str) {
                //接收到蓝牙设备的指令
                sb.append(str);
                if (-1 != sb.lastIndexOf("\r\n")) {//判断结尾符
                    StringBuffer tmp = new StringBuffer(sb.toString());
                    myMessageManager.matchCommandCode(tmp);
                    sb.setLength(0);            //StringBuffer清零
                }
            }

            @Override
            public void onBleDisconnect() {
                //蓝牙设备断开连接
                buleToothIsConnect.getbuleToothIsConnect(getActivity().getResources().getString(R.string.ble_state_disconnect));
            }

            @Override
            public void onBleConnect() {
                //蓝牙设备已匹配上
                buleToothIsConnect.getbuleToothIsConnect(getActivity().getResources().getString(R.string.ble_state_match));
            }
        });
        //系统蓝牙服务开启监听
        IntentFilter systemBleListenerFilter = new IntentFilter();
        systemBleListenerFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        getActivity().registerReceiver(systemBleBroadcast, systemBleListenerFilter);


    }


    static class ViewHolderHuXiDialog {
        @BindView(R.id.btn_bluetooth_isconnect)
        Button btnBluetoothIsconnect;
        @BindView(R.id.btn_bluetooth_del)
        Button btnBluetoothDel;
        @BindView(R.id.btn_bluetooth_connect)
        Button btnBluetoothConnect;
        @BindView(R.id.tv_huxi_data)
        TextView tvHuxiData;
        @BindView(R.id.btn_cancel)
        Button btnCancel;
        @BindView(R.id.btn_save)
        Button btnSave;
        @BindView(R.id.edt_imei)
        EditText edt_imei;

        ViewHolderHuXiDialog(View view) {
            ButterKnife.bind(this, view);
        }
    }


    private void initTools() {
        myMessageManager = new MessageManager().getInstance(getActivity());
        mBluetoothController = BluetoothController.getInstance(myApplication.dashuhdApplication);
        mBluetoothController.setOnGattNoneListener(new BluetoothController.onGattNoneListener() {
            @Override
            public void onGattNone() {
                if (devicelist.size() != 0) {
                    for (BluetoothDeviceBean bleDevice : devicelist) {
                        if (bleDevice.getName().equals(IMEI)) {
                            mBluetoothController.stopScanBLE();
                            mBluetoothController.connect(bleDevice);
                            break;
                        }
                    }
                }
            }
        });
        myMessageManager.setmBluetoothController(mBluetoothController);
        myMessageManager.setHolder(holder);                                            //MessageManager需要设置检测人信息
        if (null == bleVersionMsg) {
        } else {
            myMessageManager.setBleVersionmsg(bleVersionMsg);
        }


        myMessageManager.setmListener(new MessageManager.ReceiveCommandCodeListener() {

            @Override
            public void matchSuccess() {
                //成功连接
                buleToothIsConnect.getbuleToothIsConnect(getActivity().getResources().getString(R.string.ble_state_connect));
            }

            @Override
            public void needUpdateBleProgram(Boolean isNeedUpdate) {
                Log.e(TAG, "需要进行更新设备的程序: " + isNeedUpdate);

                if (isNeedUpdate) {//需要进行更新设备的程序
                    Toast.makeText(getActivity(), R.string.hint_updateprogram, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void sendDataFromBleDevice(final PefDataFromBleBean pefdata) {
                final PefDataFromBleBean mypefData = pefdata;
                getDataListener.getData("日期:" + mypefData.getDate() + "\r\n" + "Pef:" +
                        mypefData.getPefValue() + "\r\n" + "FVC:" + mypefData.getFvcValue() + "\r\n"
                        + "FEV1:" + mypefData.getFev1Value());

            }
        });
        mySearchBleDeviceTask = new SearchBleDeviceTask(mBluetoothController);
    }


    private Intent searchIntent;

    private void startSearchBle() {
        searchIntent = new Intent(getActivity(), MyBLEService.class);
        getActivity().startService(searchIntent);
        if (!mBluetoothController.initBLE()) {//手机不支持蓝牙
            Toast.makeText(getActivity(), R.string.ble_judgement_notsupport, Toast.LENGTH_SHORT).show();
            return;//手机不支持蓝牙
        }
        if (!BluetoothController.getInstance().isBleOpen()) {// 如果蓝牙还没有打开
            Toast.makeText(getActivity(), R.string.ble_state_ble_open, Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            mySearchBleDeviceTask.execute();
        } catch (Exception e) {
            return;
        }

    }

    public class SystemBleBroadcast extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                    switch (blueState) {
                        case BluetoothAdapter.STATE_TURNING_ON:
                            break;
                        case BluetoothAdapter.STATE_ON:
                            break;
                        case BluetoothAdapter.STATE_TURNING_OFF:
                            break;
                        case BluetoothAdapter.STATE_OFF:
                            break;
                        default:
                            break;
                    }
                    break;

                default:
                    break;
            }
        }
    }

    private GetDataListener getDataListener;
    private BuleToothIsConnect buleToothIsConnect;

    public static interface GetDataListener {
        void getData(String text);
    }

    public void setGetDataListener(GetDataListener getDataListener) {
        this.getDataListener = getDataListener;
    }


    public static interface BuleToothIsConnect {
        void getbuleToothIsConnect(String isConnect);
    }

    public void setBuleToothIsConnect(BuleToothIsConnect buleToothIsConnect) {
        this.buleToothIsConnect = buleToothIsConnect;
    }*/


}



