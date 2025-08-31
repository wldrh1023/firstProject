package com.cheonmyo.shop.service;

import com.cheonmyo.shop.dto.CartResponseDto;
import com.cheonmyo.shop.entity.Cart;
import com.cheonmyo.shop.entity.Item;
import com.cheonmyo.shop.exception.NotFoundException;
import com.cheonmyo.shop.repository.CartRepository;
import com.cheonmyo.shop.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

  @Autowired
  private CartRepository cartRepository;

  @Autowired
  private ItemRepository itemRepository;

  @Override
  public List<CartResponseDto> getCartItems(int memberId) {
    List<Cart> carts = cartRepository.findByMemberId(memberId);
    List<Integer> itemIds = carts.stream().map(Cart::getItemId).toList();
    List<Item> items = itemRepository.findByIdIn(itemIds);

    List<CartResponseDto> cartItems = new ArrayList<>();
    for (Cart cart : carts) {
      Item item = items.stream()
          .filter(i -> i.getId() == cart.getItemId())
          .findFirst()
          .orElse(null);
      if (item != null) {
        cartItems.add(new CartResponseDto(item));
      }
    }

    return cartItems;
  }

  @Override
  public void addCartItem(int memberId, int itemId) {
    Cart existingCart = cartRepository.findByMemberIdAndItemId(memberId, itemId);

    if (existingCart == null) {
      Cart newCart = new Cart();
      newCart.setMemberId(memberId);
      newCart.setItemId(itemId);
      cartRepository.save(newCart);
    }
  }

  @Override
  public void removeCartItem(int memberId, int itemId) {
    System.out.println("=== CartService.removeCartItem ===");
    System.out.println("회원 ID: " + memberId);
    System.out.println("상품 ID: " + itemId);

    Cart cart = cartRepository.findByMemberIdAndItemId(memberId, itemId);
    System.out.println("찾은 장바구니 항목: " + cart);

    if (cart == null) {
      System.out.println("장바구니에서 상품을 찾을 수 없음");
      throw new NotFoundException("장바구니에서 해당 상품을 찾을 수 없습니다.");
    }

    System.out.println("장바구니 항목 삭제 중...");
    cartRepository.delete(cart);
    System.out.println("장바구니 항목 삭제 완료");
  }
}
