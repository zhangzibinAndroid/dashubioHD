package com.returnlive.dashubiohd.fragment.home;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.returnlive.dashubiohd.R;
import com.returnlive.dashubiohd.base.BaseFragment;

/**
 * 作者： 张梓彬
 * 日期： 2017/7/13 0013
 * 时间： 上午 10:18
 * 描述： 健康档案
 */
public class HealthArchivesFragment extends BaseFragment {


    public HealthArchivesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_health_archives, container, false);
    }

}
