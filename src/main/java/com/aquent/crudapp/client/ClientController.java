package com.aquent.crudapp.client;

import com.aquent.crudapp.interfaces.EntityService;
import com.aquent.crudapp.person.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for handling client management operations.
 */
@Controller
@RequestMapping("client")
public class ClientController {

    public static final String COMMAND_DELETE = "Delete";
    public static final String COMMAND_REMOVE = "Remove";

    @Autowired
    @Qualifier("clientService")
    private final EntityService<Client, Person> entityService;

    /**
     * Instantiates a ClientController
     *
     * @param entityService The EntityService<Client> for this controller
     */
    public ClientController(EntityService<Client, Person> entityService) {
        this.entityService = entityService;
    }

    /**
     * Render an empty form for creating a new client.
     *
     * @return view with empty form for creating a new client
     */
    @GetMapping(value = "create")
    public ModelAndView create() {
        ModelAndView modelAndView = new ModelAndView("client/create");
        modelAndView.addObject("client", new Client());
        modelAndView.addObject("errors", new ArrayList<String>());
        return modelAndView;
    }

    /**
     * Validates and saves a new client.
     * On success, the user is redirected to the client listing page.
     * On failure, the form is redisplayed with the validation errors.
     *
     * @param client populated form bean for the client
     * @return redirect, or view with errors
     */
    @PostMapping(value = "create")
    public ModelAndView create(Client client) {
        List<String> errors = entityService.validateEntity(client);
        ModelAndView modelAndView;
        if (errors.isEmpty()) {
            entityService.createEntity(client);
            modelAndView = new ModelAndView("redirect:/client/list");
        } else {
            modelAndView = new ModelAndView("client/create");
            modelAndView.addObject("client", client);
            modelAndView.addObject("errors", errors);
        }

        return modelAndView;
    }

    /**
     * Renders the client listing page.
     *
     * @return list view populated with the current list of clients
     */
    @GetMapping(value = "list")
    public ModelAndView list() {
        ModelAndView modelAndView = new ModelAndView("client/list");
        modelAndView.addObject("clients", entityService.listEntities());
        return modelAndView;
    }

    @GetMapping(value = "client-view/{clientId}")
    public ModelAndView viewClient(@PathVariable Integer clientId) {
        ModelAndView modelAndView = new ModelAndView("client/client-view");
        modelAndView.addObject("client", entityService.readEntity(clientId));
        modelAndView.addObject("contacts", entityService.getAssociations(clientId));
        return modelAndView;
    }

    /**
     * Renders an edit form for an existing client record.
     *
     * @param clientId the ID of the client to edit
     * @return edit view populated from the client record
     */
    @GetMapping(value = "edit/{clientId}")
    public ModelAndView edit(@PathVariable Integer clientId) {
        ModelAndView modelAndView = new ModelAndView("client/edit");
        modelAndView.addObject("client", entityService.readEntity(clientId));
        modelAndView.addObject("errors", new ArrayList<String>());
        return modelAndView;
    }

    /**
     * Renders an edit form for an existing person record.
     *
     * @param personId the ID of the person to edit
     * @return edit view populated from the person record
     */
    @PostMapping(value = "edit/{clientId}-{personId}")
    public ModelAndView edit(@PathVariable Integer clientId, @PathVariable Integer personId) {
        ModelAndView mav = new ModelAndView("client/edit");
        mav.addObject("client", entityService.readEntity(clientId));
        mav.addObject("errors", new ArrayList<String>());
        entityService.addAssociation(clientId, personId);
        return mav;
    }

    /**
     * Validates and saves an edited client.
     * On success, the user is redirected to the client listing page.
     * On failure, the form is redisplayed with the validation errors.
     *
     * @param client populated form bean for the client
     * @return redirect, or edit view with errors
     */
    @PostMapping(value = "edit")
    public ModelAndView edit(Client client, @RequestParam String command) {
        List<String> errors = entityService.validateEntity(client);
        ModelAndView modelAndView;

        if (errors.isEmpty()) {
            entityService.updateEntity(client);
            if ("Add Contact".equals(command)) {
                modelAndView = new ModelAndView("client/available-contacts-editing");
                modelAndView.addObject("client", entityService.readEntity(client.getClientId()));
                modelAndView.addObject("contacts",
                                       entityService.getAvailableAssociations(client.getClientId()));
            } else {
                modelAndView = new ModelAndView("redirect:/client/list");
            }
        } else {
            modelAndView = new ModelAndView("client/edit");
            modelAndView.addObject("client", client);
            modelAndView.addObject("errors", errors);
        }

        return modelAndView;
    }

    /**
     * Renders the deletion confirmation page.
     *
     * @param clientId the ID of the client to be deleted
     * @return delete view populated from the client record
     */
    @GetMapping(value = "delete/{clientId}")
    public ModelAndView delete(@PathVariable Integer clientId) {
        ModelAndView modelAndView = new ModelAndView("client/delete");
        modelAndView.addObject("client", entityService.readEntity(clientId));
        return modelAndView;
    }

    /**
     * Handles client deletion or cancellation, redirecting to the client listing page in either
     * case.
     *
     * @param command the command field from the form
     * @param clientId the ID of the client to be deleted
     * @return redirect to the client listing page
     */
   @PostMapping(value = "delete")
   public String delete(@RequestParam String command, @RequestParam Integer clientId) {
        if (COMMAND_DELETE.equals(command)) {
            entityService.deleteEntity(clientId);
        }

        return "redirect:/client/list";
   }

    @GetMapping("remove/{clientId}-{personId}")
    public ModelAndView remove(@PathVariable Integer clientId, @PathVariable Integer personId) {
        ModelAndView modelAndView = new ModelAndView("client/remove");
        modelAndView.addObject("client", entityService.readEntity(clientId));
        modelAndView.addObject("person", entityService.readAssociatedEntity(personId));
        return modelAndView;
    }

    @PostMapping(value = "remove/{personId}")
    public String remove(@RequestParam Integer clientId, @PathVariable Integer personId,
                         @RequestParam String command) {
        System.out.println("hit it");
        if (COMMAND_REMOVE.equals(command)) {
            entityService.removeAssociation(clientId, personId);
        }
        return "redirect:/client/client-view/" + clientId;
    }

    @GetMapping(value = "available-contacts/{clientId}")
    public ModelAndView seeAvailable(@PathVariable Integer clientId) {
        ModelAndView modelAndView = new ModelAndView("client/available-contacts");
        modelAndView.addObject("client", entityService.readEntity(clientId));
        modelAndView.addObject("contacts",
                               entityService.getAvailableAssociations(clientId));
        return modelAndView;
    }

    @PostMapping(value = "available-contacts/{personId}")
    public String addAvailable(@RequestParam Integer clientId, @PathVariable Integer personId) {
        entityService.addAssociation(clientId, personId);
        return "redirect:/client/client-view/" + clientId;
    }

    @PostMapping(value = "client-view/{clientId}")
    public String back(@RequestParam Integer clientId) {
        return "redirect:/client/client-view/" + clientId;
    }

}
