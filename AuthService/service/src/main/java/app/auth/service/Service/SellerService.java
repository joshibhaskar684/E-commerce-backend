package app.auth.service.Service;

import app.auth.service.Repository.SellerRepository;
import app.auth.service.Repository.UsersRepository;
import org.springframework.stereotype.Service;

@Service
public class SellerService {

    private SellerRepository sellerRepository;
    private UsersRepository usersRepository;

    public SellerService(SellerRepository sellerRepository, UsersRepository usersRepository) {
        this.sellerRepository = sellerRepository;
        this.usersRepository = usersRepository;
    }





}
