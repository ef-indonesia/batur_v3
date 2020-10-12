package com.laurachelaru.flexspinnerlibrary;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.List;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FlexSpinnerSingleSearch extends AppCompatSpinner implements FlexAdapterSingle.SingleClickListener, DialogInterface.OnCancelListener {

    protected String defaultSpinnerText, updatableSpinnerText;
    protected List<FlexItem> items, allItems;
    protected FlexListener listener;
    protected int highlightColor, textColor;
    protected FlexAdapterSingle adapter;
    protected static AlertDialog alertDialog;
    protected float itemPadding;

    public FlexSpinnerSingleSearch(Context context) {
        super(context);
    }

    public FlexSpinnerSingleSearch(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
        TypedArray array = arg0.obtainStyledAttributes(arg1, R.styleable.FlexSpinner);
        final int index = array.getIndexCount();
        for (int i = 0; i < index; ++i) {
            int attr = array.getIndex(i);
            if (attr == R.styleable.FlexSpinner_hintText) {
                updatableSpinnerText = array.getString(attr);
                defaultSpinnerText = updatableSpinnerText;
            }
            if (attr == R.styleable.FlexSpinner_highlightColor) {
                highlightColor = array.getColor(R.styleable.FlexSpinner_highlightColor, ContextCompat.getColor(arg0, R.color.colorPrimary));
            }
            if (attr == R.styleable.FlexSpinner_textColor) {
                textColor = array.getColor(R.styleable.FlexSpinner_textColor, ContextCompat.getColor(arg0, R.color.colorPrimaryDark));
            }
            if (attr == R.styleable.FlexSpinner_itemPadding) {
                itemPadding = array.getDimension(R.styleable.FlexSpinner_itemPadding, 10);
            }
        }
        //Log.i(TAG, "spinnerTitle: "+ defaultSpinnerText);
        array.recycle();
    }

    public void setHintText(String hintText) {
        updatableSpinnerText = hintText;
        defaultSpinnerText = updatableSpinnerText;
    }

    public void setHighlightColor(int highlightColor) {
        this.highlightColor = highlightColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public void setItemPadding(float itemPadding) {
        this.itemPadding = itemPadding;
    }

    public void setData(List<FlexItem> items, FlexListener listener) {
        this.allItems = items;
        this.items = allItems;
        this.listener = listener;

        updatableSpinnerText = defaultSpinnerText;

        onCancel(null);
    }

    @Override
    public boolean performClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.RoundedAlertDialog);
        builder.setTitle(defaultSpinnerText);

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View view = inflater.inflate(R.layout.flex_search_dialog, null);

        View divider = view.findViewById(R.id.divider);
        divider.setBackgroundColor(highlightColor);

        RecyclerView itemList = (RecyclerView) view.findViewById(R.id.item_list);
        itemList.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        itemList.setLayoutManager(layoutManager);
        adapter = new FlexAdapterSingle(getContext(), allItems, this, highlightColor, textColor, itemPadding);
        itemList.setAdapter(adapter);
        DividerItemDecorator itemDecoration =
                new DividerItemDecorator(ContextCompat.getDrawable(getContext(), R.drawable.divider));
        itemList.addItemDecoration(itemDecoration);

        final SearchView searchView = (SearchView) view.findViewById(R.id.search);
        searchView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                searchView.setIconified(false);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                //items = adapter.getCurrentList();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                //items = adapter.getCurrentList();
                return true;
            }
        });

        builder.setOnCancelListener(this);
        builder.setView(view);
        alertDialog = builder.show();

        return true;
    }

    @Override
    public void onItemClickListener(int position) {
        items = adapter.getCurrentList();
        //Log.d("onItemClickListener size", String.valueOf(items.size()));
        for (FlexItem item : items) {
            item.setSelected(false);
        }

        items.get(position).setSelected(true);
        updateAllItems(items.get(position));

        adapter.setNewList(items);
        adapter.notifyDataSetChanged();

        listener.onItemSelected(items, position);

        alertDialog.dismiss();
        onCancel(alertDialog);
    }

    private void updateAllItems(FlexItem selectedItem) {

        if (selectedItem.getIntId() != null) {
            for (FlexItem item : allItems) {
                if (item.getIntId().equals(selectedItem.getIntId())) {
                    item.setSelected(true);
                } else {
                    item.setSelected(false);
                }
            }
        } else {
            for (FlexItem item : allItems) {
                if (item.getStringId().equals(selectedItem.getStringId())) {
                    item.setSelected(true);
                } else {
                    item.setSelected(false);
                }
            }
        }
    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {
        /*if (adapter != null) {
            items = adapter.getCurrentList();
            //Toast.makeText(getContext(), String.valueOf(items.size()), Toast.LENGTH_SHORT).show();
        }*/

        for (FlexItem item: items) {
            if (item.isSelected()) {
                updatableSpinnerText = item.getText();
               // Log.d("updating text", updatableSpinnerText);
                break;
            }
        }

        ArrayAdapter<String> textAdapter = new ArrayAdapter<String>(getContext(), R.layout.flex_text_item, new String[] {updatableSpinnerText});
        setAdapter(textAdapter);

       // Log.d("updatable text", updatableSpinnerText);

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    public FlexItem getSelectedItem() {
        for (FlexItem item: items) {
            if (item.isSelected()) {
                return item;
            }
        }

        return null;
    }

    public void setSelectStatusByIntId(Integer intId, boolean select) {
        for (FlexItem item : items) {
            if (item.getIntId().equals(intId)) {
                item.setSelected(select);
            } else {
                item.setSelected(false);
            }
        }

        onCancel(null);
    }

    public void setSelectStatusByStringId(String stringId, boolean select) {
        for (FlexItem item : items) {
            if (item.getStringId().equals(stringId)) {
                item.setSelected(select);
            } else {
                item.setSelected(false);
            }
        }

        onCancel(null);
    }
}
