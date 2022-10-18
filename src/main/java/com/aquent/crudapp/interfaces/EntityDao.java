package com.aquent.crudapp.interfaces;

import java.util.List;

import org.springframework.stereotype.Repository;

/**
 * Operations on the "person" table.
 */
@Repository
public interface EntityDao<E, T> {

    /**
     * Retrieves all entity records.
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

    T readAssociatedEntity(Integer entityId);

    void removeAssociation(Integer entityId, Integer associationId);

    /**
     * Updates an existing person record.
     *
     * @param entity the new values to save
     */
    void updateEntity(E entity);

    /**
     * Deletes an entity record by ID.
     *
     * @param id the entity ID
     */
    void deleteEntity(Integer id);

    List<T> getAssociations(Integer entityId);

}
