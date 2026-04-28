from dataclasses import dataclass #imports dataclass so we can define clean containers
from typing import Any #import Any so our from_dict method accepts general parsed JSON values

@dataclass
class ChunkFeaturePayload:

    chunk_id : str # storing the input object into
    chromosome : str #stores the chromosome name in format chrNN
    start : int # stores the 1-based chunk start coordinate
    end : int # stores the 1 based chunk end coordinate
    mutation_density : float # stores normalized SSR density for a single chunk
    ssr_density : float # stores the normalized SSR density for each individual chunk.
    gc_ratio : float # stores GC ratio for the chunk.
    repeat_density : float  #stores GC ratio for the chunk.
    sensitive_span_ratio : float # Stores repeat density for the chunk
    uniqueness_score : float #storing uniqueness score, where higher values may indicate more identifying sequences.

    @staticmethod #can run without an instance

    def from_dict(data : dict[str,Any]):

        return ChunkFeaturePayload(
            chunk_id = str(data["chunk_id"]), #converting to string
            chromosome = str(data["chromosome"]),
            start = data["start"],#make sure it's an integer.
            end = int(data["end"]),
            mutation_density = float(data["mutation_density"]),
            ssr_density = float(data["repeat_density"]),
            gc_ratio = float(data["gc_ratio"]),
            repeat_density = float(data["repeat_density"]),
            sensitive_span_ratio = float(data["sensitive_span_ratio"]),
            uniqueness_score = float(data["uniqueness_score"]),
        )

    def feature_vector(self) -> list[float]:
        return[ #making a list of a chunk's features
            self.mutation_density,
            self.ssr_density,
            self.gc_ratio,
            self.repeat_density,
            self.sensitive_span_ratio,
            self.uniqueness_score
        ]

#this class is used to hold the final result after the AI analyzes,
@dataclass
class TriageResult:

    chunk_id : str #which chunk id is being used.
    sensitive : bool # true meaning privacy risk is high false means privacy risk is low
    confidence : float #final confidence rating of the model (0.0 to 1.0)
    explanation : str #human-readable reason why it is flagged
    model_version : str #identifying the model version that made this prediction.

    def to_dict(self) -> dict[str,Any]:

        return{
            "chunk_id" : self.chunk_id,
            "sensitive" : self.sensitive,
            "confidence" : round(self.confidence,4),
            "explanation" : self.explanation,
            "model_version" : self.model_version
        } #final triage result

