package com.aquent.crudapp.interfaces;

public interface Entity {

    Integer getEntityId();

    String getStreetAddress();

    void setStreetAddress(String streetAddress);

    String getCity();

    void setCity(String city);

    String getState();

    void setState(String state);

    String getZipCode();

    void setZipCode(String zipCode);

}
