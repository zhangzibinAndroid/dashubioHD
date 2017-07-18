package com.returnlive.dashubiohd.fragment.main;


import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.returnlive.dashubiohd.R;
import com.returnlive.dashubiohd.base.BaseFragment;
import com.returnlive.dashubiohd.bean.CameraCardBean;
import com.returnlive.dashubiohd.constant.CityArray;
import com.returnlive.dashubiohd.gson.GsonParsing;
import com.returnlive.dashubiohd.utils.CameraManager;
import com.returnlive.dashubiohd.utils.FileUtil;
import com.returnlive.dashubiohd.utils.HttpUtil;
import com.returnlive.dashubiohd.utils.NetUtil;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.returnlive.dashubiohd.constant.CityArray.city;
import static com.returnlive.dashubiohd.constant.CityArray.county;

/**
 * 作者： 张梓彬
 * 日期： 2017/7/13 0013
 * 时间： 上午 10:09
 * 描述： 用户注册
 */
public class UserRegisterFragment extends BaseFragment implements Callback {
    protected static final String TAG = "TAG";
    private CameraManager mCameraManager;
    private SurfaceView mSurfaceView;
    private ProgressBar pb;
    private ImageButton mShutter;
    private SurfaceHolder mSurfaceHolder;
    private String flashModel = Parameters.FLASH_MODE_OFF;
    private byte[] jpegData = null;
    private AlertDialog.Builder registerDialog;
    private AlertDialog dialog;
    private ArrayAdapter<String> provinceAdapter = null;  //省级适配器
    private ArrayAdapter<String> cityAdapter = null;    //地级适配器
    private ArrayAdapter<String> countyAdapter = null;    //县级适配器
    private static int provincePosition = 3;
    public UserRegisterFragment() {

    }

