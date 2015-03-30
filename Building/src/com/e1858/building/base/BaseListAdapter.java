package com.e1858.building.base;

import java.util.List;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class BaseListAdapter<T> extends BaseAdapter {
	protected List<T>		list	= null;
	protected Context		mContext;
	private final Object	mLock	= new Object();

	public BaseListAdapter(List<T> list, Context mContext) {
		super();
		this.list = list;
		this.mContext = mContext;
	}

	public void add(T one) {
		synchronized (mLock) {
			list.add(one);
		}
		notifyDataSetChanged();
	}

	public void add(T one, int position) {
		synchronized (mLock) {
			list.add(position, one);
		}
		notifyDataSetChanged();
	}

	public void addAll(List<T> ones) {
		synchronized (mLock) {
			list.addAll(ones);
		}
		notifyDataSetChanged();
	}

	public void insert(int index, T one) {
		synchronized (mLock) {
			list.add(index, one);
		}
		notifyDataSetChanged();
	}

	public void remove(int index) {
		synchronized (mLock) {
			list.remove(index);
		}
		notifyDataSetChanged();
	}

	public void clear() {
		synchronized (mLock) {
			list.clear();
		}
		notifyDataSetChanged();
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		return (list == null || list.isEmpty()) ? 0 : list.size();
	}

	@Override
	public T getItem(int position) {
		if (null == list) {
			return null;
		}
		return list.size() > position ? list.get(position) : null;
	}

	public abstract View getItemView(int position, View convertView, ViewGroup parent);

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getItemView(position, convertView, parent);
	}

	public class ViewHolder {
		SparseArray<View>	views;

		public ViewHolder() {
			super();
			views = new SparseArray<View>();
		}

		public void put(Integer id, View v) {
			views.put(id, v);
		}

		public View get(Integer id) {
			return views.get(id);
		}

	}
}
