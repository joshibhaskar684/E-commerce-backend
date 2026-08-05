package app.Ecommerce.ProductServiceApp.ElasticSearch.Repository;

import app.Ecommerce.ProductServiceApp.ElasticSearch.Entity.ProductDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductSearchRepository
        extends ElasticsearchRepository<ProductDocument, String> {

}