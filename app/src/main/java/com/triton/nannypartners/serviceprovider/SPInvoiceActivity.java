package com.triton.nannypartners.serviceprovider;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.triton.nannypartners.R;
import com.triton.nannypartners.activity.ChooseUserTypeActivity;
import com.triton.nannypartners.activity.LoginActivity;
import com.triton.nannypartners.api.APIClient;
import com.triton.nannypartners.api.RestApiInterface;
import com.triton.nannypartners.requestpojo.AppoinmentCompleteRequest;
import com.triton.nannypartners.requestpojo.AppointmentDetailsRequest;
import com.triton.nannypartners.requestpojo.RaiseInvoiceRequest;
import com.triton.nannypartners.responsepojo.AppoinmentCompleteResponse;
import com.triton.nannypartners.responsepojo.PetNewAppointmentDetailsResponse;
import com.triton.nannypartners.responsepojo.SPAppointmentDetailsResponse;
import com.triton.nannypartners.responsepojo.SPAppointmentProcessCompleteResponse;
import com.triton.nannypartners.utils.ConnectionDetector;
import com.triton.nannypartners.utils.RestUtils;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SPInvoiceActivity extends AppCompatActivity implements View.OnClickListener {

    private  String TAG = "SPInvoiceActivity";


    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.avi_indicator)
    AVLoadingIndicatorView avi_indicator;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.scrollablContent)
    ScrollView scrollablContent;



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
    private String end_otp = "";

