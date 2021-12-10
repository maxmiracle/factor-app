package org.maxvas.factorapp.metric;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import lombok.extern.slf4j.Slf4j;
import org.maxvas.factorapp.entity.CoreNLPArticleStatistics;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;

@Service
@Slf4j
public class CoreNLPMetric {

    private StanfordCoreNLP pipeline;

    public CoreNLPMetric(){
        // set up pipeline properties
        Properties props = new Properties();
        // set the list of annotators to run
        props.setProperty("annotators", "tokenize,ssplit,pos,parse,lemma,ner,sentiment");
        // build pipeline
        pipeline = new StanfordCoreNLP(props);
    }

    public CoreNLPArticleStatistics processArticle(String text){
        // create a document object
        CoreDocument document = new CoreDocument(text);
        // annnotate the document
        pipeline.annotate(document);

        List<CoreMap> sentences = document.annotation().get(CoreAnnotations.SentencesAnnotation.class);

        List<String> neClasses = List.of("O", "DATE", "TIME", "ORDINAL", "NUMBER");
        HashMap<String, HashMap<String, Long>> neMap = new HashMap<String, HashMap<String, Long>>();
        HashMap<Integer, Long> sentimentMap = new HashMap<Integer, Long>();

        for(CoreMap sentence: sentences) {
            // traversing the words in the current sentence
            // a CoreLabel is a CoreMap with additional token-specific methods
            for (CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                // this is the text of the token
                String word = token.get(CoreAnnotations.TextAnnotation.class);
                // this is the POS tag of the token
                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                // this is the NER label of the token
                String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                if (!neClasses.contains(ne)){
                    neMap.putIfAbsent(word, new HashMap<String, Long>());
                    HashMap<String, Long> neCatMap = neMap.get(word);
                    neCatMap.put(ne, neCatMap.getOrDefault(ne, 0L) + 1);
                }
            }

            /** Get the argmax of the class predictions.
             * The predicted classes can be an arbitrary set of non-negative integer classes,
             * but in our current sentiment models, the values used are on a 5-point scale of
             * 0 = very negative,
             * 1 = negative,
             * 2 = neutral,
             * 3 = positive
             * 4 = very positive.
             */
            Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
            int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
            sentimentMap.put(sentiment, sentimentMap.getOrDefault(sentiment, 0L) + 1);
        }
        return new CoreNLPArticleStatistics(neMap, sentimentMap);
    }
}
