package com.returnlive.dashubiohd.constant;

/**
 * Created by 张梓彬 on 2017/7/13 0013.
 */

public class InterfaceUrl {
    public static String zSesson = "t_session_";
    public static String code = "";
    public static String t_session_code = "";
    public static final String BASE_URL = "http://dashubio.cn/Mobile/";
    public static final String EQUIPMENT_REGISTER = BASE_URL+"Login/register";//设备注册
    public static final String VERIFICATION_CODE= BASE_URL+"Login/sendCode";//短信验证码
    public static final String LOGIN_URL= BASE_URL+"Login/login";//登录接口
    public static final String HELP_URL= BASE_URL+"Index/help/";//帮助接口需要添加sesson和code
    public static final String WARNING_URL= BASE_URL+"Index/hwarning/";//预警设置接口需要添加sesson和code
    public static final String USER_MESSAGE_URL= BASE_URL+"Ulogin/userlist/";//用户列表接口需要添加sesson和code
    public static final String USER_LOGIN_URL= BASE_URL+"Ulogin/userlogin/";//用户登录接口需要添加sesson和code
    public static final String MESSAGE_URL= BASE_URL+"Testing/tests/";//获取检测数据（首页）接口需要添加sesson和code
    public static final String USER_REGISTER_URL= BASE_URL+"Ulogin/register/";//用户登录接口需要添加sesson和code
    public static final String HISTORY_DATA_URL= BASE_URL+"Testing/history/";//历史数据接口需要添加sesson和code
    public static final String HEALTH_REPORT_URL= BASE_URL+"Testing/reportlist/";//健康报告接口需要添加sesson和code
    public static final String HEALTH_ARCHIVES_URL= BASE_URL+"Ulogin/perinfo/";//健康档案接口需要添加sesson和code




}
