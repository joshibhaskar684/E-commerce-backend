package app.Ecommerce.ProductServiceApp.ElasticSearch.Service;

import app.Ecommerce.ProductServiceApp.ElasticSearch.DTO.SearchSuggestion;
import app.Ecommerce.ProductServiceApp.ElasticSearch.Entity.ProductDocument;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductSearchService {

    private final ElasticsearchOperations operations;

    public ProductSearchService(ElasticsearchOperations operations) {
        this.operations = operations;
    }

    public List<ProductDocument> search(String keyword) {

        Query query = new CriteriaQuery(
                new Criteria("name")
                        .matches(keyword)
                        .or(new Criteria("brand").matches(keyword))
                        .or(new Criteria("description").matches(keyword)));

        SearchHits<ProductDocument> hits =
                operations.search(query, ProductDocument.class);

        return hits.stream()
                .map(SearchHit::getContent)
                .toList();
    }



    public List<ProductDocument> suggestions(String keyword) {

        NativeQuery query = NativeQuery.builder()
                .withQuery(q -> q
                        .matchPhrasePrefix(m -> m
                                .field("name")
                                .query(keyword)
                        )
                )
                .withMaxResults(10)
                .build();

        SearchHits<ProductDocument> hits =
                operations.search(query, ProductDocument.class);

        return hits.getSearchHits()
                .stream()
                .map(SearchHit::getContent)
                .toList();
    }



//    public List<SearchSuggestion> suggestionsd(String keyword) {
//
//        Criteria criteria = new Criteria("name").startsWith(keyword);
//
//        Query query = new CriteriaQuery(criteria);
//        query.setMaxResults(10);
//
//        SearchHits<ProductDocument> hits =
//                operations.search(query, ProductDocument.class);
//
//        return hits.stream()
//                .map(hit -> new SearchSuggestion(
//                        hit.getContent().getId(),
//                        hit.getContent().getName(),
//                        hit.getContent().getSlug(),
//                        hit.getContent().getImages() != null &&
//                                !hit.getContent().getImages().isEmpty()
//                                ? hit.getContent().getImages().get(0)
//                                : null
//                ))
//                .toList();
//    }


}