package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.entity.Party;
import ch.uzh.ifi.hase.soprafs22.repository.PartyRepository;
import ch.uzh.ifi.hase.soprafs22.rest.dto.party.PartyGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.party.PartyPostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.party.PartyPutDTO;
import ch.uzh.ifi.hase.soprafs22.service.PartyService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@WebMvcTest(PartyController.class)
public class PartyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PartyService partyService;

    @MockBean
    private SimpMessagingTemplate simpMessagingTemplate;

    @MockBean
    private PartyRepository partyRepository;

    @Test
    public void getAllParties() throws Exception{
        //given
        Party party = new Party();
        party.setPartyId(1L);
        party.setPartyName("testName");

        List<Party> allParty = Collections.singletonList(party);

        // this mocks the UserService -> we define above what the userService should
        // return when getUsers() is called
        given(partyService.getParties()).willReturn(allParty);

        // when
        MockHttpServletRequestBuilder getRequest = get("/parties").contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].partyId", is(party.getPartyId().intValue())))
                .andExpect(jsonPath("$[0].partyName", is(party.getPartyName())));
    }

    //create party with valid input
    @Test
    public void createParty_validInput() throws Exception {
        // given
        Party party = new Party();
        party.setPartyId(1L);
        party.setPartyName("testName");

        PartyPostDTO partyPostDTO = new PartyPostDTO();
        partyPostDTO.setPartyName("testUsername");

        given(partyService.createParty(Mockito.any())).willReturn(party);

        // when/then -> do the request + validate the result
        MockHttpServletRequestBuilder postRequest = post("/parties")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(partyPostDTO));

        // then
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.partyId", is(party.getPartyId().intValue())))
                .andExpect(jsonPath("$.partyName", is(party.getPartyName())));
    }

    @Test
    public void getParty_validId() throws Exception {
        // given
        Party party = new Party();
        party.setPartyId(1L);
        party.setPartyName("testName");

        PartyGetDTO partyGetDTO = new PartyGetDTO();
        partyGetDTO.setPartyName(party.getPartyName());

        // when/then -> do the request + validate the result
        Mockito.when(partyService.getPartyById(1L,party.getPartyId()))
                .thenReturn(party);

        MockHttpServletRequestBuilder getRequest = get("/users/1/parties/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(partyGetDTO));

        //then
        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.partyName", is(party.getPartyName())));
    }

    @Test
    public void getParty_invalidId() throws Exception {
        // given
        Party party = new Party();
        party.setPartyId(1L);
        party.setPartyName("testName");

        PartyGetDTO partyGetDTO = new PartyGetDTO();
        partyGetDTO.setPartyName(party.getPartyName());

        given(partyService.getPartyById(1L,100L))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        // when/then -> do the request + validate the result
        Mockito.when(partyService.getPartyById(1L,party.getPartyId()))
                .thenReturn(party);

        MockHttpServletRequestBuilder getRequest = get("/users/1/parties/100")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(partyGetDTO));

        //then
        mockMvc.perform(getRequest)
                .andExpect(status().isNotFound());
    }

    @Test
    public void getPartiesAUserIsIn_success() throws Exception {
        // given
        Party party = new Party();
        party.setPartyId(1L);
        party.setPartyName("testName");

        List<Party> parties = new ArrayList<>();
        parties.add(party);

        PartyGetDTO partyGetDTO = new PartyGetDTO();

        Mockito.when(partyService.getPartiesAUserIsIn(Mockito.any())).thenReturn(parties);

        MockHttpServletRequestBuilder getRequest = get("/users/parties/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(partyGetDTO));

        mockMvc.perform(getRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void editParty_success() throws Exception {
        Party oldParty = new Party();
        oldParty.setPartyId(1L);
        oldParty.setPartyName("old name");
        List<String> testAttendList = new ArrayList<>();
        testAttendList.add("testUser");
        oldParty.setPartyAttendantsList(testAttendList);

        PartyPutDTO partyPutDTO = new PartyPutDTO();
        partyPutDTO.setPartyName("new name");
        partyPutDTO.setPartyAttendantsList(testAttendList);

        List<Long> testList = new ArrayList<>();

        Mockito.when(partyService.createParty(Mockito.any())).thenReturn(oldParty);
        Mockito.doNothing().when(partyService).checkEditPartyConditions(Mockito.any(),Mockito.any(),Mockito.any());
        Mockito.when(partyService.findNewAttendantsAdded(Mockito.any(),Mockito.any())).thenReturn(testList);
        Mockito.when(partyRepository.findById(Mockito.any())).thenReturn(java.util.Optional.of(oldParty));

        Mockito.when(partyService.editParty(Mockito.any(),Mockito.any())).thenReturn(oldParty);

        MockHttpServletRequestBuilder putRequest = put("/users/1/parties/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(partyPutDTO));

        mockMvc.perform(putRequest)
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteParty_success() throws Exception {
        doNothing().when(partyService).deleteParty(Mockito.any(),Mockito.any());

        MockHttpServletRequestBuilder deleteRequest = delete("/parties/1/users/1")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(deleteRequest)
                .andExpect(status().isNoContent());
    }

    @Test
    public void quitParty_success() throws Exception {
        doNothing().when(partyService).quitParty(Mockito.any(), Mockito.any());

        MockHttpServletRequestBuilder putRequest = put("/parties/quitting/1/users/1")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(putRequest)
                .andExpect(status().isNoContent());
    }

    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("The request body could not be created.%s", e));
        }
    }

}
