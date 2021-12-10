package org.maxvas.factorapp;

import com.google.common.io.Resources;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Properties;

@Slf4j
class CoreNLPTest {
    @Test
    public void testCalcMetric() throws IOException {

        String text = Resources.toString(Resources.getResource("testText.txt"), Charset.defaultCharset());
        // set up pipeline properties
        Properties props = new Properties();
        // set the list of annotators to run
        props.setProperty("annotators", "tokenize,ssplit,pos,parse,lemma,ner,sentiment");
        // build pipeline
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        // create a document object
        CoreDocument document = new CoreDocument(text);
        // annnotate the document
        pipeline.annotate(document);

        List<CoreMap> sentences = document.annotation().get(CoreAnnotations.SentencesAnnotation.class);

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
                if (!ne.equalsIgnoreCase("O")){
                    log.info("{} : {}", word, ne);
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
            log.info("Sentiment: {}", sentiment);


            //Tree tree1 = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
            //Tree tree2 = sentence.get(CoreAnnotations.)
            // this is the parse tree of the current sentence
            //Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);


            // this is the Stanford dependency graph of the current sentence
            //SemanticGraph dependencies = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
        }
    }
}
