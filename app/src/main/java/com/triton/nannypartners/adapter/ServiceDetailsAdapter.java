package com.triton.nannypartners.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.triton.nannypartners.R;
import com.triton.nannypartners.doctor.shop.DoctorListOfProductsSeeMoreActivity;
import com.triton.nannypartners.interfaces.SPServiceCheckedListener;
import com.triton.nannypartners.petlover.ListOfProductsSeeMoreActivity;
import com.triton.nannypartners.responsepojo.ServiceListResponse;
import com.triton.nannypartners.responsepojo.ShopDashboardResponse;
import com.triton.nannypartners.serviceprovider.shop.SPListOfProductsSeeMoreActivity;

import java.util.List;


public class ServiceDetailsAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> implements SPServiceCheckedListener {

    private  String TAG = "ServiceDetailsAdapter";
    private Context context;
    private List<ServiceListResponse.DataBean.ServiceListBean> spServiceList;

    ServiceListResponse.DataBean.ServiceListBean currentItem;
    String fromactivity;
    SPServiceCheckedListener spServiceCheckedListener;

    public ServiceDetailsAdapter(Context context, List<ServiceListResponse.DataBean.ServiceListBean> spServiceList, SPServiceCheckedListener spServiceCheckedListener) {
        this.context = context;
        this.spServiceList = spServiceList;
        this.spServiceCheckedListener = spServiceCheckedListener;

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
        if(spServiceList.get(position).getSub_service_list() != null && spServiceList.get(position).getSub_service_list().size()>0){
            holder.txt_servicename.setVisibility(View.VISIBLE);
            holder.rv_subservices_list.setVisibility(View.VISIBLE);
            holder.txt_servicename.setText(currentItem.getService_name());
        }else{
            holder.txt_servicename.setVisibility(View.GONE);
            holder.rv_subservices_list.setVisibility(View.GONE);
        }
        holder.rv_subservices_list.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.rv_subservices_list.setItemAnimator(new DefaultItemAnimator());
        SubServiceDetailsAdapter subServiceDetailsAdapter  = new SubServiceDetailsAdapter(context,spServiceList.get(position).getSub_service_list(),fromactivity,this,spServiceList);
        holder.rv_subservices_list.setAdapter(subServiceDetailsAdapter);
    }

    @Override
    public int getItemCount() {
        return spServiceList.size();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onItemSPServiceCheck(int position, String specValue, boolean isChbxChecked,String servicename) {
        spServiceCheckedListener.onItemSPServiceCheck(position,specValue,isChbxChecked,servicename);
    }

    @Override
    public void onItemSPServiceUnCheck(int position, String specValue, boolean isChbxChecked,String servicename) {
        spServiceCheckedListener.onItemSPServiceUnCheck(position,specValue,isChbxChecked,servicename);
    }

    public class ViewHolderOne extends RecyclerView.ViewHolder {
        public TextView txt_servicename;
        RecyclerView rv_subservices_list;


        public ViewHolderOne(View itemView) {
            super(itemView);

            txt_servicename = itemView.findViewById(R.id.txt_servicename);
            rv_subservices_list = itemView.findViewById(R.id.rv_subservices_list);

         /*   if(product_details.get(getAdapterPosition()).getProduct_list() != null && product_details.get(getAdapterPosition()).getProduct_list().size()>0) {
                Intent intent = new Intent(context, DoctorListOfProductsSeeMoreActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("cat_id", product_details.get(getAdapterPosition()).getCat_id());
                context.startActivity(intent);
            }*/




        }




    }


}
