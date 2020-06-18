package allpointech.franchisee.franchisee.users;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

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

public class FranchiseeUsersFragment extends BaseFragment implements BaseAppCompatActivity.onKeyBackPressedListener, TNHttpMultiPartTask.onHttpNetResultListener, View.OnClickListener {
    public static final String FRAGMENT_TAG = FranchiseeUsersFragment.class.getSimpleName();

    private Toolbar mToolbar;

//
//    private Button mBtnNext;
//    private Button mBtnModify;
//

    @Override
    protected void BundleData(Bundle bundle) {
        setHasOptionsMenu(true);
        //mCurrentState = State.REG;
    }

    @Override
    protected int inflateLayout() {
        return R.layout.fragment_franchisee_users;
    }

    @Override
    protected void initLayout(View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((BaseAppCompatActivity) getActivity()).setSupportActionBar(mToolbar);

//
//        mBtnNext = (Button)view.findViewById(R.id.btn_franchisee_info_next);
//        mBtnNext.setOnClickListener(this);
//        mBtnModify = (Button)view.findViewById(R.id.btn_franchisee_info_modify);
//        mBtnModify.setOnClickListener(this);
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
//            case R.id.btn_franchisee_info_next:
//                startActivityForResult(new Intent(getActivity(), FranchiseeSetupActivity.class), 12123);
//                break;
        }

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
