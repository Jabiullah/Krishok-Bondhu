package Model;

public class ProductModel {
    private String product_id;
    private String product_name;
    private String product_description;
    private String product_weight;
    private String product_price;

    private String product_reduce_price;
    private String product_timelimit;


    public ProductModel(String product_id, String product_name, String product_price, String product_description, String product_weight, String product_reduce_price, String product_timelimit) {
        this.product_id             = product_id;
        this.product_name           = product_name;
        this.product_price          = product_price;
        this.product_description    = product_description;
        this.product_weight         = product_weight;
        this.product_reduce_price   = product_reduce_price;
        this.product_timelimit      = product_timelimit;

    }
    public String getProduct_id() {
        return product_id;
    }
    public String getProduct_name() {return product_name;}
    public String getProduct_price() {
        return product_price;
    }
    public String getProduct_description() {return product_description;}
    public String getProduct_weight() {
        return product_weight;
    }
    public String getProduct_reduce_price() {
        return product_reduce_price;
    }
    public String getProduct_timelimit() {
        return product_timelimit;
    }
}

