package com.returnlive.dashubiohd.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.returnlive.dashubiohd.bean.UserListBean;
import com.returnlive.dashubiohd.bean.dbmanagerbean.LoginUserBean;

import java.util.ArrayList;

/**
 * 作者： 张梓彬
 * 日期： 2017/8/21 0021
 * 时间： 上午 10:34
 * 描述： DBManager是建立在DBHelper之上，封装了常用的业务方法
 */

public class DBManager {
    private DBHelper helper;
    private SQLiteDatabase db;

    public DBManager(Context context) {
        helper = new DBHelper(context);
        db = helper.getWritableDatabase();
    }

    //向login_table表中插入设备用户名和密码
    public void addLoginUser(String name, String pwds) {
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("pwds", pwds);
        db.insert(DBHelper.LOGIN_TABLE_NAME, null, cv);
    }


    //通过用户名密码查询设备登录表
    public ArrayList<LoginUserBean> searchLoginData(final String name, final String pwd) {
        String sql = "SELECT * FROM " + DBHelper.LOGIN_TABLE_NAME + " WHERE name =" + "'" + name + "'" + "and pwds =" + "'" + pwd + "'";
        return ExecSQLForLoginData(sql);
    }

    //无条件查询设备登录表
    public ArrayList<LoginUserBean> searchLoginData() {
        String sql = "SELECT * FROM " + DBHelper.LOGIN_TABLE_NAME;
        return ExecSQLForLoginData(sql);
    }


    //清空设备登录表
    public void clearLoginTable() {
        ExecSQL("DELETE FROM "+DBHelper.LOGIN_TABLE_NAME);
    }

    //搜寻设备登录
    private ArrayList<LoginUserBean> ExecSQLForLoginData(String sql) {
        ArrayList<LoginUserBean> list = new ArrayList<>();
        Cursor c = ExecSQLForCursor(sql);
        while (c.moveToNext()) {
            LoginUserBean loginUserBean = new LoginUserBean();
            loginUserBean._id = c.getInt(c.getColumnIndex("_id"));
            loginUserBean.name = c.getString(c.getColumnIndex("name"));
            loginUserBean.pwds = c.getString(c.getColumnIndex("pwds"));
            list.add(loginUserBean);
        }
        c.close();
        return list;
    }

    //向user_table表中插入小用户数据
    public void addUserData(String mid,String name,String sex,String card_no,String phone) {
        ContentValues cv = new ContentValues();
        cv.put("mid", mid);
        cv.put("name", name);
        cv.put("sex", sex);
        cv.put("card_no", card_no);
        cv.put("phone", phone);
        db.insert(DBHelper.USER_TABLE_NAME, null, cv);
    }

    //搜寻小用户
    public ArrayList<UserListBean.UserListDataBean> searchUserList() {
        String sql = "SELECT * FROM "+DBHelper.USER_TABLE_NAME;
        return ExecSQLForUserList(sql);
    }

    public ArrayList<UserListBean.UserListDataBean> searchUserListWithMidNull() {
        String sql = "SELECT * FROM "+DBHelper.USER_TABLE_NAME+" WHERE mid ISNULL";
        return ExecSQLForMidNull(sql);
    }

    public void clearUserTable() {
        ExecSQL("DELETE FROM "+DBHelper.USER_TABLE_NAME);
    }


    //搜寻小用户
    private ArrayList<UserListBean.UserListDataBean> ExecSQLForUserList(String sql) {
        ArrayList<UserListBean.UserListDataBean> list = new ArrayList<UserListBean.UserListDataBean>();
        Cursor c = ExecSQLForCursor(sql);
        while (c.moveToNext()) {
            UserListBean.UserListDataBean userListDataBean = new UserListBean.UserListDataBean();
            userListDataBean.card_id = c.getString(c.getColumnIndex("card_no"));
            userListDataBean.id = c.getString(c.getColumnIndex("mid"));
            userListDataBean.sex = c.getString(c.getColumnIndex("sex"));
            userListDataBean.name = c.getString(c.getColumnIndex("name"));
            list.add(userListDataBean);
        }
        c.close();
        return list;
    }

    private ArrayList<UserListBean.UserListDataBean> ExecSQLForMidNull(String sql) {
        ArrayList<UserListBean.UserListDataBean> list = new ArrayList<>();
        Cursor c = ExecSQLForCursor(sql);
        while (c.moveToNext()) {
            UserListBean.UserListDataBean userListDataBean = new UserListBean.UserListDataBean();
            userListDataBean.name = c.getString(c.getColumnIndex("name"));
            userListDataBean.phone = c.getString(c.getColumnIndex("phone"));
            userListDataBean.card_id = c.getString(c.getColumnIndex("card_no"));
            list.add(userListDataBean);
        }
        c.close();
        return list;
    }


    //向multi_table表中插入多参数检测仪数据
    public void addMultiData(String mid,String rhythm,String pulse,String sysdif,String sys,String dias,String mean,String oxygen,String resp,String st) {
        ContentValues cv = new ContentValues();
        cv.put("mid", mid);
        cv.put("rhythm", rhythm);
        cv.put("pulse", pulse);
        cv.put("sysdif", sysdif);
        cv.put("sys", sys);
        cv.put("dias", dias);
        cv.put("mean", mean);
        cv.put("oxygen", oxygen);
        cv.put("resp", resp);
        cv.put("st", st);
        db.insert(DBHelper.MULTI_TABLE_NAME, null, cv);
    }




    //执行SQL，返回一个游标
    private Cursor ExecSQLForCursor(String sql) {
        Cursor c = db.rawQuery(sql, null);
        return c;
    }


    public void closeDB() {
        db.close();
    }

    private void ExecSQL(String sql) {
        try {
            db.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
