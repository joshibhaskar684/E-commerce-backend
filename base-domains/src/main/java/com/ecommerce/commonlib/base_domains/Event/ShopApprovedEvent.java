package com.ecommerce.commonlib.base_domains.Event;

import com.ecommerce.commonlib.base_domains.Enums.EventType;
import com.ecommerce.commonlib.base_domains.Enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ShopApprovedEvent {
    private Long sellerId;
    private Status status;
    private Long shopId;
    private EventType eventType;
    private String message;

    // constructor, getters, setters
}