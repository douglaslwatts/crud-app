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

    /** SQL for retrieving all client tuples */
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

    /** SQL for retrieving all person tuples which are not contacts of a given client via client ID */
    private static final String LIST_AVAILABLE_CONTACTS = "SELECT  person_id, " +
                                                                  "first_name, " +
                                                                  "last_name, " +
                                                                  "email_address, " +
                                                                  "street_address, " +
                                                                  "city, " +
                                                                  "state, " +
                                                                  "zip_code " +
                                                         "FROM person " +
                                                         "WHERE person_id NOT IN ( " +
                                                         "SELECT person_id " +
                                                         "FROM client_person_associations " +
                                                         "WHERE client_id = :clientId " +
                                                         ") " +
                                                         "ORDER BY first_name, last_name, person_id";

    /** SQL for creating a client tuple */
    private static final String CREATE_CLIENT = "INSERT INTO client ( " +
                                                    "company_name, " +
                                                    "website," +
                                                    "phone," +
                                                    "street_address," +
                                                    "city," +
                                                    "state," +
                                                    "zip_code" +
                                                ") VALUES ( " +
                                                    ":companyName, " +
                                                    ":website, " +
                                                    ":phone, " +
                                                    ":streetAddress, " +
                                                    ":city, " +
                                                    ":state, " +
                                                    ":zipCode" +
                                                ")";

    /** SQL for retrieving a given client tuple via client ID */
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

    /** SQL for updating a given client tuple via client ID */
    private static final String UPDATE_CLIENT = "UPDATE client " +
                                                "SET ( " +
                                                    "company_name, " +
                                                    "website," +
                                                    "phone," +
                                                    "street_address," +
                                                    "city," +
                                                    "state," +
                                                    "zip_code" +
                                                ") = ( " +
                                                    ":companyName, " +
                                                    ":website, " +
                                                    ":phone, " +
                                                    ":streetAddress, " +
                                                    ":city, " +
                                                    ":state, " +
                                                    ":zipCode" +
                                                ") " +
                                                "WHERE client_id = :entityId";

    /** SQL for retrieving a given person tuple via person ID */
    private static final String READ_PERSON = "SELECT * FROM person WHERE person_id = :personId";

    /** SQL for deleting a given client tuple via client ID */
    private static final String DELETE_CLIENT = "DELETE FROM client WHERE client_id = :clientId";

    /** SQL for removing a new client/person association via person ID and client ID */
    private static final String REMOVE_ASSOCIATION = "DELETE FROM client_person_associations " +
                                                     "WHERE client_id = :clientId " +
                                                     "AND person_id = :personId";

    /** SQL for inserting a new client/person association via person ID and client ID */
    private static final String ADD_ASSOCIATION = "INSERT INTO client_person_associations ( " +
                                                      "client_id, " +
                                                      "person_id " +
                                                  ") VALUES ( " +
                                                      ":clientId, " +
                                                      ":personId " +
                                                  ")";

    /** SQL for getting all associated person tuples via client ID */
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

    /**
     * Get a list of all entities associated with the entity via entity ID
     *
     * @param clientId The entity ID field of the entity
     * @return A list of all entities associated with the entity
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Person> getAssociations(Integer clientId) {
        MapSqlParameterSource paramMapper = new MapSqlParameterSource();
        paramMapper.addValue("clientId", clientId);
        return namedParameterJdbcTemplate.query(GET_CONTACTS, paramMapper, new PersonRowMapper());
    }

    /**
     * Get a list of all entities not associated with the entity.
     *
     * @param clientId The entity ID field of the entity
     * @return A list of all entities not associated with the entity
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Person> getAvailableAssociations(Integer clientId) {
        MapSqlParameterSource paramMapper = new MapSqlParameterSource();
        paramMapper.addValue("clientId", clientId);
        return namedParameterJdbcTemplate.query(LIST_AVAILABLE_CONTACTS, paramMapper,
                                                new PersonRowMapper());
    }

    /**
     * Add an association with a given entity.
     *
     * @param clientId The ID of the entity
     * @param personId The ID of the entity which should be associated
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public void addAssociation(Integer clientId, Integer personId) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("personId", personId);
        mapSqlParameterSource.addValue("clientId", clientId);
        namedParameterJdbcTemplate.update(ADD_ASSOCIATION, mapSqlParameterSource);
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
     * Retrieve an associated entity via ID
     *
     * @param personId The ID of the associated entity to retrieve
     * @return The associated entity
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Person readAssociatedEntity(Integer personId) {
        return namedParameterJdbcTemplate.queryForObject(READ_PERSON, Collections.singletonMap(
                "personId", personId), new PersonRowMapper());
    }

    /**
     * Remove an association with a given entity via ID
     *
     * @param clientId The ID of the entity
     * @param personId The ID of the associated entity, the association with which is to be
     *                      removed
     */
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
