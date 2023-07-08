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

@ContextConfiguration(classes = { ClientController.class })
@ExtendWith(SpringExtension.class)
class ClientControllerTest {

    @Autowired
    private ClientController clientController;

    @MockBean(name = "clientService")
    private EntityService<Client, Person> entityService;

    @MockBean
    private EntityService<Client, Person> entityService2;

    /**
     * Method under test: {@link ClientController#addAvailable(Integer, Integer, String)}
     */
    @Test
    void testAddAvailable() throws Exception {
        doNothing().when(entityService).addAssociation(Mockito.<Integer>any(),
                                                       Mockito.<Integer>any());
        MockHttpServletRequestBuilder postResult = MockMvcRequestBuilders
                .post("/client/available-contacts/{associatedEntityId}", 1);
        MockHttpServletRequestBuilder requestBuilder = postResult.param("entityId",
                                                                        String.valueOf(1))
                .param("referrer", "foo");
        MockMvcBuilders.standaloneSetup(clientController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/client/client-view/1"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/client/client-view/1"));
    }

    /**
     * Method under test: {@link ClientController#addAvailable(Integer, Integer, String)}
     */
    @Test
    void testAddAvailable2() throws Exception {
        doNothing().when(entityService).addAssociation(Mockito.<Integer>any(),
                                                       Mockito.<Integer>any());
        MockHttpServletRequestBuilder postResult = MockMvcRequestBuilders
                .post("/client/available-contacts/{associatedEntityId}", 1);
        MockHttpServletRequestBuilder requestBuilder = postResult.param("entityId",
                                                                        String.valueOf(1))
                .param("referrer", ClientController.EDIT_REFERRER);
        MockMvcBuilders.standaloneSetup(clientController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/client/edit/1"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/client/edit/1"));
    }

    /**
     * Method under test: {@link ClientController#back(Integer)}
     */
    @Test
    void testBack() throws Exception {
        MockHttpServletRequestBuilder postResult = MockMvcRequestBuilders.post(
                "/client/client-view/{entityId}", 1);
        MockHttpServletRequestBuilder requestBuilder = postResult.param("entityId",
                                                                        String.valueOf(1));
        MockMvcBuilders.standaloneSetup(clientController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/client/client-view/1"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/client/client-view/1"));
    }

    /**
     * Method under test: {@link ClientController#create()}
     */
    @Test
    void testCreate() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/client/create");
        MockMvcBuilders.standaloneSetup(clientController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.model().attributeExists("client", "errors"))
                .andExpect(MockMvcResultMatchers.view().name("client/create"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("client/create"));
    }

    /**
     * Method under test: {@link ClientController#create()}
     */
    @Test
    void testCreate2() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/client/create");
        requestBuilder.characterEncoding("Encoding");
        MockMvcBuilders.standaloneSetup(clientController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.model().attributeExists("client", "errors"))
                .andExpect(MockMvcResultMatchers.view().name("client/create"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("client/create"));
    }

    /**
     * Method under test: {@link ClientController#create(Client)}
     */
    @Test
    void testCreate3() throws Exception {
        when(entityService.createEntity(Mockito.<Client>any())).thenReturn(1);
        when(entityService.validateEntity(Mockito.<Client>any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                "/client/create");
        MockMvcBuilders.standaloneSetup(clientController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("client"))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/client/list"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/client/list"));
    }

    /**
     * Method under test: {@link ClientController#create(Client)}
     */
    @Test
    void testCreate4() throws Exception {
        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("?");
        when(entityService.createEntity(Mockito.<Client>any())).thenReturn(1);
        when(entityService.validateEntity(Mockito.<Client>any())).thenReturn(stringList);
        when(entityService2.createEntity(Mockito.<Client>any())).thenReturn(1);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                "/client/create");
        MockMvcBuilders.standaloneSetup(clientController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.model().attributeExists("client", "errors"))
                .andExpect(MockMvcResultMatchers.view().name("client/create"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("client/create"));
    }

    /**
     * Method under test: {@link ClientController#delete(Integer)}
     */
    @Test
    void testDelete() throws Exception {
        Client client = new Client();
        client.setCity("Oxford");
        client.setCompanyName("Company Name");
        client.setEntityId(1);
        client.setPhone("6625550144");
        client.setState("MD");
        client.setStreetAddress("42 Main St");
        client.setWebsite("Website");
        client.setZipCode("21654");
        when(entityService.readEntity(Mockito.<Integer>any())).thenReturn(client);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/client/delete/{entityId}", 1);
        MockMvcBuilders.standaloneSetup(clientController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("client"))
                .andExpect(MockMvcResultMatchers.view().name("client/delete"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("client/delete"));
    }

    /**
     * Method under test: {@link ClientController#delete(String, Integer)}
     */
    @Test
    void testDelete2() throws Exception {
        MockHttpServletRequestBuilder paramResult = MockMvcRequestBuilders.post(
                "/client/delete").param("command", "foo");
        MockHttpServletRequestBuilder requestBuilder = paramResult.param("entityId",
                                                                         String.valueOf(1));
        MockMvcBuilders.standaloneSetup(clientController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/client/list"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/client/list"));
    }

    /**
     * Method under test: {@link ClientController#delete(String, Integer)}
     */
    @Test
    void testDelete3() throws Exception {
        doNothing().when(entityService).deleteEntity(Mockito.<Integer>any());
        MockHttpServletRequestBuilder paramResult = MockMvcRequestBuilders.post("/client/delete")
                .param("command", ClientController.COMMAND_DELETE);
        MockHttpServletRequestBuilder requestBuilder = paramResult.param("entityId",
                                                                         String.valueOf(1));
        MockMvcBuilders.standaloneSetup(clientController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/client/list"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/client/list"));
    }

    /**
     * Method under test: {@link ClientController#edit(Client, String)}
     */
    @Test
    void testEdit() throws Exception {
        when(entityService.validateEntity(Mockito.<Client>any())).thenReturn(new ArrayList<>());
        doNothing().when(entityService).updateEntity(Mockito.<Client>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/client/edit")
                .param("command", "foo");
        MockMvcBuilders.standaloneSetup(clientController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("client"))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/client/list"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/client/list"));
    }

    /**
     * Method under test: {@link ClientController#edit(Client, String)}
     */
    @Test
    void testEdit2() throws Exception {
        ArrayList<String> stringList = new ArrayList<>();
        stringList.add("?");
        when(entityService.validateEntity(Mockito.<Client>any())).thenReturn(stringList);
        doNothing().when(entityService).updateEntity(Mockito.<Client>any());
        doNothing().when(entityService2).updateEntity(Mockito.<Client>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/client/edit")
                .param("command", "foo");
        MockMvcBuilders.standaloneSetup(clientController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.model().attributeExists("client", "errors"))
                .andExpect(MockMvcResultMatchers.view().name("client/edit"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("client/edit"));
    }

    /**
     * Method under test: {@link ClientController#edit(Integer)}
     */
    @Test
    void testEdit3() throws Exception {
        Client client = new Client();
        client.setCity("Oxford");
        client.setCompanyName("Company Name");
        client.setEntityId(1);
        client.setPhone("6625550144");
        client.setState("MD");
        client.setStreetAddress("42 Main St");
        client.setWebsite("Website");
        client.setZipCode("21654");
        when(entityService.readEntity(Mockito.<Integer>any())).thenReturn(client);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/client/edit/{entityId}", 1);
        MockMvcBuilders.standaloneSetup(clientController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.model().attributeExists("client", "errors"))
                .andExpect(MockMvcResultMatchers.view().name("client/edit"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("client/edit"));
    }

    /**
     * Method under test: {@link ClientController#edit(Integer, Integer, String)}
     */
    @Test
    void testEdit4() throws Exception {
        Client client = new Client();
        client.setCity("Oxford");
        client.setCompanyName("Company Name");
        client.setEntityId(1);
        client.setPhone("6625550144");
        client.setState("MD");
        client.setStreetAddress("42 Main St");
        client.setWebsite("Website");
        client.setZipCode("21654");
        when(entityService.readEntity(Mockito.<Integer>any())).thenReturn(client);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/client/edit/{entityId}-{associatedEntityId}", 1, 1)
                .param("command", "foo");
        MockMvcBuilders.standaloneSetup(clientController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.model().attributeExists("client", "errors"))
                .andExpect(MockMvcResultMatchers.view().name("client/edit"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("client/edit"));
    }

    /**
     * Method under test: {@link ClientController#edit(Integer, Integer, String)}
     */
    @Test
    void testEdit5() throws Exception {
        Client client = new Client();
        client.setCity("Oxford");
        client.setCompanyName("Company Name");
        client.setEntityId(1);
        client.setPhone("6625550144");
        client.setState("MD");
        client.setStreetAddress("42 Main St");
        client.setWebsite("Website");
        client.setZipCode("21654");
        doNothing().when(entityService).addAssociation(Mockito.<Integer>any(),
                                                       Mockito.<Integer>any());
        when(entityService.readEntity(Mockito.<Integer>any())).thenReturn(client);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/client/edit/{entityId}-{associatedEntityId}", 1, 1)
                .param("command", ClientController.ADD_CONTACT);
        MockMvcBuilders.standaloneSetup(clientController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.model().attributeExists("client", "errors"))
                .andExpect(MockMvcResultMatchers.view().name("client/edit"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("client/edit"));
    }

    /**
     * Method under test: {@link ClientController#list()}
     */
    @Test
    void testList() throws Exception {
        when(entityService.listEntities()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/client/list");
        MockMvcBuilders.standaloneSetup(clientController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("clients"))
                .andExpect(MockMvcResultMatchers.view().name("client/list"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("client/list"));
    }

    /**
     * Method under test: {@link ClientController#list()}
     */
    @Test
    void testList2() throws Exception {
        when(entityService.listEntities()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/client/list");
        requestBuilder.characterEncoding("Encoding");
        MockMvcBuilders.standaloneSetup(clientController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("clients"))
                .andExpect(MockMvcResultMatchers.view().name("client/list"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("client/list"));
    }

    /**
     * Method under test: {@link ClientController#remove(Integer, Integer)}
     */
    @Test
    void testRemove() throws Exception {
        Person person = new Person();
        person.setCity("Oxford");
        person.setEmailAddress("42 Main St");
        person.setEntityId(1);
        person.setFirstName("Jane");
        person.setLastName("Doe");
        person.setState("MD");
        person.setStreetAddress("42 Main St");
        person.setZipCode("21654");

        Client client = new Client();
        client.setCity("Oxford");
        client.setCompanyName("Company Name");
        client.setEntityId(1);
        client.setPhone("6625550144");
        client.setState("MD");
        client.setStreetAddress("42 Main St");
        client.setWebsite("Website");
        client.setZipCode("21654");
        when(entityService.readAssociatedEntity(Mockito.<Integer>any())).thenReturn(person);
        when(entityService.readEntity(Mockito.<Integer>any())).thenReturn(client);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/client/remove/{entityId}-{associatedEntityId}", 1, 1);
        MockMvcBuilders.standaloneSetup(clientController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.model().attributeExists("client", "person"))
                .andExpect(MockMvcResultMatchers.view().name("client/remove"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("client/remove"));
    }

    /**
     * Method under test: {@link ClientController#remove(Integer, Integer, String)}
     */
    @Test
    void testRemove2() throws Exception {
        MockHttpServletRequestBuilder paramResult = MockMvcRequestBuilders.post(
                        "/client/remove/{associatedEntityId}", 1)
                .param("command", "foo");
        MockHttpServletRequestBuilder requestBuilder = paramResult.param("entityId",
                                                                         String.valueOf(1));
        MockMvcBuilders.standaloneSetup(clientController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/client/client-view/1"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/client/client-view/1"));
    }

    /**
     * Method under test: {@link ClientController#remove(Integer, Integer, String)}
     */
    @Test
    void testRemove3() throws Exception {
        doNothing().when(entityService).removeAssociation(Mockito.<Integer>any(),
                                                          Mockito.<Integer>any());
        MockHttpServletRequestBuilder paramResult = MockMvcRequestBuilders.post(
                        "/client/remove/{associatedEntityId}", 1)
                .param("command", ClientController.COMMAND_REMOVE);
        MockHttpServletRequestBuilder requestBuilder = paramResult.param("entityId",
                                                                         String.valueOf(1));
        MockMvcBuilders.standaloneSetup(clientController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/client/client-view/1"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/client/client-view/1"));
    }

    /**
     * Method under test: {@link ClientController#seeAvailable(Integer)}
     */
    @Test
    void testSeeAvailable() throws Exception {
        Client client = new Client();
        client.setCity("Oxford");
        client.setCompanyName("Company Name");
        client.setEntityId(1);
        client.setPhone("6625550144");
        client.setState("MD");
        client.setStreetAddress("42 Main St");
        client.setWebsite("Website");
        client.setZipCode("21654");
        when(entityService.readEntity(Mockito.<Integer>any())).thenReturn(client);
        when(entityService.getAvailableAssociations(Mockito.<Integer>any())).thenReturn(
                new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/client/available-contacts/{entityId}",
                1);
        MockMvcBuilders.standaloneSetup(clientController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(3))
                .andExpect(MockMvcResultMatchers.model().attributeExists("client", "contacts",
                                                                         "referrer"))
                .andExpect(MockMvcResultMatchers.view().name("client/available-contacts"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("client/available-contacts"));
    }

    /**
     * Method under test: {@link ClientController#viewClient(Integer)}
     */
    @Test
    void testViewClient() throws Exception {
        Client client = new Client();
        client.setCity("Oxford");
        client.setCompanyName("Company Name");
        client.setEntityId(1);
        client.setPhone("6625550144");
        client.setState("MD");
        client.setStreetAddress("42 Main St");
        client.setWebsite("Website");
        client.setZipCode("21654");
        when(entityService.readEntity(Mockito.<Integer>any())).thenReturn(client);
        when(entityService.getAssociations(Mockito.<Integer>any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/client/client-view/{entityId}", 1);
        MockMvcBuilders.standaloneSetup(clientController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.model().attributeExists("client", "contacts"))
                .andExpect(MockMvcResultMatchers.view().name("client/client-view"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("client/client-view"));
    }
}

