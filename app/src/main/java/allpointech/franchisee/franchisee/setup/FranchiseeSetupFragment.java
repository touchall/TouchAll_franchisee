package allpointech.franchisee.franchisee.setup;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.tuna.ui.activity.BaseAppCompatActivity;
import com.tuna.ui.fragment.BaseFragment;

import org.json.JSONObject;

import allpointech.franchisee.R;
import allpointech.franchisee.network.http.TNHttpMultiPartTask;
import allpointech.franchisee.network.http.json.JSONParser;
import allpointech.franchisee.network.http.resource.BaseHttpResource;
import allpointech.franchisee.network.http.resource.data.ResSMSReq;

/**
 * Created by daze on 2016-11-15.
 */

public class FranchiseeSetupFragment extends BaseFragment implements BaseAppCompatActivity.onKeyBackPressedListener, TNHttpMultiPartTask.onHttpNetResultListener, View.OnClickListener {
    public static final String FRAGMENT_TAG = FranchiseeSetupFragment.class.getSimpleName();

    private Toolbar mToolbar;


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

                    FranchiseeSetupActivity activity = (FranchiseeSetupActivity)getActivity();

                    if (activity != null) {
                        //activity._enableNdefExchangeMode("user_point_use=" + mTelegram);
                        activity._enableTagWriteMode();
                    }
                }
                break;

                case SEND_NFC_OFF:
                {
                    FranchiseeSetupActivity activity = (FranchiseeSetupActivity)getActivity();

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
    }

    @Override
    protected int inflateLayout() {
        return R.layout.fragment_franchisee_setup;
    }

    @Override
    protected void initLayout(View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((BaseAppCompatActivity) getActivity()).setSupportActionBar(mToolbar);

    }

    private void requestSMS(String phoneNumber) {
        //통신
        // retune_code 에 따라 mTvFindMsg msg 변경
        ResSMSReq res = new ResSMSReq();
        res.setParameter(phoneNumber);

        TNHttpMultiPartTask task = new TNHttpMultiPartTask(getActivity(), getFragmentManager());
        task.setOnHttpResultListener(this);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, res);
    }

    @Override
    protected void initRequest() {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_franchisee_info1_next:
                //startActivityForResult(new Intent(getActivity(), FranchiseeInfo3Activity.class), 12123);
                break;
        }

    }

    @Override
    public void onHttpNetSuccessEvent(BaseHttpResource[] o) {
        if (o[0] instanceof ResSMSReq) {
            ResSMSReq res = (ResSMSReq) o[0];
            JSONObject obj = res.getParseData();
            if (JSONParser.isSuccess(obj)) {
                //mEditSMSNumber.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
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
            } else {
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
