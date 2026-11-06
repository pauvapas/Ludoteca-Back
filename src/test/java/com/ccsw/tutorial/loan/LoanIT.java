package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.client.model.ClientDto;
import com.ccsw.tutorial.common.pagination.PageableRequest;
import com.ccsw.tutorial.game.model.GameDto;
import com.ccsw.tutorial.loan.model.LoanDto;
import com.ccsw.tutorial.loan.model.LoanSearchDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestRestTemplate
public class LoanIT {

    public static final String LOCALHOST = "http://localhost:";
    public static final String SERVICE_PATH = "/loan";

    public static final Long DELETE_LOAN_ID = 4L;
    public static final Long MODIFY_LOAN_ID = 2L;

    public static final String NEW_LOAN_GAME_TITLE = "NEW GAME";
    public static final String NEW_LOAN_CLIENT_NAME = "NEW CLIENT";
    public static final Date NEW_LOAN_START_DATE = new Date("2026/05/09");
    public static final Date NEW_LOAN_END_DATE = new Date("2026/05/20");
    public static final Date BAD_LOAN_END_DATE = new Date("2026/05/30");

    private static final int TOTAL_LOANS = 4;
    private static final int PAGE_SIZE = 5;

    private static final String NOT_EXISTS_GAME = "?idGame=0";
    private static final String EXISTS_GAME = "?idGame=3";
    private static final String NOT_EXISTS_CLIENT = "?idClient=0";
    private static final String EXISTS_CLIENT = "?idClient=1";
    private static final String NOT_EXISTS_GAME_AND_NOT_EXISTS_CLIENT = "?idGame=0&idClient=0";
    private static final String EXISTS_GAME_AND_EXISTS_CLIENT = "?idGame=3&idClient=1";
    private static final String NOT_EXISTS_GAME_AND_EXISTS_CLIENT = "?idGame=0&idClient=1";
    private static final String EXISTS_GAME_AND_NOT_EXISTS_CLIENT = "?idGame=3&idClient=0";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void findWithoutFiltersShouldReturnAllLoansInDB() {
        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        int LOANS_WITH_FILTER = 4;

        ResponseEntity<String> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), String.class);

        JsonNode body = null;
        try {
            body = objectMapper.readTree(response.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        assertNotNull(response);
        assertEquals(LOANS_WITH_FILTER, body.get("totalElements").asInt());
    }

    @Test
    public void findExistsGameShouldReturnLoans() {
        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        int LOANS_WITH_FILTER = 1;

        ResponseEntity<String> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + EXISTS_GAME, HttpMethod.POST, new HttpEntity<>(searchDto), String.class);

        JsonNode body = null;
        try {
            body = objectMapper.readTree(response.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        assertNotNull(response);
        assertEquals(LOANS_WITH_FILTER, body.get("totalElements").asInt());
    }

    @Test
    public void findExistsClientShouldReturnLoans() {
        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        int LOANS_WITH_FILTER = 2;

        ResponseEntity<String> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + EXISTS_CLIENT, HttpMethod.POST, new HttpEntity<>(searchDto), String.class);

        JsonNode body = null;
        try {
            body = objectMapper.readTree(response.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        assertNotNull(response);
        assertEquals(LOANS_WITH_FILTER, body.get("totalElements").asInt());
    }

    @Test
    public void findExistsGameAndClientShouldReturnLoans() {
        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        int LOANS_WITH_FILTER = 1;

        ResponseEntity<String> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + EXISTS_GAME_AND_EXISTS_CLIENT, HttpMethod.POST, new HttpEntity<>(searchDto), String.class);

        JsonNode body = null;
        try {
            body = objectMapper.readTree(response.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        assertNotNull(response);
        assertEquals(LOANS_WITH_FILTER, body.get("totalElements").asInt());
    }

    @Test
    public void findNotExistsGameShouldReturnEmpty() {
        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        int LOANS_WITH_FILTER = 0;

        ResponseEntity<String> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + NOT_EXISTS_GAME, HttpMethod.POST, new HttpEntity<>(searchDto), String.class);

        JsonNode body = null;
        try {
            body = objectMapper.readTree(response.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        assertNotNull(response);
        assertEquals(LOANS_WITH_FILTER, body.get("totalElements").asInt());
    }

    @Test
    public void findNotExistsClientShouldReturnEmpty() {
        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        int LOANS_WITH_FILTER = 0;

        ResponseEntity<String> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + NOT_EXISTS_CLIENT, HttpMethod.POST, new HttpEntity<>(searchDto), String.class);

        JsonNode body = null;
        try {
            body = objectMapper.readTree(response.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        assertNotNull(response);
        assertEquals(LOANS_WITH_FILTER, body.get("totalElements").asInt());
    }

    @Test
    public void findNotExistsGameOrClientShouldReturnEmpty() {
        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        int LOANS_WITH_FILTER = 0;

        ResponseEntity<String> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + NOT_EXISTS_GAME_AND_NOT_EXISTS_CLIENT, HttpMethod.POST, new HttpEntity<>(searchDto), String.class);

        JsonNode body = null;
        try {
            body = objectMapper.readTree(response.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        assertNotNull(response);
        assertEquals(LOANS_WITH_FILTER, body.get("totalElements").asInt());

        response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + NOT_EXISTS_GAME_AND_EXISTS_CLIENT, HttpMethod.POST, new HttpEntity<>(searchDto), String.class);

        try {
            body = objectMapper.readTree(response.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        assertNotNull(response);
        assertEquals(LOANS_WITH_FILTER, body.get("totalElements").asInt());

        response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + EXISTS_GAME_AND_NOT_EXISTS_CLIENT, HttpMethod.POST, new HttpEntity<>(searchDto), String.class);

        try {
            body = objectMapper.readTree(response.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        assertNotNull(response);
        assertEquals(LOANS_WITH_FILTER, body.get("totalElements").asInt());
    }

    @Test
    public void findFirstPageWithFiveSizeShouldReturnFirstFourResults() {
        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<String> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), String.class);

        JsonNode body = null;
        try {
            body = objectMapper.readTree(response.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        assertEquals(TOTAL_LOANS, body.get("totalElements").asInt());
        assertEquals(TOTAL_LOANS, body.get("content").size());
    }

    @Test
    public void findSecondPageWithFiveSizeShouldReturnNoResults() {
        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(1, PAGE_SIZE));

        ResponseEntity<String> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), String.class);

        JsonNode body = null;
        try {
            body = objectMapper.readTree(response.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        assertEquals(TOTAL_LOANS, body.get("totalElements").asInt());
        assertEquals(0, body.get("content").size());
    }

    @Test
    public void saveWithoutIdShouldCreateNewLoan() {
        GameDto gameDto = new GameDto();
        gameDto.setId(1L);
        ClientDto clientDto = new ClientDto();
        clientDto.setId(1L);

        LoanDto dto = new LoanDto();
        dto.setGame(gameDto);
        dto.setClient(clientDto);
        dto.setStartDate(NEW_LOAN_START_DATE);
        dto.setEndDate(NEW_LOAN_END_DATE);

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<String> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), String.class);

        JsonNode body = null;
        try {
            body = objectMapper.readTree(response.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        assertEquals(5, body.get("totalElements").asInt());

        JsonNode loan = null;

        for (JsonNode node : body.get("content")) {
            if (node.get("id").asLong() == 5) {
                loan = node;
                break;
            }
        }

        assertNotNull(loan);
        assertEquals(1L, loan.get("client").get("id").asLong());
        assertEquals(1L, loan.get("game").get("id").asLong());
        assertEquals(NEW_LOAN_START_DATE.toInstant().toString(), java.time.Instant.parse(loan.get("startDate").asText()).toString());
        assertEquals(NEW_LOAN_END_DATE.toInstant().toString(), java.time.Instant.parse(loan.get("endDate").asText()).toString());
    }

    @Test
    public void modifyWithExistIdShouldModifyLoan() {
        GameDto gameDto = new GameDto();
        gameDto.setId(1L);
        gameDto.setTitle(NEW_LOAN_GAME_TITLE);
        ClientDto clientDto = new ClientDto();
        clientDto.setId(1L);
        clientDto.setName(NEW_LOAN_CLIENT_NAME);

        LoanDto dto = new LoanDto();
        dto.setGame(gameDto);
        dto.setClient(clientDto);
        dto.setStartDate(NEW_LOAN_START_DATE);
        dto.setEndDate(NEW_LOAN_END_DATE);

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + MODIFY_LOAN_ID, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<String> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), String.class);

        JsonNode body = null;
        try {
            body = objectMapper.readTree(response.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        assertEquals(TOTAL_LOANS, body.get("totalElements").asInt());

        JsonNode loan = null;

        for (JsonNode node : body.get("content")) {
            if (node.get("id").asLong() == MODIFY_LOAN_ID) {
                loan = node;
                break;
            }
        }

        assertNotNull(loan);
        assertEquals(1L, loan.get("client").get("id").asLong());
        assertEquals(1L, loan.get("game").get("id").asLong());
        assertEquals(NEW_LOAN_START_DATE.toInstant().toString(), java.time.Instant.parse(loan.get("startDate").asText()).toString());
        assertEquals(NEW_LOAN_END_DATE.toInstant().toString(), java.time.Instant.parse(loan.get("endDate").asText()).toString());
    }

    @Test
    public void saveWithBadOrderDatesAndWithoutIdShouldBadRequest() {
        GameDto gameDto = new GameDto();
        gameDto.setId(1L);
        ClientDto clientDto = new ClientDto();
        clientDto.setId(1L);

        LoanDto dto = new LoanDto();
        dto.setGame(gameDto);
        dto.setClient(clientDto);
        dto.setStartDate(NEW_LOAN_END_DATE);
        dto.setEndDate(NEW_LOAN_START_DATE);

        ResponseEntity<String> response1 = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response1.getStatusCode());

        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<String> response2 = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), String.class);

        assertNotNull(response2);

        JsonNode body = null;
        try {
            body = objectMapper.readTree(response2.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        assertEquals(TOTAL_LOANS, body.get("totalElements").asInt());
    }

    @Test
    public void modifyWithBadOrderDatesAndWithExistIdShouldBadRequest() {
        GameDto gameDto = new GameDto();
        gameDto.setId(1L);
        gameDto.setTitle(NEW_LOAN_GAME_TITLE);
        ClientDto clientDto = new ClientDto();
        clientDto.setId(1L);
        clientDto.setName(NEW_LOAN_CLIENT_NAME);

        LoanDto dto = new LoanDto();
        dto.setGame(gameDto);
        dto.setClient(clientDto);
        dto.setStartDate(NEW_LOAN_END_DATE);
        dto.setEndDate(NEW_LOAN_START_DATE);

        ResponseEntity<String> response1 = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + MODIFY_LOAN_ID, HttpMethod.PUT, new HttpEntity<>(dto), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response1.getStatusCode());

        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<String> response2 = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), String.class);

        assertNotNull(response2);

        JsonNode body = null;
        try {
            body = objectMapper.readTree(response2.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        assertEquals(TOTAL_LOANS, body.get("totalElements").asInt());
    }

    @Test
    public void saveLoanWithMoreThan14DaysAndWithoutIdShouldBadRequest() {
        GameDto gameDto = new GameDto();
        gameDto.setId(1L);
        ClientDto clientDto = new ClientDto();
        clientDto.setId(1L);

        LoanDto dto = new LoanDto();
        dto.setGame(gameDto);
        dto.setClient(clientDto);
        dto.setStartDate(NEW_LOAN_START_DATE);
        dto.setEndDate(BAD_LOAN_END_DATE);

        ResponseEntity<String> response1 = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response1.getStatusCode());

        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<String> response2 = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), String.class);

        assertNotNull(response2);

        JsonNode body = null;
        try {
            body = objectMapper.readTree(response2.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        assertEquals(TOTAL_LOANS, body.get("totalElements").asInt());
    }

    @Test
    public void modifyLoanWithMoreThan14DaysAndWithExistIdShouldBadRequest() {
        GameDto gameDto = new GameDto();
        gameDto.setId(1L);
        gameDto.setTitle(NEW_LOAN_GAME_TITLE);
        ClientDto clientDto = new ClientDto();
        clientDto.setId(1L);
        clientDto.setName(NEW_LOAN_CLIENT_NAME);

        LoanDto dto = new LoanDto();
        dto.setGame(gameDto);
        dto.setClient(clientDto);
        dto.setStartDate(NEW_LOAN_START_DATE);
        dto.setEndDate(BAD_LOAN_END_DATE);

        ResponseEntity<String> response1 = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + MODIFY_LOAN_ID, HttpMethod.PUT, new HttpEntity<>(dto), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response1.getStatusCode());

        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, PAGE_SIZE));

        ResponseEntity<String> response2 = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), String.class);

        assertNotNull(response2);

        JsonNode body = null;
        try {
            body = objectMapper.readTree(response2.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        assertEquals(TOTAL_LOANS, body.get("totalElements").asInt());
    }
    
    @Test
    public void modifyWithNotExistIdShouldThrowBadRequest() {
        long loanId = TOTAL_LOANS + 1;

        GameDto gameDto = new GameDto();
        gameDto.setTitle(NEW_LOAN_GAME_TITLE);
        ClientDto clientDto = new ClientDto();
        clientDto.setName(NEW_LOAN_CLIENT_NAME);

        LoanDto dto = new LoanDto();
        dto.setGame(gameDto);
        dto.setClient(clientDto);
        dto.setStartDate(NEW_LOAN_START_DATE);
        dto.setEndDate(NEW_LOAN_END_DATE);

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + loanId, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void deleteWithExistsIdShouldDeleteLoan() {

        long newLoansSize = TOTAL_LOANS - 1;

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + DELETE_LOAN_ID, HttpMethod.DELETE, null, Void.class);

        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, TOTAL_LOANS));

        ResponseEntity<String> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), String.class);

        JsonNode body = null;
        try {
            body = objectMapper.readTree(response.getBody());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        assertEquals(newLoansSize, body.get("totalElements").asInt());
    }

    @Test
    public void deleteWithNotExistsIdShouldThrowBadRequest() {

        long deleteLoanId = TOTAL_LOANS + 1;

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + deleteLoanId, HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}