package com.cheonmyo.shop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.cheonmyo.shop.entity.Cart;
import com.cheonmyo.shop.entity.Item;
import com.cheonmyo.shop.repository.CartRepository;
import com.cheonmyo.shop.repository.ItemRepository;
import com.cheonmyo.shop.service.JwtService;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@CrossOrigin(origins = "*")
public class CartController {

  @Autowired
  JwtService jwtService;

  @Autowired
  CartRepository cartRepository;

  @Autowired
  ItemRepository itemRepository;

  @GetMapping("/api/cart/items")
  public ResponseEntity getCartItems(@CookieValue(value = "token", required = false) String token) {

    if (!jwtService.isValid(token)) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    int memberId = jwtService.getId(token);
    List<Cart> carts = cartRepository.findByMemberId(memberId);
    List<Integer> itemIds = carts.stream().map(Cart::getItemId).toList();
    List<Item> items = itemRepository.findByIdIn(itemIds);

    return new ResponseEntity<>(items, HttpStatus.OK);
  }

  @PostMapping("/api/cart/items/{itemId}")
  public ResponseEntity pushCartItem(
      @PathVariable("itemId") int itemId,
      @CookieValue(value = "token", required = false) String token) {

    if (!jwtService.isValid(token)) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    int memberId = jwtService.getId(token);
    Cart cart = cartRepository.findByMemberIdAndItemId(memberId, itemId);

    if (cart == null) {
      Cart newCart = new Cart();
      newCart.setMemberId(memberId);
      newCart.setItemId(itemId);
      cartRepository.save(newCart);
    }

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @DeleteMapping("/api/cart/items/{itemId}")
  public ResponseEntity removeCartItem(
      @PathVariable("itemId") int itemId,
      @CookieValue(value = "token", required = false) String token) {

    if (!jwtService.isValid(token)) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    int memberId = jwtService.getId(token);
    Cart cart = cartRepository.findByMemberIdAndItemId(memberId, itemId);

    cartRepository.delete(cart);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
