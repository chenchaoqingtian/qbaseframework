package net.cc.qbaseframework.corebase;

/**
 * <p/>Description：view init interface
 */
public interface Initializable {
     int getLayoutId();
     void getParams();
     void initViews();
     void initViewsListener();
     void initData();
     void destroyTasks();
     int[] getOpenTransitionAnimIds();
     int[] getCloseTransitionAnimIds();
}
