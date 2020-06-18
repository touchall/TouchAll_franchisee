package allpointech.franchisee.franchisee.info;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tuna.ui.activity.BaseAppCompatActivity;
import com.tuna.ui.fragment.BaseFragment;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import allpointech.franchisee.R;
import allpointech.franchisee.custom.glide.GlideLoader;
import allpointech.franchisee.franchisee.FranchiseeMainFragment;
import allpointech.franchisee.network.http.HttpInfo;
import allpointech.franchisee.network.http.TNHttpMultiPartTask;
import allpointech.franchisee.network.http.json.JSONParser;
import allpointech.franchisee.network.http.resource.BaseHttpResource;
import allpointech.franchisee.network.http.resource.data.ResFranchiseeStoreIntro;
import allpointech.franchisee.network.http.resource.data.ResFranchiseeStoreLeaflet;
import allpointech.franchisee.network.http.resource.data.ResSMSReq;
import allpointech.franchisee.utils.TNPreference;

/**
 * Created by jay on 2019-02-18.
 */

public class FranchiseeStoreIntroFragment extends BaseFragment implements BaseAppCompatActivity.onKeyBackPressedListener, TNHttpMultiPartTask.onHttpNetResultListener, View.OnClickListener {
    public static final String FRAGMENT_TAG = FranchiseeStoreIntroFragment.class.getSimpleName();

    private Toolbar mToolbar;

    private TextView mTvHomepage;
    private ImageView mIvLogo;
    private LinearLayout mLiPhotos;
//    private ImageView mIvPhoto1;
//    private ImageView mIvPhoto2;
//    private ImageView mIvPhoto3;
    private TextView mTvCharacter;
    private LinearLayout mLiItems;
    private TextView mTvAddress;
    private TextView mTvParking;
    private TextView mTvSeat;
    private TextView mTvReservation;
    private TextView mTvVIP;
    private ImageView mIvAdvertise;

    private Button mBtnClose;

    private String mStoreId;

    @Override
    protected void BundleData(Bundle bundle) {
        setHasOptionsMenu(true);
        ((BaseAppCompatActivity) getActivity()).setOnKeyBackPressedListener(this);
        //mCurrentState = State.REG;
        if (getArguments() != null) {
            mStoreId = getArguments().getString("sid");
        }
    }

    @Override
    protected int inflateLayout() {
        return R.layout.fragment_franchisee_store_intro;
    }

    @Override
    protected void initLayout(View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((BaseAppCompatActivity) getActivity()).setSupportActionBar(mToolbar);

        mTvHomepage = (TextView) view.findViewById(R.id.text_franchisee_store_intro_homepage);
        mIvLogo = (ImageView) view.findViewById(R.id.imageview_franchisee_store_intro_logo);
        //mIvPhoto1 = (ImageView) view.findViewById(R.id.imageview_franchisee_store_intro_photo1);
        //mIvPhoto2 = (ImageView) view.findViewById(R.id.imageview_franchisee_store_intro_photo2);
        //mIvPhoto3 = (ImageView) view.findViewById(R.id.imageview_franchisee_store_intro_photo3);
        mLiPhotos = (LinearLayout) view.findViewById(R.id.li_franchisee_store_intro_photos);
        mTvCharacter = (TextView) view.findViewById(R.id.text_franchisee_store_intro_character);
        mLiItems = (LinearLayout) view.findViewById(R.id.li_franchisee_store_intro_items);
        mTvAddress = (TextView) view.findViewById(R.id.text_franchisee_store_intro_address);
        mTvParking = (TextView) view.findViewById(R.id.text_franchisee_store_intro_parking);
        mTvSeat = (TextView) view.findViewById(R.id.text_franchisee_store_intro_seat);
        mTvReservation = (TextView) view.findViewById(R.id.text_franchisee_store_intro_reservation);
        mTvVIP = (TextView) view.findViewById(R.id.text_franchisee_store_intro_vip);
        mIvAdvertise = (ImageView) view.findViewById(R.id.imageview_franchisee_store_intro_advertise);
        mBtnClose = (Button) view.findViewById(R.id.button_franchisee_store_intro_close);
        mBtnClose.setOnClickListener(this);
    }

