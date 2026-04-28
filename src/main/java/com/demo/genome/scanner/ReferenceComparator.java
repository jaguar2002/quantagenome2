package com.demo.genome.scanner;//defining the package

import com.demo.genome.model.GenomeChunk; //importing the chunk model so we can compare reference and sample chunks.
import com.demo.genome.model.Mutation; //importing mutation model so detected differences are returned.

import java.util.ArrayList; //dynamic list of mutations
import java.util.List; //importing list so a mutation collection is given.

public class ReferenceComparator { //declaring the class responsible for comparing the sample chunks to the reference chunk.

    public List<Mutation> compare(GenomeChunk referenceChunk, GenomeChunk sampleChunk){//compares two chunks base by base.

        if(referenceChunk == null){ //check whether reference chunk is missing or not
            throw new IllegalArgumentException("Reference chunk must not be null"); //thrown when there is no reference chunk thrown.
        }

        if(sampleChunk == null){//check whether sample chunk is missing or not
            throw new IllegalArgumentException("Sample chunk must not be null"); //throws a helpful error if the sample chunk is null
        }

        String referenceSequence = referenceChunk.sequence();//reads a DNA sequence from the reference chunk
        String sampleSequence = sampleChunk.sequence();//reads the DNA sequence from the sample chunk.

        if(referenceSequence == null || sampleSequence == null){ // reading the DNA sequence from reference chunk
            throw new IllegalArgumentException("Chunk sequences must not be null"); // throws a helpful error if either chunk has no sequence
        }

        int compareLength = Math.min(referenceSequence.length(), sampleSequence.length()); // chooses the shorter sequence length so we do not read past either chunk
        List<Mutation> mutations = new ArrayList<>(); //creating an empty list to hold all detected mutations

        for(int i = 0;i < compareLength;i++){ //loops through base position shared by both chunks.
            char referenceBase = Character.toUpperCase(referenceSequence.charAt(i)); //reading the current base from the sequence chunk
            char sampleBase = Character.toUpperCase(sampleSequence.charAt(i)); //reading current base in the sample base.

            if(referenceBase != sampleBase){ // checking if the two chunks differ at this position
                int absolutePosition = referenceChunk.start() + i; // converts the chunk-relative index into an absolute chromosome position
                mutations.add(new Mutation(absolutePosition, referenceBase, sampleBase)); // adding new mutations
            }

        }

        return mutations; //returns list of mutations
    }

}
