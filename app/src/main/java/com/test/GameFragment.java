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

public class GameFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private RequestQueue queue;
    private ProgressBar pd;
    private ListAdapter adapter;
    private List<Items> listItem = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queue = Volley.newRequestQueue(getContext());
        Log.i("GameFragment", "onCreate");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_game, container, false);
        pd = (ProgressBar) view.findViewById(R.id.progress);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Log.i("GameFragment", "onCreateView");
        fetchGamesData();
        return view;
    }
    private void fetchGamesData() {
        pd.setVisibility(View.VISIBLE);
        StringRequest req = new StringRequest(Request.Method.GET, GlobalConstant.GAMES_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("volley", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject GamesData = jsonObject.getJSONObject("GamesData");
                    listItem.clear();
                    JSONArray game = GamesData.getJSONArray("game");
                    for (int i = 0; i < game.length(); i++) {
                        JSONObject item = game.getJSONObject(i);
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
                pd.setVisibility(View.GONE);
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        req.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(req);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("GameFragment", "onResume");
        if(listItem.size() <= 0)
            fetchGamesData();
    }
}
