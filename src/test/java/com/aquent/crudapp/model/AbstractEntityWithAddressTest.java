package com.aquent.crudapp.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class AbstractEntityWithAddressTest {

    /**
     * Method under test: {@link AbstractEntityWithAddress#setEntityId(Integer)}
     */
    @Test
    void testSetEntityId() {
        Client client = new Client();
        client.setEntityId(1);
        assertEquals(1, client.getEntityId().intValue());
    }

    /**
     * Method under test: {@link AbstractEntityWithAddress#getEntityId()}
     */
    @Test
    void testGetEntityId() {
        assertNull((new Client()).getEntityId());
    }

    /**
     * Method under test: {@link AbstractEntityWithAddress#getStreetAddress()}
     */
    @Test
    void testGetStreetAddress() {
        assertNull((new Client()).getStreetAddress());
    }

    /**
     * Method under test: {@link AbstractEntityWithAddress#setStreetAddress(String)}
     */
    @Test
    void testSetStreetAddress() {
        Client client = new Client();
        client.setStreetAddress("42 Main St");
        assertEquals("42 Main St", client.getStreetAddress());
    }

    /**
     * Method under test: {@link AbstractEntityWithAddress#getCity()}
     */
    @Test
    void testGetCity() {
        assertNull((new Client()).getCity());
    }

    /**
     * Method under test: {@link AbstractEntityWithAddress#setCity(String)}
     */
    @Test
    void testSetCity() {
        Client client = new Client();
        client.setCity("Oxford");
        assertEquals("Oxford", client.getCity());
    }

    /**
     * Method under test: {@link AbstractEntityWithAddress#getState()}
     */
    @Test
    void testGetState() {
        assertNull((new Client()).getState());
    }

    /**
     * Method under test: {@link AbstractEntityWithAddress#setState(String)}
     */
    @Test
    void testSetState() {
        Client client = new Client();
        client.setState("MD");
        assertEquals("MD", client.getState());
    }

    /**
     * Method under test: {@link AbstractEntityWithAddress#getZipCode()}
     */
    @Test
    void testGetZipCode() {
        assertNull((new Client()).getZipCode());
    }

    /**
     * Method under test: {@link AbstractEntityWithAddress#setZipCode(String)}
     */
    @Test
    void testSetZipCode() {
        Client client = new Client();
        client.setZipCode("21654");
        assertEquals("21654", client.getZipCode());
    }
}

