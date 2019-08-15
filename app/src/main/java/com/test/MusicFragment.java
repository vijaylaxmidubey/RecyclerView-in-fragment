package com.test;
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

public class MusicFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private RequestQueue queue;
    private ProgressBar pd;

    private ListAdapter adapter;
    private List<Items> listItem = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i("MusicFragment", "onCreate");
        super.onCreate(savedInstanceState);
        queue = Volley.newRequestQueue(getContext());
       }


    @Override
    public void onResume() {
        super.onResume();
        Log.i("MusicFragment", "onResume");
        if(listItem.size() <= 0)
            fetchMusicData();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("MusicFragment", "onCreateView");
        view = inflater.inflate(R.layout.fragment_music, container, false);
        pd = (ProgressBar) view.findViewById(R.id.progress);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        fetchMusicData();

        return view;
    }
    private void fetchMusicData() {
        pd.setVisibility(View.VISIBLE);
        StringRequest req = new StringRequest(Request.Method.GET, GlobalConstant.MUSIC_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("volley", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject iTunes = jsonObject.getJSONObject("iTunes");
                    listItem.clear();
                    JSONArray itune = iTunes.getJSONArray("itune");
                    for (int i = 0; i < itune.length(); i++) {
                        JSONObject item = itune.getJSONObject(i);
                        Items items = new Items();
                        items.setType(item.getString("Type"));
                        items.setSortName(item.getString("ShortName"));
                        items.setTitle(item.getString("Title"));
                        items.setImageUrl(item.getString("ThumbURL"));
                        listItem.add(items);

                    }
                    adapter = new ListAdapter(getActivity(),listItem);
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
                Toast.makeText(getContext(), error.getMessage()+" : "+error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        req.setRetryPolicy(new DefaultRetryPolicy(30000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(req);


    }

}
