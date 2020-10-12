package com.laurachelaru.flexspinnerlibrary;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FlexAdapterMultiple extends RecyclerView.Adapter<FlexAdapterMultiple.ViewHolder> implements Filterable {

    private List<FlexItem> list;
    private List<FlexItem> fullList;
    private LayoutInflater inflater;
    private static MultipleClickListener multipleClickListener;
    private int highlightColor, textColor;
    protected float itemPadding;

    public FlexAdapterMultiple(Context context, List<FlexItem> list, MultipleClickListener clickListener,
                               int highlightColor, int textColor, float itemPadding) {
        this.fullList = list;
        this.list = fullList;
        this.inflater = LayoutInflater.from(context);
        this.multipleClickListener = clickListener;
        this.highlightColor = highlightColor;
        this.textColor = textColor;
        this.itemPadding = itemPadding;
    }

    public interface MultipleClickListener {
        void onItemClickListener(int position);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                List<FlexItem> listFiltered = new ArrayList<>();
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
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

                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CheckBox item;
        MultipleClickListener multipleClickListener;

        public ViewHolder(@NonNull CheckBox itemView, MultipleClickListener multipleClickListener) {
            super(itemView);
            item = itemView;
            this.multipleClickListener = multipleClickListener;
            item.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            multipleClickListener.onItemClickListener(getAdapterPosition());
        }
    }

    public void setNewList(List<FlexItem> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public FlexAdapterMultiple.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CheckBox item = (CheckBox) inflater.inflate(R.layout.flex_multiple_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(item, multipleClickListener);

        return viewHolder;
    }

    public void setChecked(CheckBox item, boolean checked) {
        if (checked) {
            item.setChecked(true);
            item.setTextColor(highlightColor);
            item.setTypeface(null, Typeface.BOLD);
        } else {
            item.setChecked(false);
            item.setTextColor(textColor);
            item.setTypeface(null, Typeface.NORMAL);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull FlexAdapterMultiple.ViewHolder holder, int position) {
        holder.item.setText(list.get(position).getText());
        holder.item.setPadding((int)itemPadding, (int)itemPadding, (int)itemPadding, (int)itemPadding);
        setChecked(holder.item, list.get(position).isSelected());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public List<FlexItem> getCurrentList() {
        return list;
    }
}