/*

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.include_petlover_footer)
    View include_petlover_footer;
*/

    BottomNavigationView bottom_navigation_view;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.include_petlover_header)
    View include_petlover_header;


    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_nanny_cust_name)
    TextView txt_nanny_cust_name;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_invoice_no)
    TextView txt_invoice_no;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_nanny_cust_addrst)
    TextView txt_nanny_cust_addrst;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_nanny_cust_city)
    TextView txt_nanny_cust_city;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_lbl_balance_due)
    TextView txt_lbl_balance_due;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_nanny_cust_landmark)
    TextView txt_nanny_cust_landmark;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_balance_due)
    TextView txt_balance_due;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_nanny_cust_pincode)
    TextView txt_nanny_cust_pincode;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_invoicedate)
    TextView txt_invoicedate;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_cust_name)
    TextView txt_cust_name;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_invoiceterms)
    TextView txt_invoiceterms;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_custaddress_st)
    TextView txt_custaddress_st;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_invoicebilldate)
    TextView txt_invoicebilldate;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_cust_addr_landmark)
    TextView txt_cust_addr_landmark;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_custaddr_pincode)
    TextView txt_custaddr_pincode;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_servtitle)
    TextView txt_servtitle;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_hrs)
    TextView txt_hrs;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_cost)
    TextView txt_cost;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_total)
    TextView txt_total;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_lbl_baldue)
    TextView txt_lbl_baldue;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_baldue)
    TextView txt_baldue;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ll_payment_method)
    LinearLayout ll_payment_method;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_raise_invoice)
    Button btn_raise_invoice;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_submit)
    Button btn_submit;

    String work_status,_id;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rdGroupPayment)
    RadioGroup rdGroupPayment;

    String radioValue = "Online";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp_invoice);
        ButterKnife.bind(this);

        ImageView img_back = include_petlover_header.findViewById(R.id.img_back);
        ImageView img_sos = include_petlover_header.findViewById(R.id.img_sos);
        ImageView img_notification = include_petlover_header.findViewById(R.id.img_notification);
        ImageView img_cart = include_petlover_header.findViewById(R.id.img_cart);
        ImageView img_profile = include_petlover_header.findViewById(R.id.img_profile);
        TextView toolbar_title = include_petlover_header.findViewById(R.id.toolbar_title);
        toolbar_title.setText(getResources().getString(R.string.create_invoice));
        img_sos.setVisibility(View.GONE);
        img_cart.setVisibility(View.GONE);
        img_profile.setVisibility(View.GONE);
        img_notification.setVisibility(View.GONE);


        img_back.setOnClickListener(v -> onBackPressed());

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            appointment_id = extras.getString("appointment_id");

            from = extras.getString("fromactivity");

            Log.w(TAG,"appointment_id : "+appointment_id+" from : "+from);

        }

        if (new ConnectionDetector(SPInvoiceActivity.this).isNetworkAvailable(SPInvoiceActivity.this)) {
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

                            work_status = response.body().getData().getWork_status();

                            _id = response.body().getData().get_id();

                            spid = response.body().getData().getSp_id().get_id();
                            appointmentid = response.body().getData().getAppointment_UID();
                            userid = response.body().getData().getUser_id().get_id();

                            String img_user = response.body().getData().getSp_id().getProfile_img();

                            String usrname = response.body().getData().getSp_id().getFirst_name();

                            String serv_name = response.body().getData().getService_name();


                            String datetimeslot = response.body().getData().getBooking_time();

                            String service_cost = response.body().getData().getService_amount();

                            String appointment_status = response.body().getData().getAppoinment_status();


                            String order_date = response.body().getData().getDate_and_time();




                            String order_cost = response.body().getData().getService_amount();

                            addr = response.body().getData().getSp_business_info().get(0).getSp_loc();

                            appoinment_status = response.body().getData().getAppoinment_status();

                            start_appointment_status = response.body().getData().getStart_appointment_status();

                            end_appointment_status = response.body().getData().getEnd_appointment_status();

                            start_otp = response.body().getData().getStart_otp();

                            end_otp = response.body().getData().getEnd_otp();

                            List<SPAppointmentDetailsResponse.DataBean.SpBusinessInfoBean> Address = response.body().getData().getSp_business_info();
                            for (int i = 0; i < Address.size(); i++) {
                                usr_image = Address.get(i).getThumbnail_image();

                            }

                            String invoice_no = response.body().getData().getAppointment_UID();

                            String balance_due = response.body().getData().getAddition_amount();

                            String invoicedate = response.body().getData().getBooking_date_time();

                            String cust_name = response.body().getData().getUser_id().getFirst_name();

                            String custaddress_st = response.body().getData().getAddress_text();

                           // String custaddress_st = "";

                            String invoicebilldate = response.body().getData().getDisplay_date();

                            String cust_addr_landmark = response.body().getData().getCity() + " "+response.body().getData().getState();

                            String custaddr_pincode = response.body().getData().getPin_code();

                          /*  String cust_addr_landmark = "";

                            String custaddr_pincode = "";*/



                            String servname = response.body().getData().getService_name();

                            String hrs = response.body().getData().getHrs();

                            String service_amount = response.body().getData().getService_amount();

                            String total_amount = response.body().getData().getTotal_paid_amount();


                            setView(invoice_no,balance_due,invoicedate,cust_name,custaddress_st,invoicebilldate,cust_addr_landmark,custaddr_pincode,servname,hrs,service_amount,total_amount);


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
    private void setView(String invoice_no, String balance_due, String invoicedate, String cust_name, String custaddress_st, String invoicebilldate, String cust_addr_landmark, String custaddr_pincode, String servname, String hrs, String service_amount, String total_amount) {


        if(invoice_no != null && !invoice_no.isEmpty()){

            txt_invoice_no.setText(invoice_no);
        }
        else{
            txt_invoice_no.setText("");
        }

        if(balance_due != null && !balance_due.isEmpty()){

            txt_lbl_balance_due.setVisibility(View.VISIBLE);
            txt_balance_due.setVisibility(View.VISIBLE);
            txt_balance_due.setText(balance_due);
            txt_lbl_baldue.setVisibility(View.VISIBLE);
            txt_baldue.setVisibility(View.VISIBLE);
            txt_baldue.setText(balance_due);
        }
        else{

            txt_lbl_balance_due.setVisibility(View.GONE);
            txt_balance_due.setVisibility(View.GONE);
            txt_lbl_baldue.setVisibility(View.GONE);
            txt_baldue.setVisibility(View.GONE);
        }

        if(invoicedate != null && !invoicedate.isEmpty()){

            txt_invoicedate.setText(invoicedate);
        }
        else{
            txt_invoicedate.setText("");
        }

        if(cust_name != null && !cust_name.isEmpty()){

            txt_cust_name.setText(cust_name);
        }
        else{
            txt_cust_name.setText("");
        }

        if(custaddress_st != null && !custaddress_st.isEmpty()){

            txt_custaddress_st.setText(custaddress_st);
        }
        else{
            txt_custaddress_st.setText("");
        }

        if(invoicebilldate != null && !invoicebilldate.isEmpty()){

            txt_invoicebilldate.setText(invoicebilldate);
        }
        else{
            txt_invoicebilldate.setText("");
        }

        if(cust_addr_landmark != null && !cust_addr_landmark.isEmpty()){

            txt_cust_addr_landmark.setText(cust_addr_landmark);
        }
        else{
            txt_cust_addr_landmark.setText("");
        }

        if(custaddr_pincode != null && !custaddr_pincode.isEmpty()){

            txt_custaddr_pincode.setText(custaddr_pincode);
        }
        else{
            txt_custaddr_pincode.setText("");
        }

        if(servname != null && !servname.isEmpty()){

            txt_servtitle.setText(servname);
        }else{
            txt_servtitle.setText("");
        }


        if(hrs != null && !hrs.isEmpty()){

            txt_hrs.setText(hrs);
        }else{
            txt_hrs.setText("");
        }


        if(service_amount != null && !service_amount.isEmpty()){

            txt_cost.setText("\u20B9 "+service_amount);

        }
        else{
            txt_cost.setText("");
        }

        if(total_amount != null && !total_amount.isEmpty()){

            txt_total.setText("\u20B9 "+service_amount);

        }
        else{
            txt_total.setText("");
        }


        if (work_status!=null&&!work_status.isEmpty()&&work_status.equals("customer paid")){

            ll_payment_method.setVisibility(View.GONE);

            btn_raise_invoice.setVisibility(View.GONE);

            btn_submit.setVisibility(View.VISIBLE);
        }

        else {

            ll_payment_method.setVisibility(View.VISIBLE);

            btn_raise_invoice.setVisibility(View.VISIBLE);

            btn_submit.setVisibility(View.GONE);
        }

        rdGroupPayment.setOnCheckedChangeListener(new  RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb) {

                    // checkedId is the RadioButton selected
                    switch (checkedId) {
                        case R.id.rdonline:
                            // Do Something
                            radioValue="Online";
                              btn_raise_invoice.setVisibility(View.VISIBLE);
                              btn_submit.setVisibility(View.GONE);
                            break;

                        case R.id.rdcod:
                            // Do Something
                            radioValue="Cash";
                            btn_raise_invoice.setVisibility(View.GONE);
                            btn_submit.setVisibility(View.VISIBLE);
                            break;

                    }
                }
            }

        });

        btn_raise_invoice.setOnClickListener(this);

        btn_submit.setOnClickListener(this);


    }

    @SuppressLint({"LongLogTag", "LogNotTimber"})
    private void payStatusUpdateResponseCall() {
        RestApiInterface apiInterface = APIClient.getClient().create(RestApiInterface.class);
        Call<SPAppointmentProcessCompleteResponse> call = apiInterface.payStatusUpdateResponseCall(RestUtils.getContentType(), RaiseInvoiceRequest());
        Log.w(TAG,"SPAppointmentProcessCompleteResponse url  :%s"+" "+ call.request().url().toString());

        call.enqueue(new Callback<SPAppointmentProcessCompleteResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NonNull Call<SPAppointmentProcessCompleteResponse> call, @NonNull Response<SPAppointmentProcessCompleteResponse> response) {

                Log.w(TAG,"SPAppointmentProcessCompleteResponse"+ "--->" + new Gson().toJson(response.body()));

                if (response.body() != null) {
                    if(response.body().getCode() == 200){

                        Toasty.success(getApplicationContext(),""+response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        gotoDashboard();
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
    private RaiseInvoiceRequest RaiseInvoiceRequest() {


        /**
         * _id : 6176a1378dd3e15b142de08e
         * addition_payment_status :
         */

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm aa", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        RaiseInvoiceRequest RaiseInvoiceRequest = new RaiseInvoiceRequest();
        RaiseInvoiceRequest.set_id(appointment_id);
        RaiseInvoiceRequest.setAddition_payment_status("Not Paid");

        Log.w(TAG,"RaiseInvoiceRequest"+ "--->" + new Gson().toJson(RaiseInvoiceRequest));
        return RaiseInvoiceRequest;
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

                        Toasty.success(getApplicationContext(),""+response.body().getMessage(), Toast.LENGTH_SHORT).show();

                        gotoDashboard();
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

    private void gotoDashboard() {

        startActivity(new Intent(getApplicationContext(), ServiceProviderDashboardActivity.class));

    }

    @SuppressLint({"LongLogTag", "LogNotTimber"})
    private AppoinmentCompleteRequest appoinmentCompleteRequest(String id) {
        /*
         * _id : 5fc639ea72fc42044bfa1683
         * completed_at : 23-10-2000 10 : 00 AM
         * appoinment_status : Completed
         * "work_status" : "customer paid"
         * "addition_payment_status": "Paid"
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


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_raise_invoice:
                if (new ConnectionDetector(SPInvoiceActivity.this).isNetworkAvailable(SPInvoiceActivity.this)) {
                    payStatusUpdateResponseCall();
                }
                break;
            case R.id.btn_submit:
                if (new ConnectionDetector(SPInvoiceActivity.this).isNetworkAvailable(SPInvoiceActivity.this)) {
                    appoinmentCompleteResponseCall(appointment_id);
                }
                break;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        gotoDashboard();
    }
}