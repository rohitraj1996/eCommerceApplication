package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private final String username = "test";
    private final String password = "password";

    private OrderController orderController;
    private OrderRepository orderRepository = mock(OrderRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);

    @Before
    public void setUp() {
        this.orderController = new OrderController();
        TestUtils.injectObject(this.orderController, "orderRepository", this.orderRepository);
        TestUtils.injectObject(this.orderController, "userRepository", this.userRepository);
    }

    @Test
    public void submitSuccessTest() {

        Cart cart = HelperControllerTest.createCart(1l, this.username, this.password);
        User user = cart.getUser();
        when(this.userRepository.findByUsername(this.username)).thenReturn(user);

        final ResponseEntity<UserOrder> response = this.orderController.submit(this.username);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        UserOrder userOrder = response.getBody();
        assertNotNull(userOrder);
        assertEquals(this.username, userOrder.getUser().getUsername());
        assertEquals(cart.getTotal(), userOrder.getTotal());
    }

    @Test
    public void submitFailTest() {
        when(this.userRepository.findByUsername(this.username)).thenReturn(null);

        final ResponseEntity<UserOrder> response = this.orderController.submit(this.username);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void getOrdersForUserSuccessTest() {

        Cart cart = HelperControllerTest.createCart(1l, this.username, this.password);
        User user = cart.getUser();
        when(this.userRepository.findByUsername(this.username)).thenReturn(user);

        final ResponseEntity<UserOrder> response = this.orderController.submit(this.username);
        UserOrder userOrder = response.getBody();
        List<UserOrder> userOrders = Arrays.asList(userOrder);

        when(this.orderRepository.findByUser(user)).thenReturn(userOrders);
        final ResponseEntity<List<UserOrder>> userOrderResponse = this.orderController.getOrdersForUser(this.username);

        assertNotNull(userOrderResponse);
        assertEquals(200, userOrderResponse.getStatusCodeValue());

        List<UserOrder> actualOrders = userOrderResponse.getBody();
        assertNotNull(actualOrders);
        assertEquals(userOrders.get(0).getTotal(), actualOrders.get(0).getTotal());
    }

    @Test
    public void getOrdersForUserFailTest() {

        final ResponseEntity<List<UserOrder>> userOrderResponse = this.orderController.getOrdersForUser(this.username);

        assertNotNull(userOrderResponse);
        assertEquals(404, userOrderResponse.getStatusCodeValue());
    }
}
