package com.test;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class BookFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private RequestQueue queue;
    private ProgressBar pd;

    private ListAdapter adapter;
    private List<Items> listItem = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("BookFragment", "onCreate");
        queue = Volley.newRequestQueue(getContext());

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("BookFragment", "onResume");
        if(listItem.size() <= 0)
            featchBooksData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("BookFragment", "onCreateView");
        view = inflater.inflate(R.layout.fragment_book, container, false);
        pd = (ProgressBar) view.findViewById(R.id.progress);
        recyclerView = (RecyclerView) view
                .findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView
                .setLayoutManager(new LinearLayoutManager(getActivity()));
        featchBooksData();
        return view;
    }

    private void featchBooksData() {
        pd.setVisibility(View.VISIBLE);
        StringRequest req = new StringRequest(Request.Method.GET, GlobalConstant.BOOK_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("volley", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject BookData = jsonObject.getJSONObject("BookData");
                    listItem.clear();
                    JSONArray book = BookData.getJSONArray("book");
                    for (int i = 0; i < book.length(); i++) {
                        JSONObject item = book.getJSONObject(i);
                        Items items = new Items();
                        items.setType(item.getString("Type"));
                        items.setSortName(item.getString("ShortName"));
                        items.setTitle(item.getString("Title"));
                        items.setImageUrl(item.getString("ThumbURL"));
                        listItem.add(items);
                    }

                    adapter = new ListAdapter(getActivity(), listItem);
                    recyclerView.setAdapter(adapter);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
                pd.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        req.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(req);
    }


}
