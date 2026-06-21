package cart.service.CartServiceApp.Controller;

import cart.service.CartServiceApp.DTO.CartItemRequest;
import cart.service.CartServiceApp.DTO.UpdateCartRequest;
import cart.service.CartServiceApp.Entity.Cart;
import cart.service.CartServiceApp.Entity.CartItem;
import cart.service.CartServiceApp.Security.JwtUtil;
import cart.service.CartServiceApp.Security.SecurityUtil;
import cart.service.CartServiceApp.Service.CartService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private JwtUtil jwtUtil;

    public CartController(CartService cartService, JwtUtil jwtUtil) {
        this.cartService = cartService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public Cart getCart(@RequestHeader("Authorization") String authHeader ) {
        String token=authHeader.substring(7);

        Long userId = jwtUtil.extractUserId(token);

        return cartService.getCart(userId);
    }

    // -------------------------
    // 2. ADD ITEM TO CART
    // -------------------------
    @PostMapping("/item/add")
    public Cart addItem(@RequestBody CartItemRequest request,@RequestHeader("Authorization") String authHeader ) {

        Long userId = SecurityUtil.getCurrentUserId();

        CartItem item = new CartItem();
        item.setProductId(request.getProductId());
        item.setQuantity(request.getQuantity());
        item.setPrice(request.getPrice());
        item.setProductName(request.getProductName());
        item.setProductImage(request.getProductImage());
        item.setPriceAtAddTime(request.getPrice());
        return cartService.addItem(userId, item);
    }

    // -------------------------
    // 3. UPDATE ITEM QUANTITY
    // -------------------------
    @PutMapping("/item")
    public Cart updateQuantity(@RequestBody UpdateCartRequest request) {

        Long userId = SecurityUtil.getCurrentUserId();

        return cartService.updateQuantity(
                userId,
                request.getProductId(),
                request.getQuantity()
        );
    }

    // -------------------------
    // 4. REMOVE ITEM
    // -------------------------
    @DeleteMapping("/item/{productId}")
    public Cart removeItem(@PathVariable String productId) {

        Long userId = SecurityUtil.getCurrentUserId();

        return cartService.removeItem(userId, productId);
    }

    // -------------------------
    // 5. CLEAR CART
    // -------------------------
    @DeleteMapping("/clear")
    public Cart clearCart() {

        Long userId = SecurityUtil.getCurrentUserId();

        return cartService.clearCart(userId);
    }

    // -------------------------
    // 6. CART ITEM COUNT
    // -------------------------
    @GetMapping("/count")
    public int getCartCount() {

        Long userId = SecurityUtil.getCurrentUserId();

        return cartService.getCartItemCount(userId);
    }
}