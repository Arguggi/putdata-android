package com.arguggi.putdata;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class PutFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_put, container, false);
        final TextView stringText = (TextView) rootView.findViewById(R.id.putClipboardString);
        updateTextView(stringText);

        final Button refreshButton = (Button) rootView.findViewById(R.id.putRefreshButton);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                updateTextView(stringText);
            }
        });

        final Button sendButton = (Button) rootView.findViewById(R.id.putSendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendString(stringText.getText().toString());
            }
        });

        return rootView;
    }


    public void updateTextView(TextView textView) {
        ClipboardManager clipMan = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipMan.getPrimaryClip() != null && clipMan.getPrimaryClip().getItemCount() > 0) {
            textView.setText(clipMan.getPrimaryClip().getItemAt(0).getText());
        }
    }

    public void sendString(final String addString) {

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        final String POSTURL = "https://putdata.arguggi.co.uk/newurl";

        JSONObject obj = new JSONObject();
        try {
            obj.put("newUrl", addString);
        } catch (JSONException e){
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.POST, POSTURL, obj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getActivity(), R.string.putPostSuccess, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), R.string.putPostError, Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsonObjReq);
    }
}
