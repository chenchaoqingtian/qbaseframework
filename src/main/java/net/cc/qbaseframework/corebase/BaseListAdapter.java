package net.cc.qbaseframework.corebase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * <p/>Description：自定义listview、gridview等基本控件适配器基类
 */
public abstract class BaseListAdapter<T> extends BaseAdapter {

    protected List<? extends T> mList = null;
    protected Context mContext = null;
    protected LayoutInflater mLayoutInflater = null;

    public BaseListAdapter(Context context, List<? extends T> list) {
        this.mList = list;
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (mList != null) {
            return mList.size();
        } else {
            return 0;
        }
    }

    @Override
    public T getItem(int position) {
        if (mList != null) {
            return mList.get(position);
        } else {
            return null;
        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setData(List<? extends T> list) {
        this.mList = list;
    }

    public List<? extends T> getData() {
        return mList;
    }

    public void updateAdapter(List<? extends T> list) {
        this.mList = list;
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    protected abstract View getCustomView(int position, View convertView, ViewGroup parent);

    protected abstract void onUserClick(View v, T t);

    protected class UserOnClickListener implements View.OnClickListener{

        private int mPosition;

        public UserOnClickListener(int position){
            this.mPosition = position;
        }

        @Override
        public void onClick(View v) {
            onUserClick(v, getItem(mPosition));
        }
    }
}
