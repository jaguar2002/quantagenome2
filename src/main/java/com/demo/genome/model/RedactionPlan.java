package com.demo.genome.model;

import com.demo.genome.util.JsonUtil;

import java.util.List;

public record RedactionPlan(String chunkId, String status, String strategy,String redactedSequence, String publicSummary, List<String> notes) {
        public String toJson(){
          return """
                  { 
                    
                    "chunk_id": %s,
                    "status": %s,
                    "strategy":%s,
                    "redacted_sequence":%s,
                    "public_summary":%s,
                    "notes":%s
                  
                  }
                  """.formatted(
                  JsonUtil.str(chunkId),
                  JsonUtil.str(status),
                  JsonUtil.str(strategy),
                  JsonUtil.str(redactedSequence),
                  JsonUtil.str(publicSummary),
                  JsonUtil.stringArray(notes)
          );
        };
}
