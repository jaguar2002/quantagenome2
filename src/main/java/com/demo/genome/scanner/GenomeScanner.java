package com.demo.genome.scanner;//scanner package

import com.demo.genome.model.GenomeChunk;// chunk model used to find chunk pairs.
import com.demo.genome.model.ScanFinding; //imports the model because this class returns many findings.

import java.util.ArrayList;//importing an array list to build dynamic list of findings.
import java.util.List; //importing a list so we can work with chunk lists and finding lists
import java.util.Optional; //importing optional so the chunk scanner might

public class GenomeScanner { //top level class to orchestrate chunk by chunk scanning

    private final ChunkScanner chunkScanner; // storing the chunk scanner one pair at a time.

    public GenomeScanner(ChunkScanner chunkScanner){ //constructor receiving chunk scanner dependency
        this.chunkScanner = chunkScanner; //stores the chunk scanner
    }//ends the constructor

    public List<ScanFinding> scanAll(List<GenomeChunk> referenceChunks, List<GenomeChunk> sampleChunks) {// main method: scans all chunk pairs and returns findings.
        if (referenceChunks == null || sampleChunks == null) {//check if the list is missing.
            throw new IllegalArgumentException("Reference chunks and sample chunks must not be null."); // throws an error if the list was null before
        }

        List<ScanFinding> findings = new ArrayList<>();//list of all findings in the chunks for sensitive data.

        for(int i = 0; i < referenceChunks.size();i++){ //looping through all chunk indexes.

            GenomeChunk referenceChunk = referenceChunks.get(i);// reading the current sample chunk.
            GenomeChunk sampleChunk = sampleChunks.get(i);//reading the current sample chunk.

            validateChunkAlignment(referenceChunk,sampleChunk); //checking the reference and sample chunks to correspond to the same genomic region.
            Optional<ScanFinding> finding = chunkScanner.scan(referenceChunk,sampleChunk); //scanning the chunk pair that may produce a finding.
            finding.ifPresent(findings::add);//adding the finding to the final list if one exists
        }//ending the loop over all chunk pairs.

        return findings; //returning the full list of findings
    }

    private void validateChunkAlignment(GenomeChunk referenceChunk, GenomeChunk sampleChunk) { // helper method to verify chunk pairs to the same genomic region.

            if(!referenceChunk.chromosome().equals(sampleChunk.chromosome())){
                throw new IllegalArgumentException("Mismatched chromosomes: " + referenceChunk.chromosome());

            }

            if(referenceChunk.start() != sampleChunk.start()){
                throw new IllegalArgumentException("Mismatched chunk start positions");
            }

    }

}
