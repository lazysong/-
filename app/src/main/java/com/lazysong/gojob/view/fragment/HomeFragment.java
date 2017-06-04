package com.lazysong.gojob.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lazysong.gojob.R;
import com.lazysong.gojob.module.beans.PostInformation;
import com.lazysong.gojob.view.activity.PostInfoDetailActivity;
import com.lazysong.gojob.view.activity.SearchActivity;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HomeFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ActionBar actionBar;
    private RecyclerView recycleViewRemdPost;

    private List<PostInformation> postInfoList;
    private PostInformation postInfo;
    private String userId;

    private OnFragmentInteractionListener mListener;

    TestParaTask testParaTask;
    RequestRecommandTask recommandTask;
    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

        userId = getUserId();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recycleViewRemdPost = (RecyclerView) view.findViewById(R.id.recycleview_remd_post);
        recommandTask = new RequestRecommandTask("1");
        recommandTask.execute();
        testParaTask = new TestParaTask("s");
        testParaTask.execute();
        return view;
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(getActivity());
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

    public String getUserId() {
        //TODO 从本地加载userId
        String userId = "1";
        return userId;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setShowHideAnimationEnabled(false);
        actionBar.show();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.actionbar_home);
        EditText edtSearch = (EditText) actionBar.getCustomView().findViewById(R.id.search);
        edtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });
    }

    class TestParaTask extends AsyncTask<Void, Void, String> {

        private final String BASE_URL = "http://10.50.248.96:8080/GoJob";
        private final String userId;
        private final OkHttpClient client = new OkHttpClient();

        public TestParaTask(String userId) {
            this.userId = userId;
        }
        @Override
        protected String doInBackground(Void... params) {
            String urlStr = BASE_URL + "/a.scaction?requestcode=100";
            Response response;

            RequestBody formBody = new FormBody.Builder()
                    .add("arg_cities", "suzhou, shanghai")
                    .build();

            Request request = new Request.Builder()
                    .url(urlStr)
                    .post(formBody)
                    .build();
            String result = null;
            try {
                response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    result = response.body().string();
                }
                else {
                    throw new IOException("Unexpected code " + response);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "似乎出了点问题", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
        }
    }
    class RequestRecommandTask extends AsyncTask<Void, Void, String> {
        private final String BASE_URL = "http://www.lazysong.cn:8080/Test";
        private final String userId;
        private final OkHttpClient client = new OkHttpClient();

        public RequestRecommandTask(String userId) {
            this.userId = userId;
        }
        @Override
        protected String doInBackground(Void... params) {
            String urlStr = BASE_URL + "/a.scaction?limit=10";
            Request request = new Request.Builder().url(urlStr).build();
            Response response;
            String result = null;
            try {
                response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    result = response.body().string();
                }
                else {
                    throw new IOException("Unexpected code " + response);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "似乎出了点问题", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
            e.printStackTrace();
        }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            Gson gson = new Gson();
            postInfoList = gson.fromJson(result, new TypeToken<List<PostInformation>>(){}.getType());
            recycleViewRemdPost.setAdapter(new RecommandPostInfoAdapter());
            recycleViewRemdPost.setLayoutManager(new LinearLayoutManager(getActivity()));
            super.onPostExecute(result);
        }
    }

    class ViewCache extends RecyclerView.ViewHolder {
        public ImageView imgCompanyLogo;
        public TextView txtPostitionName;
        public TextView txtCompanyName;
        public TextView txtWorkPlace;
        public TextView txtSalary;
        public TextView txtPostTime;
        public TextView txtWatchCount;
        public ImageView imgToDetail;
        public LinearLayout linearayoutPostInfo;

        public ViewCache(View itemView) {
            super(itemView);
            imgCompanyLogo = (ImageView) itemView.findViewById(R.id.imgCompanyLogo);
            txtPostitionName = (TextView) itemView.findViewById(R.id.txtPositionName);
            txtCompanyName = (TextView) itemView.findViewById(R.id.txtCompanyName);
            txtWorkPlace = (TextView) itemView.findViewById(R.id.txtWorkPlace);
            txtSalary = (TextView) itemView.findViewById(R.id.txtSalary);
            txtPostTime = (TextView) itemView.findViewById(R.id.txtPostTime);
            txtWatchCount = (TextView) itemView.findViewById(R.id.txtWatchNum);
            imgToDetail = (ImageView) itemView.findViewById(R.id.imgToDetail);
            linearayoutPostInfo = (LinearLayout) itemView.findViewById(R.id.linearayoutPostInfo);
        }
    }

    class RecommandPostInfoAdapter extends RecyclerView.Adapter<ViewCache> {

        @Override
        public ViewCache onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View itemView = inflater.inflate(R.layout.listview_item_postinfo, parent, false);
            ViewCache viewCache = new ViewCache(itemView);
            return viewCache;
        }

        @Override
        public void onBindViewHolder(ViewCache holder, final int position) {
            final PostInformation postInfo = postInfoList.get(position);
            holder.txtPostitionName.setText(postInfo.getPosition_type());
            holder.txtCompanyName.setText(postInfo.getCompany_name());
            holder.txtWorkPlace.setText(postInfo.getWork_place());
            holder.txtSalary.setText(postInfo.getSalary_month());
            holder.txtPostTime.setText(postInfo.getPost_date().toString());
            holder.txtWatchCount.setText(new Random().nextInt(1000) + "");
            holder.linearayoutPostInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("postId", postInfo.getPost_id());
                    intent.putExtra("userId", userId);//userId默认为1
                    Gson gson = new Gson();
                    intent.putExtra("postInfo", gson.toJson(postInfo));
                    intent.setClass(getActivity(), PostInfoDetailActivity.class);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return postInfoList.size();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        recommandTask.cancel(true);
        testParaTask.cancel(true);
    }
}
