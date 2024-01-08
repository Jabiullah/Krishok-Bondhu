package Model;

public class User_nid {
    //$name, $nidNo, $address, $birthPlace, $postCode
    private String name, nidNo, address, birthPlace, postCode;

    public User_nid(String name, String nidNo, String address, String birthPlace,String postCode) {
        this.name = name;
        this.nidNo = nidNo;
        this.address = address;
        this.birthPlace = birthPlace;
        this.postCode = postCode;
    }



    public String getName(){ return name;}

    public String getNidNo() {
        return nidNo;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public String getPostCode(){ return postCode; }

    public String getAddress(){return address;}

}
