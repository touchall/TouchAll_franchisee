package allpointech.franchisee.franchisee.device;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.LiveFolders;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tuna.ui.activity.BaseAppCompatActivity;
import com.tuna.ui.fragment.BaseFragment;
import com.tuna.utils.SLog;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import allpointech.franchisee.R;
import allpointech.franchisee.franchisee.setup.FranchiseeSetupActivity;
import allpointech.franchisee.network.http.TNHttpMultiPartTask;
import allpointech.franchisee.network.http.json.JSONParser;
import allpointech.franchisee.network.http.resource.BaseHttpResource;
import allpointech.franchisee.network.http.resource.data.ResFranchiseeStoreDeviceDetail;
import allpointech.franchisee.network.http.resource.data.ResFranchiseeStoreDevicePreUpdate;
import allpointech.franchisee.network.http.resource.data.ResReqTelegram;
import allpointech.franchisee.network.http.resource.data.ResSMSReq;
import allpointech.franchisee.utils.TNPreference;

/**
 * Created by daze on 2016-11-15.
 */

public class FranchiseeDeviceSetupFragment extends BaseFragment implements BaseAppCompatActivity.onKeyBackPressedListener, TNHttpMultiPartTask.onHttpNetResultListener, View.OnClickListener, FranchiseeDeviceSetupActivity.OnNfcCompleteListener {
    public static final String FRAGMENT_TAG = FranchiseeDeviceSetupFragment.class.getSimpleName();

    private Toolbar mToolbar;

    private TextView mTvStoreName;

    private LinearLayout mLayoutBonusPreSetup;
    private LinearLayout mLayoutBonusSetupOk;

    private TextView mTvPreDeviceSetupTimeout;
    private TimerTask mReaminTimer;// = new Timer();
    Timer t = new Timer();
    final Handler handler = new Handler();
    private int mRemainTime = 60;
    private boolean mBsendMessageOK = false;

    private CheckBox mChkStorePoint;
    private CheckBox mChkStoreStemp;
    private CheckBox mChkStoreGame;

    private CheckBox mChkStoreTotal;
    private EditText mEdStoreTotalPoint;
    private CheckBox mChkStoreGroup;
    private EditText mEdStoreGroupPoint;
    private CheckBox mChkStoreSingle;
    private EditText mEdStoreSinglePoint;
    private EditText mEdStoreSingleStempMoney;
    private EditText mEdStoreSingleGameMoney;
    private EditText mEdStoreGroupCode;
    private TextView mTvStoreGroupName;

    private EditText mEdStoreVipGradePoint;
    private EditText mEdStorePayMoney;
    private EditText mEdStorePayMoneyPoint;
    private CheckBox mChkStorePayTypeCash;
    private CheckBox mChkStorePayTypePoint;
    private EditText mEdStorePayTypePoint;
    private EditText mEdStorePayTimeStartHour;
    private EditText mEdStorePayTimeStartMin;
    private EditText mEdStorePayTimeEndHour;
    private EditText mEdStorePayTimeEndMin;
    private EditText mEdStorePayTimePoint;
    private EditText mEdStoreDeviceTimeout;

    private Button mBtnDeviceSetup;

    private String mDeviceSerial;
    private String mDeviceSetupTelegram;
    private String mJunmun;


    private boolean bEditOn = true;


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
//                    mEtUseMyPoint.setEnabled(false);

                    FranchiseeDeviceSetupActivity activity = (FranchiseeDeviceSetupActivity)getActivity();

