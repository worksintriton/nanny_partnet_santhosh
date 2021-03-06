package com.triton.nannypartners.serviceprovider;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.triton.nannypartners.R;
import com.triton.nannypartners.activity.LoginActivity;

import com.triton.nannypartners.activity.NotificationActivity;

import com.triton.nannypartners.api.APIClient;
import com.triton.nannypartners.api.RestApiInterface;
import com.triton.nannypartners.requestpojo.LogoutRequest;
import com.triton.nannypartners.requestpojo.NotificationCartCountRequest;
import com.triton.nannypartners.responsepojo.NotificationCartCountResponse;
import com.triton.nannypartners.responsepojo.SuccessResponse;
import com.triton.nannypartners.serviceprovider.shop.SPCartActivity;
import com.triton.nannypartners.serviceprovider.shop.SPMyOrdrersActivity;
import com.triton.nannypartners.serviceprovider.shop.SPProductsFavActivity;
import com.triton.nannypartners.sessionmanager.SessionManager;
import com.triton.nannypartners.utils.ConnectionDetector;
import com.triton.nannypartners.utils.RestUtils;

import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ServiceProviderNavigationDrawer extends AppCompatActivity implements View.OnClickListener {

    String TAG = "ServiceProviderNavigationDrawer";

    public NavigationView navigationView;
    private DrawerLayout drawerLayout;
    LayoutInflater inflater;
    View view, header;
    Toolbar toolbar;


    ImageView drawerImg;
    CircleImageView nav_header_imageView;
    FrameLayout frameLayout;
    TextView nav_header_profilename, nav_header_emailid,nav_header_ref_code, nav_header_edit;
    //SessionManager session;
    String name, image_url, phoneNo;


    public TextView tvWelcomeName;

    public Menu menu;

    BroadcastReceiver imgReceiver;


    SessionManager session;


    String emailid = "";
    private Dialog dialog;
    private String refcode;

    TextView txt_notification_count_badge;
    TextView txt_cart_count_badge;
    private String userid;

    @SuppressLint({"InflateParams", "LongLogTag"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_NoActionBar);
        Log.w(TAG, "onCreate---->");

        inflater = LayoutInflater.from(this);
        view = inflater.inflate(R.layout.navigation_drawer_sp_layout, null);
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getProfileDetails();
        userid = user.get(SessionManager.KEY_ID);
        name = user.get(SessionManager.KEY_FIRST_NAME);
        emailid = user.get(SessionManager.KEY_EMAIL_ID);
        phoneNo = user.get(SessionManager.KEY_MOBILE);
        refcode = user.get(SessionManager.KEY_REF_CODE);



        String userid = user.get(SessionManager.KEY_ID);
        Log.w(TAG, "userid : " + userid+"refcode : "+refcode);


        Log.w(TAG, "user details--->" + "name :" + " " + name + " " + "image_url :" + image_url);

        initUI(view);
        initToolBar(view);


        // myBoradcastReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (imgReceiver != null) {
            unregisterReceiver(imgReceiver);
        }
    }

    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    private void initUI(View view) {

        //Initializing NavigationView
        navigationView = view.findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);

        frameLayout = view.findViewById(R.id.base_container);


        menu = navigationView.getMenu();


        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = view.findViewById(R.id.drawer_layout);
        header = navigationView.getHeaderView(0);
        nav_header_imageView = header.findViewById(R.id.nav_header_imageView);
        nav_header_emailid = header.findViewById(R.id.nav_header_emailid);
        nav_header_profilename = header.findViewById(R.id.nav_header_profilename);
        nav_header_edit = header.findViewById(R.id.nav_header_edit);
        // Glide.with(this).load(image_url).into(nav_header_imageView);
        nav_header_ref_code = view.findViewById(R.id.nav_header_ref_code);
        if(refcode != null && !refcode.isEmpty() ){
            nav_header_ref_code.setVisibility(View.VISIBLE);
            nav_header_ref_code.setText(getResources().getString(R.string.ref_code)+" : "+refcode);
        }else{
            nav_header_ref_code.setVisibility(View.GONE);
            nav_header_ref_code.setText("");
        }

        nav_header_emailid.setText(emailid);
        nav_header_profilename.setText(name);
        RelativeLayout llheader = header.findViewById(R.id.llheader);
        llheader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SPProfileScreenActivity.class));
            }
        });

        TextView nav_header_edit = header.findViewById(R.id.nav_header_edit);
        nav_header_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SPEditProfileActivity.class));
            }
        });



        navigationView.setNavigationItemSelectedListener(menuItem -> {
            //Closing drawer on item click
            drawerLayout.closeDrawers();
            //Check to see which item was being clicked and perform appropriate action
            switch (menuItem.getItemId()) {


                //Replacing the main content with ContentFragment Which is our Inbox View;
                case R.id.nav_item_one:
                     gotoSPDashboard();
                    return true;

                // For rest of the options we just show a toast on click
                case R.id.nav_item_two:
                    gotoMyCalendar();
                    return true;

                case R.id.nav_item_three:
                    gotoManageServices();
                    return true;

                case R.id.nav_item_four:
                    gotoMyOrders();
                    return true;

                case R.id.nav_item_five:
                    gotoFavourites();
                    return true;

                case R.id.nav_item_six:
                    return true;
               case R.id.nav_item_seven:
                   gotoNotifications();
                   return true;
                case R.id.nav_item_eight:
                    //confirmLogoutDialog();
                    showLogOutAppAlert();
                    return true;
                case R.id.nav_item_nine:
                    //confirmLogoutDialog();
                    gotoTransactionHistory();
                    return true;


                default:
                    return true;

            }
        });


    }

    private void gotoTransactionHistory() {
        Intent intent = new Intent(getApplicationContext(), TransactionHistoryActivity.class);
        intent.putExtra("fromactivity", TAG);
        startActivity(intent);
    }

    private void gotoSPDashboard() {
        Intent intent = new Intent(getApplicationContext(),ServiceProviderDashboardActivity.class);
        intent.putExtra("fromactivity",TAG);
        startActivity(intent);
    }

    private void gotoNotifications() {
        Intent intent = new Intent(getApplicationContext(),NotificationActivity.class);
        intent.putExtra("fromactivity",TAG);
        startActivity(intent);
    }
    private void gotoFavourites() {
        Intent intent = new Intent(getApplicationContext(), SPProductsFavActivity.class);
        startActivity(intent);
    }

    private void gotoMyOrders() {
        Intent intent = new Intent(getApplicationContext(), SPMyOrdrersActivity.class);
        startActivity(intent);
    }

    private void gotoMyCalendar() {
        Intent i = new Intent(ServiceProviderNavigationDrawer.this, SPMyCalendarActivity.class);
        startActivity(i);
    }


    @SuppressLint("SetTextI18n")
    private void initToolBar(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerImg = toolbar.findViewById(R.id.img_menu);

        ImageView img_cart = toolbar.findViewById(R.id.img_cart);
        ImageView img_notification = toolbar.findViewById(R.id.img_notification);
        txt_notification_count_badge = toolbar.findViewById(R.id.txt_notification_count_badge);
        txt_cart_count_badge = toolbar.findViewById(R.id.txt_cart_count_badge);
        txt_notification_count_badge.setVisibility(View.GONE);
        txt_cart_count_badge.setVisibility(View.GONE);


//        tvWelcomeName = toolbar.findViewById(R.id.toolbar_title);
//
//        tvWelcomeName.setText("Home");


        img_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),NotificationActivity.class));
            }
        });
        img_cart.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onClick(View v) {
                if(ServiceProviderDashboardActivity.active_tag != null){
                    Log.w(TAG,"active_tag : "+ServiceProviderDashboardActivity.active_tag);
                }
                startActivity(new Intent(getApplicationContext(), SPCartActivity.class));
            }
        });

        if (new ConnectionDetector(getApplicationContext()).isNetworkAvailable(getApplicationContext())) {
            notificationandCartCountResponseCall();
        }



        toggleView();
    }

    @SuppressLint("LogNotTimber")
    private void notificationandCartCountResponseCall() {
       /* avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();*/

        RestApiInterface apiInterface = APIClient.getClient().create(RestApiInterface.class);
        Call<NotificationCartCountResponse> call = apiInterface.notificationandCartCountResponseCall(RestUtils.getContentType(), notificationCartCountRequest());
        Log.w(TAG,"NotificationCartCountResponse url  :%s"+" "+ call.request().url().toString());

        call.enqueue(new Callback<NotificationCartCountResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<NotificationCartCountResponse> call, @NonNull Response<NotificationCartCountResponse> response) {

                Log.w(TAG,"NotificationCartCountResponse"+ "--->" + new Gson().toJson(response.body()));

                // avi_indicator.smoothToHide();

                if (response.body() != null) {
                    if(response.body().getCode() == 200) {
                        if(response.body().getData()!=null){
                            int Notification_count = response.body().getData().getNotification_count();
                            int Product_count = response.body().getData().getProduct_count();
                            if(Notification_count != 0){
                                txt_notification_count_badge.setVisibility(View.VISIBLE);
                                txt_notification_count_badge.setText(""+Notification_count);
                            }else{
                                txt_notification_count_badge.setVisibility(View.GONE);
                            }
                            if(Product_count != 0){
                                txt_cart_count_badge.setVisibility(View.VISIBLE);
                                txt_cart_count_badge.setText(""+Product_count);
                            }else{
                                txt_cart_count_badge.setVisibility(View.GONE);
                            }


                        }
                    }



                }




            }

            @Override
            public void onFailure(@NonNull Call<NotificationCartCountResponse> call, @NonNull Throwable t) {

                // avi_indicator.smoothToHide();
                Log.w(TAG,"NotificationCartCountResponse flr"+"--->" + t.getMessage());
            }
        });


    }
    @SuppressLint("LogNotTimber")
    private NotificationCartCountRequest notificationCartCountRequest() {
        /*
         * user_id : 6048589d0b3a487571a1c567
         */

        NotificationCartCountRequest notificationCartCountRequest = new NotificationCartCountRequest();
        notificationCartCountRequest.setUser_id(userid);
        Log.w(TAG,"notificationCartCountRequest"+ "--->" + new Gson().toJson(notificationCartCountRequest));
        return notificationCartCountRequest;
    }


    private void toggleView() {
        drawerImg.setOnClickListener(v -> {
            if (v.isClickable()) {
                drawerMethod();
            } else {

                Intent intent_re = getIntent();
                overridePendingTransition(0, 0);
                intent_re.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent_re);

            }
        });
    }

    public void drawerMethod() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);

        } else {
            drawerLayout.openDrawer(GravityCompat.START);
        }

    }

    public void setContentView(int layoutId) {

        Log.e("BaseOncreate", "setContentView");
        View activityView = inflater.inflate(layoutId, null);
        frameLayout.addView(activityView);
        super.setContentView(view);
        //drawerMethod();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_menu) {
            drawerMethod();
        }
    }


    private void confirmLogoutDialog() {

        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(ServiceProviderNavigationDrawer.this);
        alertDialogBuilder.setMessage("Are you sure want to logout?");
        alertDialogBuilder.setPositiveButton("yes",
                (arg0, arg1) -> gotoLogout());

        alertDialogBuilder.setNegativeButton("No", (dialog, which) -> alertDialogBuilder.setCancelable(true));

        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }


    private void gotoManageServices() {

        Intent i = new Intent(ServiceProviderNavigationDrawer.this, SPProfileScreenActivity.class);
        startActivity(i);
    }


    private void gotoLogout() {

        if (new ConnectionDetector(getApplicationContext()).isNetworkAvailable(getApplicationContext())) {

            logoutResponseCall();
        }
    }

    private void showLogOutAppAlert() {
        try {

            dialog = new Dialog(ServiceProviderNavigationDrawer.this);
            dialog.setContentView(R.layout.alert_logout_layout);
            Button btn_no = dialog.findViewById(R.id.btn_no);
            Button btn_yes = dialog.findViewById(R.id.btn_yes);

            btn_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    gotoLogout();

                }
            });
            btn_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        }




    }

    @SuppressLint("LogNotTimber")
    private void logoutResponseCall() {
        RestApiInterface apiInterface = APIClient.getClient().create(RestApiInterface.class);
        Call<SuccessResponse> call = apiInterface.logoutResponseCall(RestUtils.getContentType(), logoutRequest());
        Log.w(TAG,"SignupResponse url  :%s"+" "+ call.request().url().toString());
        call.enqueue(new Callback<SuccessResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull Response<SuccessResponse> response) {
                Log.w(TAG,"SuccessResponse" + new Gson().toJson(response.body()));
                if (response.body() != null) {
                    if (200 == response.body().getCode()) {
                        session.logoutUser();
                        session.setIsLogin(false);
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();


                    }
                }


            }

            @Override
            public void onFailure(@NonNull Call<SuccessResponse> call,@NonNull Throwable t) {

                Log.e("SuccessResponse flr", "--->" + t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private LogoutRequest logoutRequest() {
        LogoutRequest defaultLocationRequest = new LogoutRequest();
        defaultLocationRequest.setUser_id(userid);

        Log.w(TAG,"LogoutRequest "+ new Gson().toJson(logoutRequest()));
        return logoutRequest();
    }




}