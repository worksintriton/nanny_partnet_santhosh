package com.triton.nannypartners.requestpojo;


public class ServiceDeleteRequest {


    /**
     * user_id : 616a83bf9e7b943a38ec0883
     * service_name : Pet Grooming
     */

    private String user_id;
    private String service_name;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }
}
