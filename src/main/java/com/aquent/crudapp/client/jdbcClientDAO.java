package com.aquent.crudapp.client;

import com.aquent.crudapp.interfaces.EntityDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@Repository
@Qualifier("clientDAO")
public class jdbcClientDAO implements EntityDao<Client> {

    private static final String LIST_CLIENTS = "SELECT  client_id, " +
                                               "company_name, " +
                                               "website, " +
                                               "phone, " +
                                               "street_address, " +
                                               "city, " +
                                               "state, " +
                                               "zip_code " +
                                               "FROM client " +
                                               "ORDER BY company_name, website";

    private static final String CREATE_CLIENT = "INSERT INTO client ( " +
                                                "company_name, " +
                                                "website," +
                                                "phone," +
                                                "street_address," +
                                                "city," +
                                                "state," +
                                                "zip_code" +
                                                ") " +
                                                "VALUES ( " +
                                                ":companyName, " +
                                                ":website, " +
                                                ":phone, " +
                                                ":streetAddress, " +
                                                ":city, " +
                                                ":state, " +
                                                ":zipCode" +
                                                ")";

    private static final String READ_CLIENT = "SELECT  client_id, " +
                                              "company_name, " +
                                              "website, " +
                                              "phone, " +
                                              "street_address, " +
                                              "city, " +
                                              "state, " +
                                              "zip_code " +
                                              "FROM client " +
                                              "WHERE client_id = :clientId";

    private static final String UPDATE_CLIENT = "UPDATE client " +
                                                "SET ( " +
                                                "company_name, " +
                                                "website," +
                                                "phone," +
                                                "street_address," +
                                                "city," +
                                                "state," +
                                                "zip_code" +
                                                ") " +
                                                "= ( " +
                                                ":companyName, " +
                                                ":website, " +
                                                ":phone, " +
                                                ":streetAddress, " +
                                                ":city, " +
                                                ":state, " +
                                                ":zipCode" +
                                                ") " +
                                                "WHERE client_id = :clientId";

    private static final String DELETE_CLIENT = "DELETE FROM client WHERE client_id = :clientId";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public jdbcClientDAO(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    /**
     * Retrieves all client records.
     *
     * @return list of client records
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Client> listEntities() {
        return namedParameterJdbcTemplate.getJdbcOperations().query(LIST_CLIENTS,
                                                                    new ClientRowMapper());
    }

    /**
     * Creates a new client record.
     *
     * @param client the values to save
     *
     * @return the new client ID
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Integer createEntity(Client client) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(CREATE_CLIENT,
                                          new BeanPropertySqlParameterSource(client), keyHolder);
        return keyHolder.getKey().intValue();
    }

    /**
     * Retrieves a client record by ID.
     *
     * @param clientId the client ID
     *
     * @return the client record
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Client readEntity(Integer clientId) {
        return namedParameterJdbcTemplate.queryForObject(READ_CLIENT, Collections.singletonMap(
                "clientId", clientId), new ClientRowMapper());
    }

    /**
     * Updates an existing client record.
     *
     * @param client the new values to save
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public void updateEntity(Client client) {
        namedParameterJdbcTemplate.update(UPDATE_CLIENT,
                                          new BeanPropertySqlParameterSource(client));
    }

    /**
     * Deletes a client record by ID.
     *
     * @param clientId the client ID
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public void deleteEntity(Integer clientId) {
        namedParameterJdbcTemplate.update(DELETE_CLIENT, Collections.singletonMap("clientId",
                                                                                  clientId));
    }

    private static final class ClientRowMapper implements RowMapper<Client> {

        @Override
        public Client mapRow(ResultSet rs, int rowNum) throws SQLException {
            Client client = new Client();
            client.setClientId(rs.getInt("client_id"));
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

}
