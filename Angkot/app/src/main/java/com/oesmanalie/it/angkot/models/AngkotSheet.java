package com.oesmanalie.it.angkot.models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oesmanalie.it.angkot.R;

import java.util.ArrayList;
import java.util.List;

public class AngkotSheet extends RecyclerView.Adapter<AngkotSheet.AngkotViewHolder> {
    private List<AngkotModel> listAngkot = new ArrayList<>();
    public AngkotSheet(List<AngkotModel> listAngkot) {
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
                inflate(R.layout.item_sheet,viewGroup,false);
        AngkotViewHolder viewHolder = new AngkotViewHolder(vh);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AngkotViewHolder angkotViewHolder, int i) {
        AngkotModel item = listAngkot.get(i);
        angkotViewHolder.nama_angkot.setText(item.getNamaAngkot());

        if( String.valueOf( item.getNamaAngkot()).contains("Lyn A") )
            angkotViewHolder.imageAngkot.setImageResource(R.mipmap.ic_lyn_a_foreground);
        else if(String.valueOf( item.getNamaAngkot()).contains("Lyn B") )
            angkotViewHolder.imageAngkot.setImageResource(R.mipmap.ic_lyn_b_foreground);
        else if( String.valueOf( item.getNamaAngkot()).contains("Lyn C") )
            angkotViewHolder.imageAngkot.setImageResource(R.mipmap.ic_lyn_c_foreground);
        else if( String.valueOf( item.getNamaAngkot()).contains("Lyn D") )
            angkotViewHolder.imageAngkot.setImageResource(R.mipmap.ic_lyn_d_foreground);
        else if( String.valueOf( item.getNamaAngkot()).contains("Lyn E") )
            angkotViewHolder.imageAngkot.setImageResource(R.mipmap.ic_lyn_e_foreground);
        else if( String.valueOf( item.getNamaAngkot()).contains("HALTE") )
            angkotViewHolder.imageAngkot.setImageResource(R.drawable.haltebiru);
        else
            angkotViewHolder.imageAngkot.setImageResource(R.mipmap.ic_lyn_fg_foreground);
    }

    @Override
    public int getItemCount() {
        return listAngkot.size();
    }

    public class AngkotViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageAngkot;
        public TextView nama_angkot;
        public Button btn;
        public AngkotViewHolder(@NonNull View itemView) {
            super(itemView);
            imageAngkot = itemView.findViewById(R.id.imageAngkot);
            nama_angkot = itemView.findViewById(R.id.nama_angkot);
            btn = itemView.findViewById(R.id.button4);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(v, getAdapterPosition());
                }
            });
        }
    }
}
