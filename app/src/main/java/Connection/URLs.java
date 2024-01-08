package Connection;

public class URLs {
    private static final String ROOT_URL = "http://192.168.1.7/KrishokBondhu/";
    //sign in and sign up.
    public static final String URL_USER_CHECK           =  ROOT_URL + "UserCheck.php?";
    public static final String URL_USER_NID_DISPLAY     =  ROOT_URL + "registrationView.php?";
    public static final String URL_USER_NID_DONE        =  ROOT_URL + "Api.php?apiCall=regDone";
    //Field Page
    public static final String URL_FIELD_ADD            =  ROOT_URL + "Api.php?apiCall=AddField";
    public static final String URL_FIELD_DELETE         =  ROOT_URL + "field_delete_api.php?field_no=";
    public static final String URL_FIELD_LIST           = ROOT_URL +"field_api.php?field_owner=";

    //Notification
    public static final String URL_NOTIFICATION         = ROOT_URL +"notification_api.php?field_owner=";

    //Product Display
    public static final String URL_PRODUCT_LIST         = ROOT_URL +"product_api.php?field_owner=";

    //Payment
    public static final String URL_PAYMENT              = ROOT_URL + "Api.php?apiCall=payment";
    //Payment History
    public static final String URL_PAYMENT_HISTORY      = ROOT_URL + "payment_history_api.php?field_owner=";

    //Payment History
    public static final String URL_GNPL_PAYMENT_HISTORY  = ROOT_URL + "gnpl_payment_history_api.php?field_owner=";

    //GNPL Apply
    public static final String URL_GNPL_APPLY           = ROOT_URL + "Api.php?apiCall=GNPLAPPLY";


}
