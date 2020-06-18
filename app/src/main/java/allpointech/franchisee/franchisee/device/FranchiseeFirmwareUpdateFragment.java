package allpointech.franchisee.franchisee.device;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tuna.ui.activity.BaseAppCompatActivity;
import com.tuna.ui.fragment.BaseFragment;
import com.tuna.utils.SLog;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import allpointech.franchisee.R;
import allpointech.franchisee.network.http.TNHttpMultiPartTask;
import allpointech.franchisee.network.http.json.JSONParser;
import allpointech.franchisee.network.http.resource.BaseHttpResource;
import allpointech.franchisee.network.http.resource.data.ResFranchiseeStoreDeviceDetail;
import allpointech.franchisee.network.http.resource.data.ResFranchiseeStoreDevicePreUpdate;
import allpointech.franchisee.network.http.resource.data.ResReqTelegram;
import allpointech.franchisee.utils.TNPreference;

/**
 * Created by daze on 2016-11-15.
 */

public class FranchiseeFirmwareUpdateFragment extends BaseFragment implements BaseAppCompatActivity.onKeyBackPressedListener, TNHttpMultiPartTask.onHttpNetResultListener, View.OnClickListener, FranchiseeFirmwareUpdateActivity.OnNfcCompleteListener {
    public static final String FRAGMENT_TAG = FranchiseeFirmwareUpdateFragment.class.getSimpleName();

    private Toolbar mToolbar;

    private LinearLayout mLiFirmwareUpdateInfo;
    private LinearLayout mLiFirmwareUpdateStart;
    private LinearLayout mLiFirmwareUpdateComplete;

    private TextView mTvFirmwareLastVersion;
    private TextView mTvFirmwareLastDate;
    private TextView mTvFirmwareLastDescription;

    private Button mBtnFirmwareDownload;
    private Button mBtnFirmwareLocalLoad;

    private Button mBtnFirmwareUpdateOk;


    private PowerManager.WakeLock mWakeLock;
    private  final String device_fw = "device_fw=";
    private byte [] fw_data = {};

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
                    FranchiseeFirmwareUpdateActivity activity = (FranchiseeFirmwareUpdateActivity)getActivity();

