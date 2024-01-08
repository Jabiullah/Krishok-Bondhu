package Model;

public class Field {
    private int field_id;
    private String land_serial_no;
    private String deed_nature;
    private String soil_property;

    private String moujar_name;
    private String ward_name;
    private String thana_name;

    private String deed_status;
    private String deed_owner;

    public Field(int field_id,String land_serial_no, String deed_nature, String  soil_property,String moujar_name, String  ward_name,String thana_name, String deed_status, String deed_owner) {
        this.field_id = field_id;
        this.land_serial_no = land_serial_no;
        this.deed_nature = deed_nature;
        this.soil_property = soil_property;

        this.moujar_name = moujar_name;
        this.ward_name = ward_name;
        this.thana_name = thana_name;

        this.deed_status = deed_status;
        this.deed_owner = deed_owner;

    }
    public int getField_id() {return field_id;}
    public String getLand_serial_no() {
        return land_serial_no;
    }
    public String getDeed_nature() {return deed_nature;}
    public String getSoil_property() {
        return soil_property;
    }

    public String getMoujar_name() {
        return moujar_name;
    }
    public String getWard_name() {
        return ward_name;
    }
    public String getThana_name() {
        return thana_name;
    }

    public String getDeed_status() {
        return deed_status;
    }
    public String getDeed_owner() {
        return deed_owner;
    }

}
