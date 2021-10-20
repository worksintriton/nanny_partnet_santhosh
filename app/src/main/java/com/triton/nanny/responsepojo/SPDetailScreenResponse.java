package com.triton.nanny.responsepojo;

import java.util.List;

public class SPDetailScreenResponse {


    /**
     * Status : Success
     * Message : Service Provider Details
     * Data : {"bus_service_list":[{"amount":100,"time_slots":"60 mins","bus_service_list":" Pet Training"},{"amount":100,"time_slots":"60 mins","bus_service_list":"Pet daycare"},{"amount":200,"time_slots":"60 mins","bus_service_list":"Vet Care 2"},{"amount":100,"time_slots":"60 mins","bus_service_list":"Pet daycare 1"},{"amount":200,"time_slots":"60 mins","bus_service_list":"Pet daycare 2"}],"bus_spec_list":[{"bus_spec_list":"Kennel Cut"},{"bus_spec_list":"Teddy Bear Trim"},{"bus_spec_list":"Breed Trims"},{"bus_spec_list":"Full Coat / Show Trims"}],"bus_service_gall":[{"bus_service_gall":"http://54.193.18.54:3000/api/uploads/61275eccdc2406057b5aeb152608175750"}],"bus_certif":[{"bus_certif":"http://54.193.18.54:3000/api/uploads/61275eccdc2406057b5aeb15certificate.pdf"}],"_id":"6127891a4f7e5d1561c63783","user_id":"61275eccdc2406057b5aeb15","bus_user_name":"Sample","bus_user_email":"","bussiness_name":"Abc nanny service ","bus_user_phone":"2342342344","bus_profile":"http://54.193.18.54:3000/api/uploads/61275eccdc2406057b5aeb152608175858","bus_proof":"http://54.193.18.54:3000/api/uploads/61275eccdc2406057b5aeb15certificate.pdf","date_and_time":"26-08-2021 05:59 PM","mobile_type":"IOS","profile_status":true,"profile_verification_status":"Verified","thumbnail_image":"","sp_loc":"5-38A, 2nd Main Rd, Ram Nagar, Karnam Street, Karunabigai Colony, Velachery, Chennai, Tamil Nadu 600042, India","sp_lat":12.983360290527344,"sp_long":80.22343632290122,"city_name":"Chennai","delete_status":false,"calender_status":true,"comments":0,"rating":5,"updatedAt":"2021-08-26T12:37:43.458Z","createdAt":"2021-08-26T12:29:14.243Z","__v":0}
     * banner : [{"title":"title1","image_path":"http://54.212.108.156:3000/api/uploads/1625687015621.png"},{"title":"title1","image_path":"http://54.212.108.156:3000/api/uploads/1625687032584.png"},{"title":"title1","image_path":"http://54.212.108.156:3000/api/uploads/1625687050185.png"},{"title":"title1","image_path":"http://54.212.108.156:3000/api/uploads/1625687064021.png"},{"title":"title1","image_path":"http://54.212.108.156:3000/api/uploads/1625687086700.png"}]
     * Code : 200
     */

    private String Status;
    private String Message;
    /**
     * bus_service_list : [{"amount":100,"time_slots":"60 mins","bus_service_list":" Pet Training"},{"amount":100,"time_slots":"60 mins","bus_service_list":"Pet daycare"},{"amount":200,"time_slots":"60 mins","bus_service_list":"Vet Care 2"},{"amount":100,"time_slots":"60 mins","bus_service_list":"Pet daycare 1"},{"amount":200,"time_slots":"60 mins","bus_service_list":"Pet daycare 2"}]
     * bus_spec_list : [{"bus_spec_list":"Kennel Cut"},{"bus_spec_list":"Teddy Bear Trim"},{"bus_spec_list":"Breed Trims"},{"bus_spec_list":"Full Coat / Show Trims"}]
     * bus_service_gall : [{"bus_service_gall":"http://54.193.18.54:3000/api/uploads/61275eccdc2406057b5aeb152608175750"}]
     * bus_certif : [{"bus_certif":"http://54.193.18.54:3000/api/uploads/61275eccdc2406057b5aeb15certificate.pdf"}]
     * _id : 6127891a4f7e5d1561c63783
     * user_id : 61275eccdc2406057b5aeb15
     * bus_user_name : Sample
     * bus_user_email :
     * bussiness_name : Abc nanny service
     * bus_user_phone : 2342342344
     * bus_profile : http://54.193.18.54:3000/api/uploads/61275eccdc2406057b5aeb152608175858
     * bus_proof : http://54.193.18.54:3000/api/uploads/61275eccdc2406057b5aeb15certificate.pdf
     * date_and_time : 26-08-2021 05:59 PM
     * mobile_type : IOS
     * profile_status : true
     * profile_verification_status : Verified
     * thumbnail_image :
     * sp_loc : 5-38A, 2nd Main Rd, Ram Nagar, Karnam Street, Karunabigai Colony, Velachery, Chennai, Tamil Nadu 600042, India
     * sp_lat : 12.983360290527344
     * sp_long : 80.22343632290122
     * city_name : Chennai
     * delete_status : false
     * calender_status : true
     * comments : 0
     * rating : 5
     * updatedAt : 2021-08-26T12:37:43.458Z
     * createdAt : 2021-08-26T12:29:14.243Z
     * __v : 0
     */

    private DataBean Data;
    private int Code;
    /**
     * title : title1
     * image_path : http://54.212.108.156:3000/api/uploads/1625687015621.png
     */

    private List<BannerBean> banner;

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

    public DataBean getData() {
        return Data;
    }

