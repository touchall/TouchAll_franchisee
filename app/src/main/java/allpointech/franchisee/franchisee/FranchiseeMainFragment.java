package allpointech.franchisee.franchisee;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tuna.ui.activity.BaseAppCompatActivity;
import com.tuna.ui.fragment.BaseFragment;
import com.tuna.utils.SLog;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import allpointech.franchisee.R;
import allpointech.franchisee.franchisee.device.FranchiseeDeviceSetupActivity;
import allpointech.franchisee.franchisee.device.FranchiseeDeviceSetupFragment;
import allpointech.franchisee.franchisee.info.FranchiseeStoreInfoFragment;
import allpointech.franchisee.franchisee.info.FranchiseeStoreIntroFragment;
import allpointech.franchisee.franchisee.main.FranchiseeMainViewFragment;
import allpointech.franchisee.network.http.TNHttpMultiPartTask;
import allpointech.franchisee.network.http.json.JSONParser;
import allpointech.franchisee.network.http.resource.BaseHttpResource;
import allpointech.franchisee.network.http.resource.data.ResMain;
import allpointech.franchisee.user.mypoint.Franchisee.CheckFranchiseeActivity;
import allpointech.franchisee.user.mypoint.processPoint.CancelPointActivity;
import allpointech.franchisee.user.mypoint.processPoint.InsertPointActivity;
import allpointech.franchisee.user.setup.DeviceSetupResultActivity;
import allpointech.franchisee.utils.TNConstants;
import allpointech.franchisee.utils.TNPreference;
import allpointech.franchisee.webview.WebViewFragment;

/**
 * Created by daze on 2016-11-15.
 */

public class FranchiseeMainFragment extends BaseFragment implements BaseAppCompatActivity.onKeyBackPressedListener, View.OnClickListener, TNHttpMultiPartTask.onHttpNetResultListener {
    public static final String FRAGMENT_TAG = FranchiseeMainFragment.class.getSimpleName();


    /***
     * 메뉴 레이아웃.
     */

    private DrawerLayout mDrawerLayout;
    //private android.app.Fragment fragment = null;
    private ExpandableListView expListView;
    private HashMap<String, List<String>> listDataChild;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    static int[] _icons = {
            R.drawable.ic_home,
            R.drawable.ic_people,
            R.drawable.ic_photos,
            R.drawable.ic_communities,
            R.drawable.ic_pages,
            R.drawable.ic_whats_hot,
            R.drawable.ic_home,
            R.drawable.ic_home,
            R.drawable.ic_home};
    View view_Group;

    private static final int FILTER_ID = 0;

    public static final int COMPANY_INTRO = 1001;
    public static final int TOUCHALL_INTRO = 1002;

    // nav drawer title
    //private CharSequence mDrawerTitle;

    // used to store app title
    //private CharSequence mTitle;

    private ActionBarDrawerToggle mDrawerToggle;

    //private DrawerLayout mDrawerLayout;

//    private LinearLayout mBtnMenuIntro;
//    private LinearLayout mBtnMenuIntroCompany;
//    private LinearLayout mBtnMenuIntroTouchAll;
//    private LinearLayout mBtnMenuFindStore;
//    private LinearLayout mBtnMenuCheckUseMyPoint;
//    private LinearLayout mBtnMenuMyPage;
//    private LinearLayout mBtnMenuMyInfo;
//    private LinearLayout mBtnMenuMyBonus;
//    private LinearLayout mBtnMenuMyNews;
//    private LinearLayout mBtnMenuEvent;
//    private LinearLayout mBtnMenuCustomer;
//    private LinearLayout mBtnMenuNotice;
//    private LinearLayout mBtnMenuFaq;
//
//    private LinearLayout mLiSideMenu;
    private String mGcmData;

    private int commandGroup = -1;
    private int commandChild = -1;


    public String mSelStoreId;
    public String mSelEmail;
    public String mSelBirthDay;

    private boolean _bMyPointTest = false;
    private boolean _bJuklibPointTest = false;
    private boolean _bCancelPointTest = false;
    private boolean _bDeviceSetupTest = false;

    @Override
    protected void BundleData(Bundle bundle) {
        setHasOptionsMenu(true);
        ((BaseAppCompatActivity) getActivity()).setOnKeyBackPressedListener(this);
        if (getArguments() != null) {
            mGcmData = getArguments().getString("obj");
        }
    }

