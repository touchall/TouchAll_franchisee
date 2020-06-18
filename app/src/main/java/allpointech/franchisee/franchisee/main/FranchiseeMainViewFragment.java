package allpointech.franchisee.franchisee.main;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tuna.ui.activity.BaseAppCompatActivity;
import com.tuna.ui.fragment.BaseFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

import allpointech.franchisee.R;
import allpointech.franchisee.franchisee.FranchiseeMainActivity;
import allpointech.franchisee.franchisee.FranchiseeMainFragment;
import allpointech.franchisee.franchisee.device.FranchiseeFirmwareUpdateActivity;
import allpointech.franchisee.network.http.TNHttpMultiPartTask;
import allpointech.franchisee.network.http.TNHttpMultiPartTaskEx;
import allpointech.franchisee.network.http.json.JSONParser;
import allpointech.franchisee.network.http.resource.BaseHttpResource;
import allpointech.franchisee.network.http.resource.data.ResApiInfo;
import allpointech.franchisee.network.http.resource.data.ResFranchiseeOwnerInfo;
import allpointech.franchisee.network.http.resource.data.ResFranchiseeStore;
import allpointech.franchisee.network.http.resource.data.ResFranchiseeStoreFav;
import allpointech.franchisee.network.http.resource.data.ResFranchiseeStoreFavAvg;
import allpointech.franchisee.network.http.resource.data.ResFranchiseeStoreFavCnt;
import allpointech.franchisee.network.http.resource.data.ResInfo;
import allpointech.franchisee.setting.SettingActivity;
import allpointech.franchisee.user.UserMainFragment;
import allpointech.franchisee.utils.TNConstants;
import allpointech.franchisee.utils.TNPreference;

/**
 * Created by jay on 2018. 6. 19..
 */

public class FranchiseeMainViewFragment extends BaseFragment implements View.OnClickListener, BaseAppCompatActivity.onKeyBackPressedListener, TNHttpMultiPartTask.onHttpNetResultListener, TNHttpMultiPartTaskEx.onHttpNetResultListener, TNHttpMultiPartTask.onHttpNetFailResultListener {
    public static final String FRAGMENT_TAG = FranchiseeMainViewFragment.class.getSimpleName();

    private Toolbar mToolbar;

    private TextView mTvHello;

    private TextView mTvBonusType;
    private TextView mTvStoreType;
    private TextView mTvFranchiseeGrade;
    private TextView mTvUsersGrade;

    private String mUsersGradeLevel;
    private String mUsersGradeCount;

    private LinearLayout mBtnCompanyIntro;
    private LinearLayout mBtnStoreInfo;
    private LinearLayout mBtnStoreIntro;
    private LinearLayout mBtnStoreUsersInfo;
    private LinearLayout mBtnStoreDetailList;
    private LinearLayout mBtnStoreMonthlyList;
    private LinearLayout mBtnAdTips;
    private LinearLayout mBtnNotices;

    private TextView mTvCompanyIntro;
    private TextView mTvTouchallIntro;

    private final int SEND_NFC_ON = 1;
    private final int SEND_NFC_OFF = 2;

    // Handler 클래스
    Handler mMainHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case SEND_NFC_ON:
                {
                    FranchiseeMainActivity activity = (FranchiseeMainActivity)getActivity();
                    if (activity != null) {
                        //activity._enableNdefExchangeMode("device_fw=" + mMsgData);
                        //activity._enableTagWriteMode();

                        //activity._enableFirmwareUpdate(fw_data);
                    }
                }
                break;

                case SEND_NFC_OFF:
                {
                    FranchiseeMainActivity activity = (FranchiseeMainActivity)getActivity();
                    if (activity != null) {
                        activity._disableNdef(true);
                    }
                }
                break;

