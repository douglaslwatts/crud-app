package com.aquent.crudapp.data_access;

import java.util.Collections;
import java.util.List;

import com.aquent.crudapp.model.client.Client;
import com.aquent.crudapp.model.client.ClientRowMapper;
import com.aquent.crudapp.interfaces.EntityDao;
import com.aquent.crudapp.model.person.Person;
import com.aquent.crudapp.model.person.PersonRowMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Spring JDBC implementation of {@link EntityDao}.
 */
@Repository
@Qualifier("personDAO")
public class JdbcPersonDAO implements EntityDao<Person, Client> {

    /** SQL for retrieving all person tuples */
    private static final String SQL_LIST_PEOPLE = "SELECT * FROM person ORDER BY first_name, " +
                                                  "last_name, person_id";

    /** SQL for retrieving all client tuples which are not contacts of a given person via person ID */
    private static final String LIST_AVAILABLE_CLIENTS = "SELECT  client_id, " +
                                                                 "company_name, " +
                                                                 "website, " +
                                                                 "phone, " +
                                                                 "street_address, " +
                                                                 "city, " +
                                                                 "state, " +
                                                                 "zip_code " +
                                                         "FROM client " +
                                                         "WHERE client_id NOT IN ( " +
                                                            "SELECT client_id " +
                                                            "FROM client_person_associations " +
                                                            "WHERE person_id = :personId " +
                                                         ") " +
                                                         "ORDER BY company_name, website";

    /** SQL for retrieving a given person tuple via person ID */
    private static final String SQL_READ_PERSON = "SELECT * FROM person WHERE person_id = :personId";

    /** SQL for retrieving a given client tuple via client ID */
    private static final String READ_CLIENT = "SELECT * FROM client WHERE client_id = :clientId";

    /** SQL for deleting a given person tuple via person ID */
    private static final String SQL_DELETE_PERSON = "DELETE FROM person WHERE person_id = :personId";

    /** SQL for removing a new client/person association via person ID and client ID */
    private static final String REMOVE_ASSOCIATION = "DELETE FROM client_person_associations " +
                                                     "WHERE person_id = :personId " +
                                                     "AND client_id = :clientId";

    /** SQL for inserting a new client/person association via person ID and client ID */
    private static final String ADD_ASSOCIATION = "INSERT INTO client_person_associations ( " +
                                                      "client_id, " +
                                                      "person_id " +
                                                  ") VALUES ( " +
                                                      ":clientId, " +
                                                      ":personId " +
                                                  ")";
    /** SQL for updating a given person tuple via person ID */
    private static final String SQL_UPDATE_PERSON = "UPDATE person SET (first_name, last_name, email_address, street_address, city, state, zip_code)"
                                                  + " = (:firstName, :lastName, :emailAddress, :streetAddress, :city, :state, :zipCode)"
                                                  + " WHERE person_id = :entityId";
    /** SQL for creating a person tuple */
    private static final String SQL_CREATE_PERSON = "INSERT INTO person (first_name, last_name, email_address, street_address, city, state, zip_code)"
                                                  + " VALUES (:firstName, :lastName, :emailAddress, :streetAddress, :city, :state, :zipCode)";

    /** SQL for getting all associated client tuples via person ID */
    private static final String GET_CLIENTS = "SELECT  c.client_id, " +
                                                      "company_name, " +
                                                      "website, " +
                                                      "phone, " +
                                                      "c.street_address, " +
                                                      "c.city, " +
                                                      "c.state, " +
                                                      "c.zip_code " +
                                               "FROM person p JOIN client_person_associations cpa" +
                                               " ON p.person_id = cpa.person_id " +
                                               "JOIN client c " +
                                               "ON cpa.client_id = c.client_id " +
                                               "WHERE cpa.person_id = :personId";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public JdbcPersonDAO(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    /**
     * Retrieves all person records.
     *
     * @return list of person records
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Person> listEntities() {
        return namedParameterJdbcTemplate.getJdbcOperations().query(SQL_LIST_PEOPLE, new PersonRowMapper());
    }

    /**
     * Get a list of all entities associated with the entity via entity ID
     *
     * @param personId The entity ID field of the entity
     * @return A list of all entities associated with the entity
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Client> getAssociations(Integer personId) {
        MapSqlParameterSource paramMapper = new MapSqlParameterSource();
        paramMapper.addValue("personId", personId);
        return namedParameterJdbcTemplate.query(GET_CLIENTS, paramMapper, new ClientRowMapper());
    }

    /**
     * Get a list of all entities not associated with the entity.
     *
     * @param personId The entity ID field of the entity
     * @return A list of all entities not associated with the entity
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Client> getAvailableAssociations(Integer personId) {
        MapSqlParameterSource paramMapper = new MapSqlParameterSource();
        paramMapper.addValue("personId", personId);
        return namedParameterJdbcTemplate.query(LIST_AVAILABLE_CLIENTS, paramMapper,
                                                new ClientRowMapper());
    }

    /**
     * Add an association with a given entity.
     *
     * @param personId The ID of the entity which should be associated
     * @param clientId The ID of the entity
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public void addAssociation(Integer personId, Integer clientId) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("personId", personId);
        mapSqlParameterSource.addValue("clientId", clientId);
        namedParameterJdbcTemplate.update(ADD_ASSOCIATION, mapSqlParameterSource);
    }

    /**
     * Retrieves a person record by ID.
     *
     * @param personId the client ID
     *
     * @return the client record
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Person readEntity(Integer personId) {
        return namedParameterJdbcTemplate.queryForObject(SQL_READ_PERSON, Collections.singletonMap("personId", personId), new PersonRowMapper());
    }

    /**
     * Retrieve an associated entity via ID
     *
     * @param clientId The ID of the associated entity to retrieve
     * @return The associated entity
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Client readAssociatedEntity(Integer clientId) {
        return namedParameterJdbcTemplate.queryForObject(READ_CLIENT, Collections.singletonMap(
                "clientId", clientId), new ClientRowMapper());
    }

    /**
     * Remove an association with a given entity via ID
     *
     * @param personId The ID of the associated entity, the association with which is to be
     *                      removed
     * @param clientId The ID of the entity
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public void removeAssociation(Integer personId, Integer clientId) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("personId", personId);
        mapSqlParameterSource.addValue("clientId", clientId);
        namedParameterJdbcTemplate.update(REMOVE_ASSOCIATION, mapSqlParameterSource);
    }

    /**
     * Deletes a person record by ID.
     *
     * @param personId the client ID
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public void deleteEntity(Integer personId) {
        namedParameterJdbcTemplate.update(SQL_DELETE_PERSON, Collections.singletonMap("personId", personId));
    }

    /**
     * Updates an existing person record.
     *
     * @param person the new values to save
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public void updateEntity(Person person) {
        namedParameterJdbcTemplate.update(SQL_UPDATE_PERSON, new BeanPropertySqlParameterSource(person));
    }

    /**
     * Creates a new person record.
     *
     * @param person the values to save
     *
     * @return the new client ID
     */
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public Integer createEntity(Person person) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(SQL_CREATE_PERSON, new BeanPropertySqlParameterSource(person), keyHolder);
        return keyHolder.getKey().intValue();
    }

}
