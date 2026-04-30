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

    public static String str(String value) { // wraps a Java string in JSON double quotes after escaping it
        return "\"" + esc(value) + "\""; // returns a valid JSON string literal
    } // ends the str method

    public static String stringArray(List<String> values){
        if(values == null){
            return "[]"; //returns empty json array if null
        }
        StringBuilder sb = new StringBuilder("[");

        for(int i = 0; i < values.size();i++){
            if(i > 0){
                sb.append(", ");
            }
            sb.append(str(values.get(i)));
        }

        return sb.append("]").toString();
//returns full JSON string at the end.
    }

    public static String booleanField(String fieldName, boolean value) { // creates a single JSON boolean field fragment

        return "\"" + esc(fieldName) + "\": " + value; // returns something like "valid": true

    } // ends the booleanField method

    public static String numberField(String fieldName, double value) { // creates a single JSON numeric field fragment

        return "\"" + esc(fieldName) + "\": " + String.format(Locale.US, "%.4f", value); // returns something like "gc_ratio": 0.4615

    } // ends the numberField method

    public static String intField(String fieldName, int value) { // creates a single JSON integer field fragment

        return "\"" + esc(fieldName) + "\": " + value; // returns something like "mutation_count": 12

    } // ends the intField method

    public static String stringField(String fieldName, String value) { // creates a single JSON string field fragment

        return "\"" + esc(fieldName) + "\": " + str(value); // returns something like "sample_id": "sample-001"

    } // ends the stringField method

}
