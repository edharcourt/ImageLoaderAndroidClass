package com.example.ehar.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ehar on 10/4/2016.
 */

public class NetworkImageArrayAdapter
        extends ArrayAdapter<String> {

    protected String [] urls = null;
    protected Bitmap placeholder = null;

    public NetworkImageArrayAdapter(Context ctx,
                                    int resource,
                                    String [] urls,
                                    Bitmap placeholder) {
        super(ctx, resource, urls);

        this.urls = urls;
        this.placeholder = placeholder;
    }

    @Override
    public int getCount() {
        return urls.length;
    }

    @Override
    public String getItem(int position) {
        return urls[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*
     * getView is called when position come into the display
     */
    @Override
    public View getView(
            int i,            // array index coming into view
            View reusedView,  // recycled view if not null
            ViewGroup parent  // parent layout file
    ) {

        if (reusedView == null) {
            reusedView = LayoutInflater
                  .from(getContext()).inflate(
                            R.layout.list_item, parent, false);
        }

        // what is true?

        TextView tv = (TextView) reusedView.findViewById(R.id.list_item_text);
        ImageView iv = (ImageView) reusedView.findViewById(R.id.list_item_image);

        tv.setText(getItem(i));
        iv.setImageBitmap(placeholder);
        return reusedView;
    }
}
