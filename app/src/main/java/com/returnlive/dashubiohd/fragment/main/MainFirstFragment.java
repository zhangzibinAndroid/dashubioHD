package com.returnlive.dashubiohd.fragment.main;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.returnlive.dashubiohd.R;
import com.returnlive.dashubiohd.adapter.MainPageAdapter;
import com.returnlive.dashubiohd.base.BaseFragment;
import com.returnlive.dashubiohd.bean.ErrorCodeBean;
import com.returnlive.dashubiohd.bean.MainPageBean;
import com.returnlive.dashubiohd.constant.ErrorCode;
import com.returnlive.dashubiohd.constant.InterfaceUrl;
import com.returnlive.dashubiohd.gson.GsonParsing;
import com.returnlive.dashubiohd.view.ViewHeader;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

import static com.returnlive.dashubiohd.R.id.tv_time;

/**
 * 作者： 张梓彬
 * 日期： 2017/7/13 0013
 * 时间： 上午 10:08
 * 描述： 第一个页面的首页
 */
public class MainFirstFragment extends BaseFragment {


    @BindView(R.id.lv_message)
    RecyclerView lv_message;
    @BindView(R.id.xrefresh_message)
    XRefreshView xrefresh_message;
    @BindView(tv_time)
    TextView tvTime;

    private MainPageAdapter mainPageAdapter;
    private List<MainPageBean.MessageDataBean> data;
    private Unbinder unbinder;
    private Handler downHandler;
    private Runnable downRunable;
    private boolean isdown = false;

    public MainFirstFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_first, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        initXRefresh();
        return view;
    }

    private void initView() {
        isdown = false;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        lv_message.setLayoutManager(linearLayoutManager);

        new Thread(new Runnable() {
            @Override
            public void run() {
                getMessageInterface();
            }
        }).start();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        tvTime.setText(df.format(calendar.getTime()) + " " + getString(R.string.check_list));
    }

    private void getMessageInterface() {
        OkHttpUtils.get().url(InterfaceUrl.MESSAGE_URL + sessonWithCode)
                .addParams("m_id", "").build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                toastOnUi("获取列表异常，请检查网络");

            }

            @Override
            public void onResponse(String response, int id) {
                Message msg = new Message();
                msg.obj = response;
                mainPageMessageHandler.sendMessage(msg);
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

    private Handler mainPageMessageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            if (result.indexOf(ErrorCode.SUCCESS) > 0) {
                try {
                    MainPageBean mainPageBean = GsonParsing.getMainPageMessageJson(result);
                    data = mainPageBean.getData();
                    mainPageAdapter = new MainPageAdapter(data);
                    lv_message.setAdapter(mainPageAdapter);
                    mainPageAdapter.setOnLookingClickListener(new MainPageAdapter.OnLookingClickListener() {
                        @Override
                        public void lookingClick(View v, int position, AutoLinearLayout resultLayout) {

                            if (resultLayout.isSelected()==false){
                                resultLayout.setVisibility(View.VISIBLE);
                                resultLayout.setSelected(true);
                            }else {
                                resultLayout.setVisibility(View.GONE);
                                resultLayout.setSelected(false);
                            }
                        }
                    });
                    mainPageAdapter.notifyDataSetChanged();
                } catch (Exception e) {
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


    private void initXRefresh() {
        ViewHeader viewHeader = new ViewHeader(getActivity());
        xrefresh_message.setCustomHeaderView(viewHeader);
        // 设置是否可以上拉刷新
        xrefresh_message.setPullLoadEnable(false);
        // 设置是否可以下拉刷新
        xrefresh_message.setPullRefreshEnable(true);
        xrefresh_message.setXRefreshViewListener(new XRefreshView.XRefreshViewListener() {
            @Override
            public void onRefresh() {
                isdown = true;
                downHandler = new Handler();
                downHandler.postDelayed(downRunable = new Runnable() {
                    @Override
                    public void run() {
                        xrefresh_message.stopRefresh();
                    }
                }, 1500);
            }

            @Override
            public void onRefresh(boolean isPullDown) {
                if (isPullDown) {
                    getMessageInterface();
                }
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        xrefresh_message.stopLoadMore();
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
}
