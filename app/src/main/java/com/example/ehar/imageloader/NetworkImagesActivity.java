package com.example.ehar.imageloader;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class NetworkImagesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_images);

        // Does this need to be an AsyncTask?
        Bitmap placeholder =
                BitmapFactory.decodeResource(
                        getResources(), R.drawable.placeholder);

        ArrayAdapter<String> adapter =
            new NetworkImageArrayAdapter(
                this,
                R.layout.list_item,
                Images.imageUrls,
                placeholder);

        ListView lv = (ListView) findViewById(R.id.image_listview);

        lv.setAdapter(adapter);
    }
}
