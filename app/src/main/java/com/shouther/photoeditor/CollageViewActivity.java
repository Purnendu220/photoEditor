package com.shouther.photoeditor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jcmore2.collage.CollageView;
import com.shouther.photoeditor.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CollageViewActivity extends BaseActivity {
    private Context mContext;
    private Handler handler;
    private Runnable nextScreenRunnable;
    private long DELAY_NAVIGATION = 2400; // 1.4 sec

    public static void open(Context context) {
        Intent intent = new Intent(context, CollageViewActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
    @BindView(R.id.collage)
    CollageView collage;

    @BindView(R.id.button)
    Button button;

    @BindView(R.id.result)
    ImageView result;

    @BindView(R.id.layoutCollage)
    RelativeLayout layoutCollage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collage_view);
        mContext=this;
        ButterKnife.bind(this);
        handler = new Handler();

        createNewCollage();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createNewCollage();
            }
        });




    }

    public void createNewCollage() {

        layoutCollage.removeAllViews();

        CollageView collage = new CollageView(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(10, 10, 10, 10);
        collage.setLayoutParams(params);

        collage.setFixedCollage(false);
        collage.createCollageResources(createFakeList());

        layoutCollage.addView(collage);



        //startTimerForGoToNextScreen();


    }

    private List<Integer> createFakeList() {
        List<Integer> listRes = new ArrayList<Integer>();
        for (int i = 0; i < 3; i++) {
            listRes.add(R.drawable.img1);
            listRes.add(R.drawable.img2);
            listRes.add(R.drawable.img3);
            listRes.add(R.drawable.img4);
        }

        return listRes;
    }
    public Bitmap createBitmapFromView(View v) {
        v.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT));
       v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
               View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
        Bitmap bitmap = Bitmap.createBitmap(v.getMeasuredWidth(),
                v.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);

        Canvas c = new Canvas(bitmap);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);
        return bitmap;
    }

    private void startTimerForGoToNextScreen() {
        nextScreenRunnable = new Runnable() {
            @Override
            public void run() {
                Bitmap bmp=createBitmapFromView(layoutCollage);
                result.setImageBitmap(bmp);
                layoutCollage.setDrawingCacheEnabled(false);

            }
        };

        handler.postDelayed(nextScreenRunnable, DELAY_NAVIGATION);
    }
}
