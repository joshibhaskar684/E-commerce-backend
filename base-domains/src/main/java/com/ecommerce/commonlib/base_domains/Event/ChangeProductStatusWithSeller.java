package com.ecommerce.commonlib.base_domains.Event;

import com.ecommerce.commonlib.base_domains.Enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChangeProductStatusWithSeller {
    private Long sellerId;
    private String message;
    private EventType eventType;

}
