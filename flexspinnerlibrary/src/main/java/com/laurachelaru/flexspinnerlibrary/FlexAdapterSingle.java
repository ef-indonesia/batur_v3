package com.laurachelaru.flexspinnerlibrary;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FlexAdapterSingle extends RecyclerView.Adapter<FlexAdapterSingle.ViewHolder> implements Filterable {

    private List<FlexItem> list;
    private List<FlexItem> fullList;
    private LayoutInflater inflater;
    private static SingleClickListener singleClickListener;
    private int highlightColor, textColor;
    private float itemPadding;

    public FlexAdapterSingle(Context context, List<FlexItem> list, SingleClickListener clickListener,
                             int highlightColor, int textColor, float itemPadding) {
        int i = 0;
        for (FlexItem item: list) {
            if(i > 0) {
                item.setSelected(false);
            } else {
                if (item.isSelected()) {
                    i++;
                }
            }
        }

        this.fullList = list;
        this.list = fullList;
        this.inflater = LayoutInflater.from(context);
        this.singleClickListener = clickListener;
        this.highlightColor = highlightColor;
        this.textColor = textColor;
        this.itemPadding = itemPadding;


    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView item;
        SingleClickListener singleClickListener;

        public ViewHolder(@NonNull TextView itemView, SingleClickListener singleClickListener) {
            super(itemView);
            item = itemView;
            this.singleClickListener = singleClickListener;
            item.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            singleClickListener.onItemClickListener(getAdapterPosition());
        }
    }

    public void setNewList(List<FlexItem> list) {
        this.fullList = list;
        this.list = fullList;
    }

    public List<FlexItem> getCurrentList() {
        return list;
    }

    @NonNull
    @Override
    public FlexAdapterSingle.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView item = (TextView) inflater.inflate(R.layout.flex_single_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(item, singleClickListener);

        return viewHolder;
    }

    public void setChecked(TextView item, boolean checked) {
        if (checked) {
            item.setTextColor(highlightColor);
            item.setTypeface(null, Typeface.BOLD);
        } else {
            item.setTextColor(textColor);
            item.setTypeface(null, Typeface.NORMAL);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final FlexAdapterSingle.ViewHolder holder, final int position) {
        holder.item.setText(list.get(position).getText());
        holder.item.setPadding((int)itemPadding, (int)itemPadding, (int)itemPadding, (int)itemPadding);
        setChecked(holder.item, list.get(position).isSelected());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface SingleClickListener {
        void onItemClickListener(int position);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                List<FlexItem> listFiltered;
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    Log.d("query", "empty");
                    listFiltered = fullList;
                } else {
                    listFiltered = new ArrayList<>();

                    for (FlexItem item : fullList) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (item.getText().toLowerCase().contains(charString.toLowerCase())) {
                            listFiltered.add(item);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                list = (ArrayList<FlexItem>) filterResults.values;
                Log.d("Filtered list size", String.valueOf(list.size()));

                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }
}
