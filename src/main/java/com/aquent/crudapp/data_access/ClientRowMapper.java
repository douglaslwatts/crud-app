package com.aquent.crudapp.data_access;

import com.aquent.crudapp.model.Client;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A RowMapper for a Client object
 * {@See} https://docs.oracle.com/javase/8/docs/api/javax/swing/tree/RowMapper.html
 */
public class ClientRowMapper implements RowMapper<Client> {

    @Override
    public Client mapRow(ResultSet rs, int rowNum) throws SQLException {
        Client client = new Client();
        client.setEntityId(rs.getInt("client_id"));
        client.setCompanyName(rs.getString("company_name"));
        client.setWebsite(rs.getString("website"));
        client.setPhone(rs.getString("phone"));
        client.setStreetAddress(rs.getString("street_address"));
        client.setCity(rs.getString("city"));
        client.setState(rs.getString("state"));
        client.setZipCode(rs.getString("zip_code"));
        return client;
    }

}
