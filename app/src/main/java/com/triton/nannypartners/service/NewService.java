package com.triton.nannypartners.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.gson.Gson;
import com.triton.nannypartners.R;
import com.triton.nannypartners.api.APIClient;
import com.triton.nannypartners.api.RestApiInterface;
import com.triton.nannypartners.requestpojo.FetchServiceProviderRequest;
import com.triton.nannypartners.requestpojo.SPAcceptRequest;
import com.triton.nannypartners.requestpojo.SPRejectRequest;
import com.triton.nannypartners.requestpojo.SPTimeExistRequest;
import com.triton.nannypartners.responsepojo.FetchServiceProviderResponse;
import com.triton.nannypartners.responsepojo.SPAcceptResponse;
import com.triton.nannypartners.responsepojo.SPRejectResponse;
import com.triton.nannypartners.responsepojo.SPTimeExistResponse;
import com.triton.nannypartners.sessionmanager.SessionManager;
import com.triton.nannypartners.utils.RestUtils;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewService extends Service {

    // declaring object of MediaPlayer
    private MediaPlayer player;

    private String TAG = "NewService";

    private boolean mRunning;

    CountDownTimer timer;

    int delay = 0; // delay for 0 sec.
    int period = 10000; // repeat every 10 sec.
    Timer timers = new Timer();
    String userid;

    @Override
    public void onCreate()
    {
        super.onCreate();
        mRunning = false;
    }

    @Override

    // execution of service will start
    // on calling this method
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (!mRunning) {
            mRunning = true;
           /* // do something
            // creating a media player which
            // will play the audio of Default
            // ringtone in android device
            player = MediaPlayer.create( this, Settings.System.DEFAULT_RINGTONE_URI );

            // providing the boolean
            // value as true to play
            // the audio on loop
            player.setLooping( true );

            // starting the process
            player.start();
*/

            SessionManager session = new SessionManager(getApplicationContext());
            HashMap<String, String> user = session.getProfileDetails();
            userid = user.get(SessionManager.KEY_ID);


            timers.scheduleAtFixedRate(new TimerTask()
            {
                public void run()
                {
                    fetchRequestResponseCall();
                }
            }, delay, period);

        }

        // returns the status
        // of the program
        return START_STICKY;
    }

    @Override

    // execution of the service will
    // stop on calling this method
    public void onDestroy() {
        super.onDestroy();

        // stopping the process
        timers.cancel();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("LongLogTag")
    private void fetchRequestResponseCall() {
        RestApiInterface apiInterface = APIClient.getClient().create(RestApiInterface.class);
        Call<FetchServiceProviderResponse> call = apiInterface.fetchRequestResponseCall(RestUtils.getContentType(), FetchServiceProviderRequest());
        Log.w(TAG,"FetchServiceProviderResponse url  :%s"+" "+ call.request().url().toString());

        call.enqueue(new Callback<FetchServiceProviderResponse>() {
            @Override
            public void onResponse(@NonNull Call<FetchServiceProviderResponse> call, @NonNull Response<FetchServiceProviderResponse> response) {
                Log.w(TAG,"FetchServiceProviderResponse" + new Gson().toJson(response.body()));
                if (response.body() != null) {
                    if (200 == response.body().getCode()) {
                        Toasty.success(getApplicationContext(),response.body().getMessage(), Toast.LENGTH_SHORT, true).show();
                        if(response.body().getData()!=null){

                            FetchServiceProviderResponse.DataBean dataBean = response.body().getData();

                            showPopupayout(dataBean);
                        }


                    } else {

                    }
                }


            }

            @Override
            public void onFailure(@NonNull Call<FetchServiceProviderResponse> call,@NonNull Throwable t) {
                Log.e("FetchServiceProviderResponse flr", "--->" + t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }



    @SuppressLint("LongLogTag")
    private FetchServiceProviderRequest FetchServiceProviderRequest() {

      /*  *
         * sp_id : 6164232765d9a57d7fc957545

*/
        FetchServiceProviderRequest FetchServiceProviderRequest = new FetchServiceProviderRequest();
        FetchServiceProviderRequest.setSp_id(userid);
        Log.w(TAG,"FetchServiceProviderRequest "+ new Gson().toJson(FetchServiceProviderRequest));
        return FetchServiceProviderRequest;
    }


    private void showPopupayout(FetchServiceProviderResponse.DataBean dataBean) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.alert_sp_accept_reject_appointment, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setCancelable(false);

        Button btn_accept = dialogView.findViewById(R.id.btn_accept);

        Button btn_reject = dialogView.findViewById(R.id.btn_reject);

        TextView txt_appointid = dialogView.findViewById(R.id.txt_appointid);

        TextView txt_custname = dialogView.findViewById(R.id.txt_custname);

        TextView txt_servtitle = dialogView.findViewById(R.id.txt_servtitle);

        TextView txt_location = dialogView.findViewById(R.id.txt_location);

        TextView txt_date = dialogView.findViewById(R.id.txt_date);

        TextView txt_time = dialogView.findViewById(R.id.txt_time);

        TextView txt_timer_count = dialogView.findViewById(R.id.txt_timer_count);

        RelativeLayout rl_root = dialogView.findViewById(R.id.rl_root);

        if(dataBean.getAppointment_id() != null) {

            txt_appointid.setText(dataBean.getAppointment_id());

        }

        else {

            txt_appointid.setText("");
        }

        if(dataBean.getCustomer_name() != null) {

            txt_custname.setText(dataBean.getCustomer_name());

        }

        else {

            txt_custname.setText("");
        }

        if(dataBean.getService_name() != null) {

            txt_servtitle.setText(dataBean.getService_name());

        }

        else {

            txt_servtitle.setText("");
        }

        if(dataBean.getLocation() != null) {

            txt_location.setText(dataBean.getLocation());

        }

        else {

            txt_location.setText("");
        }

        if(dataBean.getSelected_date() != null) {

            txt_date.setText(dataBean.getSelected_date());

        }

        else {

            txt_date.setText("");
        }

        if(dataBean.getSelected_time() != null) {

            txt_time.setText(dataBean.getSelected_time());

        }

        else {

            txt_time.setText("");
        }

        String start_time = dataBean.getStart_time();

        String end_time = dataBean.getEnd_time();

        if(start_time != null&&end_time!=null) {


        }

        timer = new CountDownTimer(6000, 1000) {
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            @Override
            public void onTick(long millisUntilFinished) {
                rl_root.setVisibility(View.VISIBLE);
                txt_timer_count.setText(getResources().getString(R.string.resendotp)+" " + String.format("%02d : %02d ",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

            }

            @Override
            public void onFinish() {
                rl_root.setVisibility(View.GONE);

                timer.cancel();

                if(dataBean.get_id()!=null){

                    timeExistResponseCall(dataBean.get_id());
                }


            }
        };
        timer.start();

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(dataBean.get_id()!=null&&dataBean.getAppointment_id()!=null){

                    SPacceptResponseCall(dataBean.get_id(),dataBean.getAppointment_id());
                }

            }
        });

        btn_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(dataBean.get_id()!=null){

                    SPrejectResponseCall(dataBean.get_id());
                }

            }
        });


    }

    @SuppressLint("LongLogTag")
    private void timeExistResponseCall(String id) {
        RestApiInterface apiInterface = APIClient.getClient().create(RestApiInterface.class);
        Call<SPTimeExistResponse> call = apiInterface.timeExistResponseCall(RestUtils.getContentType(), SPTimeExistRequest(id));
        Log.w(TAG,"SPTimeExistResponse url  :%s"+" "+ call.request().url().toString());

        call.enqueue(new Callback<SPTimeExistResponse>() {
            @Override
            public void onResponse(@NonNull Call<SPTimeExistResponse> call, @NonNull Response<SPTimeExistResponse> response) {
                Log.w(TAG,"SPTimeExistResponse" + new Gson().toJson(response.body()));
                if (response.body() != null) {
                    if (200 == response.body().getCode()) {
                        Toasty.success(getApplicationContext(),response.body().getMessage(), Toast.LENGTH_SHORT, true).show();
                        if(response.body().getData()!=null){

                            SPTimeExistResponse.DataBean dataBean = response.body().getData();

                            showTimeExistAlertPopupayout(dataBean);

                        }


                    } else {

                    }
                }


            }

            @Override
            public void onFailure(@NonNull Call<SPTimeExistResponse> call, @NonNull Throwable t) {
                Log.e("SPTimeExistResponse flr", "--->" + t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }



    @SuppressLint("LongLogTag")
    private SPTimeExistRequest SPTimeExistRequest(String id) {

       /* *
         * _id : 6164232765d9a57d7fc957545

*/
        SPTimeExistRequest SPTimeExistRequest = new SPTimeExistRequest();
        SPTimeExistRequest.set_id(id);
        Log.w(TAG,"SPTimeExistRequest "+ new Gson().toJson(SPTimeExistRequest));
        return SPTimeExistRequest;
    }


    private void showTimeExistAlertPopupayout(SPTimeExistResponse.DataBean dataBean) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.alert_sp_appointment_time_session_layout, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setCancelable(false);

        Button btn_back_to_appointment = dialogView.findViewById(R.id.btn_back_to_appointment);


        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        btn_back_to_appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });


    }

    @SuppressLint("LongLogTag")
    private void SPacceptResponseCall(String id, String appointment_id) {
        RestApiInterface apiInterface = APIClient.getClient().create(RestApiInterface.class);
        Call<SPAcceptResponse> call = apiInterface.SPacceptResponseCall(RestUtils.getContentType(), SPAcceptRequest(id,appointment_id));
        Log.w(TAG,"SPAcceptResponse url  :%s"+" "+ call.request().url().toString());

        call.enqueue(new Callback<SPAcceptResponse>() {
            @Override
            public void onResponse(@NonNull Call<SPAcceptResponse> call, @NonNull Response<SPAcceptResponse> response) {
                Log.w(TAG,"SPAcceptResponse" + new Gson().toJson(response.body()));
                if (response.body() != null) {
                    if (200 == response.body().getCode()) {
                        Toasty.success(getApplicationContext(),response.body().getMessage(), Toast.LENGTH_SHORT, true).show();
                        if(response.body().getData()!=null){

                            SPAcceptResponse.DataBean dataBean = response.body().getData();

                            showAcceptAlertPopupayout(dataBean);

                        }


                    } else {

                    }
                }


            }

            @Override
            public void onFailure(@NonNull Call<SPAcceptResponse> call, @NonNull Throwable t) {
                Log.e("SPAcceptResponse flr", "--->" + t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }



    @SuppressLint("LongLogTag")
    private SPAcceptRequest SPAcceptRequest(String id, String appointment_id) {

      /*  *
         * _id : 616d1c89214ef52ba9313d35
         * appointment_id : SP001

*/
        SPAcceptRequest SPAcceptRequest = new SPAcceptRequest();
        SPAcceptRequest.set_id(id);
        SPAcceptRequest.set_id(appointment_id);
        Log.w(TAG,"SPAcceptRequest "+ new Gson().toJson(SPAcceptRequest));
        return SPAcceptRequest;
    }



    private void showAcceptAlertPopupayout(SPAcceptResponse.DataBean dataBean) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.alert_sp_appointment_accept_success_layout, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setCancelable(false);

        Button btn_back_to_appointment = dialogView.findViewById(R.id.btn_back_to_appointment);


        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        btn_back_to_appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });


    }

    @SuppressLint("LongLogTag")
    private void SPrejectResponseCall(String id) {
        RestApiInterface apiInterface = APIClient.getClient().create(RestApiInterface.class);
        Call<SPRejectResponse> call = apiInterface.SPrejectResponseCall(RestUtils.getContentType(), SPRejectRequest(id));
        Log.w(TAG,"SPRejectResponse url  :%s"+" "+ call.request().url().toString());

        call.enqueue(new Callback<SPRejectResponse>() {
            @Override
            public void onResponse(@NonNull Call<SPRejectResponse> call, @NonNull Response<SPRejectResponse> response) {
                Log.w(TAG,"SPRejectResponse" + new Gson().toJson(response.body()));
                if (response.body() != null) {
                    if (200 == response.body().getCode()) {
                        Toasty.success(getApplicationContext(),response.body().getMessage(), Toast.LENGTH_SHORT, true).show();
                        if(response.body().getData()!=null){

                            SPRejectResponse.DataBean dataBean = response.body().getData();

                            showRejectAlertPopupayout(dataBean);

                        }


                    } else {

                    }
                }


            }

            @Override
            public void onFailure(@NonNull Call<SPRejectResponse> call, @NonNull Throwable t) {
                Log.e("SPRejectResponse flr", "--->" + t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }



    @SuppressLint("LongLogTag")
    private SPRejectRequest SPRejectRequest(String id) {

  /*      *
         * _id : 6164232765d9a57d7fc957545
*/

        SPRejectRequest SPRejectRequest = new SPRejectRequest();
        SPRejectRequest.set_id(id);
        Log.w(TAG,"SPRejectRequest "+ new Gson().toJson(SPRejectRequest));
        return SPRejectRequest;
    }



    private void showRejectAlertPopupayout(SPRejectResponse.DataBean dataBean) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
// ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.alert_sp_appointment_reject_layout, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setCancelable(false);

        Button btn_back_to_appointment = dialogView.findViewById(R.id.btn_back_to_appointment);


        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        btn_back_to_appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });


    }
}
