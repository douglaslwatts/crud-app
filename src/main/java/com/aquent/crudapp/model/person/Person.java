package com.aquent.crudapp.model.person;

import com.aquent.crudapp.model.AbstractEntityWithAddress;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * The person entity corresponding to the "person" table in the database.
 *
 * TODO: make an Entity interface for Client and Person to implement as to have the
 *       flexibility of adding future Entity types
 */
public class Person extends AbstractEntityWithAddress {

    @NotNull
    @Size(min = 1, max = 50, message = "First name is required with maximum length of 50")
    private String firstName;

    @NotNull
    @Size(min = 1, max = 50, message = "Last name is required with maximum length of 50")
    private String lastName;

    @NotNull
    @Size(min = 1, max = 50, message = "Email address is required with maximum length of 50")
    private String emailAddress;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

}
