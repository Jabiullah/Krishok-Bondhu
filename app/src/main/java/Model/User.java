package Model;

public class User {
    private int user_id;
    private String user_phone,user_name, user_nid, user_address, user_birthPlace;

    public User(int user_id, String user_phone, String user_name, String user_nid,String user_birthPlace, String user_address) {
        this.user_id = user_id;
        this.user_phone = user_phone;
        this.user_name = user_name;
        this.user_nid = user_nid;
        this.user_address = user_address;
        this.user_birthPlace = user_birthPlace;
    }

    public int getId() {
        return user_id;
    }

    public String getName(){ return user_name;}

    public String getPhone() {
        return user_phone;
    }

    public String getNID() {
        return user_nid;
    }

    public String getUser_birthPlace(){ return user_birthPlace; }

    public String getAddress(){return user_address;}


}
