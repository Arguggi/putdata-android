package com.arguggi.putdata;

import android.app.Fragment;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetFragment extends Fragment {

    public final String URL = "https://putdata.arguggi.co.uk/urls";
    public UrlAdapter mListAdapter;
    public final ArrayList<App.Url> urls = new ArrayList<App.Url>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_get, container, false);

        ListView list = (ListView) rootView.findViewById(R.id.main_url_listview);
        mListAdapter = new UrlAdapter(getActivity(), urls);
        list.setAdapter(mListAdapter);
        updateListStrings();

        list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                App.Url clickedUrl = urls.get(position);
                ClipboardManager clipMan = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("urlString", clickedUrl.url);
                clipMan.setPrimaryClip(clip);
                Toast.makeText(getActivity(), R.string.getCopiedString, Toast.LENGTH_SHORT).show();
            }
        });

        list.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
                String urlHash = urls.get(position).hash;
                deleteString(urlHash);
                return true;
            }
        });


        // Update ListView on swipe
        final SwipeRefreshLayout swipeView = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeView.setRefreshing(true);
                updateListStrings();
                swipeView.setRefreshing(false);
            }
        });

        return rootView;
    }

    public void updateListStrings() {

        urls.clear();
        mListAdapter.notifyDataSetChanged();
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest jsArrRequest = new JsonObjectRequest(Request.Method.GET, URL, "", new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getInt("respCode") == 1) {
                        return;
                    }
                    JSONArray data = response.getJSONArray("urls");

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject oUrl = data.getJSONObject(i);
                        App.Url appUrl = new App.Url();
                        appUrl.hash = oUrl.getString("hash");
                        appUrl.url = oUrl.getString("url");
                        urls.add(appUrl);
                    }
                    mListAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(jsArrRequest);
    }

    public void deleteString(String urlHash) {

        Toast.makeText(getActivity(), R.string.getDeletingString, Toast.LENGTH_SHORT).show();
        final String DELETEURL = "https://putdata.arguggi.co.uk/url/" + urlHash;
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest mStringRequest = new StringRequest(Request.Method.DELETE, DELETEURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getActivity(), R.string.getStringDeleted, Toast.LENGTH_SHORT).show();
                updateListStrings();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), R.string.getErrorDeletingString, Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(mStringRequest);
    }
}
