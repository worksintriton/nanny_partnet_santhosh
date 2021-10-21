package com.triton.nannypartners.serviceprovider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.triton.nannypartners.R;
import com.triton.nannypartners.adapter.ServiceTypesListAdapter;
import com.triton.nannypartners.api.APIClient;
import com.triton.nannypartners.api.RestApiInterface;
import com.triton.nannypartners.interfaces.ServicesTypeSelectListener;
import com.triton.nannypartners.requestpojo.ServiceCatRequest;
import com.triton.nannypartners.requestpojo.ServiceProviderRegisterFormCreateRequest;
import com.triton.nannypartners.responsepojo.ServiceCatResponse;
import com.triton.nannypartners.sessionmanager.SessionManager;
import com.triton.nannypartners.utils.ConnectionDetector;
import com.triton.nannypartners.utils.RestUtils;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChooseServiceTypeActivity extends AppCompatActivity implements View.OnClickListener, ServicesTypeSelectListener, Serializable {

    private static final String TAG = "ChooseServiceTypeActivity";

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.avi_indicator)
    AVLoadingIndicatorView avi_indicator;


    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rv_usertype)
    RecyclerView rv_usertype;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_norecords)
    TextView tv_norecords;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.img_back)
    ImageView img_back;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_change)
    Button btn_change;

    private String UserType;
    private int UserTypeValue;
    private String user_id;
    private List<ServiceCatResponse.DataBean> servicetypedataBeanList;
    private int userTypeValue;
    private String ServiceName;
    private String ServiceId;
    private String fromactivity;
    private ArrayList<ServiceProviderRegisterFormCreateRequest> serviceProviderRegisterFormCreateRequestArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_user_type);
        ButterKnife.bind(this);
        avi_indicator.setVisibility(View.GONE);
        img_back.setOnClickListener(this);
        btn_change.setOnClickListener(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            UserType = extras.getString("UserType");
            UserTypeValue = extras.getInt("UserTypeValue");
            fromactivity = extras.getString("fromactivity");
            serviceProviderRegisterFormCreateRequestArrayList = (ArrayList<ServiceProviderRegisterFormCreateRequest>) getIntent().getSerializableExtra("serviceProviderRegisterFormCreateRequestArrayList");
            Log.w(TAG,"serviceProviderRegisterFormCreateRequestArrayList : "+new Gson().toJson(serviceProviderRegisterFormCreateRequestArrayList));

            Log.w(TAG,"fromactivity if : "+fromactivity);

        }

        SessionManager session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getProfileDetails();
        user_id = user.get(SessionManager.KEY_ID);
        userTypeValue = Integer.parseInt(user.get(SessionManager.KEY_TYPE));


        if (new ConnectionDetector(ChooseServiceTypeActivity.this).isNetworkAvailable(ChooseServiceTypeActivity.this)) {
            ServiceCatResponseCall();
        }


    }

    @SuppressLint("LogNotTimber")
    public void ServiceCatResponseCall(){
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        //Creating an object of our api interface
        RestApiInterface apiInterface = APIClient.getClient().create(RestApiInterface.class);
        Call<ServiceCatResponse> call = apiInterface.ServiceCatResponseCall(RestUtils.getContentType(), serviceCatRequest());
        Log.w(TAG,"url  :%s"+ call.request().url().toString());

        call.enqueue(new Callback<ServiceCatResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NonNull Call<ServiceCatResponse> call, @NonNull Response<ServiceCatResponse> response) {
                avi_indicator.smoothToHide();


                if (response.body() != null) {
                    if(200 == response.body().getCode()){
                        Log.w(TAG,"UserTypeListResponse" + new Gson().toJson(response.body()));

                        if(response.body().getData() != null){
                            servicetypedataBeanList = response.body().getData();
                        }


                        if(servicetypedataBeanList != null && servicetypedataBeanList.size()>0){
                            setView();
                        }
                    }



                }








            }


            @Override
            public void onFailure(@NonNull Call<ServiceCatResponse> call,@NonNull  Throwable t) {
                avi_indicator.smoothToHide();
                Log.w(TAG,"ServiceCatResponse flr"+t.getMessage());
            }
        });

    }

    @SuppressLint("LogNotTimber")
    private ServiceCatRequest serviceCatRequest() {
        /*
         * user_id : 5fd778437aa4cc1c6a1e5632
         */
        ServiceCatRequest serviceCatRequest = new ServiceCatRequest();
        serviceCatRequest.setUser_id(user_id);
        Log.w(TAG,"serviceCatRequest"+ new Gson().toJson(serviceCatRequest));
        return serviceCatRequest;
    }

    @SuppressLint("LogNotTimber")
    private void setView() {
       /* for(int i=0; i<servicetypedataBeanList.size();i++){
            if(userTypeValue == usertypedataBeanList.get(i).getUser_type_value()){
                usertypedataBeanList.get(i).setSelected(true);
                break;
            }
        }
        Log.w(TAG, "setView : "+userTypeValue);*/
        rv_usertype.setLayoutManager(new GridLayoutManager(this, 2));
        rv_usertype.setItemAnimator(new DefaultItemAnimator());
        ServiceTypesListAdapter serviceTypesListAdapter = new ServiceTypesListAdapter(getApplicationContext(), servicetypedataBeanList,this,userTypeValue);
        rv_usertype.setAdapter(serviceTypesListAdapter);
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.btn_change:
                if(ServiceName != null && ServiceId != null){
                    gotoChooseSubServiceTypeActivity();
                }

                break;
        }
    }

    private void gotoChooseSubServiceTypeActivity() {
        Intent intent = new Intent(ChooseServiceTypeActivity.this, ChooseSubServiceTypeActivity.class);
        intent.putExtra("ServiceName",ServiceName);
        intent.putExtra("ServiceId",ServiceId);
        intent.putExtra("fromactivity",fromactivity);
        intent.putExtra("serviceProviderRegisterFormCreateRequestArrayList",serviceProviderRegisterFormCreateRequestArrayList);
        Log.w(TAG,"gotoSignup UserType : "+UserType +"UserTypeValue : "+UserTypeValue);
        startActivity(intent);
        finish();
    }

    @Override
    public void servicesTypeSelectListener(String Servicname, String Serviceid) {
        Log.w(TAG,"servicesTypeSelectListener Servicname : "+ Servicname +" Serviceid : "+Serviceid);

        ServiceName =  Servicname;
        ServiceId =  Serviceid;

    }
}