package com.triton.nannypartners.serviceprovider;

import android.Manifest;
import android.annotation.SuppressLint;

import android.app.Activity;
import android.app.Dialog;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;

import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.triton.nannypartners.R;

import com.triton.nannypartners.api.API;
import com.triton.nannypartners.api.APIClient;
import com.triton.nannypartners.api.RestApiInterface;

import com.triton.nannypartners.fragmentserviceprovider.FragmentSPDashboard;
import com.triton.nannypartners.fragmentserviceprovider.SPCommunityFragment;
import com.triton.nannypartners.fragmentserviceprovider.SPShopFragment;
import com.triton.nannypartners.requestpojo.FetchServiceProviderRequest;
import com.triton.nannypartners.requestpojo.SPAcceptRequest;
import com.triton.nannypartners.requestpojo.SPRejectRequest;
import com.triton.nannypartners.requestpojo.SPTimeExistRequest;
import com.triton.nannypartners.requestpojo.ShippingAddressFetchByUserIDRequest;
import com.triton.nannypartners.responsepojo.FetchServiceProviderResponse;
import com.triton.nannypartners.responsepojo.GetAddressResultResponse;
import com.triton.nannypartners.responsepojo.SPAcceptResponse;
import com.triton.nannypartners.responsepojo.SPRejectResponse;
import com.triton.nannypartners.responsepojo.SPTimeExistResponse;
import com.triton.nannypartners.responsepojo.ShippingAddressFetchByUserIDResponse;
import com.triton.nannypartners.service.GPSTracker;
import com.triton.nannypartners.service.NewService;
import com.triton.nannypartners.sessionmanager.SessionManager;

import com.triton.nannypartners.utils.RestUtils;
import com.wang.avi.AVLoadingIndicatorView;


import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

import java.util.HashMap;

import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ServiceProviderDashboardActivity  extends ServiceProviderNavigationDrawer implements Serializable,
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private String TAG = "ServiceProviderDashboardActivity";


    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.avi_indicator)
    AVLoadingIndicatorView avi_indicator;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.include_doctor_footer)
    View include_doctor_footer;


    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_location)
    TextView txt_location;


    final Fragment fragmentSPDashboard = new FragmentSPDashboard();
    final Fragment sPShopFragment = new SPShopFragment();
    final Fragment spCommunityFragment = new SPCommunityFragment();



    public static String active_tag = "1";


    Fragment active = fragmentSPDashboard;
    String tag;

    String fromactivity;
    private int reviewcount;
    private String specialization;

    private static final int REQUEST_CHECK_SETTINGS_GPS = 0x1;
    private GoogleApiClient googleApiClient;
    Location mLastLocation;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private double latitude;
    private double longitude;
    public static String cityName;
    private Dialog dialog;
    private String userid;

    /* Bottom Navigation */

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rl_home)
    RelativeLayout rl_home;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rl_shop)
    RelativeLayout rl_shop;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.title_shop)
    TextView title_shop;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.img_shop)
    ImageView img_shop;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rl_comn)
    RelativeLayout rl_comn;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.title_community)
    TextView title_community;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.img_community)
    ImageView img_community;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rl_homes)
    RelativeLayout rl_homes;

    public static String appintments;
    private int someIndex = 0;
    CountDownTimer timer;

    int delay = 0; // delay for 0 sec.
    int period = 10000; // repeat every 10 sec.
    Timer timers = new Timer();

    boolean isdata=false;

    @SuppressLint("LogNotTimber")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_provider_dashboard);
        ButterKnife.bind(this);
        Log.w(TAG, "lifecycle : onCreate-->");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            appintments = bundle.getString("appintments");
            Log.w(TAG,"appintments : "+appintments);
        }


