package com.triton.nannypartners.serviceprovider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.triton.nannypartners.R;
import com.triton.nannypartners.api.APIClient;
import com.triton.nannypartners.api.RestApiInterface;
import com.triton.nannypartners.requestpojo.TimeCalRequest;
import com.triton.nannypartners.responsepojo.CartDetailsResponse;
import com.triton.nannypartners.responsepojo.SPAppointmentProcessCompleteResponse;
import com.triton.nannypartners.responsepojo.SPDetailsRepsonse;
import com.triton.nannypartners.utils.RestUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SPLoaderActivity extends AppCompatActivity {

    private String TAG = "SPLoaderActivity";

    private static final long SPLASH_TIME_OUT = 10000;

/*    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.avi_indicator)
    AVLoadingIndicatorView avi_indicator;*/

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.logo)
    ImageView logo;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_title)
    TextView txt_title;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_sub_title)
    TextView txt_sub_title;

    String fromactivity,appointment_id, start_appointment_status,end_appointment_status,service_amount,hrs,_id,appoinment_status,payment_method;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(+R.layout.activity_sp_loader);
        ButterKnife.bind(this);
        Log.w(TAG,"onCreate");

        //  avi_indicator.smoothToShow();


        Bundle extras = getIntent().getExtras();

        if (extras != null) {

            fromactivity = extras.getString("fromactivity");

            appointment_id = extras.getString("appointment_id");

            start_appointment_status = extras.getString("start_appointment_status");

            end_appointment_status = extras.getString("end_appointment_status");

            service_amount = extras.getString("service_amount");

            hrs = extras.getString("hrs");

            _id = extras.getString("_id");

            appoinment_status = extras.getString("appoinment_status");

            payment_method = extras.getString("payment_method");

            Log.w(TAG,"fromactivity : "+fromactivity);


        }

        Glide.with(SPLoaderActivity.this)
                .load(R.drawable.calculator)
                .into(logo);


        MaterialProgressBar progressBar = (MaterialProgressBar) findViewById(R.id.progress);

        // timer for seekbar
//        final int oneMin = 1 * 60 * 1000; // 1 minute in milli seconds

        final int oneMin = 10000; // 1 minute in milli seconds

        /** CountDownTimer starts with 1 minutes and every onTick is 1 second */
        new CountDownTimer(oneMin, 1000) {
            public void onTick(long millisUntilFinished) {

                //forward progress
                long finishedSeconds = oneMin - millisUntilFinished;
                int total = (int) (((float)finishedSeconds / (float)oneMin) * 100.0);
                progressBar.setProgress(total);

//                //backward progress
//                int total = (int) (((float) millisUntilFinished / (float) oneMin) * 100.0);
//                progressBar.setProgress(total);

            }

            public void onFinish() {
                // DO something when 1 minute is up




            }
        }.start();

    /*    new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                *//* Create an Intent that will start the SPInvoiceActivity. *//*

            }
        }, SPLASH_TIME_OUT);*/
    }

    @Override
    public void onBackPressed() {
        /*super.onBackPressed();*/
    }


    @SuppressLint({"LongLogTag", "LogNotTimber"})
    private void timeCalResponseCall() {
        RestApiInterface apiInterface = APIClient.getClient().create(RestApiInterface.class);
        Call<SPAppointmentProcessCompleteResponse> call = apiInterface.timeCalResponseCall(RestUtils.getContentType(), TimeCalRequest());
        Log.w(TAG,"SPAppointmentProcessCompleteResponse url  :%s"+" "+ call.request().url().toString());

        call.enqueue(new Callback<SPAppointmentProcessCompleteResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NonNull Call<SPAppointmentProcessCompleteResponse> call, @NonNull Response<SPAppointmentProcessCompleteResponse> response) {

                Log.w(TAG,"SPAppointmentProcessCompleteResponse"+ "--->" + new Gson().toJson(response.body()));

                if (response.body() != null) {
                    if(response.body().getCode() == 200){

                    }

                }


            }

            @Override
            public void onFailure(@NonNull Call<SPAppointmentProcessCompleteResponse> call, @NonNull Throwable t) {

                Log.w(TAG,"SPAppointmentProcessCompleteResponseflr"+"--->" + t.getMessage());
            }
        });

    }
    @SuppressLint({"LongLogTag", "LogNotTimber"})
    private TimeCalRequest TimeCalRequest() {


        /**
         * _id : 6176a1378dd3e15b142de08e
         * total_hours :
         * additional_hours :
         * addition_amount :
         * addition_payment_method :
         * addition_payment_status :
         * total_paid_amount :
         * work_status :
         */

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm aa", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        TimeCalRequest TimeCalRequest = new TimeCalRequest();
        TimeCalRequest.set_id(_id);
        TimeCalRequest.setTotal_hours("");
        TimeCalRequest.setAdditional_hours("");
        TimeCalRequest.setAddition_amount("");
        TimeCalRequest.setAddition_payment_method("");
        TimeCalRequest.setAddition_payment_status("");
        TimeCalRequest.setTotal_paid_amount("");
        TimeCalRequest.setWork_status("");
        Log.w(TAG,"TimeCalRequest"+ "--->" + new Gson().toJson(TimeCalRequest));
        return TimeCalRequest;
    }


}