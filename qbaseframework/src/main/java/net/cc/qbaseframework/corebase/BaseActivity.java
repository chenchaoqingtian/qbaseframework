package net.cc.qbaseframework.corebase;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import net.cc.qbaseframework.coreannotation.InjectHandler;

public abstract class BaseActivity extends AppCompatActivity implements Initializable {

	private static Handler handler;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (0 == getLayoutId())
			throw new IllegalArgumentException(
					"layoutId method not return 0");
		setContentView(getLayoutId());
		InjectHandler.inject(this, this);
		getParams();
		initViews();
		initViewsListener();
		initData();
	}

	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		if (null != getOpenTransitionAnimIds() && getOpenTransitionAnimIds().length > 0 && getOpenTransitionAnimIds()[0] != 0 && getOpenTransitionAnimIds()[1] != 0) {
			overridePendingTransition(getOpenTransitionAnimIds()[0], getOpenTransitionAnimIds()[1]);
		}
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		super.startActivityForResult(intent, requestCode);
		if (null != getOpenTransitionAnimIds() && getOpenTransitionAnimIds().length > 0 && getOpenTransitionAnimIds()[0] != 0 && getOpenTransitionAnimIds()[1] != 0) {
			overridePendingTransition(getOpenTransitionAnimIds()[0], getOpenTransitionAnimIds()[1]);
		}
	}

	@Override
	public void finish() {
		super.finish();
		if (null != getCloseTransitionAnimIds() && getCloseTransitionAnimIds().length > 0 && getCloseTransitionAnimIds()[0] != 0 && getCloseTransitionAnimIds()[1] != 0) {
			overridePendingTransition(getCloseTransitionAnimIds()[0], getCloseTransitionAnimIds()[1]);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		destroyTasks();
	}

	@Override
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

	protected void addFragment(int containerId, Fragment fragment) {
		if (fragment != null && !fragment.isAdded()) {
			FragmentTransaction fragmentTransaction = getSupportFragmentManager()
					.beginTransaction();
			fragmentTransaction.add(containerId, fragment);
			fragmentTransaction.commitAllowingStateLoss();
		}
	}

	protected void replaceFragment(int containerId, Fragment fragment) {
		if (fragment != null && !fragment.isAdded()) {
			FragmentTransaction fragmentTransaction = getSupportFragmentManager()
					.beginTransaction();
			fragmentTransaction.replace(containerId, fragment);
			fragmentTransaction.commitAllowingStateLoss();
		}
	}

	protected Fragment switchFragment(int containerId, Fragment currentFragment, Fragment willShowFragment, String tag) {
		if (currentFragment != willShowFragment) {
			FragmentTransaction fragmentTransaction = getSupportFragmentManager()
					.beginTransaction();
			if (currentFragment == null) {
				fragmentTransaction.add(containerId, willShowFragment, tag)
						.commit();
			} else {
				if (!willShowFragment.isAdded()) {
					fragmentTransaction.hide(currentFragment)
							.add(containerId, willShowFragment, tag).commit();
				} else {
					fragmentTransaction.hide(currentFragment)
							.show(willShowFragment).commit();
				}
			}
		}
		return willShowFragment;
	}

	protected final Handler getHandler() {
		if (handler == null) {
			handler = new Handler(getMainLooper());
		}
		return handler;
	}

	protected void showKeyboard(boolean isShow) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (isShow) {
			if (getCurrentFocus() == null) {
				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
			} else {
				imm.showSoftInput(getCurrentFocus(), 0);
			}
		} else {
			if (getCurrentFocus() != null) {
				imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
	}

	/**
	 * 延时弹出键盘
	 *
	 * @param focus 键盘的焦点项
	 */
	protected void showKeyboardDelayed(View focus) {
		final View viewToFocus = focus;
		if (focus != null) {
			focus.requestFocus();
		}

		getHandler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if (viewToFocus == null || viewToFocus.isFocused()) {
					showKeyboard(true);
				}
			}
		}, 200);
	}

}
