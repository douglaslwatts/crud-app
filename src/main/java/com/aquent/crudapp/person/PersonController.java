package com.aquent.crudapp.person;

import java.util.ArrayList;
import java.util.List;

import com.aquent.crudapp.client.Client;
import com.aquent.crudapp.interfaces.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for handling basic person management operations.
 */
@Controller
@RequestMapping("person")
public class PersonController {

    public static final String COMMAND_DELETE = "Delete";
    public static final String COMMAND_REMOVE = "Remove";

    @Autowired
    @Qualifier("personService")
    private final EntityService<Person, Client> entityService;

    public PersonController(EntityService<Person, Client> entityService) {
        this.entityService = entityService;
    }

    /**
     * Renders the person listing page.
     *
     * @return list view populated with the current list of people
     */
    @GetMapping(value = "list")
    public ModelAndView list() {
        ModelAndView mav = new ModelAndView("person/list");
        mav.addObject("persons", entityService.listEntities());
        return mav;
    }

    @GetMapping(value = "person-view/{personId}")
    public ModelAndView viewPerson(@PathVariable Integer personId) {
        ModelAndView modelAndView = new ModelAndView("person/person-view");
        modelAndView.addObject("person", entityService.readEntity(personId));
        modelAndView.addObject("clients", entityService.getAssociations(personId));
        return modelAndView;
    }

    /**
     * Renders an empty form used to create a new person record.
     *
     * @return view populated with an empty person
     */
    @GetMapping(value = "create")
    public ModelAndView create() {
        ModelAndView mav = new ModelAndView("person/create");
        mav.addObject("person", new Person());
        mav.addObject("errors", new ArrayList<String>());
        return mav;
    }

    /**
     * Validates and saves a new person.
     * On success, the user is redirected to the listing page.
     * On failure, the form is redisplayed with the validation errors.
     *
     * @param person populated form bean for the person
     * @return redirect, or view with errors
     */
    @PostMapping(value = "create")
    public ModelAndView create(Person person) {
        List<String> errors = entityService.validateEntity(person);
        if (errors.isEmpty()) {
            entityService.createEntity(person);
            return new ModelAndView("redirect:/person/list");
        } else {
            ModelAndView mav = new ModelAndView("person/create");
            mav.addObject("person", person);
            mav.addObject("errors", errors);
            return mav;
        }
    }

    /**
     * Renders an edit form for an existing person record.
     *
     * @param personId the ID of the person to edit
     * @return edit view populated from the person record
     */
    @GetMapping(value = "edit/{personId}")
    public ModelAndView edit(@PathVariable Integer personId) {
        ModelAndView mav = new ModelAndView("person/edit");
        mav.addObject("person", entityService.readEntity(personId));
        mav.addObject("errors", new ArrayList<String>());
        return mav;
    }

    /**
     * Renders an edit form for an existing person record.
     *
     * @param personId the ID of the person to edit
     * @return edit view populated from the person record
     */
    @PostMapping(value = "edit/{personId}-{clientId}")
    public ModelAndView edit(@PathVariable Integer personId, @PathVariable Integer clientId) {
        ModelAndView mav = new ModelAndView("person/edit");
        mav.addObject("person", entityService.readEntity(personId));
        mav.addObject("errors", new ArrayList<String>());
        entityService.addAssociation(personId, clientId);
        return mav;
    }

    /**
     * Validates and saves an edited person.
     * On success, the user is redirected to the person listing page.
     * On failure, the form is redisplayed with the validation errors.
     *
     * @param person populated form bean for the person
     * @return redirect, or edit view with errors
     */
    @PostMapping(value = "edit")
    public ModelAndView edit(Person person, @RequestParam String command) {
        List<String> errors = entityService.validateEntity(person);
        if (errors.isEmpty()) {
            entityService.updateEntity(person);
            if ("Add Client".equals(command)) {
                ModelAndView modelAndView = new ModelAndView("person/available-clients-editing");
                modelAndView.addObject("person", entityService.readEntity(person.getPersonId()));
                modelAndView.addObject("clients",
                                       entityService.getAvailableAssociations(person.getPersonId()));
                return modelAndView;
            } else {
                return new ModelAndView("redirect:/person/list");
            }
        } else {
            ModelAndView mav = new ModelAndView("person/edit");
            mav.addObject("person", person);
            mav.addObject("errors", errors);
            return mav;
        }
    }

    /**
     * Renders the deletion confirmation page.
     *
     * @param personId the ID of the person to be deleted
     * @return delete view populated from the person record
     */
    @GetMapping(value = "delete/{personId}")
    public ModelAndView delete(@PathVariable Integer personId) {
        ModelAndView mav = new ModelAndView("person/delete");
        mav.addObject("person", entityService.readEntity(personId));
        return mav;
    }

    /**
     * Handles person deletion or cancellation, redirecting to the person listing page in either
     * case.
     *
     * @param command the command field from the form
     * @param personId the ID of the person to be deleted
     * @return redirect to the person listing page
     */
    @PostMapping(value = "delete")
    public String delete(@RequestParam String command, @RequestParam Integer personId) {
        if (COMMAND_DELETE.equals(command)) {
            entityService.deleteEntity(personId);
        }
        return "redirect:/person/list";
    }

    @GetMapping("remove/{personId}-{clientId}")
    public ModelAndView remove(@PathVariable Integer personId, @PathVariable Integer clientId) {
        ModelAndView modelAndView = new ModelAndView("person/remove");
        modelAndView.addObject("person", entityService.readEntity(personId));
        modelAndView.addObject("client", entityService.readAssociatedEntity(clientId));
        return modelAndView;
    }

    @PostMapping(value = "remove/{clientId}")
    public String remove(@RequestParam Integer personId, @PathVariable Integer clientId,
                         @RequestParam String command) {
        if (COMMAND_REMOVE.equals(command)) {
            entityService.removeAssociation(personId, clientId);
        }
        return "redirect:/person/person-view/" + personId;
    }

    @GetMapping(value = "available-clients/{personId}")
    public ModelAndView seeAvailable(@PathVariable Integer personId) {
        System.out.println("correct one");
        ModelAndView modelAndView = new ModelAndView("person/available-clients");
        modelAndView.addObject("person", entityService.readEntity(personId));
        modelAndView.addObject("clients",
                               entityService.getAvailableAssociations(personId));
        return modelAndView;
    }

    @PostMapping(value = "available-clients/{clientId}")
    public String addAvailable(@RequestParam Integer personId, @PathVariable Integer clientId) {
        System.out.println("incorrect one");
        entityService.addAssociation(personId, clientId);
        return "redirect:/person/person-view/" + personId;
    }

    @PostMapping(value = "person-view/{personId}")
    public String back(@RequestParam Integer personId) {
        return "redirect:/person/person-view/" + personId;
    }

}
