package com.example.ehar.imageloader;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity extends Activity {

    ImageButton main_image_view = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main_image_view = (ImageButton) findViewById(R.id.main_image);

        main_image_view.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                main_image_view.setVisibility(View.GONE);
            }
        });
    }

    /**
     * Called by the fragment
     * @param d
     */
    void showMainImage(Drawable d) {
        main_image_view.setImageDrawable(d);
        main_image_view.setVisibility(View.VISIBLE);
    }
}