    private Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(getActivity(), "拍照失败", Toast.LENGTH_SHORT).show();
                    mCameraManager.initPreView();
                    break;
                case 1:
                    jpegData = (byte[]) msg.obj;
                    if (jpegData != null && jpegData.length > 0) {
                        pb.setVisibility(View.VISIBLE);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if ((jpegData.length > (1000 * 1024 * 5))) {
                                    mHandler.sendMessage(mHandler.obtainMessage(3, getResources().getString(R.string.photo_too_lage)));
                                    return;
                                }
                                String result = null;
                                boolean isavilable = NetUtil.isNetworkConnectionActive(getActivity());
                                if (isavilable) {
                                    result = Scan(UserLoginFragment.action, jpegData, "jpg");
                                    Log.d(TAG, "" + result);

                                    if (result.equals("-2")) {
                                        result = "连接超时！";
                                        mHandler.sendMessage(mHandler.obtainMessage(3, result));
                                    } else if (HttpUtil.connFail.equals(result)) {
                                        mHandler.sendMessage(mHandler.obtainMessage(3, result));
                                    } else {
                                        mHandler.sendMessage(mHandler.obtainMessage(4, result));
                                    }
                                } else {
                                    mHandler.sendMessage(mHandler.obtainMessage(3, "无网络，请确定网络是否连接!"));
                                }
                            }
                        }).start();
                    }
                    break;
                case 3:
                    pb.setVisibility(View.GONE);
                    String str = msg.obj + "";
                    Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
                    mCameraManager.initPreView();
                    mShutter.setEnabled(true);
                    break;
                case 4:
                    mShutter.setEnabled(true);
                    pb.setVisibility(View.GONE);
                    String result = msg.obj + "";
                    Log.e(TAG, "handleMessage: " + result);
                    CameraCardBean cameraCardBean = null;
                    try {
                        cameraCardBean = GsonParsing.getCardMessageJson(result);
                        CameraCardBean.DataBean dataBean = cameraCardBean.getData();
                        CameraCardBean.DataBean.ItemBean itemBean = dataBean.getItem();
                        Log.e(TAG, "handleMessage: " + itemBean.getName());
                        Toast.makeText(getActivity(), "操作成功", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "身份证识别失败", Toast.LENGTH_SHORT).show();
                    }

                    break;
                case 5:
                    String filePath = msg.obj + "";
                    byte[] data = FileUtil.getByteFromPath(filePath);
                    if (data != null && data.length > 0) {
                        mHandler.sendMessage(mHandler.obtainMessage(1, data));
                    } else {
                        mHandler.sendMessage(mHandler.obtainMessage(0));
                    }
                    break;
                case 6:
                    Toast.makeText(getActivity(), "请插入存储卡！", Toast.LENGTH_SHORT).show();
                    mCameraManager.initPreView();
                    break;
            }
        }

        ;
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_register, container, false);
        mCameraManager = new CameraManager(getActivity(), mHandler);
        initViews(view);

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(getActivity(), "请插入存储卡", Toast.LENGTH_LONG).show();
        }

        File dir = new File(CameraManager.strDir);
        if (!dir.exists()) {
            dir.mkdir();
        }
        return view;
    }


    private void initViews(View view) {
        pb = (ProgressBar) view.findViewById(R.id.reco_recognize_bar);
        mSurfaceView = (SurfaceView) view.findViewById(R.id.camera_preview);
        mShutter = (ImageButton) view.findViewById(R.id.camera_shutter);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mShutter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mCameraManager.requestFocuse();
                mShutter.setEnabled(false);
            }
        });
        Button btn_register = (Button) view.findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCameraManager.openCamera(mSurfaceHolder);
            if (flashModel == null || !mCameraManager.isSupportFlash(flashModel)) {
                flashModel = mCameraManager.getDefaultFlashMode();
            }
            mCameraManager.setCameraFlashMode(flashModel);
        } catch (RuntimeException e) {
            Toast.makeText(getActivity(), R.string.camera_open_error, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(getActivity(), R.string.camera_open_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (width > height) {
            mCameraManager.setPreviewSize(width, height);
        } else {
            mCameraManager.setPreviewSize(height, width);
        }
        mCameraManager.initPreView();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCameraManager.closeCamera();
    }

    public static String Scan(String type, byte[] file, String ext) {
        String xml = HttpUtil.getSendXML(type, ext);
        return HttpUtil.send(xml, file);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    private void showDialog() {
        registerDialog = new AlertDialog.Builder(getActivity());
        View dialogView = View.inflate(getActivity(), R.layout.dialog_user_register, null);
        final ViewHolder viewHolder = new ViewHolder(dialogView);
        //绑定适配器和值
        provinceAdapter = new ArrayAdapter<String>(getActivity(),R.layout.item_city_text_select, CityArray.province);
        viewHolder.tvProvince.setAdapter(provinceAdapter);
        viewHolder.tvProvince.setSelection(0, true);  //设置默认选中项，此处为默认选中第1个值

        cityAdapter = new ArrayAdapter<String>(getActivity(),R.layout.item_city_text_select, city[0]);
        viewHolder.tvCity.setAdapter(cityAdapter);
        viewHolder.tvCity.setSelection(0, true);  //默认选中第0个

        countyAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item_city_text_select, county[0][0]);
        viewHolder.tvDistrict.setAdapter(countyAdapter);
        viewHolder.tvDistrict.setSelection(0, true);

        viewHolder.tvProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cityAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item_city_text_select, city[position]);
                viewHolder.tvCity.setAdapter(cityAdapter);
                provincePosition = position;    //记录当前省级序号，留给下面修改县级适配器时用
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        viewHolder.tvCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                countyAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item_city_text_select, county[provincePosition][position]);
                viewHolder.tvDistrict.setAdapter(countyAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        viewHolder.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = viewHolder.dialogTvUserName.getText().toString();
                String sex = viewHolder.dialogTvUserSex.getText().toString();
                String folk = viewHolder.dialogTvUserFolk.getText().toString();
                String birth = viewHolder.dialogTvUserBirth.getText().toString();
                String province = viewHolder.tvProvince.getSelectedItem().toString();
                String city = viewHolder.tvCity.getSelectedItem().toString();
                String district = viewHolder.tvDistrict.getSelectedItem().toString();
                String address = viewHolder.dialogTvUserAddress.getText().toString();
                String id_number = viewHolder.dialogTvUserIdNumber.getText().toString();
                String phone_number = viewHolder.dialogTvUserPhoneNumber.getText().toString();
                String user_Frequent_contacts = viewHolder.dialogTvUserFrequentContacts.getText().toString();
                if (name.equals("")){
                    Toast.makeText(getActivity(), getResources().getString(R.string.username_cannot_empty), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (id_number.equals("")){
                    Toast.makeText(getActivity(), getResources().getString(R.string.user_id_card_cannot_empty), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (phone_number.equals("")){
                    Toast.makeText(getActivity(), getResources().getString(R.string.phone_cannot_empty), Toast.LENGTH_SHORT).show();
                    return;
                }



            }
        });
        viewHolder.btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        registerDialog.setView(dialogView);
        dialog = registerDialog.show();
        getActivity().getWindow().getAttributes().softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN;

    }


    static class ViewHolder {
        @BindView(R.id.dialog_tv_user_name)
        EditText dialogTvUserName;
        @BindView(R.id.dialog_tv_user_sex)
        TextView dialogTvUserSex;
        @BindView(R.id.dialog_tv_user_folk)
        TextView dialogTvUserFolk;
        @BindView(R.id.dialog_tv_user_birth)
        TextView dialogTvUserBirth;
        @BindView(R.id.tv_province)
        Spinner tvProvince;
        @BindView(R.id.tv_city)
        Spinner tvCity;
        @BindView(R.id.tv_district)
        Spinner tvDistrict;
        @BindView(R.id.dialog_tv_user_address)
        EditText dialogTvUserAddress;
        @BindView(R.id.dialog_tv_user_id_number)
        EditText dialogTvUserIdNumber;
        @BindView(R.id.dialog_tv_user_phone_number)
        EditText dialogTvUserPhoneNumber;
        @BindView(R.id.dialog_tv_user_Frequent_contacts)
        EditText dialogTvUserFrequentContacts;
        @BindView(R.id.btn_register)
        Button btnRegister;
        @BindView(R.id.btn_cancle)
        Button btnCancle;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
