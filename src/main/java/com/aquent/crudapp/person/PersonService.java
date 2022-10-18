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

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Person> listEntities() {
        return entityDao.listEntities();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Client> getAssociations(Integer personId) {
        return entityDao.getAssociations(personId);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Person readEntity(Integer id) {
        return entityDao.readEntity(id);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Client readAssociatedEntity(Integer clientId) {
        return entityDao.readAssociatedEntity(clientId);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public Integer createEntity(Person person) {
        return entityDao.createEntity(person);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public void updateEntity(Person person) {
        entityDao.updateEntity(person);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public void deleteEntity(Integer id) {
        entityDao.deleteEntity(id);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public void removeAssociation(Integer personId, Integer clientId) {
        entityDao.removeAssociation(personId, clientId);
    }

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
