package com.aquent.crudapp.client;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A Client entity
 *
 * TODO: make an Entity interface for Client and Person to implement as to have the
 *       flexibility of adding future Entity types
 */
public class Client {

    /** The id of a client */
    private int clientId;

    /** The company name of a client */
    @NotNull
    @Size(min = 1, max = 100, message = "Required field company name : maximum length 100")
    private String companyName;

    /** The website of a client */
    @NotNull
    @Size(min = 1, max = 255, message = "Required field website : maximum length 255")
    private String website;

    /** The phone number of a client */
    @NotNull
    @Size(min = 10, max = 15, message = "Required field phone : maximum length 15")
    private String phone;

    /** The street address of a client */
    @NotNull
    @Size(min = 1, max = 50, message = "Required field street address : maximum length 50")
    private String streetAddress;

    /** The city of a client */
    @NotNull
    @Size(min = 1, max = 50, message = "Required field city : maximum length 50")
    private String city;

    /** The two letter state abbreviation of a client */
    @NotNull
    @Size(min = 2, max = 2, message = "Required field state abbreviation : maximum length 2")
    private String state;

    /** The zip code of a client */
    @NotNull
    @Size(min = 5, max = 5, message = "Required field zip code : maximum length 5")
    private String zipCode;

    /**
     * Get client's ID.
     *
     * @return The ID of this client
     */
    public Integer getClientId() {
        return clientId;
    }

    /**
     * Set client's ID
     *
     * @param clientId The ID to set for this client
     */
    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    /**
     * get client's company name.
     *
     * @return The company name for this client
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * Set client's company name.
     *
     * @param companyName The company name to set for this client
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * Get client's website.
     *
     * @return The website of this client
     */
    public String getWebsite() {
        return website;
    }

    /**
     * Set client's website
     *
     * @param website The website URL to set for this client
     */
    public void setWebsite(String website) {
        this.website = website;
    }

    /**
     * Get client's phone number.
     *
     * @return The phone number of this client
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Set client's phone number
     *
     * @param phone The phone number to set for this client
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Get client's street address.
     *
     * @return The street address of this client
     */
    public String getStreetAddress() {
        return streetAddress;
    }

    /**
     * Set client's street address.
     *
     * @param streetAddress The street address to set for this client
     */
    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    /**
     * Get client's city.
     *
     * @return The city portion of this client's address
     */
    public String getCity() {
        return city;
    }

    /**
     * Set client's city.
     *
     * @param city The city to set for this client
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Get client's state.
     *
     * @return The state portion of this client's address
     */
    public String getState() {
        return state;
    }

    /**
     * Set client's state.
     *
     * @param state The state to set for this client
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * Get client's zip code.
     *
     * @return The zip code of this client
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * Set client's zip code.
     *
     * @param zipCode The zip code to set for this client
     */
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

}
