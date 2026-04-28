package com.demo.genome.scanner; // puts this class inside the scanner package

import com.demo.genome.model.GenomeChunk; // imports the chunk model because we scan motifs inside one chunk sequence

import java.util.ArrayList; // imports ArrayList so we can build a list of matched motifs
import java.util.LinkedHashSet; // imports LinkedHashSet so we can keep matched motifs unique while preserving insertion order
import java.util.List; // imports List so we can return result collections
import java.util.Set; // imports Set so we can use a unique collection for motif names

public class MotifScanner { // declares the class responsible for scanning one chunk for configured motifs

    private final List<String> motifs; // stores the list of motifs/SSR markers that this scanner should search for

    public MotifScanner(List<String> motifs) { // constructor that receives the list of motifs to scan for
        if (motifs == null || motifs.isEmpty()) { // checks whether the incoming motif list is missing or empty
            throw new IllegalArgumentException("Motif list must not be null or empty."); // throws an error if the scanner is created without any motifs
        } // ends the motif-list validation

        this.motifs = motifs; // stores the configured motif list inside the scanner
    } // ends the constructor

    public MotifScanResult scan(GenomeChunk chunk) { // main method: scans one chunk sequence for the configured motifs
        if (chunk == null) { // checks whether the incoming chunk is null
            throw new IllegalArgumentException("Chunk must not be null."); // throws an error if the chunk is missing
        } // ends the null check

        String sequence = chunk.sequence(); // reads the DNA sequence from the chunk

        if (sequence == null) { // checks whether the chunk has no sequence
            throw new IllegalArgumentException("Chunk sequence must not be null."); // throws an error if the sequence is missing
        } // ends the null-sequence check

        String normalizedSequence = sequence.toUpperCase(); // normalizes the chunk sequence to uppercase so matching stays consistent
        boolean[] coverageMask = new boolean[normalizedSequence.length()]; // creates a mask that tracks which sequence positions are covered by matched motifs
        Set<String> matchedMotifSet = new LinkedHashSet<>(); // creates a unique ordered set of motif strings that were actually found
        int occurrenceCount = 0; // starts a counter for the total number of motif occurrences found

        for (String motif : motifs) { // loops through each configured motif
            String normalizedMotif = motif.toUpperCase(); // normalizes the motif to uppercase so matching stays consistent
            int fromIndex = 0; // starts searching this motif from the beginning of the chunk sequence

            while (true) { // keeps searching for more occurrences of the same motif until none are left
                int matchIndex = normalizedSequence.indexOf(normalizedMotif, fromIndex); // finds the next occurrence of the motif at or after fromIndex

                if (matchIndex < 0) { // checks whether no more matches were found
                    break; // exits the loop for this motif if no more occurrences exist
                } // ends the no-more-matches check

                matchedMotifSet.add(normalizedMotif); // records that this motif type was found in the chunk
                occurrenceCount++; // increments the total motif occurrence counter
                for (int i = matchIndex; i < matchIndex + normalizedMotif.length() && i < coverageMask.length; i++) { // loops over every sequence position covered by this motif occurrence
                    coverageMask[i] = true; // marks this sequence position as covered by a sensitive motif
                } // ends the loop over the motif-covered positions

                fromIndex = matchIndex + 1; // moves the search window one step forward so overlapping or later occurrences can still be detected
            } // ends the repeated search loop for one motif
        } // ends the loop over all motifs

        int sensitiveBasesCovered = 0; // starts a counter for how many chunk bases are covered by matched motifs
        for (boolean covered : coverageMask) { // loops through the coverage mask
            if (covered) { // checks whether the current position was covered by any matched motif
                sensitiveBasesCovered++; // increments the covered-base count
            } // ends the covered-position check
        } // ends the loop over the coverage mask

        return new MotifScanResult( // creates and returns the final motif scan result object
                new ArrayList<>(matchedMotifSet), // converts the unique set of matched motifs into a list
                occurrenceCount, // stores the total number of motif occurrences
                sensitiveBasesCovered // stores how many chunk bases are covered by motif occurrences
        ); // ends the MotifScanResult construction
    } // ends the scan method

    public record MotifScanResult( // declares a compact result object for one chunk's motif scan

                                   List<String> matchedMotifs, // stores the unique motif types that were found
                                   int occurrenceCount, // stores the total number of motif occurrences found
                                   int sensitiveBasesCovered // stores the number of chunk bases covered by matched motifs

    ) {}  // ends the MotifScanResult record
} // ends the MotifScanner class  ke