package com.shouther.photoeditor.viewholder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.shouther.photoeditor.R;
import com.shouther.photoeditor.adapter.AdapterCallbacks;
import com.shouther.photoeditor.data.ActionItemData;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecentFilesViewHolder extends RecyclerView.ViewHolder {
    private final Context context;

    @BindView(R.id.imageViewProfile)
    ImageView imageViewProfile;

    public RecentFilesViewHolder(@NonNull View itemView) {
        super(itemView);
        context = itemView.getContext();

        ButterKnife.bind(this, itemView);
    }

    public void bind(final Object data, final AdapterCallbacks adapterCallbacks, final int position) {
        if (data != null && data instanceof File) {
            File actionData= (File) data;
            if(actionData.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(actionData.getAbsolutePath());
                imageViewProfile.setImageBitmap(myBitmap);
                }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterCallbacks.onAdapterItemClick(RecentFilesViewHolder.this, itemView, data, position);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    adapterCallbacks.onAdapterItemLongClick(RecentFilesViewHolder.this, itemView, data, position);
                    return true;
                }
            });

        }
    }
}
