package com.example.firstTry.touur;

import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.time.*;
import edu.stanford.nlp.util.CoreMap;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.*;

@Service
public class TimeExtractorService {

    private AnnotationPipeline pipeline;

    @PostConstruct
    public void init() {
        Properties props = new Properties();
        pipeline = new AnnotationPipeline();
        pipeline.addAnnotator(new TokenizerAnnotator(false));
        pipeline.addAnnotator(new WordsToSentencesAnnotator(false));
        pipeline.addAnnotator(new POSTaggerAnnotator(false));
        pipeline.addAnnotator(new TimeAnnotator("sutime", props));
    }

    public List<TimeExpressionResult> extractTimeExpressions(String text) {
        List<TimeExpressionResult> results = new ArrayList<>();

        Annotation annotation = new Annotation(text);
        annotation.set(CoreAnnotations.DocDateAnnotation.class, LocalDate.now().toString());
        pipeline.annotate(annotation);

        List<CoreMap> timexAnnsAll = annotation.get(TimeAnnotations.TimexAnnotations.class);
        for (CoreMap cm : timexAnnsAll) {
            List<CoreLabel> tokens = cm.get(CoreAnnotations.TokensAnnotation.class);
            SUTime.Temporal timex = cm.get(TimeExpression.Annotation.class).getTemporal();
            String timexValue = timex.getTimexValue();
            String timexType = timex.getTimexType().toString();

            int start = tokens.get(0).beginPosition();
            int end = tokens.get(tokens.size()-1).endPosition();

            // Split into components while preserving original
            String[] parts = timexValue.split("T");
            String date = parts.length > 0 && !parts[0].isEmpty() ? parts[0] : null;
            String time = parts.length > 1 ? parts[1].replaceFirst("^T?", "") : null;

            results.add(new TimeExpressionResult(
                    cm.toString(),
                    timexType,
                    timexValue,
                    date,
                    time,
                    start,
                    end
            ));
        }
        return results;
    }


}
