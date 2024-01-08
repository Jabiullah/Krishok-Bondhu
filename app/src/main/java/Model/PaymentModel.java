package Model;

public class PaymentModel {
    private String info_history;
    private String payment_value;
    private String payment_process;
    private String farmer_no;
    private String transaction_id;
    private String buy_structure;

    public PaymentModel(String info_history, String payment_value,String payment_process, String farmer_no, String transaction_id, String buy_structure) {
        this.info_history = info_history;
        this.payment_value = payment_value;
        this.payment_process = payment_process;
        this.farmer_no = farmer_no;
        this.transaction_id = transaction_id;
        this.buy_structure = buy_structure;

    }

    public String getInfo_history() {return info_history;}
    public String getPayment_value() {return payment_value;}
    public String getPayment_process() {return payment_process;}
    public String getFarmer_no() {return farmer_no;}
    public String getTransaction_id () {return transaction_id;}
}
