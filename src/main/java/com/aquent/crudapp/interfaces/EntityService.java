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

    /**
     * Retrieve an associated entity via ID
     *
     * @param entityId The ID of the associated entity to retrieve
     * @return The associated entity
     */
    T readAssociatedEntity(Integer entityId);

    /**
     * Remove an association with a given entity via ID
     *
     * @param entityId The ID of this entity
     * @param associationId The ID of the associated entity, the association with which is to be
     *                      removed
     */
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


    /**
     * Get a list of all entities associated with this entity via entity ID
     *
     * @param entityId The entity ID field of this entity
     * @return A list of all entities associated with this entity
     */
    List<T> getAssociations(Integer entityId);

    /**
     * Get a list of all entities not associated with this entity.
     *
     * @param entityId The entity ID field of this entity
     * @return A list of all entities not associated with this entity
     */
    List<T> getAvailableAssociations(Integer entityId);

    /**
     * Add an association with a given entity.
     *
     * @param entityId The ID of this entity
     * @param associationId The ID of the entity which should be associated
     */
    void addAssociation(Integer entityId, Integer associationId);
}
