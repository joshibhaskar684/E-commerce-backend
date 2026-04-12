package app.auth.service.Service;

import app.auth.service.Repository.SellerRepository;
import app.auth.service.Repository.ShopRepository;
import app.auth.service.Repository.UsersRepository;
import org.springframework.stereotype.Service;

@Service
public class ShopService {
    private SellerRepository sellerRepository;
    private ShopRepository shopRepository;
    private UsersRepository usersRepository;

    public ShopService(SellerRepository sellerRepository, ShopRepository shopRepository, UsersRepository usersRepository) {
        this.sellerRepository = sellerRepository;
        this.shopRepository = shopRepository;
        this.usersRepository = usersRepository;
    }





}
