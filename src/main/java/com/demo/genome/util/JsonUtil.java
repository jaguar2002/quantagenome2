package com.demo.genome.util;

import java.util.List; //to convert java lists into a json style array.
import java.util.Locale; // importing locale so decmial formatting uses a dot and not local-dependent commas

public class JsonUtil {

    private JsonUtil(){}

    public static String esc(String value){ //used to deal with special characters that mess with JSON formatting
        if(value == null){
            return "";
        }

        return value
                .replace("\\", "\\\\") // escapes backslashes so JSON does not break

                .replace("\"", "\\\"") // escapes double quotes so JSON strings stay valid

                .replace("\n", "\\n") // escapes newline characters

                .replace("\r", "\\r") // escapes carriage-return characters

                .replace("\t", "\\t");
    } //ends escape method

}
