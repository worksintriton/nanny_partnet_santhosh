package com.triton.nannypartners.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.triton.nannypartners.R;
import com.triton.nannypartners.api.APIClient;
import com.triton.nannypartners.interfaces.ServicesTypeSelectListener;
import com.triton.nannypartners.interfaces.UserTypeSelectListener;
import com.triton.nannypartners.responsepojo.ServiceCatResponse;
import com.triton.nannypartners.responsepojo.UserTypeListResponse;

import java.util.List;


public class ServiceTypesListAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private  String TAG = "ServiceTypesListAdapter";
    private Context context;
    private ServicesTypeSelectListener servicesTypeSelectListener;


    private List<ServiceCatResponse.DataBean> servicetypedataBeanList;

    ServiceCatResponse.DataBean currentItem;

    private int userTypeValue;

    int usrtype;

    public ServiceTypesListAdapter(Context context, List<ServiceCatResponse.DataBean> servicetypedataBeanList, ServicesTypeSelectListener servicesTypeSelectListener, int userTypeValue) {
        this.servicetypedataBeanList = servicetypedataBeanList;
        this.context = context;
        this.servicesTypeSelectListener = servicesTypeSelectListener;
        this.userTypeValue = userTypeValue;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_usertypes_cardview, parent, false);
        return new ViewHolderOne(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        initLayoutOne((ViewHolderOne) holder, position);


    }

    @SuppressLint("LogNotTimber")
    private void initLayoutOne(ViewHolderOne holder, final int position) {
        currentItem = servicetypedataBeanList.get(position);
        Log.w(TAG,"userTypeValue : "+userTypeValue);

        if(currentItem.getTitle() != null){
            holder.txt_usertypes.setText(currentItem.getTitle());

        }
        if (currentItem.getImage() != null && !currentItem.getImage().isEmpty()) {
            Glide.with(context)
                        .load(currentItem.getImage())
                        .into(holder.img_userimages);

            }
        else{
                Glide.with(context)
                        .load(APIClient.PROFILE_IMAGE_URL)
                        .into(holder.img_userimages);

            }

        holder.ll_usertypes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i=0;i<servicetypedataBeanList.size();i++){
                    servicetypedataBeanList.get(i).setSelected(false);

                }
                servicetypedataBeanList.get(position).setSelected(true);
                notifyDataSetChanged();
                if(servicetypedataBeanList.get(position).getTitle() != null && servicetypedataBeanList.get(position).get_id() != null){
                    servicesTypeSelectListener.servicesTypeSelectListener(servicetypedataBeanList.get(position).getTitle(),servicetypedataBeanList.get(position).get_id());

                }
            }
        });
        if (servicetypedataBeanList.get(position).isSelected()) {
            Log.w(TAG, "IF isSelected--->");
            holder.ll_usertypes.setBackgroundResource(R.drawable.user_type_bgm);
            holder.chx_usertypes.setVisibility(View.VISIBLE);
            holder.chx_usertypes.setChecked(true);
            return;

        }
        else {
            Log.w(TAG, "ELSE isSelected--->");

            holder.ll_usertypes.setBackgroundResource(R.drawable.user_bgm_trans);
            holder.chx_usertypes.setVisibility(View.INVISIBLE);
            holder.chx_usertypes.setChecked(false);

        }



    }
    @Override
    public int getItemCount() {
        return servicetypedataBeanList.size();
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
            chx_usertypes.setClickable(false);
            chx_usertypes.setChecked(false);
            chx_usertypes.setVisibility(View.GONE);










        }




    }














}
