package com.triton.nannypartners.interfaces;

public interface SPServiceCheckedListener {

    void onItemSPServiceCheck(int position, String specValue,boolean isChbxChecked,String servicename);

    void onItemSPServiceUnCheck(int position, String specValue,boolean isChbxChecked,String servicename);

}