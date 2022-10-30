package com.aquent.crudapp.client;

import com.aquent.crudapp.abstracts.EntityAbstract;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A Client entity
 *
 * TODO: make an Entity interface for Client and Person to implement as to have the
 *       flexibility of adding future Entity types
 */
public class Client extends EntityAbstract {

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

}
