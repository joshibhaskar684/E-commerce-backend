package com.ecommerce.commonlib.base_domains.Event;

import com.ecommerce.commonlib.base_domains.Enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCartEvent {

    private Long userId;

    private EventType eventType;

}
