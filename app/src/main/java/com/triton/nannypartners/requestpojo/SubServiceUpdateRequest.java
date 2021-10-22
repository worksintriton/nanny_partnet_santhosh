package com.triton.nannypartners.requestpojo;

import java.util.List;

public class SubServiceUpdateRequest {


    /**
     * user_id : 616a83bf9e7b943a38ec0883
     * service_id : 5ff7f5171c72093650a13a14
     * service_name : Pet Grooming
     * sub_service_data : [{"_id":"6164231b65d9a57d7fc9575e","service_id":"5ff7f5171c72093650a13a14","title":"Sub Service 1","isservice":true},{"_id":"6164232765d9a57d7fc9575f","service_id":"5ff7f5171c72093650a13a14","title":"Sub Service 2","isservice":false}]
     */

    private String user_id;
    private String service_id;
    private String service_name;
    /**
     * _id : 6164231b65d9a57d7fc9575e
     * service_id : 5ff7f5171c72093650a13a14
     * title : Sub Service 1
     * isservice : true
     */

    private List<SubServiceDataBean> sub_service_data;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public List<SubServiceDataBean> getSub_service_data() {
        return sub_service_data;
    }

    public void setSub_service_data(List<SubServiceDataBean> sub_service_data) {
        this.sub_service_data = sub_service_data;
    }

    public static class SubServiceDataBean {
        private String _id;
        private String service_id;
        private String title;
        private boolean isservice;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getService_id() {
            return service_id;
        }

        public void setService_id(String service_id) {
            this.service_id = service_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean isIsservice() {
            return isservice;
        }

        public void setIsservice(boolean isservice) {
            this.isservice = isservice;
        }
    }
}
