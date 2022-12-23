package com.example.android_baitap4;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OfficialAdapter extends RecyclerView.Adapter<OfficialAdapter.OfficialViewHolder>{

    private ArrayList<Official> officialList;
    private IOfficialClickHandle mListener;

    public OfficialAdapter(ArrayList<Official> officialList, IOfficialClickHandle mListener) {
        this.officialList = officialList;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public OfficialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.official_item,parent,false);
        return new OfficialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfficialViewHolder holder, int position) {

        Official official=officialList.get(position);
        holder.officies.setText(official.getTitle());
        holder.officialName.setText(official.getName());
        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onOfficialClick(official);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(officialList!=null&&officialList.size()!=0)
            return officialList.size();
        return 0;
    }

    public class OfficialViewHolder extends RecyclerView.ViewHolder{

        private TextView officies,officialName;
        private LinearLayout mLayout;
        public OfficialViewHolder(@NonNull View itemView) {
            super(itemView);
            officies=itemView.findViewById(R.id.officies);
            officialName=itemView.findViewById(R.id.officialName);
            mLayout=itemView.findViewById(R.id.officialItem_layout);
        }
    }
}
