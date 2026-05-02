package com.demo.genome.io; //puts this inside the IO package.

import com.demo.genome.util.SequenceUtil; // importing sequenceutil so we can normalize DNA sequences after reading them

import java.io.BufferedReader; //for reading the text in the fasta file
import java.io.IOException; // importing the IOException for file-reading failures.
import java.nio.file.Files; //imports files so we can open a reader from a file.
import java.nio.file.Path; //Importing the path class so we can work with the file paths clearly

public class FastaReader { //FASTA file format file reader

    public FastaSequence read(Path fastaPath) throws IOException {
        if(fastaPath == null){
            throw new IllegalArgumentException("FASTA path must not be null.");
        }

        if(!Files.exists(fastaPath)){
            throw new IllegalArgumentException("FASTA file does not exist: " + fastaPath);
        }

        String header = null; //starts with no FASTA header found yet
        StringBuilder sequenceBuilder = new StringBuilder(); //creating a builder to create all the sequences officially.

        try(BufferedReader reader = Files.newBufferedReader(fastaPath)){ //uses the buffered reader to go through the fasta file.
            String line; //declaring a variable to hold each line.
            while((line = reader.readLine()) != null){ //looping through each line in the fasta file.
                line = line.trim(); // removes whitespace from the current line.

                if(line.isEmpty()){//checking whether the current line is blank.
                    continue; //skips blank lines so they do not pollute the sequence.
                } //ends the blank line check.

                if(line.startsWith(">")){ //checking for a fasta header file.
                    if(header == null){//checks if this is the first header line encountered
                        header = line.substring(1).trim(); // stores a new header with the > removed.
                    } else{//checks for a second header.
                        throw new IllegalArgumentException("Expected a single-sequence FASTA file but found multiple headers in: " + fastaPath); // throws an error because this reader expects one sequence per FASTA file
                    }
                }
                else{
                    sequenceBuilder.append(line);//else it appends the line to te sequence builder.
                }

            }
        }

    }

    public record FastaSequence(String header,String sequence){

    }

}
