package com.triton.nannypartners.doctor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.triton.nannypartners.R;
import com.triton.nannypartners.api.APIClient;
import com.triton.nannypartners.api.RestApiInterface;
import com.triton.nannypartners.appUtils.FileUtil;
import com.triton.nannypartners.requestpojo.DoctorUpdateProfileImageRequest;
import com.triton.nannypartners.responsepojo.DoctorUpdateProfileImageResponse;
import com.triton.nannypartners.responsepojo.FileUploadResponse;
import com.triton.nannypartners.sessionmanager.SessionManager;
import com.triton.nannypartners.utils.RestUtils;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class EditDoctorProfileImageActivity extends AppCompatActivity implements View.OnClickListener {
    private  String TAG = "EditDoctorProfileImageActivity";

    @BindView(R.id.img_back)
    ImageView img_back;




    @BindView(R.id.img_pet_imge)
    ImageView img_pet_imge;


    @BindView(R.id.avi_indicator)
    AVLoadingIndicatorView avi_indicator;

    @BindView(R.id.txt_uploadpetimage)
    TextView txt_uploadpetimage;

    @BindView(R.id.btn_continue)
    Button btn_continue;


    public final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1;
    private static final String CAMERA_PERMISSION = CAMERA ;
    private static final String READ_EXTERNAL_STORAGE_PERMISSION = READ_EXTERNAL_STORAGE;
    private static final String WRITE_EXTERNAL_STORAGE_PERMISSION = WRITE_EXTERNAL_STORAGE;







    private static final int REQUEST_READ_CLINIC_PIC_PERMISSION = 786;
    private static final int REQUEST_CLINIC_CAMERA_PERMISSION_CODE = 785 ;




    private static final int SELECT_CLINIC_CAMERA = 1000 ;

    private static final int SELECT_CLINIC_PICTURE = 1001 ;
    private MultipartBody.Part filePart;
    private String userid;
    private String firstname,lastname,useremail;
    private String phonenumber,usertype,userstatus,profileimage;
    private String verifyemailstatus;
    private String refcode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_doctor_profile_image);
        Log.w(TAG,"onCreate ");
        ButterKnife.bind(this);
        avi_indicator.setVisibility(View.GONE);
        btn_continue.setVisibility(View.GONE);
        img_back.setOnClickListener(this);
        btn_continue.setOnClickListener(this);
        txt_uploadpetimage.setOnClickListener(this);
        img_pet_imge.setOnClickListener(this);



        SessionManager session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getProfileDetails();
        firstname = user.get(SessionManager.KEY_FIRST_NAME);
        lastname = user.get(SessionManager.KEY_LAST_NAME);
        useremail = user.get(SessionManager.KEY_EMAIL_ID);
        phonenumber = user.get(SessionManager.KEY_MOBILE);
        userid = user.get(SessionManager.KEY_ID);
        usertype = user.get(SessionManager.KEY_TYPE);
        userstatus = user.get(SessionManager.KEY_PROFILE_STATUS);
        profileimage = user.get(SessionManager.KEY_PROFILE_IMAGE);
        verifyemailstatus = user.get(SessionManager.KEY_VERIFY_EMAIL_STATUS);
        refcode = user.get(SessionManager.KEY_REF_CODE);




    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                onBackPressed();
                break;


                case R.id.txt_uploadpetimage:
                   gotoUplodPetImage();
                break;

                case R.id.img_pet_imge:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkMultiplePermissions(REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS, EditDoctorProfileImageActivity.this);
                }else{
                    choosePetImage();

                }
                break;

                case R.id.btn_continue:
                    DoctorUpdateProfileImageResponseCall();
                break;
        }
    }

    private void gotoUplodPetImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkMultiplePermissions(REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS, EditDoctorProfileImageActivity.this);
        }else{
            choosePetImage();

        }
    }

    private void gotoDoctorProfileScreenActivity() {
        Intent intent = new Intent(EditDoctorProfileImageActivity.this, DoctorProfileScreenActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(EditDoctorProfileImageActivity.this, DoctorProfileScreenActivity.class));
        finish();
    }




    private void choosePetImage() {


            final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
            //AlertDialog.Builder alert=new AlertDialog.Builder(this);
            AlertDialog.Builder builder = new AlertDialog.Builder(EditDoctorProfileImageActivity.this);
            builder.setTitle("Choose option");
            builder.setItems(items, (dialog, item) -> {
                if (items[item].equals("Take Photo"))
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(EditDoctorProfileImageActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                    {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CLINIC_CAMERA_PERMISSION_CODE);
                    }
                    else
                    {


                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                        startActivityForResult(intent, SELECT_CLINIC_CAMERA);
                    }

                }

                else if (items[item].equals("Choose from Library"))
                {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(EditDoctorProfileImageActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_CLINIC_PIC_PERMISSION);
                    }

                    else{

                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_CLINIC_PICTURE);


                    }
                }

                else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            });
            builder.show();



    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //	Toast.makeText(getActivity(),"kk",Toast.LENGTH_SHORT).show();
        if(requestCode== SELECT_CLINIC_PICTURE || requestCode == SELECT_CLINIC_CAMERA)
        {

            if(requestCode == SELECT_CLINIC_CAMERA)
            {
                Bitmap photo = (Bitmap) data.getExtras().get("data");

                File file = new File(getFilesDir(), "Petfolio1" + ".jpg");

                OutputStream os;
                try {
                    os = new FileOutputStream(file);
                    photo.compress(Bitmap.CompressFormat.JPEG, 100, os);
                    os.flush();
                    os.close();
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
                }
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm aa", Locale.getDefault());
                String currentDateandTime = sdf.format(new Date());

                RequestBody requestFile = RequestBody.create(MediaType.parse("image*/"), file);

                filePart = MultipartBody.Part.createFormData("sampleFile",  userid+currentDateandTime+file.getName(), requestFile);

                uploadPetImage();

            }

            else{

                try {
                    if (resultCode == Activity.RESULT_OK)
                    {

                        Log.w("VALUEEEEEEE1111", " " + data);

                        Uri selectedImageUri = data.getData();

                        Log.w("selectedImageUri", " " + selectedImageUri);

                        String filename = getFileName(selectedImageUri);

                        Log.w("filename", " " + filename);

                        String filePath = FileUtil.getPath(EditDoctorProfileImageActivity.this,selectedImageUri);

                        assert filePath != null;

                        File file = new File(filePath); // initialize file here

                        long length = file.length() / 1024; // Size in KB

                        Log.w("filesize", " " + length);

                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm aa", Locale.getDefault());
                        String currentDateandTime = sdf.format(new Date());

                        filePart = MultipartBody.Part.createFormData("sampleFile", userid+currentDateandTime+file.getName(), RequestBody.create(MediaType.parse("image/*"), file));

                        uploadPetImage();


                    }
                } catch (Exception e) {

                    Log.w("Exception", " " + e);
                }

            }

        }



    }




    private void uploadPetImage() {

        avi_indicator.show();

        RestApiInterface apiInterface = APIClient.getImageClient().create(RestApiInterface.class);


        Call<FileUploadResponse> call = apiInterface.getImageStroeResponse(filePart);


        Log.w(TAG,"url  :%s"+ call.request().url().toString());

        call.enqueue(new Callback<FileUploadResponse>() {
            @Override
            public void onResponse(@NonNull Call<FileUploadResponse> call, @NonNull Response<FileUploadResponse> response) {
                avi_indicator.smoothToHide();
                Log.w(TAG,"Profpic"+ "--->" + new Gson().toJson(response.body()));

                if (response.body() != null) {
                    if (200 == response.body().getCode()) {

                        profileimage = response.body().getData();
                        btn_continue.setVisibility(View.VISIBLE);

                        Log.w(TAG, "ServerUrlImagePath " + profileimage);

                        if( response.body().getData() != null){
                            Glide.with(EditDoctorProfileImageActivity.this)
                                    .load(profileimage)
                                    .into(img_pet_imge);
                        }else{
                            Glide.with(EditDoctorProfileImageActivity.this)
                                    .load(R.drawable.image_thumbnail)
                                    .into(img_pet_imge);

                        }



                    }

                }


            }

            @Override
            public void onFailure(@NonNull Call<FileUploadResponse> call, @NonNull Throwable t) {
                // avi_indicator.smoothToHide();
                Log.w(TAG,"ServerUrlImagePath"+ "On failure working"+ t.getMessage());
                //Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CLINIC_PIC_PERMISSION) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_CLINIC_PICTURE);

            } else {
                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Permisson Required")
                        .setContentText("Plz Allow Permissions for choosing Images from Gallery ")
                        .setConfirmText("Ok")
                        .setConfirmClickListener(sDialog -> {

                            sDialog.dismissWithAnimation();

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_CLINIC_PIC_PERMISSION);
                            }


                        })
                        .setCancelButton("Cancel", sDialog -> {
                            sDialog.dismissWithAnimation();

                            showWarning(REQUEST_READ_CLINIC_PIC_PERMISSION);
                        })
                        .show();

            }

        }

        else if (requestCode == REQUEST_CLINIC_CAMERA_PERMISSION_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(intent, SELECT_CLINIC_CAMERA);

            } else {
                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Permisson Required")
                        .setContentText("Plz Allow Camera for taking picture")
                        .setConfirmText("Ok")
                        .setConfirmClickListener(sDialog -> {

                            sDialog.dismissWithAnimation();

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CLINIC_CAMERA_PERMISSION_CODE);
                            }


                        })
                        .setCancelButton("Cancel", sDialog -> {
                            sDialog.dismissWithAnimation();

                            showWarning(REQUEST_CLINIC_CAMERA_PERMISSION_CODE);
                        })
                        .show();

            }

        }
    }

    //check for camera and storage access permissions
    @TargetApi(Build.VERSION_CODES.M)
    private void checkMultiplePermissions(int permissionCode, Context context) {

        String[] PERMISSIONS = {CAMERA_PERMISSION, READ_EXTERNAL_STORAGE_PERMISSION, WRITE_EXTERNAL_STORAGE_PERMISSION};
        if (!hasPermissions(context, PERMISSIONS)) {
            ActivityCompat.requestPermissions((Activity) context, PERMISSIONS, permissionCode);
        } else {
            choosePetImage();
            // Open your camera here.
        }
    }
    private boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void showWarning(int REQUEST_PERMISSION_CODE) {

        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Sorry!!")
                .setContentText("You Can't proceed further unless you allow permission")
                .setConfirmText("Ok")
                .setConfirmClickListener(sDialog -> {

                    sDialog.dismissWithAnimation();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
                    }


                })
                .setCancelButton("Cancel", SweetAlertDialog::dismissWithAnimation)
                .show();
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }


    @SuppressLint("LogNotTimber")
    private void DoctorUpdateProfileImageResponseCall() {
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        RestApiInterface apiInterface = APIClient.getClient().create(RestApiInterface.class);
        Call<DoctorUpdateProfileImageResponse> call = apiInterface.DoctorUpdateProfileImageResponseCall(RestUtils.getContentType(), doctorUpdateProfileImageRequest());
        Log.w(TAG,"DoctorUpdateProfileImageResponse url  :%s"+" "+ call.request().url().toString());

        call.enqueue(new Callback<DoctorUpdateProfileImageResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NonNull Call<DoctorUpdateProfileImageResponse> call, @NonNull Response<DoctorUpdateProfileImageResponse> response) {

                Log.w(TAG,"DoctorUpdateProfileImageResponse"+ "--->" + new Gson().toJson(response.body()));

                avi_indicator.smoothToHide();

                if (response.body() != null) {
                    if(response.body().getCode() == 200){
                        SessionManager sessionManager = new SessionManager(getApplicationContext());
                        sessionManager.setIsLogin(true);
                        sessionManager.createLoginSession(
                                userid,
                                firstname,
                                lastname,
                                useremail,
                                phonenumber,
                                String.valueOf(usertype),
                                userstatus,
                                profileimage,
                                verifyemailstatus,
                                refcode

                        );
                       onBackPressed();
                    }

                }


            }

            @Override
            public void onFailure(@NonNull Call<DoctorUpdateProfileImageResponse> call, @NonNull Throwable t) {

                avi_indicator.smoothToHide();
                Log.w(TAG,"DoctorUpdateProfileImageResponse flr"+"--->" + t.getMessage());
            }
        });

    }
    private DoctorUpdateProfileImageRequest doctorUpdateProfileImageRequest() {
        DoctorUpdateProfileImageRequest  doctorUpdateProfileImageRequest = new DoctorUpdateProfileImageRequest();
        doctorUpdateProfileImageRequest.setUser_id(userid);
        if(profileimage != null && !profileimage.isEmpty()){
        doctorUpdateProfileImageRequest.setProfile_img(profileimage);
        }else{
            doctorUpdateProfileImageRequest.setProfile_img(APIClient.PROFILE_IMAGE_URL);

        }

        Log.w(TAG,"doctorUpdateProfileImageRequest"+ "--->" + new Gson().toJson(doctorUpdateProfileImageRequest));
        return doctorUpdateProfileImageRequest;
    }
}