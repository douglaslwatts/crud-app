package com.aquent.crudapp.client;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Defines required functionality for client CRUD operations.
 */
@Service
public interface clientService {

    /**
     * Retrieves all client tuples.
     *
     * @return a list of all clients
     */
    List<Client> listClients();

}
