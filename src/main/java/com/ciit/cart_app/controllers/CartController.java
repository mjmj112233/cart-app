package com.ciit.cart_app.controllers;

import com.ciit.cart_app.models.CartItem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;


@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final List<CartItem> cartItems = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong();

    // Get all Items
    @GetMapping
    public List<CartItem> getAllItems() {
        return cartItems;
    }

    // Get Item by ID
    @GetMapping("/{id}")
    public ResponseEntity<CartItem> getItemById(@PathVariable Long id) {
        Optional<CartItem> Item = cartItems.stream().filter(u -> u.getId().equals(id)).findFirst();
        return Item.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<CartItem> createItemFromBody(@RequestBody CartItem cartItem) {
        cartItem.setId(counter.incrementAndGet());
        cartItems.add(cartItem);
        return new ResponseEntity<>(cartItem, HttpStatus.CREATED);
    }

    // Update existing Item
    @PutMapping("/{id}")
    public ResponseEntity<CartItem> updateItem(@PathVariable Long id, @RequestBody CartItem
            cartItemDetails) {
        Optional<CartItem> ItemOptional = cartItems.stream().filter(u -> u.getId().equals(id)).findFirst();
        if (ItemOptional.isPresent()) {
            CartItem cartItem = ItemOptional.get();
            cartItem.setName(cartItemDetails.getName());
            cartItem.setQuantity(cartItemDetails.getQuantity());
            cartItem.setPrice(cartItemDetails.getPrice());
            return ResponseEntity.ok(cartItem);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete Item
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        Optional<CartItem> ItemOptional = cartItems.stream().filter(u -> u.getId().equals(id)).findFirst();
        if (ItemOptional.isPresent()) {
            cartItems.remove(ItemOptional.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}




