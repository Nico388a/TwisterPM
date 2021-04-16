package com.example.twisterpm;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewSimpleAdapter <T> extends RecyclerView.Adapter<RecyclerViewSimpleAdapter<T>.MyViewHolder>{

    private static final String LOG_TAG = "recycler";
    private final List<T> data;
    //private final LayoutInflater inflater;
    private OnItemClickListener<T> onItemClickListener;
    private final int viewId = View.generateViewId();

    public RecyclerViewSimpleAdapter(/*Context context, */List<T> data) {
        this.data = data;
        //this.inflater = LayoutInflater.from(context);
        Log.d(LOG_TAG, data.toString());
    }

    @NonNull
    @Override
    public RecyclerViewSimpleAdapter<T>.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      /*  Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.recyclerview_simple_row_layout, parent, false); */
        View v = makeView(parent.getContext());
        Log.d(LOG_TAG, v.toString());
        MyViewHolder vh = new MyViewHolder(v);
        Log.d(LOG_TAG, "onCreateViewHolder called");
        return vh;
    }

    private View makeView(Context context) {
        ViewGroup.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        //this.addContentView(layout, params);

        TextView textView = new TextView(context);
        textView.setId(viewId);
        textView.setLayoutParams(params);
        layout.addView(textView);
        return layout;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        T dataItem = data.get(position);
        holder.view.setText(dataItem.toString());
        Log.d(LOG_TAG, "onBindViewHolder called " + position);
    }

    @Override
    public int getItemCount() {
        int count = data.size();
        Log.d(LOG_TAG, "getItemCount called: " + count);
        return count;
    }

    void setOnItemClickListener(OnItemClickListener<T> itemClickListener) {
        this.onItemClickListener = itemClickListener;
    }

    public interface OnItemClickListener<T> {
        void onItemClick(View view, int position, T element);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView view;

        MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            //view = itemView.findViewById(R.id.recyclerViewSimpleRowItem);
            view = itemView.findViewById(viewId);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(view, getAdapterPosition(), data.get(getAdapterPosition()));
            }
        }
    }
}



