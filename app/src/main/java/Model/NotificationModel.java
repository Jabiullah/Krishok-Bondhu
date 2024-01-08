package Model;

public class NotificationModel {
    private String date;
    private String info;

    public NotificationModel(String date, String info) {
        this.date = date;
        this.info = info;
    }
    public String getDate() {
        return date;
    }
    public String getInfo() {return info;}
}
