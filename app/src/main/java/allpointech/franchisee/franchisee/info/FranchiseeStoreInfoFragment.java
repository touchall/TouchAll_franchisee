package allpointech.franchisee.franchisee.info;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.tuna.ui.activity.BaseAppCompatActivity;
import com.tuna.ui.fragment.BaseDialogFragment;
import com.tuna.ui.fragment.BaseFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import allpointech.franchisee.R;
import allpointech.franchisee.dialog.MsgSingleDialog;
import allpointech.franchisee.franchisee.FranchiseeMainFragment;
import allpointech.franchisee.franchisee.device.FranchiseeDeviceSetupActivity;
import allpointech.franchisee.network.http.TNHttpMultiPartTask;
import allpointech.franchisee.network.http.json.JSONParser;
import allpointech.franchisee.network.http.resource.BaseHttpResource;
import allpointech.franchisee.network.http.resource.data.ResFranchiseeStoreDeviceDetail;
import allpointech.franchisee.network.http.resource.data.ResFranchiseeStoreDevices;
import allpointech.franchisee.network.http.resource.data.ResFranchiseeStoreInfo;
import allpointech.franchisee.network.http.resource.data.ResFranchiseeStoreInfoUpdate;
import allpointech.franchisee.network.http.resource.data.ResFranchiseeStoreServices;
import allpointech.franchisee.network.http.resource.data.ResFranchiseeStoreStempInfo;
import allpointech.franchisee.network.http.resource.data.ResInfo;
import allpointech.franchisee.utils.TNConstants;
import allpointech.franchisee.utils.TNPreference;

/**
 * Created by jay on 2019-02-18.
 */

public class FranchiseeStoreInfoFragment extends BaseFragment implements BaseAppCompatActivity.onKeyBackPressedListener, TNHttpMultiPartTask.onHttpNetResultListener, View.OnClickListener {
    public static final String FRAGMENT_TAG = FranchiseeStoreInfoFragment.class.getSimpleName();

    private Toolbar mToolbar;

    private EditText mEdStoreName;
    private EditText mEdStoreBusinessNumber;
    private EditText mEdCEOName;
    private EditText mEdPassword;
    private EditText mEdCEOPhone;
    private EditText mEdStoreMail;
    private EditText mEdStoreTel;
    private EditText mEdStoreFax;
    private EditText mEdManagerName;
    private EditText mEdManagerPhone;
    private EditText mEdManagerMail;
    private EditText mEdEarn;
    private EditText mEdEarnDetail;
//    private Spinner mSpinnerAddressState;
//    private Spinner mSpinnerAddressGun;
//    private Spinner mSpinnerAddressDong;
    private EditText mEdAddress;
    private EditText mEdAddressDetail;
    private Spinner mSpinnerBanks;
    private EditText mEdBankAccount;
    private EditText mEdBankUser;
    private EditText mEdAds;
    private EditText mEdEvent;
    private EditText mEdDeposit;
    private Spinner mSpinnerWifi;
    private Spinner mSpinnerLan;
    private EditText mEdAnnualFee;
    private EditText mEdMaintenceCost;
    private EditText mEdNonActiveCost;
    private EditText mEdRegDate;
    private EditText mEdDelDate;
    private EditText mEdStopDate;
    private EditText mEdCurrentSetup;
    private EditText mEdDeviceSerial;
    private EditText mEdAgent;
    private EditText mEdAgentPhone;
    private EditText mEdAgency;
    private EditText mEdAgencyPhone;

    private CheckBox mChkStorePoint;
    private CheckBox mChkStoreStemp;
    private CheckBox mChkStoreGame;

    private CheckBox mChkStoreTotal;
    private TextView mTvStoreTotalPoint;
    private CheckBox mChkStoreGroup;
    private TextView mTvStoreGroupPoint;
    private CheckBox mChkStoreSingle;
    private TextView mTvStoreSinglePoint;
    private TextView mTvStoreSingleStempMoney;
    private TextView mTvStoreSingleGameMoney;
    private EditText mEdStoreGroupCode;
    private TextView mTvStoreGroupName;


    private TextView mTvStoreVipGrade;
    private TextView mTvStorePayMoney;
    private TextView mTvStorePayType;
    private TextView mTvStorePayTime;
    private TextView mTvStoreDeviceTimeout;

    private EditText mEdStoreStempItem;
    private EditText mEdStoreStempItemPrice;
    private EditText mEdStoreStempItemCount;
    private EditText mEdStoreStempItemOnePrice;
    private EditText mEdStoreStempItemUseLimit;

    private Button mButtonDeviceSetup;
    private Button mButtonNext;
    private Button mButtonUpdate;

    private String mStoreId;
    private String mEMail;
    private String mBirthDay;
    private String mStoreSectors;
    private String mStoreZipcode;
    private String mDeviceSerialNo;

    private boolean _bTestStoreService = true;