    @Override
    protected int inflateLayout() {
        return R.layout.fragment_franchisee_main;
    }

    @Override
    protected void initLayout(View view) {
        mDrawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);

        setUpDrawer();
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout,
                R.drawable.ic_drawer, // nav menu toggle icon
                R.string.app_name, // nav drawer open - description for
                // accessibility
                R.string.app_name // nav drawer close - description for
                // accessibility
        ) {
            @Override
            public void onDrawerClosed(View view) {
//                getActionBar().setTitle(mTitle);
//                // calling onPrepareOptionsMenu() to show action bar icons
//                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
//                getActionBar().setTitle(mDrawerTitle);
//                // calling onPrepareOptionsMenu() to hide action bar icons
//                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        makeActionOverflowMenuShown();
    }

    // actionbar over flow icon
    private void makeActionOverflowMenuShown() {
        // devices with hardware menu button (e.g. Samsung ) don't show action
        // overflow menu
        try {
            final ViewConfiguration config = ViewConfiguration.get(getActivity());
            final Field menuKeyField = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (final Exception e) {
            Log.e("", e.getLocalizedMessage());
        }
    }

    /**
     *
     * Get the names and icons references to build the drawer menu...
     */
    private void setUpDrawer() {
        mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
		/*
		 * mDrawerLayout.setScrimColor(getResources().getColor(
		 * android.R.color.transparent));
		 */
        mDrawerLayout.setDrawerListener(mDrawerListener);
        expListView = (ExpandableListView) getActivity().findViewById(R.id.list_slidermenu);
        prepareListData();

        listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);
        // setting list adapter
        expListView.setAdapter(listAdapter);
        // expandable list view click listener

        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public  boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                switch (groupPosition) {

				/*
				 * Here add your fragment class name for each case menu (eg.
				 * Layout1, layout2 in screen) you can add n number of classes
				 * to the swithch case Also when you add the class name here,
				 * also add the corresponding name to the array list
				 */
                    case 0:
                        onGoHome();
                        break;
                    case 1:
                        break;
                    case 2:
                        onGoStoreInfo();
                        break;
                    case 3:
                        onGoStoreIntro();
                        break;
                    case 4:
                        onGoUsersInfo();
                        break;
                    case 5:
                        onGoDetailList();
                        break;
                    case 6:
                        onGoMonthlyList();
                        break;
                    case 7:
                        onGoAdTips();
                        break;
                    case 8:
                        onGoNotices();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // setbackground color for list that is selected in child group
//                BaseFragment fragment = null;
                v.setSelected(true);
                if (view_Group != null) {
                    view_Group.setBackgroundColor(Color.parseColor("#ffffff"));
                    //view_Group.setBackgroundColor(Color.parseColor("#F21E1E"));
                }
                view_Group = v;
                //view_Group.setBackgroundColor(Color.parseColor("#F21E1E"));


//                getFragmentManager().beginTransaction()
//                        .replace(R.id.contents, fragment).commit();
                expListView.setItemChecked(childPosition, true);
//                mDrawerLayout.closeDrawer(expListView);

                switch (groupPosition) {
                    case 1:
                        if (childPosition == 0)
                            onGoIntro(TNConstants.WEBVIEW.TYPE_INTRO_COMPANY);
                        else if (childPosition == 1)
                            onGoIntro(TNConstants.WEBVIEW.TYPE_INTRO_TOUCHALL);
                        break;
                }

                return false;
            }
        });
    }

    @Override
    protected void initRequest() {
//        requestMain();

        TNPreference.setAutoLogin(getActivity(), true);
        if (mGcmData == null) {
            SLog.LogD("Ace : null");
            replaceAnimationTagFragment(R.id.contents, new FranchiseeMainViewFragment(), FranchiseeMainViewFragment.FRAGMENT_TAG, 0, 0);
        } else {
            SLog.LogD("Ace : null");
            SLog.LogD(mGcmData);
            try {
                JSONObject obj = new JSONObject(mGcmData);
                int idx = JSONParser.getInt(obj, "test", 1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (_bMyPointTest) {
            //requestFranchiseePoint("20181010", "102000", "0", "KR201808100003");
        }
        if (_bJuklibPointTest) {
            setPayResult("200501011200341KR201808100003T000000000000000000000000300000M00030501010000010090000000000000000");
        }
        if (_bCancelPointTest) {
            requestCancelPoint("200501011200343KR201808100003T000000000000000000000000300000M00030501010000010090000000000000000");
        }
        if (_bDeviceSetupTest) {
            requestDeviceSetup("200501011209174KR201808100003T00000000001001110300000005000000000000000100100018000010301");
        }
    }

    public void openDrawer() {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Check if no view has focus:
                View view = getActivity().getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        }
    }

    public void onGoHome() {
//        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
//            mDrawerLayout.closeDrawers();
//        } else
        {
            //mDrawerLayout.closeDrawers();
            replaceAnimationTagFragment(R.id.contents, new FranchiseeMainViewFragment(), FranchiseeMainViewFragment.FRAGMENT_TAG, 0, 0);
            mDrawerLayout.closeDrawers();
        }
    }

    public void onGoIntro(int type) {

        Bundle b = new Bundle();
        BaseFragment fragment = null;
        String TAG = "";

        b.putInt(TNConstants.WEBVIEW.KEY_WEBVIEW_TYPE, type);
        fragment = new WebViewFragment();
        fragment.setArguments(b);
        TAG = WebViewFragment.FRAGMENT_TAG;

        if (fragment != null) {
            replaceAnimationTagFragment(R.id.contents, fragment, TAG, 0, 0);
        }
        mDrawerLayout.closeDrawers();
    }

    public void onGoStoreInfo() {
        //startActivityForResult(new Intent(getActivity(), FranchiseeInfoActivity.class), 12123);
        Bundle b = new Bundle();
        BaseFragment fragment = null;
        String TAG = "";

        fragment = new FranchiseeStoreInfoFragment();
        b.putString("sid", mSelStoreId);
        b.putString("email", mSelEmail);
        b.putString("birth", mSelBirthDay);
        fragment.setArguments(b);
        TAG = FranchiseeStoreInfoFragment.FRAGMENT_TAG;

        if (fragment != null) {
            replaceAnimationTagFragment(R.id.contents, fragment, TAG, 0, 0);
        }
        mDrawerLayout.closeDrawers();
    }

    public void onGoStoreIntro() {
        Bundle b = new Bundle();
        BaseFragment fragment = null;
        String TAG = "";

        fragment = new FranchiseeStoreIntroFragment();
        b.putString("sid", mSelStoreId);
        b.putString("email", mSelEmail);
        b.putString("birth", mSelBirthDay);
        fragment.setArguments(b);
        TAG = FranchiseeStoreIntroFragment.FRAGMENT_TAG;

        if (fragment != null) {
            replaceAnimationTagFragment(R.id.contents, fragment, TAG, 0, 0);
        }
        mDrawerLayout.closeDrawers();
    }

    public void onGoUsersInfo() {
        Bundle b = new Bundle();
        BaseFragment fragment = null;
        String TAG = "";

        b.putInt(TNConstants.WEBVIEW.KEY_WEBVIEW_TYPE, TNConstants.WEBVIEW.TYPE_FRANCHISEE_USERS_INFO);
        b.putString(TNConstants.WEBVIEW.KEY_WEBVIEW_TOKEN, TNPreference.getMemToken(getActivity()));
        fragment = new WebViewFragment();
        fragment.setArguments(b);
        TAG = WebViewFragment.FRAGMENT_TAG;

        if (fragment != null) {
            replaceAnimationTagFragment(R.id.contents, fragment, TAG, 0, 0);
        }
        mDrawerLayout.closeDrawers();
    }

    public void onGoDetailList() {
        Bundle b = new Bundle();
        BaseFragment fragment = null;
        String TAG = "";

        b.putInt(TNConstants.WEBVIEW.KEY_WEBVIEW_TYPE, TNConstants.WEBVIEW.TYPE_FRANCHISEE_DETAIL_LIST);
        b.putString(TNConstants.WEBVIEW.KEY_WEBVIEW_TOKEN, TNPreference.getMemToken(getActivity()));
        fragment = new WebViewFragment();
        fragment.setArguments(b);
        TAG = WebViewFragment.FRAGMENT_TAG;

        if (fragment != null) {
            replaceAnimationTagFragment(R.id.contents, fragment, TAG, 0, 0);
        }
        mDrawerLayout.closeDrawers();
    }

    public void onGoMonthlyList() {
        Bundle b = new Bundle();
        BaseFragment fragment = null;
        String TAG = "";

        b.putInt(TNConstants.WEBVIEW.KEY_WEBVIEW_TYPE, TNConstants.WEBVIEW.TYPE_FRANCHISEE_MONTHLY_LIST);
        b.putString(TNConstants.WEBVIEW.KEY_WEBVIEW_TOKEN, TNPreference.getMemToken(getActivity()));
        fragment = new WebViewFragment();
        fragment.setArguments(b);
        TAG = WebViewFragment.FRAGMENT_TAG;

        if (fragment != null) {
            replaceAnimationTagFragment(R.id.contents, fragment, TAG, 0, 0);
        }
        mDrawerLayout.closeDrawers();
    }

    public void onGoAdTips() {
        Bundle b = new Bundle();
        BaseFragment fragment = null;
        String TAG = "";

        b.putInt(TNConstants.WEBVIEW.KEY_WEBVIEW_TYPE, TNConstants.WEBVIEW.TYPE_FRANCHISEE_AD_TIPS);
        b.putString(TNConstants.WEBVIEW.KEY_WEBVIEW_TOKEN, TNPreference.getMemToken(getActivity()));
        fragment = new WebViewFragment();
        fragment.setArguments(b);
        TAG = WebViewFragment.FRAGMENT_TAG;

        if (fragment != null) {
            replaceAnimationTagFragment(R.id.contents, fragment, TAG, 0, 0);
        }
        mDrawerLayout.closeDrawers();
    }

    public void onGoNotices() {
        Bundle b = new Bundle();
        BaseFragment fragment = null;
        String TAG = "";

        b.putInt(TNConstants.WEBVIEW.KEY_WEBVIEW_TYPE, TNConstants.WEBVIEW.TYPE_FRANCHISEE_NOTICES);
        b.putString(TNConstants.WEBVIEW.KEY_WEBVIEW_TOKEN, TNPreference.getMemToken(getActivity()));
        fragment = new WebViewFragment();
        fragment.setArguments(b);
        TAG = WebViewFragment.FRAGMENT_TAG;

        if (fragment != null) {
            replaceAnimationTagFragment(R.id.contents, fragment, TAG, 0, 0);
        }
        mDrawerLayout.closeDrawers();
    }

    private void onSideMenuClick(View view) {
        Bundle b = new Bundle();
        BaseFragment fragment = null;
        String TAG = "";
        mDrawerLayout.closeDrawers();
    }


    public void onRefresh() {
//        requestMain();
    }

    private void requestMain() {
        ResMain res = new ResMain();
//        res.setParameter(TNPreference.getMemIndex(getActivity()));

        TNHttpMultiPartTask task = new TNHttpMultiPartTask(getActivity(), getFragmentManager());
        task.setOnHttpResultListener(this);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, res);
    }

    @Override
    public void onHttpNetSuccessEvent(BaseHttpResource[] o) {
    }

    @Override
    public void onHttpDebugEvent(String debug_msg) {

    }

    @Override
    public void onHttpNetFailEvent(int errorCode, String errorMsg) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
//            case TNConstants.REQUEST_CODE.REFRESH:
//                if (resultCode == Activity.RESULT_OK) {
//                    onRefresh();
//                }
//                break;
//            case TNConstants.REQUEST_CODE.INSERT_POINT:
            case TNConstants.REQUEST_CODE.CANCEL_POINT:
                if (resultCode == Activity.RESULT_OK) {
                    String junmun = data.getStringExtra("junmun");
                    processNFC(junmun);
                }
                break;
//            case TNConstants.REQUEST_CODE.CHECK_FRANCHISEE:
//                if (resultCode == Activity.RESULT_OK) {
//                    String junmun = data.getStringExtra("junmun");
//                    if (junmun != null && junmun.length() > 0) {
//                        processNFC(junmun);
//                    }
//                    else {
//                        FranchiseeMainViewFragment fragment = (FranchiseeMainViewFragment) getFragmentManager().findFragmentByTag(FranchiseeMainViewFragment.FRAGMENT_TAG);
//                        if (fragment != null) {
//                            fragment.onReflash();
//                        }
//                    }
//                }
//                break;
            case TNConstants.REQUEST_CODE.DEVICE_SETUP:
                if (resultCode == Activity.RESULT_OK) {
                    FranchiseeStoreInfoFragment fragment = (FranchiseeStoreInfoFragment) getFragmentManager().findFragmentByTag(FranchiseeStoreInfoFragment.FRAGMENT_TAG);
                    if (fragment != null) {
                        fragment.onReflash();
                    }
                }
                break;
        }
    }

    private boolean mFlag = false;
    private Handler mExitHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                mFlag = false;
            }
        }
    };

    public boolean isDrawOpen() {
        return mDrawerLayout.isDrawerOpen(GravityCompat.START);
    }

    public void closeDraw() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
        }
    }

    @Override
    public void onBack() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
        } else {
            if (!mFlag && getFragmentManager().getBackStackEntryCount() == 0) {
                Toast.makeText(getActivity(), getString(R.string.msg_exit_app), Toast.LENGTH_SHORT).show();
                mFlag = true;
                mExitHandler.sendEmptyMessageDelayed(0, TNConstants.EXIT_DELAY);
            } else {
                getActivity().finish();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /***
     * pay result : 적립
     */

    public void setPayResult(String junmun) {

        Intent i = new Intent(getActivity(), InsertPointActivity.class);
        i.putExtra("point_junmun", junmun);
        startActivityForResult(i, TNConstants.REQUEST_CODE.INSERT_POINT);

    }

//    public void requestFranchiseePoint(String date, String time, String action, String device_serial) {
//
//        Intent i = new Intent(getActivity(), CheckFranchiseeActivity.class);
//        i.putExtra("device_serial", device_serial);
//        startActivityForResult(i, TNConstants.REQUEST_CODE.CHECK_FRANCHISEE);
//    }
    public void requestFranchiseePoint(String junmun) {

        Intent i = new Intent(getActivity(), CheckFranchiseeActivity.class);
        i.putExtra("device_serial", junmun);
        startActivityForResult(i, TNConstants.REQUEST_CODE.CHECK_FRANCHISEE);
    }

    public void requestCancelPoint(String junmun) {

        Intent i = new Intent(getActivity(), CancelPointActivity.class);
        i.putExtra("cancel_point", junmun);
        //startActivity(i);
        startActivityForResult(i, TNConstants.REQUEST_CODE.CANCEL_POINT);
    }

    public void requestDeviceSetup(String junmun) {
        Intent i = new Intent(getActivity(), FranchiseeDeviceSetupActivity.class);
        i.putExtra("device_setup", junmun);
        //startActivity(i);
        startActivityForResult(i, TNConstants.REQUEST_CODE.DEVICE_SETUP);
    }

    private byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    // 적립 취소, 디바이스 설정 만 처리
    public void processNFC(String msg) {

        String[] fields = msg.split("\\?");
        if (fields.length > 1) {
            String[] junmuns = msg.split("\\=");
            if (junmuns.length > 1 && junmuns[0].contains("point")) {  // 적립 취소 만 확인 가능.
                String junmun = junmuns[1];
                //setPayResult(junmun);//
                requestCancelPoint(junmun);
            }
        }
        else {
            String[] junmuns = msg.split("\\=");
            if (junmuns.length > 1) {
//                if (junmuns[0].contains("device_serial")) {
//                    String junmun = junmuns[1];
//                    requestFranchiseePoint(junmun);
//                }
//                else
                if (junmuns[0].contains("cancel_point")) {
                    String junmun = junmuns[1];
                    requestCancelPoint(junmun);
                }
                else
                if (junmuns[0].contains("device_setting")) {
                    String junmun = junmuns[1];
                    requestDeviceSetup(junmun);
                }
            }
        }
    }




    // Catch the events related to the drawer to arrange views according to this
    // action if necessary...
    private DrawerLayout.DrawerListener mDrawerListener = new DrawerLayout.DrawerListener() {

        @Override
        public void onDrawerStateChanged(int status) {

        }

        @Override
        public void onDrawerSlide(View view, float slideArg) {

        }

        @Override
        public void onDrawerOpened(View view) {
            //getActionBar().setTitle(mDrawerTitle);
            // calling onPrepareOptionsMenu() to hide action bar icons
            //invalidateOptionsMenu();
        }

        @Override
        public void onDrawerClosed(View view) {
            //getActionBar().setTitle(mTitle);
            // calling onPrepareOptionsMenu() to show action bar icons
            //invalidateOptionsMenu();
        }
    };

    private void prepareListData() {

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        String[] array = getResources().getStringArray(R.array.franchisee_menu_items);
        listDataHeader = Arrays.asList(array);

        // Adding child data
        // dash board
        List<String> l0 = new ArrayList<String>();
        String[] home = getResources().getStringArray(R.array.franchisee_menu_home);
        l0 = Arrays.asList(home);

        // before you file
        List<String> l1 = new ArrayList<String>();
        String[] company_intros = getResources().getStringArray(R.array.franchisee_menu_company_intro);
        l1 = Arrays.asList(company_intros);

        // profile
        List<String> l2 = new ArrayList<String>();
        String[] store_info = getResources().getStringArray(R.array.franchisee_menu_store_info);
        l2 = Arrays.asList(store_info);

        // income slip
        List<String> l3 = new ArrayList<String>();
        String[] store_intro = getResources().getStringArray(R.array.franchisee_menu_store_intro);
        l3 = Arrays.asList(store_intro);

        List<String> l4 = new ArrayList<String>();
        String[] users_info = getResources().getStringArray(R.array.franchisee_menu_users_info);
        l4 = Arrays.asList(users_info);

        List<String> l5 = new ArrayList<String>();
        String[] detail_list = getResources().getStringArray(R.array.franchisee_menu_detail_list);
        l5 = Arrays.asList(detail_list);

        List<String> l6 = new ArrayList<String>();
        String[] monthly_list = getResources().getStringArray(R.array.franchisee_menu_monthly_list);
        l6 = Arrays.asList(monthly_list);

        List<String> l7 = new ArrayList<String>();
        String[] ad_tips = getResources().getStringArray(R.array.franchisee_menu_ad_tips);
        l7 = Arrays.asList(ad_tips);

        List<String> l8 = new ArrayList<String>();
        String[] notices = getResources().getStringArray(R.array.franchisee_menu_notices);
        l8 = Arrays.asList(notices);


        // assigning values to menu and submenu
        listDataChild.put(listDataHeader.get(0), l0); // Header, Child
        // data
        listDataChild.put(listDataHeader.get(1), l1);
        listDataChild.put(listDataHeader.get(2), l2);
        listDataChild.put(listDataHeader.get(3), l3);
        listDataChild.put(listDataHeader.get(4), l4);
        listDataChild.put(listDataHeader.get(5), l5);
        listDataChild.put(listDataHeader.get(6), l6);
        listDataChild.put(listDataHeader.get(7), l7);
        listDataChild.put(listDataHeader.get(8), l8);
    }

    public class ExpandableListAdapter extends BaseExpandableListAdapter {

        private Context _context;
        private List<String> _listDataHeader; // header titles
        // child data in format of header title, child title
        private HashMap<String, List<String>> _listDataChild;

        public ExpandableListAdapter(Context context,
                                     List<String> listDataHeader,
                                     HashMap<String, List<String>> listChildData) {
            this._context = context;
            this._listDataHeader = listDataHeader;
            this._listDataChild = listChildData;
        }

        @Override
        public Object getChild(int groupPosition, int childPosititon) {
            return this._listDataChild.get(
                    this._listDataHeader.get(groupPosition))
                    .get(childPosititon);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, final int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {

            final String childText = (String) getChild(groupPosition,
                    childPosition);

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_item, null);
            }

            TextView txtListChild = (TextView) convertView
                    .findViewById(R.id.lblListItem);

            txtListChild.setText(childText);
            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return this._listDataChild.get(
                    this._listDataHeader.get(groupPosition)).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return this._listDataHeader.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return this._listDataHeader.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            String headerTitle = (String) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_group, null);
            }

            List<String> childs = this._listDataChild.get(this._listDataHeader.get(groupPosition));
            TextView txt_plusminus = (TextView) convertView.findViewById(R.id.plus_txt);
            if (childs.size() == 0) {
                txt_plusminus.setText("");
            }
            else {
                if (isExpanded) {
                    txt_plusminus.setText("-");
                } else {
                    txt_plusminus.setText("+");
                }
            }

            TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
            // lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(headerTitle);

            // nav drawer icons from resources
            // navMenuIcons =
            // getResources().obtainTypedArray(R.array.nav_drawer_icons);
            // imgListGroup.setImageDrawable(navMenuIcons.getDrawable(groupPosition));

            // adding icon to expandable list view
            ImageView imgListGroup = (ImageView) convertView.findViewById(R.id.ic_txt);
            imgListGroup.setImageResource(_icons[groupPosition]);
            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
