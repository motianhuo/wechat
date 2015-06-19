package com.juns.wechat.widght.swipe.adapters;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.juns.wechat.widght.swipe.SwipeLayout;
import com.juns.wechat.widght.swipe.implments.SwipeItemMangerImpl;
import com.juns.wechat.widght.swipe.interfaces.SwipeAdapterInterface;
import com.juns.wechat.widght.swipe.interfaces.SwipeItemMangerInterface;

public abstract class BaseSwipeAdapter extends BaseAdapter implements
		SwipeItemMangerInterface, SwipeAdapterInterface {

	protected SwipeItemMangerImpl mItemManger = new SwipeItemMangerImpl(this);

	/**
	 * return the {@link com.daimajia.swipe.SwipeLayout} resource id, int the
	 * view item.
	 * 
	 * @param position
	 * @return
	 */
	public abstract int getSwipeLayoutResourceId(int position);

	/**
	 * generate a new view item. Never bind SwipeListener or fill values here,
	 * every item has a chance to fill value or bind listeners in fillValues. to
	 * fill it in {@code fillValues} method.
	 * 
	 * @param position
	 * @param parent
	 * @return
	 */
	public abstract View generateView(int position, ViewGroup parent);

	/**
	 * fill values or bind listeners to the view.
	 * 
	 * @param position
	 * @param convertView
	 */
	public abstract void fillValues(int position, View convertView);

	@Override
	public final View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			v = generateView(position, parent);
			mItemManger.initialize(v, position);
		} else {
			mItemManger.updateConvertView(v, position);
		}
		fillValues(position, v);
		return v;
	}

	@Override
	public void openItem(int position) {
		mItemManger.openItem(position);
	}

	@Override
	public void closeItem(int position) {
		mItemManger.closeItem(position);
	}

	@Override
	public void closeAllExcept(SwipeLayout layout) {
		mItemManger.closeAllExcept(layout);
	}

	@Override
	public List<Integer> getOpenItems() {
		return mItemManger.getOpenItems();
	}

	@Override
	public List<SwipeLayout> getOpenLayouts() {
		return mItemManger.getOpenLayouts();
	}

	@Override
	public void removeShownLayouts(SwipeLayout layout) {
		mItemManger.removeShownLayouts(layout);
	}

	@Override
	public boolean isOpen(int position) {
		return mItemManger.isOpen(position);
	}

	@Override
	public SwipeItemMangerImpl.Mode getMode() {
		return mItemManger.getMode();
	}

	@Override
	public void setMode(SwipeItemMangerImpl.Mode mode) {
		mItemManger.setMode(mode);
	}
}
