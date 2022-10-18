package com.aquent.crudapp.client;

import com.aquent.crudapp.interfaces.EntityDao;
import com.aquent.crudapp.person.Person;
import com.aquent.crudapp.person.PersonRowMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

@Repository
@Qualifier("clientDAO")
public class jdbcClientDAO implements EntityDao<Client, Person> {

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

    private static final String READ_PERSON = "SELECT * FROM person WHERE person_id = :personId";
    private static final String DELETE_CLIENT = "DELETE FROM client WHERE client_id = :clientId";

    private static final String REMOVE_ASSOCIATION = "DELETE FROM client_person_associations " +
                                                     "WHERE client_id = :clientId " +
                                                     "AND person_id = :personId";

    private static final String GET_CONTACTS = "SELECT  p.person_id, " +
                                                       "first_name, " +
                                                       "last_name, " +
                                                       "email_address, " +
                                                       "p.street_address, " +
                                                       "p.city, " +
                                                       "p.state, " +
                                                       "p.zip_code " +
                                               "FROM client c JOIN client_person_associations cpa" +
                                               " ON c.client_id = cpa.client_id " +
                                               "JOIN person p " +
                                               "ON cpa.person_id = p.person_id " +
                                               "WHERE cpa.client_id = :clientId";

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

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Person> getAssociations(Integer clientId) {
        MapSqlParameterSource paramMapper = new MapSqlParameterSource();
        paramMapper.addValue("clientId", clientId);
        return namedParameterJdbcTemplate.query(GET_CONTACTS, paramMapper, new PersonRowMapper());


        /* This one says something is wrong with the SQL, implies param-arg not set for :clientId */
//        return namedParameterJdbcTemplate.getJdbcOperations().query(GET_CONTACTS,
//                                                                    new PersonRowMapper(),
//                                                                    clientId);


        /* This one says something is wrong with the SQL, implies param-arg not set for :clientId */
//        return namedParameterJdbcTemplate.getJdbcOperations().query(GET_CONTACTS,
//                                                                    new ArgumentPreparedStatementSetter(new Integer[] {clientId}),
//                                                                    new PersonRowMapper());
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

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Person readAssociatedEntity(Integer personId) {
        return namedParameterJdbcTemplate.queryForObject(READ_PERSON, Collections.singletonMap(
                "personId", personId), new PersonRowMapper());
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public void removeAssociation(Integer clientId, Integer personId) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("clientId", clientId);
        mapSqlParameterSource.addValue("personId", personId);
        namedParameterJdbcTemplate.update(REMOVE_ASSOCIATION, mapSqlParameterSource);
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

}
