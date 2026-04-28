package com.demo.genome.scanner;

import com.demo.genome.model.GenomeChunk;//chunk model so we can scan each reference against each sample chunk
import com.demo.genome.model.Mutation; //imports a mutation model because chunks produce mutations
import com.demo.genome.model.ScanFinding; //importing a finding model so this class returns one structured finding.

import java.time.Instant; // imports Instant so IDs can be generated using the current timestamp
import java.util.List; // imports a List so we can hold mutations and motif lists.
import java.util.Optional; // imports optional used to test presence or absence of a file.


public class ChunkScanner { //declaring the class responsible for scanning a chunk pair.

    private final ReferenceComparator referenceComparator;
    private final MotifScanner motifScanner;

    

}
