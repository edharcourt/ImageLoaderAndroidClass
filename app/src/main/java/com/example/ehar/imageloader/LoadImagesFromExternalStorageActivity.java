package com.example.ehar.imageloader;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;

public class LoadImagesFromExternalStorageActivity extends Activity {

    public final static int MY_PERMISSION_CODE = 789;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_images_from_external_storage);

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(
                    new String [] {Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSION_CODE
                    );
        }
        else {
            init();
        }

    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSION_CODE) {
            if (grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                init();
            }
            else {
                Log.w("WARNING:", "Permission was deined");
                // toast
            }
        }
    }

    protected void init() {
        final ArrayList<String> pics = Utility.getPictureFiles();

        Resources res = getResources();

        for (int i = 0; i < 4; i++) {
            int id = res.getIdentifier(
                    "i" + i, "id", getPackageName());

            final int tmp_i = i;
            final ImageButton ib = (ImageButton) findViewById(id);

            ib.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new LoadImageTask(ib).execute(pics.get(tmp_i));

                    /*
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final Bitmap bmp = Utility.decodeSampledBitmapFromFilePath(
                                    pics.get(tmp_i), ib.getWidth(), ib.getHeight()
                            );
                            ib.post(new Runnable() {
                                @Override
                                public void run() {
                                    ib.setImageBitmap(bmp);
                                }
                            });
                        }
                    }).start();
                    */

                }
            });
        }
    }
}
