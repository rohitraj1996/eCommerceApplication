package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.requests.CreateUserRequest;

import java.math.BigDecimal;

public class HelperControllerTest {

    public static CreateUserRequest createUserRequest(String username, String password) {
        CreateUserRequest user = new CreateUserRequest();
        user.setUsername(username);
        user.setPassword(password);
        user.setConfirmPassword(password);

        return user;
    }

    public static Item createItem(Long itemId, String name, Double price) {
        Item item = new Item();
        item.setId(itemId);
        item.setName(name);
        item.setPrice(BigDecimal.valueOf(price));

        return item;
    }

    public static User createUser(Long id, String username, String password) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setPassword(password);

        return user;
    }

    public static Cart createCart(Long userId, String username, String password) {
        User user = HelperControllerTest.createUser(userId, username, password);
        Cart cart = new Cart();
        cart.setId(1l);
        cart.setUser(user);

        for (int i = 0; i < 3; i++) {
            cart.addItem(HelperControllerTest.createItem(i+1l, "item"+(i+1), 100*(i+1.0)));
        }

        user.setCart(cart);
        return cart;
    }
}
