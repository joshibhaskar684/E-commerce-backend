package app.Ecommerce.ProductServiceApp.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "stores")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Store {
    @Id
    private String id;
    private String storeName;
    private String ownerName;
    private String gstNo;
    private String address;
    private String pinCode;
    private String contactNo;
}
