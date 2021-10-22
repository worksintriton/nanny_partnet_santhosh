package com.triton.nannypartners.requestpojo;

import java.util.List;

public class FetchServiceDataResponse {

    /**
     * Status : Success
     * Message : Service Detail
     * Data : [{"service_name":"Pet Grooming","subsericelist":[{"_id":"6164231b65d9a57d7fc9575e","isservice":false,"service_id":"5ff7f5171c72093650a13a14","title":"Sub Service 1"},{"_id":"6164232765d9a57d7fc9575f","isservice":true,"service_id":"5ff7f5171c72093650a13a14","title":"Sub Service 2"}]},{"service_name":" Pet Training","subsericelist":[{"_id":"6164232d65d9a57d7fc95760","isservice":true,"service_id":"5ff815d4414b1052a09bb2b1","title":"Sub Service 2"}]}]
     * Code : 200
     */

    private String Status;
    private String Message;
    private int Code;
    /**
     * service_name : Pet Grooming
     * subsericelist : [{"_id":"6164231b65d9a57d7fc9575e","isservice":false,"service_id":"5ff7f5171c72093650a13a14","title":"Sub Service 1"},{"_id":"6164232765d9a57d7fc9575f","isservice":true,"service_id":"5ff7f5171c72093650a13a14","title":"Sub Service 2"}]
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
        private String service_name;
        /**
         * _id : 6164231b65d9a57d7fc9575e
         * isservice : false
         * service_id : 5ff7f5171c72093650a13a14
         * title : Sub Service 1
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

        public static class SubsericelistBean {
            private String _id;
            private boolean isservice;
            private String service_id;
            private String title;

            public String get_id() {
                return _id;
            }

            public void set_id(String _id) {
                this._id = _id;
            }

            public boolean isIsservice() {
                return isservice;
            }

            public void setIsservice(boolean isservice) {
                this.isservice = isservice;
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
        }
    }
}
