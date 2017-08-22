package com.returnlive.dashubiohd.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.returnlive.dashubiohd.bean.LoginBean;
import com.returnlive.dashubiohd.bean.UserListBean;
import com.returnlive.dashubiohd.bean.dbmanagerbean.LoginUserBean;
import com.returnlive.dashubiohd.constant.ErrorCode;
import com.returnlive.dashubiohd.constant.InterfaceUrl;
import com.returnlive.dashubiohd.db.DBManager;
import com.returnlive.dashubiohd.gson.GsonParsing;
import com.returnlive.dashubiohd.utils.NetUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

import static com.returnlive.dashubiohd.constant.InterfaceUrl.code;
import static com.returnlive.dashubiohd.constant.InterfaceUrl.t_session_code;
import static com.returnlive.dashubiohd.constant.InterfaceUrl.zSesson;

/**
 * 作者： 张梓彬
 * 日期： 2017/8/21 0021
 * 时间： 下午 5:43
 * 描述： 网络广播
 */
public class NetBroadcastReceiver extends BroadcastReceiver {
    private DBManager dbManager;
    private static final String TAG = "NetBroadcastReceiver";
    private Context context;
    private String sessonWithCode;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        if (NetUtil.isNetworkConnectionActive(context)) {
            dbManager = new DBManager(context);
            ArrayList<LoginUserBean> loginList = dbManager.searchLoginData();
            String phone = "";
            String pwds = "";
            for (int i = 0; i < loginList.size(); i++) {
                LoginUserBean loginUserBean = loginList.get(i);
                phone = loginUserBean.getName();
                pwds = loginUserBean.getPwds();
            }

            final String finalPhone = phone;
            final String finalPwds = pwds;
            equipmentLoginInterface(InterfaceUrl.LOGIN_URL, finalPhone, finalPwds);

        } else {
            Log.e(TAG, "无网络连接");
        }
    }

    //用户登录接口
    private void equipmentLoginInterface(String loginUrl, String finalPhone, String finalPwds) {
        OkHttpUtils.post().url(loginUrl)
                .addParams("phone", finalPhone)
                .addParams("password", finalPwds)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(TAG, "调用登录接口失败" + e.getMessage());
            }

            @Override
            public void onResponse(String result, int id) {
                Log.e(TAG, "登录接口返回值: " + result);
                LoginBean loginBean = null;
                try {
                    loginBean = GsonParsing.getMessage(result);
                    code = loginBean.getCode();
                } catch (Exception e) {
                    Log.e(TAG, "连接超时，或非法请求");
                }

                try {
                    t_session_code = GsonParsing.gsonLoginSesson(result);
                } catch (JSONException e) {
                    Toast.makeText(context, "t_session_code解析失败", Toast.LENGTH_SHORT).show();
                }
                sessonWithCode = zSesson + code + "/" + t_session_code + "/uid/" + code;
                doActyion();
            }
        });
    }


    private void doActyion() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //注册离线用户
                ArrayList<UserListBean.UserListDataBean> userList = dbManager.searchUserListWithMidNull();
                for (UserListBean.UserListDataBean userListDataBean : userList) {
                    String name = userListDataBean.getName();
                    String phone = userListDataBean.getPhone();
                    String card_id = userListDataBean.getCard_id();
                    postUserRegister(name, phone, card_id);
                }

                //清空用户数据库
                dbManager.clearUserTable();//清空小用户列表

                //获取用户数据库重新储存
                getUserListInterface(-1);


            }
        }).start();


    }

    //恢复网络注册数据库中的用户
    private void postUserRegister(String name, String phone, String card_id) {
        Log.e(TAG, "url: " + InterfaceUrl.USER_REGISTER_URL + sessonWithCode);
        OkHttpUtils.post().url(InterfaceUrl.USER_REGISTER_URL + sessonWithCode)
                .addParams("card_id", card_id)
                .addParams("phone", phone)
                .addParams("name", name)
                .addParams("sex", "0")
                .addParams("birth", "2017-08-22")
                .addParams("nation", "汉族")
                .addParams("province", "——选择省——")
                .addParams("city", "——选择市——")
                .addParams("district", "——选择区——")
                .addParams("phone_contacts", "待修改")
                .addParams("address", "")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(TAG, "离线注册失败: " + e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                Log.e(TAG, "离线注册成功: " + response);
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
                Log.e(TAG, "获取用户列表接口失败: " + e.getMessage());
            }

            @Override
            public void onResponse(String response, int id) {
                Log.e(TAG, "获取用户列表接口成功: " + response);
                UserListBean userListBean = null;
                if (response.indexOf(ErrorCode.SUCCESS) > 0) {
                    try {
                        userListBean = GsonParsing.getUserListMessage(response);
                        List<UserListBean.UserListDataBean> userList = userListBean.getData();
                        List<UserListBean.UserListDataBean> dbUserList = dbManager.searchUserList();
                        String mResult = "";
                        for (UserListBean.UserListDataBean userListDataBean : dbUserList) {
                            mResult = mResult + String.valueOf(userListDataBean.card_id);
                            mResult = mResult + "\n" + "------------------------------------------" + "\n";
                        }

                        for (int i = userList.size()-1; i >-1; i--) {
                            UserListBean.UserListDataBean users = userList.get(i);
                            String userName = users.getName();
                            String sex = users.getSex();
                            String userid = users.getId();
                            String card_id = users.getCard_id();
                            String phone = users.getPhone();
                            if (mResult.indexOf(card_id)!=-1){
                                //数据库有数据
                            }else {
                                dbManager.addUserData(userid,userName,sex,card_id,phone);
                            }
                        }


                    } catch (Exception e) {
                        Log.e(TAG, "获取用户列表接口解析失败: "+e.getMessage() );
                    }

                }

            }
        });
    }

}
