package com.example.ehar.imageloader;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;

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

        ViewHolder vh = null;
        if (reusedView == null) {
            reusedView = LayoutInflater
                  .from(getContext()).inflate(
                            R.layout.list_item, parent, false);
            TextView tv = (TextView) reusedView.findViewById(R.id.list_item_text);
            ImageView iv = (ImageView) reusedView.findViewById(R.id.list_item_image);
            vh = new ViewHolder(tv, iv);
            reusedView.setTag(vh); // attach viewholder to view
        }
        else {
            vh = (ViewHolder) reusedView.getTag();
        }

        vh.tv.setText(getItem(i));


        if (cancelPotentialWork(getItem(i), vh.iv)) {
            DownloadBitmapTask task = new DownloadBitmapTask(vh);
            AsyncDrawable asyncDrawable =
                    new AsyncDrawable(getContext().getResources(),
                                      placeholder, task);
            vh.iv.setImageDrawable(asyncDrawable);
            task.execute(getItem(i));
        }

        return reusedView;
    }


    // static classes do not use instance
    // data of the outer class
    private static class ViewHolder {
        TextView tv;
        ImageView iv;
        ViewHolder(TextView tv, ImageView iv) {
            this.tv = tv;
            this.iv = iv;
        }
    }

    // Make an AsyncTask to download image from network
    class DownloadBitmapTask extends AsyncTask<String, Void, Bitmap> {

        ViewHolder vh = null;
        String url = null;

        DownloadBitmapTask(ViewHolder vh) {
            this.vh = vh;
        }

        @Override
        protected Bitmap doInBackground(String... url) {
            this.url = url[0];
            // TODO do not hard code width and height
            return Utility.downloadBitmap(url[0], 120, 120);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            if (isCancelled())
                bitmap = null; // ????

            if (bitmap != null) {
                DownloadBitmapTask task = getBitmapWorkerTask(vh.iv);
                if (this == task) {
                    vh.iv.setImageBitmap(bitmap);
                    vh.tv.setText(url);
                }
            }
        }
    }

    /**
     * https://developer.android.com/training/displaying-bitmaps/process-bitmap.html
     */
    static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<DownloadBitmapTask> bitmapWorkerTaskReference;

        public AsyncDrawable(Resources res, Bitmap bitmap,
                             DownloadBitmapTask bitmapWorkerTask) {
            super(res, bitmap);
            bitmapWorkerTaskReference =
                    new WeakReference<>(bitmapWorkerTask);
        }

        public DownloadBitmapTask getBitmapWorkerTask() {
            return bitmapWorkerTaskReference.get();
        }
    }

    private static DownloadBitmapTask getBitmapWorkerTask(
            ImageView imageView) {

        if (imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }

    /*
     *
     */
    public static boolean cancelPotentialWork(
            String url, ImageView imageView) {

        final DownloadBitmapTask bitmapWorkerTask = getBitmapWorkerTask(imageView);

        if (bitmapWorkerTask != null) {
            final String bitmapUrl = bitmapWorkerTask.url;
            // If bitmapData is not yet set or it differs from the new data
            if (bitmapUrl == null || !bitmapUrl.equals(url)) {
                // Cancel previous task
                bitmapWorkerTask.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was cancelled
        return true;
    }
}
