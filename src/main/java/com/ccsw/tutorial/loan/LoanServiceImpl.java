package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.client.ClientService;
import com.ccsw.tutorial.common.criteria.SearchCriteria;
import com.ccsw.tutorial.game.GameService;
import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;
import com.ccsw.tutorial.loan.model.LoanSearchDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * @author ccsw
 *
 */
@Service
@Transactional
public class LoanServiceImpl implements LoanService {

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    GameService gameService;

    @Autowired
    ClientService clientService;

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Loan> findPage(LoanSearchDto dto, Long idGame, Long idClient, Date date) {

        LoanSpecification gameSpec = new LoanSpecification(new SearchCriteria("game.id", ":", idGame));
        LoanSpecification clientSpec = new LoanSpecification(new SearchCriteria("client.id", ":", idClient));

        LoanSpecification dateSpec1 = new LoanSpecification(new SearchCriteria("startDate", "<", date));
        LoanSpecification dateSpec2 = new LoanSpecification(new SearchCriteria("endDate", ">", date));

        Specification<Loan> spec = Specification.where(gameSpec).and(clientSpec).and(dateSpec1).and(dateSpec2);

        return this.loanRepository.findAll(spec, dto.getPageable().getPageable());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkDatesOrder(Date start, Date end) {
        boolean correct = true;

        if (end.before(start)) {
            correct = false;
            return correct;
        }

        return correct;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkDatesDays(Date start, Date end) {
        boolean correct = true;

        LocalDate startLocaleDate = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endLocaleDate = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        LocalDate expectedDuration = startLocaleDate.plusDays(14);

        if (endLocaleDate.isAfter(expectedDuration)) {
            correct = false;
            return correct;
        }

        return correct;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkIntervalDates(Loan original, Loan updated) {
        boolean datesInInterval = false;
        LocalDate startLocaleDate_Loan = original.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endLocaleDate_Loan = original.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate startLocaleDate_NewLoan = updated.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endLocaleDate_NewLoan = updated.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        if (startLocaleDate_NewLoan.isEqual(startLocaleDate_Loan) || startLocaleDate_NewLoan.isEqual(endLocaleDate_Loan) || endLocaleDate_NewLoan.isEqual(startLocaleDate_Loan) || endLocaleDate_NewLoan.isEqual(endLocaleDate_Loan)) {
            datesInInterval = true;
            return datesInInterval;
        } else if ((startLocaleDate_NewLoan.isAfter(startLocaleDate_Loan) && startLocaleDate_NewLoan.isBefore(endLocaleDate_Loan)) || (endLocaleDate_NewLoan.isBefore(endLocaleDate_Loan) && endLocaleDate_NewLoan.isAfter(
                startLocaleDate_Loan))) {
            datesInInterval = true;
            return datesInInterval;
        } else if ((startLocaleDate_NewLoan.isBefore(startLocaleDate_Loan) && startLocaleDate_NewLoan.isBefore(endLocaleDate_Loan)) && (endLocaleDate_NewLoan.isAfter(startLocaleDate_Loan) && endLocaleDate_NewLoan.isBefore(
                endLocaleDate_Loan))) {
            datesInInterval = true;
            return datesInInterval;
        } else if ((startLocaleDate_NewLoan.isBefore(startLocaleDate_Loan) && startLocaleDate_NewLoan.isBefore(endLocaleDate_Loan)) && (endLocaleDate_NewLoan.isBefore(endLocaleDate_Loan) && endLocaleDate_NewLoan.isAfter(
                startLocaleDate_Loan))) {
            datesInInterval = true;
            return datesInInterval;
        } else if ((startLocaleDate_NewLoan.isBefore(startLocaleDate_Loan) && startLocaleDate_NewLoan.isBefore(endLocaleDate_Loan)) && (endLocaleDate_NewLoan.isAfter(endLocaleDate_Loan) && endLocaleDate_NewLoan.isAfter(
                startLocaleDate_Loan))) {
            datesInInterval = true;
            return datesInInterval;
        } else if ((startLocaleDate_NewLoan.isAfter(startLocaleDate_Loan) && startLocaleDate_NewLoan.isBefore(endLocaleDate_Loan)) && (endLocaleDate_NewLoan.isAfter(endLocaleDate_Loan) && endLocaleDate_NewLoan.isAfter(
                startLocaleDate_Loan))) {
            datesInInterval = true;
            return datesInInterval;
        } else if ((startLocaleDate_NewLoan.isBefore(endLocaleDate_Loan) && startLocaleDate_NewLoan.isAfter(startLocaleDate_Loan)) && (endLocaleDate_NewLoan.isAfter(endLocaleDate_Loan) && endLocaleDate_NewLoan.isAfter(
                startLocaleDate_Loan))) {
            datesInInterval = true;
            return datesInInterval;
        }

        return datesInInterval;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkGame(Loan loan) {
        Long id = loan.getId();
        boolean gameAlreadyLoaned = false;

        List<Loan> loans = this.findAll();

        for (Loan l : loans) {
            if (l != loan) {
                if (loan.getGame() == l.getGame()) {
                    gameAlreadyLoaned = this.checkIntervalDates(l, loan);
                }
            }
        }

        return gameAlreadyLoaned;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkClient(Loan loan) {
        Long id = loan.getId();
        boolean clientAlreadyLoaned = false;

        List<Loan> loans = this.findAll();

        for (Loan l : loans) {
            if (l != loan) {
                if (loan.getClient() == l.getClient()) {
                    clientAlreadyLoaned = this.checkIntervalDates(l, loan);
                }
            }
        }

        return clientAlreadyLoaned;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(Long id, LoanDto data) throws Exception {

        Loan loan;

        if (id == null) {
            loan = new Loan();
        } else {
            loan = this.loanRepository.findById(id).orElse(null);
        }

        if (this.checkDatesOrder(data.getStartDate(), data.getEndDate())) {
            if (this.checkDatesDays(data.getStartDate(), data.getEndDate())) {
                BeanUtils.copyProperties(data, loan, "id", "game", "client");

                loan.setGame(gameService.get(data.getGame().getId()));
                loan.setClient(clientService.get(data.getClient().getId()));

                if (!this.checkGame(loan)) {
                    if (!this.checkClient(loan)) {
                        this.loanRepository.save(loan);
                    } else {
                        throw new Exception("Client has a current loan for the chosen dates");
                    }
                } else {
                    throw new Exception("Game is already loaned for the chosen dates");
                }
            } else {
                throw new Exception("The loan cannot exceed 14 days");
            }
        } else {
            throw new Exception("endDate cannot be earlier than startDate");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) throws Exception {

        if (this.loanRepository.findById(id).orElse(null) == null) {
            throw new Exception("Not exists");
        }

        this.loanRepository.deleteById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Loan> findAll() {

        return (List<Loan>) this.loanRepository.findAll();
    }
}