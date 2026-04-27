import json #to parse input json from the java  fils and write an output back
import sys #so the script can use stdin, stdout stderr

from model import Mark2PrivacyFNN #importing the feed forward NN
from schemas import ChunkFeaturePayload, TriageResult #inputing data shapes to be used by the engine.

MODEL_VERSION = "mark2-ffnn-v1"

def build_explanation(payload: ChunkFeaturePayload, probability: float, sensitive: bool):

    reasons = []

    if payload.mutation_density >= 0.01:
        reasons.append("mutation density is elevated")

    if payload.ssr_density >= 0.005:
        reasons.append("SSR density is elevated")

    if payload.repeat_density >= 0.20:
        reasons.append("repeat density is high")

    if payload.uniqueness_score >= 0.60:
        reasons.append("sensitive span ratio is high")

    if payload.gc_ratio >= 0.55 or payload.gc_ratio <= 0.30:
        reasons.append("GC composition is unusual")

    if not reasons:
        reasons.append("the chunk shows only mild privacy-risk signals in this prototype")

    joined_reasons = ",".join(reasons)

    if sensitive:
        return(
            f"Mark 2 FFNN predicts the chunk is not strongly privacy sensitive"
            f"Main pattern: {joined_reasons}"
            f"Predicted sensitivity score = {probability:.4f}"
        )

def score_one(payload_dict : dict) -> dict:

    payload = ChunkFeaturePayload.form_dict(payload_dict)
    model = Mark2PrivacyFNN()
    probability = model.predict(payload.feature_vector())
    sensitive = probability >= 0.50,
    explanation = build_explanation(payload, probability, sensitive)

    result = TriageResult(
        chunk_id=payload.chunk_id,
        sensitive=sensitive,
        confidence=probability,
        explanation=explanation,
        model_version=MODEL_VERSION
    )

    return result.to_dict()

def main() -> None:

    raw = sys.stdin.read()

    if raw is None or raw.strip() == "":
        raise ValueError("No input JSON received by ai_engine.py.")

    payload = json.loads(raw)

    if "chunks" in payload:  # checks whether Java sent a batch request containing multiple chunks
        results = [score_one(item) for item in payload["chunks"]]  # scores every chunk in the batch and collects the results
        response = {  # builds the batch response object
            "model_version": MODEL_VERSION,  # includes the model version at the batch level
            "results": results,  # includes the list of per-chunk scoring results
        }  # ends the batch response object
    else:  # runs when Java sent a single-chunk request
        response = score_one(payload)

    print(json.dumps(response))

if __name__ == "__main__":
    main()

