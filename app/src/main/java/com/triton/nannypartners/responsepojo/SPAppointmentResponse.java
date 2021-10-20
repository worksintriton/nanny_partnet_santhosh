package com.triton.nannypartners.responsepojo;

import java.util.List;

public class SPAppointmentResponse {


    /**
     * Status : Success
     * Message : New Appointment List
     * Data : [{"_id":"61680377988d1d35aa95b007","user_id":"6163d60a489ccc3d894683d2","sp_id":"61275eccdc2406057b5aeb15","Appointment_id":"SP-1634206583206","image_url":"","bussiness_name":"Abc nanny service ","service_name":"Sub Service 2","service_hrs":"1","booking_time":"14-10-2021 3:46 PM","booking_cost":"300","status":"Incomplete","booking_at":"14-10-2021 03:46 PM","user_name":"Maddy Sam","service_provider_name":"Sample Test"},{"_id":"616848149e7b943a38ec087f","user_id":"6163d60a489ccc3d894683d2","sp_id":"61275eccdc2406057b5aeb15","Appointment_id":"SP-1634224148181","image_url":"","bussiness_name":"Abc nanny service ","service_name":"Sub Service 2","service_hrs":"1","booking_time":"14-10-2021 8:38 PM","booking_cost":"300","status":"Incomplete","booking_at":"14-10-2021 08:39 PM","user_name":"Maddy Sam","service_provider_name":"Sample Test"},{"_id":"616848689e7b943a38ec0880","user_id":"6163d60a489ccc3d894683d2","sp_id":"61275eccdc2406057b5aeb15","Appointment_id":"SP-1634224232752","image_url":"","bussiness_name":"Abc nanny service ","service_name":"Sub Service 2","service_hrs":"1","booking_time":"14-10-2021 8:40 PM","booking_cost":"300","status":"Incomplete","booking_at":"14-10-2021 08:40 PM","user_name":"Maddy Sam","service_provider_name":"Sample Test"},{"_id":"616858479e7b943a38ec0881","user_id":"6163d60a489ccc3d894683d2","sp_id":"61275eccdc2406057b5aeb15","Appointment_id":"SP-1634228295587","image_url":"","bussiness_name":"Abc nanny service ","service_name":"Sub Service 2","service_hrs":"1","booking_time":"14-10-2021 9:47 PM","booking_cost":"300","status":"Incomplete","booking_at":"14-10-2021 09:48 PM","user_name":"Maddy Sam","service_provider_name":"Sample Test"},{"_id":"61685b379e7b943a38ec0882","user_id":"6163d60a489ccc3d894683d2","sp_id":"61275eccdc2406057b5aeb15","Appointment_id":"SP-1634229047675","image_url":"","bussiness_name":"Abc nanny service ","service_name":"Sub Service 2","service_hrs":"1","booking_time":"14-10-2021 10:00 PM","booking_cost":"300","status":"Incomplete","booking_at":"14-10-2021 10:00 PM","user_name":"Maddy Sam","service_provider_name":"Sample Test"}]
     * Code : 200
     */

    private String Status;
    private String Message;
    private int Code;
    /**
     * _id : 61680377988d1d35aa95b007
     * user_id : 6163d60a489ccc3d894683d2
     * sp_id : 61275eccdc2406057b5aeb15
     * Appointment_id : SP-1634206583206
     * image_url :
     * bussiness_name : Abc nanny service
     * service_name : Sub Service 2
     * service_hrs : 1
     * booking_time : 14-10-2021 3:46 PM
     * booking_cost : 300
     * status : Incomplete
     * booking_at : 14-10-2021 03:46 PM
     * user_name : Maddy Sam
     * service_provider_name : Sample Test
     */

    private List<DataBean> Data;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int Code) {
        this.Code = Code;
    }

    public List<DataBean> getData() {
        return Data;
    }

    public void setData(List<DataBean> Data) {
        this.Data = Data;
    }

    public static class DataBean {
        private String _id;
        private String user_id;
        private String sp_id;
        private String Appointment_id;
        private String image_url;
        private String bussiness_name;
        private String service_name;
        private String service_hrs;
        private String booking_time;
        private String booking_cost;
        private String status;
        private String booking_at;
        private String user_name;
        private String service_provider_name;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getSp_id() {
            return sp_id;
        }

        public void setSp_id(String sp_id) {
            this.sp_id = sp_id;
        }

        public String getAppointment_id() {
            return Appointment_id;
        }

        public void setAppointment_id(String Appointment_id) {
            this.Appointment_id = Appointment_id;
        }

        public String getImage_url() {
            return image_url;
        }

        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }

        public String getBussiness_name() {
            return bussiness_name;
        }

        public void setBussiness_name(String bussiness_name) {
            this.bussiness_name = bussiness_name;
        }

        public String getService_name() {
            return service_name;
        }

        public void setService_name(String service_name) {
            this.service_name = service_name;
        }

        public String getService_hrs() {
            return service_hrs;
        }

        public void setService_hrs(String service_hrs) {
            this.service_hrs = service_hrs;
        }

        public String getBooking_time() {
            return booking_time;
        }

        public void setBooking_time(String booking_time) {
            this.booking_time = booking_time;
        }

        public String getBooking_cost() {
            return booking_cost;
        }

        public void setBooking_cost(String booking_cost) {
            this.booking_cost = booking_cost;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getBooking_at() {
            return booking_at;
        }

        public void setBooking_at(String booking_at) {
            this.booking_at = booking_at;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getService_provider_name() {
            return service_provider_name;
        }

        public void setService_provider_name(String service_provider_name) {
            this.service_provider_name = service_provider_name;
        }
    }
}
