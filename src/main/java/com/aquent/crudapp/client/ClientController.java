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

    /** Command string for delete */
    public static final String COMMAND_DELETE = "Delete";

    /** Command string for remove */
    public static final String COMMAND_REMOVE = "Remove";

    /** Command string for remove contact */
    public static final String REMOVE_CONTACT = "Remove Contact";

    /** Command string for add contact */
    public static final String ADD_CONTACT = "Add Contact";

    /** Command string for see/remove contacts */
    public static final String SEE_REMOVE = "See/Remove Contacts";

    /** A string to represent client edit as referrer when hitting available/current contacts */
    public static final String EDIT_REFERRER = "edit";

    /** A string to represent client view as referrer when hitting available/current contacts */

    public static final String VIEW_REFERRER = "view";

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

    /**
     * Render the view for an individual client.
     *
     * @param clientId The ID of the Client to be viewed
     * @return The Client view
     */
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
    public ModelAndView edit(@PathVariable Integer clientId, @PathVariable Integer personId,
                             @RequestParam String command) {
        if (ADD_CONTACT.equals(command)) {
            entityService.addAssociation(clientId, personId);
        } else if (REMOVE_CONTACT.equals(command)) {
            entityService.removeAssociation(clientId, personId);
        }
        ModelAndView mav = new ModelAndView("client/edit");
        mav.addObject("client", entityService.readEntity(clientId));
        mav.addObject("errors", new ArrayList<String>());
        return mav;
    }

    /**
     * Validates and saves an edited client.
     *
     * On success, the user is redirected to either:
     *      the available contacts view, the current contacts view, or the client listing page
     * depending on the value of the parameter "command" In any case any changed data is saved.
     *
     * On failure, the form is redisplayed with the validation errors.
     *
     * @param client populated form bean for the client
     * @param command "Add Contact" to render available contact view, "See/Remove Contacts" to
     *                see current contacts view, otherwise validate and redirect to client list
     *                view"
     * @return redirect, or edit view with errors
     */
    @PostMapping(value = "edit")
    public ModelAndView edit(Client client, @RequestParam String command) {
        List<String> errors = entityService.validateEntity(client);
        ModelAndView modelAndView;

        if (errors.isEmpty()) {
            entityService.updateEntity(client);
            if (ADD_CONTACT.equalsIgnoreCase(command)) {
                modelAndView = new ModelAndView("client/available-contacts");
                modelAndView.addObject("client", entityService.readEntity(client.getClientId()));
                modelAndView.addObject("contacts",
                                       entityService.getAvailableAssociations(
                                               client.getClientId()));
                modelAndView.addObject("referrer", EDIT_REFERRER);
            } else if (SEE_REMOVE.equalsIgnoreCase(command)) {
                modelAndView = new ModelAndView("client/current-contacts-editing");
                modelAndView.addObject("client", entityService.readEntity(client.getClientId()));
                modelAndView.addObject("contacts",
                                       entityService.getAssociations(client.getClientId()));
                modelAndView.addObject("referrer", EDIT_REFERRER);
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

    /**
     * Render a view for removing an associated Person contact.
     *
     * @param clientId The ID of the Client removing the contact
     * @param personId The ID of the Person contact to remove
     * @return The view for removing or cancelling
     */
    @GetMapping("remove/{clientId}-{personId}")
    public ModelAndView remove(@PathVariable Integer clientId, @PathVariable Integer personId) {
        ModelAndView modelAndView = new ModelAndView("client/remove");
        modelAndView.addObject("client", entityService.readEntity(clientId));
        modelAndView.addObject("person", entityService.readAssociatedEntity(personId));
        return modelAndView;
    }

    /**
     * Remove an associated Person contact and redirect to a Client individual view.
     *
     * @param clientId The ID of the Client to redirect to the view of
     * @param personId The ID of the Person contact to remove an association with
     * @param command "Remove" if they should be removed, "Cancel" if remove was cancelled
     * @return A redirect
     */
    @PostMapping(value = "remove/{personId}")
    public String remove(@RequestParam Integer clientId, @PathVariable Integer personId,
                         @RequestParam String command) {
        if (COMMAND_REMOVE.equals(command)) {
            entityService.removeAssociation(clientId, personId);
        }
        return "redirect:/client/client-view/" + clientId;
    }

    /**
     * Render the view for all available Person's a Client is not associated with.
     *
     * @param clientId The ID of the Client to view available contacts for
     * @return The view of available Person contacts
     */
    @GetMapping(value = "available-contacts/{clientId}")
    public ModelAndView seeAvailable(@PathVariable Integer clientId) {
        ModelAndView modelAndView = new ModelAndView("client/available-contacts");
        modelAndView.addObject("client", entityService.readEntity(clientId));
        modelAndView.addObject("contacts",
                               entityService.getAvailableAssociations(clientId));
        modelAndView.addObject("referrer", VIEW_REFERRER);
        return modelAndView;
    }

    /**
     * Add an association to the Client and redirect to the individual Client view.
     *
     * @param clientId The ID of the Client
     * @param personId The ID of the person to add as a contact
     * @return A redirect
     */
    @PostMapping(value = "available-contacts/{personId}")
    public String addAvailable(@RequestParam Integer clientId, @PathVariable Integer personId,
                               @RequestParam String referrer) {
        entityService.addAssociation(clientId, personId);
        return referrer.equalsIgnoreCase(EDIT_REFERRER) ?
               "redirect:/client/edit/" + clientId :
               "redirect:/client/client-view/" + clientId;
    }

    /**
     * Redirect to the client individual view
     *
     * @param clientId The ID of the client
     * @return A redirect
     */
    @PostMapping(value = "client-view/{clientId}")
    public String back(@RequestParam Integer clientId) {
        return "redirect:/client/client-view/" + clientId;
    }

}
