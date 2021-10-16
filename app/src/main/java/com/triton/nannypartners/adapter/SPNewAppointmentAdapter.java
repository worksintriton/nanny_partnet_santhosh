package com.triton.nannypartners.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.triton.nannypartners.R;
import com.triton.nannypartners.api.APIClient;
import com.triton.nannypartners.interfaces.OnAppointmentCancel;
import com.triton.nannypartners.interfaces.OnAppointmentComplete;

import com.triton.nannypartners.responsepojo.SPAppointmentResponse;
import com.triton.nannypartners.serviceprovider.SPAppointmentDetailsActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class SPNewAppointmentAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private  String TAG = "SPNewAppointmentAdapter";
    private final List<SPAppointmentResponse.DataBean> newAppointmentResponseList;
    private Context context;

    SPAppointmentResponse.DataBean currentItem;

    private OnAppointmentCancel onAppointmentCancel;
    private OnAppointmentComplete onAppointmentComplete;
    private int size;
    private boolean isVaildDate;


    public SPNewAppointmentAdapter(Context context, List<SPAppointmentResponse.DataBean> newAppointmentResponseList, RecyclerView inbox_list, int size, OnAppointmentCancel onAppointmentCancel,OnAppointmentComplete onAppointmentComplete) {
        this.newAppointmentResponseList = newAppointmentResponseList;
        this.context = context;
        this.size = size;
        this.onAppointmentCancel = onAppointmentCancel;
        this.onAppointmentComplete = onAppointmentComplete;



    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_sp_new_custappointment, parent, false);
        return new ViewHolderOne(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        initLayoutOne((ViewHolderOne) holder, position);


    }

    @SuppressLint({"SetTextI18n", "LogNotTimber"})
    private void initLayoutOne(ViewHolderOne holder, final int position) {

        currentItem = newAppointmentResponseList.get(position);

        if(newAppointmentResponseList.get(position).getUser_name() != null) {
            holder.txt_servname.setText(newAppointmentResponseList.get(position).getUser_name());
        }

        else {

            holder.txt_servname.setText("");
        }

        if(newAppointmentResponseList.get(position).getService_hrs() != null) {
            holder.txt_hrs.setText(newAppointmentResponseList.get(position).getService_hrs());
        }

        else {

            holder.txt_hrs.setText("");
        }

        if(newAppointmentResponseList.get(position).getBooking_time() != null){
            holder.txt_datetimeslot.setText("\u20B9 "+newAppointmentResponseList.get(position).getBooking_time());
        }
        else {

            holder.txt_datetimeslot.setText("");
        }


        if(newAppointmentResponseList.get(position).getBooking_cost() != null){
            holder.txt_service_cost.setText("\u20B9 "+newAppointmentResponseList.get(position).getBooking_cost());
        }

        else {

            holder.txt_service_cost.setText("");
        }

        if(newAppointmentResponseList.get(position).getStatus() != null){
            holder.txt_appointment_status.setText(" "+newAppointmentResponseList.get(position).getStatus());

        }
        else {
            holder.txt_appointment_status.setText("");
        }

        if(newAppointmentResponseList.get(position).getBooking_at() != null){
            holder.txt_bookedon.setText("Booked for :"+" "+newAppointmentResponseList.get(position).getBooking_at());

        }


        if (newAppointmentResponseList.get(position).getImage_url() != null && !newAppointmentResponseList.get(position).getImage_url().isEmpty()) {

            Glide.with(context)
                    .load(newAppointmentResponseList.get(position).getImage_url())
                    .into(holder.img_clinic_imge);

        }
        else{
            Glide.with(context)
                    .load(APIClient.PROFILE_IMAGE_URL)
                    .into(holder.img_clinic_imge);

        }


        holder.ll_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent i = new Intent(context, SPAppointmentDetailsActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("appointment_id",newAppointmentResponseList.get(position).get_id());
                    i.putExtra("fromactivity",TAG);
                    context.startActivity(i);

            }
        });




    }


    @Override
    public int getItemCount() {
        return Math.min(newAppointmentResponseList.size(), size);

    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class ViewHolderOne extends RecyclerView.ViewHolder {

        public TextView txt_servname,txt_datetimeslot,txt_hrs,txt_appointment_status,txt_clinicname,txt_petname,txt_type,txt_service_cost,txt_bookedon,txt_lbl_doctorname,txt_doctorname;
        public ImageView img_clinic_imge,img_emergency_appointment,img_videocall;
        public Button btn_cancel;
        LinearLayout ll_new;


        public ViewHolderOne(View itemView) {
            super(itemView);
            img_clinic_imge = itemView.findViewById(R.id.img_clinic_imge);
            txt_servname = itemView.findViewById(R.id.txt_custname);
            txt_hrs = itemView.findViewById(R.id.txt_hrs);
            txt_datetimeslot = itemView.findViewById(R.id.txt_datetimeslot);
            txt_service_cost = itemView.findViewById(R.id.txt_service_cost);
            txt_appointment_status = itemView.findViewById(R.id.txt_appointment_status);
            txt_bookedon = itemView.findViewById(R.id.txt_bookedon);
            btn_cancel = itemView.findViewById(R.id.btn_cancel);
            ll_new = itemView.findViewById(R.id.ll_new);

        }




    }



    @SuppressLint("LogNotTimber")
    private void compareDatesandTime(String currentDateandTime, String bookingDateandTime) {
        try{

            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm aa");

            String str1 = currentDateandTime;
            Date currentDate = formatter.parse(str1);

            String str2 = bookingDateandTime;
            Date responseDate = formatter.parse(str2);

            Log.w(TAG,"compareDatesandTime--->"+"responseDate :"+responseDate+" "+"currentDate :"+currentDate);

            if (currentDate.compareTo(responseDate)<0 || responseDate.compareTo(currentDate) == 0)
            {
                Log.w(TAG,"date is equal");
                isVaildDate = true;

            }else{
                Log.w(TAG,"date is not equal");
                isVaildDate = false;
            }



        }catch (ParseException e1){
            e1.printStackTrace();
        }
    }





}
