package app.Ecommerce.ProductServiceApp.Service;

import app.Ecommerce.ProductServiceApp.Entity.SellerData;
import app.Ecommerce.ProductServiceApp.Repository.SellerDataRepository;
import com.ecommerce.commonlib.base_domains.Enums.Status;
import com.ecommerce.commonlib.base_domains.Event.SellerApprovedEvent;
import com.ecommerce.commonlib.base_domains.Event.ShopApprovedEvent;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SellerDataService {
    private SellerDataRepository sellerDataRepository;
    private MongoTemplate mongoTemplate;

    public SellerDataService(SellerDataRepository sellerDataRepository, MongoTemplate mongoTemplate) {
        this.sellerDataRepository = sellerDataRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public void handleApproveShop(ShopApprovedEvent event) {

        SellerData data = sellerDataRepository
                .findBySellerIdAndShopId(event.getSellerId(), event.getShopId())
                .orElse(new SellerData());

        data.setSellerId(event.getSellerId());
        data.setShopId(event.getShopId());
        data.setStatus(Status.APPROVED);

        sellerDataRepository.save(data);
    }
    public void handleRejectShop(ShopApprovedEvent event) {

//        updateShopStatus(event, Status.REJECTED);
    }

    public void handleSuspendShop(ShopApprovedEvent event) {
        boolean isSellerApproved = sellerDataRepository
                .findBySellerIdAndShopIdAndStatus(event.getSellerId(), event.getShopId(), Status.APPROVED).isPresent();

        if (!isSellerApproved) {
            throw new RuntimeException("Seller not approved yet");
        }
        updateShopStatus(event, Status.INACTIVE);
    }


    private void updateShopStatus(ShopApprovedEvent event, Status status) {


        Query query = new Query(
                Criteria.where("sellerId").is(event.getSellerId())
                        .and("shopId").is(event.getShopId())
                        .and("status").is(Status.APPROVED)
        );

        Update update = new Update()
                .set("status", Status.SUSPENDED);

        mongoTemplate.updateFirst(query, update, SellerData.class);
    }


    public void handleApprove(SellerApprovedEvent event) {
        SellerData data = new SellerData();
        data.setSellerId(event.getSellerId());
        data.setStatus(Status.APPROVED);

        sellerDataRepository.save(data);
    }

    public void handleReject(SellerApprovedEvent event) {
//        updateStatus(event.getSellerId(), Status.REJECTED);

    }

    public void handleSuspend(SellerApprovedEvent event) {
        updateSellerStatus(event.getSellerId());
    }

    private void updateStatus(Long sellerId, Status status) {
//        List<SellerData > data = sellerDataRepository
//                .findBySellerIdAndStatus(sellerId,Status.APPROVED);
//
//
//        data.setSellerId(sellerId);
//        data.setStatus(Status.SUSPENDED);
//
//        sellerDataRepository.save(data);
    }
    public void updateSellerStatus(Long sellerId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("sellerId").is(sellerId)
                .and("status").is(Status.APPROVED));
        Update update = new Update();
        for(int i=0;i<15;i++){
        List<SellerData> before = mongoTemplate.find(query, SellerData.class);
        System.out.println("Before update = " + before.size());
        }

        update.set("status", Status.INACTIVE);
        mongoTemplate.updateMulti(query, update, SellerData.class);


    }


}
