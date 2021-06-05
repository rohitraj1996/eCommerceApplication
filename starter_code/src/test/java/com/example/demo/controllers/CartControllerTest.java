package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private final String username = "test";
    private final String password = "password";

    private CartController cartController;
    private ItemRepository itemRepository = mock(ItemRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);

    @Before
    public void setUp() {
        this.cartController = new CartController();
        TestUtils.injectObject(this.cartController, "itemRepository", this.itemRepository);
        TestUtils.injectObject(this.cartController, "cartRepository", this.cartRepository);
        TestUtils.injectObject(this.cartController, "userRepository", this.userRepository);
    }

    @Test
    public void addToCartSuccessTest() {

        Cart cart = HelperControllerTest.createCart(1l, this.username, this.password);
        User user = cart.getUser();
        when(this.userRepository.findByUsername(this.username)).thenReturn(user);

        Item item = HelperControllerTest.createItem(1L, "item1", 100d);
        when(this.itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ResponseEntity<Cart> response = this.cartController.addTocart(this.createModifyCartRequest());
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart actualCart = response.getBody();
        assertNotNull(actualCart);
        assertEquals(this.username, actualCart.getUser().getUsername());
        assertEquals(4, actualCart.getItems().size());
    }

    @Test
    public void addToCartFailTest() {

        ResponseEntity<Cart> userNullFailResponse = this.cartController.addTocart(new ModifyCartRequest());
        assertNotNull(userNullFailResponse);
        assertEquals(404, userNullFailResponse.getStatusCodeValue());

        Cart cart = HelperControllerTest.createCart(1l, this.username, this.password);
        User user = cart.getUser();
        when(this.userRepository.findByUsername(this.username)).thenReturn(user);

        ModifyCartRequest modifyCartRequest = this.createModifyCartRequest();
        when(this.itemRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Cart> itemNotPresentResponse = this.cartController.addTocart(modifyCartRequest);
        assertNotNull(itemNotPresentResponse);
        assertEquals(404, itemNotPresentResponse.getStatusCodeValue());

    }

    @Test
    public void removeFromCartSuccessTest() {

        Cart cart = HelperControllerTest.createCart(1l, this.username, this.password);
        User user = cart.getUser();
        when(this.userRepository.findByUsername(this.username)).thenReturn(user);

        Item item = HelperControllerTest.createItem(1L, "item1", 100d);
        when(this.itemRepository.findById(1L)).thenReturn(Optional.of(item));

        ResponseEntity<Cart> response = this.cartController.removeFromcart(this.createModifyCartRequest());
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart actualCart = response.getBody();
        assertNotNull(actualCart);
        assertEquals(this.username, actualCart.getUser().getUsername());
        assertEquals(2, actualCart.getItems().size());
    }

    @Test
    public void removeFromCartFailTest() {

        ResponseEntity<Cart> userNullFailResponse = this.cartController.removeFromcart(new ModifyCartRequest());
        assertNotNull(userNullFailResponse);
        assertEquals(404, userNullFailResponse.getStatusCodeValue());

        Cart cart = HelperControllerTest.createCart(1l, this.username, this.password);
        User user = cart.getUser();
        when(this.userRepository.findByUsername(this.username)).thenReturn(user);

        ModifyCartRequest modifyCartRequest = this.createModifyCartRequest();
        when(this.itemRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Cart> itemNotPresentResponse = this.cartController.removeFromcart(modifyCartRequest);
        assertNotNull(itemNotPresentResponse);
        assertEquals(404, itemNotPresentResponse.getStatusCodeValue());
    }

    private ModifyCartRequest createModifyCartRequest() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setItemId(1L);
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setUsername(this.username);

        return modifyCartRequest;
    }
}
