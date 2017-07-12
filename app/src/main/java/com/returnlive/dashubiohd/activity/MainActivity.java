package com.returnlive.dashubiohd.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.returnlive.dashubiohd.R;
import com.returnlive.dashubiohd.application.MyApplication;
import com.returnlive.dashubiohd.base.BaseActivity;
import com.returnlive.dashubiohd.view.RoundImageView;
import com.zhy.autolayout.AutoFrameLayout;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * 作者： 张梓彬
 * 日期： 2017/7/12 0012
 * 时间： 下午 6:09
 * 描述： 主页面
 */
public class MainActivity extends BaseActivity {


    @BindView(R.id.logo)
    ImageView logo;
    @BindView(R.id.img_user)
    RoundImageView imgUser;
    @BindView(R.id.tv_company_name)
    TextView tvCompanyName;
    @BindViews({R.id.tv_first, R.id.tv_user_register, R.id.tv_user_login, R.id.tv_user_manage, R.id.tv_warning_setting, R.id.tv_help})
    TextView[] tv_sel;
    @BindView(R.id.content)
    AutoFrameLayout content;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        getWindow().getAttributes().softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN;
        initView();
    }

    private void initView() {
        tv_sel[0].setSelected(true);

    }


    private long exitTime = 0;//点击2次返回，退出程序

    //点击两次退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {//两秒内再次点击返回则退出
                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                MyApplication.clearActivity();
                System.exit(0);
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @OnClick({R.id.tv_first, R.id.tv_user_register, R.id.tv_user_login, R.id.tv_user_manage, R.id.tv_warning_setting, R.id.tv_help})
    public void onViewClicked(View view) {
        for (int i = 0; i < tv_sel.length; i++) {
            tv_sel[i].setSelected(false);
        }

        switch (view.getId()) {
            case R.id.tv_first:
                tv_sel[0].setSelected(true);
                JumpActivity(HomeActivity.class);

                break;
            case R.id.tv_user_register:
                tv_sel[1].setSelected(true);

                break;
            case R.id.tv_user_login:
                tv_sel[2].setSelected(true);

                break;
            case R.id.tv_user_manage:
                tv_sel[3].setSelected(true);

                break;
            case R.id.tv_warning_setting:
                tv_sel[4].setSelected(true);

                break;
            case R.id.tv_help:
                tv_sel[5].setSelected(true);

                break;
        }
    }

    //为了避免影响选择器效果，和上面的监听分开
    @OnClick(R.id.tv_quit)
    public void onViewClicked() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
