package com.oesmanalie.it.angkot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oesmanalie.it.angkot.R;
import com.oesmanalie.it.angkot.fragment.SearchingFragment;
import com.oesmanalie.it.angkot.models.Locations;

import java.util.ArrayList;
import java.util.Locale;

public class ListViewAdapter extends BaseAdapter {
    // Declare Variables

    Context mContext;
    LayoutInflater inflater;
    private ArrayList<Locations> arraylist;

    public ListViewAdapter(Context context ) {
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<Locations>();
        this.arraylist.addAll(SearchingFragment.locationsArrayList);
    }

    public class ViewHolder {
        TextView name;
    }

    @Override
    public int getCount() {
        return SearchingFragment.locationsArrayList.size();
    }

    @Override
    public Locations getItem(int position) {
        return SearchingFragment.locationsArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.list_view_item, null);
            holder.name = view.findViewById(R.id.name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.name.setText(SearchingFragment.locationsArrayList.get(position).getLocation());
        return view;
    }
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        SearchingFragment.locationsArrayList.clear();
        if (charText.length() == 0) {
            SearchingFragment.locationsArrayList.addAll(arraylist);
        } else {
            for (Locations wp : arraylist) {
                if (wp.getLocation().toLowerCase(Locale.getDefault()).contains(charText)) {
                    SearchingFragment.locationsArrayList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}