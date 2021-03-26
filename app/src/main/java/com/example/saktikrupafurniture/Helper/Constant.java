package com.example.saktikrupafurniture.Helper;

public class Constant {
    public static final String ROOT_URL = "https://shaktikrupaapi.000webhostapp.com/ShaktiKrupa/V1/";
    public static final String URL_STRING_LOGIN = ROOT_URL + "loginCustomer.php";
    public static final String URL_STRING_REGISTER = ROOT_URL + "RegisterCustomer.php";
    public static final String URL_STRING_REWARD = ROOT_URL + "GetReward.php";
    public static final String URL_STRING_REWARD_SUM = ROOT_URL + "GetRewardSum.php";
    public static final String URL_STRING_CHECK_UPDATE = ROOT_URL + "CheckUpdate.php";
    public static final String URL_STRING_CHECK_CUSTOMERDATA = ROOT_URL + "CustomerData.php";
    public static final String URL_STRING_CHECK_DELETEREWARD = ROOT_URL + "DeleteReward.php";

    public static final int CODE_POST_REQUEST = 1;
    public static final int CODE_GET_REQUEST = 0;

    public static boolean isValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }
}
