package com.shouther.photoeditor.viewholder;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shouther.photoeditor.R;
import com.shouther.photoeditor.adapter.AdapterCallbacks;
import com.shouther.photoeditor.data.ActionItemData;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActionItemViewHolder extends RecyclerView.ViewHolder {
    private final Context context;

    @BindView(R.id.cardViewItem)
    CardView cardViewItem;

    @BindView(R.id.ImgNewBitmap)
    ImageView ImgNewBitmap;

    @BindView(R.id.textViewItemName)
    TextView textViewItemName;

    public ActionItemViewHolder(View itemView) {
        super(itemView);

        context = itemView.getContext();

        ButterKnife.bind(this, itemView);
    }
    public void bind(final Object data, final AdapterCallbacks adapterCallbacks, final int position) {
        if (data != null && data instanceof ActionItemData) {
            ActionItemData actionData= (ActionItemData) data;
            ImgNewBitmap.setImageResource(actionData.getImageResourceId());
            textViewItemName.setText(actionData.getItemName());
            cardViewItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(ActionItemViewHolder.this, itemView, data, position);
                }
            });

        }


        }


    }
