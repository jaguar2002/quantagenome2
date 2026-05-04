package com.demo.genome.io; // puts this class inside the io package

import com.demo.genome.model.GenomeRecord; // imports GenomeRecord because this loader builds GenomeRecord objects from FASTA files

import java.io.IOException;
import java.nio.file.Path; // imports Path so we can pass around file locations cleanly
import java.util.ArrayList; // imports ArrayList so we can build a list of genome records dynamically
import java.util.List; // imports List so we can return collections of genome records

public class GenomeDatasetLoader { // declares the class responsible for loading reference + sample FASTA files into GenomeRecord objects

    private final FastaReader fastaReader; // stores the FASTA reader dependency used to parse files

    public GenomeDatasetLoader(FastaReader fastaReader) { // constructor that receives the FASTA reader dependency
        this.fastaReader = fastaReader; // stores the FASTA reader for use inside the loader
    } // ends the constructor

    public GenomeRecord loadSingleSample(Path referenceFastaPath, Path sampleFastaPath) throws IOException { // loads one sample FASTA file together with one reference FASTA file into a single GenomeRecord
        if (referenceFastaPath == null) { // checks whether the reference FASTA path is null
            throw new IllegalArgumentException("Reference FASTA path must not be null."); // throws an error if the reference path is missing
        } // ends the null-reference-path check

        if (sampleFastaPath == null) { // checks whether the sample FASTA path is null
            throw new IllegalArgumentException("Sample FASTA path must not be null."); // throws an error if the sample path is missing
        } // ends the null-sample-path check

        FastaReader.FastaSequence reference = fastaReader.read(referenceFastaPath); // parses the reference FASTA into header + sequence
        FastaReader.FastaSequence sample = fastaReader.read(sampleFastaPath); // parses the sample FASTA into header + sequence

        String sampleId = deriveSampleId(sample.header(), sampleFastaPath); // derives a stable sample ID from the sample header or file name

        return new GenomeRecord( // creates and returns the final GenomeRecord object
                sampleId, // stores the sample ID
                reference.sequence(), // stores the full reference sequence
                sample.sequence() // stores the observed sample sequence
        ); // ends the GenomeRecord creation
    } // ends the loadSingleSample method

    public List<GenomeRecord> loadManySamples(Path referenceFastaPath, List<Path> sampleFastaPaths) throws IOException { // loads many sample FASTA files using the same reference FASTA
        if (sampleFastaPaths == null) { // checks whether the incoming sample-path list is null
            throw new IllegalArgumentException("Sample FASTA path list must not be null."); // throws an error if the list is missing
        } // ends the null-list check

        List<GenomeRecord> records = new ArrayList<>(); // creates an empty list to store the loaded GenomeRecord objects

        for (Path samplePath : sampleFastaPaths) { // loops through every sample FASTA path in the list
            records.add(loadSingleSample(referenceFastaPath, samplePath)); // loads the current sample together with the reference and stores the GenomeRecord
        } // ends the loop over the sample files

        return records; // returns the full list of loaded GenomeRecord objects
    } // ends the loadManySamples method

    private String deriveSampleId(String header, Path samplePath) { // helper method that decides what sample ID to use
        if (header != null && !header.isBlank()) { // checks whether the FASTA header contains usable text
            return header; // returns the FASTA header directly as the sample ID if available
        } // ends the header-available check

        String fileName = samplePath.getFileName().toString(); // reads the file name from the sample path

        int dotIndex = fileName.lastIndexOf('.'); // finds the final dot position in the file name
        if (dotIndex > 0) { // checks whether the file name actually has an extension
            return fileName.substring(0, dotIndex); // returns the file name without the extension as the sample ID
        } // ends the extension check

        return fileName; // returns the full file name if no extension exists
    } // ends the deriveSampleId helper
} // ends the GenomeDatasetLoader class