/*
 * ServeStream: A HTTP stream browser/player for Android
 * Copyright 2014 William Seemann
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.sourceforge.servestream.adapter;

import net.sourceforge.servestream.R;
import net.sourceforge.servestream.utils.Utils;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * BaseAdapter for the navigation drawer
 */
public class NavListAdapter extends BaseAdapter {
    public static final int VIEW_TYPE_COUNT = 3;
    public static final int VIEW_TYPE_NAV = 0;
    public static final int VIEW_TYPE_SECTION_DIVIDER = 1;
    public static final int VIEW_TYPE_SUBSCRIPTION = 2;

    public static final int[] NAV_TITLES = {R.string.url_label, R.string.browse_label, R.string.alarm_clock_label};

    public static final int SUBSCRIPTION_OFFSET = 1 + NAV_TITLES.length;

    private Context mContext;

    public NavListAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return NAV_TITLES.length + 1 + 1;//itemAccess.getCount();
    }

    @Override
    public Object getItem(int position) {
        int viewType = getItemViewType(position);
        if (viewType == VIEW_TYPE_NAV) {
            return mContext.getString(NAV_TITLES[position]);
        } else if (viewType == VIEW_TYPE_SECTION_DIVIDER) {
            return "";
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (0 <= position && position < NAV_TITLES.length) {
            return VIEW_TYPE_NAV;
        } else if (position < NAV_TITLES.length + 1) {
            return VIEW_TYPE_SECTION_DIVIDER;
        } else {
            return VIEW_TYPE_SUBSCRIPTION;
        }
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewType = getItemViewType(position);
        View v = null;
        if (viewType == VIEW_TYPE_NAV) {
            v = getNavView((String) getItem(position), position, convertView, parent);
        } else if (viewType == VIEW_TYPE_SECTION_DIVIDER) {
            v = getSectionDividerView((String) getItem(position), position, convertView, parent);
        } else {
            v = getFeedView(position - SUBSCRIPTION_OFFSET, convertView, parent);
        }
        if (v != null) {
            TextView txtvTitle = (TextView) v.findViewById(R.id.txtvTitle);
            if (position == 0) {
                txtvTitle.setTypeface(null, Typeface.BOLD);
            } else {
                txtvTitle.setTypeface(null, Typeface.NORMAL);
            }
        }
        return v;
    }

    private View getNavView(String title, int position, View convertView, ViewGroup parent) {
        NavHolder holder;
        if (convertView == null) {
            holder = new NavHolder();
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.nav_listitem, null);

            holder.title = (TextView) convertView.findViewById(R.id.txtvTitle);
            convertView.setTag(holder);
        } else {
            holder = (NavHolder) convertView.getTag();
        }

        holder.title.setText(title);
        //holder.image.setImageDrawable(drawables[position]);

        return convertView;
    }

    private View getSectionDividerView(String title, int position, View convertView, ViewGroup parent) {
        SectionHolder holder;
        if (convertView == null) {
            holder = new SectionHolder();
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.nav_section_item, null);

            holder.title = (TextView) convertView.findViewById(R.id.txtvTitle);
            convertView.setTag(holder);
        } else {
            holder = (SectionHolder) convertView.getTag();
        }

        holder.title.setText(title);

        convertView.setEnabled(false);
        convertView.setOnClickListener(null);

        return convertView;
    }

    private View getFeedView(int feedPos, View convertView, ViewGroup parent) {
        FeedHolder holder;

        if (convertView == null) {
            holder = new FeedHolder();
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.nav_feedlistitem, null);

            holder.title = (TextView) convertView.findViewById(R.id.txtvTitle);
            holder.image = (ImageView) convertView.findViewById(R.id.imgvCover);
            convertView.setTag(holder);
        } else {
            holder = (FeedHolder) convertView.getTag();
        }

        holder.title.setText("Settings");
        holder.image.setImageResource(Utils.getThemedIcon(mContext, R.attr.ic_action_settings));
        //ImageLoader.getInstance().loadThumbnailBitmap(feed.getImage(), holder.image, (int) context.getResources().getDimension(R.dimen.thumbnail_length_navlist));

        return convertView;
    }
    
    static class NavHolder {
        TextView title;
        ImageView image;
    }

    static class SectionHolder {
        TextView title;
    }

    static class FeedHolder {
        TextView title;
        ImageView image;
    }
}