    @Override
    protected void BundleData(Bundle bundle) {
        setHasOptionsMenu(true);
        ((BaseAppCompatActivity) getActivity()).setOnKeyBackPressedListener(this);
        //mCurrentState = State.REG;
        if (getArguments() != null) {
            mStoreId = getArguments().getString("sid");
            mEMail = getArguments().getString("email");
            mBirthDay = getArguments().getString("birth");
        }
    }

    @Override
    protected int inflateLayout() {
        return R.layout.fragment_franchisee_store_info;
    }

    @Override
    protected void initLayout(View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((BaseAppCompatActivity) getActivity()).setSupportActionBar(mToolbar);

        mEdStoreName = (EditText)view.findViewById(R.id.franchiseeinfo_edit_store_name);
        mEdStoreBusinessNumber = (EditText)view.findViewById(R.id.franchiseeinfo_edit_birthday);
        mEdCEOName = (EditText)view.findViewById(R.id.franchiseeinfo_edit_ceo_name);
        mEdPassword = (EditText)view.findViewById(R.id.franchiseeinfo_edit_password);
        mEdCEOPhone = (EditText)view.findViewById(R.id.franchiseeinfo_edit_ceo_phone);
        mEdStoreMail = (EditText)view.findViewById(R.id.franchiseeinfo_edit_store_mail);
        mEdStoreTel = (EditText)view.findViewById(R.id.franchiseeinfo_edit_store_tel);
        mEdStoreFax = (EditText)view.findViewById(R.id.franchiseeinfo_edit_store_fax);
        mEdManagerName = (EditText)view.findViewById(R.id.franchiseeinfo_edit_manager_name);
        mEdManagerPhone = (EditText)view.findViewById(R.id.franchiseeinfo_edit_manager_phone);
        mEdManagerMail = (EditText)view.findViewById(R.id.franchiseeinfo_edit_manager_mail);
        mEdEarn = (EditText) view.findViewById(R.id.franchiseeinfo_edit_earn);
        mEdEarnDetail = (EditText)view.findViewById(R.id.franchiseeinfo_edit_earn_detail);
//        mSpinnerAddressState = (Spinner) view.findViewById(R.id.franchiseeinfo_spinner_address_state);
//        mSpinnerAddressGun = (Spinner) view.findViewById(R.id.franchiseeinfo_spinner_address_gun);
//        mSpinnerAddressDong = (Spinner) view.findViewById(R.id.franchiseeinfo_spinner_address_dong);
        mEdAddress = (EditText)view.findViewById(R.id.franchiseeinfo_edit_address);
        mEdAddressDetail = (EditText)view.findViewById(R.id.franchiseeinfo_edit_address_detail);
        mSpinnerBanks = (Spinner)view.findViewById(R.id.franchiseeinfo_spinner_banks);
        mEdBankAccount = (EditText)view.findViewById(R.id.franchiseeinfo_edit_bank_account);
        mEdBankUser = (EditText)view.findViewById(R.id.franchiseeinfo_edit_bank_account_user);
        mEdAds = (EditText)view.findViewById(R.id.franchiseeinfo_edit_ads);
        mEdEvent = (EditText)view.findViewById(R.id.franchiseeinfo_edit_event);
        mEdDeposit = (EditText)view.findViewById(R.id.franchiseeinfo_edit_deposit);
        mSpinnerWifi = (Spinner)view.findViewById(R.id.franchiseeinfo_spinner_wifi);
        mSpinnerLan = (Spinner)view.findViewById(R.id.franchiseeinfo_spinner_lan);
        mEdAnnualFee = (EditText)view.findViewById(R.id.franchiseeinfo_edit_annual_fee);
        mEdMaintenceCost = (EditText)view.findViewById(R.id.franchiseeinfo_edit_maintenence_cost);
        mEdNonActiveCost = (EditText)view.findViewById(R.id.franchiseeinfo_edit_non_active_cost);
        mEdRegDate = (EditText)view.findViewById(R.id.franchiseeinfo_edit_reg_date);
        mEdDelDate = (EditText)view.findViewById(R.id.franchiseeinfo_edit_del_date);
        mEdStopDate = (EditText)view.findViewById(R.id.franchiseeinfo_edit_stop_date);
        mEdCurrentSetup = (EditText)view.findViewById(R.id.franchiseeinfo_edit_current_setup);
        mEdDeviceSerial = (EditText)view.findViewById(R.id.franchiseeinfo_edit_device_serial);
        mEdAgent = (EditText)view.findViewById(R.id.franchiseeinfo_edit_agent);
        mEdAgentPhone = (EditText)view.findViewById(R.id.franchiseeinfo_edit_agent_phone);
        mEdAgency = (EditText)view.findViewById(R.id.franchiseeinfo_edit_agency);
        mEdAgencyPhone = (EditText)view.findViewById(R.id.franchiseeinfo_edit_agency_phone);


        mChkStorePoint = (CheckBox)view.findViewById(R.id.check_store_point);
        mChkStoreStemp = (CheckBox)view.findViewById(R.id.check_store_stemp);
        mChkStoreGame = (CheckBox)view.findViewById(R.id.check_store_game);

        mChkStoreTotal = (CheckBox)view.findViewById(R.id.check_store_total);
        mTvStoreTotalPoint = (TextView)view.findViewById(R.id.text_store_point);
        mChkStoreGroup = (CheckBox)view.findViewById(R.id.check_store_group);
        mTvStoreGroupPoint = (TextView)view.findViewById(R.id.text_store_group_point);
        mChkStoreSingle = (CheckBox)view.findViewById(R.id.check_store_single);
        mTvStoreSinglePoint = (TextView)view.findViewById(R.id.text_store_single_point);
        mTvStoreSingleStempMoney = (TextView)view.findViewById(R.id.text_store_single_stemp_money);
        mTvStoreSingleGameMoney = (TextView)view.findViewById(R.id.text_store_single_game_money);
        mEdStoreGroupCode = (EditText) view.findViewById(R.id.edit_franchisee_store_info_group_code);
        mEdStoreGroupCode.setEnabled(false);
        mTvStoreGroupName = (TextView)view.findViewById(R.id.tv_franchisee_store_info_group_name);

        mTvStoreVipGrade = (TextView)view.findViewById(R.id.text_store_vip_grade);
        mTvStorePayMoney = (TextView)view.findViewById(R.id.text_store_paymoney);
        mTvStorePayType = (TextView)view.findViewById(R.id.text_store_pay_type);
        mTvStorePayTime = (TextView)view.findViewById(R.id.text_store_pay_time);
        mTvStoreDeviceTimeout = (TextView)view.findViewById(R.id.text_store_device_timeout);

        mEdStoreStempItem = (EditText)view.findViewById(R.id.edit_store_stemp_item);
        mEdStoreStempItemPrice = (EditText)view.findViewById(R.id.edit_store_stemp_item_price);
        mEdStoreStempItemCount = (EditText)view.findViewById(R.id.edit_store_stemp_item_count);
        mEdStoreStempItemOnePrice = (EditText)view.findViewById(R.id.edit_store_stemp_item_one_price);
        mEdStoreStempItemUseLimit = (EditText)view.findViewById(R.id.edit_store_stemp_item_use_limit);

        mButtonDeviceSetup = (Button)view.findViewById(R.id.button_franchisee_store_device_setup);
        mButtonDeviceSetup.setOnClickListener(this);
        mButtonNext = (Button)view.findViewById(R.id.button_franchisee_store_close);
        mButtonNext.setOnClickListener(this);
        mButtonUpdate = (Button)view.findViewById(R.id.button_franchisee_store_update);
        mButtonUpdate.setOnClickListener(this);
    }

