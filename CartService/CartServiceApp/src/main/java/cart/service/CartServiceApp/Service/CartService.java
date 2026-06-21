package cart.service.CartServiceApp.Service;

import cart.service.CartServiceApp.Entity.Cart;
import cart.service.CartServiceApp.Entity.CartItem;
import cart.service.CartServiceApp.Entity.CartSummary;
import cart.service.CartServiceApp.Repository.CartRepository;
import cart.service.CartServiceApp.Repository.ProductsdataSnapshotRepository;
import com.ecommerce.commonlib.base_domains.Event.CreateCartEvent;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private CartRepository cartRepository;
    private ProductsdataSnapshotRepository productsdataSnapshotRepository;

    public CartService(CartRepository cartRepository, ProductsdataSnapshotRepository productsdataSnapshotRepository) {
        this.cartRepository = cartRepository;
        this.productsdataSnapshotRepository = productsdataSnapshotRepository;
    }

    public Cart createCart(Long userId) {

        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUserId(userId);
                    cart.setItems(new ArrayList<>());
                    cart.setSummary(new CartSummary());
                    cart.setUpdatedAt(Instant.now());
                    return cartRepository.save(cart);
                });
    }


    public CartSummary calculateSummary(List<CartItem> items) {

        double subTotal = 0;

        for (CartItem item : items) {
            subTotal += item.getPrice() * item.getQuantity();
        }

        double discount = 0; // later from coupon service
        double deliveryFee = 0; // future logic
        double tax = subTotal * 0.18; // example GST 18%

        double total = subTotal - discount + deliveryFee + tax;

        CartSummary summary = new CartSummary();
        summary.setSubTotal(subTotal);
        summary.setDiscount(discount);
        summary.setDeliveryFee(deliveryFee);
        summary.setTax(tax);
        summary.setTotalAmount(total);

        return summary;
    }

    public Cart addItem(Long userId, CartItem newItem) {

if(!newItem.getProductId().equals(productsdataSnapshotRepository.findByProductId(newItem.getProductId()))){
    throw new RuntimeException("Product not found");
}
        Cart cart = cartRepository.findByUserId(userId)
                .orElse(createCart(userId));

        List<CartItem> items = cart.getItems();

        Optional<CartItem> existing = items.stream()
                .filter(i -> i.getProductId().equals(newItem.getProductId()))
                .findFirst();

        if (existing.isPresent()) {
            existing.get().setQuantity(
                    existing.get().getQuantity() + newItem.getQuantity()
            );
        } else {
            items.add(newItem);
        }

        // 🔥 recalculate summary
        CartSummary summary = calculateSummary(items);
        cart.setSummary(summary);

        return cartRepository.save(cart);
    }

    public Cart getCart(Long userId) {

        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUserId(userId);
                    cart.setItems(new ArrayList<>());
                    cart.setSummary(new CartSummary());
                    cart.setUpdatedAt(Instant.now());
                    return cartRepository.save(cart);
                });
    }

    public Cart updateQuantity(Long userId, String productId, int quantity) {

        Cart cart = getCart(userId);

        for (CartItem item : cart.getItems()) {
            if (item.getProductId().equals(productId)) {
                item.setQuantity(quantity);
                item.setUpdatedAt(Instant.now());
                break;
            }
        }

        updateCart(cart);
        return cartRepository.save(cart);
    }

    public Cart removeItem(Long userId, String productId) {

        Cart cart = getCart(userId);

        cart.getItems().removeIf(i -> i.getProductId().equals(productId));

        updateCart(cart);
        return cartRepository.save(cart);
    }
    public Cart clearCart(Long userId) {

        Cart cart = getCart(userId);

        cart.getItems().clear();
        cart.setSummary(new CartSummary());

        cart.setUpdatedAt(Instant.now());

        return cartRepository.save(cart);
    }
    private void updateCart(Cart cart) {

        double subTotal = 0;

        for (CartItem item : cart.getItems()) {
            subTotal += item.getPrice() * item.getQuantity();
        }

        double discount = 0; // future coupon logic
        double tax = subTotal * 0.18; // GST example
        double delivery = 0;

        double total = subTotal - discount + tax + delivery;

        CartSummary summary = new CartSummary();
        summary.setSubTotal(subTotal);
        summary.setDiscount(discount);
        summary.setTax(tax);
        summary.setDeliveryFee(delivery);
        summary.setTotalAmount(total);

        cart.setSummary(summary);
        cart.setUpdatedAt(Instant.now());
    }

    public int getCartItemCount(Long userId) {

        Cart cart = getCart(userId);

        return cart.getItems()
                .stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }
}
