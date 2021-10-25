package com.triton.nannypartners.requestpojo;

public class TimeCalRequest {


    /**
     * _id : 6176a1378dd3e15b142de08e
     * total_hours :
     * additional_hours :
     * addition_amount :
     * addition_payment_method :
     * addition_payment_status :
     * total_paid_amount :
     * work_status :
     */

    private String _id;
    private String total_hours;
    private String additional_hours;
    private String addition_amount;
    private String addition_payment_method;
    private String addition_payment_status;
    private String total_paid_amount;
    private String work_status;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTotal_hours() {
        return total_hours;
    }

    public void setTotal_hours(String total_hours) {
        this.total_hours = total_hours;
    }

    public String getAdditional_hours() {
        return additional_hours;
    }

    public void setAdditional_hours(String additional_hours) {
        this.additional_hours = additional_hours;
    }

    public String getAddition_amount() {
        return addition_amount;
    }

    public void setAddition_amount(String addition_amount) {
        this.addition_amount = addition_amount;
    }

    public String getAddition_payment_method() {
        return addition_payment_method;
    }

    public void setAddition_payment_method(String addition_payment_method) {
        this.addition_payment_method = addition_payment_method;
    }

    public String getAddition_payment_status() {
        return addition_payment_status;
    }

    public void setAddition_payment_status(String addition_payment_status) {
        this.addition_payment_status = addition_payment_status;
    }

    public String getTotal_paid_amount() {
        return total_paid_amount;
    }

    public void setTotal_paid_amount(String total_paid_amount) {
        this.total_paid_amount = total_paid_amount;
    }

    public String getWork_status() {
        return work_status;
    }

    public void setWork_status(String work_status) {
        this.work_status = work_status;
    }
}
