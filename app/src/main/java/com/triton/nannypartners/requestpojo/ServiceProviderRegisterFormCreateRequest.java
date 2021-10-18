package com.triton.nannypartners.requestpojo;

import java.io.Serializable;
import java.util.List;

public class ServiceProviderRegisterFormCreateRequest implements Serializable {

    /**
     * bus_certif : [{"bus_certif":"http://54.193.18.54:3000/api/uploads/1634205831306.jpg"}]
     * bus_profile : http://54.193.18.54:3000/api/uploads/1634205803590.jpg
     * bus_proof : http://54.193.18.54:3000/api/uploads/1634205820909.jpg
     * bus_service_gall : [{"bus_service_gall":"http://54.193.18.54:3000/api/uploads/1634205785736.jpg"}]
     * bus_service_list : [{"service_name":"Vet care","subsericelist":[{"sub_service_title":"Vat care1"},{"sub_service_title":"Vat care2"}]}]
     * bus_spec_list : [{"bus_spec_list":"Kennel Cut"}]
     * bus_user_email : iddineshkumar@gmail.com
     * bus_user_name : DINESH
     * bus_user_phone : 9638527410
     * bussiness_name : fgg
     * city_name : Chennai
     * date_and_time : 14/10/2021 03:34 PM
     * mobile_type : Android
     * profile_status : true
     * profile_verification_status : Not verified
     * sp_info : gh
     * sp_lat : 12.983240215899578
     * sp_loc : Chennai 2A, 3rd Main Rd, Ram Nagar, Ramnagar South, Dhadeswaram Nagar, Velachery, Chennai, Tamil Nadu 600042, India
     * sp_long : 80.22349512276435
     * user_id : 6167cca3e605fa4c7206e49f
     */

    private String bus_profile;
    private String bus_proof;
    private String bus_user_email;
    private String bus_user_name;
    private String bus_user_phone;
    private String bussiness_name;
    private String city_name;
    private String date_and_time;
    private String mobile_type;
    private boolean profile_status;
    private String profile_verification_status;
    private String sp_info;
    private double sp_lat;
    private String sp_loc;
    private double sp_long;
    private String user_id;
    /**
     * bus_certif : http://54.193.18.54:3000/api/uploads/1634205831306.jpg
     */

    private List<BusCertifBean> bus_certif;
    /**
     * bus_service_gall : http://54.193.18.54:3000/api/uploads/1634205785736.jpg
     */

    private List<BusServiceGallBean> bus_service_gall;
    /**
     * service_name : Vet care
     * subsericelist : [{"sub_service_title":"Vat care1"},{"sub_service_title":"Vat care2"}]
     */

    private List<BusServiceListBean> bus_service_list;
    /**
     * bus_spec_list : Kennel Cut
     */

    private List<BusSpecListBean> bus_spec_list;

    public String getBus_profile() {
        return bus_profile;
    }

    public void setBus_profile(String bus_profile) {
        this.bus_profile = bus_profile;
    }

    public String getBus_proof() {
        return bus_proof;
    }

    public void setBus_proof(String bus_proof) {
        this.bus_proof = bus_proof;
    }

    public String getBus_user_email() {
        return bus_user_email;
    }

    public void setBus_user_email(String bus_user_email) {
        this.bus_user_email = bus_user_email;
    }

    public String getBus_user_name() {
        return bus_user_name;
    }

    public void setBus_user_name(String bus_user_name) {
        this.bus_user_name = bus_user_name;
    }

    public String getBus_user_phone() {
        return bus_user_phone;
    }

    public void setBus_user_phone(String bus_user_phone) {
        this.bus_user_phone = bus_user_phone;
    }

    public String getBussiness_name() {
        return bussiness_name;
    }

    public void setBussiness_name(String bussiness_name) {
        this.bussiness_name = bussiness_name;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getDate_and_time() {
        return date_and_time;
    }

    public void setDate_and_time(String date_and_time) {
        this.date_and_time = date_and_time;
    }

    public String getMobile_type() {
        return mobile_type;
    }

    public void setMobile_type(String mobile_type) {
        this.mobile_type = mobile_type;
    }

    public boolean isProfile_status() {
        return profile_status;
    }

    public void setProfile_status(boolean profile_status) {
        this.profile_status = profile_status;
    }

    public String getProfile_verification_status() {
        return profile_verification_status;
    }

    public void setProfile_verification_status(String profile_verification_status) {
        this.profile_verification_status = profile_verification_status;
    }

    public String getSp_info() {
        return sp_info;
    }

    public void setSp_info(String sp_info) {
        this.sp_info = sp_info;
    }

    public double getSp_lat() {
        return sp_lat;
    }

    public void setSp_lat(double sp_lat) {
        this.sp_lat = sp_lat;
    }

    public String getSp_loc() {
        return sp_loc;
    }

    public void setSp_loc(String sp_loc) {
        this.sp_loc = sp_loc;
    }

    public double getSp_long() {
        return sp_long;
    }

    public void setSp_long(double sp_long) {
        this.sp_long = sp_long;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public List<BusCertifBean> getBus_certif() {
        return bus_certif;
    }

    public void setBus_certif(List<BusCertifBean> bus_certif) {
        this.bus_certif = bus_certif;
    }

    public List<BusServiceGallBean> getBus_service_gall() {
        return bus_service_gall;
    }

    public void setBus_service_gall(List<BusServiceGallBean> bus_service_gall) {
        this.bus_service_gall = bus_service_gall;
    }

    public List<BusServiceListBean> getBus_service_list() {
        return bus_service_list;
    }

    public void setBus_service_list(List<BusServiceListBean> bus_service_list) {
        this.bus_service_list = bus_service_list;
    }

    public List<BusSpecListBean> getBus_spec_list() {
        return bus_spec_list;
    }

    public void setBus_spec_list(List<BusSpecListBean> bus_spec_list) {
        this.bus_spec_list = bus_spec_list;
    }

    public static class BusCertifBean implements  Serializable{
        private String bus_certif;

        public String getBus_certif() {
            return bus_certif;
        }

        public void setBus_certif(String bus_certif) {
            this.bus_certif = bus_certif;
        }
    }

    public static class BusServiceGallBean implements Serializable {
        private String bus_service_gall;

        public String getBus_service_gall() {
            return bus_service_gall;
        }

        public void setBus_service_gall(String bus_service_gall) {
            this.bus_service_gall = bus_service_gall;
        }
    }

    public static class BusServiceListBean implements Serializable {
        private String service_name;
        /**
         * sub_service_title : Vat care1
         */

        private List<SubsericelistBean> subsericelist;

        public String getService_name() {
            return service_name;
        }

        public void setService_name(String service_name) {
            this.service_name = service_name;
        }

        public List<SubsericelistBean> getSubsericelist() {
            return subsericelist;
        }

        public void setSubsericelist(List<SubsericelistBean> subsericelist) {
            this.subsericelist = subsericelist;
        }

        public static class SubsericelistBean implements Serializable{
            private String sub_service_title;

            public String getSub_service_title() {
                return sub_service_title;
            }

            public void setSub_service_title(String sub_service_title) {
                this.sub_service_title = sub_service_title;
            }
        }
    }

    public static class BusSpecListBean implements Serializable{
        private String bus_spec_list;

        public String getBus_spec_list() {
            return bus_spec_list;
        }

        public void setBus_spec_list(String bus_spec_list) {
            this.bus_spec_list = bus_spec_list;
        }
    }
}