                    if (activity != null) {
                        activity._enableNdefExchangeMode("device_setting=" + mDeviceSetupTelegram);
                        activity._enableTagWriteMode();
                    }
                }
                break;

                case SEND_NFC_OFF:
                {
                    FranchiseeDeviceSetupActivity activity = (FranchiseeDeviceSetupActivity)getActivity();

                    if (activity != null) {
                        activity._disableNdef();
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
        //mCurrentState = State.REG;
        if (getArguments() != null) {
            mDeviceSerial = getArguments().getString("device_serial");
            mJunmun = getArguments().getString("device_setup");
        }
    }

    @Override
    protected int inflateLayout() {
        return R.layout.fragment_franchisee_device_setup;
    }

    @Override
    protected void initLayout(View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((BaseAppCompatActivity) getActivity()).setSupportActionBar(mToolbar);

        FranchiseeDeviceSetupActivity deviceSetupActivity = (FranchiseeDeviceSetupActivity) getActivity();
        deviceSetupActivity.setOnNfcCompleteListener(this);


        mLayoutBonusPreSetup = (LinearLayout)view.findViewById(R.id.layout_bonus_pre_setup);
        mLayoutBonusPreSetup.setVisibility(View.GONE);
        mLayoutBonusSetupOk = (LinearLayout)view.findViewById(R.id.layout_bonus_setup_complete);
        mLayoutBonusSetupOk.setVisibility(View.GONE);

        mTvPreDeviceSetupTimeout = (TextView)view.findViewById(R.id.text_franchisee_deice_presetup_timeout);

        mTvStoreName = (TextView)view.findViewById(R.id.tv_franchisee_device_setup_store_name);

        mChkStorePoint = (CheckBox)view.findViewById(R.id.check_franchisee_store_point);
        mChkStorePoint.setOnClickListener(this);
        mChkStoreStemp = (CheckBox)view.findViewById(R.id.check_franchisee_store_stemp);
        mChkStoreStemp.setOnClickListener(this);
        mChkStoreGame = (CheckBox)view.findViewById(R.id.check_franchisee_store_game);
        mChkStoreGame.setOnClickListener(this);
        mChkStorePoint.setEnabled(bEditOn);
        mChkStoreStemp.setEnabled(bEditOn);
        mChkStoreGame.setEnabled(bEditOn);

        mChkStoreTotal = (CheckBox)view.findViewById(R.id.check_franchisee_store_total);
        mChkStoreTotal.setOnClickListener(this);
        mEdStoreTotalPoint = (EditText) view.findViewById(R.id.edit_franchisee_store_total_point);
        mChkStoreGroup = (CheckBox)view.findViewById(R.id.check_franchisee_store_group);
        mChkStoreGroup.setOnClickListener(this);
        mEdStoreGroupPoint = (EditText)view.findViewById(R.id.edit_franchisee_store_group_point);
        mChkStoreSingle = (CheckBox)view.findViewById(R.id.check_franchisee_store_single);
        mChkStoreSingle.setOnClickListener(this);
        mEdStoreSinglePoint = (EditText)view.findViewById(R.id.edit_franchisee_store_single_point);
        mEdStoreSingleStempMoney = (EditText)view.findViewById(R.id.edit_franchisee_store_single_stemp_money);
        mEdStoreSingleGameMoney = (EditText)view.findViewById(R.id.edit_franchisee_store_single_game_money);
        mEdStoreGroupCode = (EditText) view.findViewById(R.id.edit_franchisee_device_setup_group_code);
        mEdStoreGroupCode.setEnabled(false);
        mTvStoreGroupName = (TextView) view.findViewById(R.id.tv_franchisee_device_setup_group_name);
        mTvStoreGroupName.setText("");
        mChkStoreTotal.setEnabled(bEditOn);
        mChkStoreGroup.setEnabled(bEditOn);
        mChkStoreSingle.setEnabled(bEditOn);

        mEdStoreVipGradePoint = (EditText) view.findViewById(R.id.edit_franchisee_store_vip_grade);
        mEdStorePayMoney = (EditText)view.findViewById(R.id.edit_franchisee_store_paymoney);
        mEdStorePayMoneyPoint = (EditText)view.findViewById(R.id.edit_franchisee_store_paymoney_point);
        mChkStorePayTypeCash = (CheckBox)view.findViewById(R.id.check_franchisee_store_paytype_cash);
        mChkStorePayTypeCash.setOnClickListener(this);
        mChkStorePayTypePoint = (CheckBox)view.findViewById(R.id.check_franchisee_store_paytype_point);
        mChkStorePayTypePoint.setOnClickListener(this);
        mEdStorePayTypePoint = (EditText)view.findViewById(R.id.edit_franchisee_store_paytype_point);
        mEdStorePayTimeStartHour = (EditText)view.findViewById(R.id.edit_franchisee_store_paytime_start_hour);
        mEdStorePayTimeStartMin = (EditText)view.findViewById(R.id.edit_franchisee_store_paytime_start_min);
        mEdStorePayTimeEndHour = (EditText)view.findViewById(R.id.edit_franchisee_store_paytime_end_hour);
        mEdStorePayTimeEndMin = (EditText)view.findViewById(R.id.edit_franchisee_store_paytime_end_min);
        mEdStorePayTimePoint = (EditText)view.findViewById(R.id.edit_franchisee_store_paytime_point);
        mEdStoreDeviceTimeout = (EditText)view.findViewById(R.id.edit_franchisee_store_device_timeout);


        mBtnDeviceSetup = (Button)view.findViewById(R.id.button_franchisee_store_device_setup_request);
        mBtnDeviceSetup.setOnClickListener(this);

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

    private void requestStoreDevicePreUpdate() {

        boolean bUsePoint = mChkStorePoint.isChecked();
        boolean bUseStemp = mChkStoreStemp.isChecked();
        boolean bUseGame = mChkStoreGame.isChecked();

        boolean bGroupTotal = mChkStoreTotal.isChecked();
        boolean bGroupGroup = mChkStoreGroup.isChecked();
        boolean bGroupSingle = mChkStoreSingle.isChecked();

        String szTotalPoint = mEdStoreTotalPoint.getText().toString();
        String szGroupPoint = mEdStoreGroupPoint.getText().toString();
        String szSinglePoint = mEdStoreSinglePoint.getText().toString();

        String szStempMoney = mEdStoreSingleStempMoney.getText().toString();
        String szGameMoney = mEdStoreSingleGameMoney.getText().toString();


        String szEarn = "T";
        String szPoint = "";
        if (bGroupTotal) {
            szEarn = "T";
            szPoint = Integer.toString((int)(Float.parseFloat(szTotalPoint) * 10.0f));

//            szStempMoney = "0";
//            szGameMoney = "0";
        }
        else if (bGroupGroup) {
            szEarn = "G";
            szPoint = Integer.toString((int)(Float.parseFloat(szGroupPoint) * 10.0f));

//            szStempMoney = "0";
//            szGameMoney = "0";
        }
        else if (bGroupSingle) {
            szEarn = "S";
            szPoint = Integer.toString((int)(Float.parseFloat(szSinglePoint) * 10.0f));
        }

        String szKinds = "";
        if (bUsePoint)
            szKinds += "YN";
        else if (bUseStemp)
            szKinds += "NY";
        if (bUseGame)
            szKinds += "Y";
        else
            szKinds += "N";
        szKinds += "YYYY";


        String szVipRate = String.format("%02d", Integer.parseInt(mEdStoreVipGradePoint.getText().toString()));
        String szPaymoney = mEdStorePayMoney.getText().toString();
        String szPaymoneyPoint = String.format("%02d", Integer.parseInt(mEdStorePayMoneyPoint.getText().toString()));
        String szPayType = "000";
        if (mChkStorePayTypeCash.isChecked()) {
            szPayType = "0";
            szPayType += String.format("%02d", Integer.parseInt(mEdStorePayTypePoint.getText().toString()));
        }
        else if (mChkStorePayTypePoint.isChecked()) {
            szPayType = "1";
            szPayType += String.format("%02d", Integer.parseInt(mEdStorePayTypePoint.getText().toString()));
        }

        String szPayTimeStartHour = mEdStorePayTimeStartHour.getText().toString();
        String szPayTimeStartMin = mEdStorePayTimeStartMin.getText().toString();
        String szPayTimeEndHour = mEdStorePayTimeEndHour.getText().toString();
        String szPayTimeEndMin = mEdStorePayTimeEndMin.getText().toString();
        String szPayTimeRate = String.format("%02d", Integer.parseInt(mEdStorePayTimePoint.getText().toString()));

        String szTimeoutDelay = mEdStoreDeviceTimeout.getText().toString();

        ResFranchiseeStoreDevicePreUpdate res = new ResFranchiseeStoreDevicePreUpdate();
        res.setToken(TNPreference.getMemToken(getActivity()));

        res.setParameter(mDeviceSerial,
                szEarn, //"S",
                szKinds, //"NYYYYYY",
                szPoint, //"32",
                szStempMoney, //"3000",
                szGameMoney, //"5000",
                "1",  // data send type
                szTimeoutDelay, //"30",
                szVipRate, //"5",
                szPaymoney + szPaymoneyPoint, //"5000010",
                szPayType, //"010",
                szPayTimeStartHour + szPayTimeStartMin + szPayTimeEndHour + szPayTimeEndMin + szPayTimeRate);//"1800220110");

        TNHttpMultiPartTask task = new TNHttpMultiPartTask(getActivity(), getFragmentManager());
        task.setOnHttpResultListener(this);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, res);
    }


    private void requestDeviceSetup() {

        ResReqTelegram res = new ResReqTelegram();
        res.setToken(TNPreference.getMemToken(getActivity()));
        res.setParameterMobile(TNPreference.getMemphoneNumber(getActivity()));
        res.setParameterTelegram(mJunmun);
        res.setParameterRps("N");

        TNHttpMultiPartTask task = new TNHttpMultiPartTask(getActivity(), getFragmentManager());
        task.setOnHttpResultListener(this);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, res);
    }

    @Override
    protected void initRequest() {

        if (mJunmun != null && mJunmun.length() > 0)
            requestDeviceSetup();
        else
            requestStoreDeviceDetial(mDeviceSerial);
    }

    private boolean onCheck() {

        boolean bUsePoint = mChkStorePoint.isChecked();
        boolean bUseStemp = mChkStoreStemp.isChecked();
        boolean bUseGame = mChkStoreGame.isChecked();

        boolean bGroupTotal = mChkStoreTotal.isChecked();
        boolean bGroupGroup = mChkStoreGroup.isChecked();
        boolean bGroupSingle = mChkStoreSingle.isChecked();

        String szSingleStempMoney = mEdStoreSingleStempMoney.getText().toString();
        String szSingleGameMoney = mEdStoreSingleGameMoney.getText().toString();

        String szTotalPoint = mEdStoreTotalPoint.getText().toString();
        String szGroupPoint = mEdStoreGroupPoint.getText().toString();
        String szSinglePoint = mEdStoreSinglePoint.getText().toString();

        int singleStempMoney = 0;
        int singleGameMoney = 0;
        if (szSingleStempMoney != null && szSingleStempMoney.length() > 0)
            singleStempMoney = Integer.parseInt(mEdStoreSingleStempMoney.getText().toString());
        if (szSingleGameMoney != null && szSingleGameMoney.length() > 0)
            singleGameMoney = Integer.parseInt(mEdStoreSingleGameMoney.getText().toString());

        float totalPoint = 0.0f;
        float groupPoint = 0.0f;
        float singlePoint = 0.0f;
        if (szTotalPoint != null && szTotalPoint.length() > 0)
            totalPoint = Float.parseFloat(szTotalPoint);
        if (szGroupPoint != null && szGroupPoint.length() > 0)
            groupPoint = Float.parseFloat(szGroupPoint);
        if (szSinglePoint != null && szSinglePoint.length() > 0)
            singlePoint = Float.parseFloat(szSinglePoint);

        if (bUsePoint) {
            if (bGroupTotal && totalPoint <= 0.0f) {
                showToast("통합 포인트율을 입력하세요.");
                return false;
            }

            if (bGroupGroup && groupPoint <= 0.0f) {
                showToast("그룹 포인트율을 입력하세요.");
                return false;
            }

            if (bGroupSingle && singlePoint <= 0.0f) {
                showToast("단독 포인트율을 입력하세요.");
                return false;
            }
        }

        if (bUseStemp) {
            if (bGroupSingle && singleStempMoney <= 0) {
                showToast("단독 스탬프 기준금액을 입력하세요.");
                return false;
            }
        }

        if (bUseGame) {
            if (bGroupSingle && singleGameMoney <= 0) {
                showToast("단독 가위바위보 기준금액을 입력하세요.");
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.check_franchisee_store_point:
                mChkStorePoint.setChecked(true);
                mChkStoreStemp.setChecked(false);
                //mChkStoreGame.setChecked(false);
                break;
            case R.id.check_franchisee_store_stemp:
                mChkStorePoint.setChecked(false);
                mChkStoreStemp.setChecked(true);
                //mChkStoreGame.setChecked(false);
                break;
            case R.id.check_franchisee_store_game:
                //mChkStorePoint.setChecked(false);
                //mChkStoreStemp.setChecked(false);
                //mChkStoreGame.setChecked(true);
                break;
            case R.id.check_franchisee_store_total:
                mChkStoreTotal.setChecked(true);
                mChkStoreGroup.setChecked(false);
                mChkStoreSingle.setChecked(false);
                break;
            case R.id.check_franchisee_store_group:
                mChkStoreTotal.setChecked(false);
                mChkStoreGroup.setChecked(true);
                mChkStoreSingle.setChecked(false);
                break;
            case R.id.check_franchisee_store_single:
                mChkStoreTotal.setChecked(false);
                mChkStoreGroup.setChecked(false);
                mChkStoreSingle.setChecked(true);
                break;
            case R.id.check_franchisee_store_paytype_cash:
                mChkStorePayTypeCash.setChecked(true);
                mChkStorePayTypePoint.setChecked(false);
                break;
            case R.id.check_franchisee_store_paytype_point:
                mChkStorePayTypeCash.setChecked(false);
                mChkStorePayTypePoint.setChecked(true);
                break;
            case R.id.button_franchisee_store_device_setup_request:
            {
                if (onCheck()) {
                    bEditOn = false;

                    mChkStorePoint.setEnabled(bEditOn);
                    mChkStoreStemp.setEnabled(bEditOn);
                    mChkStoreGame.setEnabled(bEditOn);

                    mChkStoreTotal.setEnabled(bEditOn);
                    mChkStoreGroup.setEnabled(bEditOn);
                    mChkStoreSingle.setEnabled(bEditOn);

                    requestStoreDevicePreUpdate();
                }
            }
                break;
        }

    }

    @Override
    public void onHttpNetSuccessEvent(BaseHttpResource[] o) {
        if (o[0] instanceof ResFranchiseeStoreDeviceDetail) {
            ResFranchiseeStoreDeviceDetail res = (ResFranchiseeStoreDeviceDetail) o[0];
            JSONObject obj = res.getParseData();
            if (JSONParser.isSuccess(obj)) {
                JSONObject obj_message = JSONParser.getObject(obj, ResFranchiseeStoreDeviceDetail.KEY_RES.message);
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

                    mTvStoreName.setText(store);

                    mChkStorePoint.setChecked(pointUse.compareTo("Y") == 0);
                    mChkStoreStemp.setChecked(stampUse.compareTo("Y") == 0);
                    mChkStoreGame.setChecked(rpsUse.compareTo("Y") == 0);

                    mChkStoreTotal.setChecked(earn.compareTo("T") == 0);
                    mEdStoreTotalPoint.setText(earn.compareTo("T") == 0 ? (point) : "");
                    mChkStoreGroup.setChecked(earn.compareTo("G") == 0);
                    mEdStoreGroupPoint.setText(earn.compareTo("G") == 0 ? (point) : "");
                    mChkStoreSingle.setChecked(earn.compareTo("S") == 0);
                    mEdStoreSinglePoint.setText(earn.compareTo("S") == 0 ? (point) : "");
                    if (earn.compareTo("S") == 0) {
                        mEdStoreSingleStempMoney.setText(stamp);
                        mEdStoreSingleGameMoney.setText(rps);
                    } else {
                        mEdStoreSingleStempMoney.setText("");
                        mEdStoreSingleGameMoney.setText("");
                    }
                    //mTvStoreGroupCode.setText("");


//                    if (advancement > 0.0f)
//                        mTvStoreVipGrade.setText("2학년부터 -> 1학년씩 승급당 " + Float.toString(advancement) + "% 추가");
//                    if (amount != null && amount.length() > 0 && amountRatio > 0.0f)
//                        mTvStorePayMoney.setText( amount + "원 이상 " + Float.toString(amountRatio) + "% 추가");
//                    if (method != null && method.length() > 0 && methodRatio > 0.0f)
//                        mTvStorePayType.setText( method + " " + Float.toString(methodRatio) +"% 추가");
//                    if (stime != null && stime.length() > 0 && etime != null && etime.length() > 0 && time > 0.0f)
//                        mTvStorePayTime.setText( stime + " ~ " + etime + " " + Float.toString(time) + "% 추가");
//
//                    mTvStoreDeviceTimeout.setText("스마트폰 터치 대기시간 " + delay + "초(30~120초 범위)");


                    if (advancement > 0.0f)
                        mEdStoreVipGradePoint.setText(Integer.toString((int)advancement));
                    else
                        mEdStoreVipGradePoint.setText("0");
                    if (amount != null && amount.length() > 0 && amountRatio > 0.0f){
                        mEdStorePayMoney.setText(amount);
                        mEdStorePayMoneyPoint.setText(Integer.toString((int)amountRatio));
                    }
                    else {
                        mEdStorePayMoney.setText("");
                        mEdStorePayMoneyPoint.setText("0");
                    }

                    mChkStorePayTypeCash.setChecked(true);
                    mChkStorePayTypePoint.setChecked(false);
                    if (method != null && method.length() > 0) {
                        mChkStorePayTypeCash.setChecked(method.compareTo("0") == 0);
                        mChkStorePayTypePoint.setChecked(method.compareTo("0") != 0);
                    }
                    if (methodRatio > 0.0f) {
                        mEdStorePayTypePoint.setText(Integer.toString((int)methodRatio));
                    }
                    else {
                        mEdStorePayTypePoint.setText("0");
                    }


                    if (stime != null && stime.length() > 0 && etime != null && etime.length() > 0 && time > 0.0f) {
                        mEdStorePayTimeStartHour.setText(stime.substring(0, 2));
                        mEdStorePayTimeStartMin.setText(stime.substring(2, 4));
                        mEdStorePayTimeEndHour.setText(etime.substring(0, 2));
                        mEdStorePayTimeEndMin.setText(etime.substring(2, 4));
                        mEdStorePayTimePoint.setText(Integer.toString((int)time));
                    }
                    else {
                        mEdStorePayTimeStartHour.setText("");
                        mEdStorePayTimeStartMin.setText("");
                        mEdStorePayTimeEndHour.setText("");
                        mEdStorePayTimeEndMin.setText("");
                        mEdStorePayTimePoint.setText("0");
                    }

                    mEdStoreDeviceTimeout.setText(delay);
                }
            }
        }

        if (o[0] instanceof ResFranchiseeStoreDevicePreUpdate) {
            ResFranchiseeStoreDevicePreUpdate res = (ResFranchiseeStoreDevicePreUpdate) o[0];
            JSONObject obj = res.getParseData();
            if (JSONParser.isSuccess(obj)) {
                JSONObject obj_message = JSONParser.getObject(obj, ResFranchiseeStoreDeviceDetail.KEY_RES.message);
                if (obj_message != null) {

                    JSONParser.getString(obj_message, ResFranchiseeStoreDevicePreUpdate.KEY_RES.serial);
                    JSONParser.getString(obj_message, ResFranchiseeStoreDevicePreUpdate.KEY_RES.sid);
                    JSONParser.getString(obj_message, ResFranchiseeStoreDevicePreUpdate.KEY_RES.store);
                    JSONParser.getString(obj_message, ResFranchiseeStoreDevicePreUpdate.KEY_RES.earn);
                    JSONParser.getString(obj_message, ResFranchiseeStoreDevicePreUpdate.KEY_RES.pointUse);
                    JSONParser.getString(obj_message, ResFranchiseeStoreDevicePreUpdate.KEY_RES.stampUse);
                    JSONParser.getString(obj_message, ResFranchiseeStoreDevicePreUpdate.KEY_RES.rpsUse);
                    JSONParser.getString(obj_message, ResFranchiseeStoreDevicePreUpdate.KEY_RES.remnant);
                    JSONParser.getString(obj_message, ResFranchiseeStoreDevicePreUpdate.KEY_RES.receipt);
                    JSONParser.getString(obj_message, ResFranchiseeStoreDevicePreUpdate.KEY_RES.leaflet);
                    JSONParser.getString(obj_message, ResFranchiseeStoreDevicePreUpdate.KEY_RES.vat);
                    JSONParser.getString(obj_message, ResFranchiseeStoreDevicePreUpdate.KEY_RES.kinds);
                    JSONParser.getString(obj_message, ResFranchiseeStoreDevicePreUpdate.KEY_RES.point);
                    JSONParser.getString(obj_message, ResFranchiseeStoreDevicePreUpdate.KEY_RES.stamp);
                    JSONParser.getString(obj_message, ResFranchiseeStoreDevicePreUpdate.KEY_RES.rps);
                    JSONParser.getString(obj_message, ResFranchiseeStoreDevicePreUpdate.KEY_RES.transfer);
                    JSONParser.getString(obj_message, ResFranchiseeStoreDevicePreUpdate.KEY_RES.delay);
                    JSONParser.getString(obj_message, ResFranchiseeStoreDevicePreUpdate.KEY_RES.advancement);
                    JSONParser.getString(obj_message, ResFranchiseeStoreDevicePreUpdate.KEY_RES.amount);
                    JSONParser.getString(obj_message, ResFranchiseeStoreDevicePreUpdate.KEY_RES.amountRatio);
                    JSONParser.getString(obj_message, ResFranchiseeStoreDevicePreUpdate.KEY_RES.method);
                    JSONParser.getString(obj_message, ResFranchiseeStoreDevicePreUpdate.KEY_RES.methodRatio);
                    JSONParser.getString(obj_message, ResFranchiseeStoreDevicePreUpdate.KEY_RES.time);
                    JSONParser.getString(obj_message, ResFranchiseeStoreDevicePreUpdate.KEY_RES.stime);
                    JSONParser.getString(obj_message, ResFranchiseeStoreDevicePreUpdate.KEY_RES.etime);
                    JSONParser.getString(obj_message, ResFranchiseeStoreDevicePreUpdate.KEY_RES.createdAt);
                    JSONParser.getString(obj_message, ResFranchiseeStoreDevicePreUpdate.KEY_RES.lastModified);
                    mDeviceSetupTelegram = JSONParser.getString(obj_message, ResFranchiseeStoreDevicePreUpdate.KEY_RES.telegram);

                    if (mDeviceSetupTelegram != null && mDeviceSetupTelegram.length() > 0) {
                        SLog.LogD("device setup telegram -> " + mDeviceSetupTelegram);
                        Message msg = mMainHandler.obtainMessage();
                        msg.what = SEND_NFC_ON;
                        mMainHandler.handleMessage(msg);
                    }

                    mLayoutBonusPreSetup.setVisibility(View.VISIBLE);

                    //mReaminTimer.cancel();
                    mRemainTime = 60;
                    mTvPreDeviceSetupTimeout.setText("대기시간 60초");

                    if (mReaminTimer != null)
                        mReaminTimer.cancel();

                    mReaminTimer = new TimerTask() {
                        public void run() {
                            handler.post(new Runnable() {
                                public void run() {
                                    mRemainTime--;
                                    if (mRemainTime < 0) {
                                        mReaminTimer.cancel();
                                        mTvPreDeviceSetupTimeout.setText("대기시간 60초");
                                        //mBsendMessageOK = false;
                                        mLayoutBonusPreSetup.setVisibility(View.GONE);

                                        Message msg = mMainHandler.obtainMessage();
                                        msg.what = SEND_NFC_OFF;
                                        mMainHandler.handleMessage(msg);
                                    }
                                    else {
                                        mTvPreDeviceSetupTimeout.setText(String.format("대기시간 %02d초", mRemainTime % 60));
                                        //mBsendMessageOK = true;
                                    }
                                }
                            });
                        }};

                    // public void schedule (TimerTask task, long delay, long period)
                    t.schedule(mReaminTimer, 1000, 1000);  //

//                    mReaminTimer.scheduleAtFixedRate(new TimerTask() {
//                        @Override
//                        public void run() {
//                            getActivity().runOnUiThread(new Runnable()
//                            {
//                                @Override
//                                public void run()
//                                {
//                                    mRemainTime--;
//                                    if (mRemainTime < 0) {
//                                        mReaminTimer.cancel();
//                                        mTvPreDeviceSetupTimeout.setText("대기시간 60초");
//                                        //mBsendMessageOK = false;
//                                        mLayoutBonusPreSetup.setVisibility(View.GONE);
//                                    }
//                                    else {
//                                        mTvPreDeviceSetupTimeout.setText(String.format("대기시간 %02d초", mRemainTime % 60));
//                                        //mBsendMessageOK = true;
//                                    }
//                                }
//                            });
//                        }
//                    }, 1000, 1000);
                }
            }
        }



        if (o[0] instanceof ResReqTelegram) {
            ResReqTelegram res = (ResReqTelegram) o[0];
            JSONObject obj = res.getParseData();
            if (JSONParser.isSuccess(obj)) {

                JSONObject obj_message = JSONParser.getObject(obj, ResReqTelegram.KEY_RES.message);
                if (obj_message != null) {

                    String serial = JSONParser.getString(obj_message, ResFranchiseeStoreDevicePreUpdate.KEY_RES.serial);
                    String sid = JSONParser.getString(obj_message, ResFranchiseeStoreDevicePreUpdate.KEY_RES.sid);
                    String store = JSONParser.getString(obj_message, ResFranchiseeStoreDevicePreUpdate.KEY_RES.store);
                    String earn = JSONParser.getString(obj_message, ResFranchiseeStoreDevicePreUpdate.KEY_RES.earn);
                    String pointUse = JSONParser.getString(obj_message, ResFranchiseeStoreDevicePreUpdate.KEY_RES.pointUse);
                    String stampUse = JSONParser.getString(obj_message, ResFranchiseeStoreDevicePreUpdate.KEY_RES.stampUse);
                    String rpsUse = JSONParser.getString(obj_message, ResFranchiseeStoreDevicePreUpdate.KEY_RES.rpsUse);
                    String remnant = JSONParser.getString(obj_message, ResFranchiseeStoreDevicePreUpdate.KEY_RES.remnant);
                    String receipt = JSONParser.getString(obj_message, ResFranchiseeStoreDevicePreUpdate.KEY_RES.receipt);
                    String leaflet = JSONParser.getString(obj_message, ResFranchiseeStoreDevicePreUpdate.KEY_RES.leaflet);
                    String vat = JSONParser.getString(obj_message, ResFranchiseeStoreDevicePreUpdate.KEY_RES.vat);
                    String kinds = JSONParser.getString(obj_message, ResFranchiseeStoreDevicePreUpdate.KEY_RES.kinds);
                    String point = JSONParser.getString(obj_message, ResFranchiseeStoreDevicePreUpdate.KEY_RES.point);
                    String stamp = JSONParser.getString(obj_message, ResFranchiseeStoreDevicePreUpdate.KEY_RES.stamp);
                    String rps = JSONParser.getString(obj_message, ResFranchiseeStoreDevicePreUpdate.KEY_RES.rps);
                    String transfer = JSONParser.getString(obj_message, ResFranchiseeStoreDevicePreUpdate.KEY_RES.transfer);
                    String delay = JSONParser.getString(obj_message, ResFranchiseeStoreDevicePreUpdate.KEY_RES.delay);
                    float advancement = JSONParser.getFloat(obj_message, ResFranchiseeStoreDevicePreUpdate.KEY_RES.advancement, 0.0f);
                    String amount = JSONParser.getString(obj_message, ResFranchiseeStoreDevicePreUpdate.KEY_RES.amount);
                    float amountRatio = JSONParser.getFloat(obj_message, ResFranchiseeStoreDevicePreUpdate.KEY_RES.amountRatio, 0.0f);
                    String method = JSONParser.getString(obj_message, ResFranchiseeStoreDevicePreUpdate.KEY_RES.method);
                    float methodRatio = JSONParser.getFloat(obj_message, ResFranchiseeStoreDevicePreUpdate.KEY_RES.methodRatio, 0.0f);
                    float time = JSONParser.getFloat(obj_message, ResFranchiseeStoreDevicePreUpdate.KEY_RES.time, 0.0f);
                    String stime = JSONParser.getString(obj_message, ResFranchiseeStoreDevicePreUpdate.KEY_RES.stime);
                    String etime = JSONParser.getString(obj_message, ResFranchiseeStoreDevicePreUpdate.KEY_RES.etime);
                    long createdAt = JSONParser.getLong(obj_message, ResFranchiseeStoreDevicePreUpdate.KEY_RES.createdAt, 0L);
                    long lastModified = JSONParser.getLong(obj_message, ResFranchiseeStoreDevicePreUpdate.KEY_RES.lastModified, 0L);
                    mDeviceSetupTelegram = JSONParser.getString(obj_message, ResFranchiseeStoreDevicePreUpdate.KEY_RES.telegram);

                    mTvStoreName.setText(store);

                    mChkStorePoint.setChecked(pointUse.compareTo("Y") == 0);
                    mChkStoreStemp.setChecked(stampUse.compareTo("Y") == 0);
                    mChkStoreGame.setChecked(rpsUse.compareTo("Y") == 0);

                    mChkStoreTotal.setChecked(earn.compareTo("T") == 0);
                    mEdStoreTotalPoint.setText(earn.compareTo("T") == 0 ? point : "");
                    mChkStoreGroup.setChecked(earn.compareTo("G") == 0);
                    mEdStoreGroupPoint.setText(earn.compareTo("G") == 0 ? point : "");
                    mChkStoreSingle.setChecked(earn.compareTo("S") == 0);
                    mEdStoreSinglePoint.setText(earn.compareTo("S") == 0 ? point : "");
                    if (earn.compareTo("S") == 0) {
                        mEdStoreSingleStempMoney.setText(stamp);
                        mEdStoreSingleGameMoney.setText(rps);
                    }
                    else {
                        mEdStoreSingleStempMoney.setText("");
                        mEdStoreSingleGameMoney.setText("");
                    }
                    //mTvStoreGroupCode.setText("");


//                    if (advancement > 0.0f)
//                        mTvStoreVipGrade.setText("2학년부터 -> 1학년씩 승급당 " + Float.toString(advancement) + "% 추가");
//                    if (amount != null && amount.length() > 0 && amountRatio > 0.0f)
//                        mTvStorePayMoney.setText( amount + "원 이상 " + Float.toString(amountRatio) + "% 추가");
//                    if (method != null && method.length() > 0 && methodRatio > 0.0f)
//                        mTvStorePayType.setText( method + " " + Float.toString(methodRatio) +"% 추가");
//                    if (stime != null && stime.length() > 0 && etime != null && etime.length() > 0 && time > 0.0f)
//                        mTvStorePayTime.setText( stime + " ~ " + etime + " " + Float.toString(time) + "% 추가");
//
//                    mTvStoreDeviceTimeout.setText("스마트폰 터치 대기시간 " + delay + "초(30~120초 범위)");

                    if (advancement > 0.0f)
                        mEdStoreVipGradePoint.setText(Integer.toString((int)advancement));
                    else
                        mEdStoreVipGradePoint.setText("0");
                    if (amount != null && amount.length() > 0 && amountRatio > 0.0f){
                        mEdStorePayMoney.setText(amount);
                        mEdStorePayMoneyPoint.setText(Integer.toString((int)amountRatio));
                    }
                    else {
                        mEdStorePayMoney.setText("");
                        mEdStorePayMoneyPoint.setText("0");
                    }
                    mChkStorePayTypeCash.setChecked(true);
                    mChkStorePayTypePoint.setChecked(false);
                    if (method != null && method.length() > 0) {
                        mChkStorePayTypeCash.setChecked(method.compareTo("0") == 0);
                        mChkStorePayTypePoint.setChecked(method.compareTo("0") != 0);
                    }
                    if (methodRatio > 0.0f) {
                        mEdStorePayTypePoint.setText(Integer.toString((int)methodRatio));
                    }
                    else {
                        mEdStorePayTypePoint.setText("0");
                    }
                    if (stime != null && stime.length() > 0 && etime != null && etime.length() > 0 && time > 0.0f) {
                        mEdStorePayTimeStartHour.setText(stime.substring(0, 2));
                        mEdStorePayTimeStartMin.setText(stime.substring(2, 4));
                        mEdStorePayTimeEndHour.setText(etime.substring(0, 2));
                        mEdStorePayTimeEndMin.setText(etime.substring(2, 4));
                        mEdStorePayTimePoint.setText(Integer.toString((int)time));
                    }
                    else {
                        mEdStorePayTimeStartHour.setText("");
                        mEdStorePayTimeStartMin.setText("");
                        mEdStorePayTimeEndHour.setText("");
                        mEdStorePayTimeEndMin.setText("");
                        mEdStorePayTimePoint.setText("0");
                    }

                    mEdStoreDeviceTimeout.setText(delay);

                    if (mDeviceSetupTelegram != null && mDeviceSetupTelegram.length() > 0) {
                        SLog.LogD("device setup telegram -> " + mDeviceSetupTelegram);
                        Message msg = mMainHandler.obtainMessage();
                        msg.what = SEND_NFC_ON;
                        mMainHandler.handleMessage(msg);
                    }

                    mLayoutBonusPreSetup.setVisibility(View.VISIBLE);

                    //mReaminTimer.cancel();
                    mRemainTime = 60;
                    mTvPreDeviceSetupTimeout.setText("대기시간 60초");

                    if (mReaminTimer != null)
                        mReaminTimer.cancel();

                    mReaminTimer = new TimerTask() {
                        public void run() {
                            handler.post(new Runnable() {
                                public void run() {
                                    mRemainTime--;
                                    if (mRemainTime < 0) {
                                        mReaminTimer.cancel();
                                        mTvPreDeviceSetupTimeout.setText("대기시간 60초");
                                        //mBsendMessageOK = false;
                                        mLayoutBonusPreSetup.setVisibility(View.GONE);

                                        Message msg = mMainHandler.obtainMessage();
                                        msg.what = SEND_NFC_OFF;
                                        mMainHandler.handleMessage(msg);
                                    }
                                    else {
                                        mTvPreDeviceSetupTimeout.setText(String.format("대기시간 %02d초", mRemainTime % 60));
                                        //mBsendMessageOK = true;
                                    }
                                }
                            });
                        }};

                    // public void schedule (TimerTask task, long delay, long period)
                    t.schedule(mReaminTimer, 1000, 1000);  //

                }

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
    public void onBack() {
        getActivity().finish();
    }

    @Override
    public void onComplete() {
        if (mReaminTimer != null)
            mReaminTimer.cancel();
        mLayoutBonusPreSetup.setVisibility(View.GONE);
        mLayoutBonusSetupOk.setVisibility(View.VISIBLE);
    }

    public void setDeviceSetup(String junmun) {
        mJunmun = junmun;
        requestDeviceSetup();
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
