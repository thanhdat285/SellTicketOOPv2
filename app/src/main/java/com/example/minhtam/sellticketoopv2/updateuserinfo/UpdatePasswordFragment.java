package com.example.minhtam.sellticketoopv2.updateuserinfo;


import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.minhtam.sellticketoopv2.ApiUrl;
import com.example.minhtam.sellticketoopv2.MainActivity;
import com.example.minhtam.sellticketoopv2.R;

import java.io.IOException;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class UpdatePasswordFragment extends Fragment {

    String token;

    @SuppressLint("ValidFragment")
    public UpdatePasswordFragment(String token) {
        // Required empty public constructor
        this.token = token;
    }
    EditText edtUpdatePass,edtReUpdatePass;
    Button btnSubmitUpdatePass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_password, container, false);
        findView(view);

        btnSubmitUpdatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = edtUpdatePass.getText().toString();
                String repass = edtReUpdatePass.getText().toString();
                if(pass.equals(repass)){
                    new PutPass().execute(ApiUrl.getUpdateUserPassword(),pass);
                }
                else {
                    Toast.makeText(getActivity(),"Mời xác nhận lại mật khẩu",Toast.LENGTH_SHORT).show();

                }
            }
        });
        return view;
    }
    void findView(View view){
        edtUpdatePass = (EditText) view.findViewById(R.id.edtUpdatePass);
        edtReUpdatePass = (EditText) view.findViewById(R.id.edtReUpdatePass);
        btnSubmitUpdatePass = (Button) view.findViewById(R.id.btnSubmitUpdatePass);
    }
    class PutPass extends AsyncTask<String,Void,String> {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .build();
        @Override
        protected String doInBackground(String... strings) {
            RequestBody requestBody = new MultipartBody.Builder()
                    .addFormDataPart("password",strings[1])
                    .setType(MultipartBody.FORM)
                    .build();
            Request request = new Request.Builder()
                    .url(strings[0])
                    .addHeader("Authorization",token)
                    .put(requestBody)
                    .build();

            try {
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(getActivity(),"Cap nhat pass thành công",Toast.LENGTH_SHORT).show();
            ((MainActivity) getActivity()).moveToHomeFragment();

        }
    }
}