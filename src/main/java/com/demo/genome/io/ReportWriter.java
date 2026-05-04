package com.demo.genome.io; //putting this class inside the IO package.

import com.demo.genome.model.AnalysisReport; //importing top level report model that is written out by the class.
import com.demo.genome.model.RedactionPlan; //importing RedactionPlan because the report includes redaction detials
import com.demo.genome.model.ScanFinding; //Importing scan finding to produce the output of the scanner result.
import com.demo.genome.model.TriageResult; //TriageResult from the AI/ML analysis
import com.demo.genome.model.ValidationResult; //So we can produce a validation result.

import java.io.IOException; //importing IOException for file writing.
import java.nio.file.Files; //importing files so we can write files to disk.
import java.nio.file.Path;//importing the path so we can specify the destination of the output file.
import java.util.List;//importing the list so we can report collections.


public class ReportWriter { //class that writes out the analysis output to the files.

    public void writeJsonReport(Path outputPath, AnalysisReport report ) { //outputs a full JSON style report to a given output
        if (outputPath == null) { //checks if the output path is null
            throw new IllegalArgumentException("Output path must not be null."); // throwing an error if the output is missing.
        }//ending null output path check.

        if(report == null){ //checks if the report object is null or not.
             throw new IllegalArgumentException("");
        }
    }


}
