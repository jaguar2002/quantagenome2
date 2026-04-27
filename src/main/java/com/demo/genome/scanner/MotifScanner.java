package com.demo.genome.scanner;

import com.demo.genome.model.GenomeChunk;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class MotifScanner {
    private final List<String> motifs;

    public MotifScanner(List<String> motifs){

        if(motifs == null || motifs.isEmpty()){
            throw new IllegalArgumentException("Motifs list must be null or empty.");
        }

        this.motifs = motifs;

    }

    public  MotifScanResult scan(GenomeChunk chunk){
        if(chunk == null){
            throw new IllegalArgumentException("Chunk must not be null");
        }

        String sequence = chunk.sequence();

        if(sequence == null){
            throw new IllegalArgumentException("");
        }

        String normalizedSequence = sequence.toUpperCase();
        boolean[] coverageMask = new boolean[normalizedSequence.length()];
        Set<String> matchedMotifSet = new LinkedHashSet<>();
        int occurenceCount = 0;

        for(String motif : motifs){
            String normalizedMotif = motif.toUpperCase();
            int fromIndex = 0;

            while(true){
                int matchIndex = normalizedSequence.indexOf(normalizedMotif,fromIndex);

                if(matchIndex < 0){
                    break;
                }

                matchedMotifSet.add(normalizedMotif);
                occurenceCount++;

                for(int i = matchIndex; i < matchIndex + normalizedMotif.length() && i < coverageMask.length;i++){
                    coverageMask[i] = true;
                }

                fromIndex = matchIndex + 1;


            }
        }

        int sensitiveBasesCovered = 0;
        for(boolean covered : coverageMask){
            if(covered){
                sensitiveBasesCovered++;
            }
        }




    }

}