    public void setData(DataBean Data) {
        this.Data = Data;
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int Code) {
        this.Code = Code;
    }

    public List<BannerBean> getBanner() {
        return banner;
    }

    public void setBanner(List<BannerBean> banner) {
        this.banner = banner;
    }

    public static class DataBean {
        private String _id;
        private String user_id;
        private String bus_user_name;
        private String bus_user_email;
        private String bussiness_name;
        private String bus_user_phone;
        private String bus_profile;
        private String bus_proof;
        private String date_and_time;
        private String mobile_type;
        private boolean profile_status;
        private String profile_verification_status;
        private String thumbnail_image;
        private String sp_loc;
        private double sp_lat;
        private double sp_long;
        private String city_name;
        private boolean delete_status;
        private boolean calender_status;
        private int comments;
        private int rating;
        private String updatedAt;
        private String createdAt;
        private int __v;
        /**
         * amount : 100
         * time_slots : 60 mins
         * bus_service_list :  Pet Training
         */

        private List<BusServiceListBean> bus_service_list;
        /**
         * bus_spec_list : Kennel Cut
         */

        private List<BusSpecListBean> bus_spec_list;
        /**
         * bus_service_gall : http://54.193.18.54:3000/api/uploads/61275eccdc2406057b5aeb152608175750
         */

        private List<BusServiceGallBean> bus_service_gall;
        /**
         * bus_certif : http://54.193.18.54:3000/api/uploads/61275eccdc2406057b5aeb15certificate.pdf
         */

        private List<BusCertifBean> bus_certif;

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

        public String getBus_user_name() {
            return bus_user_name;
        }

        public void setBus_user_name(String bus_user_name) {
            this.bus_user_name = bus_user_name;
        }

        public String getBus_user_email() {
            return bus_user_email;
        }

        public void setBus_user_email(String bus_user_email) {
            this.bus_user_email = bus_user_email;
        }

        public String getBussiness_name() {
            return bussiness_name;
        }

        public void setBussiness_name(String bussiness_name) {
            this.bussiness_name = bussiness_name;
        }

        public String getBus_user_phone() {
            return bus_user_phone;
        }

        public void setBus_user_phone(String bus_user_phone) {
            this.bus_user_phone = bus_user_phone;
        }

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

        public String getThumbnail_image() {
            return thumbnail_image;
        }

        public void setThumbnail_image(String thumbnail_image) {
            this.thumbnail_image = thumbnail_image;
        }

        public String getSp_loc() {
            return sp_loc;
        }

        public void setSp_loc(String sp_loc) {
            this.sp_loc = sp_loc;
        }

        public double getSp_lat() {
            return sp_lat;
        }

        public void setSp_lat(double sp_lat) {
            this.sp_lat = sp_lat;
        }

        public double getSp_long() {
            return sp_long;
        }

        public void setSp_long(double sp_long) {
            this.sp_long = sp_long;
        }

        public String getCity_name() {
            return city_name;
        }

        public void setCity_name(String city_name) {
            this.city_name = city_name;
        }

        public boolean isDelete_status() {
            return delete_status;
        }

        public void setDelete_status(boolean delete_status) {
            this.delete_status = delete_status;
        }

        public boolean isCalender_status() {
            return calender_status;
        }

        public void setCalender_status(boolean calender_status) {
            this.calender_status = calender_status;
        }

        public int getComments() {
            return comments;
        }

        public void setComments(int comments) {
            this.comments = comments;
        }

        public int getRating() {
            return rating;
        }

        public void setRating(int rating) {
            this.rating = rating;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public int get__v() {
            return __v;
        }

        public void set__v(int __v) {
            this.__v = __v;
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

        public List<BusServiceGallBean> getBus_service_gall() {
            return bus_service_gall;
        }

        public void setBus_service_gall(List<BusServiceGallBean> bus_service_gall) {
            this.bus_service_gall = bus_service_gall;
        }

        public List<BusCertifBean> getBus_certif() {
            return bus_certif;
        }

        public void setBus_certif(List<BusCertifBean> bus_certif) {
            this.bus_certif = bus_certif;
        }

        public static class BusServiceListBean {
            private int amount;
            private String time_slots;
            private String bus_service_list;

            public int getAmount() {
                return amount;
            }

            public void setAmount(int amount) {
                this.amount = amount;
            }

            public String getTime_slots() {
                return time_slots;
            }

            public void setTime_slots(String time_slots) {
                this.time_slots = time_slots;
            }

            public String getBus_service_list() {
                return bus_service_list;
            }

            public void setBus_service_list(String bus_service_list) {
                this.bus_service_list = bus_service_list;
            }
        }

        public static class BusSpecListBean {
            private String bus_spec_list;

            public String getBus_spec_list() {
                return bus_spec_list;
            }

            public void setBus_spec_list(String bus_spec_list) {
                this.bus_spec_list = bus_spec_list;
            }
        }

        public static class BusServiceGallBean {
            private String bus_service_gall;

            public String getBus_service_gall() {
                return bus_service_gall;
            }

            public void setBus_service_gall(String bus_service_gall) {
                this.bus_service_gall = bus_service_gall;
            }
        }

        public static class BusCertifBean {
            private String bus_certif;

            public String getBus_certif() {
                return bus_certif;
            }

            public void setBus_certif(String bus_certif) {
                this.bus_certif = bus_certif;
            }
        }
    }

    public static class BannerBean {
        private String title;
        private String image_path;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImage_path() {
            return image_path;
        }

        public void setImage_path(String image_path) {
            this.image_path = image_path;
        }
    }
}
