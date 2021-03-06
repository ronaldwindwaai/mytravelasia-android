package com.twormobile.mytravelasia.philippines.feed;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.loopj.android.image.SmartImageView;
import com.twormobile.mytravelasia.philippines.R;
import com.twormobile.mytravelasia.philippines.model.Poi;

/**
 * Bridge between POI data and the view.
 *
 * @author avendael
 */
public class PoiCursorAdapter extends CursorAdapter {
    private static final String TAG = PoiCursorAdapter.class.getSimpleName();

    /**
     * Instances of this class are used by Android's Adapter component to cache views as they go in and out of the
     * viewable area. For more info, see Romain Guy's 2009 Google IO talk at
     * <a href="http://www.youtube.com/watch?v=N6YdwzAvwOA">YouTube</a>
     */
    static class ViewHolder {
        SmartImageView ivThumbnail;
        TextView tvTitle;
        TextView tvSubTitle;
        TextView tvLikes;
        TextView tvComments;
        TextView tvPrice;
        TextView tvDistance;
    }

    public PoiCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, final Cursor cursor, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.poi_list_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder();
        final String poiId = cursor.getString(cursor.getColumnIndex(Poi.RESOURCE_ID));

        viewHolder.ivThumbnail = (SmartImageView) view.findViewById(R.id.iv_poi_thumbnail);
        viewHolder.tvTitle = (TextView) view.findViewById(R.id.tv_poi_title);
        viewHolder.tvSubTitle = (TextView) view.findViewById(R.id.tv_poi_subtitle);
        viewHolder.tvLikes = (TextView) view.findViewById(R.id.tv_likes);
        viewHolder.tvComments = (TextView) view.findViewById(R.id.tv_comments);
        viewHolder.tvPrice = (TextView) view.findViewById(R.id.tv_price);
        viewHolder.tvDistance = (TextView) view.findViewById(R.id.tv_distance);

        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        String name = cursor.getString(cursor.getColumnIndex(Poi.NAME));
        String feedName = cursor.getString(cursor.getColumnIndex(Poi.FEED_NAME));
        String address = cursor.getString(cursor.getColumnIndex(Poi.ADDRESS));
        String imgPath = cursor.getString(cursor.getColumnIndex(Poi.IMAGE_THUMB_URL));
        long totalLikes = cursor.getLong(cursor.getColumnIndex(Poi.TOTAL_LIKES));
        long totalComments = cursor.getLong(cursor.getColumnIndex(Poi.TOTAL_COMMENTS));
        String distance = cursor.getString(cursor.getColumnIndex(Poi.DISTANCE));

        String feedContent = cursor.getString(cursor.getColumnIndex(Poi.FB_FULL_FEED_CONTENT));
        String feedAge = cursor.getString(cursor.getColumnIndex(Poi.FB_FEED_AGE));

        viewHolder.ivThumbnail.setImageUrl(imgPath, R.drawable.loading);
        viewHolder.tvTitle.setText(null == name || "".equals(name) ? feedName : name);

        if(feedAge != null && feedAge.equals("") == false) {
            viewHolder.tvSubTitle.setText(feedContent);
            viewHolder.tvDistance.setText(feedAge);
        }
        else {
            viewHolder.tvSubTitle.setText(address);
            viewHolder.tvDistance.setText(null == distance ? "" : distance);
        }

        viewHolder.tvLikes.setText(totalLikes + "");
        viewHolder.tvComments.setText(totalComments + "");
    }

    @Override
    public long getItemId(int position) {
        Cursor cursor = getCursor();

        cursor.moveToPosition(position);

        return cursor.getLong(cursor.getColumnIndex(Poi.RESOURCE_ID));
    }
}
