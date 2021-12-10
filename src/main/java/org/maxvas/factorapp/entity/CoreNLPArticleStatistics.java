package org.maxvas.factorapp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;

/**
 * CoreNLP metrics
 */
@AllArgsConstructor
@Data
public class CoreNLPArticleStatistics {
    /**
     * Names/Categories/Counts
     */
    private HashMap<String, HashMap<String, Long>> ne;

    /**
     * Sentiment sentence count
     * 0 = very negative,
     * 1 = negative,
     * 2 = neutral,
     * 3 = positive,
     * 4 = very positive.
     */
    private HashMap<Integer, Long> sentiment;
}
