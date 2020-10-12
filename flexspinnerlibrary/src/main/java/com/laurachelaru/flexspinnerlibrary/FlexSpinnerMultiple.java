package com.laurachelaru.flexspinnerlibrary;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.ColorInt;
import androidx.annotation.Dimension;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FlexSpinnerMultiple extends AppCompatSpinner
        implements DialogInterface.OnCancelListener, FlexAdapterMultiple.MultipleClickListener {
    private static final String TAG = FlexSpinnerMultiple.class.getSimpleName();

    protected String defaultSpinnerText, updatableSpinnerText;
    protected List<FlexItem> items;
    protected FlexListener listener;
    protected int highlightColor, textColor;
    protected FlexAdapterMultiple adapter;
    protected static AlertDialog alertDialog;
    protected float itemPadding;

    public FlexSpinnerMultiple(Context context) {
        super(context);
    }

    public FlexSpinnerMultiple(Context arg0, AttributeSet arg1) {
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

    @Override
    public void onCancel(DialogInterface dialogInterface) {
        updatableSpinnerText = defaultSpinnerText;
        for (FlexItem item: items) {
            if (item.isSelected()) {
                if (updatableSpinnerText.equals(defaultSpinnerText)) {
                    updatableSpinnerText = item.getText();
                } else {
                    updatableSpinnerText = updatableSpinnerText + ", " + item.getText();
                }
            }
        }

        ArrayAdapter<String> textAdapter = new ArrayAdapter<String>(getContext(), R.layout.flex_text_item, new String[] {updatableSpinnerText});
        setAdapter(textAdapter);

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    public void setData(List<FlexItem> items, FlexListener listener) {
        this.items = items;
        this.listener = listener;

        updatableSpinnerText = defaultSpinnerText;

        /*for (FlexItem item: items) {
            if (item.isSelected()) {
                if (updatableSpinnerText.isEmpty()) {
                    updatableSpinnerText = item.getText();
                } else {
                    updatableSpinnerText = updatableSpinnerText + ", " + item.getText();
                }
            }
        }*/

        onCancel(null);
    }

    @Override
    public boolean performClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.RoundedAlertDialog);
        builder.setTitle(defaultSpinnerText);

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View view = inflater.inflate(R.layout.flex_dialog, null);

        View divider = view.findViewById(R.id.divider);
        divider.setBackgroundColor(highlightColor);

        RecyclerView itemList = (RecyclerView) view.findViewById(R.id.item_list);
        itemList.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        itemList.setLayoutManager(layoutManager);
        adapter = new FlexAdapterMultiple(getContext(), items, this, highlightColor, textColor, itemPadding);
        itemList.setAdapter(adapter);
        DividerItemDecorator itemDecoration =
                new DividerItemDecorator(ContextCompat.getDrawable(getContext(), R.drawable.divider));
        itemList.addItemDecoration(itemDecoration);

        builder.setOnCancelListener(this);
        builder.setView(view);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();
                onCancel(alertDialog);
            }
        });

        alertDialog = builder.show();

        final Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(highlightColor);
        LinearLayout.LayoutParams positiveButtonLL = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
        positiveButtonLL.width = ViewGroup.LayoutParams.MATCH_PARENT;
        positiveButton.setLayoutParams(positiveButtonLL);

        return true;
    }

    @Override
    public void onItemClickListener(int position) {
        if (items.get(position).isSelected()) {
            items.get(position).setSelected(false);
        } else {
            items.get(position).setSelected(true);
        }

        listener.onItemSelected(items, position);

        adapter.setNewList(items);
        adapter.notifyDataSetChanged();
    }

    public List<FlexItem> getSelectedItems() {
        List<FlexItem> selectedItems = new ArrayList<>();

        for (FlexItem item: items) {
            if (item.isSelected()) {
                selectedItems.add(item);
            }
        }

        return selectedItems;
    }

    public void setSelectStatusByIntId(Integer intId, boolean select) {
        for (FlexItem item : items) {
            if (item.getIntId().equals(intId)) {
                item.setSelected(select);
            }
        }

        onCancel(null);
    }

    public void setSelectStatusByStringId(String stringId, boolean select) {
        for (FlexItem item : items) {
            if (item.getStringId().equals(stringId)) {
                item.setSelected(select);
            }
        }

        onCancel(null);
    }
}
