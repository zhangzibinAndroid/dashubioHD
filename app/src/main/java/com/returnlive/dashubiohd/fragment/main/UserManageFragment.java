package com.returnlive.dashubiohd.fragment.main;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.returnlive.dashubiohd.R;
import com.returnlive.dashubiohd.base.BaseFragment;

/**
 * 作者： 张梓彬
 * 日期： 2017/7/13 0013
 * 时间： 上午 10:11
 * 描述： 用户管理
 */
public class UserManageFragment extends BaseFragment {


    public UserManageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_manage, container, false);
    }

}
