package com.returnlive.dashubiohd.fragment.home;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.returnlive.dashubiohd.R;
import com.returnlive.dashubiohd.activity.HomeActivity;
import com.returnlive.dashubiohd.base.BaseFragment;
import com.returnlive.dashubiohd.constant.InterfaceUrl;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * 作者： 张梓彬
 * 日期： 2017/7/13 0013
 * 时间： 上午 10:18
 * 描述： 健康指导
 */
public class HealthGuideFragment extends BaseFragment {

    private static final String TAG = "HealthGuideFragment";
    public HealthGuideFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_health_guide, container, false);
        initView();
        getMessage("5");
        return view;
    }

    private void initView() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, "url: "+InterfaceUrl.HEALTH_GUIDE_URL+sessonWithCode+"/m_id/"+HomeActivity.mid );
                OkHttpUtils.get().url(InterfaceUrl.HEALTH_GUIDE_URL+sessonWithCode)
                        .addParams("m_id", HomeActivity.mid)
                        .build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG, "父: "+response );

                    }
                });
            }
        }).start();
    }



    private void getMessage(final String so_id){
        new Thread(new Runnable() {
            @Override
            public void run() {

                OkHttpUtils.post().url(InterfaceUrl.HEALTH_GUIDE_LIST_URL+sessonWithCode+"/m_id/"+HomeActivity.mid)
                        .addParams("so_id",so_id)
                        .build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e(TAG, "子: "+response );
                    }
                });
            }
        }).start();
    }



}