    private void requestStoreInfo() {
        //통신
        // retune_code 에 따라 mTvFindMsg msg 변경
        ResFranchiseeStoreInfo res = new ResFranchiseeStoreInfo();
        res.setToken(TNPreference.getMemToken(getActivity()));

        res.setParameterQuestion(mStoreId);

        TNHttpMultiPartTask task = new TNHttpMultiPartTask(getActivity(), getFragmentManager());
        task.setOnHttpResultListener(this);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, res);
    }

    private void requestStoreServices() {
        //통신
        // retune_code 에 따라 mTvFindMsg msg 변경
        ResFranchiseeStoreServices res = new ResFranchiseeStoreServices();
        res.setToken(TNPreference.getMemToken(getActivity()));

        if (_bTestStoreService)
            res.setParameterQuestion("S62354340", "extra");
        else
            res.setParameterQuestion(mStoreId, "extra");

        TNHttpMultiPartTask task = new TNHttpMultiPartTask(getActivity(), getFragmentManager());
        task.setOnHttpResultListener(this);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, res);
    }

    private void requestStoreDevices() {
        //통신
        // retune_code 에 따라 mTvFindMsg msg 변경
        ResFranchiseeStoreDevices res = new ResFranchiseeStoreDevices();
        res.setToken(TNPreference.getMemToken(getActivity()));

        TNHttpMultiPartTask task = new TNHttpMultiPartTask(getActivity(), getFragmentManager());
        task.setOnHttpResultListener(this);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, res);
    }

    private void requestStoreDeviceDetial(String deviceSerialNo) {
        //통신
        // retune_code 에 따라 mTvFindMsg msg 변경
        ResFranchiseeStoreDeviceDetail res = new ResFranchiseeStoreDeviceDetail();
        res.setToken(TNPreference.getMemToken(getActivity()));

        res.setParameterQuestion(deviceSerialNo);

        TNHttpMultiPartTask task = new TNHttpMultiPartTask(getActivity(), getFragmentManager());
        task.setOnHttpResultListener(this);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, res);
    }

    private void requestStoreStempInfo() {
        //통신
        // retune_code 에 따라 mTvFindMsg msg 변경
        ResFranchiseeStoreStempInfo res = new ResFranchiseeStoreStempInfo();
        res.setToken(TNPreference.getMemToken(getActivity()));

        //if (_bTestStoreService)
        //    res.setParameterQuestion("S62354340", "prize");
        //else
            res.setParameterQuestion(mStoreId, "prize");

        TNHttpMultiPartTask task = new TNHttpMultiPartTask(getActivity(), getFragmentManager());
        task.setOnHttpResultListener(this);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, res);
    }

    private void requestStoreInfoUpdate(
            String storeId,
            String storeName,
            String ssn,
            String sectors,
            String phone,
            String fax,
            String zipcode,
            String addr,
            String address,
            String gendor) {

        //통신
        // retune_code 에 따라 mTvFindMsg msg 변경
        ResFranchiseeStoreInfoUpdate res = new ResFranchiseeStoreInfoUpdate();
        res.setToken(TNPreference.getMemToken(getActivity()));

        res.setParameter(storeId, storeName, ssn, sectors, phone, fax, zipcode, addr, address, gendor);

        TNHttpMultiPartTask task = new TNHttpMultiPartTask(getActivity(), getFragmentManager());
        task.setOnHttpResultListener(this);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, res);
    }

    @Override
    protected void initRequest() {
//        mEditUserName.setText(UserInfoActivity.userName);
//        mEditBirthDay.setText(UserInfoActivity.userBirth);
//        mEditPhoneNumber.setText(UserInfoActivity.userMobile);
        requestStoreInfo();
        requestStoreServices();
        requestStoreDevices();
        requestStoreStempInfo();
    }

    private boolean onCheck() {



        return true;
    }

    public void onReflash() {
        initRequest();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_franchisee_store_device_setup:
                Intent i = new Intent(getActivity(), FranchiseeDeviceSetupActivity.class);
                i.putExtra("device_serial", mDeviceSerialNo);
                startActivityForResult(i, TNConstants.REQUEST_CODE.DEVICE_SETUP);
                break;
            case R.id.button_franchisee_store_close:
                onBack();
                break;
            case R.id.button_franchisee_store_update:
                if (onCheck())
                    requestStoreInfoUpdate(mStoreId,
                            mEdStoreName.getText().toString(),
                            mEdStoreBusinessNumber.getText().toString(),
                            mStoreSectors,
                            mEdStoreTel.getText().toString(),
                            mEdStoreFax.getText().toString(),
                            mStoreZipcode,
                            mEdAddress.getText().toString(),
                            mEdAddressDetail.getText().toString(),
                            "" //gendor
                            );
                break;
        }

    }

    public String convertTime(long time){
        Date date = new Date(time);
        //Format format = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm:ss");
        Format format = new SimpleDateFormat("yyyy년 MM월 dd일");
        return format.format(date);
    }

    @Override
    public void onHttpNetSuccessEvent(BaseHttpResource[] o) {
        if (o[0] instanceof ResFranchiseeStoreInfo) {
            ResFranchiseeStoreInfo res = (ResFranchiseeStoreInfo) o[0];
            JSONObject obj = res.getParseData();
            if (JSONParser.isSuccess(obj)) {

                JSONObject obj_info = JSONParser.getObject(obj, ResInfo.KEY_RES.message);
                if (obj_info != null) {

                    //{"owner":"김이박",
                    // "sectors":"0604",
                    // "address":"독산장작불",
                    // "manager":"대리점",
                    // "subs":0,
                    // "devices":1,
                    // "author":"rkaodghldnjs1",
                    // "mid":"eoflwja",
                    // "sid":"S914186059",
                    // "ssn":"8888800234",
                    // "zipcode":"08576",
                    // "createdAt":1540799962664,
                    // "sectorsName":"양식",
                    // "joinDate":1538319600000,
                    // "phone":"028614827",
                    // "name":"독산장작불 독산점",
                    // "statusName":"정상",
                    // "id":"rkaodghldnjs1",
                    // "accounts":0,
                    // "addr":"서울 금천구 시흥대로126길 4",
                    // "status":"1"}
                    String owner = JSONParser.getString(obj_info, "owner");
                    String sectors = JSONParser.getString(obj_info, "sectors");
                    String address = JSONParser.getString(obj_info, "address");
                    String manager = JSONParser.getString(obj_info, "manager");
                    int subs = JSONParser.getInt(obj_info, "subs", 0);
                    int devices = JSONParser.getInt(obj_info, "devices", 0);
                    String author = JSONParser.getString(obj_info, "author");
                    String mid = JSONParser.getString(obj_info, "mid");
                    String sid = JSONParser.getString(obj_info, "sid");
                    String ssn = JSONParser.getString(obj_info, "ssn");
                    String zipcode = JSONParser.getString(obj_info, "zipcode");
                    String createdAt = JSONParser.getString(obj_info, "createdAt");
                    String sectorsName = JSONParser.getString(obj_info, "sectorsName");
                    Long joinDate = JSONParser.getLong(obj_info, "joinDate", 0L);
                    String phone = JSONParser.getString(obj_info, "phone");
                    String name = JSONParser.getString(obj_info, "name");
                    String statusName = JSONParser.getString(obj_info, "statusName");
                    String id = JSONParser.getString(obj_info, "id");
                    int accounts = JSONParser.getInt(obj_info, "accounts", 0);
                    String addr = JSONParser.getString(obj_info, "addr");
                    String status = JSONParser.getString(obj_info, "status");

                    //String datetime = convertTime(Long.getLong(createdAt));

                    //long time_create = Long.getLong(createdAt, 0L);
                    //long time_join = Long.getLong(joinDate, 0L);

                    mStoreSectors = sectors;
                    mStoreZipcode = zipcode;

                    mEdStoreName.setText(name);
                    mEdStoreBusinessNumber.setText(ssn);
                    mEdCEOName.setText(owner);
                    mEdPassword.setText(TNPreference.getMemPw(getActivity())); // 어떻게 표현 ?
                    mEdCEOPhone.setText(phone); // 대표자 핸폰 ?
                    mEdStoreMail.setText(mEMail); // 대표자 메일 ?
                    mEdStoreTel.setText(phone);
                    mEdStoreFax.setText(phone);  // fax 번호 ?
                    mEdManagerName.setText(manager);
                    mEdManagerPhone.setText(phone);  // 매니저 전화 ??
                    mEdManagerMail.setText(phone);  // 매니저 메일 ??
                    //mSpinnerEarn = (Spinner)view.findViewById(R.id.franchiseeinfo_spinner_earn);
                    //mSpinnerEarnDetail = (Spinner)view.findViewById(R.id.franchiseeinfo_spinner_earn_detail);
                    mEdAddress.setText(addr);
                    mEdAddressDetail.setText(address);
                    //mSpinnerBanks = (Spinner)view.findViewById(R.id.franchiseeinfo_spinner_banks);
                    mEdBankAccount.setText("");
                    mEdBankUser.setText("");
                    mEdAds.setText(""); // 광고 ??
                    mEdEvent.setText(""); // 이벤트 ??
                    mEdDeposit.setText(""); // 이벤트 ??
                    //mSpinnerWifi = (Spinner)view.findViewById(R.id.franchiseeinfo_spinner_wifi);
                    //mSpinnerLan = (Spinner)view.findViewById(R.id.franchiseeinfo_spinner_lan);
                    mEdAnnualFee.setText(""); // ??
                    mEdMaintenceCost.setText(""); // ??
                    mEdNonActiveCost.setText(""); // ??
                    mEdRegDate.setText(""); // ??
                    mEdDelDate.setText(""); // ??
                    mEdStopDate.setText(""); // ??
                    mEdCurrentSetup.setText(""); // ??
                    mEdDeviceSerial.setText(""); // ??
                    mEdAgent.setText(""); // ??
                    mEdAgentPhone.setText(""); // ??
                    mEdAgency.setText(""); // ??
                    mEdAgencyPhone.setText(""); // ??

//                    if (devices > 0) {
//                        requestDevices();
//                    }
                }
            }
        }

        // 광고 정보.
        if (o[0] instanceof ResFranchiseeStoreServices) {
            ResFranchiseeStoreServices res = (ResFranchiseeStoreServices) o[0];
            JSONObject obj = res.getParseData();
            if (JSONParser.isSuccess(obj)) {
                JSONObject obj_message = JSONParser.getObject(obj, ResFranchiseeStoreServices.KEY_RES.message);
                if (obj_message != null) {
                    String sid = JSONParser.getString(obj_message, "sid");
                    String code = JSONParser.getString(obj_message, "code");
                    String name = JSONParser.getString(obj_message, "name");
                    Long sdate = JSONParser.getLong(obj_message, "sdate", 0L);
                    Long edate = JSONParser.getLong(obj_message, "edate", 0L);
                    int dues = JSONParser.getInt(obj_message, "dues", 0);
                    int fee = JSONParser.getInt(obj_message, "fee", 0);
                }
            }
        }

        if (o[0] instanceof ResFranchiseeStoreDevices) {
            ResFranchiseeStoreDevices res = (ResFranchiseeStoreDevices) o[0];
            JSONObject obj = res.getParseData();
            if (JSONParser.isSuccess(obj)) {
                JSONArray array_devices = JSONParser.getArray(obj, ResInfo.KEY_RES.message);
                if (array_devices != null && array_devices.length() > 0) {
                    for (int i = 0; i < array_devices.length(); i++) {
                        JSONObject obj_row = JSONParser.getArrayItem(array_devices, i);
                        if (obj_row != null) {
                            String stampUse = JSONParser.getString(obj_row, "stampUse");
                            String createdAt = JSONParser.getString(obj_row, "createdAt");
                            String transfer = JSONParser.getString(obj_row, "transfer");
                            String earnType = JSONParser.getString(obj_row, "earnType");
                            String serial = JSONParser.getString(obj_row, "serial");
                            String vat = JSONParser.getString(obj_row, "vat");
                            String rpsUse = JSONParser.getString(obj_row, "rpsUse");
                            String store = JSONParser.getString(obj_row, "store");
                            String sid = JSONParser.getString(obj_row, "sid");
                            String pointUse = JSONParser.getString(obj_row, "pointUse");

//                            mChkStorePoint.setChecked(pointUse.compareTo("Y") == 0);
//                            mChkStoreStemp.setChecked(stampUse.compareTo("Y") == 0);
//                            mChkStoreGame.setChecked(rpsUse.compareTo("Y") == 0);
//
//                            mChkStoreTotal.setChecked(earnType.compareTo("통합형") == 0);
//                            mTvStoreTotalPoint.setText("-%");
//                            mChkStoreGroup.setChecked(earnType.compareTo("그룹형") == 0);
//                            mTvStoreGroupPoint.setText("-%");
//                            mChkStoreSingle.setChecked(earnType.compareTo("단독형") == 0);
//                            mTvStoreSinglePoint.setText("-%");
//                            mTvStoreSingleStempMoney.setText("기준금액당\n-원당 1개");
//                            mTvStoreSingleGameMoney.setText("기본승점 X\n기준금액 -원당 1게임승점");
//                            mTvStoreGroupCode.setText("");

                            mDeviceSerialNo = serial;
                            requestStoreDeviceDetial(mDeviceSerialNo);
//
//                            mTvStoreVipGrade = (TextView)view.findViewById(R.id.text_store_vip_grade);
//                            mTvStorePayMoney = (TextView)view.findViewById(R.id.text_store_paymoney);
//                            mTvStorePayType = (TextView)view.findViewById(R.id.text_store_pay_type);
//                            mTvStorePayTime = (TextView)view.findViewById(R.id.text_store_pay_time);
//                            mTvStoreDeviceTimeout = (TextView)view.findViewById(R.id.text_store_device_timeout);
//
//                            mEdStoreStempItem = (EditText)view.findViewById(R.id.edit_store_stemp_item);
//                            mEdStoreStempItemPrice = (EditText)view.findViewById(R.id.edit_store_stemp_item_price);
//                            mEdStoreStempItemCount = (EditText)view.findViewById(R.id.edit_store_stemp_item_count);
//                            mEdStoreStempItemOnePrice = (EditText)view.findViewById(R.id.edit_store_stemp_item_one_price);
//                            mEdStoreStempItemUseLimit = (EditText)view.findViewById(R.id.edit_store_stemp_item_use_limit);

                            break;
                        }
                    }
                }
            }
        }

        if (o[0] instanceof ResFranchiseeStoreDeviceDetail) {
            ResFranchiseeStoreDeviceDetail res = (ResFranchiseeStoreDeviceDetail) o[0];
            JSONObject obj = res.getParseData();
            if (JSONParser.isSuccess(obj)) {
                JSONObject obj_message = JSONParser.getObject(obj, ResInfo.KEY_RES.message);
                if (obj_message != null) {
                    String serial = JSONParser.getString(obj_message, "serial");
                    String sid = JSONParser.getString(obj_message, "sid");
                    String vat = JSONParser.getString(obj_message, "vat");
                    String transfer = JSONParser.getString(obj_message, "transfer");
                    String delay = JSONParser.getString(obj_message, "delay");
                    String pointUse = JSONParser.getString(obj_message, "pointUse");
                    String stampUse = JSONParser.getString(obj_message, "stampUse");
                    String rpsUse = JSONParser.getString(obj_message, "rpsUse");
                    String remnant = JSONParser.getString(obj_message, "remnant");
                    String receipt = JSONParser.getString(obj_message, "receipt");
                    String leaflet = JSONParser.getString(obj_message, "leaflet");
                    String earn = JSONParser.getString(obj_message, "earn");
                    String point = JSONParser.getString(obj_message, "point");
                    String stamp = JSONParser.getString(obj_message, "stamp");
                    String rps = JSONParser.getString(obj_message, "rps");
                    float advancement = JSONParser.getFloat(obj_message, "advancement", 0.0f);
                    String amount = JSONParser.getString(obj_message, "amount");
                    float amountRatio = JSONParser.getFloat(obj_message, "amountRatio", 0.0f);
                    String method = JSONParser.getString(obj_message, "method");
                    float methodRatio = JSONParser.getFloat(obj_message, "methodRatio", 0.0f);
                    float time = JSONParser.getFloat(obj_message, "time", 0.0f);
                    String stime = JSONParser.getString(obj_message, "stime");
                    String etime = JSONParser.getString(obj_message, "etime");
                    String applyDow = JSONParser.getString(obj_message, "applyDow");
                    String store = JSONParser.getString(obj_message, "store");
                    long createdAt = JSONParser.getLong(obj_message, "createdAt", 0L);
                    long lastModified = JSONParser.getLong(obj_message, "lastModified", 0L);


                    mChkStorePoint.setChecked(pointUse.compareTo("Y") == 0);
                    mChkStoreStemp.setChecked(stampUse.compareTo("Y") == 0);
                    mChkStoreGame.setChecked(rpsUse.compareTo("Y") == 0);

                    mChkStoreTotal.setChecked(earn.compareTo("T") == 0);
                    mTvStoreTotalPoint.setText(earn.compareTo("T") == 0 ? (point + "%") : "-%");
                    mChkStoreGroup.setChecked(earn.compareTo("G") == 0);
                    mTvStoreGroupPoint.setText(earn.compareTo("G") == 0 ? (point + "%") : "-%");
                    mChkStoreSingle.setChecked(earn.compareTo("S") == 0);
                    mTvStoreSinglePoint.setText(earn.compareTo("S") == 0 ? (point + "%") : "-%");
                    if (earn.compareTo("S") == 0) {
                        mTvStoreSingleStempMoney.setText("기준금액\n" + stamp + "원당 1개");
                        mTvStoreSingleGameMoney.setText("기본승점 X\n기준금액 " + rps +"원당 1게임승점");
                    }
                    else {
                        mTvStoreSingleStempMoney.setText("기준금액\n-원당 1개");
                        mTvStoreSingleGameMoney.setText("기본승점 X\n기준금액 -원당 1게임승점");
                    }
                    mTvStoreGroupName.setText("");


                    if (advancement > 0.0f)
                        mTvStoreVipGrade.setText("2학년부터 -> 1학년씩 승급당 " + Float.toString(advancement) + "% 추가");
                    if (amount != null && amount.length() > 0 && amountRatio > 0.0f)
                        mTvStorePayMoney.setText( amount + "원 이상 " + Float.toString(amountRatio) + "% 추가");
                    if (method != null && method.length() > 0 && methodRatio > 0.0f)
                        mTvStorePayType.setText( method + " " + Float.toString(methodRatio) +"% 추가");
                    if (stime != null && stime.length() > 0 && etime != null && etime.length() > 0 && time > 0.0f)
                        mTvStorePayTime.setText( stime + " ~ " + etime + " " + Float.toString(time) + "% 추가");

                    mTvStoreDeviceTimeout.setText("스마트폰 터치 대기시간 " + delay + "초(30~120초 범위)");

                }
            }
        }

        if (o[0] instanceof ResFranchiseeStoreStempInfo) {
            ResFranchiseeStoreStempInfo res = (ResFranchiseeStoreStempInfo) o[0];
            JSONObject obj = res.getParseData();
            if (JSONParser.isSuccess(obj)) {
                JSONObject obj_message = JSONParser.getObject(obj, ResInfo.KEY_RES.message);
                if (obj_message != null) {

                    String sid = JSONParser.getString(obj_message, "sid");
                    String name = JSONParser.getString(obj_message, "name");
                    int price  = JSONParser.getInt(obj_message, "price", 0);
                    int stamp = JSONParser.getInt(obj_message, "stamp", 0);
                    int payment = JSONParser.getInt(obj_message, "payment", 0);
                    int oneoff = JSONParser.getInt(obj_message, "oneoff", 0);
                    String author = JSONParser.getString(obj_message, "author");
                    long createdAt = JSONParser.getLong(obj_message, "createdAt", 0L);

                    mEdStoreStempItem.setText(name);
                    mEdStoreStempItemPrice.setText(price + "원");
                    mEdStoreStempItemCount.setText(stamp + "개");
                    mEdStoreStempItemOnePrice.setText(payment + "원");
                    mEdStoreStempItemUseLimit.setText(oneoff + "회");
                }
            }
        }

        if (o[0] instanceof ResFranchiseeStoreInfoUpdate) {
            ResFranchiseeStoreInfoUpdate res = (ResFranchiseeStoreInfoUpdate) o[0];
            JSONObject obj = res.getParseData();
            if (JSONParser.isSuccess(obj)) {
                int obj_message = JSONParser.getInt(obj, ResFranchiseeStoreInfoUpdate.KEY_RES.message, 0);
                if (obj_message > 0) {

                    Bundle b = new Bundle();
                    //b.putString("dialog_msg", getString(R.string.msg_success_guide_answer));
                    b.putString("dialog_msg", "가맹정보 수정이 완료 되었습니다.");
                    MsgSingleDialog dialog = new MsgSingleDialog();
                    dialog.setArguments(b);
                    dialog.setOnResultListener(new BaseDialogFragment.OnResultListener() {
                        @Override
                        public void onDialogResult(Object... objects) {
                            if (objects != null && objects[0] instanceof Boolean) {
                                boolean isOk = (boolean) objects[0];
                                if (isOk) {
                                    initRequest();
                                }
                            }
                        }
                    });
                    dialog.show(getFragmentManager(), "dialog");

                }
            }
        }
//                //mEditSMSNumber.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
//                mBtnCheckCode.setEnabled(true);
//                mEditSMSLayout.setEnabled(true);
//                mRemainTime = 6*30;
//                mSMSTimer.scheduleAtFixedRate(new TimerTask() {
//                    @Override
//                    public void run() {
//                        getActivity().runOnUiThread(new Runnable()
//                        {
//                            @Override
//                            public void run()
//                            {
//                                mRemainTime--;
//                                if (mRemainTime < 0) {
//                                    mSMSTimer.cancel();
//                                    mSMSInjeonMsg.setText(String.format("유효시간 00:00:00\n3분 이내에 인증번호를 입력해 주세요\n입력시간이 초과하면 인증번호 재전송을 눌러주세요."));
//                                    mBtnSMSInJeon.setOnClickListener(mFragment);
//                                    mBsendMessageOK = false;
//                                }
//                                else {
//                                    mSMSInjeonMsg.setText(String.format("유효시간 %02d:%02d:%02d\n3분 이내에 인증번호를 입력해 주세요\n입력시간이 초과하면 인증번호 재전송을 눌러주세요.",
//                                            mRemainTime / 3600, (mRemainTime / 60) % 60, mRemainTime % 60));
//                                    mBsendMessageOK = true;
//                                }
//                            }
//                        });
//                    }
//                }, 1000, 1000);
//            } else {
//            }
//        }
//
//        if (o[0] instanceof ResSMSCheck) {
//            ResSMSCheck res = (ResSMSCheck) o[0];
//            JSONObject obj = res.getParseData();
//            if (JSONParser.isSuccess(obj)) {
//                mbInjeonOk = true;
//                //mBtnNext.setBackground(getResources().getDrawable(R.drawable.rect_gray_radius));
//                //mBtnNext.setVisibility(View.VISIBLE);
//
//                mSMSTimer.cancel();
//                mEditPhoneNumber.setEnabled(false);
//
//                Bundle b = new Bundle();
//                //b.putString("dialog_msg", getString(R.string.msg_success_guide_answer));
//                b.putString("dialog_msg", "인증이 완료 되었습니다.");
//                MsgSingleDialog dialog = new MsgSingleDialog();
//                dialog.setArguments(b);
//                dialog.setOnResultListener(new BaseDialogFragment.OnResultListener() {
//                    @Override
//                    public void onDialogResult(Object... objects) {
////                        if (objects != null && objects[0] instanceof Boolean) {
//////                            boolean isOk = (boolean) objects[0];
//////                            if (isOk) {
////////                                getActivity().setResult(getActivity().RESULT_OK);
////////                                getActivity().finish();
//////                            }
////                        }
//                    }
//                });
//                dialog.show(getFragmentManager(), "dialog");
//
//                mBtnNext.setEnabled(true);
//                mBtnNext.setOnClickListener(this);
//            } else {
//                mbInjeonOk = false;
//            }
//        }
    }

    @Override
    public void onHttpDebugEvent(String debug_msg) {

    }

    @Override
    public void onHttpNetFailEvent(int errorCode, String errorMsg) {
//        if (mBsendMessageOK == false)
//            mBtnSMSInJeon.setOnClickListener(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TNConstants.REQUEST_CODE.DEVICE_SETUP:
                if (resultCode == Activity.RESULT_OK) {
                    //onRefresh();
                    initRequest();
                }
                break;
        }
    }

    @Override
    public void onBack() {
        FranchiseeMainFragment fragment = (FranchiseeMainFragment) getFragmentManager().findFragmentByTag(FranchiseeMainFragment.FRAGMENT_TAG);
        if (fragment != null) {
            fragment.onGoHome();
        }
    }

//
//    private TextWatcher mUserInfoTextWatcher = new TextWatcher() {
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            mBtnSMSInJeon.setEnabled(mEditUserName.length() > 0 && mEditBirthDay.length() > 0 && mEditPhoneNumber.length() > 0);
//            //mEditSMSNumber.setEnabled(mEditUserName.length() > 0 && mEditBirthDay.length() > 0 && mEditPhoneNumber.length() > 0);
//        }
//
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count,
//                                      int after) {
//        }
//
//        @Override
//        public void afterTextChanged(Editable s) {
//        }
//
//    };
}
