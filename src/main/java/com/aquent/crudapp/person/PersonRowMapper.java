package com.aquent.crudapp.person;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Row mapper for person records.
 */
public class PersonRowMapper implements RowMapper<Person> {

    @Override
    public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
        Person person = new Person();
        person.setPersonId(rs.getInt("person_id"));
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
