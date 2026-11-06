package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.client.ClientService;
import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.client.model.ClientDto;
import com.ccsw.tutorial.game.GameService;
import com.ccsw.tutorial.game.model.Game;
import com.ccsw.tutorial.game.model.GameDto;
import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoanTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private GameService gameService;

    @Mock
    private ClientService clientService;

    @InjectMocks
    private LoanServiceImpl loanService;

    @Test
    public void findAllShouldReturnAllLoans() {

        List<Loan> list = new ArrayList<>();
        list.add(mock(Loan.class));

        when(loanRepository.findAll()).thenReturn(list);

        List<Loan> loans = loanService.findAll();

        assertNotNull(loans);
        assertEquals(1, loans.size());
    }

    public static final String LOAN_GAME_TITLE = "GAME1";
    public static final String LOAN_CLIENT_NAME = "CLIENT1";
    public static final Date LOAN_START_DATE = new Date("2026/10/09");
    public static final Date LOAN_END_DATE = new Date("2026/10/20");

    @Test
    public void saveNotExistsLoanIdShouldInsert() {
        GameDto gameDto = new GameDto();
        gameDto.setId(1L);
        gameDto.setTitle(LOAN_GAME_TITLE);
        ClientDto clientDto = new ClientDto();
        clientDto.setId(1L);
        clientDto.setName(LOAN_CLIENT_NAME);

        LoanDto loanDto = new LoanDto();
        loanDto.setGame(gameDto);
        loanDto.setClient(clientDto);
        loanDto.setStartDate(LOAN_START_DATE);
        loanDto.setEndDate(LOAN_END_DATE);

        Game game = new Game();
        game.setTitle(LOAN_GAME_TITLE);

        Client client = new Client();
        client.setName(LOAN_CLIENT_NAME);

        when(gameService.get(1L)).thenReturn(game);
        when(clientService.get(1L)).thenReturn(client);
        when(loanRepository.findAll()).thenReturn(new ArrayList<>());

        ArgumentCaptor<Loan> loan = ArgumentCaptor.forClass(Loan.class);

        try {
            loanService.save(null, loanDto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(loanRepository).save(loan.capture());

        assertEquals(LOAN_GAME_TITLE, loan.getValue().getGame().getTitle());
        assertEquals(LOAN_CLIENT_NAME, loan.getValue().getClient().getName());
        assertEquals(LOAN_START_DATE, loan.getValue().getStartDate());
        assertEquals(LOAN_END_DATE, loan.getValue().getEndDate());
    }

    public static final Long EXISTS_LOAN_ID = 1L;

    @Test
    public void saveExistsLoanIdShouldUpdate() {
        GameDto gameDto = new GameDto();
        gameDto.setId(1L);
        gameDto.setTitle(LOAN_GAME_TITLE);
        ClientDto clientDto = new ClientDto();
        clientDto.setId(1L);
        clientDto.setName(LOAN_CLIENT_NAME);

        LoanDto loanDto = new LoanDto();
        loanDto.setGame(gameDto);
        loanDto.setClient(clientDto);
        loanDto.setStartDate(LOAN_START_DATE);
        loanDto.setEndDate(LOAN_END_DATE);

        Game game = new Game();
        game.setTitle(LOAN_GAME_TITLE);

        Client client = new Client();
        client.setName(LOAN_CLIENT_NAME);

        when(gameService.get(1L)).thenReturn(game);
        when(clientService.get(1L)).thenReturn(client);
        when(loanRepository.findAll()).thenReturn(new ArrayList<>());

        Loan loan = mock(Loan.class);
        when(loanRepository.findById(EXISTS_LOAN_ID)).thenReturn(Optional.of(loan));

        try {
            loanService.save(EXISTS_LOAN_ID, loanDto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(loanRepository).save(loan);
    }

    @Test
    public void deleteExistsLoanIdShouldDelete() throws Exception {

        Loan loan = mock(Loan.class);
        when(loanRepository.findById(EXISTS_LOAN_ID)).thenReturn(Optional.of(loan));

        loanService.delete(EXISTS_LOAN_ID);

        verify(loanRepository).deleteById(EXISTS_LOAN_ID);
    }

    @Test
    public void checkDatesCorrectOrder() {
        LocalDate start = LocalDate.of(2026, 11, 05);
        LocalDate end = LocalDate.of(2026, 11, 27);

        boolean check = loanService.checkDatesOrder(Date.from(start.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), Date.from(end.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));

        assertTrue(check);
    }

    @Test
    public void checkDatesIncorrectOrder() {
        LocalDate start = LocalDate.of(2026, 11, 05);
        LocalDate end = LocalDate.of(2026, 10, 27);

        boolean check = loanService.checkDatesOrder(Date.from(start.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), Date.from(end.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));

        assertFalse(check);
    }

    @Test
    public void checkDatesLoanAre14DaysMaximumIsCorrect() {
        LocalDate start = LocalDate.of(2026, 11, 05);
        LocalDate end = LocalDate.of(2026, 11, 13);

        boolean check = loanService.checkDatesDays(Date.from(start.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), Date.from(end.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));

        assertTrue(check);

        start = LocalDate.of(2026, 11, 06);
        end = LocalDate.of(2026, 11, 20);

        check = loanService.checkDatesDays(Date.from(start.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), Date.from(end.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));

        assertTrue(check);
    }

    @Test
    public void checkDatesLoanAre14DaysMaximumIsIncorrect() {
        LocalDate start = LocalDate.of(2026, 11, 05);
        LocalDate end = LocalDate.of(2026, 11, 30);

        boolean check = loanService.checkDatesDays(Date.from(start.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), Date.from(end.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));

        assertFalse(check);

        start = LocalDate.of(2026, 11, 06);
        end = LocalDate.of(2026, 11, 21);

        check = loanService.checkDatesDays(Date.from(start.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), Date.from(end.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));

        assertFalse(check);
    }

    @Test
    public void checkGameIntervalDatesIsIncorrect() {
        Game game = new Game();

        Loan existingLoan = new Loan();
        existingLoan.setGame(game);
        existingLoan.setStartDate(new Date("2026/11/01"));
        existingLoan.setEndDate(new Date("2026/11/10"));

        Loan newLoan = new Loan();
        newLoan.setGame(game);
        newLoan.setStartDate(new Date("2026/10/30"));
        newLoan.setEndDate(new Date("2026/11/06"));

        when(loanRepository.findAll()).thenReturn(List.of(existingLoan));

        boolean result = loanService.checkGame(newLoan);

        assertTrue(result);

        newLoan.setStartDate(new Date("2026/10/30"));
        newLoan.setEndDate(new Date("2026/11/12"));

        result = loanService.checkGame(newLoan);

        assertTrue(result);

        newLoan.setStartDate(new Date("2026/11/03"));
        newLoan.setEndDate(new Date("2026/11/08"));

        result = loanService.checkGame(newLoan);

        assertTrue(result);

        newLoan.setStartDate(new Date("2026/11/03"));
        newLoan.setEndDate(new Date("2026/11/12"));

        result = loanService.checkGame(newLoan);

        assertTrue(result);
    }

    @Test
    public void checkGameIntervalDatesIsCorrect() {
        Game game = new Game();

        Loan existingLoan = new Loan();
        existingLoan.setGame(game);
        existingLoan.setStartDate(new Date("2026/11/01"));
        existingLoan.setEndDate(new Date("2026/11/10"));

        Loan newLoan = new Loan();
        newLoan.setGame(game);
        newLoan.setStartDate(new Date("2026/11/15"));
        newLoan.setEndDate(new Date("2026/11/20"));

        when(loanRepository.findAll()).thenReturn(List.of(existingLoan));

        boolean result = loanService.checkGame(newLoan);

        assertFalse(result);
    }

    @Test
    public void checkClientIntervalDatesIsIncorrect() {
        Client client = new Client();

        Loan existingLoan = new Loan();
        existingLoan.setClient(client);
        existingLoan.setStartDate(new Date("2026/11/01"));
        existingLoan.setEndDate(new Date("2026/11/10"));

        Loan newLoan = new Loan();
        newLoan.setClient(client);
        newLoan.setStartDate(new Date("2026/10/30"));
        newLoan.setEndDate(new Date("2026/11/06"));

        when(loanRepository.findAll()).thenReturn(List.of(existingLoan));

        boolean result = loanService.checkClient(newLoan);

        assertTrue(result);

        newLoan.setStartDate(new Date("2026/10/30"));
        newLoan.setEndDate(new Date("2026/11/12"));

        result = loanService.checkClient(newLoan);

        assertTrue(result);

        newLoan.setStartDate(new Date("2026/11/03"));
        newLoan.setEndDate(new Date("2026/11/08"));

        result = loanService.checkClient(newLoan);

        assertTrue(result);

        newLoan.setStartDate(new Date("2026/11/03"));
        newLoan.setEndDate(new Date("2026/11/12"));

        result = loanService.checkClient(newLoan);

        assertTrue(result);
    }

    @Test
    public void checkClientIntervalDatesIsCorrect() {
        Client client = new Client();

        Loan existingLoan = new Loan();
        existingLoan.setClient(client);
        existingLoan.setStartDate(new Date("2026/11/01"));
        existingLoan.setEndDate(new Date("2026/11/10"));

        Loan newLoan = new Loan();
        newLoan.setClient(client);
        newLoan.setStartDate(new Date("2026/11/15"));
        newLoan.setEndDate(new Date("2026/11/20"));

        when(loanRepository.findAll()).thenReturn(List.of(existingLoan));

        boolean result = loanService.checkClient(newLoan);

        assertFalse(result);
    }
}