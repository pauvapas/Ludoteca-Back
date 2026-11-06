package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;
import com.ccsw.tutorial.loan.model.LoanSearchDto;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

/**
 * @author ccsw
 *
 */
public interface LoanService {

    /**
     * Método para recuperar un listado paginado de {@link Loan} filtrando opcionalmente por juego y/o cliente
     *
     * @param dto dto de búsqueda
     * @param idGame PK del juego
     * @param idClient PK del cliente
     * @param date fecha del préstamo
     * @return {@link Page} de {@link Loan}
     */
    Page<Loan> findPage(LoanSearchDto dto, Long idGame, Long idClient, Date date);

    /**
     * Método para comprobar si las fechas de un {@link Loan} son correctas (fecha de fin no es menor que fecha de inicio)
     *
     * @param start fecha de inicio del préstamo
     * @param end fecha de fin del préstamo
     * @return {boolean}
     */
    boolean checkDatesOrder(Date start, Date end);

    /**
     * Método para comprobar si los {@link Loan} son de máximo 14 días
     *
     * @param start fecha de inicio del préstamo
     * @param end fecha de fin del préstamo
     * @return {boolean}
     */
    boolean checkDatesDays(Date start, Date end);

    /**
     * Método para comprobar si las fechas de un {@link Loan} nuevo no coinciden con un intervalo ya existente
     *
     * @param original préstamo ya creado en BD
     * @param updated nuevo préstamo a insertar en BD si cumple las condicones
     * @return {boolean}
     */
    boolean checkIntervalDates(Loan original, Loan updated);

    /**
     * Método para comprobar si un juego ya pertenece a un {@link Loan} existente
     *
     * @param loan préstamo a comprobar
     * @return {boolean}
     */
    boolean checkGame(Loan loan);

    /**
     * Método para comprobar si un cliente ya tiene un {@link Loan} existente
     *
     * @param loan préstamo a comprobar
     * @return {boolean}
     */
    boolean checkClient(Loan loan);

    /**
     * Método para crear o actualizar un {@link Loan}
     *
     * @param id PK de la entidad
     * @param dto datos de la entidad
     */
    void save(Long id, LoanDto dto) throws Exception;

    /**
     * Método para crear o actualizar un {@link Loan}
     *
     * @param id PK de la entidad
     */
    void delete(Long id) throws Exception;

    /**
     * Recupera un listado de préstamos {@link Loan}
     *
     * @return {@link List} de {@link Loan}
     */
    List<Loan> findAll();
}