package com.triton.nannypartners.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.triton.nannypartners.R;
import com.triton.nannypartners.interfaces.ServiceDeleteListener;
import com.triton.nannypartners.requestpojo.FetchServiceDataResponse;
import com.triton.nannypartners.serviceprovider.ChooseSubServiceTypeActivity;

import java.util.List;


public class ServiceDetailsEditAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private  String TAG = "ServiceDetailsEditAdapter";
    private Context context;
    List<FetchServiceDataResponse.DataBean> spServiceList;

    FetchServiceDataResponse.DataBean currentItem;
    String fromactivity;
    ServiceDeleteListener serviceDeleteListener;
    private String concatenatedStarNames = "";

    public ServiceDetailsEditAdapter(Context context, List<FetchServiceDataResponse.DataBean> spServiceList, ServiceDeleteListener serviceDeleteListener) {
        this.context = context;
        this.spServiceList = spServiceList;
        this.serviceDeleteListener = serviceDeleteListener;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_parent_servicelist, parent, false);
        return new ViewHolderOne(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        initLayoutOne((ViewHolderOne) holder, position);
    }

    @SuppressLint({"SetTextI18n", "LogNotTimber"})
    private void initLayoutOne(ViewHolderOne holder, final int position) {

        currentItem = spServiceList.get(position);
        if(spServiceList != null && spServiceList.size()>0){
            holder.txt_servicename.setVisibility(View.VISIBLE);
            holder.txt_subservicename.setVisibility(View.VISIBLE);
            holder.txt_servicename.setText(currentItem.getService_name());

            if(spServiceList.get(position).getSubsericelist() != null){
                concatenatedStarNames = "";
                for (int i = 0; i < spServiceList.get(position).getSubsericelist().size(); i++) {
                    if(spServiceList.get(position).getSubsericelist().get(i).isIsservice()){
                        concatenatedStarNames += spServiceList.get(position).getSubsericelist().get(i).getTitle();
                        if (i < spServiceList.get(position).getSubsericelist().size() - 1) concatenatedStarNames += ", ";
                        holder.txt_subservicename.setText(concatenatedStarNames);
                        Log.w(TAG," concatenatedStarNames : "+concatenatedStarNames);
                    }

                }


            }
        }else{
            holder.txt_servicename.setVisibility(View.GONE);
            holder.txt_subservicename.setVisibility(View.GONE);
        }

        holder.img_settings.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(context, v);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_menus, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        String titleName = String.valueOf(item.getTitle());
                        if(titleName != null && titleName.equalsIgnoreCase("Edit")){
                                Intent i = new Intent(context, ChooseSubServiceTypeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.putExtra("ServiceId", spServiceList.get(position).getSubsericelist().get(0).getService_id());
                                i.putExtra("ServiceName", spServiceList.get(position).getService_name());
                                i.putExtra("fromactivity", TAG);
                                context.startActivity(i);


                        }
                        else if(titleName != null && titleName.equalsIgnoreCase("Delete")){
                            serviceDeleteListener.serviceDeleteListener(spServiceList.get(position).getService_name());
                        }
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });

    }

    @Override
    public int getItemCount() {
        return spServiceList.size();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }



    public class ViewHolderOne extends RecyclerView.ViewHolder {
        public TextView txt_servicename,txt_subservicename;
        public ImageView img_settings;


        public ViewHolderOne(View itemView) {
            super(itemView);

            txt_servicename = itemView.findViewById(R.id.txt_servicename);
            txt_subservicename = itemView.findViewById(R.id.txt_subservicename);
            img_settings = itemView.findViewById(R.id.img_settings);





        }




    }


}
