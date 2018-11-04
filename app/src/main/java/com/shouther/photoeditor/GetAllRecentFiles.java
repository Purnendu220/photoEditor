package com.shouther.photoeditor;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.shouther.photoeditor.adapter.ActionItemAdapter;
import com.shouther.photoeditor.adapter.AdapterCallbacks;
import com.shouther.photoeditor.adapter.RecentFilesAdapter;
import com.shouther.photoeditor.data.ActionItemData;
import com.shouther.photoeditor.utils.GridSpacingItemDecoration;
import com.shouther.photoeditor.utils.VerticalSpaceItemDecoration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GetAllRecentFiles extends AppCompatActivity implements AdapterCallbacks<File> {

    public static void show(Context context) {
        Intent intent = new Intent(context, GetAllRecentFiles.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    @BindView(R.id.textViewNoRecentFiles)
    TextView textViewNoRecentFiles;

    @BindView(R.id.showAllRecentFilesView)
    RecyclerView showAllRecentFilesView;

    @BindView(R.id.adView)
    AdView mAdView;
    private Context mContext;
    private RecentFilesAdapter mRecentFilesAdapter;
    InterstitialAd mInterstitialAd;
    List<File> filesList=new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_all_recent_files);
        mContext=this;
        ButterKnife.bind(this);
        setListeners();
        showAllRecentFilesView.setLayoutManager(new GridLayoutManager(mContext,3));
        showAllRecentFilesView.setHasFixedSize(false);

        try {
            //showAllRecentFilesView.addItemDecoration(new GridSpacingItemDecoration(3,25,true));

            ((SimpleItemAnimator) showAllRecentFilesView.getItemAnimator()).setSupportsChangeAnimations(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mRecentFilesAdapter = new RecentFilesAdapter(mContext, false, this);
        showAllRecentFilesView.setAdapter(mRecentFilesAdapter);
        createActionItemList();
        loadAds();
        loadInterstialAds();
    }
    private void loadInterstialAds(){
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen_test));

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                // Check the LogCat to get your test device ID
                .addTestDevice("508759325B5A5CE39EF96B111271B496")
                .build();

        mInterstitialAd.loadAd(adRequest);

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }

            @Override
            public void onAdClosed() {
                // Toast.makeText(getApplicationContext(), "Ad is closed!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                //Toast.makeText(getApplicationContext(), "Ad failed to load! error code: " + errorCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLeftApplication() {
                // Toast.makeText(getApplicationContext(), "Ad left application!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdOpened() {
                // Toast.makeText(getApplicationContext(), "Ad is opened!", Toast.LENGTH_SHORT).show();
            }
        });


    }
    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    private void loadAds(){
       // mAdView.setAdSize(AdSize.LARGE_BANNER);
//        mAdView.setAdUnitId(getString(R.string.banner_home_footer));

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
    private void createActionItemList(){
        filesList.clear();
        filesList=getAllRecentFilesList();
    if(filesList!=null&&filesList.size()>0){
        mRecentFilesAdapter.clearAll();
        mRecentFilesAdapter.addAllData(filesList);
        mRecentFilesAdapter.notifyDataSetChanged();
    }else{
        textViewNoRecentFiles.setVisibility(View.VISIBLE);

    }





    }

    private List<File> getAllRecentFilesList(){
        List<File> listFile=new ArrayList<>();
        File file = new File(Environment.getExternalStorageDirectory(), "EditedPhotos");
        File[] pictures = file.listFiles();
        if(pictures!=null) {
            for (File item : pictures) {
                listFile.add(item);
            }
        }
       return listFile;
    }




    private void setListeners(){

    }

    @Override
    public void onAdapterItemClick(RecyclerView.ViewHolder viewHolder, View view, File model, int position) {
        GalleryActivity.show(mContext,filesList,position);
        Toast.makeText(getApplicationContext(), "Long press to edit." , Toast.LENGTH_LONG).show();


    }

    @Override
    public void onAdapterItemLongClick(RecyclerView.ViewHolder viewHolder, View view, File model, int position) {
        try{
            EditImageActivity.show(mContext,model.getAbsolutePath());

        }catch (Exception e){

        }

    }

    @Override
    public void onShowLastItem() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        showInterstitial();
        createActionItemList();

    }
}
