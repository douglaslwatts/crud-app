package com.aquent.crudapp.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.aquent.crudapp.interfaces.EntityDao;
import com.aquent.crudapp.model.Client;
import com.aquent.crudapp.model.Person;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.validation.Validator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = { PersonService.class })
@ExtendWith(SpringExtension.class)
class PersonServiceTest {

    @MockBean(name = "personDAO")
    private EntityDao<Person, Client> entityDao;

    @MockBean
    private EntityDao<Person, Client> entityDao2;

    @Autowired
    private PersonService personService;

    @MockBean
    private Validator validator;

    /**
     * Method under test: {@link PersonService#listEntities()}
     */
    @Test
    void testListEntities() {
        ArrayList<Person> personList = new ArrayList<>();
        when(entityDao.listEntities()).thenReturn(personList);
        List<Person> actualListEntitiesResult = personService.listEntities();
        assertSame(personList, actualListEntitiesResult);
        assertTrue(actualListEntitiesResult.isEmpty());
        verify(entityDao).listEntities();
    }

    /**
     * Method under test: {@link PersonService#getAssociations(Integer)}
     */
    @Test
    void testGetAssociations() {
        ArrayList<Client> clientList = new ArrayList<>();
        when(entityDao.getAssociations(Mockito.<Integer>any())).thenReturn(clientList);
        List<Client> actualAssociations = personService.getAssociations(1);
        assertSame(clientList, actualAssociations);
        assertTrue(actualAssociations.isEmpty());
        verify(entityDao).getAssociations(Mockito.<Integer>any());
    }

    /**
     * Method under test: {@link PersonService#getAvailableAssociations(Integer)}
     */
    @Test
    void testGetAvailableAssociations() {
        ArrayList<Client> clientList = new ArrayList<>();
        when(entityDao.getAvailableAssociations(Mockito.<Integer>any())).thenReturn(clientList);
        List<Client> actualAvailableAssociations = personService.getAvailableAssociations(1);
        assertSame(clientList, actualAvailableAssociations);
        assertTrue(actualAvailableAssociations.isEmpty());
        verify(entityDao).getAvailableAssociations(Mockito.<Integer>any());
    }

    /**
     * Method under test: {@link PersonService#addAssociation(Integer, Integer)}
     */
    @Test
    void testAddAssociation() {
        doNothing().when(entityDao).addAssociation(Mockito.<Integer>any(), Mockito.<Integer>any());
        personService.addAssociation(1, 1);
        verify(entityDao).addAssociation(Mockito.<Integer>any(), Mockito.<Integer>any());
    }

    /**
     * Method under test: {@link PersonService#readEntity(Integer)}
     */
    @Test
    void testReadEntity() {
        Person person = new Person();
        person.setCity("Oxford");
        person.setEmailAddress("42 Main St");
        person.setEntityId(1);
        person.setFirstName("Jane");
        person.setLastName("Doe");
        person.setState("MD");
        person.setStreetAddress("42 Main St");
        person.setZipCode("21654");
        when(entityDao.readEntity(Mockito.<Integer>any())).thenReturn(person);
        assertSame(person, personService.readEntity(1));
        verify(entityDao).readEntity(Mockito.<Integer>any());
    }

    /**
     * Method under test: {@link PersonService#readAssociatedEntity(Integer)}
     */
    @Test
    void testReadAssociatedEntity() {
        Client client = new Client();
        client.setCity("Oxford");
        client.setCompanyName("Company Name");
        client.setEntityId(1);
        client.setPhone("6625550144");
        client.setState("MD");
        client.setStreetAddress("42 Main St");
        client.setWebsite("Website");
        client.setZipCode("21654");
        when(entityDao.readAssociatedEntity(Mockito.<Integer>any())).thenReturn(client);
        assertSame(client, personService.readAssociatedEntity(1));
        verify(entityDao).readAssociatedEntity(Mockito.<Integer>any());
    }

    /**
     * Method under test: {@link PersonService#createEntity(Person)}
     */
    @Test
    void testCreateEntity() {
        when(entityDao.createEntity(Mockito.<Person>any())).thenReturn(1);

        Person person = new Person();
        person.setCity("Oxford");
        person.setEmailAddress("42 Main St");
        person.setEntityId(1);
        person.setFirstName("Jane");
        person.setLastName("Doe");
        person.setState("MD");
        person.setStreetAddress("42 Main St");
        person.setZipCode("21654");
        assertEquals(1, personService.createEntity(person).intValue());
        verify(entityDao).createEntity(Mockito.<Person>any());
    }

    /**
     * Method under test: {@link PersonService#updateEntity(Person)}
     */
    @Test
    void testUpdateEntity() {
        doNothing().when(entityDao).updateEntity(Mockito.<Person>any());

        Person person = new Person();
        person.setCity("Oxford");
        person.setEmailAddress("42 Main St");
        person.setEntityId(1);
        person.setFirstName("Jane");
        person.setLastName("Doe");
        person.setState("MD");
        person.setStreetAddress("42 Main St");
        person.setZipCode("21654");
        personService.updateEntity(person);
        verify(entityDao).updateEntity(Mockito.<Person>any());
        assertEquals("Oxford", person.getCity());
        assertEquals("21654", person.getZipCode());
        assertEquals("42 Main St", person.getStreetAddress());
        assertEquals("MD", person.getState());
        assertEquals("Doe", person.getLastName());
        assertEquals("Jane", person.getFirstName());
        assertEquals(1, person.getEntityId().intValue());
        assertEquals("42 Main St", person.getEmailAddress());
    }

    /**
     * Method under test: {@link PersonService#deleteEntity(Integer)}
     */
    @Test
    void testDeleteEntity() {
        doNothing().when(entityDao).deleteEntity(Mockito.<Integer>any());
        personService.deleteEntity(1);
        verify(entityDao).deleteEntity(Mockito.<Integer>any());
    }

    /**
     * Method under test: {@link PersonService#removeAssociation(Integer, Integer)}
     */
    @Test
    void testRemoveAssociation() {
        doNothing().when(entityDao).removeAssociation(Mockito.<Integer>any(),
                                                      Mockito.<Integer>any());
        personService.removeAssociation(1, 1);
        verify(entityDao).removeAssociation(Mockito.<Integer>any(), Mockito.<Integer>any());
    }

    /**
     * Method under test: {@link PersonService#validateEntity(Person)}
     */
    @Test
    void testValidateEntity() {
        when(validator.validate(Mockito.<Person>any(), (Class[]) any())).thenReturn(
                new HashSet<>());

        Person person = new Person();
        person.setCity("Oxford");
        person.setEmailAddress("42 Main St");
        person.setEntityId(1);
        person.setFirstName("Jane");
        person.setLastName("Doe");
        person.setState("MD");
        person.setStreetAddress("42 Main St");
        person.setZipCode("21654");
        assertTrue(personService.validateEntity(person).isEmpty());
        verify(validator).validate(Mockito.<Person>any(), (Class[]) any());
    }
}

