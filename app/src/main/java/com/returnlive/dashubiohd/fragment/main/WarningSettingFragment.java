package com.returnlive.dashubiohd.fragment.main;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andview.refreshview.XRefreshView;
import com.returnlive.dashubiohd.R;
import com.returnlive.dashubiohd.adapter.WarningAdapter;
import com.returnlive.dashubiohd.base.BaseFragment;
import com.returnlive.dashubiohd.bean.WarningBean;
import com.returnlive.dashubiohd.constant.InterfaceUrl;
import com.returnlive.dashubiohd.gson.GsonParsing;
import com.returnlive.dashubiohd.view.ViewHeader;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * 作者： 张梓彬
 * 日期： 2017/7/13 0013
 * 时间： 上午 10:12
 * 描述： 预警设置
 */
public class WarningSettingFragment extends BaseFragment {

    private static final String TAG = "WarningSettingFragment";
    @BindView(R.id.lv_warning)
    RecyclerView lvWarning;
    @BindView(R.id.xrefresh_warning)
    XRefreshView xrefreshWarning;
    Unbinder unbinder;
    private WarningAdapter warningAdapter;
    private List<WarningBean.WarningDataBean> dataList;

    public WarningSettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_warning_setting, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lvWarning.setLayoutManager(linearLayoutManager);
        initXRefreshWarning();
        new Thread(new Runnable() {
            @Override
            public void run() {
                warningInterface();
            }
        }).start();


    }

    private void warningInterface() {
        OkHttpUtils.get().url(InterfaceUrl.WARNING_URL+sessonWithCode).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                toastOnUi("获取预警列表异常，请检查网络");
            }

            @Override
            public void onResponse(String response, int id) {
                Message msg = new Message();
                msg.obj = response;
                warningHandler.sendMessage(msg);
            }
        });
    }

    private void initXRefreshWarning() {
        ViewHeader viewHeader = new ViewHeader(getActivity());
        xrefreshWarning.setCustomHeaderView(viewHeader);
        // 设置是否可以上拉刷新
        xrefreshWarning.setPullLoadEnable(false);
        // 设置是否可以下拉刷新
        xrefreshWarning.setPullRefreshEnable(true);
        //设置是否可以自动刷新
        xrefreshWarning.setAutoRefresh(false);

        xrefreshWarning.setXRefreshViewListener(new XRefreshView.XRefreshViewListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        xrefreshWarning.stopRefresh();
                    }
                }, 1500);
            }

            @Override
            public void onRefresh(boolean isPullDown) {

            }

            @Override
            public void onLoadMore(boolean isSilence) {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        xrefreshWarning.stopLoadMore();
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
    }


    private Handler warningHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            WarningBean bean = GsonParsing.getWarningMessage(result);
            dataList = bean.getData();
            warningAdapter = new WarningAdapter(dataList);
            lvWarning.setAdapter(warningAdapter);
            warningAdapter.notifyDataSetChanged();
        }
    };
}
