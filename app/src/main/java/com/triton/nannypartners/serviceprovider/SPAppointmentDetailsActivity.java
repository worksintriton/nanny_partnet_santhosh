package com.triton.nannypartners.serviceprovider;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.triton.nannypartners.R;
import com.triton.nannypartners.activity.NotificationActivity;
import com.triton.nannypartners.api.APIClient;
import com.triton.nannypartners.api.RestApiInterface;
import com.triton.nannypartners.petlover.VideoCallPetLoverActivity;
import com.triton.nannypartners.requestpojo.AppoinmentCancelledRequest;
import com.triton.nannypartners.requestpojo.AppoinmentCompleteRequest;
import com.triton.nannypartners.requestpojo.AppointmentDetailsRequest;
import com.triton.nannypartners.requestpojo.EndAppointmentStatusRequest;
import com.triton.nannypartners.requestpojo.SPNotificationSendRequest;
import com.triton.nannypartners.requestpojo.StartAppointmentStatusRequest;
import com.triton.nannypartners.responsepojo.AppoinmentCancelledResponse;
import com.triton.nannypartners.responsepojo.AppoinmentCompleteResponse;
import com.triton.nannypartners.responsepojo.EndAppointmentStatusResponse;
import com.triton.nannypartners.responsepojo.NotificationSendResponse;
import com.triton.nannypartners.responsepojo.PetNewAppointmentDetailsResponse;
import com.triton.nannypartners.responsepojo.SPAppointmentDetailsResponse;
import com.triton.nannypartners.responsepojo.StartAppointmentStatusResponse;
import com.triton.nannypartners.utils.ConnectionDetector;
import com.triton.nannypartners.utils.RestUtils;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SPAppointmentDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private  String TAG = "SPAppointmentDetailsActivity";


    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.avi_indicator)
    AVLoadingIndicatorView avi_indicator;




    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.img_user)
    ImageView img_user;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_usrname)
    TextView txt_usrname;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_serv_name)
    TextView txt_serv_name;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_serv_cost)
    TextView txt_serv_cost;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_cancel)
    Button btn_cancel;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_start_stop)
    Button btn_start_stop;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_viewinvoice)
    Button btn_viewinvoice;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_stop)
    Button btn_stop;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_servname)
    TextView txt_servname;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_hrs)
    TextView txt_hrs;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_datetimeslot)
    TextView txt_datetimeslot;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_service_cost)
    TextView txt_service_cost;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_appointment_status)
    TextView txt_appointment_status;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_appointment_date)
    TextView txt_appointment_date;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_order_date)
    TextView txt_order_date;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_order_id)
    TextView txt_order_id;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_order_cost)
    TextView txt_order_cost;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_address)
    TextView txt_address;

    /*@SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_serv_timer)
    TextView txt_serv_timer;*/

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.scrollablContent)
    ScrollView scrollablContent;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.include_petlover_footer)
    View include_petlover_footer;

    BottomNavigationView bottom_navigation_view;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.include_petlover_header)
    View include_petlover_header;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.chronometer)
    Chronometer chronometer;

    String appointment_id;
    String appoinment_status;
    String start_appointment_status="";
    String end_appointment_status="";
    private Dialog dialog;
    private String bookedat;
    private String startappointmentstatus;
    private boolean isVaildDate;
    private String appointmentfor;
    private String spid;
    private String userid;
    private String from;
    private String userrate;
    Dialog alertDialog;
    private String appointmentid;
    private List<PetNewAppointmentDetailsResponse.DataBean.PetIdBean.PetImgBean> pet_image;
    private String petAgeandMonth;

    private String concatenatedStarNames = "";
    private String start_otp = "";
    private String end_otp = "",service_amount, hrs, work_status, payment_method,_id;
    CountDownTimer timer;
    private long pauseOffset;
    private boolean running;

    @SuppressLint({"LongLogTag", "LogNotTimber"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp_appointment_details);
        ButterKnife.bind(this);


        scrollablContent.setVisibility(View.GONE);



        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            appointment_id = extras.getString("appointment_id");

            from = extras.getString("fromactivity");

            Log.w(TAG,"appointment_id : "+appointment_id+" from : "+from);

        }



        ImageView img_back = include_petlover_header.findViewById(R.id.img_back);
        ImageView img_sos = include_petlover_header.findViewById(R.id.img_sos);
        ImageView img_notification = include_petlover_header.findViewById(R.id.img_notification);
        ImageView img_cart = include_petlover_header.findViewById(R.id.img_cart);
        ImageView img_profile = include_petlover_header.findViewById(R.id.img_profile);
        TextView toolbar_title = include_petlover_header.findViewById(R.id.toolbar_title);
        toolbar_title.setText(getResources().getString(R.string.appointment));
        img_sos.setVisibility(View.GONE);
        img_cart.setVisibility(View.GONE);



        img_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), NotificationActivity.class));
            }
        });
        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SPProfileScreenActivity.class);
                intent.putExtra("fromactivity",TAG);
                intent.putExtra("appointment_id",appointment_id);
                intent.putExtra("bookedat",bookedat);
                intent.putExtra("startappointmentstatus",startappointmentstatus);
                intent.putExtra("appointmentfor",appointmentfor);
                intent.putExtra("userrate",userrate);
                intent.putExtra("from",from);
                startActivity(intent);
            }
        });

        img_back.setOnClickListener(v -> onBackPressed());




        btn_cancel.setVisibility(View.GONE);



        if(from != null){
            if(from.equalsIgnoreCase("SPNewAppointmentAdapter")){

                btn_cancel.setVisibility(View.VISIBLE);

                btn_start_stop.setVisibility(View.GONE);

                btn_viewinvoice.setVisibility(View.GONE);

                btn_stop.setVisibility(View.GONE);

            }
            else if(from.equalsIgnoreCase("SPMissedAppointmentAdapter")){

                btn_cancel.setVisibility(View.GONE);

                btn_start_stop.setVisibility(View.GONE);

                btn_viewinvoice.setVisibility(View.GONE);

                btn_stop.setVisibility(View.GONE);

            }
            else if(from.equalsIgnoreCase("SPCompletedAppointmentAdapter")) {

                btn_cancel.setVisibility(View.GONE);

                btn_start_stop.setVisibility(View.GONE);

                btn_viewinvoice.setVisibility(View.VISIBLE);

                btn_stop.setVisibility(View.GONE);

            }

        }

        if (new ConnectionDetector(SPAppointmentDetailsActivity.this).isNetworkAvailable(SPAppointmentDetailsActivity.this)) {
            spAppointmentDetailsResponse();
        }

    }


    @SuppressLint({"LongLogTag", "LogNotTimber"})
    private void spAppointmentDetailsResponse() {
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        RestApiInterface ApiService = APIClient.getClient().create(RestApiInterface.class);
        Call<SPAppointmentDetailsResponse> call = ApiService.spAppointmentDetailsResponse(RestUtils.getContentType(), appointmentDetailsRequest());
        Log.w(TAG, "url  :%s" + call.request().url().toString());

        call.enqueue(new Callback<SPAppointmentDetailsResponse>() {
            @SuppressLint({"LongLogTag", "LogNotTimber"})
            @Override
            public void onResponse(@NonNull Call<SPAppointmentDetailsResponse> call, @NonNull Response<SPAppointmentDetailsResponse> response) {
                avi_indicator.smoothToHide();
                Log.w(TAG, "SPAppointmentDetailsResponse" + "--->" + new Gson().toJson(response.body()));


                if (response.body() != null) {

                    if (200 == response.body().getCode()) {
                        scrollablContent.setVisibility(View.VISIBLE);

                        String vaccinated, addr;

                        String usr_image = "";
                        if (response.body().getData() != null) {

                            _id = response.body().getData().get_id();

                            spid = response.body().getData().getSp_id().get_id();
                            appointmentid = response.body().getData().getAppointment_UID();
                            userid = response.body().getData().getUser_id().get_id();

                            String img_user = response.body().getData().getSp_id().getProfile_img();

                            String usrname = response.body().getData().getSp_id().getFirst_name();

                            String serv_name = response.body().getData().getService_name();

                            String service_amount = response.body().getData().getService_amount();

                            String servname = response.body().getData().getService_name();

                            hrs = response.body().getData().getHrs();

                            String datetimeslot = response.body().getData().getBooking_time();

                            String service_cost = response.body().getData().getService_amount();

                            String appointment_status = response.body().getData().getAppoinment_status();

                            String appointment_date = response.body().getData().getDisplay_date();

                            String order_date = response.body().getData().getDate_and_time();


                            String orderid = response.body().getData().getAppointment_UID();

                            String order_cost = response.body().getData().getService_amount();

                            addr = response.body().getData().getSp_business_info().get(0).getSp_loc();

                            appoinment_status = response.body().getData().getAppoinment_status();

                            start_appointment_status = response.body().getData().getStart_appointment_status();

                            end_appointment_status = response.body().getData().getEnd_appointment_status();

                            start_otp = response.body().getData().getStart_otp();

                            end_otp = response.body().getData().getEnd_otp();

                            payment_method = response.body().getData().getAddition_payment_method();

                            Log.w(TAG,"start_otp  "+ start_otp);

                            Log.w(TAG,"end_otp  "+ end_otp);

                            List<SPAppointmentDetailsResponse.DataBean.SpBusinessInfoBean> Address = response.body().getData().getSp_business_info();
                            for (int i = 0; i < Address.size(); i++) {
                                usr_image = Address.get(i).getThumbnail_image();

                            }
                            setView(img_user, usrname, serv_name ,service_amount ,servname, hrs,datetimeslot,service_cost,appointment_status,appointment_date,order_date,orderid,order_cost,addr);


                        }
                    }else{
                        scrollablContent.setVisibility(View.GONE);
                    }


                }
            }

            @Override
            public void onFailure(@NonNull Call<SPAppointmentDetailsResponse> call, @NonNull Throwable t) {
                avi_indicator.smoothToHide();

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


    @SuppressLint({"SetTextI18n", "LongLogTag", "LogNotTimber"})
    private void setView(String img_user, String usrname, String serv_name, String service_amount, String servname, String hrs, String datetimeslot, String service_cost, String appointment_status, String appointment_date, String order_date, String orderid, String order_cost, String addr) {


        if(img_user!= null && !img_user.isEmpty()){
            Glide.with(SPAppointmentDetailsActivity.this)
                    .load(img_user)
                    .into(this.img_user);

        }else{
            Glide.with(SPAppointmentDetailsActivity.this)
                    .load(APIClient.PROFILE_IMAGE_URL)
                    .into(this.img_user);
        }


        if(usrname != null && !usrname.isEmpty()){

            txt_usrname.setText(usrname);
        }
        else{
            txt_usrname.setText("");
        }

        if(serv_name != null && !serv_name.isEmpty()){

            txt_serv_name.setText(serv_name);
        }else{
            txt_serv_name.setText("");
        }


        if(service_amount != null && !service_amount.isEmpty()){

            txt_serv_cost.setText("\u20B9 "+service_amount);
        }else{
            txt_serv_cost.setText("");
        }

        if(servname != null && !servname.isEmpty()){

            txt_servname.setText(servname);
        }else{
            txt_servname.setText("");
        }


        if(hrs != null && !hrs.isEmpty()){

            txt_hrs.setText(hrs);
        }else{
            txt_hrs.setText("");
        }

        if(datetimeslot != null && !datetimeslot.isEmpty()){

            txt_datetimeslot.setText(datetimeslot);
        }else{
            txt_datetimeslot.setText("");
        }

        if(service_cost != null && !service_cost.isEmpty()){

            txt_service_cost.setText(service_cost);
        }else{
            txt_service_cost.setText("");
        }

        if(appointment_status != null && !appointment_status.isEmpty()){

            txt_appointment_status.setText(appointment_status);
        }else{
            txt_appointment_status.setText("");
        }

        if(appointment_date != null && !appointment_date.isEmpty()){

            txt_appointment_date.setText(appointment_date);
        }else{
            txt_appointment_date.setText("");
        }

        if(order_date != null && !order_date.isEmpty()){

            txt_order_date.setText(order_date);
        }else{
            txt_order_date.setText("");
        }


        if(orderid != null && !orderid.isEmpty()){

            txt_order_id.setText(orderid);
        }
        else{
            txt_order_id.setText("");
        }

        if(order_cost != null && !order_cost.isEmpty()){

            txt_order_cost.setText("\u20B9 "+order_cost);

        }
        else{
            txt_order_cost.setText("");
        }

        if(addr != null && !addr.isEmpty()){

            txt_address.setText(addr);
        }
        else{
            txt_address.setText("");
        }

        Log.w(TAG,"start_appointment_status  "+ start_appointment_status);

        Log.w(TAG,"end_appointment_status  "+ end_appointment_status);

        if(from!=null&&from.equals("SPNewAppointmentAdapter")&&start_appointment_status != null && !start_appointment_status.isEmpty()&&start_appointment_status.equals("Not Started")){

            btn_start_stop.setVisibility(View.VISIBLE);

            btn_stop.setVisibility(View.GONE);

            chronometer.setVisibility(View.GONE);

            btn_cancel.setVisibility(View.GONE);


        }
        else if(from!=null&&from.equals("SPNewAppointmentAdapter")&&end_appointment_status != null && !end_appointment_status.isEmpty()&&start_appointment_status.equals("Not End")){

            btn_start_stop.setVisibility(View.GONE);

            btn_cancel.setVisibility(View.GONE);

            btn_stop.setVisibility(View.VISIBLE);

            chronometer.setVisibility(View.VISIBLE);

            startChronometer();
        }

        else {

            btn_start_stop.setVisibility(View.GONE);

            btn_stop.setVisibility(View.GONE);

            chronometer.setVisibility(View.GONE);

            btn_cancel.setVisibility(View.GONE);

        }

        btn_cancel.setOnClickListener(v -> showStatusAlert(appointment_id));

        btn_start_stop.setOnClickListener(this);

        btn_stop.setOnClickListener(this);

        btn_viewinvoice.setOnClickListener(this);



    }


    @SuppressLint("SetTextI18n")
    private void showStatusAlert(String id) {
        try {
            dialog = new Dialog(SPAppointmentDetailsActivity.this);
            dialog.setContentView(R.layout.alert_approve_reject_layout);
            TextView tvheader = dialog.findViewById(R.id.tvInternetNotConnected);
            tvheader.setText(R.string.cancelappointment);
            Button dialogButtonApprove = dialog.findViewById(R.id.btnApprove);
            dialogButtonApprove.setText("Yes");
            Button dialogButtonRejected = dialog.findViewById(R.id.btnReject);
            dialogButtonRejected.setText("No");

            dialogButtonApprove.setOnClickListener(view -> {
                dialog.dismiss();
                    appoinmentCancelledResponseCall(id);

            });
            dialogButtonRejected.setOnClickListener(view -> dialog.dismiss());
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        }


    }

    @SuppressLint({"LongLogTag", "LogNotTimber"})
    private void appoinmentCancelledResponseCall(String id) {
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        RestApiInterface apiInterface = APIClient.getClient().create(RestApiInterface.class);
        Call<AppoinmentCancelledResponse> call = apiInterface.spappoinmentCancelledResponseCall(RestUtils.getContentType(), appoinmentCancelledRequest(id));
        Log.w(TAG,"appoinmentCancelledResponseCall url  :%s"+" "+ call.request().url().toString());

        call.enqueue(new Callback<AppoinmentCancelledResponse>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(@NonNull Call<AppoinmentCancelledResponse> call, @NonNull Response<AppoinmentCancelledResponse> response) {

                Log.w(TAG,"appoinmentCancelledResponseCall"+ "--->" + new Gson().toJson(response.body()));

                avi_indicator.smoothToHide();

                if (response.body() != null) {
                    if(response.body().getCode() == 200){
                        spnotificationSendResponseCall();





                    }

                }


            }

            @Override
            public void onFailure(@NonNull Call<AppoinmentCancelledResponse> call, @NonNull Throwable t) {

                avi_indicator.smoothToHide();
                Log.w(TAG,"appoinmentCancelledResponseCall flr"+"--->" + t.getMessage());
            }
        });

    }
    @SuppressLint({"LongLogTag", "LogNotTimber"})
    private AppoinmentCancelledRequest appoinmentCancelledRequest(String id) {

        /*
         * _id : 5fc639ea72fc42044bfa1683
         * missed_at : 23-10-2000 10 : 00 AM
         * doc_feedback : One Emergenecy work i am cancelling this appointment
         * appoinment_status : Missed
         */


        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm aa", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        AppoinmentCancelledRequest appoinmentCancelledRequest = new AppoinmentCancelledRequest();
        appoinmentCancelledRequest.set_id(id);
        appoinmentCancelledRequest.setMissed_at(currentDateandTime);
        appoinmentCancelledRequest.setDoc_feedback("");
        appoinmentCancelledRequest.setAppoinment_status("Missed");
        appoinmentCancelledRequest.setAppoint_patient_st("Doctor Cancelled appointment");
        Log.w(TAG,"appoinmentCancelledRequest"+ "--->" + new Gson().toJson(appoinmentCancelledRequest));
        return appoinmentCancelledRequest;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    public void callDirections(String tag){
        Intent intent = new Intent(getApplicationContext(), ServiceProviderDashboardActivity.class);
        intent.putExtra("tag",tag);
        startActivity(intent);
        finish();
    }


    @SuppressLint("SetTextI18n")
    private void showStatusAlertCompleteAppointment(String id) {

        try {

            dialog = new Dialog(SPAppointmentDetailsActivity.this);
            dialog.setContentView(R.layout.alert_approve_reject_layout);
            TextView tvheader = dialog.findViewById(R.id.tvInternetNotConnected);
            tvheader.setText(R.string.completeappointment);
            Button dialogButtonApprove = dialog.findViewById(R.id.btnApprove);
            dialogButtonApprove.setText("Yes");
            Button dialogButtonRejected = dialog.findViewById(R.id.btnReject);
            dialogButtonRejected.setText("No");

            dialogButtonApprove.setOnClickListener(view -> {
                dialog.dismiss();
                appoinmentCompleteResponseCall(id);


            });
            dialogButtonRejected.setOnClickListener(view -> dialog.dismiss());
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        }




    }
    @SuppressLint({"LongLogTag", "LogNotTimber"})
    private void appoinmentCompleteResponseCall(String id) {
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        RestApiInterface apiInterface = APIClient.getClient().create(RestApiInterface.class);
        Call<AppoinmentCompleteResponse> call = apiInterface.spappoinmentCompleteResponseCall(RestUtils.getContentType(), appoinmentCompleteRequest(id));
        Log.w(TAG,"AppoinmentCompleteResponse url  :%s"+" "+ call.request().url().toString());

        call.enqueue(new Callback<AppoinmentCompleteResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NonNull Call<AppoinmentCompleteResponse> call, @NonNull Response<AppoinmentCompleteResponse> response) {

                Log.w(TAG,"AppoinmentCompleteResponse"+ "--->" + new Gson().toJson(response.body()));

                avi_indicator.smoothToHide();

                if (response.body() != null) {
                    if(response.body().getCode() == 200){
                        startActivity(new Intent(getApplicationContext(), ServiceProviderDashboardActivity.class));
                    }

                }


            }

            @Override
            public void onFailure(@NonNull Call<AppoinmentCompleteResponse> call, @NonNull Throwable t) {

                avi_indicator.smoothToHide();
                Log.w(TAG,"AppoinmentCompleteResponseflr"+"--->" + t.getMessage());
            }
        });

    }
    @SuppressLint({"LongLogTag", "LogNotTimber"})
    private AppoinmentCompleteRequest appoinmentCompleteRequest(String id) {
        /*
         * _id : 5fc639ea72fc42044bfa1683
         * completed_at : 23-10-2000 10 : 00 AM
         * appoinment_status : Completed
         */

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm aa", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        AppoinmentCompleteRequest appoinmentCompleteRequest = new AppoinmentCompleteRequest();
        appoinmentCompleteRequest.set_id(id);
        appoinmentCompleteRequest.setCompleted_at(currentDateandTime);
        appoinmentCompleteRequest.setAppoinment_status("Completed");
        Log.w(TAG,"appoinmentCompleteRequest"+ "--->" + new Gson().toJson(appoinmentCompleteRequest));
        return appoinmentCompleteRequest;
    }

    @SuppressLint({"LongLogTag", "LogNotTimber"})
    private void spnotificationSendResponseCall() {
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        RestApiInterface ApiService = APIClient.getClient().create(RestApiInterface.class);
        Call<NotificationSendResponse> call = ApiService.spnotificationSendResponseCall(RestUtils.getContentType(),spNotificationSendRequest());

        Log.w(TAG,"url  :%s"+ call.request().url().toString());

        call.enqueue(new Callback<NotificationSendResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NonNull Call<NotificationSendResponse> call, @NonNull Response<NotificationSendResponse> response) {
                avi_indicator.smoothToHide();
                Log.w(TAG,"notificationSendResponseCall"+ "--->" + new Gson().toJson(response.body()));


                if (response.body() != null) {
                    if(response.body().getCode() == 200){
                        startActivity(new Intent(getApplicationContext(), ServiceProviderDashboardActivity.class));

                    }

                }


            }

            @SuppressLint("LogNotTimber")
            @Override
            public void onFailure(@NonNull Call<NotificationSendResponse> call, @NonNull Throwable t) {
                avi_indicator.smoothToHide();

                Log.w(TAG,"NotificationSendResponse flr"+"--->" + t.getMessage());
            }
        });

    }
    @SuppressLint({"LongLogTag", "LogNotTimber"})
    private SPNotificationSendRequest spNotificationSendRequest() {

        /*
         * status : Payment Failed
         * date : 23-10-2020 11:00 AM
         * appointment_UID : PET-2923029239123
         * user_id : 601b8ac3204c595ee52582f2
         * sp_id : 601ba9c6270cbe79fd900183
         */
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm aa");
        String currentDateandTime = simpleDateFormat.format(new Date());



        SPNotificationSendRequest spNotificationSendRequest = new SPNotificationSendRequest();
        spNotificationSendRequest.setStatus("Patient Appointment Cancelled");
        spNotificationSendRequest.setDate(currentDateandTime);
        spNotificationSendRequest.setAppointment_UID(appointmentid);
        spNotificationSendRequest.setUser_id(userid);
        spNotificationSendRequest.setSp_id(spid);


        Log.w(TAG,"spNotificationSendRequest"+ "--->" + new Gson().toJson(spNotificationSendRequest));
        return spNotificationSendRequest;
    }

    @SuppressLint({"LogNotTimber", "LongLogTag"})
    private void compareDatesandTime(String currentDateandTime, String bookingDateandTime) {
        try{

            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm aa");

            Date currentDate = formatter.parse(currentDateandTime);

            Date responseDate = formatter.parse(bookingDateandTime);

            Log.w(TAG,"compareDatesandTime--->"+"responseDate :"+responseDate+" "+"currentDate :"+currentDate);

            if (currentDate != null) {
                if (responseDate != null) {
                    if (currentDate.compareTo(responseDate)<0 || responseDate.compareTo(currentDate) == 0)
                    {
                        Log.w(TAG,"date is equal");
                        isVaildDate = true;

                    }else{
                        Log.w(TAG,"date is not equal");
                        isVaildDate = false;
                    }
                }
            }


        }catch (ParseException e1){
            e1.printStackTrace();
        }
    }
    @SuppressLint({"LogNotTimber", "LongLogTag"})
    private void getAge(int year, int month, int day){
        Log.w(TAG,"getAge : year "+year+" month : "+ month+" day : "+day);
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        Log.w(TAG,"age : "+age+" todayyear : "+today.get(Calendar.YEAR)+" dobyear : "+ dob.get(Calendar.YEAR));


        int months = dob.get(Calendar.MONTH) - today.get(Calendar.MONTH);
        int currentmonths = (today.get(Calendar.MONTH))+1;
        Log.w(TAG,"dob months: "+dob.get(Calendar.MONTH)+" currentmonths : "+ currentmonths);

        Log.w(TAG," todayyear : "+today.get(Calendar.YEAR)+" dobyear : "+ dob.get(Calendar.YEAR));

        Log.w(TAG,"Conditions : "+(today.get(Calendar.YEAR) < dob.get(Calendar.YEAR)));
        if(today.get(Calendar.YEAR) < dob.get(Calendar.YEAR)){
            age--;
        }

        Log.w(TAG,"age: "+age+" monthsInt : "+ months);
        String ageS = Integer.toString(age);
        String monthsS = Integer.toString(months);

        Log.w(TAG,"ageS: "+ageS+" months : "+monthsS);

        if(age != 0){
            petAgeandMonth = ageS+" years "+monthsS+" months";
        }else{
            petAgeandMonth = monthsS+" months";

        }



        Log.w(TAG,"ageS: "+ageS+" months : "+monthsS);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.rl_homes:
                callDirections("1");
                break;

            case R.id.rl_home:
                callDirections("1");
                break;

            case R.id.rl_shop:
                callDirections("2");
                break;

            case R.id.rl_comn:
                callDirections("3");
                break;

            case R.id.btn_start_stop:

                otpValidation("start",appointment_id);

                break;

            case R.id.btn_stop:

                otpValidation("stop",appointment_id);

                break;

            case R.id.btn_viewinvoice:

                break;

        }

    }

    @SuppressLint("SetTextI18n")
    private void otpValidation(String mode,String appointment_id) {

        try {

            dialog = new Dialog(SPAppointmentDetailsActivity.this);
            dialog.setContentView(R.layout.alert_otp_layout);
            EditText edt_pin_entry = dialog.findViewById(R.id.edt_pin_entry);

            Button btn_back_to_appointment = dialog.findViewById(R.id.btn_back_to_appointment);


            btn_back_to_appointment.setOnClickListener(view -> {


                if(edt_pin_entry.getText().toString().length()==6){

                    Log.w(TAG,"mode "+ mode);

                    Log.w(TAG,"length  "+ edt_pin_entry.getText().toString().length());

                    Log.w(TAG,"start_otp  "+ start_otp);

                    Log.w(TAG,"end_otp  "+ end_otp);

                    Log.w(TAG,"appointment_id   "+ appointment_id);

                    if(mode.equals("start")&&start_otp!=null&&start_otp.equals(edt_pin_entry.getText().toString())){

                        appoinmentStartResponseCall(appointment_id);
                        dialog.dismiss();
                    }
                    else if(mode.equals("stop")&&end_otp!=null&&end_otp.equals(edt_pin_entry.getText().toString())){

                        /*timer.cancel();*/
                        appoinmentStopResponseCall(appointment_id);
                        dialog.dismiss();
                    }

                    else {

                        Toasty.warning(getApplicationContext(), "Please Enter Valid OTP", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                    }
                }
                else {

                    Toasty.warning(getApplicationContext(), "Please Enter Valid OTP", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }


            });

            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        }

    }

    @SuppressLint({"LongLogTag", "LogNotTimber"})
    private void appoinmentStartResponseCall(String id) {
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        RestApiInterface apiInterface = APIClient.getClient().create(RestApiInterface.class);
        Call<StartAppointmentStatusResponse> call = apiInterface.spStartAppointmentResponseCall(RestUtils.getContentType(), StartAppointmentStatusRequest(id));
        Log.w(TAG,"StartAppointmentStatusResponse url  :%s"+" "+ call.request().url().toString());

        call.enqueue(new Callback<StartAppointmentStatusResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NonNull Call<StartAppointmentStatusResponse> call, @NonNull Response<StartAppointmentStatusResponse> response) {

                Log.w(TAG,"StartAppointmentStatusResponse"+ "--->" + new Gson().toJson(response.body()));

                avi_indicator.smoothToHide();

                if (response.body() != null) {
                    if(response.body().getCode() == 200){

                        Toasty.success(getApplicationContext(),""+response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        startTimer();

                    }

                }


            }

            @Override
            public void onFailure(@NonNull Call<StartAppointmentStatusResponse> call, @NonNull Throwable t) {

                avi_indicator.smoothToHide();
                Log.w(TAG,"StartAppointmentStatusResponseflr"+"--->" + t.getMessage());
            }
        });

    }

    @SuppressLint({"LongLogTag", "LogNotTimber"})
    private StartAppointmentStatusRequest StartAppointmentStatusRequest(String id) {

        /**
         * _id : 616b1d179e7b943a38ec088e
         * start_appointment_status : 18-10-2021 08:11 AM
         */


        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm aa", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        StartAppointmentStatusRequest StartAppointmentStatusRequest = new StartAppointmentStatusRequest();
        StartAppointmentStatusRequest.set_id(id);
        StartAppointmentStatusRequest.setStart_appointment_status(currentDateandTime);
        Log.w(TAG,"StartAppointmentStatusRequest"+ "--->" + new Gson().toJson(StartAppointmentStatusRequest));
        return StartAppointmentStatusRequest;
    }

    @SuppressLint({"LongLogTag", "LogNotTimber"})
    private void appoinmentStopResponseCall(String id) {
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        RestApiInterface apiInterface = APIClient.getClient().create(RestApiInterface.class);
        Call<EndAppointmentStatusResponse> call = apiInterface.spStopAppointmentResponseCall(RestUtils.getContentType(), EndAppointmentStatusRequest(id));
        Log.w(TAG,"EndAppointmentStatusResponse url  :%s"+" "+ call.request().url().toString());

        call.enqueue(new Callback<EndAppointmentStatusResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NonNull Call<EndAppointmentStatusResponse> call, @NonNull Response<EndAppointmentStatusResponse> response) {

                Log.w(TAG,"EndAppointmentStatusResponse"+ "--->" + new Gson().toJson(response.body()));

                avi_indicator.smoothToHide();

                if (response.body() != null) {
                    if(response.body().getCode() == 200){

                        btn_start_stop.setVisibility(View.GONE);

                        btn_cancel.setVisibility(View.GONE);

                        btn_stop.setVisibility(View.GONE);

                        resetChronometer();

                        chronometer.setVisibility(View.GONE);

                        gotoInvoiceLoader();

                        Toasty.success(getApplicationContext(),""+response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    }

                }


            }

            @Override
            public void onFailure(@NonNull Call<EndAppointmentStatusResponse> call, @NonNull Throwable t) {

                avi_indicator.smoothToHide();
                Log.w(TAG,"EndAppointmentStatusResponseflr"+"--->" + t.getMessage());
            }
        });

    }

    private void gotoInvoiceLoader() {

        Intent intent = new Intent(getApplicationContext(), SPLoaderActivity.class);
        intent.putExtra("fromactivity",TAG);
        intent.putExtra("appointment_id",appointment_id);
        intent.putExtra("start_appointment_status",start_appointment_status);
        intent.putExtra("end_appointment_status",end_appointment_status);
        intent.putExtra("service_amount",service_amount);
        intent.putExtra("hrs",hrs);
        intent.putExtra("_id",_id);
        intent.putExtra("appoinment_status",appoinment_status);
        intent.putExtra("payment_method",payment_method);
        startActivity(intent);

    }

    @SuppressLint({"LongLogTag", "LogNotTimber"})
    private EndAppointmentStatusRequest EndAppointmentStatusRequest(String id) {

        /**
         * _id : 616b1d179e7b943a38ec088e
         * end_appointment_status : 18-10-2021 08:20 AM
         */


        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm aa", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        EndAppointmentStatusRequest EndAppointmentStatusRequest = new EndAppointmentStatusRequest();
        EndAppointmentStatusRequest.set_id(id);
        EndAppointmentStatusRequest.setEnd_appointment_status(currentDateandTime);
        Log.w(TAG,"EndAppointmentStatusRequest"+ "--->" + new Gson().toJson(EndAppointmentStatusRequest));
        return EndAppointmentStatusRequest;
    }

    private void startTimer() {

        btn_start_stop.setVisibility(View.GONE);
        btn_cancel.setVisibility(View.GONE);
        chronometer.setVisibility(View.VISIBLE);
        btn_stop.setVisibility(View.VISIBLE);

        chronometer.setBase(SystemClock.elapsedRealtime());

        startChronometer();

        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
               /* if ((SystemClock.elapsedRealtime() - chronometer.getBase()) >= 10000) {


                }*/

               // chronometer.setBase(SystemClock.elapsedRealtime());
            }
        });

    }

    public void startChronometer() {
        if (!running) {
           //chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            running = true;
        }
    }

    public void pauseChronometer() {
        if (running) {
            chronometer.stop();
           // pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
        }
    }

    public void resetChronometer() {
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
    }

}