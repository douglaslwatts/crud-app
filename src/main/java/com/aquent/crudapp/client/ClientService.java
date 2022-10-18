package com.aquent.crudapp.client;

import com.aquent.crudapp.interfaces.EntityDao;
import com.aquent.crudapp.interfaces.EntityService;
import com.aquent.crudapp.person.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
@Qualifier("clientService")
public class ClientService implements EntityService<Client, Person> {

    @Autowired
    @Qualifier("clientDAO")
    private final EntityDao<Client, Person> entityDao;

    private final Validator validator;

    public ClientService(EntityDao<Client, Person> entityDao, Validator validator) {
        this.entityDao = entityDao;
        this.validator = validator;
    }

    /**
     * Retrieves all client records.
     *
     * @return list of client records
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Client> listEntities() {
        return entityDao.listEntities();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Person> getAssociations(Integer clientId) {
        return entityDao.getAssociations(clientId);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Person readAssociatedEntity(Integer personId) {
        return entityDao.readAssociatedEntity(personId);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public void removeAssociation(Integer clientId, Integer personId) {
        entityDao.removeAssociation(clientId, personId);
    }

    /**
     * Creates a new client record.
     *
     * @param client the values to save
     *
     * @return the new client ID
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Integer createEntity(Client client) {
        return entityDao.createEntity(client);
    }

    /**
     * Retrieves a client record by ID.
     *
     * @param clientId the client ID
     *
     * @return the client record
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Client readEntity(Integer clientId) {
        return entityDao.readEntity(clientId);
    }

    /**
     * Updates an existing client record.
     *
     * @param client the new values to save
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public void updateEntity(Client client) {
        entityDao.updateEntity(client);
    }

    /**
     * Deletes a client record by ID.
     *
     * @param clientId the client ID
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public void deleteEntity(Integer clientId) {
        entityDao.deleteEntity(clientId);
    }

    /**
     * Validates populated client data.
     *
     * @param client the values to validate
     *
     * @return list of error messages
     */
    @Override
    public List<String> validateEntity(Client client) {
        Set<ConstraintViolation<Client>> violations = validator.validate(client);
        List<String> errors = new ArrayList<>(violations.size());

        for (ConstraintViolation<Client> violation : violations) {
            errors.add(violation.getMessage());
        }

        Collections.sort(errors);
        return errors;
    }

}
