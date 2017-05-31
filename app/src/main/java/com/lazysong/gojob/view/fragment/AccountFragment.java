package com.lazysong.gojob.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lazysong.gojob.view.activity.CheckUserInfoActivity;
import com.lazysong.gojob.module.LocalInfoManager;
import com.lazysong.gojob.view.activity.MainActivity;
import com.lazysong.gojob.R;
import com.lazysong.gojob.module.ServerInfoManager;
import com.lazysong.gojob.view.activity.SettingsActivity;
import com.lazysong.gojob.utils.Util;
import com.lazysong.gojob.module.beans.User;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

public class AccountFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private User user;
    private boolean logined;

    private OnFragmentInteractionListener mListener;

    private RelativeLayout layoutUserInfo;
    private TextView tvNickname;
    private ImageView userImage;
    private TextView sign;
    private RelativeLayout layoutSettings;
    private static Tencent mTencent;
    private UserInfo mInfo;
    private LocalInfoManager localManager;
    private ServerInfoManager serverManger;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        user = new User();
        user.initUser();
        localManager = new LocalInfoManager(getContext());
        serverManger = new ServerInfoManager(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        initViews(view);

        loadLoginState();
        //如果已经登录过，则加载本地保存的User信息
        if (logined)
            loadUserInfo();
        setUiData();

        //创建mTencent实例，作为QQ登录的事务代理
        if (mTencent == null)
            mTencent = Tencent.createInstance(MainActivity.appId, getContext());
        initListener();
        return view;
    }

    private void setUiData() {
        if(!logined) {
            tvNickname.setText("点击登录");
            sign.setText("未设置");
            Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.bilibili);
            userImage.setImageBitmap(bitmap);
            return;
        }
        if(user.getImg() != null)
            userImage.setImageBitmap(user.getImg());
        tvNickname.setText(user.getNickname());
        sign.setText(user.getSign());
    }

    //对布局控件进行初始化
    private void initViews(View view) {
        tvNickname = (TextView) view.findViewById(R.id.tvNickname);
        layoutUserInfo = (RelativeLayout) view.findViewById(R.id.layoutUserInfo);
        userImage = (ImageView) view.findViewById(R.id.imgUser);
        layoutSettings = (RelativeLayout) view.findViewById(R.id.layoutSettings);
        sign = (TextView) view.findViewById(R.id.tvsign);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        final int id = v.getId();
        switch (id) {
            case R.id.layoutSettings:
                intent.setClass(getContext(), SettingsActivity.class);
                startActivityForResult(intent, MainActivity.REQUEST_SETTINGS);
                break;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onChangeToolbarTitle(int data);
    }

    private void loadLoginState() {
        JSONObject jsonObject = localManager.getLogPref();
        try {
            logined = jsonObject.getBoolean("logined");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadUserInfo() {
        JSONObject jsonObject = localManager.getUserInfo();
        try {
            user = (User) jsonObject.get("user");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //为控件设置监听器
    private void initListener() {
        layoutUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!logined)
                    QQLogin();
                else {
                    //跳转到编辑用户信息界面
                    Intent intent = new Intent();
                    intent.setClass(getContext(), CheckUserInfoActivity.class);
                    intent.putExtra("user", (Parcelable) user);
                    startActivity(intent);
                }
            }
        });
        layoutSettings.setOnClickListener(this);
    }

    private void QQLogin() {
        if (!mTencent.isSessionValid()) {
            mTencent.login(this, "all", loginListener);
            logined = true;
        } else {
            if (!logined) {
                mTencent.login(this, "all", loginListener);
                logined = true;
            }
            else {
                mTencent.logout(getContext());
                logined = false;
            }
        }

    }

    IUiListener loginListener = new BaseUiListener() {
        @Override
        protected void doComplete(JSONObject values) {
            initOpenidAndToken(values);
            onResponseGot();
        }
    };


    //根据网络请求的返回结果设置OpenId和AccessToken
    public static void initOpenidAndToken(JSONObject jsonObject) {
        try {
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)) {
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);
            }
        } catch(Exception e) {
        }
    }

    private void onResponseGot() {
        if (mTencent != null && mTencent.isSessionValid()) {
            IUiListener listener = new IUiListener() {
                @Override
                public void onError(UiError e) {
                }

                @Override
                public void onComplete(final Object response) {
                    Message msg = new Message();
                    msg.obj = response;
                    msg.what = 0;
                    mHandler.sendMessage(msg);
                    new Thread(){

                        @Override
                        public void run() {
                            JSONObject json = (JSONObject)response;
                            if(json.has("figureurl")){
                                Bitmap bitmap = null;
                                try {
                                    bitmap = Util.getbitmap(json.getString("figureurl_qq_2"));
                                } catch (JSONException e) {

                                }
                                Message msg = new Message();
                                msg.obj = bitmap;
                                msg.what = 1;
                                mHandler.sendMessage(msg);
                            }
                        }

                    }.start();
                }

                @Override
                public void onCancel() {

                }
            };
            mInfo = new UserInfo(getContext(), mTencent.getQQToken());
            mInfo.getUserInfo(listener);

        } else {
            tvNickname.setText(R.string.login);
            userImage.setImageResource(R.drawable.bilibili);
        }
    }

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            user.setUserid(mTencent.getOpenId());

            //如果不是首次登录，则从服务器下载用户数据
            if (serverManger.userExists(user.getUserid())) {
                try {
                    user = (User) serverManger.getUserInfo(user.getUserid()).get("user");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                if (msg.what == 0) { //如果是首次登录，则更新界面上的头像，昵称，签名，并更新根本地的用户信息
                    JSONObject response = (JSONObject) msg.obj;
                    if (response.has("nickname")) {
                        try {
                            user.setNickname(response.getString("nickname"));
                            user.setUserid(mTencent.getOpenId());
                            tvNickname.setText(user.getNickname());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else if (msg.what == 1) {
                    Bitmap bitmap = (Bitmap) msg.obj;
                    user.setImgName(mTencent.getOpenId());
                    user.setImg(bitmap);
                    userImage.setImageBitmap(bitmap);
                }
                if(!hasMessages(0) && !hasMessages(1)) {
                    serverManger.saveUserInfo(user);
                }
            }
            localManager.setLogin(logined);
            localManager.setUser(user);
        }
    };

    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            if (null == response) {
                Util.showResultDialog(getContext(), "返回为空", "登录失败");
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (null != jsonResponse && jsonResponse.length() == 0) {
                Util.showResultDialog(getContext(), "返回为空", "登录失败");
                return;
            }
//            Util.showResultDialog(getContext(), response.toString(), "登录成功");
            doComplete((JSONObject)response);
        }

        protected void doComplete(JSONObject values) {

        }

        @Override
        public void onError(UiError e) {
            Util.toastMessage(getActivity(), "onError: " + e.errorDetail);
            Util.dismissDialog();
        }

        @Override
        public void onCancel() {
            Util.toastMessage(getActivity(), "onCancel: ");
            Util.dismissDialog();
            if (logined) {
                logined = false;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN ||
                requestCode == Constants.REQUEST_APPBAR) {
            Tencent.onActivityResultData(requestCode,resultCode,data,loginListener);
        }
        else if (requestCode == MainActivity.REQUEST_SETTINGS && resultCode == MainActivity.RESULT_LOGOUT) {
            Toast.makeText(getContext(), "reslutCode == RESULT_LOGOUT", Toast.LENGTH_SHORT).show();
            mTencent.logout(getContext());
            logined = false;
            localManager.setLogin(logined);
            setUiData();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListener.onChangeToolbarTitle(MainActivity.FRAGMENT_ACCOUNT);
    }
}
