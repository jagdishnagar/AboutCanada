package org.aboutcanada.com.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import org.aboutcanada.com.Model.Row;
import org.aboutcanada.com.R;

import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewAdapterAdapter extends RecyclerView.Adapter<ViewAdapterAdapter.MyViewHolder> {

    private Context mContext;
    private List<Row> mListCanada;
    //private RequestQueue mImageRequest;


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

    public ViewAdapterAdapter(Context mContext, List<Row> mListCanada) {
        this.mContext = mContext;
        this.mListCanada = mListCanada;

        //mImageRequest = Volley.newRequestQueue(mContext);
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
            holder.textviewTitle.setText(mListCanada.get(position).getTitle());
            holder.textDescription.setText(mListCanada.get(position).getDescription());

            Picasso.with(mContext)
                    .load(mListCanada.get(position).getImageHref())
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