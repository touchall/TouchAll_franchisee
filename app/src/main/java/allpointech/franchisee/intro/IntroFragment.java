package allpointech.franchisee.intro;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;

import com.tuna.ui.fragment.BaseFragment;
import com.tuna.utils.SLog;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import allpointech.franchisee.R;
import allpointech.franchisee.franchisee.FranchiseeMainActivity;
import allpointech.franchisee.login.LoginActivity;
import allpointech.franchisee.network.gcm.GcmRegUtil;
import allpointech.franchisee.network.http.SSLConnect;
import allpointech.franchisee.network.http.TNHttpMultiPartTask;
import allpointech.franchisee.network.http.json.JSONParser;
import allpointech.franchisee.network.http.resource.BaseHttpResource;
import allpointech.franchisee.network.http.resource.data.ResLogin;
import allpointech.franchisee.utils.TNConstants;
import allpointech.franchisee.utils.TNPermissionCheck;
import allpointech.franchisee.utils.TNPreference;

/**
 * Created by daze on 2016-11-14.
 */

public class IntroFragment extends BaseFragment implements TNHttpMultiPartTask.onHttpNetResultListener, TNHttpMultiPartTask.onHttpNetFailResultListener {
    public static final String FRAGMENT_TAG = IntroFragment.class.getSimpleName();

    private String mGcmData;

    @Override
    protected void BundleData(Bundle bundle) {
        if (getArguments() != null) {
            mGcmData = getArguments().getString("data");
        }
    }

    @Override
    protected int inflateLayout() {
        return R.layout.fragment_intro;
    }

    @Override
    protected void initLayout(View view) {

    }

    @Override
    protected void initRequest() {
        String permission[] = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        Intent i = new Intent(getActivity(), TNPermissionCheck.class);
        i.putExtra("permission", permission);
        startActivityForResult(i, TNConstants.PERMISSION.PERMISSION_RECORD);
    }

    private void startLogin() {

        String phoneNumber = TNPreference.getMemphoneNumber(getActivity());
        String password = TNPreference.getMemPw(getActivity());
        SLog.LogD("Ace :");

        // SJH : 가맹점.
//        loginLocal("rkaodghldnjs1", "1234");
        if (TNPreference.getAutoLogin(getActivity()) && phoneNumber != null && password != null) {
            loginLocal(phoneNumber, password);
        } else {
            startActivityForResult(new Intent(getActivity(), LoginActivity.class), TNConstants.REQUEST_CODE.REFRESH);
        }

    }

    private void startMain() {

        // SJH : 임시 가맹점 화면 실행.

        Intent intent = new Intent(getActivity(), FranchiseeMainActivity.class);
        if (mGcmData != null) {
            SLog.LogD("Ace : " + mGcmData);
            intent.putExtra("obj", mGcmData);
        }
        startActivityForResult(intent, TNConstants.REQUEST_CODE.APP_FINISH);

//        Intent intent = new Intent(getActivity(), UserMainActivity.class);
//        if (mGcmData != null) {
//            SLog.LogD("Ace : " + mGcmData);
//            intent.putExtra("obj", mGcmData);
//        }
//        startActivityForResult(intent, TNConstants.REQUEST_CODE.APP_FINISH);
    }

    private void loginLocal(String phoneNumber, String passwd) {
//        ResLogin res = new ResLogin();
//        res.setParameter(
//                phoneNumber,
//                passwd,
//                GcmRegUtil.getRegId(getActivity())
//        );

        final String androidId = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        ResLogin res = new ResLogin();
        res.setParameter(
                phoneNumber,
                passwd,
                //"U",
                "S",  // 가맹점
                GcmRegUtil.getRegId(getActivity()),
                androidId
        );

        TNHttpMultiPartTask task = new TNHttpMultiPartTask(getActivity(), getFragmentManager());
        task.setOnHttpResultListener(this);
        task.setOnHttpFailResultListener(this);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, res);
    }

    @Override
    public void onHttpNetSuccessEvent(BaseHttpResource[] o) {
        if (o[0] instanceof ResLogin) {
            ResLogin res = (ResLogin) o[0];
            JSONObject object = res.getParseData();
            if (object != null && JSONParser.isSuccess(object)) {
                //JSONObject obj_member = JSONParser.getObject(object, ResLogin.KEY_RES.message);
                //if (obj_member != null)
                {
                    SLog.LogD("Auto Login OK !");
                    //TNPreference.setUserInfo(getActivity(), obj_member);
                    startMain();
                }
            }
        }
    }

    @Override
    public void onHttpDebugEvent(String debug_msg) {

    }

    @Override
    public void onHttpNetFailEvent(int errorCode, String errorMsg) {
        startActivityForResult(new Intent(getActivity(), LoginActivity.class), TNConstants.REQUEST_CODE.REFRESH);
    }

    @Override
    public void onHttpNetFailResultListener(BaseHttpResource resource) {
        clearPre();
        startActivityForResult(new Intent(getActivity(), LoginActivity.class), TNConstants.REQUEST_CODE.APP_LOGIN);
    }

    private void clearPre() {
        TNPreference.clearUserInfo(getActivity());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TNConstants.REQUEST_CODE.APP_LOGIN:
                if (resultCode == Activity.RESULT_OK) {
                    startMain();
                } else {
                    getActivity().finish();
                }
                break;
            case TNConstants.REQUEST_CODE.APP_FINISH:
                if (resultCode == Activity.RESULT_OK) {
                    getActivity().finish();
                }
                break;
            case TNConstants.REQUEST_CODE.REFRESH:
                if (resultCode == Activity.RESULT_OK) {
                    startMain();
                }
                break;
            case TNConstants.PERMISSION.PERMISSION_RECORD:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        String granteds[] = data.getStringArrayExtra("result_granted");
                        String denials[] = data.getStringArrayExtra("result_denial");
                        if (granteds != null && granteds.length > 1) {
                            if (Manifest.permission.READ_EXTERNAL_STORAGE.equalsIgnoreCase(granteds[0]) && Manifest.permission.WRITE_EXTERNAL_STORAGE.equalsIgnoreCase(granteds[1])) {
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        startLogin();
                                    }
                                }, 3000);
                            } else {
                                getActivity().finish();
                            }
                        }
//                        if (denials != null && denials.length > 0) {
//                            if (Manifest.permission.RECORD_AUDIO.equals(denials[0])) {
//                                showToast(getString(R.string.msg_error_permission));
//                            }
//                        }
                    }
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    public class HttpUtil extends AsyncTask<String, Void, Void> {
        @Override
        public Void doInBackground(String... params) {
            try {
                String url = "https://dev.touchall.co.kr/api/cert/";

                // SJH   ssl >>>>>>>>>>>>>>>>>>>>
                SSLConnect ssl = new SSLConnect();
                ssl.postHttps(url, 1000, 1000);
                // SJH   >>>>>>>>>>>>>>>>>>>>

                URL obj = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) obj.openConnection();

                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

                byte[] outputInBytes = params[0].getBytes("UTF-8");
                OutputStream os = conn.getOutputStream();
                os.write( outputInBytes );
                os.close();

                int retCode = conn.getResponseCode();

                InputStream is = conn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer response = new StringBuffer();
                while((line = br.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                br.close();

                String res = response.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;

        }
    }

}
