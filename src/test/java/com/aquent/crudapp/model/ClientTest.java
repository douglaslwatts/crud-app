package com.aquent.crudapp.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class ClientTest {

    /**
     * Methods under test:
     *
     * <ul>
     *   <li>default or parameterless constructor of {@link Client}
     *   <li>{@link Client#setCompanyName(String)}
     *   <li>{@link Client#setPhone(String)}
     *   <li>{@link Client#setWebsite(String)}
     *   <li>{@link Client#getCompanyName()}
     *   <li>{@link Client#getPhone()}
     *   <li>{@link Client#getWebsite()}
     * </ul>
     */
    @Test
    void testConstructor() {
        Client actualClient = new Client();
        actualClient.setCompanyName("Company Name");
        actualClient.setPhone("6625550144");
        actualClient.setWebsite("Website");
        assertEquals("Company Name", actualClient.getCompanyName());
        assertEquals("6625550144", actualClient.getPhone());
        assertEquals("Website", actualClient.getWebsite());
    }
}

