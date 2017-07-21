package com.returnlive.dashubiohd.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.returnlive.dashubiohd.R;
import com.returnlive.dashubiohd.base.BaseActivity;
import com.returnlive.dashubiohd.fragment.home.HealthArchivesFragment;
import com.returnlive.dashubiohd.fragment.home.HealthGuideFragment;
import com.returnlive.dashubiohd.fragment.home.HealthReportFragment;
import com.returnlive.dashubiohd.fragment.home.HistoryDataFragment;
import com.returnlive.dashubiohd.fragment.home.HomeFristFragment;
import com.returnlive.dashubiohd.fragment.home.StartMeasurementFragment;
import com.zhy.autolayout.AutoFrameLayout;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class HomeActivity extends BaseActivity {
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_return)
    TextView tvReturn;

    @BindViews({R.id.tv_first_home, R.id.tv_start_measurement, R.id.tv_history_data, R.id.tv_health_report, R.id.tv_health_archives, R.id.tv_health_guide})
    TextView[] tv_sel;

    @BindView(R.id.home_content)
    AutoFrameLayout content;
    private Unbinder unbinder;
    public static String userName = "";
    public static String mid = "";
    private static final String TAG = "HomeActivity";
    private HomeFristFragment homeFristFragment;
    private StartMeasurementFragment startMeasurementFragment;
    private HistoryDataFragment historyDataFragment;
    private HealthReportFragment healthReportFragment;
    private HealthArchivesFragment healthArchivesFragment;
    private HealthGuideFragment healthGuideFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        unbinder = ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        homeFristFragment = new HomeFristFragment();
        startMeasurementFragment = new StartMeasurementFragment();
        historyDataFragment = new HistoryDataFragment();
        healthReportFragment = new HealthReportFragment();
        healthArchivesFragment = new HealthArchivesFragment();
        healthGuideFragment = new HealthGuideFragment();
        tv_sel[0].setSelected(true);
        setReplaceFragment(R.id.home_content,homeFristFragment);
        Intent intent = getIntent();
        userName = intent.getStringExtra("userName");
        mid = intent.getStringExtra("mid");
        tvUserName.setText(userName);

    }

    @OnClick({R.id.tv_first_home, R.id.tv_start_measurement, R.id.tv_history_data, R.id.tv_health_report, R.id.tv_health_archives, R.id.tv_health_guide})
    public void onViewClicked(View view) {
        for (int i = 0; i < tv_sel.length; i++) {
            tv_sel[i].setSelected(false);
        }

        switch (view.getId()) {
            case R.id.tv_first_home:
                tv_sel[0].setSelected(true);
                setReplaceFragment(R.id.home_content,homeFristFragment);
                break;
            case R.id.tv_start_measurement:
                tv_sel[1].setSelected(true);
                setReplaceFragment(R.id.home_content,startMeasurementFragment);
                break;
            case R.id.tv_history_data:
                tv_sel[2].setSelected(true);
                setReplaceFragment(R.id.home_content,historyDataFragment);
                break;
            case R.id.tv_health_report:
                tv_sel[3].setSelected(true);
                setReplaceFragment(R.id.home_content,healthReportFragment);
                break;
            case R.id.tv_health_archives:
                tv_sel[4].setSelected(true);
                setReplaceFragment(R.id.home_content,healthArchivesFragment);
                break;
            case R.id.tv_health_guide:
                tv_sel[5].setSelected(true);
                setReplaceFragment(R.id.home_content,healthGuideFragment);
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick(R.id.tv_return)
    public void onViewClicked() {
        finish();
    }
}
