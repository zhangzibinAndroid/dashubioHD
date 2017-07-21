package com.returnlive.dashubiohd.gson;

import com.google.gson.Gson;
import com.returnlive.dashubiohd.bean.CameraCardBean;
import com.returnlive.dashubiohd.bean.ErrorCodeBean;
import com.returnlive.dashubiohd.bean.HealthArchivesBean;
import com.returnlive.dashubiohd.bean.HealthReportBean;
import com.returnlive.dashubiohd.bean.HelpMessageBean;
import com.returnlive.dashubiohd.bean.LoginBean;
import com.returnlive.dashubiohd.bean.MainPageBean;
import com.returnlive.dashubiohd.bean.UserListBean;
import com.returnlive.dashubiohd.bean.UserLoginBean;
import com.returnlive.dashubiohd.bean.WarningBean;
import com.returnlive.dashubiohd.constant.InterfaceUrl;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 作者： 张梓彬
 * 日期： 2017/7/13 0013
 * 时间： 下午 4:06
 * 描述： gson解析json
 */

public class GsonParsing {
    //短信请求错误码解析
    public static ErrorCodeBean sendCodeError(String json) throws Exception{
        ErrorCodeBean result = GsonUtils.parseJsonWithGson(json, ErrorCodeBean.class);
        return result;
    }

    public static LoginBean getMessage(String json)throws Exception{
        LoginBean result = GsonUtils.parseJsonWithGson(json, LoginBean.class);
        return result;
    }

    public static String gsonLoginSesson(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        LoginBean user = new LoginBean();
        user.setT_session_3(jsonObject.getString(InterfaceUrl.zSesson+InterfaceUrl.code));
        return user.getT_session_3();
    }

    public static HelpMessageBean getHelpMessage(String json)throws Exception{
        HelpMessageBean result = GsonUtils.parseJsonWithGson(json, HelpMessageBean.class);
        return result;
    }

    public static WarningBean getWarningMessage(String json)throws Exception{
        WarningBean result = GsonUtils.parseJsonWithGson(json, WarningBean.class);
        return result;
    }

    public static UserListBean getUserListMessage(String json)throws Exception{
        UserListBean result = GsonUtils.parseJsonWithGson(json, UserListBean.class);
        return result;
    }

    public static CameraCardBean getCardMessageJson(String json) throws Exception{
        CameraCardBean result = GsonUtils.parseJsonWithGson(json, CameraCardBean.class);
        return result;
    }

    public static MainPageBean getMainPageMessageJson(String json) throws Exception{
        MainPageBean result = GsonUtils.parseJsonWithGson(json, MainPageBean.class);
        return result;
    }

    public static UserLoginBean getUserLoginMessageJson(String json) throws Exception{
        UserLoginBean result = GsonUtils.parseJsonWithGson(json, UserLoginBean.class);
        return result;
    }


    public static HealthReportBean getHealthReportMessageJson(String json) throws Exception{
        HealthReportBean result = GsonUtils.parseJsonWithGson(json, HealthReportBean.class);
        return result;
    }

    public static HealthArchivesBean getHealthArchivesMessageJson(String json) throws Exception{
        HealthArchivesBean result = GsonUtils.parseJsonWithGson(json, HealthArchivesBean.class);
        return result;
    }


}


class GsonUtils {
    //将Json数据解析成相应的映射对象
    public static <T> T parseJsonWithGson(String jsonData, Class<T> type) {
        Gson gson = new Gson();
        T result = gson.fromJson(jsonData, type);
        return result;
    }


}