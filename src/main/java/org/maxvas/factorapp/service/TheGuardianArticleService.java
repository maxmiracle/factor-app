package org.maxvas.factorapp.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Safelist;
import org.jsoup.select.Elements;
import org.maxvas.factorapp.entity.ArticleData;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@AllArgsConstructor
public class TheGuardianArticleService {

    private final DocumentService documentService;

    /**
     * Download and map an article by article link.
     *
     * @param url resource to download
     * @return formatted dto Article
     */
    public ArticleData getArticleData(String url) {
        String title = null;
        String text = null;
        try {
            Document doc = documentService.getDocument(url);
            title = doc.select("h1").first().html();
            Elements elements = doc.select("div.article-body-commercial-selector");
            Element element = elements.first();
            if (element == null) {
                return new ArticleData(url, title, text, false, "Error to find text block. Wrong format.");
            }
            text = elements.first().html();
            text = Jsoup.clean(text, Safelist.simpleText());
            return new ArticleData(url, title, text, true, null);
        } catch (Exception e) {
            return new ArticleData(url, title, text, false, e.toString());
        }
    }


}
