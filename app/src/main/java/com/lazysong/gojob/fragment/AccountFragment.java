package com.lazysong.gojob.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lazysong.gojob.EditUserInfoActivity;
import com.lazysong.gojob.MainActivity;
import com.lazysong.gojob.PreferenceUtils;
import com.lazysong.gojob.R;
import com.lazysong.gojob.SettingsActivity;
import com.lazysong.gojob.Util;
import com.lazysong.gojob.com.lazysong.gojob.beans.User;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AccountFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
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
    private RelativeLayout layoutSettings;
    private static Tencent mTencent;
    private UserInfo mInfo;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        loadLoginState();
        initViews(view);
        if(!logined) {
            tvNickname.setText("点击登录");
        }
        else {//用户已经登陆过，采用本地的用户资料，包括头像，昵称等
            user = getLocalUserInfo();
            if(user != null)
                tvNickname.setText(user.getNickname());
            else
                tvNickname.setText("用户已登录");
            //可以进一步换成getBitmapFromName(...);
            try {
                FileInputStream input = getContext().openFileInput(user.getImgname());
                byte[] buffer = new byte[1024*100];
                input.read(buffer);
                Bitmap bitmap = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
                userImage.setImageBitmap(bitmap);
                input.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //创建mTencent实例，作为QQ登录的事务代理
        if (mTencent == null)
            mTencent = Tencent.createInstance(MainActivity.appId, getContext());
        initListener();
        return view;
    }

    //对布局控件进行初始化
    private void initViews(View view) {
        tvNickname = (TextView) view.findViewById(R.id.tvNickname);
        layoutUserInfo = (RelativeLayout) view.findViewById(R.id.layoutUserInfo);
        userImage = (ImageView) view.findViewById(R.id.imgUser);
        layoutSettings = (RelativeLayout) view.findViewById(R.id.layoutSettings);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void loadLoginState() {
        JSONObject jsonObject = PreferenceUtils.readUserInfo(getContext());
        try {
            logined = jsonObject.getBoolean("logined");
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
                    intent.setClass(getContext(), EditUserInfoActivity.class);
                    startActivity(intent);
                }
            }
        });
        layoutSettings.setOnClickListener(this);
    }

    //执行通过QQ登录的操作
    private void QQLogin() {
        JSONObject jsonObject = PreferenceUtils.getLogPref(getContext());
        try {
            logined = jsonObject.getBoolean("logined");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (!mTencent.isSessionValid()) {
            mTencent.login(this, "all", loginListener);
            logined = true;
            PreferenceUtils.setLoginPref(getContext(), true,mTencent.getOpenId());
        } else {
            if (!logined) {
                mTencent.login(this, "all", loginListener);
                logined = true;
                PreferenceUtils.setLoginPref(getContext(), true,mTencent.getOpenId());
            }
            else {
                mTencent.logout(getContext());
                logined = false;
                PreferenceUtils.setLoginPref(getContext(), false, null);
            }
            updateUserInfo();
            updateLoginButton();
        }
    }

    IUiListener loginListener = new BaseUiListener() {
        @Override
        protected void doComplete(JSONObject values) {
            initOpenidAndToken(values);
            updateUserInfo();
            PreferenceUtils.setLoginPref(getContext(), true, mTencent.getOpenId());
            updateLoginButton();
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

    private void updateUserInfo() {
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
            /*tvNickname.setVisibility(android.view.View.GONE);
            userImage.setVisibility(android.view.View.GONE);*/
        }
    }

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            //如果不是首次登录，则从本地读取用户状态
            if(!isFirstLogin()) {
                user = getLocalUserInfo();
                tvNickname.setText(user.getNickname());
                userImage.setImageBitmap(getBitmapFromName(user.getImgname(), 1024*100));
                return;
            }
            //如果是首次登录，则更新界面上的头像，昵称，签名，并更新根本地的用户信息
            if (msg.what == 0) {
                JSONObject response = (JSONObject) msg.obj;
                if (response.has("nickname")) {
                    try {
                        tvNickname.setVisibility(android.view.View.VISIBLE);
                        tvNickname.setText(response.getString("nickname"));
                        if(user == null)
                            user = new User();
                        user.setNickname(response.getString("nickname"));
                        user.setUserid(mTencent.getOpenId());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }else if(msg.what == 1){
                Bitmap bitmap = (Bitmap)msg.obj;
                userImage.setImageBitmap(bitmap);
                userImage.setVisibility(android.view.View.VISIBLE);
                if(user == null)
                    user = new User();
                user.setImgname("userPic.png");
            }
            PreferenceUtils.writeUserInfo(getContext(), true, user);
        }

    };

    private void updateLoginButton() {
        if (mTencent != null && mTencent.isSessionValid()) {
            if (!logined) {
                tvNickname.setText("登录");
            } else {
//                btnQQLogin.setTextColor(Color.RED);
//                btnQQLogin.setText("退出帐号");
            }
        }
        else {
            tvNickname.setText("登录");
        }
    }

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
            Util.showResultDialog(getContext(), response.toString(), "登录成功");
            /*// 有奖分享处理
            handlePrizeShare();*/
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
            PreferenceUtils.setLoginPref(getContext(), false, null);
//            updateLoginButton();
            updateUserInfo();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    //向服务器查询用户是否首次登录系统
    private boolean isFirstLogin() {
        return true;
    }

    private User getLocalUserInfo() {//从本地的文件中获得User对象
        User user = null;
        JSONObject jsonObject = PreferenceUtils.readUserInfo(getContext());
        try {
            user = (User) jsonObject.get("user");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    private Bitmap getBitmapFromName(String name, int size) {
        Bitmap bitmap = null;
        try {
            FileInputStream input = getContext().openFileInput(name);
            byte[] buffer = new byte[size];
            input.read(buffer);
            bitmap = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
            input.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
