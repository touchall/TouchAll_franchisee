package allpointech.franchisee.franchisee.info;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.tuna.ui.activity.BaseAppCompatActivity;
import com.tuna.ui.fragment.BaseFragment;

import allpointech.franchisee.R;
import allpointech.franchisee.network.http.TNHttpMultiPartTask;
import allpointech.franchisee.network.http.resource.BaseHttpResource;
import allpointech.franchisee.network.http.resource.data.ResSMSCheck;
import allpointech.franchisee.network.http.resource.data.ResSMSReq;

/**
 * Created by daze on 2016-11-15.
 */

public class FranchiseeInfo3Fragment extends BaseFragment implements BaseAppCompatActivity.onKeyBackPressedListener, TNHttpMultiPartTask.onHttpNetResultListener, View.OnClickListener {
    public static final String FRAGMENT_TAG = FranchiseeInfo3Fragment.class.getSimpleName();

    private Toolbar mToolbar;


    private Button mBtnFinish;


    @Override
    protected void BundleData(Bundle bundle) {
        setHasOptionsMenu(true);
        //mCurrentState = State.REG;
    }

    @Override
    protected int inflateLayout() {
        return R.layout.fragment_franchisee_info3;
    }

    @Override
    protected void initLayout(View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((BaseAppCompatActivity) getActivity()).setSupportActionBar(mToolbar);


        mBtnFinish = (Button)view.findViewById(R.id.btn_franchisee_info2_finish);
        mBtnFinish.setOnClickListener(this);
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

    private void checkSMS(String phoneNumber, String regCode) {
        //통신
        // retune_code 에 따라 mTvFindMsg msg 변경
        ResSMSCheck res = new ResSMSCheck(regCode);
        res.setParameter(phoneNumber, regCode);

        TNHttpMultiPartTask task = new TNHttpMultiPartTask(getActivity(), getFragmentManager());
        task.setOnHttpResultListener(this);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, res);
    }

    @Override
    protected void initRequest() {
//        mEditUserName.setText(UserInfoActivity.userName);
//        mEditBirthDay.setText(UserInfoActivity.userBirth);
//        mEditPhoneNumber.setText(UserInfoActivity.userMobile);
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

    private boolean onCheck() {
//        if (mEditUserName.getText().length() == 0) {
//            //showToast(getString(R.string.msg_error_join_id));
//            showToast("사용자 이름을 입력하세요.");
//            return false;
//        }
//        if (mEditBirthDay.getText().length() == 0) {
//            //showToast(getString(R.string.msg_error_join_pw_0));
//            showToast("생일을 입력하세요.");
//            return false;
//        }
//        if (mEditPhoneNumber.getText().length() == 0) {
//            //showToast(getString(R.string.msg_error_join_pw_1));
//            showToast("휴대폰 번호를 입력하세요.");
//            return false;
//        }
////        if (mbInjeonOk == false) {
////            showToast("본인 인증이 필요합니다.");
////            return false;
////        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_franchisee_info2_finish:
                getActivity().finish();
                break;
//            case R.id.btn_userinfo_send_injeon:
//                if (onCheck()) {
//                    requestSMS(mEditPhoneNumber.getText().toString());
//                    //requestSMS("010-9443-3244");
//                    mBtnSMSInJeon.setOnClickListener(null);
//                }
//                break;
//            case R.id.btn_userinfo_injeon_ok: {
//                checkSMS(mEditPhoneNumber.getText().toString(), mEditSMSNumber.getText().toString());
//            }
//            break;
//            case R.id.btn_userinfo_next:
//                //if (onCheck())
//            {
//                mSMSTimer.cancel();
//                //TNPreference.setMemPhoneNumber(getActivity(), mEditPhoneNumber.getText().toString());
//                //TNPreference.setMemName(getActivity(), mEditUserName.getText().toString());
//                UserInfoActivity.userMobile = mEditPhoneNumber.getText().toString();
//                UserInfoActivity.userName = mEditUserName.getText().toString();
//
//                //startActivity(new Intent(getActivity(), JoinStep2Activity.class));
//                //replaceAnimationTagFragment(((BaseAppCompatActivity) getActivity()).getMainViewId(), new JoinStep3Fragment(), JoinStep1Fragment.FRAGMENT_TAG, R.anim.layout_leftin, R.anim.layout_leftout);
//                replaceAnimationTagFragment(((BaseAppCompatActivity) getActivity()).getMainViewId(), new UserInfoFragment2(), UserInfoFragment2.FRAGMENT_TAG, R.anim.layout_leftin, R.anim.layout_leftout);
//            }
//            break;
//
//            case R.id.edit_userinfo_birthday:
//            {
//                // Create the new DatePickerDialog instance.
//                Bundle b = new Bundle();
//                //b.putString("dialog_msg", getString(R.string.msg_success_guide_ask));
//                b.putString("dialog_msg", "생년월일을 선택 하세요.");
//                SpinnerDateDialog date_dialog = new SpinnerDateDialog();
//                date_dialog.setArguments(b);
//                date_dialog.setOnResultListener(new BaseDialogFragment.OnResultListener() {
//                    @Override
//                    public void onDialogResult(Object... objects) {
//                        if (objects != null && objects[0] instanceof Boolean) {
//                            boolean isOk = (boolean) objects[0];
//                            if (isOk) {
//                                int year = (int)objects[1];
//                                int month = (int)objects[2];
//                                int day = (int)objects[3];
//
//                                String birth_day = String.format("%04d-%02d-%02d", year, month, day);
//                                Date newDate = null;
//                                try {
//                                    newDate = dateFormatter.parse(birth_day);
//                                } catch (ParseException e) {
//                                    e.printStackTrace();
//                                }
//
//                                mEditBirthDay.setText(dateFormatter.format(newDate.getTime()));
//                                UserInfoActivity.userBirth = dateFormatter.format(newDate.getTime()).toString();
//
//                                //TNPreference.setMemBirthDay(getActivity(), dateFormatter.format(newDate.getTime()).toString());
//                            }
//                        }
//                    }
//                });
//                date_dialog.show(getFragmentManager(), "dialog");
//            }
//            break;
        }

    }

    @Override
    public void onHttpNetSuccessEvent(BaseHttpResource[] o) {
//        if (o[0] instanceof ResSMSReq) {
//            ResSMSReq res = (ResSMSReq) o[0];
//            JSONObject obj = res.getParseData();
//            if (JSONParser.isSuccess(obj)) {
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
