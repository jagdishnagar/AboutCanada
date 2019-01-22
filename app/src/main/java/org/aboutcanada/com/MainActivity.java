package org.aboutcanada.com;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.aboutcanada.com.Adapter.ViewAdapterAdapter;
import org.aboutcanada.com.Pojo.CanadaData;
import org.aboutcanada.com.Util.NetworkCheck;
import org.aboutcanada.com.Util.URL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends Activity {

    private final String TAG = "Main-Activity" ;
    @BindView(R.id.recyclerViewAbout)RecyclerView mRecyclerView;
    @BindView(R.id.TextView_ErrorMsg)TextView TextView_ErrorMsg;
    @BindView(R.id.swifeRefresh)SwipeRefreshLayout mSwipeRefreshLayout;
    private RequestQueue mRequestQueue;
    private ProgressDialog progress;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ButterKnife
        ButterKnife.bind(this);
         // Recycle View Stuff
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        DividerItemDecoration divider = new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.divider));
        mRecyclerView.addItemDecoration(divider);


        //Network Check & Progress Bar
        if(NetworkCheck.isOnline(this)){
            // Progress Bar
            progress=new ProgressDialog(this);
            progress.setMessage("Please wait Data Fetching...");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setCanceledOnTouchOutside(false);
            progress.show();
            // Calling Webservice
            mGetWebData();
        }else {
            TextView_ErrorMsg.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this ,"Please check Network" , Toast.LENGTH_LONG).show();
        }

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(NetworkCheck.isOnline(MainActivity.this)){
                    mGetWebData();
                }else {
                    Toast.makeText(MainActivity.this ,"Please check Network" , Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void mGetWebData() {

        //String Request initialized
        mRequestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL.MAIN_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject Jobject) {

                try {
                    // Setting Value in Action Bar
                    if(Jobject.getString("title")!=null || Jobject.getString("title")!="null")
                    setTitle(Jobject.getString("title"));

                    // Parsing Data from Row
                    JSONArray Jarray = Jobject.getJSONArray("rows");
                    List<CanadaData> mListCanada = new ArrayList<CanadaData>();
                    for(int i=0 ; i<Jarray.length() ; i++){
                        //Pojo Object
                        CanadaData mCData = new CanadaData();
                        //Sequence Data retreving
                        if((Jarray.getJSONObject(i).getString("title")==null) || (Jarray.getJSONObject(i).getString("title")=="null")){
                            mCData.setmTitle("NA");
                        }else {
                            mCData.setmTitle(Jarray.getJSONObject(i).getString("title"));
                        }

                        if((Jarray.getJSONObject(i).getString("description")==null)|| (Jarray.getJSONObject(i).getString("description")=="null")) {
                            mCData.setmDescription("NA");
                        }else {
                            mCData.setmDescription(Jarray.getJSONObject(i).getString("description"));
                        }

                        if((Jarray.getJSONObject(i).getString("imageHref")==null) || (Jarray.getJSONObject(i).getString("imageHref")=="null")) {
                            mCData.setmImageHref("NA");
                        }else {
                            mCData.setmImageHref(Jarray.getJSONObject(i).getString("imageHref"));
                        }

                        // Adding Data in List
                        mListCanada.add(mCData);
                    }
                    if(mListCanada!=null && mListCanada.size()>0) {
                        mAdapter = new ViewAdapterAdapter(getApplicationContext(), mListCanada);
                        mRecyclerView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                progress.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                Log.i(TAG,"Error :" + error.toString());
            }
        });
        mRequestQueue.add(jsonObjectRequest);
    }
}
