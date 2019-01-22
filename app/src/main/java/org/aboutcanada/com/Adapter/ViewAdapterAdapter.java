package org.aboutcanada.com.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.aboutcanada.com.Pojo.CanadaData;
import org.aboutcanada.com.R;
import org.w3c.dom.Text;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

import static java.security.AccessController.getContext;

public class ViewAdapterAdapter extends RecyclerView.Adapter<ViewAdapterAdapter.MyViewHolder> {

    private Context mContext;
    private List<CanadaData> mListCanada;
    private RequestQueue mImageRequest;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageView)
        ImageView imageView;

        @BindView(R.id.TextViewTitle)
        TextView textviewTitle;

        @BindView(R.id.TextViewDescription)
        TextView textDescription;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }

    public ViewAdapterAdapter(Context mContext, List<CanadaData> mListCanada) {
        this.mContext = mContext;
        this.mListCanada = mListCanada;

        mImageRequest = Volley.newRequestQueue(mContext);
    }

    @Override
    public ViewAdapterAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_aboutdesciptio, parent, false);
        return new ViewAdapterAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewAdapterAdapter.MyViewHolder holder, final int position) {
        try {
            holder.textviewTitle.setText(mListCanada.get(position).getmTitle());
            holder.textDescription.setText(mListCanada.get(position).getmDescription());

            Picasso.with(mContext)
                    .load(mListCanada.get(position).getmImageHref())
                    .error(android.R.drawable.stat_notify_error)
                    .into(holder.imageView);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mListCanada.size();
      }
    }