    private void requestStoreIntro() {
        //통신
        // retune_code 에 따라 mTvFindMsg msg 변경
        ResFranchiseeStoreIntro res = new ResFranchiseeStoreIntro();
        res.setToken(TNPreference.getMemToken(getActivity()));

        res.setParameterQuestion(mStoreId);

        TNHttpMultiPartTask task = new TNHttpMultiPartTask(getActivity(), getFragmentManager());
        task.setOnHttpResultListener(this);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, res);
    }

    private void requestStoreLeaflet() {
        //통신
        // retune_code 에 따라 mTvFindMsg msg 변경
        ResFranchiseeStoreLeaflet res = new ResFranchiseeStoreLeaflet();
        res.setToken(TNPreference.getMemToken(getActivity()));

        res.setParameterQuestion(mStoreId, "leaflet");

        TNHttpMultiPartTask task = new TNHttpMultiPartTask(getActivity(), getFragmentManager());
        task.setOnHttpResultListener(this);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, res);
    }

    @Override
    protected void initRequest() {
//        mEditUserName.setText(UserInfoActivity.userName);
//        mEditBirthDay.setText(UserInfoActivity.userBirth);
//        mEditPhoneNumber.setText(UserInfoActivity.userMobile);
        requestStoreIntro();
        requestStoreLeaflet();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //getActivity().finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_franchisee_store_intro_close:
                onBack();
                break;
        }

    }

    @Override
    public void onHttpNetSuccessEvent(BaseHttpResource[] o) {

        if (o[0] instanceof ResFranchiseeStoreIntro) {
            ResFranchiseeStoreIntro res = (ResFranchiseeStoreIntro) o[0];
            JSONObject obj = res.getParseData();
            if (JSONParser.isSuccess(obj)) {

                JSONObject obj_message = JSONParser.getObject(obj, ResFranchiseeStoreIntro.KEY_RES.message);
                if (obj_message != null) {

                    final float scale = getResources().getDisplayMetrics().density;

                    String sid = JSONParser.getString(obj_message, "sid");
                    String logo = JSONParser.getString(obj_message, "logo");
                    String homepage = JSONParser.getString(obj_message, "homepage");
                    String explan = JSONParser.getString(obj_message, "explan");
                    String location = JSONParser.getString(obj_message, "location");
                    String parking = JSONParser.getString(obj_message, "parking");
                    String seat = JSONParser.getString(obj_message, "seat");
                    String reservation = JSONParser.getString(obj_message, "reservation");
                    String preference = JSONParser.getString(obj_message, "preference");
                    JSONArray items = JSONParser.getArray(obj_message, "items");
                    JSONArray images = JSONParser.getArray(obj_message, "images");
                    long updatedAt = JSONParser.getLong(obj_message, "updatedAt", 0L);
                    long createdAt = JSONParser.getLong(obj_message, "createdAt", 0L);

                    mTvHomepage.setText("홈페이지 주소 : " + homepage);

                    GlideLoader.loadView(getActivity(), HttpInfo.HOST_URL + logo, mIvLogo );

                    for (int i = 0 ; i < images.length() ; i+=3) {
//                        if (image_url != null) {
//                            if (i == 0)
//                                GlideLoader.loadView(getActivity(), HttpInfo.HOST_URL + image_url, mIvPhoto1);
//                            else if (i == 1)
//                                GlideLoader.loadView(getActivity(), HttpInfo.HOST_URL + image_url, mIvPhoto2);
//                            else if (i == 2)
//                                GlideLoader.loadView(getActivity(), HttpInfo.HOST_URL + image_url, mIvPhoto3);
//                        }


                        LinearLayout ll = new LinearLayout(getActivity());
                        ll.setOrientation(LinearLayout.HORIZONTAL);
                        ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        ll.setPadding((int)(6*scale), (int)(6*scale), (int)(6*scale), (int)(6*scale));

                        ImageView iv = new ImageView(getActivity());
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, (int)(60*scale), 1);
                        iv.setLayoutParams(params);
                        iv.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.color_ccc));
                        JSONObject obj_image = JSONParser.getArrayItem(images, i);
                        if (obj_image != null) {
                            String image_url = JSONParser.getString(obj_image, "url");
                            GlideLoader.loadView(getActivity(), HttpInfo.HOST_URL + image_url, iv);
                        }
                        ll.addView(iv);

                        View v = new View(getActivity());
                        v.setLayoutParams(new LinearLayout.LayoutParams((int)(10*scale), (int)(10*scale)));
                        ll.addView(v);

                        iv = new ImageView(getActivity());
                        params = new LinearLayout.LayoutParams(0, (int)(60*scale), 1);
                        iv.setLayoutParams(params);
                        iv.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.color_ccc));
                        obj_image = JSONParser.getArrayItem(images, i+1);
                        if (obj_image != null) {
                            String image_url = JSONParser.getString(obj_image, "url");
                            GlideLoader.loadView(getActivity(), HttpInfo.HOST_URL + image_url, iv);
                        }
                        ll.addView(iv);

                        v = new View(getActivity());
                        v.setLayoutParams(new LinearLayout.LayoutParams((int)(10*scale), (int)(10*scale)));
                        ll.addView(v);

                        iv = new ImageView(getActivity());
                        params = new LinearLayout.LayoutParams(0, (int)(60*scale), 1);
                        iv.setLayoutParams(params);
                        iv.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.color_ccc));
                        obj_image = JSONParser.getArrayItem(images, i+2);
                        if (obj_image != null) {
                            String image_url = JSONParser.getString(obj_image, "url");
                            GlideLoader.loadView(getActivity(), HttpInfo.HOST_URL + image_url, iv);
                        }
                        ll.addView(iv);

                        mLiPhotos.addView(ll);
                    }

                    mTvCharacter.setText(explan);


                    for (int i = 0 ; i < items.length() ; i++) {
                        JSONObject obj_item = JSONParser.getArrayItem(items, i);
                        if (obj_item != null) {

                            String item_sid = JSONParser.getString(obj_item, "sid");
                            int item_idx = JSONParser.getInt(obj_item, "sid", 0);
                            String item_name = JSONParser.getString(obj_item, "name");
                            String item_image = JSONParser.getString(obj_item, "image");
                            int item_price = JSONParser.getInt(obj_item, "price", 0);
                            String item_explan = JSONParser.getString(obj_item, "explan");
                            String item_author = JSONParser.getString(obj_item, "author");
                            long item_createdAt = JSONParser.getLong(obj_item, "createdAt", 0L);


                            LinearLayout ll = new LinearLayout(getActivity());
                            ll.setOrientation(LinearLayout.HORIZONTAL);
                            ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT));

                            LinearLayout ll1 = new LinearLayout(getActivity());
                            ll1.setOrientation(LinearLayout.VERTICAL);
                            ll1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT));

                            LinearLayout ll2 = new LinearLayout(getActivity());
                            ll2.setOrientation(LinearLayout.HORIZONTAL);
                            ll2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT));

                            LinearLayout ll3 = new LinearLayout(getActivity());
                            ll3.setOrientation(LinearLayout.VERTICAL);
                            ll3.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                            TextView tv = new TextView(getActivity());
                            tv.setText(item_name);
                            //tv.setId(10000+2*i);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int)(60*scale), (int)(30*scale));
                            //params.weight = 1.0f;
                            params.gravity = Gravity.CENTER_VERTICAL;
                            tv.setLayoutParams(params);
                            //tv.setTextSize(16);
                            //tv.setPadding(5, 3, 0, 3);
                            ll3.addView(tv);

                            TextView tv1 = new TextView(getActivity());
                            tv1.setText(Integer.toString(item_price));
                            //tv1.setId(10000+2*i+1);
                            LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams((int)(60*scale), (int)(30*scale));
                            //params1.weight = 1.0f;
                            params1.gravity = Gravity.CENTER_VERTICAL;
                            tv1.setLayoutParams(params1);
                            ll3.addView(tv1);
                            ll2.addView(ll3);

                            View v = new View(getActivity());
                            v.setLayoutParams(new LinearLayout.LayoutParams((int)(10*scale), LinearLayout.LayoutParams.MATCH_PARENT));
                            ll2.addView(v);

                            ImageView iv = new ImageView(getActivity());
                            iv.setLayoutParams(new LinearLayout.LayoutParams((int)(80*scale), (int)(60*scale)));
                            GlideLoader.loadView(getActivity(), HttpInfo.HOST_URL + item_image, iv);
                            ll2.addView(iv);

                            View v1 = new View(getActivity());
                            v1.setLayoutParams(new LinearLayout.LayoutParams((int)(10*scale), LinearLayout.LayoutParams.MATCH_PARENT));
                            ll2.addView(v1);

                            TextView tvExplain = new TextView(getActivity());
                            tvExplain.setText(item_explan);
                            tvExplain.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
                            ll2.addView(tvExplain);

                            ll1.addView(ll2);
                            ll.addView(ll1);

                            mLiItems.addView(ll);
                            //mLiItems.getLayoutParams().height = mLiItems.getLayoutParams().height + 60;
                        }
                    }

                    mTvAddress.setText(location);

                    if (parking == null || parking.length() <= 0 || parking.compareTo("0") == 0)
                        mTvParking.setText("불가");
                    else
                        mTvParking.setText(parking + "대가능");

                    if (seat == null || seat.length() <= 0 || seat.compareTo("0") == 0)
                        mTvSeat.setText("없음");
                    else
                        mTvSeat.setText(seat + "개");

                    if (reservation == null || reservation.length() <= 0 || reservation.compareTo("0") == 0)
                        mTvReservation.setText("불가");
                    else
                        mTvReservation.setText(reservation);

                    if (preference == null || preference.length() <= 0 || preference.compareTo("0") == 0)
                        mTvVIP.setText("없음");
                    else
                        mTvVIP.setText(preference + " 우대");

                }

            }
        }

        if (o[0] instanceof ResFranchiseeStoreLeaflet) {
            ResFranchiseeStoreLeaflet res = (ResFranchiseeStoreLeaflet) o[0];
            JSONObject obj = res.getParseData();
            if (JSONParser.isSuccess(obj)) {

                //JSONObject obj_message = JSONParser.getObject(obj, ResFranchiseeStoreLeaflet.KEY_RES.message);
                JSONArray obj_message =  JSONParser.getArray(obj, ResFranchiseeStoreLeaflet.KEY_RES.message);
                if (obj_message != null && obj_message.length() > 0) {

                    for (int i = 0 ; i < obj_message.length() ; i++) {

                        JSONObject obj_ad = JSONParser.getArrayItem(obj_message, i);
                        if (obj_ad != null) {
                            String sid = JSONParser.getString(obj_ad, "sid");
                            String url = JSONParser.getString(obj_ad, "url");
                            String sdate = JSONParser.getString(obj_ad, "sdate");
                            String edate = JSONParser.getString(obj_ad, "edate");
                            String author = JSONParser.getString(obj_ad, "author");
                            String status = JSONParser.getString(obj_ad, "status");
                            long createdAt = JSONParser.getLong(obj_ad, "createdAt", 0L);

                            if (url != null) {
                                GlideLoader.loadView(getActivity(), HttpInfo.HOST_URL + url, mIvAdvertise);
                            }

                            break;
                        }
                    }
                }
            }
        }
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
