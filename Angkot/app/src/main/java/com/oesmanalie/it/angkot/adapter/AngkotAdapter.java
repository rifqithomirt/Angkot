package com.oesmanalie.it.angkot.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.oesmanalie.it.angkot.R;
import com.oesmanalie.it.angkot.models.AngkotModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AngkotAdapter extends RecyclerView.Adapter<AngkotAdapter.AngkotViewHolder> {
    private List<AngkotModel> listAngkot = new ArrayList<>();
    public AngkotAdapter(List<AngkotModel> listAngkot) {
        this.listAngkot = listAngkot;
    }
    private OnAngkotClickListener listener;

    public interface OnAngkotClickListener {
        public void onClick(View view, int position);
    }

    public void setListener(OnAngkotClickListener listener) {
        this.listener = listener;
    }
    @NonNull
    @Override
    public AngkotViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View vh = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.item_angkot,viewGroup,false);
        AngkotViewHolder viewHolder = new AngkotViewHolder(vh);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AngkotViewHolder angkotViewHolder, int i) {
        AngkotModel item = listAngkot.get(i);
        angkotViewHolder.nama_angkot.setText(item.getNamaAngkot());
        angkotViewHolder.imageAngkot.setImageResource(R.drawable.haltebiru);
//        Picasso.get().load(item.getImageUrl())
//                .placeholder(R.drawable.ic_launcher_background)
//                .into(angkotViewHolder.imageAngkot);

    }

    @Override
    public int getItemCount() {
        return listAngkot.size();
    }

    public class AngkotViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageAngkot;
        public TextView nama_angkot;
        public AngkotViewHolder(@NonNull View itemView) {
            super(itemView);
            imageAngkot = itemView.findViewById(R.id.imageAngkot);
            nama_angkot = itemView.findViewById(R.id.nama_angkot);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(v, getAdapterPosition());
                }
            });
        }
    }
}
