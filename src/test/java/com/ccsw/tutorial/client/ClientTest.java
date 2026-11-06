package com.ccsw.tutorial.client;

import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.client.model.ClientDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientTest {
    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientServiceImpl clientService;

    @Test
    public void findAllShouldReturnAllClients() {

        List<Client> list = new ArrayList<>();
        list.add(mock(Client.class));

        when(clientRepository.findAll()).thenReturn(list);

        List<Client> clients = clientService.findAll();

        assertNotNull(clients);
        assertEquals(1, clients.size());
    }

    public static final String CLIENT_NAME = "CLIENT1";

    @Test
    public void saveNotExistsClientIdShouldInsert() {

        ClientDto clientDto = new ClientDto();
        clientDto.setName(CLIENT_NAME);

        ArgumentCaptor<Client> client = ArgumentCaptor.forClass(Client.class);

        try {
            clientService.save(null, clientDto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(clientRepository).save(client.capture());

        assertEquals(CLIENT_NAME, client.getValue().getName());
    }

    public static final Long EXISTS_CLIENT_ID = 1L;

    @Test
    public void saveExistsClientIdShouldUpdate() {

        ClientDto clientDto = new ClientDto();
        clientDto.setName(CLIENT_NAME);

        Client client = mock(Client.class);

        when(client.getName()).thenReturn(CLIENT_NAME);

        when(clientRepository.findById(EXISTS_CLIENT_ID)).thenReturn(Optional.of(client));

        try {
            clientService.save(EXISTS_CLIENT_ID, clientDto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        verify(clientRepository).save(client);
    }

    @Test
    public void deleteExistsClientIdShouldDelete() throws Exception {

        Client client = mock(Client.class);
        when(clientRepository.findById(EXISTS_CLIENT_ID)).thenReturn(Optional.of(client));

        clientService.delete(EXISTS_CLIENT_ID);

        verify(clientRepository).deleteById(EXISTS_CLIENT_ID);
    }

    public static final Long NOT_EXISTS_CLIENT_ID = 0L;

    @Test
    public void getExistsClientIdShouldReturnClient() {

        Client client = mock(Client.class);
        when(client.getId()).thenReturn(EXISTS_CLIENT_ID);
        when(clientRepository.findById(EXISTS_CLIENT_ID)).thenReturn(Optional.of(client));

        Client clientResponse = clientService.get(EXISTS_CLIENT_ID);

        assertNotNull(clientResponse);
        assertEquals(EXISTS_CLIENT_ID, client.getId());
    }

    @Test
    public void getNotExistsClientIdShouldReturnNull() {

        when(clientRepository.findById(NOT_EXISTS_CLIENT_ID)).thenReturn(Optional.empty());

        Client client = clientService.get(NOT_EXISTS_CLIENT_ID);

        assertNull(client);
    }

    public static final String CLIENT_NAME_CHECK_TRUE = "FeRnAnDo";

    @Test
    public void checkSameClientNamesReturnsTrue() {
        Client existingClient = new Client();
        existingClient.setName("Fernando");

        when(clientRepository.findAll()).thenReturn(List.of(existingClient));

        Client client = new Client();
        client.setName(CLIENT_NAME_CHECK_TRUE);

        boolean found = clientService.checkClientNameExists(client);

        assertTrue(found);
    }

    public static final String CLIENT_NAME_CHECK_FALSE = "PaU";

    @Test
    public void checkDifferentClientNamesReturnFalse() {
        Client existingClient = new Client();
        existingClient.setName("Fernando");

        when(clientRepository.findAll()).thenReturn(List.of(existingClient));

        Client client = new Client();
        client.setName(CLIENT_NAME_CHECK_FALSE);

        boolean found = clientService.checkClientNameExists(client);

        assertFalse(found);
    }
}