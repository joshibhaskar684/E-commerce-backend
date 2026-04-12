package app.auth.service.Repository;

import app.auth.service.Entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Long , Seller> {

}
