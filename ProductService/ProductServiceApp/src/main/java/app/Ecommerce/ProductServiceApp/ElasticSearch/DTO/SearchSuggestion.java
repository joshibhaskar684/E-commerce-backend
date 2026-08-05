package app.Ecommerce.ProductServiceApp.ElasticSearch.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchSuggestion {

    private String id;
    private String name;
    private String slug;
    private String image;
}