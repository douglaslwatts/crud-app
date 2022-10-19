package com.aquent.crudapp.person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.aquent.crudapp.client.Client;
import com.aquent.crudapp.interfaces.EntityDao;
import com.aquent.crudapp.interfaces.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of {@link EntityService}.
 */
@Service
@Qualifier("personService")
public class PersonService implements EntityService<Person, Client> {

    @Autowired
    @Qualifier("personDAO")
    private final EntityDao<Person, Client> entityDao;
    private final Validator                 validator;

    public PersonService(EntityDao<Person, Client> entityDao, Validator validator) {
        this.entityDao = entityDao;
        this.validator = validator;
    }

    /**
     * Retrieves all person records.
     *
     * @return list of person records
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Person> listEntities() {
        return entityDao.listEntities();
    }

    /**
     * Get a list of all entities associated with this entity via entity ID
     *
     * @param personId The entity ID field of this entity
     * @return A list of all entities associated with this entity
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Client> getAssociations(Integer personId) {
        return entityDao.getAssociations(personId);
    }

    /**
     * Get a list of all entities not associated with this entity.
     *
     * @param personId The entity ID field of this entity
     * @return A list of all entities not associated with this entity
     */
    @Override
    public List<Client> getAvailableAssociations(Integer personId) {
        return entityDao.getAvailableAssociations(personId);
    }

    /**
     * Add an association with a given entity.
     *
     * @param personId The ID of the entity which should be associated
     * @param clientId The ID of this entity
     */
    @Override
    public void addAssociation(Integer personId, Integer clientId) {
        entityDao.addAssociation(personId, clientId);
    }

    /**
     * Retrieves a person record by ID.
     *
     * @param id the client ID
     *
     * @return the client record
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Person readEntity(Integer id) {
        return entityDao.readEntity(id);
    }

    /**
     * Retrieve an associated entity via ID
     *
     * @param clientId The ID of the associated entity to retrieve
     * @return The associated entity
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Client readAssociatedEntity(Integer clientId) {
        return entityDao.readAssociatedEntity(clientId);
    }

    /**
     * Creates a new person record.
     *
     * @param person the values to save
     *
     * @return the new client ID
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public Integer createEntity(Person person) {
        return entityDao.createEntity(person);
    }

    /**
     * Updates an existing person record.
     *
     * @param person the new values to save
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public void updateEntity(Person person) {
        entityDao.updateEntity(person);
    }

    /**
     * Deletes a person record by ID.
     *
     * @param id the client ID
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public void deleteEntity(Integer id) {
        entityDao.deleteEntity(id);
    }

    /**
     * Remove an association with a given entity via ID
     *
     * @param personId The ID of the associated entity, the association with which is to be
     *                      removed
     * @param clientId The ID of this entity
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public void removeAssociation(Integer personId, Integer clientId) {
        entityDao.removeAssociation(personId, clientId);
    }

    /**
     * Validates populated person data.
     *
     * @param person the values to validate
     *
     * @return list of error messages
     */
    @Override
    public List<String> validateEntity(Person person) {
        Set<ConstraintViolation<Person>> violations = validator.validate(person);
        List<String> errors = new ArrayList<String>(violations.size());
        for (ConstraintViolation<Person> violation : violations) {
            errors.add(violation.getMessage());
        }
        Collections.sort(errors);
        return errors;
    }
}