                default:
                    break;
            }
        }

    };

    @Override
    protected void BundleData(Bundle bundle) {
        setHasOptionsMenu(true);
        ((BaseAppCompatActivity) getActivity()).setOnKeyBackPressedListener(this);
        if (getArguments() != null) {
        }
    }

    @Override
    protected int inflateLayout() {
        return R.layout.fragment_franchisee_main_view;
    }

    @Override
    protected void initLayout(View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((BaseAppCompatActivity) getActivity()).setSupportActionBar(mToolbar);

        mTvHello = (TextView) view.findViewById(R.id.text_main_hello);

        mTvBonusType = (TextView) view.findViewById(R.id.text_bonus_type);
//        mTvBonusType.setOnClickListener(this);
        mTvStoreType = (TextView) view.findViewById(R.id.text_store_type);
        mTvFranchiseeGrade = (TextView) view.findViewById(R.id.text_franchisee_grade);
        mTvUsersGrade = (TextView) view.findViewById(R.id.text_user_grade);

        mBtnCompanyIntro = (LinearLayout) view.findViewById(R.id.btn_company_intro);
        mBtnCompanyIntro.setOnClickListener(this);
        mBtnStoreInfo = (LinearLayout) view.findViewById(R.id.btn_franchisee_store_info);
        mBtnStoreInfo.setOnClickListener(this);
        mBtnStoreIntro = (LinearLayout) view.findViewById(R.id.btn_franchisee_store_intro);
        mBtnStoreIntro.setOnClickListener(this);
        mBtnStoreUsersInfo = (LinearLayout) view.findViewById(R.id.btn_franchisee_store_usersinfo);
        mBtnStoreUsersInfo.setOnClickListener(this);
        mBtnStoreDetailList = (LinearLayout) view.findViewById(R.id.btn_franchisee_detail_list);
        mBtnStoreDetailList.setOnClickListener(this);
        mBtnStoreMonthlyList = (LinearLayout) view.findViewById(R.id.btn_franchisee_monthly_list);
        mBtnStoreMonthlyList.setOnClickListener(this);
        mBtnAdTips = (LinearLayout) view.findViewById(R.id.btn_franchisee_ad_tips);
        mBtnAdTips.setOnClickListener(this);
        mBtnNotices = (LinearLayout) view.findViewById(R.id.btn_franchisee_notices);
        mBtnNotices.setOnClickListener(this);

        mTvCompanyIntro = (TextView) view.findViewById(R.id.tv_company_intro);
        mTvCompanyIntro.setOnClickListener(this);
        mTvTouchallIntro = (TextView) view.findViewById(R.id.tv_touchall_intro);
        mTvTouchallIntro.setOnClickListener(this);
    }


    private void requestMain() {
        ResApiInfo res = new ResApiInfo();
        res.setToken(TNPreference.getMemToken(getActivity()));

        TNHttpMultiPartTask task = new TNHttpMultiPartTask(getActivity(), getFragmentManager());
        task.setOnHttpResultListener(this);
        task.setOnHttpFailResultListener(this);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, res);
    }

    private void requestOwnerInfo() {
        ResFranchiseeOwnerInfo res = new ResFranchiseeOwnerInfo();
        res.setToken(TNPreference.getMemToken(getActivity()));

        res.setParameterQuestion(TNPreference.getMemberID(getActivity()));

        TNHttpMultiPartTask task = new TNHttpMultiPartTask(getActivity(), getFragmentManager());
        task.setOnHttpResultListener(this);
        task.setOnHttpFailResultListener(this);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, res);
    }

    private void requestStore() {
        ResFranchiseeStore res = new ResFranchiseeStore();
        res.setToken(TNPreference.getMemToken(getActivity()));

        TNHttpMultiPartTask task = new TNHttpMultiPartTask(getActivity(), getFragmentManager());
        task.setOnHttpResultListener(this);
        task.setOnHttpFailResultListener(this);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, res);
    }

    private void requestFav(String sid) {
        ResFranchiseeStoreFav res = new ResFranchiseeStoreFav();
        res.setToken(TNPreference.getMemToken(getActivity()));

        res.setParameter(sid);

        TNHttpMultiPartTask task = new TNHttpMultiPartTask(getActivity(), getFragmentManager());
        task.setOnHttpResultListener(this);
        task.setOnHttpFailResultListener(this);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, res);
    }

    private void requestFavCnt(String sid) {
        ResFranchiseeStoreFavCnt res = new ResFranchiseeStoreFavCnt();
        res.setToken(TNPreference.getMemToken(getActivity()));

        res.setParameter(sid);

        TNHttpMultiPartTask task = new TNHttpMultiPartTask(getActivity(), getFragmentManager());
        task.setOnHttpResultListener(this);
        task.setOnHttpFailResultListener(this);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, res);
    }

    private void requestFavAvg(String sid) {
        ResFranchiseeStoreFavAvg res = new ResFranchiseeStoreFavAvg();
        res.setToken(TNPreference.getMemToken(getActivity()));

        res.setParameter(sid);

        TNHttpMultiPartTask task = new TNHttpMultiPartTask(getActivity(), getFragmentManager());
        task.setOnHttpResultListener(this);
        task.setOnHttpFailResultListener(this);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, res);
    }

    @Override
    protected void initRequest() {
        requestMain();
        requestOwnerInfo();
        requestStore();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.user_setting, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                UserMainFragment fragment = (UserMainFragment) getFragmentManager().findFragmentByTag(UserMainFragment.FRAGMENT_TAG);
                if (fragment != null) {
                    fragment.openDrawer();
                }
                break;

            case R.id.action_setting:
            {
                Intent i = new Intent(getActivity(), FranchiseeFirmwareUpdateActivity.class);
                startActivity(i);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onHttpNetSuccessEvent(BaseHttpResource[] o) {
        if (o[0] instanceof ResApiInfo) {
            ResApiInfo res = (ResApiInfo) o[0];
            JSONObject obj = res.getParseData();
            if (JSONParser.isSuccess(obj)) {
                JSONObject obj_info = JSONParser.getObject(obj, ResInfo.KEY_RES.message);
                if (obj_info != null) {
//                    String id = JSONParser.getString(obj_info, "id");
//                    TNPreference.setMemberID(getActivity(), id);
//                    String name = JSONParser.getString(obj_info, "name");
//                    TNPreference.setMemName(getActivity(), name);
//                    String mobile = JSONParser.getString(obj_info, "mobile");
//                    TNPreference.setMemPhoneNumber(getActivity(), mobile);
//                    String email = JSONParser.getString(obj_info, "email");
//                    TNPreference.setMemEMail(getActivity(), email);
//                    String area = JSONParser.getString(obj_info, "area");
//                    TNPreference.setMemAddr(getActivity(), area);
//                    String gender = JSONParser.getString(obj_info, "gender");
//                    TNPreference.setMemSex(getActivity(), gender);
//                    int level = JSONParser.getInt(obj_info, "level", 0);
//                    TNPreference.setMemGrade(getActivity(), String.valueOf(level));
//                    String duplicate = JSONParser.getString(obj_info, "duplicate");
//                    //TNPreference.setMemName(getActivity(), name);
//                    String  certification = JSONParser.getString(obj_info, "certification");
//                    //TNPreference.setMemName(getActivity(), name);
//                    String status = JSONParser.getString(obj_info, "status");
//                    //TNPreference.setMemName(getActivity(), name);
//                    Long createdAt = JSONParser.getLong(obj_info, "createdAt", 0L);
//                    //TNPreference.setMemName(getActivity(), name);
//                    String genderName = JSONParser.getString(obj_info, "genderName");
//                    TNPreference.setMemGenderName(getActivity(), genderName);
//                    String statusName = JSONParser.getString(obj_info, "statusName");
//                    //TNPreference.setMemName(getActivity(), name);
//                    String birth = JSONParser.getString(obj_info, "birth");
//                    TNPreference.setMemBirthDay(getActivity(), birth);

                    String id = JSONParser.getString(obj_info, "id");
                    TNPreference.setMemberID(getActivity(), id);
                    String storeLevel = JSONParser.getString(obj_info, "level");
                    String name = JSONParser.getString(obj_info, "name");
                    String mobile = JSONParser.getString(obj_info, "mobile");
                    //String szHello = String.valueOf(level) + "학년 " + name + "님 안녕하세요";
                    String szHello = name + "님 안녕하세요";
                    mTvHello.setText(szHello);
                }
            }
        }

        if (o[0] instanceof ResFranchiseeOwnerInfo) {
            ResFranchiseeOwnerInfo res = (ResFranchiseeOwnerInfo) o[0];
            JSONObject obj = res.getParseData();
            if (JSONParser.isSuccess(obj)) {
                JSONObject obj_info = JSONParser.getObject(obj, ResFranchiseeOwnerInfo.KEY_RES.message);
                if (obj_info != null) {
                    String id = JSONParser.getString(obj_info, "id");
                    String name = JSONParser.getString(obj_info, "name");
                    String mobile = JSONParser.getString(obj_info, "mobile");
                    String email = JSONParser.getString(obj_info, "email");
                    String birth = JSONParser.getString(obj_info, "birth");
                    String gender = JSONParser.getString(obj_info, "gender");
                    String genderName = JSONParser.getString(obj_info, "genderName");
                    String status = JSONParser.getString(obj_info, "status");
                    String statusName = JSONParser.getString(obj_info, "statusName");
                    String createdAt = JSONParser.getString(obj_info, "createdAt");
                    String updatedAt = JSONParser.getString(obj_info, "updatedAt");

                    FranchiseeMainFragment mainFragment = (FranchiseeMainFragment) getFragmentManager().findFragmentByTag(FranchiseeMainFragment.FRAGMENT_TAG);
                    if (mainFragment != null) {
                        mainFragment.mSelEmail = email;
                        mainFragment.mSelBirthDay = birth.substring(0, 4) + "년" + birth.substring(4, 6) + " 월" + birth.substring(6, 8) + " 일";
                    }

                }
            }
        }

        if (o[0] instanceof ResFranchiseeStore) {
            ResFranchiseeStore res = (ResFranchiseeStore) o[0];
            JSONObject obj = res.getParseData();
            if (JSONParser.isSuccess(obj)) {
                // {"result":true,"message":
                // [{"level":"1","sid":"S62354340","ssn":"2208728885","stampUse":"N","sectorsName":"한식","joinDate":1540134000000,"phone":"0214089575","earn":"T","name":"금대한우도매식당","statusName":"정상","rpsUse":"N","id":"rkaodghldnjs1","addr":"서울 금천구 독산동 293-4","status":"1","pointUse":"Y"},
                // {"level":"1","sid":"S509339245","ssn":"7878700239","stampUse":"N","sectorsName":"전문음식","joinDate":1540134000000,"phone":"028665562","earn":"S","name":"누리한방삼계탕","statusName":"정상","rpsUse":"Y","id":"rkaodghldnjs1","addr":"서울 구로구 구로동 212-8","status":"1","pointUse":"Y"},{"level":"1","sid":"S914186059","ssn":"8888800234","stampUse":"N","sectorsName":"양식","joinDate":1538319600000,"phone":"028614827","earn":"T","name":"독산장작불 독산점","statusName":"정상","rpsUse":"N","id":"rkaodghldnjs1","addr":"서울 금천구 시흥대로126길 4","status":"1","pointUse":"Y"},{"level":"1","sid":"S639286503","ssn":"7878700239","stampUse":"N","sectorsName":"한식","joinDate":1540134000000,"phone":"028530118","earn":"T","name":"서대문김치찜 독산점","statusName":"정상","rpsUse":"Y","id":"rkaodghldnjs1","addr":"서울 금천구 가산로 49-16","status":"1","pointUse":"Y"}]}
                JSONArray array_stores = JSONParser.getArray(obj, ResInfo.KEY_RES.message);
                if (array_stores != null) {
                    for (int i = 0; i < array_stores.length(); i++) {
                        JSONObject obj_row = JSONParser.getArrayItem(array_stores, i);
                        if (obj_row != null) {
                            String level = JSONParser.getString(obj_row, "level");
                            String sid = JSONParser.getString(obj_row, "sid");
                            String ssn = JSONParser.getString(obj_row, "ssn");
                            String stampUse = JSONParser.getString(obj_row, "stampUse");
                            String sectorsName = JSONParser.getString(obj_row, "sectorsName");
                            String joinDate = JSONParser.getString(obj_row, "joinDate");
                            String phone = JSONParser.getString(obj_row, "phone");
                            String earn = JSONParser.getString(obj_row, "earn");
                            String name = JSONParser.getString(obj_row, "name");
                            String statusName = JSONParser.getString(obj_row, "statusName");
                            String rpsUse = JSONParser.getString(obj_row, "rpsUse");
                            String id = JSONParser.getString(obj_row, "id");
                            String addr = JSONParser.getString(obj_row, "addr");
                            String status = JSONParser.getString(obj_row, "status");
                            String pointUse = JSONParser.getString(obj_row, "pointUse");

                            if (sid != null && sid.length() > 0 && sid.compareTo("S914186059") == 0) {
                                requestFav(sid);
                                requestFavCnt(sid);
                                requestFavAvg(sid);

                                if (pointUse.compareTo("Y") == 0)
                                    mTvBonusType.setText("보너스 - 포인트");
                                else if (stampUse.compareTo("Y") == 0)
                                    mTvBonusType.setText("보너스 - 스탬프");
                                else
                                    mTvBonusType.setText("보너스 - 없음");

                                if (earn.compareTo("T") == 0)
                                    mTvStoreType.setText("유형 - 통합" + "[" + name + "]");
                                else if (stampUse.compareTo("G") == 0)
                                    mTvStoreType.setText("유형 - 그룹" + "[" + name + "]");
                                else
                                    mTvStoreType.setText("유형 - 단독" + "[" + name + "]");

                                mTvFranchiseeGrade.setText("등급 - " + level);
                                //mTvUsersGrade.setText("고객 호감 : ");


                                FranchiseeMainFragment mainFragment = (FranchiseeMainFragment) getFragmentManager().findFragmentByTag(FranchiseeMainFragment.FRAGMENT_TAG);
                                if (mainFragment != null) {
                                    mainFragment.mSelStoreId = sid;
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }


        if (o[0] instanceof ResFranchiseeStoreFav) {
            ResFranchiseeStoreFav res = (ResFranchiseeStoreFav) o[0];
            JSONObject obj = res.getParseData();
            if (JSONParser.isSuccess(obj)) {
                JSONObject obj_msg = JSONParser.getObject(obj, ResInfo.KEY_RES.message);
                if(obj_msg != null) {
                    //Long createdAt = JSONParser.getLong(obj_info, "createdAt", 0L);
                }
            }
        }

        if (o[0] instanceof ResFranchiseeStoreFavCnt) {
            ResFranchiseeStoreFavCnt res = (ResFranchiseeStoreFavCnt) o[0];
            JSONObject obj = res.getParseData();
            if (JSONParser.isSuccess(obj)) {
                int userGradeCount = JSONParser.getInt(obj, ResInfo.KEY_RES.message, 0);
                mUsersGradeCount = String.format("%02d", userGradeCount);

                mTvUsersGrade.setText("고객 호감 - " + mUsersGradeLevel + "(" + mUsersGradeCount + ")");
            }
        }

        if (o[0] instanceof ResFranchiseeStoreFavAvg) {
            ResFranchiseeStoreFavAvg res = (ResFranchiseeStoreFavAvg) o[0];
            JSONObject obj = res.getParseData();
            if (JSONParser.isSuccess(obj)) {
                mUsersGradeLevel = JSONParser.getString(obj, ResInfo.KEY_RES.message);

                mTvUsersGrade.setText("고객 호감 - " + mUsersGradeLevel + "(" + mUsersGradeCount + ")");
            }
        }
    }

    @Override
    public void onHttpDebugEvent(String debug_msg) {

    }

    @Override
    public void onHttpNetFailEvent(int errorCode, String errorMsg) {

    }

    @Override
    public void onHttpNetFailResultListener(BaseHttpResource resource) {

    }

    @Override
    public void onClick(View view) {

        FranchiseeMainFragment mainFragment = (FranchiseeMainFragment) getFragmentManager().findFragmentByTag(FranchiseeMainFragment.FRAGMENT_TAG);

        switch (view.getId()) {
            case R.id.btn_company_intro:
                if (mainFragment != null) {
                    mainFragment.onGoIntro(TNConstants.WEBVIEW.TYPE_INTRO_COMPANY);
                }
                break;
            case R.id.btn_franchisee_store_info:
                if (mainFragment != null) {
                    mainFragment.onGoStoreInfo();
                }
                break;
            case R.id.btn_franchisee_store_intro:
                if (mainFragment != null) {
                    mainFragment.onGoStoreIntro();
                }
                break;
            case R.id.btn_franchisee_store_usersinfo:
                if (mainFragment != null) {
                    mainFragment.onGoUsersInfo();
                }
                break;
            case R.id.btn_franchisee_detail_list:
                if (mainFragment != null) {
                    mainFragment.onGoDetailList();
                }
                break;
            case R.id.btn_franchisee_monthly_list:
                if (mainFragment != null) {
                    mainFragment.onGoMonthlyList();
                }
                break;
            case R.id.btn_franchisee_ad_tips:
                if (mainFragment != null) {
                    mainFragment.onGoAdTips();
                }
                break;
            case R.id.btn_franchisee_notices:
                if (mainFragment != null) {
                    mainFragment.onGoNotices();
                }
                break;

            case R.id.tv_company_intro:
                if (mainFragment != null) {
                    mainFragment.onGoIntro(TNConstants.WEBVIEW.TYPE_INTRO_COMPANY);
                }
                break;
            case R.id.tv_touchall_intro:
                if (mainFragment != null) {
                    mainFragment.onGoIntro(TNConstants.WEBVIEW.TYPE_INTRO_TOUCHALL);
                }
                break;
        }
    }

    @Override
    public void onBack() {
        FranchiseeMainFragment fragment = (FranchiseeMainFragment) getFragmentManager().findFragmentByTag(FranchiseeMainFragment.FRAGMENT_TAG);
        if (fragment != null) {
            fragment.onBack();
        }
    }

    public void onReflash() {
        requestMain();
    }
}
