package com.returnlive.dashubiohd.activity;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.returnlive.dashubiohd.R;
import com.returnlive.dashubiohd.application.MyApplication;
import com.returnlive.dashubiohd.base.BaseActivity;
import com.returnlive.dashubiohd.constant.InterfaceUrl;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 作者： 张梓彬
 * 日期： 2017/7/12 0012
 * 时间： 上午 10:04
 * 描述： 登录页面
 */
public class LoginActivity extends BaseActivity {
    private Unbinder unbinder;
    PopupWindow login_window = null, register_window = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        unbinder = ButterKnife.bind(this);
        MyApplication.addActivity(this);
    }

    private void showLoginWindow() {
        //登录
        View loginView = LayoutInflater.from(this).inflate(R.layout.dialog_login, null);
        login_window = new PopupWindow(loginView,
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT);
        ColorDrawable cd = new ColorDrawable(0x000000);
        login_window.setBackgroundDrawable(cd);
        login_window.setOutsideTouchable(true);
        login_window.setFocusable(true);
        //设置弹出窗体需要软键盘，
        login_window.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        login_window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        login_window.showAtLocation(loginView, Gravity.CENTER, 0, 0);
        final EditText et_account = (EditText) loginView.findViewById(R.id.et_account);
        final EditText et_password = (EditText) loginView.findViewById(R.id.et_password);
        Button btn_login = (Button) loginView.findViewById(R.id.btn_login);
        Button btn_reg = (Button) loginView.findViewById(R.id.btn_reg);
        ImageView cancle = (ImageView) loginView.findViewById(R.id.cancle);
        sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        String phone =  sharedPreferences.getString("phone", "");
        String password = sharedPreferences.getString("password", "");
        et_account.setText(phone);
        et_password.setText(password);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMessage("正在登录");
                progressDialog.show();
                //手机号
                final String name = et_account.getText().toString();
                if (name.equals("")) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.please_input_username), Toast.LENGTH_SHORT).show();
                    return;
                }

                //密码
                final String mPassword = et_password.getText().toString();
                if (mPassword.equals("")) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.please_input_password), Toast.LENGTH_SHORT).show();
                    return;
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        equipmentLoginInterface(InterfaceUrl.LOGIN_URL,name,mPassword);

                    }
                }).start();


            }
        });

        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_window.dismiss();
            }
        });

        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_window.dismiss();
                showRegisterWindow();
            }
        });

    }


    private void showRegisterWindow() {
        //注册
        View registerView = LayoutInflater.from(this).inflate(R.layout.dialog_register, null);
        register_window = new PopupWindow(registerView, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        ColorDrawable cd = new ColorDrawable(0x000000);
        register_window.setBackgroundDrawable(cd);
        register_window.setOutsideTouchable(true);
        register_window.setFocusable(true);
        register_window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        register_window.showAtLocation(registerView, Gravity.CENTER, 0, 0);
        ImageView cancle = (ImageView) registerView.findViewById(R.id.cancle);
        final EditText et_imei = (EditText) registerView.findViewById(R.id.et_imei);
        final EditText et_phone = (EditText) registerView.findViewById(R.id.et_phone);
        final EditText et_password = (EditText) registerView.findViewById(R.id.et_password);
        final EditText et_password_again = (EditText) registerView.findViewById(R.id.et_password_again);
        final EditText et_code = (EditText) registerView.findViewById(R.id.et_code);
        final Button btn_code = (Button) registerView.findViewById(R.id.btn_code);
        Button btn_reg = (Button) registerView.findViewById(R.id.btn_reg);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register_window.dismiss();
            }
        });

        btn_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String telephone = et_phone.getText().toString();
                if (telephone.equals("")) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.input_reg_telphone), Toast.LENGTH_SHORT).show();
                    return;
                }
                startTime(TIME, btn_code);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        verificationCodeInterface(InterfaceUrl.VERIFICATION_CODE, telephone);
                    }
                }).start();


            }
        });


        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMessage("正在注册");
                progressDialog.show();
                //设备串号
                final String serialNumber = et_imei.getText().toString();
                if (serialNumber.equals("")) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.please_enter_the_serial_number_of_equipment), Toast.LENGTH_SHORT).show();
                    return;
                }

                //手机号
                final String telephone = et_phone.getText().toString();
                if (telephone.equals("")) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.input_reg_telphone), Toast.LENGTH_SHORT).show();
                    return;
                }

                //密码
                final String password = et_password.getText().toString();
                if (password.equals("")) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.please_input_password), Toast.LENGTH_SHORT).show();
                    return;
                }

                //再次输入密码
                String confirmPassword = et_password_again.getText().toString();
                if (!confirmPassword.equals(password)) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.confirm_password_error), Toast.LENGTH_SHORT).show();
                    return;
                }


                //验证码
                final String verificationCode = et_code.getText().toString();
                if (verificationCode.equals("")) {
                    Toast.makeText(LoginActivity.this, getResources().getString(R.string.please_input_verification_code), Toast.LENGTH_SHORT).show();
                    return;
                }


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        equipmentReisterInterface(InterfaceUrl.EQUIPMENT_REGISTER, serialNumber, telephone, password, verificationCode);
                    }
                }).start();


            }
        });

    }

    @OnClick({R.id.tv_login, R.id.tv_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_login:
                showLoginWindow();
                break;
            case R.id.tv_register:
                showRegisterWindow();
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

}
