package model;

public class Util {

    public static String generateCode() {
        double r = Math.random();
        int x = (int) (r * 1000000);
        return String.format("%06d", x);
    }
    
    public static boolean isEmailValid(String email) {
        return email.matches("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
    }
    
    public static boolean isPasswordValid(String password) {
        return password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=]).{8,12}$");
    }
    public static boolean isCodeValid(String code){
        return code.matches("^\\d{4,5}$");
    }
    
    public static boolean isInteger(String value){
        return value.matches("^\\d+$");
    }
    
    public static boolean isDouble(String value){
        return value.matches("^\\d+(\\.\\d{2})?$");
    }
    
}
