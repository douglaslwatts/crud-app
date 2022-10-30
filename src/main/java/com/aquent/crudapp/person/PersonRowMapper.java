package com.aquent.crudapp.person;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A RowMapper for a Person object
 * {@See} https://docs.oracle.com/javase/8/docs/api/javax/swing/tree/RowMapper.html
 */
public class PersonRowMapper implements RowMapper<Person> {

    @Override
    public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
        Person person = new Person();
        person.setEntityId(rs.getInt("entity_id"));
        person.setFirstName(rs.getString("first_name"));
        person.setLastName(rs.getString("last_name"));
        person.setEmailAddress(rs.getString("email_address"));
        person.setStreetAddress(rs.getString("street_address"));
        person.setCity(rs.getString("city"));
        person.setState(rs.getString("state"));
        person.setZipCode(rs.getString("zip_code"));
        return person;
    }
}
