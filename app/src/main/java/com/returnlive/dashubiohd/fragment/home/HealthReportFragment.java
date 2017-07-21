package com.returnlive.dashubiohd.fragment.home;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.returnlive.dashubiohd.R;
import com.returnlive.dashubiohd.activity.HomeActivity;
import com.returnlive.dashubiohd.adapter.HealthReportAdapter;
import com.returnlive.dashubiohd.base.BaseFragment;
import com.returnlive.dashubiohd.bean.ErrorCodeBean;
import com.returnlive.dashubiohd.bean.HealthReportBean;
import com.returnlive.dashubiohd.constant.ErrorCode;
import com.returnlive.dashubiohd.constant.InterfaceUrl;
import com.returnlive.dashubiohd.gson.GsonParsing;
import com.returnlive.dashubiohd.view.ViewHeader;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * 作者： 张梓彬
 * 日期： 2017/7/13 0013
 * 时间： 上午 10:17
 * 描述： 健康报告
 */
public class HealthReportFragment extends BaseFragment {


    @BindView(R.id.tv_single)
    TextView tvSingle;
    @BindView(R.id.tv_comprehensive)
    TextView tvComprehensive;
    @BindView(R.id.lv_health_report)
    ExpandableListView lvHealthReport;
    @BindView(R.id.xrefresh_health_report)
    XRefreshView xrefreshHealthReport;
    private Unbinder unbinder;
    private boolean isdown = false;
    private Handler downHandler;
    private Runnable downRunable;
    private static final String TAG = "HealthReportFragment";
    private HealthReportAdapter healthReportAdapter;
    public HealthReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_health_report, container, false);
        unbinder = ButterKnife.bind(this, view);
        initRefresh();
        initView();
        return view;
    }

    private void initView() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                healthReportInterface();
            }
        }).start();

    }

    private void healthReportInterface() {
        OkHttpUtils.get().url(InterfaceUrl.HEALTH_REPORT_URL + sessonWithCode)
                .addParams("m_id", HomeActivity.mid)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                toastOnUi(getResources().getString(R.string.network_exception_please_try_again_later));
            }

            @Override
            public void onResponse(String response, int id) {
                Message msg = new Message();
                msg.obj = response;
                healthReportHandler.sendMessage(msg);
            }
        });
    }

    private void initRefresh() {
        isdown = false;
        ViewHeader viewHeader = new ViewHeader(getActivity());
        xrefreshHealthReport.setCustomHeaderView(viewHeader);
        // 设置是否可以上拉刷新
        xrefreshHealthReport.setPullLoadEnable(false);
        // 设置是否可以下拉刷新
        xrefreshHealthReport.setPullRefreshEnable(true);
        xrefreshHealthReport.setXRefreshViewListener(new XRefreshView.XRefreshViewListener() {
            @Override
            public void onRefresh() {
                isdown = true;
                downHandler = new Handler();
                downHandler.postDelayed(downRunable = new Runnable() {
                    @Override
                    public void run() {
                        xrefreshHealthReport.stopRefresh();
                    }
                }, 1500);
            }

            @Override
            public void onRefresh(boolean isPullDown) {
                if (isPullDown) {
                    //加载
                }
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        xrefreshHealthReport.stopLoadMore();
                    }
                }, 1500);
            }

            @Override
            public void onRelease(float direction) {

            }

            @Override
            public void onHeaderMove(double headerMovePercent, int offsetY) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (isdown) {
            downHandler.removeCallbacks(downRunable);
        }
    }

    @OnClick({R.id.tv_single, R.id.tv_comprehensive})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_single:
                tvSingle.setBackground(getResources().getDrawable(R.drawable.textbg_shape_border_left_all));
                tvComprehensive.setBackground(getResources().getDrawable(R.drawable.textbg_shape_border_right));
                tvSingle.setTextColor(getResources().getColor(R.color.white));
                tvComprehensive.setTextColor(getResources().getColor(R.color.black));
                break;
            case R.id.tv_comprehensive:
                tvSingle.setBackground(getResources().getDrawable(R.drawable.textbg_shape_border_left));
                tvComprehensive.setBackground(getResources().getDrawable(R.drawable.textbg_shape_border_right_all));
                tvSingle.setTextColor(getResources().getColor(R.color.black));
                tvComprehensive.setTextColor(getResources().getColor(R.color.white));
                break;
        }
    }


    private Handler healthReportHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            if (result.indexOf(ErrorCode.SUCCESS) > 0) {
                try {
                    HealthReportBean healthReportBean = GsonParsing.getHealthReportMessageJson(result);
                    List<HealthReportBean.HealthDataBean> groupList = healthReportBean.getData();
                    String[] parentList = new String[groupList.size()];
                    Map<String, ArrayList<String>> childernDataset = new HashMap<>();
                    List<String> childenList = new ArrayList<>();

                    for (int i = 0; i < groupList.size(); i++) {
                        HealthReportBean.HealthDataBean bean= groupList.get(i);
                        parentList[i] = bean.getYear();
                        List<HealthReportBean.HealthDataBean.FatherDetBean> childrenBean = bean.getDet();
                        for (int j = 0; j < childrenBean.size(); j++) {
                            HealthReportBean.HealthDataBean.FatherDetBean monthBean = childrenBean.get(j);
                            childenList.add(monthBean.getMonth());
                        }
                        childernDataset.put("children"+i,new ArrayList<String>(childenList));
                        childenList.clear();

                    }

                    healthReportAdapter = new HealthReportAdapter(getActivity(),parentList,childernDataset);
                    lvHealthReport.setAdapter(healthReportAdapter);
                    healthReportAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    Log.e(TAG, "e: "+e );
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_timeout_or_illegal_request), Toast.LENGTH_SHORT).show();
                }

            } else {
                //解析
                ErrorCodeBean errorCodeBean = null;
                try {
                    errorCodeBean = GsonParsing.sendCodeError(result);
                    judge(errorCodeBean.getCode() + "");
                } catch (Exception e) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.connection_timeout_or_illegal_request), Toast.LENGTH_SHORT).show();
                }
            }


        }
    };
}
