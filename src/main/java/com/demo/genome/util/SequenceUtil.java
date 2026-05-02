package com.demo.genome.util; // putting in the util package

public class SequenceUtil { //utility class for DNA sequencing methods

    private SequenceUtil(){}
    public static String normalize(String sequence){
        if(sequence == null){
            return "";
        }//returns with blank string if a sequence is null
        return sequence.toUpperCase().replaceAll("\\s+","");//removing white spaces and keeping the string uppercase
    }

    public static boolean isCanonicalBase(char base){ //fixing any character in the string that is not A G C T
        char normalizedBase = Character.toUpperCase(base);//converts the string to upper case

        return normalizedBase == 'A' || normalizedBase == 'C' || normalizedBase == 'G' || normalizedBase == 'T'; //returning the normalized base
    }

    public static int countCanonicalBase(String sequence){ //count number of bases in sequence
        String normalized = normalize(sequence); //normalizing input sequence
        int count = 0; //string count.

        for(int i = 0;i < normalized.length();i++){ //looping through all the characters in the sequence.
            if(isCanonicalBase(normalized.charAt(i))){ //check if the current character is A,C,G, or T.
                count++;//increasing the count for every character that is a base
            }
        }
        return count; //returns taht count.
    }

    public static double gcRatio(String sequence){

    }





}
