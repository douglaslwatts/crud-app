package com.aquent.crudapp.person;

import java.util.Collections;
import java.util.List;

import com.aquent.crudapp.client.Client;
import com.aquent.crudapp.client.ClientRowMapper;
import com.aquent.crudapp.interfaces.EntityDao;
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

/**
 * Spring JDBC implementation of {@link EntityDao}.
 */
@Repository
@Qualifier("personDAO")
public class JdbcPersonDAO implements EntityDao<Person, Client> {

    private static final String SQL_LIST_PEOPLE = "SELECT * FROM person ORDER BY first_name, last_name, person_id";

    private static final String SQL_READ_PERSON = "SELECT * FROM person WHERE person_id = :personId";
    private static final String READ_CLIENT = "SELECT * FROM client WHERE client_id = :clientId";
    private static final String SQL_DELETE_PERSON = "DELETE FROM person WHERE person_id = :personId";
    private static final String REMOVE_ASSOCIATION = "DELETE FROM client_person_associations " +
                                                     "WHERE person_id = :personId " +
                                                     "AND client_id = :clientId";
    private static final String SQL_UPDATE_PERSON = "UPDATE person SET (first_name, last_name, email_address, street_address, city, state, zip_code)"
                                                  + " = (:firstName, :lastName, :emailAddress, :streetAddress, :city, :state, :zipCode)"
                                                  + " WHERE person_id = :personId";
    private static final String SQL_CREATE_PERSON = "INSERT INTO person (first_name, last_name, email_address, street_address, city, state, zip_code)"
                                                  + " VALUES (:firstName, :lastName, :emailAddress, :streetAddress, :city, :state, :zipCode)";

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

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Person> listEntities() {
        return namedParameterJdbcTemplate.getJdbcOperations().query(SQL_LIST_PEOPLE, new PersonRowMapper());
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Client> getAssociations(Integer personId) {
        MapSqlParameterSource paramMapper = new MapSqlParameterSource();
        paramMapper.addValue("personId", personId);
        return namedParameterJdbcTemplate.query(GET_CLIENTS, paramMapper, new ClientRowMapper());

        /* This one says something is wrong with the SQL, implies param-arg not set for :personId */
//        return namedParameterJdbcTemplate.getJdbcOperations().query(GET_CLIENTS,
//                                                                    new ClientRowMapper(),
//                                                                    personId);


        /* This one says something is wrong with the SQL, implies param-arg not set for :personId */
//        return namedParameterJdbcTemplate.getJdbcOperations().query(GET_CLIENTS,
//                                                                    new ArgumentPreparedStatementSetter(new Integer[] {personId}),
//                                                                    new ClientRowMapper());
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Person readEntity(Integer personId) {
        return namedParameterJdbcTemplate.queryForObject(SQL_READ_PERSON, Collections.singletonMap("personId", personId), new PersonRowMapper());
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Client readAssociatedEntity(Integer clientId) {
        return namedParameterJdbcTemplate.queryForObject(READ_CLIENT, Collections.singletonMap(
                "clientId", clientId), new ClientRowMapper());
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public void removeAssociation(Integer personId, Integer clientId) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("personId", personId);
        mapSqlParameterSource.addValue("clientId", clientId);
        namedParameterJdbcTemplate.update(REMOVE_ASSOCIATION, mapSqlParameterSource);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public void deleteEntity(Integer personId) {
        namedParameterJdbcTemplate.update(SQL_DELETE_PERSON, Collections.singletonMap("personId", personId));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public void updateEntity(Person person) {
        namedParameterJdbcTemplate.update(SQL_UPDATE_PERSON, new BeanPropertySqlParameterSource(person));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public Integer createEntity(Person person) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(SQL_CREATE_PERSON, new BeanPropertySqlParameterSource(person), keyHolder);
        return keyHolder.getKey().intValue();
    }

}