//        bottom_navigation_view = include_doctor_footer.findViewById(R.id.bottom_navigation_view);
//        bottom_navigation_view.setItemIconTintList(null);
//        bottom_navigation_view.getMenu().findItem(R.id.home).setChecked(true);
//        bottom_navigation_view.setOnNavigationItemSelectedListener(this);


        /*home*/

        title_shop.setTextColor(getResources().getColor(R.color.darker_grey_new,getTheme()));
        img_shop.setImageResource(R.drawable.grey_shop);
        title_community.setTextColor(getResources().getColor(R.color.darker_grey_new,getTheme()));
        img_community.setImageResource(R.drawable.grey_community);


        rl_home.setOnClickListener(this);

        rl_shop.setOnClickListener(this);

        rl_comn.setOnClickListener(this);


        rl_homes.setOnClickListener(this);



        googleApiConnected();



        avi_indicator.setVisibility(View.GONE);


        SessionManager session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getProfileDetails();
        userid = user.get(SessionManager.KEY_ID);

      /*  if (new ConnectionDetector(getApplicationContext()).isNetworkAvailable(getApplicationContext())) {
            shippingAddressresponseCall();
        }*/

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            fromactivity = extras.getString("fromactivity");
            reviewcount = extras.getInt("reviewcount");
            specialization = extras.getString("specialization");


        }

        tag = getIntent().getStringExtra("tag");
        Log.w(TAG," tag : "+tag);
        if(tag != null){
            if(tag.equalsIgnoreCase("1")){
                active = fragmentSPDashboard;
//                bottom_navigation_view.getMenu().findItem(R.id.home).setChecked(true);
                title_shop.setTextColor(getResources().getColor(R.color.darker_grey_new,getTheme()));
                img_shop.setImageResource(R.drawable.grey_shop);
                title_community.setTextColor(getResources().getColor(R.color.darker_grey_new,getTheme()));
                img_community.setImageResource(R.drawable.grey_community);
                loadFragment(new FragmentSPDashboard());
            }else if(tag.equalsIgnoreCase("2")){
                active = sPShopFragment;
//                bottom_navigation_view.getMenu().findItem(R.id.shop).setChecked(true);
                title_community.setTextColor(getResources().getColor(R.color.darker_grey_new,getTheme()));
                img_community.setImageResource(R.drawable.grey_community);
                title_shop.setTextColor(getResources().getColor(R.color.new_gree_color,getTheme()));
                img_shop.setImageResource(R.drawable.green_shop);
                loadFragment(new SPShopFragment());
            } else if(tag.equalsIgnoreCase("3")){
//                bottom_navigation_view.getMenu().findItem(R.id.community).setChecked(true);
                active=spCommunityFragment;
                title_shop.setTextColor(getResources().getColor(R.color.darker_grey_new,getTheme()));
                img_shop.setImageResource(R.drawable.grey_shop);
                title_community.setTextColor(getResources().getColor(R.color.new_gree_color,getTheme()));
                img_community.setImageResource(R.drawable.green_comm);
                startActivity(new Intent(getApplicationContext(), SPProfileScreenActivity.class));
            }
        }
        else{
//            bottom_navigation_view.getMenu().findItem(R.id.home).setChecked(true);
            title_shop.setTextColor(getResources().getColor(R.color.darker_grey_new,getTheme()));
            img_shop.setImageResource(R.drawable.grey_shop);
            title_community.setTextColor(getResources().getColor(R.color.darker_grey_new,getTheme()));
            img_community.setImageResource(R.drawable.grey_community);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_schedule, active, active_tag);
            transaction.commitNowAllowingStateLoss();
        }

        txt_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ManageAddressSPActivity.class));
            }
        });


    }


    private void loadFragment(Fragment fragment) {
        Bundle bundle = new Bundle();
        if (fromactivity != null) {
            Log.w(TAG, "fromactivity loadFragment : " + fromactivity);

            if (fromactivity.equalsIgnoreCase("FiltersActivity")) {
                bundle.putString("fromactivity", fromactivity);
                bundle.putString("specialization", specialization);
                bundle.putInt("reviewcount", reviewcount);
                // set Fragmentclass Arguments
                fragment.setArguments(bundle);
                Log.w(TAG, "fromactivity : " + fromactivity);
                // load fragment
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_schedule, fragment);
                transaction.addToBackStack(null);
                transaction.commitAllowingStateLoss();
            }
        } else {

            // set Fragmentclass Arguments
            fragment.setArguments(bundle);

            // load fragment
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_schedule, fragment);
            transaction.addToBackStack(null);
            transaction.commitAllowingStateLoss();
        }
    }

    @Override
    public void onBackPressed() {
        Log.w(TAG, "tag : " + tag);
//        if (bottom_navigation_view.getSelectedItemId() == R.id.home) {
            showExitAppAlert();
          /*  new android.app.AlertDialog.Builder(PetLoverDashboardActivity.this)
                    .setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id) -> PetLoverDashboardActivity.this.finishAffinity())
                    .setNegativeButton("No", null)
                    .show();*/
//        } else if (tag != null) {
//            Log.w(TAG, "Else IF--->" + "fromactivity : " + fromactivity);
//            if (fromactivity != null) {
//
//
//            } else {
//                bottom_navigation_view.setSelectedItemId(R.id.home);
//                // load fragment
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.frame_schedule, new FragmentSPDashboard());
//                transaction.commitNowAllowingStateLoss();
//            }
//
//
//        } else {
//            bottom_navigation_view.setSelectedItemId(R.id.home);
//            // load fragment
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            transaction.replace(R.id.frame_schedule, new FragmentSPDashboard());
//            transaction.commitNowAllowingStateLoss();
//        }

    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_schedule, fragment);
        transaction.commitNowAllowingStateLoss();
    }

