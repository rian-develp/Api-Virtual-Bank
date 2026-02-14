package com.example.virtualbank.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Utils {

    public static String removeMasksFromPhoneNumber(String phoneNumber){
        return phoneNumber.replaceAll("\\D", "");
    }

    public static LocalDate convertString(String strDate){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(strDate, formatter);
    }
}
