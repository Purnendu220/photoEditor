package com.shouther.photoeditor;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.shouther.photoeditor.utils.ViewPagerFragmentSelection;


import butterknife.BindView;
import butterknife.ButterKnife;

public class MediaGalleryFragment extends Fragment implements ViewPagerFragmentSelection {

    @BindView(R.id.imageViewImage)
    ImageView imageViewImage;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.textViewEdit)
    TextView textViewEdit;

    String uri;
    private Context mContext;


    public MediaGalleryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_media_gallery, container, false);
        mContext=getContext();
        ButterKnife.bind(this, view);
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                EditImageActivity.show(mContext,uri);

                return true;
            }

        });

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

         uri = getArguments().getString("uri", "");

        if (uri == null && !uri.isEmpty()) {
            return;
        }

        progressBar.setVisibility(View.GONE);
        Bitmap myBitmap = BitmapFactory.decodeFile(uri);
        imageViewImage.setImageBitmap(myBitmap);
        imageViewImage.setOnTouchListener(new ImageMatrixTouchHandler(getContext()));


    }


    @Override
    public void onTabSelection(int position) {

    }

}
