package com.triton.nannypartners.serviceprovider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.triton.nannypartners.R;
import com.triton.nannypartners.api.APIClient;
import com.triton.nannypartners.api.RestApiInterface;
import com.triton.nannypartners.requestpojo.AppointmentDetailsRequest;
import com.triton.nannypartners.requestpojo.TimeCalRequest;
import com.triton.nannypartners.responsepojo.CartDetailsResponse;
import com.triton.nannypartners.responsepojo.SPAppointmentDetailsResponse;
import com.triton.nannypartners.responsepojo.SPAppointmentProcessCompleteResponse;
import com.triton.nannypartners.responsepojo.SPDetailsRepsonse;
import com.triton.nannypartners.utils.ConnectionDetector;
import com.triton.nannypartners.utils.RestUtils;

import java.text.ParseException;
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

    int additional_hours;
    int addition_amount;
    String total_hours;
    String total_paid_amount,addition_payment_status,work_status;
    int additional_minutes;

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

            //start_appointment_status = extras.getString("start_appointment_status");

            //end_appointment_status = extras.getString("end_appointment_status");

           // service_amount = extras.getString("service_amount");

           // hrs = extras.getString("hrs");

            _id = extras.getString("_id");

            //appoinment_status = extras.getString("appoinment_status");

            //payment_method = extras.getString("payment_method");

            Log.w(TAG,"fromactivity : "+fromactivity);


        }

        if (new ConnectionDetector(SPLoaderActivity.this).isNetworkAvailable(SPLoaderActivity.this)){

            spAppointmentDetailsResponse();

        }


        Glide.with(SPLoaderActivity.this)
                .load(R.drawable.calculator)
                .into(logo);


        MaterialProgressBar progressBar = (MaterialProgressBar) findViewById(R.id.progress);

        // timer for seekbar
        final int oneMin = 1 * 60 * 1000; // 1 minute in milli seconds

    //    final int oneMin = 10000; // 1 minute in milli seconds

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

                gotoInvoice();


            }
        }.start();

    /*    new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                *//* Create an Intent that will start the SPInvoiceActivity. *//*

            }
        }, SPLASH_TIME_OUT);*/
    }



    @SuppressLint({"LongLogTag", "LogNotTimber"})
    private void spAppointmentDetailsResponse() {
        RestApiInterface ApiService = APIClient.getClient().create(RestApiInterface.class);
        Call<SPAppointmentDetailsResponse> call = ApiService.spAppointmentDetailsResponse(RestUtils.getContentType(), appointmentDetailsRequest());
        Log.w(TAG, "url  :%s" + call.request().url().toString());

        call.enqueue(new Callback<SPAppointmentDetailsResponse>() {
            @SuppressLint({"LongLogTag", "LogNotTimber"})
            @Override
            public void onResponse(@NonNull Call<SPAppointmentDetailsResponse> call, @NonNull Response<SPAppointmentDetailsResponse> response) {
                Log.w(TAG, "SPAppointmentDetailsResponse" + "--->" + new Gson().toJson(response.body()));


                if (response.body() != null) {

                    if (200 == response.body().getCode()) {

                        if (response.body().getData() != null) {

                           // work_status = response.body().getData().getWork_status();

                            //_id = response.body().getData().get_id();

                                appoinment_status = response.body().getData().getAppoinment_status();

                                start_appointment_status = response.body().getData().getStart_appointment_status();

                                end_appointment_status = response.body().getData().getEnd_appointment_status();

                            service_amount = response.body().getData().getService_amount();

                            hrs = response.body().getData().getHrs();

                            payment_method = response.body().getData().getPayment_method();


                            addition_payment_status = "Not Paid";

                            work_status = "Completed";

                            if(start_appointment_status!=null&&end_appointment_status!=null&&!start_appointment_status.isEmpty()&&!end_appointment_status.isEmpty()){

                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm aa");

                                try {
                                    Date date1 = simpleDateFormat.parse(start_appointment_status);
                                    Date date2 = simpleDateFormat.parse(end_appointment_status);

                                    String[] values = printDifference(date1, date2);

                                    additional_hours = Integer.parseInt(values[1]);

                                    Log.w(TAG,"additional_hours : "+additional_hours);

                                    additional_minutes = Integer.parseInt(values[2]);

                                    Log.w(TAG,"additional_minutes : "+additional_minutes);

                                    int comphrss = Integer.parseInt(hrs);

                                    if(additional_minutes>1||additional_hours>comphrss){

                                        Log.w(TAG,"Condition : true");

                                        int total_hrs_calc = comphrss + additional_hours;

                                        total_hours = String.valueOf(total_hrs_calc);

                                        Log.w(TAG,"total_hours : "+total_hours);

                                        addition_amount = (Integer.parseInt(service_amount) * additional_hours);

                                        Log.w(TAG,"addition_amount : "+addition_amount);

                                        int total_amount_calc = Integer.parseInt(service_amount) + addition_amount;

                                        total_paid_amount = String.valueOf(total_amount_calc);

                                        Log.w(TAG,"total_paid_amount : "+total_paid_amount);

                                        timeCalResponseCall(total_hours,additional_hours,addition_amount,payment_method,addition_payment_status,total_paid_amount,work_status);
                                    }

                                    else {

                                        Log.w(TAG,"Condition : false");

                                        additional_hours = 0;

                                        Log.w(TAG,"additional_hours : "+additional_hours);

                                        total_hours = hrs;

                                        Log.w(TAG,"total_hours : "+total_hours);

                                        addition_amount = 0;

                                        Log.w(TAG,"addition_amount : "+addition_amount);

                                        total_paid_amount = service_amount;

                                        Log.w(TAG,"total_paid_amount : "+total_paid_amount);

                                        timeCalResponseCall(total_hours,additional_hours,addition_amount,payment_method,addition_payment_status,total_paid_amount,work_status);


                                    }



                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }



                        }
                    }else{

                    }


                }
            }

            @Override
            public void onFailure(@NonNull Call<SPAppointmentDetailsResponse> call, @NonNull Throwable t) {


                Log.w(TAG, "PetNewAppointmentDetailsResponse" + "--->" + t.getMessage());
            }
        });



    }


    @SuppressLint({"LongLogTag", "LogNotTimber"})
    private AppointmentDetailsRequest appointmentDetailsRequest() {

        AppointmentDetailsRequest appointmentDetailsRequest = new AppointmentDetailsRequest();
        appointmentDetailsRequest.setAppointment_id(appointment_id);
        Log.w(TAG, "appointmentDetailsRequest" + "--->" + new Gson().toJson(appointmentDetailsRequest));
        return appointmentDetailsRequest;
    }

    @Override
    public void onBackPressed() {
        /*super.onBackPressed();*/
    }


    //1 minute = 60 seconds
    //1 hour = 60 x 60 = 3600
    //1 day = 3600 x 24 = 86400
    static String[] printDifference(Date startDate, Date endDate) {
        //milliseconds

        String[] strings = new String[4];

        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        System.out.printf("%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);

        strings[0] = String.valueOf(elapsedDays);

        strings[1] = String.valueOf(elapsedHours);

        strings[2] = String.valueOf(elapsedMinutes);

        strings[3] = String.valueOf(elapsedSeconds);

        return strings;
    }

    @SuppressLint({"LongLogTag", "LogNotTimber"})
    private void timeCalResponseCall(String total_hours, int additional_hours, int addition_amount, String payment_method, String addition_payment_status, String total_paid_amount, String work_status) {
        RestApiInterface apiInterface = APIClient.getClient().create(RestApiInterface.class);
        Call<SPAppointmentProcessCompleteResponse> call = apiInterface.timeCalResponseCall(RestUtils.getContentType(), TimeCalRequest(total_hours,additional_hours,addition_amount,payment_method,addition_payment_status,total_paid_amount,work_status));
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

    private void gotoInvoice() {

        Intent intent = new Intent(getApplicationContext(), SPInvoiceActivity.class);
        intent.putExtra("fromactivity",TAG);
        intent.putExtra("appointment_id",appointment_id);
        intent.putExtra("start_appointment_status",start_appointment_status);
        intent.putExtra("end_appointment_status",end_appointment_status);
        intent.putExtra("service_amount",service_amount);
        intent.putExtra("hrs",hrs);
        intent.putExtra("_id",_id);
        intent.putExtra("appoinment_status",appoinment_status);
        intent.putExtra("payment_method",payment_method);
        intent.putExtra("work_status",work_status);
        startActivity(intent);
    }

    @SuppressLint({"LongLogTag", "LogNotTimber"})
    private TimeCalRequest TimeCalRequest(String total_hours, int additional_hours, int addition_amount, String payment_method, String addition_payment_status, String total_paid_amount, String work_status) {


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

        String post_additional_hours,post_addition_amount;

        if(additional_hours!=0){

            post_additional_hours = String.valueOf(additional_hours);

        }

        else {

            post_additional_hours = "";
        }


        if(addition_amount!=0){

            post_addition_amount = String.valueOf(addition_amount);

        }

        else {

            post_addition_amount = "";
        }

        TimeCalRequest TimeCalRequest = new TimeCalRequest();
        TimeCalRequest.set_id(appointment_id);
        TimeCalRequest.setTotal_hours(total_hours);
        TimeCalRequest.setAdditional_hours(post_additional_hours);
        TimeCalRequest.setAddition_amount(post_addition_amount);
        TimeCalRequest.setAddition_payment_method(payment_method);
        TimeCalRequest.setAddition_payment_status(addition_payment_status);
        TimeCalRequest.setTotal_paid_amount(total_paid_amount);
        TimeCalRequest.setWork_status(work_status);
        Log.w(TAG,"TimeCalRequest"+ "--->" + new Gson().toJson(TimeCalRequest));
        return TimeCalRequest;
    }


}