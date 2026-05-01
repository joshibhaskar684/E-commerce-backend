package com.ecommerce.commonlib.base_domains.Event;

import com.ecommerce.commonlib.base_domains.Enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SellerApprovedEvent {
    private String message;
    private String messageStatus;
    private Long sellerId;
    private Status status;

    // constructor, getters, setters
}