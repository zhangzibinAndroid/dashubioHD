package com.returnlive.dashubiohd.fragment.main;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.returnlive.dashubiohd.R;
import com.returnlive.dashubiohd.activity.HelpActivity;
import com.returnlive.dashubiohd.adapter.HelpAdapter;
import com.returnlive.dashubiohd.base.BaseFragment;
import com.returnlive.dashubiohd.bean.HelpMessageBean;
import com.returnlive.dashubiohd.constant.InterfaceUrl;
import com.returnlive.dashubiohd.gson.GsonParsing;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * 作者： 张梓彬
 * 日期： 2017/7/13 0013
 * 时间： 上午 10:13
 * 描述： 使用帮助
 */
public class HelpFragment extends BaseFragment {
    private static final String TAG = "HelpFragment";

    @BindView(R.id.lv_help)
    ListView lvHelp;
    private Unbinder unbinder;
    private HelpAdapter helpAdapter;

    public HelpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_help, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        helpAdapter = new HelpAdapter(getActivity());
        lvHelp.setAdapter(helpAdapter);
        lvHelp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String title = "";
                String content = "";
                for (int i = 0; i < helpAdapter.getHelpDataBeanList().size(); i++) {
                    HelpMessageBean.HelpDataBean bean = (HelpMessageBean.HelpDataBean) helpAdapter.getHelpDataBeanList().get(position);
                    content = bean.getContent();
                    title = bean.getTitle();
                }
                JumpActivityWithData(HelpActivity.class,title,content);
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                helpInterface();
            }
        }).start();
    }

    private void helpInterface() {
        OkHttpUtils.get().url(InterfaceUrl.HELP_URL + sessonWithCode).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                toastOnUi("获取信息失败，请检查网络");
            }

            @Override
            public void onResponse(String response, int id) {
                Message msg = new Message();
                msg.obj = response;
                helpHandler.sendMessage(msg);

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private Handler helpHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = (String) msg.obj;
            HelpMessageBean helpMessageBean = GsonParsing.getHelpMessage(result);
            helpAdapter.addDATA(helpMessageBean);
            helpAdapter.notifyDataSetChanged();
        }
    };
}
