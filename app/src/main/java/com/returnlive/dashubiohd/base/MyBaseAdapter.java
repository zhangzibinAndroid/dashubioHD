package com.returnlive.dashubiohd.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 张梓彬
 * Data : 2017/5/17 0017
 * Time : 下午 2:45
 * Describe : 基础适配器封装类
 */
public abstract class MyBaseAdapter<E> extends BaseAdapter{
	protected Context context;
	protected LayoutInflater inflater;
	protected ArrayList<E> list = new ArrayList<E>();

	public MyBaseAdapter(Context context) {
		super();
		this.context = context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/**
	 * 适配器添加数据
	 * */
	public void addDATA(E e){
		list.add(e);
	}

	public void addAllDataToMyadapter(List<E> e){
		list.clear();
		list.addAll(e);//增加一个列表
	}

	public void addAllDataToMyadapterWithoutClean(List<E> e){
		list.addAll(e);//增加一个列表
	}

	/**
	 * 清空所有数据
	 * */
	public void removeAllDATA(){
		list.clear();
	}

	public void delectData(int position){
		list.remove(position);
		notifyDataSetChanged();
	}

	/**
	 * 返回集合
	 * */
	public ArrayList<E> getList() {
		return list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public E getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}



}
