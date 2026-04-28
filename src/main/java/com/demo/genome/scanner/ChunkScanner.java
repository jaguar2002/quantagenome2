package com.demo.genome.scanner;

import com.demo.genome.model.GenomeChunk;//chunk model so we can scan each reference against each sample chunk
import com.demo.genome.model.Mutation; //imports a mutation model because chunks produce mutations
import com.demo.genome.model.ScanFinding; //importing a finding model so this class returns one structured finding.

import java.time.Instant; // imports Instant so IDs can be generated using the current timestamp
import java.util.List; // imports a List so we can hold mutations and motif lists.
import java.util.Optional; // imports optional used to test presence or absence of a file.


public class ChunkScanner { //declaring the class responsible for scanning a chunk pair.

    private final ReferenceComparator referenceComparator; //storing the comparator used to detect mutations wrt the reference sample 
    private final MotifScanner motifScanner; //the motif scanner object used in identifying motifs in the sample. 
    
    public ChunkScanner(ReferenceComparator referenceComparator, MotifScanner motifScanner) { 
	
	this.referenceComparator = referenceComparator; //storing the comparator dependency 
	this.motifScanner = motifScanner; //sotring the motif scanner dependency.

	

    } //constructor to wire the scanner together

    public Optional<ScanFinding> scan(GenomeChunk referenceChunk, GenomeChunk sampleChunk) {
	
	if(referenceChunk == null){ //if no sample chunk is found.
		throw new IllegalArgumentException("Reference chunk must not be null.");
	}//ending the reference null check.

	List<Mutation> mutations = referenceComparator.compare(referenceChunk, sampleChunk); 
	MotifScanner.MotifScanResult motifResult = motifScanner.scan(sampleChunk); 
	
	boolean noMutations = mutations.isEmpty(); //if no mutations are found this is set to true
    boolean noMotifs = motifResult.matchedMotifs().isEmpty();//
	MotifScanner.MotifScanResult motifScanResult = motifScanner.scan(sampleChunk); // final result of scan n 
	
	if(noMutations && noMotifs){
		return Optional.empty();
	} //ends the no-signals check
	
	String severity = determineSeverity(mutations.size(),motifResult.occurrenceCount(), motifResult.sensitiveBasesCovered(), sampleChunk.sequence().length());//computes a severity label from mutations and motifs found in sequence.
	
	ScanFinding finding = new ScanFinding(
            "finding-" + Instant.now().toEpochMilli() + "-" + sampleChunk.chunkId(),
            sampleChunk.chunkId(), //stores the chunk ID in the findings
            sampleChunk.chromosome(), //storing the chromosome name
            sampleChunk.start(),//storing the chunk start coordinate.
            severity, //storing severity as a label
            sampleChunk.sequence().length(), //storing the chunk length by sequence
            mutations.size(),//storing the mutation count
            mutations, // storing a list of mutations
            motifResult.matchedMotifs()// unique motifs that were stored.
    );


		return Optional.of(finding);//wrapping the finding in an Optional and returning it.
    } //scanning the method

    public String determineSeverity(int mutationCount, int motifOccurenceCount, int sensitiveBasesCovered, int sequenceLength){
        double coverageRatio = sequenceLength == 0 ? 0.0 : (double) sensitiveBasesCovered / sequenceLength;
        if(motifOccurenceCount >= 2 || coverageRatio >= 0.10 || mutationCount >= 50){
            return "HIGH";
        }
        if(motifOccurenceCount >= 1 || mutationCount >= 10){
            return "MEDIUM";
        }
        return "LOW";
    }

}
