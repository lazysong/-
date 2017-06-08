package com.lazysong.gojob.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lazysong.gojob.R;
import com.lazysong.gojob.module.beans.PostInformation;
import com.lazysong.gojob.view.activity.MainActivity;
import com.lazysong.gojob.view.activity.PostInfoDetailActivity;
import com.lazysong.gojob.view.activity.SelectTagActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DiscoverFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private TagContainerLayout tagGroup;
    private RecyclerView recyclerView;
    private Button btnToSelectTag;

    private List<PostInformation> postInfoList;
    private ArrayList<String> listPlaceSelected = new ArrayList<>();
    private ArrayList<String> listIndustrySelected = new ArrayList<>();

    DiscoverPostInfoTask discoverTask;
    public static final int REQUEST_SELECT_TAG  = 1;

    public DiscoverFragment() {
    }

    public static DiscoverFragment newInstance(String param1, String param2) {
        DiscoverFragment fragment = new DiscoverFragment();
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
        View view = inflater.inflate(R.layout.fragment_discover, container, false);
        tagGroup = (TagContainerLayout) view.findViewById(R.id.tag_group);
        tagGroup.setTags(new String[]{"北京", "上海", "软件开发"});
        List<String> tagStrs = tagGroup.getTags();
        TagView tagView;
        for (int i = 0; i < tagStrs.size(); i++) {
            tagView = tagGroup.getTagView(i);
            tagView.setTagTextColor(Color.WHITE);
            tagView.setTagBackgroundColor(SelectTagActivity.COLOR_PRIMARY);
        }
        listPlaceSelected.add(0, "北京");
        listPlaceSelected.add(1, "上海");
        listIndustrySelected.add(0, "软件开发");
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleviewDiscover);
        btnToSelectTag = (Button) view.findViewById(R.id.btnSelectTag);
        btnToSelectTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), SelectTagActivity.class);
                startActivityForResult(intent, REQUEST_SELECT_TAG);
            }
        });
        discoverTask = new DiscoverPostInfoTask(listPlaceSelected, listIndustrySelected);
        discoverTask.execute();
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) activity;
        } else {
            throw new RuntimeException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onChangeToolbarTitle(int data);
    }

    class DiscoverPostInfoTask extends AsyncTask<Void, Void, String> {
        private final String BASE_URL = "http://www.lazysong.cn:8080/GoJob";
//        private final String BASE_URL = "http://192.168.0.104:8080/GoJob";
        private final OkHttpClient client = new OkHttpClient();
        private ArrayList<String> listPlaceSelected;
        private ArrayList<String> listIndustrySelected;

        public DiscoverPostInfoTask(ArrayList<String> listPlaceSelected, ArrayList<String> listIndustrySelected) {
            this.listPlaceSelected = listPlaceSelected;
            this.listIndustrySelected = listIndustrySelected;
        }

        public void setListIndustrySelected(ArrayList<String> listIndustrySelected) {
            this.listIndustrySelected = listIndustrySelected;
        }

        public void setListPlaceSelected(ArrayList<String> listPlaceSelected) {
            this.listPlaceSelected = listPlaceSelected;
        }

        @Override
        protected String doInBackground(Void... params) {
            //TODO 根据listPlaceSelected和listIndustrySelected加载数据
            String urlStr = BASE_URL + "/a.scaction?requestcode=25&PLACE_NAME=苏州&limit=5";
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
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            postInfoList = gson.fromJson(result, new TypeToken<List<PostInformation>>(){}.getType());
            recyclerView.setAdapter(new PostInfoAdapter());
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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

    class PostInfoAdapter extends RecyclerView.Adapter<ViewCache> {

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        mListener.onChangeToolbarTitle(MainActivity.FRAGMENT_DISCOVER);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == SelectTagActivity.RESULT_OK) {
            listPlaceSelected = data.getStringArrayListExtra("listPlaceSelected");
            listIndustrySelected = data.getStringArrayListExtra("listIndustrySelected");
            Toast.makeText(getActivity(),
                    "listPlaceSelected size " + listPlaceSelected.size()
                            + " listIndustrySelected size " + listIndustrySelected.size(),
                    Toast.LENGTH_SHORT).show();
            //TODO 重新载入Listview中的数据
            //TODO 重新设置discoverTask的参宿，并执行discoverTask
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        discoverTask.cancel(true);
    }
}
