package com.demo.genome.scanner;

import com.demo.genome.model.GenomeChunk;
import com.demo.genome.model.Mutation;

import java.util.ArrayList;
import java.util.List;

public class ReferenceComparator {

    public List<Mutation> compare(GenomeChunk referenceChunk, GenomeChunk sampleChunk){
        if(referenceChunk == null){
            throw new IllegalArgumentException("Reference chunk must not be null");
        }

        if(sampleChunk == null){
            throw new IllegalArgumentException("Sample chunk must not be null");
        }

        String referenceSequence = referenceChunk.sequence();
        String sampleSequence = sampleChunk.sequence();

        if(referenceSequence == null || sampleSequence == null){
            throw new IllegalArgumentException("Chunk sequences must not be null");
        }

        int compareLength = Math.min(referenceSequence.length(), sampleSequence.length()); // chooses the shorter sequence length so we do not read past either chunk
        List<Mutation> mutations = new ArrayList<>();

        for(int i = 0;i < compareLength;i++){
            char referenceBase = Character.toUpperCase(referenceSequence.charAt(i));
            char sampleBase = Character.toUpperCase(sampleSequence.charAt(i));

            if(referenceBase != sampleBase){
                int absolutePosition = referenceChunk.start() + i; // converts the chunk-relative index into an absolute chromosome position
                mutations.add(new Mutation(absolutePosition, referenceBase, sampleBase));
            }

        }

        return mutations;
    }

}