                    if (activity != null) {
                        //activity._enableNdefExchangeMode("device_setting=" + mDeviceSetupTelegram);
                        //activity._enableTagWriteMode();

                        activity._enableFirmwareUpdate(fw_data);

                        mLiFirmwareUpdateInfo.setVisibility(View.GONE);
                        mLiFirmwareUpdateStart.setVisibility(View.VISIBLE);
                        mLiFirmwareUpdateComplete.setVisibility(View.GONE);
                    }
                }
                break;

                case SEND_NFC_OFF:
                {
                    FranchiseeFirmwareUpdateActivity activity = (FranchiseeFirmwareUpdateActivity)getActivity();

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
        }
    }

    @Override
    protected int inflateLayout() {
        return R.layout.fragment_franchisee_firmware_update;
    }

    @Override
    protected void initLayout(View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((BaseAppCompatActivity) getActivity()).setSupportActionBar(mToolbar);

        FranchiseeFirmwareUpdateActivity firmwareUpdateActivity = (FranchiseeFirmwareUpdateActivity) getActivity();
        firmwareUpdateActivity.setOnNfcCompleteListener(this);

        mLiFirmwareUpdateInfo = (LinearLayout)view.findViewById(R.id.li_franchisee_firmware_info);
        mLiFirmwareUpdateInfo.setVisibility(View.VISIBLE);
        mLiFirmwareUpdateStart = (LinearLayout)view.findViewById(R.id.li_franchisee_firmware_update_start);
        mLiFirmwareUpdateStart.setVisibility(View.GONE);
        mLiFirmwareUpdateComplete = (LinearLayout)view.findViewById(R.id.li_franchisee_firmware_update_complete);
        mLiFirmwareUpdateComplete.setVisibility(View.GONE);

        mTvFirmwareLastVersion = (TextView)view.findViewById(R.id.text_fw_last_version);
        mTvFirmwareLastVersion = (TextView)view.findViewById(R.id.text_fw_last_date);
        mTvFirmwareLastVersion = (TextView)view.findViewById(R.id.text_fw_last_description);

        mBtnFirmwareDownload = (Button)view.findViewById(R.id.button_franchisee_fw_download);
        mBtnFirmwareDownload.setOnClickListener(this);
        mBtnFirmwareLocalLoad = (Button)view.findViewById(R.id.button_franchisee_fw_local_load);
        mBtnFirmwareLocalLoad.setOnClickListener(this);

        mBtnFirmwareUpdateOk = (Button)view.findViewById(R.id.button_franchisee_firmware_update_ok);
        mBtnFirmwareUpdateOk.setOnClickListener(this);

    }

    private void writeFile() {
        /////////////////////// 파일 쓰기 ///////////////////////
        String str = "test";//input_text.getText().toString();
        // 파일 생성
        File saveFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/touchall_fw"); // 저장 경로
        // 폴더 생성
        if(!saveFile.exists()){ // 폴더 없을 경우
            saveFile.mkdir(); // 폴더 생성
        }
        try {
            long now = System.currentTimeMillis(); // 현재시간 받아오기
            Date date = new Date(now); // Date 객체 생성
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String nowTime = sdf.format(date);

            BufferedWriter buf = new BufferedWriter(new FileWriter(saveFile+"/CarnumData.txt", true));
            buf.append(nowTime + " "); // 날짜 쓰기
            buf.append(str); // 파일 쓰기
            buf.newLine(); // 개행
            buf.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //    public static byte[] readBytesFromFile(File file) throws IOException {
    private byte[] readBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        // Get the size of the file
        long length = file.length();

        // You cannot create an array using a long type.
        // It needs to be an int type.
        // Before converting to an int type, check
        // to ensure that file is not larger than Integer.MAX_VALUE.
        if (length > Integer.MAX_VALUE) {
            throw new IOException("Could not completely read file " + file.getName() + " as it is too long (" + length + " bytes, max supported " + Integer.MAX_VALUE + ")");
        }

        // Create the byte array to hold the data
        byte[] bytes = new byte[(int)length];

        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }

        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        // Close the input stream and return bytes
        is.close();
//        return bytes;

        int data_length = (int)length;
        byte[] data_sizes = ByteBuffer.allocate(4).putInt(data_length).array();
        byte[] datas = new byte[device_fw.length()+4+(int)length];
        System.arraycopy(device_fw.getBytes(), 0, datas, 0, device_fw.length());
        System.arraycopy(data_sizes, 0, datas, device_fw.length(), data_sizes.length);
        System.arraycopy(bytes, 0, datas, device_fw.length()+data_sizes.length, (int)length);

        return datas;
    }

    private void readFile() {
        /////////////////////// 파일 읽기 ///////////////////////
        // 파일 생성
        String line = null; // 한줄씩 읽기
        String file_text = "";
        //byte [] read_data = {};
        File saveFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/touchall_fw"); // 저장 경로
        // 폴더 생성
        if(!saveFile.exists()){ // 폴더 없을 경우
            saveFile.mkdir(); // 폴더 생성
        }
        try {

            File file = new File(saveFile.getAbsolutePath(), "/ob_device.fw");
            //InputStream in = new BufferedInputStream(new FileInputStream(file));
            //byte[] buf = new byte[file.length()];
            //int numRead = in.read(buf);

            fw_data = readBytesFromFile(file);

//            BufferedReader buf = new BufferedReader(new FileReader(saveFile+"/CarnumData.txt"));
//            while((line=buf.readLine())!=null){
//                //tv.append(line);
//                //tv.append("\n");
//
//                file_text += line;
//            }
//            buf.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //mMsgData = encodeHexString(read_data);
        //Toast.makeText(getActivity(), ToHex(read_data), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void initRequest() {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //해제
                if(mWakeLock != null){
                    if(mWakeLock.isHeld()){
                        mWakeLock.release();
                    }
                }
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_franchisee_fw_download:
            case R.id.button_franchisee_fw_local_load:{
                final PowerManager powerMgr = (PowerManager)getActivity().getSystemService(Context.POWER_SERVICE);
                mWakeLock = powerMgr.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, this.getClass().getName());
                if(mWakeLock != null ){
                    mWakeLock.acquire();
                }

                //writeFile();
                readFile();

                Message msg = mMainHandler.obtainMessage();
                msg.what = SEND_NFC_ON;
                mMainHandler.handleMessage(msg);

                Toast.makeText(getActivity(), "NFC Firmware Make complete.", Toast.LENGTH_SHORT).show();
            }
                break;
            case R.id.button_franchisee_firmware_update_ok:
                //해제
                if(mWakeLock != null){
                    if(mWakeLock.isHeld()){
                        mWakeLock.release();
                    }
                }
                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
                break;
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

    @Override
    public void onComplete() {
        mLiFirmwareUpdateInfo.setVisibility(View.GONE);
        mLiFirmwareUpdateStart.setVisibility(View.GONE);
        mLiFirmwareUpdateComplete.setVisibility(View.VISIBLE);
    }
}
