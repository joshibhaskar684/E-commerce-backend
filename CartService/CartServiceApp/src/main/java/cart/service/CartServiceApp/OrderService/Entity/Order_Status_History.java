package cart.service.CartServiceApp.OrderService.Entity;

import java.time.Instant;

public class Order_Status_History {

    private String id;

    private String oldStatus;

    private String newStatus;

    private String remarks;

    private Long updatedBy;

    private String updatedByRole;

    private Instant changedAt;
}
