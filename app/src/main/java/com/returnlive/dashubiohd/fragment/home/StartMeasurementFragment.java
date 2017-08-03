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

import com.klw.singleleadsdk.OnCallBack;
import com.klw.singleleadsdk.SingleLeadUtil;
import com.klw.singleleadsdk.ble.JPBleNormalData;
import com.klw.singleleadsdk.entity.Data;
import com.returnlive.dashubiohd.R;
import com.returnlive.dashubiohd.adapter.blueadapter.BlueAdapter;
import com.returnlive.dashubiohd.base.BaseFragment;
import com.returnlive.dashubiohd.view.EcgPathOne;
import com.returnlive.dashubiohd.view.EcgPathSecond;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

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
                break;
            case R.id.lay_respiratory_monitor:
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

    //多参数监测仪
    private void showMultiParameterMonitorDialog() {
        AlertDialog.Builder multiParameterMonitorDialog = new AlertDialog.Builder(getActivity());
        View view = View.inflate(getActivity(), R.layout.dialog_multi_parameter_monitor, null);
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


}
