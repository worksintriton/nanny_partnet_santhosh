package com.triton.nannypartners.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
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
import com.google.gson.Gson;
import com.triton.nannypartners.R;
import com.triton.nannypartners.api.APIClient;
import com.triton.nannypartners.interfaces.SPSubServiceCheckedListener;
import com.triton.nannypartners.interfaces.ServicesTypeSelectListener;
import com.triton.nannypartners.responsepojo.ServiceCatResponse;
import com.triton.nannypartners.responsepojo.SubServiceCatResponse;

import java.util.List;


public class SubServiceTypesListAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private  String TAG = "SubServiceTypesListAdapter";
    private Context context;
    private SPSubServiceCheckedListener spSubServiceCheckedListener;
    private List<SubServiceCatResponse.DataBean> subservicetypedataBeanList;
    SubServiceCatResponse.DataBean currentItem;
    private String ServiceName;
    int usrtype;
    private String chservice;
    private boolean isChbxChecked;

    public SubServiceTypesListAdapter(Context context, List<SubServiceCatResponse.DataBean> subservicetypedataBeanList, SPSubServiceCheckedListener spSubServiceCheckedListener, String ServiceName) {
        this.context = context;
        this.subservicetypedataBeanList = subservicetypedataBeanList;
        this.spSubServiceCheckedListener = spSubServiceCheckedListener;
        this.ServiceName = ServiceName;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_subservices_cardview, parent, false);
        return new ViewHolderOne(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        initLayoutOne((ViewHolderOne) holder, position);


    }

    @SuppressLint("LogNotTimber")
    private void initLayoutOne(ViewHolderOne holder, final int position) {
        currentItem = subservicetypedataBeanList.get(position);
        Log.w(TAG,"ServiceName : "+ServiceName);

        if(currentItem.getTitle() != null){
            holder.txt_usertypes.setText(currentItem.getTitle());

        }
        if (currentItem.getIcon_banner() != null && !currentItem.getIcon_banner().isEmpty()) {
            Glide.with(context)
                        .load(currentItem.getIcon_banner())
                        .into(holder.img_userimages);

            }
        else{
                Glide.with(context)
                        .load(APIClient.PROFILE_IMAGE_URL)
                        .into(holder.img_userimages);

            }

        Log.w(TAG,"isservice : "+subservicetypedataBeanList.get(position).isIsservice());

        holder.chx_usertypes.setTag(position);

        if(subservicetypedataBeanList.get(position).isIsservice()){
            holder.chx_usertypes.setChecked(true);
            holder.chx_usertypes.setBackgroundResource(R.drawable.checked);
        }else{
            holder.chx_usertypes.setChecked(false);
            holder.chx_usertypes.setBackgroundResource(R.drawable.uncheck);
        }




       holder.chx_usertypes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.w(TAG,"setOnCheckedChangeListener isChecked : "+isChecked);

                Log.w(TAG,"onCheckedChanged spServiceList : "+new Gson().toJson(subservicetypedataBeanList));

                chservice = subservicetypedataBeanList.get(position).getTitle();
                String id = subservicetypedataBeanList.get(position).get_id();
                isChbxChecked =  isChecked;
                if(isChecked){
                    holder.chx_usertypes.setBackgroundResource(R.drawable.checked);
                    spSubServiceCheckedListener.onItemSPSubServiceCheck(position,chservice,isChecked,ServiceName,id);
                }else{
                    holder.chx_usertypes.setBackgroundResource(R.drawable.uncheck);
                    spSubServiceCheckedListener.onItemSPSubServiceUnCheck(position,chservice,isChecked,ServiceName,id);
                }

            }
        });






    }
    @Override
    public int getItemCount() {
        return subservicetypedataBeanList.size();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class ViewHolderOne extends RecyclerView.ViewHolder {
        public TextView txt_usertypes;
        public ImageView img_userimages;
        public LinearLayout ll_usertypes;
        public CheckBox chx_usertypes;



        public ViewHolderOne(View itemView) {
            super(itemView);

            txt_usertypes = itemView.findViewById(R.id.txt_usertypes);
            img_userimages = itemView.findViewById(R.id.img_userimages);
            ll_usertypes = itemView.findViewById(R.id.ll_usertypes);
            chx_usertypes = itemView.findViewById(R.id.chx_usertypes);










        }




    }














}
