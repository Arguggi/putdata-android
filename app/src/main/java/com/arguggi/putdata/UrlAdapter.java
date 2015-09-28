package com.arguggi.putdata;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class UrlAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    public ArrayList<App.Url> mUrls;

    public UrlAdapter(Context context, ArrayList<App.Url> urls) {
        this.mInflater = LayoutInflater.from(context);
        this.mUrls = urls;
    }

    @Override
    public int getCount() {
        if (mUrls != null && mUrls.size() != 0) {
            return mUrls.size();
        } else {
            return 0;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public App.Url getItem(int position) {
        return mUrls.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        App.Url mUrl = getItem(position);
        ViewHolderCategory holder = new ViewHolderCategory();

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.url_row, parent, false);
            holder.urlId = (TextView) convertView.findViewById(R.id.url_row_url_id);
            holder.urlText = (TextView) convertView.findViewById(R.id.url_row_url_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolderCategory) convertView.getTag();
        }

        holder.urlText.setText(mUrl.url);
        holder.urlId.setText(Integer.toString(mUrl.id));
        return convertView;
    }

    static class ViewHolderCategory {
        public TextView urlText;
        public TextView urlId;
    }
}
