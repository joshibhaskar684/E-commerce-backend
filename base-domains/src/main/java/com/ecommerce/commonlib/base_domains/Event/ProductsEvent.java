package com.ecommerce.commonlib.base_domains.Event;

import com.ecommerce.commonlib.base_domains.Enums.EventType;
import com.ecommerce.commonlib.base_domains.Enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductsEvent {


    private List<String> images;

    private String color;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private String name;

    private Status sellerStatus;
    private Status shopStatus;

    private String productId;
    private Integer quantity;
    private String message;
    private EventType eventType;
    private Long sellerId;
    private Long ShopId;
    private Status productStatus;
    private Instant updateAt;
}
