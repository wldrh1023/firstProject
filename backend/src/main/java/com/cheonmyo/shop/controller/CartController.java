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
import org.springframework.web.bind.annotation.CrossOrigin;

import com.cheonmyo.shop.dto.CartResponseDto;
import com.cheonmyo.shop.exception.UnauthorizedException;
import com.cheonmyo.shop.service.CartService;
import com.cheonmyo.shop.service.JwtService;

@RestController
@CrossOrigin(origins = "*")
public class CartController {

  @Autowired
  private JwtService jwtService;

  @Autowired
  private CartService cartService;

  @GetMapping("/api/cart/items")
  public ResponseEntity<List<CartResponseDto>> getCartItems(
      @CookieValue(value = "token", required = false) String token) {

    if (!jwtService.isValid(token)) {
      throw new UnauthorizedException();
    }

    int memberId = jwtService.getId(token);
    List<CartResponseDto> cartItems = cartService.getCartItems(memberId);

    return new ResponseEntity<>(cartItems, HttpStatus.OK);
  }

  @PostMapping("/api/cart/items/{itemId}")
  public ResponseEntity<Void> pushCartItem(
      @PathVariable("itemId") int itemId,
      @CookieValue(value = "token", required = false) String token) {

    if (!jwtService.isValid(token)) {
      throw new UnauthorizedException();
    }

    int memberId = jwtService.getId(token);
    cartService.addCartItem(memberId, itemId);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @DeleteMapping("/api/cart/items/{itemId}")
  public ResponseEntity<Void> removeCartItem(
      @PathVariable("itemId") int itemId,
      @CookieValue(value = "token", required = false) String token) {

    if (!jwtService.isValid(token)) {
      throw new UnauthorizedException();
    }

    int memberId = jwtService.getId(token);
    cartService.removeCartItem(memberId, itemId);

    return new ResponseEntity<>(HttpStatus.OK);
  }
}
