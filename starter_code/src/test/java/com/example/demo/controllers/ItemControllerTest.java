package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {

        this.itemController = new ItemController();
        TestUtils.injectObject(this.itemController, "itemRepository", this.itemRepository);
    }

    @Test
    public void getItemsTest() {

        Item item1 = HelperControllerTest.createItem(1l, "smartphone", 599d);
        Item item2 = HelperControllerTest.createItem(2l, "laptop", 999.99);
        List<Item> items = Arrays.asList(item1, item2);

        when(this.itemRepository.findAll()).thenReturn(items);
        final ResponseEntity<List<Item>> response = this.itemController.getItems();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> itemList = response.getBody();
        assertNotNull(itemList);
        assertEquals(2, itemList.size());
    }

    @Test
    public void getItemByIdTest() {

        Item item = HelperControllerTest.createItem(1l, "smartphone", 599d);
        when(this.itemRepository.findById(1l)).thenReturn(Optional.of(item));
        final ResponseEntity<Item> response = this.itemController.getItemById(1l);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Item responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("smartphone", responseBody.getName());
    }

    @Test
    public void getItemsByNameTest() {

        Item item = HelperControllerTest.createItem(1l, "smartphone", 599d);
        List<Item> items = Arrays.asList(item);

        when(this.itemRepository.findByName("smartphone")).thenReturn(items);
        final ResponseEntity<List<Item>> response = this.itemController.getItemsByName("smartphone");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> itemList = response.getBody();

        assertNotNull(itemList);
        assertEquals("smartphone", itemList.get(0).getName());
        assertEquals(1, itemList.size());
    }
}
