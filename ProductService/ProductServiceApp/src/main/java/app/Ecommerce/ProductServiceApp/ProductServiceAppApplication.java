package app.Ecommerce.ProductServiceApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@EnableMongoAuditing
@SpringBootApplication
public class ProductServiceAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductServiceAppApplication.class, args);
	}

}
