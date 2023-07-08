package com.aquent.crudapp.controllers;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.aquent.crudapp.interfaces.EntityService;
import com.aquent.crudapp.model.Client;
import com.aquent.crudapp.model.Person;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = { PersonController.class })
@ExtendWith(SpringExtension.class)
class PersonControllerTest {

    @MockBean(name = "personService")
    private EntityService<Person, Client> entityService;

    @MockBean
    private EntityService<Person, Client> entityService2;

    @Autowired
    private PersonController personController;

    /**
     * Method under test: {@link PersonController#addAvailable(Integer, Integer, String)}
     */
    @Test
    void testAddAvailable() throws Exception {
        doNothing().when(entityService).addAssociation(Mockito.<Integer>any(),
                                                       Mockito.<Integer>any());
        MockHttpServletRequestBuilder postResult = MockMvcRequestBuilders
                .post("/person/available-clients/{associatedEntityId}", 1);
        MockHttpServletRequestBuilder requestBuilder = postResult.param("entityId",
                                                                        String.valueOf(1))
                .param("referrer", "foo");
        MockMvcBuilders.standaloneSetup(personController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/person/person-view/1"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/person/person-view/1"));
    }

    /**
     * Method under test: {@link PersonController#addAvailable(Integer, Integer, String)}
     */
    @Test
    void testAddAvailable2() throws Exception {
        doNothing().when(entityService).addAssociation(Mockito.<Integer>any(),
                                                       Mockito.<Integer>any());
        MockHttpServletRequestBuilder postResult = MockMvcRequestBuilders
                .post("/person/available-clients/{associatedEntityId}", 1);
        MockHttpServletRequestBuilder requestBuilder = postResult.param("entityId",
                                                                        String.valueOf(1))
                .param("referrer", PersonController.EDIT_REFERRER);
        MockMvcBuilders.standaloneSetup(personController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/person/edit/1"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/person/edit/1"));
    }

    /**
     * Method under test: {@link PersonController#back(Integer)}
     */
    @Test
    void testBack() throws Exception {
        MockHttpServletRequestBuilder postResult = MockMvcRequestBuilders.post(
                "/person/person-view/{entityId}", 1);
        MockHttpServletRequestBuilder requestBuilder = postResult.param("entityId",
                                                                        String.valueOf(1));
        MockMvcBuilders.standaloneSetup(personController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/person/person-view/1"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/person/person-view/1"));
    }

    /**
     * Method under test: {@link PersonController#create()}
     */
    @Test
    void testCreate() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/person/create");
        MockMvcBuilders.standaloneSetup(personController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.model().attributeExists("errors", "person"))
                .andExpect(MockMvcResultMatchers.view().name("person/create"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("person/create"));
    }

    /**
     * Method under test: {@link PersonController#create()}
     */
    @Test
    void testCreate2() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/person/create");
        requestBuilder.characterEncoding("Encoding");
        MockMvcBuilders.standaloneSetup(personController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.model().attributeExists("errors", "person"))
                .andExpect(MockMvcResultMatchers.view().name("person/create"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("person/create"));
    }

    /**
     * Method under test: {@link PersonController#create(Person)}
     */
    @Test
    void testCreate3() throws Exception {
        when(entityService.createEntity(Mockito.<Person>any())).thenReturn(1);
        when(entityService.validateEntity(Mockito.<Person>any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                "/person/create");
        MockMvcBuilders.standaloneSetup(personController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("person"))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/person/list"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/person/list"));
    }

    /**
     * Method under test: {@link PersonController#create(Person)}
     */
    @Test
    void testCreate4() throws Exception {
        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("?");
        when(entityService.createEntity(Mockito.<Person>any())).thenReturn(1);
        when(entityService.validateEntity(Mockito.<Person>any())).thenReturn(stringList);
        when(entityService2.createEntity(Mockito.<Person>any())).thenReturn(1);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                "/person/create");
        MockMvcBuilders.standaloneSetup(personController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.model().attributeExists("errors", "person"))
                .andExpect(MockMvcResultMatchers.view().name("person/create"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("person/create"));
    }

    /**
     * Method under test: {@link PersonController#delete(Integer)}
     */
    @Test
    void testDelete() throws Exception {
        Person person = new Person();
        person.setCity("Oxford");
        person.setEmailAddress("42 Main St");
        person.setEntityId(1);
        person.setFirstName("Jane");
        person.setLastName("Doe");
        person.setState("MD");
        person.setStreetAddress("42 Main St");
        person.setZipCode("21654");
        when(entityService.readEntity(Mockito.<Integer>any())).thenReturn(person);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/person/delete/{entityId}", 1);
        MockMvcBuilders.standaloneSetup(personController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("person"))
                .andExpect(MockMvcResultMatchers.view().name("person/delete"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("person/delete"));
    }

    /**
     * Method under test: {@link PersonController#delete(String, Integer)}
     */
    @Test
    void testDelete2() throws Exception {
        MockHttpServletRequestBuilder paramResult = MockMvcRequestBuilders.post(
                "/person/delete").param("command", "foo");
        MockHttpServletRequestBuilder requestBuilder = paramResult.param("entityId",
                                                                         String.valueOf(1));
        MockMvcBuilders.standaloneSetup(personController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/person/list"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/person/list"));
    }

    /**
     * Method under test: {@link PersonController#delete(String, Integer)}
     */
    @Test
    void testDelete3() throws Exception {
        doNothing().when(entityService).deleteEntity(Mockito.<Integer>any());
        MockHttpServletRequestBuilder paramResult = MockMvcRequestBuilders.post("/person/delete")
                .param("command", PersonController.COMMAND_DELETE);
        MockHttpServletRequestBuilder requestBuilder = paramResult.param("entityId",
                                                                         String.valueOf(1));
        MockMvcBuilders.standaloneSetup(personController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/person/list"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/person/list"));
    }

    /**
     * Method under test: {@link PersonController#edit(Person, String)}
     */
    @Test
    void testEdit() throws Exception {
        when(entityService.validateEntity(Mockito.<Person>any())).thenReturn(new ArrayList<>());
        doNothing().when(entityService).updateEntity(Mockito.<Person>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/person/edit")
                .param("command", "foo");
        MockMvcBuilders.standaloneSetup(personController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("person"))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/person/list"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/person/list"));
    }

    /**
     * Method under test: {@link PersonController#edit(Person, String)}
     */
    @Test
    void testEdit2() throws Exception {
        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("?");
        when(entityService.validateEntity(Mockito.<Person>any())).thenReturn(stringList);
        doNothing().when(entityService).updateEntity(Mockito.<Person>any());
        doNothing().when(entityService2).updateEntity(Mockito.<Person>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/person/edit")
                .param("command", "foo");
        MockMvcBuilders.standaloneSetup(personController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.model().attributeExists("errors", "person"))
                .andExpect(MockMvcResultMatchers.view().name("person/edit"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("person/edit"));
    }

    /**
     * Method under test: {@link PersonController#edit(Integer)}
     */
    @Test
    void testEdit3() throws Exception {
        Person person = new Person();
        person.setCity("Oxford");
        person.setEmailAddress("42 Main St");
        person.setEntityId(1);
        person.setFirstName("Jane");
        person.setLastName("Doe");
        person.setState("MD");
        person.setStreetAddress("42 Main St");
        person.setZipCode("21654");
        when(entityService.readEntity(Mockito.<Integer>any())).thenReturn(person);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/person/edit/{entityId}", 1);
        MockMvcBuilders.standaloneSetup(personController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.model().attributeExists("errors", "person"))
                .andExpect(MockMvcResultMatchers.view().name("person/edit"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("person/edit"));
    }

    /**
     * Method under test: {@link PersonController#edit(Integer, Integer, String)}
     */
    @Test
    void testEdit4() throws Exception {
        Person person = new Person();
        person.setCity("Oxford");
        person.setEmailAddress("42 Main St");
        person.setEntityId(1);
        person.setFirstName("Jane");
        person.setLastName("Doe");
        person.setState("MD");
        person.setStreetAddress("42 Main St");
        person.setZipCode("21654");
        when(entityService.readEntity(Mockito.<Integer>any())).thenReturn(person);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/person/edit/{entityId}-{associatedEntityId}", 1, 1)
                .param("command", "foo");
        MockMvcBuilders.standaloneSetup(personController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.model().attributeExists("errors", "person"))
                .andExpect(MockMvcResultMatchers.view().name("person/edit"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("person/edit"));
    }

    /**
     * Method under test: {@link PersonController#edit(Integer, Integer, String)}
     */
    @Test
    void testEdit5() throws Exception {
        Person person = new Person();
        person.setCity("Oxford");
        person.setEmailAddress("42 Main St");
        person.setEntityId(1);
        person.setFirstName("Jane");
        person.setLastName("Doe");
        person.setState("MD");
        person.setStreetAddress("42 Main St");
        person.setZipCode("21654");
        doNothing().when(entityService).addAssociation(Mockito.<Integer>any(),
                                                       Mockito.<Integer>any());
        when(entityService.readEntity(Mockito.<Integer>any())).thenReturn(person);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/person/edit/{entityId}-{associatedEntityId}", 1, 1)
                .param("command", PersonController.ADD_CLIENT);
        MockMvcBuilders.standaloneSetup(personController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.model().attributeExists("errors", "person"))
                .andExpect(MockMvcResultMatchers.view().name("person/edit"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("person/edit"));
    }

    /**
     * Method under test: {@link PersonController#list()}
     */
    @Test
    void testList() throws Exception {
        when(entityService.listEntities()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/person/list");
        MockMvcBuilders.standaloneSetup(personController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("persons"))
                .andExpect(MockMvcResultMatchers.view().name("person/list"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("person/list"));
    }

    /**
     * Method under test: {@link PersonController#list()}
     */
    @Test
    void testList2() throws Exception {
        when(entityService.listEntities()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/person/list");
        requestBuilder.characterEncoding("Encoding");
        MockMvcBuilders.standaloneSetup(personController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("persons"))
                .andExpect(MockMvcResultMatchers.view().name("person/list"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("person/list"));
    }

    /**
     * Method under test: {@link PersonController#remove(Integer, Integer)}
     */
    @Test
    void testRemove() throws Exception {
        Client client = new Client();
        client.setCity("Oxford");
        client.setCompanyName("Company Name");
        client.setEntityId(1);
        client.setPhone("6625550144");
        client.setState("MD");
        client.setStreetAddress("42 Main St");
        client.setWebsite("Website");
        client.setZipCode("21654");

        Person person = new Person();
        person.setCity("Oxford");
        person.setEmailAddress("42 Main St");
        person.setEntityId(1);
        person.setFirstName("Jane");
        person.setLastName("Doe");
        person.setState("MD");
        person.setStreetAddress("42 Main St");
        person.setZipCode("21654");
        when(entityService.readAssociatedEntity(Mockito.<Integer>any())).thenReturn(client);
        when(entityService.readEntity(Mockito.<Integer>any())).thenReturn(person);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/person/remove/{entityId}-{associatedEntityId}", 1, 1);
        MockMvcBuilders.standaloneSetup(personController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.model().attributeExists("client", "person"))
                .andExpect(MockMvcResultMatchers.view().name("person/remove"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("person/remove"));
    }

    /**
     * Method under test: {@link PersonController#remove(Integer, Integer, String)}
     */
    @Test
    void testRemove2() throws Exception {
        MockHttpServletRequestBuilder paramResult = MockMvcRequestBuilders.post(
                        "/person/remove/{associatedEntityId}", 1)
                .param("command", "foo");
        MockHttpServletRequestBuilder requestBuilder = paramResult.param("entityId",
                                                                         String.valueOf(1));
        MockMvcBuilders.standaloneSetup(personController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/person/person-view/1"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/person/person-view/1"));
    }

    /**
     * Method under test: {@link PersonController#remove(Integer, Integer, String)}
     */
    @Test
    void testRemove3() throws Exception {
        doNothing().when(entityService).removeAssociation(Mockito.<Integer>any(),
                                                          Mockito.<Integer>any());
        MockHttpServletRequestBuilder paramResult = MockMvcRequestBuilders.post(
                        "/person/remove/{associatedEntityId}", 1)
                .param("command", PersonController.COMMAND_REMOVE);
        MockHttpServletRequestBuilder requestBuilder = paramResult.param("entityId",
                                                                         String.valueOf(1));
        MockMvcBuilders.standaloneSetup(personController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/person/person-view/1"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/person/person-view/1"));
    }

    /**
     * Method under test: {@link PersonController#seeAvailable(Integer)}
     */
    @Test
    void testSeeAvailable() throws Exception {
        Person person = new Person();
        person.setCity("Oxford");
        person.setEmailAddress("42 Main St");
        person.setEntityId(1);
        person.setFirstName("Jane");
        person.setLastName("Doe");
        person.setState("MD");
        person.setStreetAddress("42 Main St");
        person.setZipCode("21654");
        when(entityService.readEntity(Mockito.<Integer>any())).thenReturn(person);
        when(entityService.getAvailableAssociations(Mockito.<Integer>any())).thenReturn(
                new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/person/available-clients/{entityId}",
                1);
        MockMvcBuilders.standaloneSetup(personController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(3))
                .andExpect(MockMvcResultMatchers.model().attributeExists("clients", "person",
                                                                         "referrer"))
                .andExpect(MockMvcResultMatchers.view().name("person/available-clients"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("person/available-clients"));
    }

    /**
     * Method under test: {@link PersonController#viewPerson(Integer)}
     */
    @Test
    void testViewPerson() throws Exception {
        Person person = new Person();
        person.setCity("Oxford");
        person.setEmailAddress("42 Main St");
        person.setEntityId(1);
        person.setFirstName("Jane");
        person.setLastName("Doe");
        person.setState("MD");
        person.setStreetAddress("42 Main St");
        person.setZipCode("21654");
        when(entityService.readEntity(Mockito.<Integer>any())).thenReturn(person);
        when(entityService.getAssociations(Mockito.<Integer>any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/person/person-view/{entityId}", 1);
        MockMvcBuilders.standaloneSetup(personController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.model().attributeExists("clients", "person"))
                .andExpect(MockMvcResultMatchers.view().name("person/person-view"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("person/person-view"));
    }
}

