package com.triton.nannypartners.interfaces;

public interface SPSubServiceCheckedListener {

    void onItemSPSubServiceCheck(int position, String specValue,boolean isChbxChecked,String servicename,String id);

    void onItemSPSubServiceUnCheck(int position, String specValue,boolean isChbxChecked,String servicename,String id);

}