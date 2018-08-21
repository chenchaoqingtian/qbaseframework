package net.cc.qbaseframework.corebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import net.cc.qbaseframework.coreannotation.InjectHandler;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.UUID;


public abstract class BaseFragment extends Fragment implements Initializable {

	protected View mMainView;

	protected HashMap<String, SoftReference<BaseFragment>> mCallbakFragments = new HashMap<>();

	private static final String KEY_UUID = "uuid";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		if (0 == getLayoutId())
			throw new IllegalArgumentException(
					"layoutId method not return 0");
		if (mMainView == null) {
			if(getLayoutInflater()!=null){
				mMainView = getLayoutInflater().inflate(getLayoutId(), container, false);
			}else{
				mMainView = inflater.inflate(getLayoutId(), container, false);
			}
			InjectHandler.inject(this, mMainView);
		}
		return mMainView;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		getParams();
		initViews();
		initViewsListener();
		initData();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		destroyTasks();
		ViewParent parent = mMainView.getParent();
		if (parent != null && parent instanceof ViewGroup) {
			((ViewGroup) parent).removeView(mMainView);
		}
		mMainView = null;
	}

	protected void replaceFragment(BaseFragment fragment, int fragmentContainer) {

		if (this.isAdded() && fragment != null && !fragment.isAdded()) {
			FragmentManager fragmentManager = getChildFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
			fragmentTransaction.replace(fragmentContainer, fragment);
			fragmentTransaction.commitAllowingStateLoss();
		}
	}

	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		if (null != getOpenTransitionAnimIds() && getOpenTransitionAnimIds().length > 0 && getOpenTransitionAnimIds()[0] != 0 && getOpenTransitionAnimIds()[1] != 0) {
			getActivity().overridePendingTransition(getOpenTransitionAnimIds()[0], getOpenTransitionAnimIds()[1]);
		}
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		Fragment root = getRootFragment();
		if (root == null || !(root instanceof BaseFragment)) {
			super.startActivityForResult(intent, requestCode);
		} else {
			String key = UUID.randomUUID().toString();

			BaseFragment fragment = (BaseFragment) root;
			fragment.addCallbackFragment(key, this);
			intent.putExtra(KEY_UUID, key);

			fragment.startActivityForResult(intent, requestCode);
		}
		if (null != getOpenTransitionAnimIds() && getOpenTransitionAnimIds().length > 0 && getOpenTransitionAnimIds()[0] != 0 && getOpenTransitionAnimIds()[1] != 0) {
			getActivity().overridePendingTransition(getOpenTransitionAnimIds()[0], getOpenTransitionAnimIds()[1]);
		}
	}

	protected void handleResult(int requestCode, int resultCode, Intent data) {

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null) {
			handleResult(requestCode, resultCode, data);
			return;
		}

		String key = data.getStringExtra(KEY_UUID);
		if (TextUtils.isEmpty(key)) {
			handleResult(requestCode, resultCode, data);
			return;
		}

		SoftReference<BaseFragment> fragmentRef = mCallbakFragments.get(key);
		if (fragmentRef == null) {
			return;
		}
		BaseFragment fragment = fragmentRef.get();
		mCallbakFragments.remove(key);

		if (fragment != null && fragment.isAdded()) {
			fragment.handleResult(requestCode, resultCode, data);
		}
	}

	protected void addCallbackFragment(String key, BaseFragment fragment) {
		mCallbakFragments.put(key, new SoftReference<BaseFragment>(fragment));
	}

	private Fragment getRootFragment() {
		Fragment parent = getParentFragment();

		if (parent == null) {
			return null;
		}

		while (parent.getParentFragment() != null) {
			parent = parent.getParentFragment();
		}

		return parent;
	}

	public void getParams() {
	}

	@Override
	public int[] getOpenTransitionAnimIds() {
		return null;
	}

	@Override
	public int[] getCloseTransitionAnimIds() {
		return null;
	}
}
