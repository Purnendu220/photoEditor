package com.shouther.photoeditor;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.shouther.photoeditor.adapter.ActionItemAdapter;
import com.shouther.photoeditor.adapter.AdapterCallbacks;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.shouther.photoeditor.base.BaseActivity;
import com.shouther.photoeditor.customDialogs.createOneColorBitmap;
import com.shouther.photoeditor.data.ActionItemData;
import com.shouther.photoeditor.utils.GridSpacingItemDecoration;
import com.shouther.photoeditor.utils.PathUtil;
import com.shouther.photoeditor.utils.VerticalSpaceItemDecoration;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity implements View.OnClickListener, AdapterCallbacks<ActionItemData> {
    public static void open(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
    private static final int CAMERA_REQUEST = 52;
    private static final int PICK_REQUEST = 53;




    @BindView(R.id.adView)
    AdView mAdView;



    @BindView(R.id.actionItemRecyclerView)
    RecyclerView actionItemRecyclerView;

    public Context mContext;
    private int clickedId=0;

    private ActionItemAdapter mActionItemAdapter;

    private ArrayList actionItemList= new ArrayList<ActionItemData>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mContext=this;
        ButterKnife.bind(this);
        setListeners();
        actionItemRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        actionItemRecyclerView.setHasFixedSize(false);

        try {
            actionItemRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(50));

            ((SimpleItemAnimator) actionItemRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mActionItemAdapter = new ActionItemAdapter(mContext, false, this);
        actionItemRecyclerView.setAdapter(mActionItemAdapter);
        createActionItemList();
        loadAds();

    }

    private void loadAds(){
       //        mAdView.setAdSize(AdSize.LARGE_BANNER);
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
        actionItemList.add(new ActionItemData("Take From Gallery",1,R.mipmap.ic_camera));
        actionItemList.add(new ActionItemData("Take From Camera",2,R.mipmap.ic_camera));
        actionItemList.add(new ActionItemData("Create Empty Image",4,R.mipmap.ic_camera));
        actionItemList.add(new ActionItemData("Recently Edited Images",6,R.mipmap.ic_camera));
        actionItemList.add(new ActionItemData("Rate Our App",5,R.mipmap.ic_camera));
        mActionItemAdapter.addAllData(actionItemList);





    }




    private void setListeners(){

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {



        }

    }
    @Override
    public void isPermissionGranted(boolean isGranted, String permission) {
        if (isGranted) {
        switch (clickedId){
            case 2:
                openCameraIntent();
                break;
            case 1:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_REQUEST);
                break;


        }


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CAMERA_REQUEST:
                    try{
                        EditImageActivity.show(mContext,imageFilePath);
                    }catch (Exception e){
                        e.printStackTrace();
                    }


                    break;
                case PICK_REQUEST:
                    try {
                        Uri uri = data.getData();
                        imageFilePath=PathUtil.getPath(mContext,uri);
                        EditImageActivity.show(mContext,imageFilePath);

                        } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    private void openCameraIntent() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(pictureIntent.resolveActivity(getPackageManager()) != null){
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();

            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,"com.shouther.photoeditor.provider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        photoURI);
                startActivityForResult(pictureIntent,
                        CAMERA_REQUEST);
            }
        }
    }
    String imageFilePath;
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        return image;
    }

    private Bitmap fileToBitmap(){
        try{
            Bitmap rotatedBitmap = null;
            Bitmap bitmap=null;
            File imgFile = new  File(imageFilePath);
            if(imgFile.exists()){
                bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            }
            ExifInterface ei = new ExifInterface(imageFilePath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            switch(orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImage(bitmap, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImage(bitmap, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImage(bitmap, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = bitmap;
            }
            return rotatedBitmap;


        }catch (Exception e){
            e.printStackTrace();
            return null;

        }

    }
    public  Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onAdapterItemClick(RecyclerView.ViewHolder viewHolder, View view, ActionItemData model, int position) {
        switch (model.getItemResourceId()){
            case 1:
                if (requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_REQUEST);
                }
                else{
                    clickedId= 1;
                }
                break;
            case 2:
                if (requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    openCameraIntent();
                }
                else{
                    clickedId=2;
                }
                break;

                case 5:
                //CollageViewActivity.open(mContext);
                    final String appPackageName = getPackageName(); // package name of the app
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                break;
            case 4:
                createOneColorBitmap mCustomThankyouDialog = new createOneColorBitmap(mContext);
                try {
                    mCustomThankyouDialog.show();
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case 6:
                GetAllRecentFiles.show(mContext);
                break;


        }


    }

    @Override
    public void onAdapterItemLongClick(RecyclerView.ViewHolder viewHolder, View view, ActionItemData model, int position) {

    }

    @Override
    public void onShowLastItem() {

    }
}
