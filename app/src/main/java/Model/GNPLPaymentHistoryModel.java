package Model;

public class GNPLPaymentHistoryModel {
    private String date_history;
    private String product_name;
    private String product_quantity;
    private String product_mainValue;
    private String total_cost;
    private String field_value;
    private String gnpl_status;
    private String gnpl_payment;
    //historyPage Purpose

    public GNPLPaymentHistoryModel(String date_history,String product_name, String product_quantity,String product_mainValue,String total_cost, String field_value, String gnpl_status, String gnpl_payment) {
        this.date_history = date_history;
        this.product_name = product_name;
        this.product_quantity = product_quantity;
        this.product_mainValue = product_mainValue;
        this.total_cost = total_cost;
        this.field_value= field_value;
        this.gnpl_status= gnpl_status;
        this.gnpl_payment= gnpl_payment;
    }

    public String getDate_history() {return date_history;}

    public String getTotal_cost() {
        return total_cost;
    }

    public String getField_value() {
        return field_value;
    }


    public String getGnpl_status() {
        return gnpl_status;
    }

    public String getProduct_mainValue() {
        return product_mainValue;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getProduct_quantity() {
        return product_quantity;
    }

    public void setGnpl_payment(String gnpl_payment) {
        this.gnpl_payment = gnpl_payment;
    }
}
