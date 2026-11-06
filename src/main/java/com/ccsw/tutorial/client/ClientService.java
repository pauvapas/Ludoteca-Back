package com.ccsw.tutorial.client;

import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.client.model.ClientDto;

import java.util.List;

/**
 * @author ccsw
 *
 */
public interface ClientService {

    /**
     * Método para comprobar si el nombre de un {@link Client} existe ya en la BD
     *
     * @param client cliente a guardar o actualizar
     * @return {boolean}
     */
    boolean checkClientNameExists(Client client);

    /**
     * Método para recuperar todos los {@link Client}
     *
     * @return {@link List} de {@link Client}
     */
    List<Client> findAll();

    /**
     * Método para crear o actualizar un {@link Client}
     *
     * @param id PK de la entidad
     * @param dto datos de la entidad
     */
    void save(Long id, ClientDto dto) throws Exception;

    /**
     * Método para borrar un {@link Client}
     *
     * @param id PK de la entidad
     */
    void delete(Long id) throws Exception;

    /**
     * Recupera un {@link Client} a través de su ID
     *
     * @param id PK de la entidad
     * @return {@link Client}
     */
    Client get(Long id);
}