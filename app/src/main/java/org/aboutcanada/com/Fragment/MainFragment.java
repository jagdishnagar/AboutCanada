package org.aboutcanada.com.Fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import org.aboutcanada.com.Adapter.ViewAdapterAdapter;
import org.aboutcanada.com.Model.About;
import org.aboutcanada.com.Model.Row;
import org.aboutcanada.com.Network.ApiClient;
import org.aboutcanada.com.Network.WebConnect;
import org.aboutcanada.com.R;
import org.aboutcanada.com.Util.NetworkCheck;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class MainFragment extends Fragment {

    private final String TAG = "Main-Fragment" ;
    @BindView(R.id.recyclerViewAbout)RecyclerView mRecyclerView;
    @BindView(R.id.TextView_ErrorMsg)TextView TextView_ErrorMsg;
    @BindView(R.id.swifeRefresh)SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressDialog progress;
    private RecyclerView.Adapter mAdapter;
    private String MY_PREFS_NAME = "MDataPre";
    private Context mContext;
    private List<Row> mListCanada = new ArrayList<>();
    private String mTitle;


    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        // ButterKnife
        ButterKnife.bind(MainFragment.this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Recycle View Stuff
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        DividerItemDecoration divider = new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.divider));
        mRecyclerView.addItemDecoration(divider);

        // Progres bar Initialization
        progress = new ProgressDialog(getActivity());
        progress.setMessage("Please wait Data Fetching...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCanceledOnTouchOutside(false);

        if(savedInstanceState!=null){

            mListCanada = (List)savedInstanceState.getSerializable("mListCanada");
            mTitle = savedInstanceState.getString("mTitle");
            mSetListDatainAdapter();

        } else {
            //Network Check
            if (NetworkCheck.isOnline(getActivity())) {
                // Progress Bar & Web Data
                if(mListCanada.size()==0) {
                    mWEbProgress();
                }else {
                    mSetListDatainAdapter();
                }
            } else {
                SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                String restoredText = prefs.getString("CachceValCheck", null);
                if (restoredText == null) {
                    TextView_ErrorMsg.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Please check Network", Toast.LENGTH_LONG).show();
                } else {
                    // Progress Bar & Web Data
                    mWEbProgress();
                }
            }
        }

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkCheck.isOnline(getActivity())) {
                    mGetWebData();
                } else {
                    Toast.makeText(getActivity(), "Please check Network", Toast.LENGTH_LONG).show();
                }
            }
        });

}

    private void mSetListDatainAdapter() {
        getActivity().setTitle(mTitle);
        mAdapter = new ViewAdapterAdapter(getActivity().getApplicationContext(), mListCanada);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private void mWEbProgress() {
        progress.show();
        // Calling Webservice
        mGetWebData();
    }

    private void mGetWebData() {

        WebConnect apiService =
                ApiClient.getClient(getActivity()).create(WebConnect.class);

        Call<About> call = apiService.getCanadata();
        call.enqueue(new Callback<About>() {
            @Override
            public void onResponse(Call<About> call, Response<About> response) {
                mListCanada = response.body().getRows();
                mTitle = response.body().getTitle();
                getActivity().setTitle(mTitle);
                if(mListCanada!=null && mListCanada.size()>0) {
                    mSetListDatainAdapter();
                    mSwipeRefreshLayout.setRefreshing(false);

                    SharedPreferences.Editor editor = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putString("CachceValCheck", "Yes");
                    editor.apply();
                }

                if(progress.isShowing())
                    progress.dismiss();
            }

            @Override
            public void onFailure(Call<About> call, Throwable t) {
                Log.e(TAG, t.toString());
                if(progress.isShowing())
                    progress.dismiss();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("mListCanada", (Serializable)mListCanada);
        outState.putString("mTitle",mTitle);
    }
}
