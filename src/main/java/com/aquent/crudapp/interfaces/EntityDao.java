package com.aquent.crudapp.interfaces;

import java.util.List;

import org.springframework.stereotype.Repository;

/**
 * Operations on the "person" table.
 */
@Repository
public interface EntityDao<E> {

    /**
     * Retrieves all of the entity records.
     *
     * @return list of entity records
     */
    List<E> listEntities();

    /**
     * Creates a new person record.
     *
     * @param entity the values to save
     * @return the new entity ID
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
     * Updates an existing person record.
     *
     * @param person the new values to save
     */
    void updateEntity(E person);

    /**
     * Deletes a person record by ID.
     *
     * @param id the person ID
     */
    void deleteEntity(Integer id);
}
