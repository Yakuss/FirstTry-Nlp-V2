package com.example.firstTry.utils;

import com.example.firstTry.model.Specialization;
import com.example.firstTry.repository.SpecializationRepository;
import org.apache.commons.text.similarity.JaroWinklerSimilarity;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.util.CoreMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LocationSpecialityService {
    private static final Logger logger = LoggerFactory.getLogger(LocationSpecialityService.class);
    private static final StanfordCoreNLP pipeline;
    private final SpecializationRepository specializationRepo;

    static {
        try {
            Properties props = new Properties();
            // Add NER to the pipeline to extract locations
            props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner");
            props.setProperty("ner.useSUTime", "false"); // Optional: disable SUTime if not needed

            pipeline = new StanfordCoreNLP(props);
            logger.info("Stanford CoreNLP initialized successfully");
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public LocationSpecialityService(SpecializationRepository specializationRepo) {
        this.specializationRepo = specializationRepo;
    }

    public ParsedAppointmentRequest parse(String query) {
        Annotation document = new Annotation(query);
        pipeline.annotate(document);

        return ParsedAppointmentRequest.builder()
                .specialty(extractSpecialty(document))
                .location(extractLocation(document))
                .build();
    }



    private String extractSpecialty(Annotation document) {
        // Get all specializations from repository
        List<Specialization> allSpecializations = specializationRepo.findAll();

        // Extract and prepare specialization names for lookup
        Map<String, String> specializationMap = allSpecializations.stream()
                .collect(Collectors.toMap(
                        spec -> spec.getName().toLowerCase(),
                        Specialization::getName
                ));

        // Extract tokens from the document
        List<String> tokens = document.get(CoreAnnotations.TokensAnnotation.class).stream()
                .map(token -> token.get(CoreAnnotations.TextAnnotation.class))
                .toList();

        // Initialize similarity calculator - JaroWinkler works very well for medical/technical terms
        JaroWinklerSimilarity jaroWinkler = new JaroWinklerSimilarity();

        final double SIMILARITY_THRESHOLD = 0.88;

        // For each token in the document
        for (String token : tokens) {
            if (token.length() < 4) continue; // Skip very short tokens

            String lowerToken = token.toLowerCase();

            // Check for exact match first (most efficient)
            if (specializationMap.containsKey(lowerToken)) {
                return specializationMap.get(lowerToken);
            }

            // Find the best fuzzy match
            String bestMatch = null;
            double bestScore = 0;

            for (Map.Entry<String, String> entry : specializationMap.entrySet()) {
                double similarity = jaroWinkler.apply(lowerToken, entry.getKey());

                if (similarity > SIMILARITY_THRESHOLD && similarity > bestScore) {
                    bestScore = similarity;
                    bestMatch = entry.getValue();
                }
            }

            if (bestMatch != null) {
                return bestMatch;
            }
        }

        return null;
    }

    private String extractLocation(Annotation document) {
        // This method extracts locations using NER (Named Entity Recognition)
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        StringBuilder location = new StringBuilder();

        for (CoreMap sentence : sentences) {
            // Get all tokens in this sentence
            List<CoreLabel> tokens = sentence.get(CoreAnnotations.TokensAnnotation.class);

            // Group consecutive LOCATION entities together
            boolean inLocationEntity = false;
            StringBuilder currentLocation = new StringBuilder();

            for (CoreLabel token : tokens) {
                String ner = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                String word = token.get(CoreAnnotations.TextAnnotation.class);

                if ("LOCATION".equals(ner) || "CITY".equals(ner)) {
                    if (inLocationEntity) {
                        currentLocation.append(" ").append(word);
                    } else {
                        currentLocation = new StringBuilder(word);
                        inLocationEntity = true;
                    }
                } else {
                    if (inLocationEntity) {
                        // End of the location entity
                        if (!location.isEmpty()) {
                            location.append(", ");
                        }
                        location.append(currentLocation);
                        inLocationEntity = false;
                    }
                }
            }

            // Check if we ended the sentence while still in a location entity
            if (inLocationEntity && !currentLocation.isEmpty()) {
                if (!location.isEmpty()) {
                    location.append(", ");
                }
                location.append(currentLocation);
            }
        }

        return !location.isEmpty() ? location.toString() : null;
    }
}