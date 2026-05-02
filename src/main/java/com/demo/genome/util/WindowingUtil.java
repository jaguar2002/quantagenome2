package com.demo.genome.util; // puts this class inside the util package

import com.demo.genome.model.GenomeChunk; // imports the chunk model because this utility builds chunk objects from long sequences

import java.util.ArrayList; // imports ArrayList so we can build a dynamic list of chunks
import java.util.List; // imports List so we can return collections of chunks

public final class WindowingUtil { // declares a utility class for splitting long sequences into chunk windows
    private WindowingUtil() {} // private constructor prevents anyone from instantiating this helper class

    public static List<GenomeChunk> chunkSequence(String chromosome, String sequence, int windowSize, int stepSize) { // splits one long sequence into many GenomeChunk windows
        if (chromosome == null || chromosome.isBlank()) { // checks whether the chromosome name is missing or blank
            throw new IllegalArgumentException("Chromosome name must not be null or blank."); // throws an error if chromosome name is invalid
        } // ends the chromosome-name check

        String normalizedSequence = SequenceUtil.normalize(sequence); // normalizes the incoming sequence before chunking it

        if (normalizedSequence.isEmpty()) { // checks whether the sequence is empty after normalization
            return List.of(); // returns an empty list if there is nothing to chunk
        } // ends the empty-sequence check

        if (windowSize <= 0) { // checks whether the requested window size is invalid
            throw new IllegalArgumentException("Window size must be greater than 0."); // throws an error if window size is not positive
        } // ends the window-size check

        if (stepSize <= 0) { // checks whether the requested step size is invalid
            throw new IllegalArgumentException("Step size must be greater than 0."); // throws an error if step size is not positive
        } // ends the step-size check

        List<GenomeChunk> chunks = new ArrayList<>(); // creates an empty list to store all generated GenomeChunk objects
        int chunkIndex = 1; // starts a 1-based chunk counter so each chunk gets a readable chunk ID

        for (int startIndex = 0; startIndex < normalizedSequence.length(); startIndex += stepSize) { // loops through the sequence using the requested sliding-window step size
            int endIndex = Math.min(startIndex + windowSize, normalizedSequence.length()); // computes the end index for the current window without exceeding sequence length
            String chunkSequence = normalizedSequence.substring(startIndex, endIndex); // extracts the current window sequence

            int oneBasedStart = startIndex + 1; // converts the 0-based Java index into a 1-based genomic coordinate
            int oneBasedEnd = endIndex; // uses the end index directly as the 1-based inclusive genomic end coordinate
            String chunkId = chromosome + ":" + oneBasedStart + "-" + oneBasedEnd; // creates a readable chunk ID like chr14:1-10000

            chunks.add(new GenomeChunk( // creates a new GenomeChunk object for this window and stores it
                    chunkId, // stores the readable chunk ID
                    chromosome, // stores the chromosome name
                    oneBasedStart, // stores the 1-based start coordinate
                    oneBasedEnd, // stores the 1-based end coordinate
                    chunkSequence // stores the actual sequence content of the window
            )); // ends the GenomeChunk creation

            chunkIndex++; // increments the chunk counter even though the chunk ID is coordinate-based, keeping the loop progression clear
            if (endIndex == normalizedSequence.length()) { // checks whether this window reached the end of the sequence
                break; // exits the loop once the sequence end has been covered
            } // ends the sequence-end check
        } // ends the chunking loop

        return chunks; // returns the full list of generated GenomeChunk objects
    } // ends the chunkSequence method

    public static List<GenomeChunk> nonOverlappingChunks(String chromosome, String sequence, int windowSize) { // convenience method for creating non-overlapping windows
        return chunkSequence(chromosome, sequence, windowSize, windowSize); // calls the main chunking method using the same size for both window and step
    } // ends the nonOverlappingChunks method

    public static List<GenomeChunk> overlappingChunks(String chromosome, String sequence, int windowSize, int overlapSize) { // convenience method for creating overlapping windows
        if (overlapSize < 0) { // checks whether overlap size is negative
            throw new IllegalArgumentException("Overlap size must not be negative."); // throws an error if overlap is invalid
        } // ends the negative-overlap check

        if (overlapSize >= windowSize) { // checks whether the overlap is as large as or larger than the window itself
            throw new IllegalArgumentException("Overlap size must be smaller than the window size."); // throws an error if the overlap would make the step invalid
        } // ends the overlap-vs-window-size check

        int stepSize = windowSize - overlapSize; // converts overlap size into the correct step size for sliding windows
        return chunkSequence(chromosome, sequence, windowSize, stepSize); // calls the main chunking method with the calculated step size
    } // ends the overlappingChunks method
} // ends the WindowingUtil class