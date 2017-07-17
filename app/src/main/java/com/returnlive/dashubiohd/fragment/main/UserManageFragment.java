package com.returnlive.dashubiohd.fragment.main;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.returnlive.dashubiohd.R;
import com.returnlive.dashubiohd.activity.HomeActivity;
import com.returnlive.dashubiohd.adapter.UserListAdapter;
import com.returnlive.dashubiohd.base.BaseFragment;
import com.returnlive.dashubiohd.bean.UserListBean;
import com.returnlive.dashubiohd.constant.InterfaceUrl;
import com.returnlive.dashubiohd.gson.GsonParsing;
import com.returnlive.dashubiohd.view.ViewFooter;
import com.returnlive.dashubiohd.view.ViewHeader;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * 作者： 张梓彬
 * 日期： 2017/7/13 0013
 * 时间： 上午 10:11
 * 描述： 用户管理
 */
public class UserManageFragment extends BaseFragment {


    @BindView(R.id.user_list_tv)
    TextView userListTv;
    @BindView(R.id.input_query_et)
    EditText inputQueryEt;
    @BindView(R.id.lv_user_list)
    ListView lvUserList;
    @BindView(R.id.xrefresh_user_list)
    XRefreshView xrefreshUserList;
    private Unbinder unbinder;
    private int page;
    private UserListAdapter userListAdapter;
    private static final String TAG = "UserManageFragment";
    private Handler refreshUpHandler, refreshdownHandler;
    private Runnable upRunable, downRunable;
    private int upIsRefresh = 1;
    private int downIsRefresh = 1;

    public UserManageFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_manage, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        upIsRefresh = 1;
        downIsRefresh = 1;
        page = 1;
        new Thread(new Runnable() {
            @Override
            public void run() {
                getUserListInterface(1);
            }
        }).start();

        initXRefresh();
        userListAdapter = new UserListAdapter(getActivity());
        lvUserList.setAdapter(userListAdapter);
        userListAdapter.setOnButtonClickListener(new UserListAdapter.OnButtonClickListener() {
            @Override
            public void onButtonClick(View view, String name) {
                JumpActivityWithData(HomeActivity.class, name);
            }
        });
    }

    protected void initXRefresh() {
        ViewHeader viewHeader = new ViewHeader(getActivity());
        xrefreshUserList.setCustomHeaderView(viewHeader);
        ViewFooter viewFooter = new ViewFooter(getActivity());
        xrefreshUserList.setCustomFooterView(viewFooter);
        // 设置是否可以上拉刷新
        xrefreshUserList.setPullLoadEnable(true);
        // 设置是否可以下拉刷新
        xrefreshUserList.setPullRefreshEnable(true);
        xrefreshUserList.setXRefreshViewListener(new XRefreshView.XRefreshViewListener() {
            @Override
            public void onRefresh() {
                downIsRefresh = 2;
                refreshdownHandler = new Handler();
                refreshdownHandler.postDelayed(downRunable = new Runnable() {
                    @Override
                    public void run() {
                        xrefreshUserList.stopRefresh();
                    }
                }, 1000);
            }

            @Override
            public void onRefresh(boolean isPullDown) {
                userListAdapter.removeAllDATA();
                page = 1;
                getUserListInterface(1);
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                upIsRefresh = 2;
                refreshUpHandler = new Handler();
                refreshUpHandler.postDelayed(upRunable = new Runnable() {
                    @Override
                    public void run() {
                        xrefreshUserList.stopLoadMore();
                    }
                }, 1000);
                page = page + 1;
                getUserListInterface(page);
            }

            @Override
            public void onRelease(float direction) {
            }

            @Override
            public void onHeaderMove(double headerMovePercent, int offsetY) {

            }
        });
    }


    //获取用户列表接口
    private void getUserListInterface(int page) {
        OkHttpUtils.get().url(InterfaceUrl.USER_MESSAGE_URL + sessonWithCode)
                .addParams("p", String.valueOf(page))
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                Message msg = new Message();
                msg.obj = response;
                userListHandler.sendMessage(msg);
            }
        });
    }


    //用户列表查询接口
    private void getUserListSearchInterface(String search) {
        OkHttpUtils.get().url(InterfaceUrl.USER_MESSAGE_URL + sessonWithCode)
                .addParams("search", search)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                Log.e(TAG, "onResponse: " + response);
                Message msg = new Message();
                msg.obj = response;
                searchUserHandler.sendMessage(msg);
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (upIsRefresh == 2) {
            refreshUpHandler.removeCallbacks(upRunable);
        }

        if (downIsRefresh == 2) {
            refreshdownHandler.removeCallbacks(downRunable);
        }

    }

    @OnClick(R.id.go_search)
    public void onViewClicked() {
        getActivity().getWindow().getAttributes().softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN;
        final String search = inputQueryEt.getText().toString();
        userListTv.setText("搜索： " + search);
        new Thread(new Runnable() {
            @Override
            public void run() {
                getUserListSearchInterface(search);
            }
        }).start();
    }


    private Handler userListHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            UserListBean userListBean = GsonParsing.getUserListMessage(result);
            List<UserListBean.UserListDataBean> userList = userListBean.getData();
            userListAdapter.addAllDataToMyadapterWithoutClean(userList);
            userListAdapter.notifyDataSetChanged();
        }
    };

    private Handler searchUserHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            UserListBean userListBean = GsonParsing.getUserListMessage(result);
            List<UserListBean.UserListDataBean> searchUserList = userListBean.getData();
            userListAdapter.addAllDataToMyadapter(searchUserList);
            userListAdapter.notifyDataSetChanged();
        }
    };
}
