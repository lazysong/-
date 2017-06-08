package com.lazysong.gojob.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lazysong.gojob.R;
import com.lazysong.gojob.controler.RequestCode;
import com.lazysong.gojob.module.LocalInfoManager;
import com.lazysong.gojob.module.beans.MyUser;
import com.lazysong.gojob.module.beans.User;
import com.lazysong.gojob.utils.BitmapLoader;
import com.lazysong.gojob.utils.MarkInfoTask;
import com.lazysong.gojob.utils.Util;
import com.lazysong.gojob.view.activity.CheckUserInfoActivity;
import com.lazysong.gojob.view.activity.FollowActivity;
import com.lazysong.gojob.view.activity.MainActivity;
import com.lazysong.gojob.view.activity.MarkActivity;
import com.lazysong.gojob.view.activity.ResumeActivity;
import com.lazysong.gojob.view.activity.SettingsActivity;
import com.lazysong.gojob.view.activity.WillingsActivity;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AccountFragmentNew.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AccountFragmentNew#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragmentNew extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int LOAD_IMG_WITH_URL = 1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private User user = new User();
    private String userId;
    private boolean isLogined = false;

    private RelativeLayout layoutUserInfo;
    private TextView tvNickname;
    private ImageView userImage;
    private TextView sign;
    private RelativeLayout layoutMark;
    private RelativeLayout layoutFollow;
    private RelativeLayout layoutWillings;
    private RelativeLayout layoutResume;
    private RelativeLayout layoutSettings;

    private MarkInfoTask userInfoTask;
    private MarkInfoTask userExistTask;
    private MarkInfoTask addUserTask;

    private static Tencent mTencent;
    private UserInfo mInfo;
    private Bitmap bitmapQQImg;
    private String qqImagUrl;
    private String qqNickname;

    private LocalInfoManager localInfoManager;

    public AccountFragmentNew() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragmentNew.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragmentNew newInstance(String param1, String param2) {
        AccountFragmentNew fragment = new AccountFragmentNew();
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_account, container, false);
        initViews(view);
        localInfoManager = new LocalInfoManager(getActivity());
        //TODO 查看本地文件，用户是否已经登录
        isLogined = localInfoManager.getLogin();
        if (isLogined) {
            //TODO 从服务器加载用户信息
            userId = localInfoManager.getUserId();
            loadUserInfo(userId);
            Log.v("songhui", "用户已经登录, userId=" + userId);
        }
        else {
            Log.v("songhui", "用户未登录");
        }
        if (mTencent == null)
            mTencent = Tencent.createInstance(MainActivity.appId, getActivity());

        return view;
    }

    @Override
    public void onAttach(Activity context) {
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
    private void initViews(View view) {
        userImage = (ImageView) view.findViewById(R.id.imgUser);
        sign = (TextView) view.findViewById(R.id.tvsign);
        tvNickname = (TextView) view.findViewById(R.id.tvNickname);
        layoutUserInfo = (RelativeLayout) view.findViewById(R.id.layoutUserInfo);
        layoutUserInfo.setOnClickListener(this);
        layoutSettings = (RelativeLayout) view.findViewById(R.id.layoutSettings);
        layoutSettings.setOnClickListener(this);
        layoutMark = (RelativeLayout) view.findViewById(R.id.layoutMark);
        layoutMark.setOnClickListener(this);
        layoutFollow = (RelativeLayout) view.findViewById(R.id.layoutFollow);
        layoutFollow.setOnClickListener(this);
        layoutResume = (RelativeLayout) view.findViewById(R.id.layoutResume);
        layoutResume.setOnClickListener(this);
        layoutWillings = (RelativeLayout) view.findViewById(R.id.layoutWillings);
        layoutWillings.setOnClickListener(this);
    }

    private void loadUserInfo(String userId) {
        List<Map<String, String>> params = new ArrayList<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("name", "USER_ID");
        map.put("value", userId);
        params.add(map);
        userInfoTask = new MarkInfoTask(RequestCode.CAT_USER, params, getActivity());
        userInfoTask.execute();
    }

    public void onResopnse(int requestcode, String result) {
        Log.v("songhui", "onResponse ,requestCode = " + requestcode + " result = " + result);
        if (requestcode == RequestCode.CAT_USER) {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            List<User> userList = gson.fromJson(result, new TypeToken<List<User>>(){}.getType());
            user = userList.get(0);
            tvNickname.setText(user.getNickname());
            sign.setText(user.getSign());
            //TODO 加载用户头像
            BitmapLoader bitmapLoader = new BitmapLoader(user.getImg_name(), userImage);
            bitmapLoader.execute();
            localInfoManager.setLogin(true);
            localInfoManager.setUserId(user.getUser_id());
        }
        else if (requestcode == RequestCode.USER_EXISTS) {
            Log.v("songhui", "userExists: " + " result = " + result + " openId = " + mTencent.getOpenId());
            if (result.equals("USER_EXISTS=true\r\n") || result.equals("USER_EXISTS=true\n")) {
                Log.v("songhui", "用户存在");
                //TODO 加载用户的信息，包括nickname，userId, img
                user.setUser_id(mTencent.getOpenId());
                List<Map<String, String>> params = new ArrayList<>();
                HashMap<String, String> map = new HashMap<>();
                map.put("name", "USER_ID");
                map.put("value", user.getUser_id());
                params.add(map);
                Log.v("songhui", "catuser: userId=" + user.getUser_id());
                userInfoTask = new MarkInfoTask(RequestCode.CAT_USER, params, getActivity());
                userInfoTask.execute();
                isLogined = true;
                localInfoManager.setLogin(true);
                localInfoManager.setUserId(user.getUser_id());
            }
            else if (result.equals("USER_EXISTS=false\r\n") || result.equals("USER_EXISTS=false\n")) {
                //TODO 添加用户，使用qq的nickname，openId，img
                Log.v("songhui", "用户不存在");
                List<Map<String, String>> params = new ArrayList<>();
                HashMap<String, String> map = new HashMap<>();
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                user.setImg_name(qqImagUrl);
                user.setNickname(qqNickname);
                String userInfo = gson.toJson(user);
                map.put("name", "USER_INFO");
                map.put("value", userInfo);
                params.add(map);
                Log.v("songhui", "添加用户 userInfo: " + userInfo);
                addUserTask = new MarkInfoTask(RequestCode.UPLOAD_USER, params, getActivity());
                addUserTask.execute();
            }
            else {
                Log.v("songhui", "未匹配到");
            }
        }
        else if (requestcode == RequestCode.UPLOAD_USER) {
            Log.v("songhui", "upload user: " + " result = " + result + " openId = " + mTencent.getOpenId());
            if (result.equals("Update success\r\n")) {
                //TODO 加载用户的信息，包括nickname， userId，img
                tvNickname.setText(user.getNickname());
                userImage.setImageBitmap(bitmapQQImg);
                //TODO 写入本地文件，更新isLogined
                isLogined = true;
                localInfoManager.setLogin(true);
                localInfoManager.setUserId(user.getUser_id());
            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        final int id = v.getId();
        switch (id) {
            case R.id.layoutUserInfo:
                if(!isLogined)
                    QQLogin();
                else {
                    //跳转到编辑用户信息界面
                    intent.setClass(getActivity(), CheckUserInfoActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                }
                break;
            case R.id.layoutSettings:
                intent.setClass(getActivity(), SettingsActivity.class);
                startActivityForResult(intent, MainActivity.REQUEST_SETTINGS);
                break;
            case R.id.layoutFollow:
                if (!isLogined) {
                    Toast.makeText(getActivity(), "请登录", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent.setClass(getActivity(), FollowActivity.class);
                intent.putExtra("userId", user.getUser_id());
                startActivity(intent);
                break;
            case R.id.layoutMark:
                if (!isLogined) {
                    Toast.makeText(getActivity(), "请登录", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent.setClass(getActivity(), MarkActivity.class);
                intent.putExtra("userId", user.getUser_id());
//                intent.putExtra("userId", "1");
                startActivity(intent);
                break;
            case R.id.layoutResume:
                if (!isLogined) {
                    Toast.makeText(getActivity(), "请登录", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent.setClass(getActivity(), ResumeActivity.class);
                intent.putExtra("userId", user.getUser_id());
                startActivity(intent);
                break;
            case R.id.layoutWillings:
                if (!isLogined) {
                    Toast.makeText(getActivity(), "请登录", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent.setClass(getActivity(), WillingsActivity.class);
                intent.putExtra("userId", user.getUser_id());
                startActivity(intent);
                break;
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onChangeToolbarTitle(int data);
    }

    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            if (null == response) {
                Util.showResultDialog(getActivity(), "返回为空", "登录失败");
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (null != jsonResponse && jsonResponse.length() == 0) {
                Util.showResultDialog(getActivity(), "返回为空", "登录失败");
                return;
            }
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
            if (isLogined) {
                isLogined = false;
            }
        }
    }

    IUiListener loginListener = new BaseUiListener() {
        @Override
        protected void doComplete(JSONObject values) {
            initOpenidAndToken(values);
            loadQQInfo();
        }
    };

    //根据网络请求的返回结果设置OpenId和AccessToken
    public void initOpenidAndToken(JSONObject jsonObject) {
        Log.v("songhui", "initOpenidAndToken() is called");
        try {
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);//根据appid和qq号码生成
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)) {
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void loadQQInfo() {
        Log.v("songhui", "loadQQInfo() is called");
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
                                    qqImagUrl = json.getString("figureurl_qq_2");
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
            mInfo = new UserInfo(getActivity(), mTencent.getQQToken());
            mInfo.getUserInfo(listener);
        }
        else {
            Log.v("songhui", "tecent session is invalid or mTecent == null");
        }
    }


    Handler mHandler = new Handler() {
        private int messageCount = 0;

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                JSONObject response = (JSONObject) msg.obj;
                if (response.has("nickname")) {
                    try {
//                        user.setNickname(response.getString("nickname"));
                        qqNickname = response.getString("nickname");
                        user.setUser_id(mTencent.getOpenId());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                messageCount++;
            } else if (msg.what == 1) {
                bitmapQQImg = (Bitmap) msg.obj;
                messageCount++;
            }
            if(messageCount == 2) {//处理完第二条消息后
                //TODO 将openId作为userId，查询用户是否存在
                List<Map<String, String>> params = new ArrayList<>();
                HashMap<String, String> map = new HashMap<>();
                map.put("name", "USER_ID");
                map.put("value", mTencent.getOpenId());
                params.add(map);
                userExistTask = new MarkInfoTask(RequestCode.USER_EXISTS, params, getActivity());
                userExistTask.execute();
                Log.v("songhui", "handleMessage, user:" + user.getUser_id());
            }
        }
    };

    Handler imgHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == LOAD_IMG_WITH_URL) {
                Bitmap bitmap = Util.getbitmap((String)msg.obj);
                userImage.setImageBitmap(bitmap);
            }
        }
    };
    private void QQLogin() {
        if (!mTencent.isSessionValid()) {
            mTencent.login(this, "all", loginListener);
            isLogined = true;
        } else {
            if (!isLogined) {
                mTencent.login(this, "all", loginListener);
                isLogined = true;
            }
            else {
                mTencent.logout(getActivity());
                isLogined = false;
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
            Toast.makeText(getActivity(), "reslutCode == RESULT_LOGOUT", Toast.LENGTH_SHORT).show();
//            mTencent.logout(getActivity());
            mTencent.logout(null);
            isLogined = false;
            localInfoManager.setLogin(false);
            tvNickname.setText("未登录");
            sign.setText("未设置签名");
            userImage.setImageResource(R.drawable.bilibili);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListener.onChangeToolbarTitle(MainActivity.FRAGMENT_ACCOUNT);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (addUserTask != null)
            addUserTask.cancel(true);
        if (userExistTask != null)
            userExistTask.cancel(true);
        if (userInfoTask != null)
            userInfoTask.cancel(true);
    }
}
