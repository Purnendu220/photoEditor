package com.shouther.photoeditor.customDialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.shouther.photoeditor.EditImageActivity;
import com.shouther.photoeditor.R;
import com.shouther.photoeditor.utils.VerticalSlideColorPicker;

import butterknife.BindView;
import butterknife.ButterKnife;

public class createOneColorBitmap extends Dialog implements View.OnClickListener {
    private Context mContext;
    @BindView(R.id.color_picker)
    VerticalSlideColorPicker color_picker;

    @BindView(R.id.selectedColorView)
    View selectedColorView;

    @BindView(R.id.selectedColor)
    TextView selectedColorTextView;

    @BindView(R.id.cancelButton)
    Button cancelButton;

    @BindView(R.id.okButton)
    Button okButton;
    @BindView(R.id.adView)
    AdView mAdView;

    int selectedColorForBitmap;








    public createOneColorBitmap(@NonNull Context context) {
        super(context, R.style.AdvanceDialogTheme);
        this.mContext=context;
        init(context);
    }
    private void init(Context context) {
        setContentView(R.layout.view_bitmap_creater);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ButterKnife.bind(this);
        setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 0.90);
        lp.gravity = Gravity.CENTER;
        getWindow().setAttributes(lp);
        setListeners();
        selectedColorForBitmap=Color.parseColor("#ffffff");
        setUpAdsView();


    }

    private void setUpAdsView() {
        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                // Check the LogCat to get your test device ID
                .addTestDevice("508759325B5A5CE39EF96B111271B496")
                .build();

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Toast.makeText(getApplicationContext(), "Ad is loaded!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdClosed() {
                //Toast.makeText(getApplicationContext(), "Ad is closed!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                //Toast.makeText(getApplicationContext(), "Ad failed to load! error code: " + errorCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLeftApplication() {
                //Toast.makeText(getApplicationContext(), "Ad left application!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }
        });

        mAdView.loadAd(adRequest);
    }

    private void setListeners(){
        cancelButton.setOnClickListener(this);
        okButton.setOnClickListener(this);
        color_picker.setOnColorChangeListener(new VerticalSlideColorPicker.OnColorChangeListener() {
            public void onColorChange(int selectedColor) {
                if (selectedColor != 0) {
                    selectedColorForBitmap=selectedColor;
                    selectedColorView.setBackgroundColor(selectedColor);
                    selectedColorTextView.setText("#"+Integer.toHexString(selectedColor));
                }
            }
        });





    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cancelButton:
                dismiss();

                break;
            case R.id.okButton:
                EditImageActivity.show(mContext,selectedColorForBitmap);
                dismiss();

                break;
        }


    }


}
