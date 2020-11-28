package com.vijayjangid.cubereum;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class HomeFragment extends Fragment {

    private int pageNoLink = 1;
    ArrayList<ApiData> apiDataList;
    RecyclerView recyclerView;
    Adapter adapter;
    TextView textView, adTextView;
    int currentItems, scrollOutItems, totalItems;
    boolean isScrolling = false, allPagesDone = false, firstPage = true;
    LinearLayoutManager linearLayoutManager;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_home, container, false);


        progressBar = root.findViewById(R.id.progressBar);
        textView = root.findViewById(R.id.errorTextView);
        adTextView = root.findViewById(R.id.advertisement);
        recyclerView = root.findViewById(R.id.recyclerViewHome);
        apiDataList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = linearLayoutManager.getChildCount();
                totalItems = linearLayoutManager.getItemCount();
                scrollOutItems = linearLayoutManager.findFirstVisibleItemPosition();

                if (isScrolling && (currentItems + scrollOutItems == totalItems) && !allPagesDone) {
                    isScrolling = false;
                    getDataFromApi();

                }
            }
        });

        getDataFromApi();

        return root;
    }

    // used to load data in recycler view from the api
    public void getDataFromApi() {

        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://reqres.in/api/users?page=" + pageNoLink,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject objectParent = new JSONObject(response);

                            // getting string to make a format for showing data
                            int page = Integer.parseInt(objectParent.getString("page"));
                            int total_pages = Integer.parseInt(objectParent.getString("total_pages"));
                            int per_page = Integer.parseInt(objectParent.getString("per_page"));
                            if (total_pages == 0) return;
                            if (pageNoLink == total_pages) allPagesDone = true;
                            pageNoLink = pageNoLink + 1;

                            // to set the advertisement textView
                            JSONObject adArray = objectParent.getJSONObject("ad");
                            setAdvertisement(adArray.getString("company"),
                                    adArray.getString("url"), adArray.getString("text"));

                            // getting the data array and object from it now, adding to arrayList also
                            JSONArray dataArray = objectParent.getJSONArray("data");

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject jsonObject = dataArray.getJSONObject(i);

                                ApiData apiData = new ApiData(jsonObject.getString("id"),
                                        jsonObject.getString("email"),
                                        jsonObject.getString("first_name"),
                                        jsonObject.getString("last_name"),
                                        jsonObject.getString("avatar"));

                                apiDataList.add(apiData);

                            }

                            if (firstPage) {
                                firstPage = false;
                                Collections.sort(apiDataList, ApiData.apiDataComparator);
                                adapter = new Adapter(getContext(), apiDataList);
                                recyclerView.setAdapter(adapter);
                            } else {
                                Collections.sort(apiDataList.subList((page-1)*per_page , page*per_page - 1), ApiData.apiDataComparator);
                                adapter.notifyDataSetChanged();
                            }

                            progressBar.setVisibility(View.GONE);

                        } catch (JSONException ignored) {
                            progressBar.setVisibility(View.GONE);
                            textView.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        textView.setVisibility(View.VISIBLE);
                    }
                });

        if(getContext() == null) return;
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    void setAdvertisement(String company, final String url, String text) {
        adTextView.setText(getResources().getString(R.string.advertisement,company,text));
        adTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}