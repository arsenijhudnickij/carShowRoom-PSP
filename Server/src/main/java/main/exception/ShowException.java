package main.exception;

import java.util.Arrays;

public class ShowException {
    public static void showNotice(Exception e){
        System.out.println(e.getMessage());
        System.out.println(Arrays.toString(e.getStackTrace()));
    }
}
