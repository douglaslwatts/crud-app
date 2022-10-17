package com.aquent.crudapp.interfaces;

import java.util.List;

import org.springframework.stereotype.Service;

/**
 * Person operations.
 */
@Service
public interface EntityService<E> {

    /**
     * Retrieves all of the person records.
     *
     * @return list of person records
     */
    List<E> listEntities();

    /**
     * Creates a new entity record.
     *
     * @param entity the values to save
     * @return the new person ID
     */
    Integer createEntity(E entity);

    /**
     * Retrieves a entity record by ID.
     *
     * @param id the entity ID
     * @return the entity record
     */
    E readEntity(Integer id);

    /**
     * Updates an existing entity record.
     *
     * @param entity the new values to save
     */
    void updateEntity(E entity);

    /**
     * Deletes a person record by ID.
     *
     * @param id the person ID
     */
    void deleteEntity(Integer id);

    /**
     * Validates populated entity data.
     *
     * @param entity the values to validate
     * @return list of error messages
     */
    List<String> validateEntity(E entity);
}
