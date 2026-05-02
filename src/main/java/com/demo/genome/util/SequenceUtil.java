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

    public static double gcRatio(String sequence){ // calculating the base ratio that is G or C
        String normalized = normalize(sequence); //normalizing input

        if(normalized.isEmpty()){ //checks if the sequence is empty or not.
            return 0.0;//returns 0 if no sequence is there to measure.
        }//ending the empty sequence.

        int gcCount = 0; //starting a counter for G and C bases.
        int validCount = 0; //starting a counter for canonical bases.

        for(int i = 0; i < normalized.length();i++){ //for loop
            char base = normalized.charAt(i); //checks each base in the input

            if(isCanonicalBase(base)){
                validCount++;//increasing the count of every VALID base found

                if(base == 'G' || base == 'C'){
                    gcCount++;//incrementing the GC base count.
                } //ending the canonical base loop
            }//ends the loop over sequence.

        }

        if(validCount == 0){ //checks if there are zero canonical bases
            return 0.0; //returns 0 to avoid a divide by zero error.
        }

        return (double) gcCount / validCount;//returns the gc count ratio over the total valid count.

    }

    public static int countMotifOccurrences(String sequence, String motif){ //counts how many motifs appear in a sequence

        String normalizedSequence = normalize(sequence); //normalizes the sequence to uppercase without whitespace.
        String normalizedMotif = normalize(motif); //motiff  without extra characters or whitespaces.

        if(normalizedSequence.isEmpty() || normalizedMotif.isEmpty()){ //checking wheter the sequence or motif is empty.
            return 0; //returns 0 if nothing meaningful to search is found.
        }

        int motifCount = 0; //starts a counter for motif occurences.
        int fromIndex = 0; //starts the search from the beginning of the sequence.

        while(true){ //repeated searches for the motif until no more are there.
            int matchIndex = normalizedSequence.indexOf(normalizedMotif,fromIndex); // finds the next occured motif
            if(matchIndex < 0){ //checks if no more matches exist.
                break; //exits the search loop
            }//ends no more motif search loop

            motifCount++; //increasing the motif count
            fromIndex = matchIndex + 1; //moves one character forward.
        }

        return motifCount; //returns the full motiff count.

    }

    public static String safeSubsequences(String sequence, int startInclusive, int endInclusive){ //extracting a subsqeuence from the given DNA document

        String normalized = normalize(sequence);// normalizing the sequence before slicing

        if(normalized.isEmpty()){ //checking if the sequence is empty.
            return ""; //returning blank if empty
        }

        int safeStart = Math.max(0, startInclusive); //clamping the start index so it cannot go bellow 0
        int safeEnd = Math.min(normalized.length(),endInclusive); //clamps the end so the loop  does not go beyond a certain length.

        if(safeStart >= safeEnd){
            return "";
        }

        return normalized.substring(safeStart,safeEnd);

    }



    public static String reverseComplement(String sequence) { // computes the reverse complement of a DNA sequence

        String normalized = normalize(sequence); // normalizes the input sequence before processing
        StringBuilder builder = new StringBuilder(); // creates a StringBuilder to build the reverse complement efficiently
        for (int i = normalized.length() - 1; i >= 0; i--) { // loops backward through the sequence so the output becomes reversed
            char base = normalized.charAt(i); // reads the current base from the original sequence
            builder.append(complement(base)); // appends the complement of that base into the output
        } // ends the reverse traversal loop

        return builder.toString(); // returns the completed reverse-complement sequence

    } // ends the reverseComplement method

    public static char complement(char base) { // returns the DNA complement for one base
        return switch (Character.toUpperCase(base)) { // switches on the uppercase version of the incoming base
            case 'A' -> 'T'; // maps A to T
            case 'T' -> 'A'; // maps T to A
            case 'C' -> 'G'; // maps C to G
            case 'G' -> 'C'; // maps G to C
            default -> 'N'; // maps any unknown base to N
        }; // ends the switch expression

    } // ends the complement method





}
