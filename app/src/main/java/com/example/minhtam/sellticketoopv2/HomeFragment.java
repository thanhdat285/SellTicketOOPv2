package com.example.minhtam.sellticketoopv2;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }

    String token;
    FilmAdapter adapter;
    RecyclerView rcFilm;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //lấy chuỗi token được truyền từ MainActivity sang
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        token = getArguments().getString("token");
        rcFilm = (RecyclerView) view.findViewById(R.id.rcFilm);
        context = getActivity();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new GetFilm().execute("https://tickett.herokuapp.com/api/v1/customers/films","1");
            }
        });
        return view;
    }
    private class GetFilm extends AsyncTask<String,Integer,String> {
        //API web dang nhap
        OkHttpClient okHttpClient = new OkHttpClient();

        @Override
        protected String doInBackground(String... params) {

            String page = params[1];
            String url = params[0]+"?page="+page;
            Request request = new Request.Builder()                     //request len web
                    .url(url)
                    .addHeader("Authorization",token)
                    .build();
            try {
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string(); //chuoi tra lai s o ham onPostExecute
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject body = new JSONObject(s);
                Toast.makeText(getActivity(),s,Toast.LENGTH_LONG).show();
                if(body !=null) {
                    String data = body.getString("data");
                    JSONArray listfilms = body.getJSONArray("data");

                    ArrayList<ItemFilm> items = new ArrayList<ItemFilm>();
                    for (int i = 0; i < listfilms.length(); i++) {
                        String name = listfilms.getJSONObject(i).getString("name").toString();
                        String kind = listfilms.getJSONObject(i).getString("kind").toString();
                        String image = listfilms.getJSONObject(i).getString("image").toString();

                        items.add(new ItemFilm(name, image, kind));
                    }
                    rcFilm.setHasFixedSize(true);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                    rcFilm.setLayoutManager(layoutManager);
                    adapter = new FilmAdapter(getActivity(), items);
                    rcFilm.setAdapter(adapter);
//                Toast.makeText(FilmActivity.this,s,Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
