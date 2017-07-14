package com.returnlive.dashubiohd.base;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import static com.returnlive.dashubiohd.constant.InterfaceUrl.code;
import static com.returnlive.dashubiohd.constant.InterfaceUrl.t_session_code;
import static com.returnlive.dashubiohd.constant.InterfaceUrl.zSesson;

/**
 * 作者： 张梓彬
 * 日期： 2017/7/14 0014
 * 时间： 上午 11:35
 * 描述： 基础的Fragment封装
 */

public class BaseFragment extends Fragment {
    protected View view;
    protected String sessonWithCode = zSesson+code+"/"+t_session_code+"/uid/"+code;

    protected void toastOnUi(final String text) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void JumpActivity(Class<?> cls) {
        Intent intent = new Intent(getActivity(), cls);
        startActivity(intent);
    }

    protected void JumpActivityWithData(Class<?> cls,String title,String content) {
        Intent intent = new Intent(getActivity(), cls);
        intent.putExtra("helpTitle",title);
        intent.putExtra("helpContent",content);
        startActivity(intent);
    }
}
