package com.shouther.photoeditor;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import com.jcmore2.collage.CollageView;
import com.shouther.photoeditor.base.BaseActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CollageViewActivity extends BaseActivity {
    private Context mContext;

    public static void open(Context context) {
        Intent intent = new Intent(context, CollageViewActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
    @BindView(R.id.collage)
    CollageView collage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collage_view);
        mContext=this;
        ButterKnife.bind(this);
        List<Integer> listRes = new ArrayList<Integer>();
        listRes.add(R.drawable.img1);
        listRes.add(R.drawable.img2);
        listRes.add(R.drawable.img3);
        listRes.add(R.drawable.img4);

        collage.createCollageResources(listRes);
        collage.setFixedCollage(false);
//        collage.buildDrawingCache();
//        Bitmap bmp = Bitmap.createBitmap(collage.getDrawingCache());
//        Uri tempUri = getImageUri(getApplicationContext(), bmp);
//
//        // CALL THIS METHOD TO GET THE ACTUAL PATH
//        File finalFile = new File(getRealPathFromURI(tempUri));
//        System.out.print(finalFile.getAbsolutePath().toString());

    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
}
