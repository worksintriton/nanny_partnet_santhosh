package com.triton.nannypartners.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.triton.nannypartners.R;
import com.triton.nannypartners.api.APIClient;
import com.triton.nannypartners.doctor.shop.DoctorProductDetailsActivity;
import com.triton.nannypartners.interfaces.SPServiceCheckedListener;
import com.triton.nannypartners.petlover.ProductDetailsActivity;
import com.triton.nannypartners.responsepojo.ServiceListResponse;
import com.triton.nannypartners.responsepojo.ShopDashboardResponse;

import java.util.List;


public class SubServiceDetailsAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private  String TAG = "SubServiceDetailsAdapter";
    private Context context;

    List<ServiceListResponse.DataBean.ServiceListBean.SubServiceListBean> subServiceListBeans;

    String fromactivity;
    SPServiceCheckedListener spServiceCheckedListener;
    List<ServiceListResponse.DataBean.ServiceListBean> spServiceList;


    public SubServiceDetailsAdapter(Context context, List<ServiceListResponse.DataBean.ServiceListBean.SubServiceListBean> subServiceListBeans, String fromactivity,SPServiceCheckedListener spServiceCheckedListener, List<ServiceListResponse.DataBean.ServiceListBean> spServiceList) {
        this.context = context;
        this.subServiceListBeans = subServiceListBeans;
        this.fromactivity = fromactivity;
        this.spServiceCheckedListener = spServiceCheckedListener;
        this.spServiceList = spServiceList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_parent_subservlist, parent, false);
        return new ViewHolderOne(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        initLayoutOne((ViewHolderOne) holder, position);
    }

    @SuppressLint({"SetTextI18n", "LogNotTimber"})
    private void initLayoutOne(ViewHolderOne holder, final int position) {
        ServiceListResponse.DataBean.ServiceListBean.SubServiceListBean productListBean = subServiceListBeans.get(position);
        holder.txt_subservicename.setText(productListBean.getTitle());

        holder.checkbox_subservice_type.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String chspecialization = subServiceListBeans.get(position).getTitle();
                String ServiceTitle = subServiceListBeans.get(position).getService_id();

                if(isChecked){
                    if (holder.checkbox_subservice_type.isChecked()) {
                        spServiceCheckedListener.onItemSPServiceCheck(position,chspecialization,true,ServiceTitle);
                    }
                }else{
                    spServiceCheckedListener.onItemSPServiceUnCheck(position,chspecialization,false,ServiceTitle);

                }

            }
        });






    }

    @Override
    public int getItemCount() {

        return subServiceListBeans.size();

    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }
    static class ViewHolderOne extends RecyclerView.ViewHolder {
        public TextView txt_subservicename;
        public CheckBox checkbox_subservice_type;
        LinearLayout ll_root;


        public ViewHolderOne(View itemView) {
            super(itemView);
            txt_subservicename = itemView.findViewById(R.id.txt_subservicename);
            checkbox_subservice_type = itemView.findViewById(R.id.checkbox_subservice_type);
            ll_root = itemView.findViewById(R.id.ll_root);


        }




    }


}
