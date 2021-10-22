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
import com.triton.nannypartners.activity.LoginActivity;
import com.triton.nannypartners.activity.SignUpActivity;
import com.triton.nannypartners.adapter.ServiceTypesListAdapter;
import com.triton.nannypartners.adapter.SubServiceTypesListAdapter;
import com.triton.nannypartners.api.APIClient;
import com.triton.nannypartners.api.RestApiInterface;
import com.triton.nannypartners.interfaces.SPSubServiceCheckedListener;
import com.triton.nannypartners.interfaces.ServicesTypeSelectListener;
import com.triton.nannypartners.requestpojo.ServiceCatRequest;
import com.triton.nannypartners.requestpojo.SubServiceCatRequest;
import com.triton.nannypartners.requestpojo.SubServiceUpdateRequest;
import com.triton.nannypartners.responsepojo.ServiceCatResponse;
import com.triton.nannypartners.responsepojo.SubServiceCatResponse;
import com.triton.nannypartners.responsepojo.SuccessResponse;
import com.triton.nannypartners.sessionmanager.SessionManager;
import com.triton.nannypartners.utils.ConnectionDetector;
import com.triton.nannypartners.utils.RestUtils;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChooseSubServiceTypeActivity extends AppCompatActivity implements View.OnClickListener, SPSubServiceCheckedListener {

    private static final String TAG = "ChooseSubServiceTypeActivity";

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
    @BindView(R.id.txt_selected_service)
    TextView txt_selected_service;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.img_back)
    ImageView img_back;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_change)
    Button btn_change;

    private String UserType;
    private int UserTypeValue;
    private String user_id;
    private int userTypeValue;
    private String ServiceName;
    private String ServiceId;
    private List<SubServiceCatResponse.DataBean> subservicetypedataBeanList;
    List<SubServiceUpdateRequest.SubServiceDataBean> sub_service_data = new ArrayList<>();
    private String fromactivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_service_user_type);
        ButterKnife.bind(this);

        avi_indicator.setVisibility(View.GONE);
        img_back.setOnClickListener(this);
        btn_change.setOnClickListener(this);
        Bundle extras = getIntent().getExtras();


        if (extras != null) {
            ServiceName = extras.getString("ServiceName");
            ServiceId = extras.getString("ServiceId");
            fromactivity = extras.getString("fromactivity");
            Log.w(TAG,"ServiceName : "+ServiceName +"ServiceId : "+ServiceId+" fromactivity : "+fromactivity);

            if(ServiceName != null && !ServiceName.isEmpty()){
                 txt_selected_service.setText(ServiceName);
             }else{
                txt_selected_service.setText("");
            }
        }

        SessionManager session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getProfileDetails();
        user_id = user.get(SessionManager.KEY_ID);
        userTypeValue = Integer.parseInt(user.get(SessionManager.KEY_TYPE));
        if(ServiceName != null && ServiceId != null) {
            if (new ConnectionDetector(ChooseSubServiceTypeActivity.this).isNetworkAvailable(ChooseSubServiceTypeActivity.this)) {
                SubServiceCatResponseCall();
            }
        }


    }

    @SuppressLint("LogNotTimber")
    public void SubServiceCatResponseCall(){
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        //Creating an object of our api interface
        RestApiInterface apiInterface = APIClient.getClient().create(RestApiInterface.class);
        Call<SubServiceCatResponse> call = apiInterface.SubServiceCatResponseCall(RestUtils.getContentType(), subServiceCatRequest());
        Log.w(TAG,"url  :%s"+ call.request().url().toString());

        call.enqueue(new Callback<SubServiceCatResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NonNull Call<SubServiceCatResponse> call, @NonNull Response<SubServiceCatResponse> response) {
                avi_indicator.smoothToHide();


                if (response.body() != null) {
                    if(200 == response.body().getCode()){
                        Log.w(TAG,"SubServiceCatResponse" + new Gson().toJson(response.body()));

                        if(response.body().getData() != null){
                            subservicetypedataBeanList = response.body().getData();
                        }


                        if(subservicetypedataBeanList != null && subservicetypedataBeanList.size()>0){
                            for(int i=0;i<subservicetypedataBeanList.size();i++) {
                                SubServiceUpdateRequest.SubServiceDataBean subServiceDataBean = new SubServiceUpdateRequest.SubServiceDataBean();
                                subServiceDataBean.set_id(subservicetypedataBeanList.get(i).get_id());
                                subServiceDataBean.setService_id(subservicetypedataBeanList.get(i).getService_id());
                                subServiceDataBean.setTitle(subservicetypedataBeanList.get(i).getTitle());
                                subServiceDataBean.setIsservice(subservicetypedataBeanList.get(i).isIsservice());
                                sub_service_data.add(subServiceDataBean);
                            }

                            Log.w(TAG,"sub_service_data" + new Gson().toJson(sub_service_data));
                            rv_usertype.setVisibility(View.VISIBLE);
                            tv_norecords.setVisibility(View.GONE);
                            setView();
                        }else{
                            rv_usertype.setVisibility(View.GONE);
                            tv_norecords.setVisibility(View.VISIBLE);
                            tv_norecords.setText("No sub service");

                        }
                    }



                }

            }

            @Override
            public void onFailure(@NonNull Call<SubServiceCatResponse> call,@NonNull  Throwable t) {
                avi_indicator.smoothToHide();
                Log.w(TAG,"SubServiceCatResponse flr"+t.getMessage());
            }
        });

    }
    @SuppressLint("LogNotTimber")
    private SubServiceCatRequest subServiceCatRequest() {
        /*
         * service_id : 5fd778437aa4cc1c6a1e5632
         * user_id
         */
        SubServiceCatRequest subServiceCatRequest = new SubServiceCatRequest();
        subServiceCatRequest.setService_id(ServiceId);
        subServiceCatRequest.setUser_id(user_id);
        Log.w(TAG,"subServiceCatRequest"+ new Gson().toJson(subServiceCatRequest));
        return subServiceCatRequest;
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
        SubServiceTypesListAdapter subServiceTypesListAdapter = new SubServiceTypesListAdapter(getApplicationContext(), subservicetypedataBeanList,this,ServiceName);
        rv_usertype.setAdapter(subServiceTypesListAdapter);
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(fromactivity != null && fromactivity.equalsIgnoreCase("ServiceDetailsAdapter")){
            startActivity(new Intent(getApplicationContext(), ServiceProviderRegisterFormActivity.class));
            finish();
        }else if(fromactivity != null && fromactivity.equalsIgnoreCase("ServiceDetailsEditAdapter")){
            startActivity(new Intent(getApplicationContext(), ServiceProviderEditFormActivity.class));
            finish();
        }else {
            startActivity(new Intent(getApplicationContext(), ChooseServiceTypeActivity.class));
            finish();
        }

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.btn_change:
                SubServiceUpdateResponseCall();

                break;
        }
    }

    @SuppressLint("LogNotTimber")
    private void gotoSignup() {
        Intent intent = new Intent(ChooseSubServiceTypeActivity.this, SignUpActivity.class);
        intent.putExtra("UserType",UserType);
        intent.putExtra("UserTypeValue",UserTypeValue);
        Log.w(TAG,"gotoSignup UserType : "+UserType +"UserTypeValue : "+UserTypeValue);
        startActivity(intent);
        finish();
    }


    @SuppressLint("LogNotTimber")
    @Override
    public void onItemSPSubServiceCheck(int position, String subservicename, boolean isChbxChecked, String servicename, String id) {
        Log.w(TAG,"onItemSPSubServiceCheck position : "+position +" subservicename : "+subservicename+" isChbxChecked "+isChbxChecked+" servicename "+servicename+" id : "+id+" ServiceId : "+ServiceId);

        if(sub_service_data != null && sub_service_data.size()>0){
            for(int i=0; i<sub_service_data.size();i++){
                if(subservicename.equalsIgnoreCase(sub_service_data.get(i).getTitle())){
                    sub_service_data.get(i).setIsservice(true);
                    break;
                }
            }
        }



        Log.w(TAG, "after adding list if subservicename" + new Gson().toJson(sub_service_data));


    }

    @SuppressLint("LogNotTimber")
    @Override
    public void onItemSPSubServiceUnCheck(int position, String subservicename, boolean isChbxChecked, String servicename, String id) {
        Log.w(TAG, "onItemSPSubServiceUnCheck position : " + position + " subservicename : " + subservicename + " isChbxChecked " + isChbxChecked + " servicename " + servicename + " id : " + id + " ServiceId  : " + ServiceId);
      /*  if (subservicename != null) {
            if (sub_service_data != null && sub_service_data.size() > 0) {
                for (int i = 0; i < sub_service_data.size(); i++) {
                    if (sub_service_data.get(i).getTitle().equalsIgnoreCase(subservicename)) {
                        sub_service_data.remove(i);
                        Log.w(TAG, "after removing list if subservicename" + new Gson().toJson(sub_service_data));

                    }
                }
            }
        }*/

        Log.w(TAG, "onItemSPSubServiceUnCheck sub_service_data " + new Gson().toJson(sub_service_data));


        if (subservicename != null) {
            if(sub_service_data != null && sub_service_data.size()>0){
                for(int i=0; i<sub_service_data.size();i++){
                    Log.w(TAG,"Subserviename : "+subservicename+" Subserviename list : "+sub_service_data.get(i).getTitle());
                    if(subservicename.equalsIgnoreCase(sub_service_data.get(i).getTitle())){
                        sub_service_data.get(i).setIsservice(false);
                        break;
                    }
                }
            }
        }

        Log.w(TAG, "after removing list if subservicename" + new Gson().toJson(sub_service_data));

    }

    @SuppressLint("LogNotTimber")
    public void SubServiceUpdateResponseCall(){
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        //Creating an object of our api interface
        RestApiInterface apiInterface = APIClient.getClient().create(RestApiInterface.class);
        Call<SuccessResponse> call = apiInterface.SubServiceUpdateResponseCall(RestUtils.getContentType(), subServiceUpdateRequest());
        Log.w(TAG,"url  :%s"+ call.request().url().toString());

        call.enqueue(new Callback<SuccessResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull Response<SuccessResponse> response) {
                avi_indicator.smoothToHide();


                if (response.body() != null) {
                    if(200 == response.body().getCode()){
                        Log.w(TAG,"SubServiceUpdateResponseCall" + new Gson().toJson(response.body()));
                        startActivity(new Intent(getApplicationContext(),ServiceProviderRegisterFormActivity.class));
                        finish();


                    }



                }

            }

            @Override
            public void onFailure(@NonNull Call<SuccessResponse> call,@NonNull  Throwable t) {
                avi_indicator.smoothToHide();
                Log.w(TAG,"SubServiceUpdateResponseCall flr"+t.getMessage());
            }
        });

    }
    @SuppressLint("LogNotTimber")
    private SubServiceUpdateRequest subServiceUpdateRequest() {
        /*
         * user_id : 616a83bf9e7b943a38ec0883
         * service_id : 5ff7f5171c72093650a13a14
         * service_name : Pet Grooming
         * sub_service_data : [{"_id":"6164231b65d9a57d7fc9575e","service_id":"5ff7f5171c72093650a13a14","title":"Sub Service 1","isservice":true},{"_id":"6164232765d9a57d7fc9575f","service_id":"5ff7f5171c72093650a13a14","title":"Sub Service 2","isservice":false}]
         */
        SubServiceUpdateRequest subServiceUpdateRequest = new SubServiceUpdateRequest();
        subServiceUpdateRequest.setUser_id(user_id);
        subServiceUpdateRequest.setService_id(ServiceId);
        subServiceUpdateRequest.setService_name(ServiceName);
        subServiceUpdateRequest.setSub_service_data(sub_service_data);

        Log.w(TAG,"subServiceUpdateRequest"+ new Gson().toJson(subServiceUpdateRequest));
        return subServiceUpdateRequest;
    }
}