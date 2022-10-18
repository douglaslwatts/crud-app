package com.aquent.crudapp.interfaces;

import java.util.List;

import org.springframework.stereotype.Service;

/**
 * Person operations.
 */
@Service
public interface EntityService<E, T> {

    /**
     * Retrieves all entity records.
     *
     * @return list of entity records
     */
    List<E> listEntities();

    /**
     * Creates a new entity record.
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
     * Updates an existing entity record.
     *
     * @param entity the new values to save
     */
    void updateEntity(E entity);

    /**
     * Deletes a entity record by ID.
     *
     * @param id the entity ID
     */
    void deleteEntity(Integer id);

    /**
     * Validates populated entity data.
     *
     * @param entity the values to validate
     * @return list of error messages
     */
    List<String> validateEntity(E entity);


    List<T> getAssociations(Integer entityId);

    List<T> getAvailableAssociations(Integer entityId);

    void addAssociation(Integer entityId, Integer associationId);
}