//    @SuppressLint("NonConstantResourceId")
//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//        switch (item.getItemId()) {
//            case R.id.home:
//                active_tag = "1";
//                replaceFragment(new FragmentSPDashboard());
//                break;
//            case R.id.shop:
//                active_tag = "2";
//                replaceFragment(new SPShopFragment());
//                break;
//
//            case R.id.community:
//                showComingSoonAlert();
//                active_tag = "3";
//                break;
//
//            default:
//                return false;
//        }
//        return true;
//    }

    @SuppressLint("LogNotTimber")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS_GPS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        getMyLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        getMyLocation();
                        break;
                }
                break;
        }



        Fragment fragment = Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.frame_schedule));
        fragment.onActivityResult(requestCode,resultCode,data);
    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String @NotNull [] permissions, @NotNull int @NotNull [] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    getMyLocation();

                }
            } else {
                Toast.makeText(getApplicationContext(), "permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void getLatandLong() {
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ServiceProviderDashboardActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            } else {
                GPSTracker gps = new GPSTracker(getApplicationContext());
                // Check if GPS enabled
                if (gps.canGetLocation()) {
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();

                    if(latitude != 0 && longitude != 0){
                        LatLng latLng = new LatLng(latitude,longitude);
                        getAddressResultResponse(latLng);


                    }




                }
            }




        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void googleApiConnected() {

        googleApiClient = new GoogleApiClient.Builder(Objects.requireNonNull(getApplicationContext())).
                addConnectionCallbacks(this).
                addOnConnectionFailedListener(this).
                addApi(LocationServices.API).build();
        googleApiClient.connect();

    }
    private void checkLocation() {
        try {
            LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            boolean gps_enabled = false;
            boolean network_enabled = false;

            try {
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch (Exception ignored) {
            }

            try {
                network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            } catch (Exception ignored) {
            }

            if (!gps_enabled && !network_enabled) {

                if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    getMyLocation();
                }

            } else {
                getLatandLong();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        latitude = mLastLocation.getLatitude();
        longitude = mLastLocation.getLongitude();







    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        permissionChecking();
    }
    @Override
    public void onConnectionSuspended(int i) {

    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @SuppressLint("LongLogTag")
    @Override
    public void onMapReady(GoogleMap googleMap) {


    }
    private void permissionChecking() {
        if (getApplicationContext() != null) {
            if (Build.VERSION.SDK_INT >= 23 && (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) &&
                    (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {

                ActivityCompat.requestPermissions(ServiceProviderDashboardActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 5);

            } else {

                checkLocation();
            }
        }
    }
    public void getMyLocation() {
        if (googleApiClient != null) {

            if (googleApiClient.isConnected()) {
                if(getApplicationContext() != null){
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }

                }

                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                LocationRequest locationRequest = new LocationRequest();
                locationRequest.setInterval(2000);
                locationRequest.setFastestInterval(2000);
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
                builder.setAlwaysShow(true);
                LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
                PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
                result.setResultCallback(result1 -> {
                    Status status = result1.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            // All location settings are satisfied.
                            // You can initialize location requests here.
                            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);







                            Handler handler = new Handler();
                            int delay = 1000; //milliseconds

                            handler.postDelayed(new Runnable() {
                                @SuppressLint({"LongLogTag", "LogNotTimber"})
                                public void run() {
                                    //do something
                                    if(getApplicationContext() != null) {
                                        if(latitude != 0 && longitude != 0) {
                                            LatLng latLng = new LatLng(latitude,longitude);
                                            getAddressResultResponse(latLng);

                                        }
                                    }
                                }
                            }, delay);


                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                status.startResolutionForResult(ServiceProviderDashboardActivity.this, REQUEST_CHECK_SETTINGS_GPS);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            break;
                    }
                });
            }


        }
    }
    private void getAddressResultResponse(LatLng latLng) {
        //avi_indicator.setVisibility(View.VISIBLE);
        // avi_indicator.smoothToShow();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        API service = retrofit.create(API.class);
        String strlatlng = String.valueOf(latLng);
        String newString = strlatlng.replace("lat/lng:", "");

        String latlngs = newString.trim().replaceAll("\\(", "").replaceAll("\\)","").trim();



        String key = API.MAP_KEY;
        service.getAddressResultResponseCall(latlngs, key).enqueue(new Callback<GetAddressResultResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NotNull Call<GetAddressResultResponse> call, @NotNull Response<GetAddressResultResponse> response) {
                //avi_indicator.smoothToHide();
                Log.w(TAG,"url  :%s"+ call.request().url().toString());




                if(response.body() != null) {
                    String currentplacename = null;
                    String compundcode = null;

                    if(response.body().getPlus_code().getCompound_code() != null){
                        compundcode = response.body().getPlus_code().getCompound_code();
                    }
                    if(compundcode != null) {
                        String[] separated = compundcode.split(",");
                        String placesname = separated[0];
                        String[] splitData = placesname.split("\\s", 2);
                        String code = splitData[0];
                        currentplacename = splitData[1];
                        Log.w(TAG,"currentplacename : "+currentplacename);
                    }




                    String localityName = null;
                    String sublocalityName = null;
                    String CityName = null;
                    String postalCode;


                    List<GetAddressResultResponse.ResultsBean> getAddressResultResponseList;
                    getAddressResultResponseList = response.body().getResults();
                    if (getAddressResultResponseList.size() > 0) {
                        String AddressLine = getAddressResultResponseList.get(0).getFormatted_address();

                    }
                    List<GetAddressResultResponse.ResultsBean.AddressComponentsBean> addressComponentsBeanList = response.body().getResults().get(0).getAddress_components();
                    if(addressComponentsBeanList != null) {
                        if (addressComponentsBeanList.size() > 0) {
                            for (int i = 0; i < addressComponentsBeanList.size(); i++) {

                                for (int j = 0; j < addressComponentsBeanList.get(i).getTypes().size(); j++) {

                                    List<String> typesList = addressComponentsBeanList.get(i).getTypes();

                                    if (typesList.contains("postal_code")) {
                                        postalCode = addressComponentsBeanList.get(i).getShort_name();
                                        String PostalCode = postalCode;

                                    }
                                    if (typesList.contains("locality")) {
                                        CityName = addressComponentsBeanList.get(i).getLong_name();
                                        localityName = addressComponentsBeanList.get(i).getShort_name();
                                        Log.w(TAG,"CityName : "+CityName+"localityName : "+localityName);


                                    }

                                    if(currentplacename != null){
                                        txt_location.setText(currentplacename);
                                    }else if(CityName != null){
                                        txt_location.setText(CityName);
                                    }else if(localityName != null){
                                        txt_location.setText(localityName);
                                    }else{
                                        txt_location.setText("");
                                    }

                                    if (typesList.contains("administrative_area_level_2")) {
                                        cityName = addressComponentsBeanList.get(i).getShort_name();
                                        //  CityName = cityName;




                                    }
                                    if (typesList.contains("sublocality_level_1")) {
                                        sublocalityName = addressComponentsBeanList.get(i).getShort_name();
                                        Log.w(TAG,"sublocalityName : "+sublocalityName);

                                    }

                                }

                            }





                        }
                    }
                }


            }

            @Override
            public void onFailure(@NotNull Call<GetAddressResultResponse> call, @NotNull Throwable t) {
                //avi_indicator.smoothToHide();
                t.printStackTrace();
            }
        });
    }


    private void showExitAppAlert() {
        try {

            dialog = new Dialog(ServiceProviderDashboardActivity.this);
            dialog.setContentView(R.layout.alert_exit_layout);
            Button btn_cancel = dialog.findViewById(R.id.btn_cancel);
            Button btn_exit = dialog.findViewById(R.id.btn_exit);

            btn_exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    ServiceProviderDashboardActivity.this.finishAffinity();
                }
            });
            btn_cancel.setOnClickListener(new View.OnClickListener() {
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
    private void showComingSoonAlert() {

        try {

            Dialog dialog = new Dialog(ServiceProviderDashboardActivity.this);
            dialog.setContentView(R.layout.alert_comingsoon_layout);
            dialog.setCanceledOnTouchOutside(false);

            ImageView img_close = dialog.findViewById(R.id.img_close);
            img_close.setOnClickListener(new View.OnClickListener() {
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
    private void shippingAddressresponseCall() {
        /* avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();*/
        RestApiInterface apiInterface = APIClient.getClient().create(RestApiInterface.class);
        Call<ShippingAddressFetchByUserIDResponse> call = apiInterface.fetch_shipp_addr_ResponseCall(RestUtils.getContentType(), shippingAddressFetchByUserIDRequest());
        Log.w(TAG, "ShippingAddressFetchByUserIDResponse url  :%s" + " " + call.request().url().toString());
        call.enqueue(new Callback<ShippingAddressFetchByUserIDResponse>() {
            @Override
            public void onResponse(@NonNull Call<ShippingAddressFetchByUserIDResponse> call, @NonNull Response<ShippingAddressFetchByUserIDResponse> response) {
                Log.w(TAG, "ShippingAddressFetchByUserIDResponse" + "--->" + new Gson().toJson(response.body()));
                //  avi_indicator.smoothToHide();
                if (response.body() != null) {
                    if (response.body().getCode() == 200) {
                        if (response.body().getData() != null) {
                            ShippingAddressFetchByUserIDResponse.DataBean dataBeanList = response.body().getData();

                            if (dataBeanList != null) {
                                if (dataBeanList.isDefault_status()) {
                                    Log.w(TAG, "true-->");
                                    String city = dataBeanList.getLocation_city();
                                    if (city != null) {
                                        //txt_location.setText(city);
                                    }

                                }


                            }

                        }
                    }


                }


            }

            @Override
            public void onFailure(@NonNull Call<ShippingAddressFetchByUserIDResponse> call, @NonNull Throwable t) {

                //  avi_indicator.smoothToHide();
                Log.w(TAG, "ShippingAddressFetchByUserIDResponse flr" + "--->" + t.getMessage());
            }
        });


    }

    @SuppressLint("LogNotTimber")
    private ShippingAddressFetchByUserIDRequest shippingAddressFetchByUserIDRequest() {
        /*
         * user_id : 6048589d0b3a487571a1c567
         */

        ShippingAddressFetchByUserIDRequest shippingAddressFetchByUserIDRequest = new ShippingAddressFetchByUserIDRequest();
        shippingAddressFetchByUserIDRequest.setUser_id(userid);

        Log.w(TAG, "shippingAddressFetchByUserIDRequest" + "--->" + new Gson().toJson(shippingAddressFetchByUserIDRequest));
        return shippingAddressFetchByUserIDRequest;
    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.rl_homes:
                active_tag = "1";
                title_shop.setTextColor(getResources().getColor(R.color.darker_grey_new,getTheme()));
                img_shop.setImageResource(R.drawable.grey_shop);
                title_community.setTextColor(getResources().getColor(R.color.darker_grey_new,getTheme()));
                img_community.setImageResource(R.drawable.grey_community);
                replaceFragment(new FragmentSPDashboard());
                break;

            case R.id.rl_home:
                active_tag = "1";
                title_shop.setTextColor(getResources().getColor(R.color.darker_grey_new,getTheme()));
                img_shop.setImageResource(R.drawable.grey_shop);
                title_community.setTextColor(getResources().getColor(R.color.darker_grey_new,getTheme()));
                img_community.setImageResource(R.drawable.grey_community);
                replaceFragment(new FragmentSPDashboard());
                break;

            case R.id.rl_shop:
                active_tag = "2";
                title_community.setTextColor(getResources().getColor(R.color.darker_grey_new,getTheme()));
                img_community.setImageResource(R.drawable.grey_community);
                title_shop.setTextColor(getResources().getColor(R.color.new_gree_color,getTheme()));
                img_shop.setImageResource(R.drawable.green_shop);
                replaceFragment(new SPShopFragment());
                break;

            case R.id.rl_comn:
                active_tag = "3";
                title_shop.setTextColor(getResources().getColor(R.color.darker_grey_new,getTheme()));
                img_shop.setImageResource(R.drawable.grey_shop);
                title_community.setTextColor(getResources().getColor(R.color.new_gree_color,getTheme()));
                img_community.setImageResource(R.drawable.green_comm);
                startActivity(new Intent(getApplicationContext(), SPProfileScreenActivity.class));
                break;
        }


    }



    private void gotoSPAppointments() {

        startActivity(getIntent());
    }

    @Override
    protected void onStart() {
        super.onStart();
        // starting the service
        Log.w(TAG, "lifecycle : onStart : " );
        SessionManager session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getProfileDetails();
        userid = user.get(SessionManager.KEY_ID);


        timers.scheduleAtFixedRate(new TimerTask()
        {
            public void run()
            {
                if(!isdata){

                    fetchRequestResponseCall();
                }


            }
        }, delay, period);


    }

    @Override
    protected void onPause() {
        super.onPause();
        // stopping the service
        Log.w(TAG, "lifecycle : onPause : " );
        isdata=true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w(TAG, "lifecycle : OnResume");
      isdata=false;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w(TAG, "lifecycle : onDestroy");
      timers.cancel();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.w(TAG, "lifecycle : onStop");
        isdata=true;
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
                        if(response.body().getData()!=null){

                            FetchServiceProviderResponse.DataBean dataBean = response.body().getData();

                            if(dataBean.get_id()!=null&&!dataBean.get_id().isEmpty()){

                                isdata=true;
                                showPopupayout(dataBean);
                            }


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
         *
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

        Log.w(TAG,"end_time :" +end_time);

/*
        if(end_time!=null) {

            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = inputFormat.parse(end_time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String formattedDate = outputFormat.format(date);

            Log.w(TAG,"formattedDate :" +formattedDate);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());

            String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

            Log.w(TAG,"currentTime :" +currentTime);


        }*/
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        timer = new CountDownTimer(40000, 1000) {
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            @Override
            public void onTick(long millisUntilFinished) {
                rl_root.setVisibility(View.VISIBLE);
                txt_timer_count.setText(String.format("%02d ",
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

                alertDialog.dismiss();


            }
        };
        timer.start();


        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(dataBean.get_id()!=null&&dataBean.getAppointment_id()!=null){

                    timer.cancel();

                    SPacceptResponseCall(dataBean.get_id(),dataBean.getAppointment_id());

                    alertDialog.dismiss();
                }

            }
        });

        btn_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(dataBean.get_id()!=null){

                    timer.cancel();

                    SPrejectResponseCall(dataBean.get_id());

                    alertDialog.dismiss();
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
                        startActivity(getIntent());


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
        SPAcceptRequest.setAppointment_id(appointment_id);
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

               isdata=false;


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

                isdata  = false;
            }
        });


    }
}