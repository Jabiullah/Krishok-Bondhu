package Model;

public class PaymentHistoryModel {
    private String date_history;
    private String info_history;
    private String payment_value;
    private String payment_process;
    private String transaction_id;
    private String buy_structure;
    //historyPage Purpose

    public PaymentHistoryModel(String date_history,String info_history, String payment_value,String payment_process,String transaction_id, String buy_structure) {
        this.date_history = date_history;
        this.info_history = info_history;
        this.payment_value = payment_value;
        this.payment_process = payment_process;
        this.transaction_id = transaction_id;
        this.buy_structure= buy_structure;
    }

    public String getDate_history() {return date_history;}
    public String getInfo_history() {return info_history;}
    public String getPayment_value() {return payment_value;}
    public String getPayment_process() {return payment_process;}
    public String getTransaction_id () {return transaction_id;}

    public String getBuy_structure() {
        return buy_structure;
    }
}
