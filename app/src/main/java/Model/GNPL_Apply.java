package Model;

public class GNPL_Apply {
    private String product_name;
    private String product_quantity;
    private String product_mainValue;
    private String total_cost;
    private String field_value;
    private String farmer_serial;

    public GNPL_Apply(String product_name, String product_quantity,String product_mainValue, String total_cost, String field_value,String farmer_serial) {
        this.product_name = product_name;
        this.product_quantity = product_quantity;
        this.product_mainValue = product_mainValue;
        this.total_cost = total_cost;
        this.field_value = field_value;
        this.farmer_serial = farmer_serial;
    }

    public String getProduct_name() {return product_name;}
    public String getProduct_quantity() {return product_quantity;}
    public String getProduct_mainValue() {return product_mainValue;}
    public String getTotal_cost() {return total_cost;}
    public String getField_value () {return field_value;}
    public String getFarmer_serial() {return farmer_serial;}
}
