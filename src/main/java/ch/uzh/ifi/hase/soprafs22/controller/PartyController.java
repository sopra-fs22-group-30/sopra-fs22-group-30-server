package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.entity.Party;
import ch.uzh.ifi.hase.soprafs22.rest.dto.party.PartyGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.party.PartyPostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.party.PartyPutDTO;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs22.service.PartyService;
import ch.uzh.ifi.hase.soprafs22.websocket.dtoWS.InvitationNotificationDTO;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PartyController {

    private final PartyService partyService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    PartyController(PartyService partyService, SimpMessagingTemplate simpMessagingTemplate) {
        this.partyService = partyService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    // get all parties
    @GetMapping("/parties")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<PartyGetDTO> getAllParties() {
        // fetch all parties in the internal representation
        List<Party> parties = partyService.getParties();
        List<PartyGetDTO> partyGetDTOs = new ArrayList<>();


        // convert each user to the API representation
        for (Party party : parties) {
            partyGetDTOs.add(DTOMapper.INSTANCE.convertEntityToPartyGetDTO(party));
        }
        return partyGetDTOs;
    }

    // create new party
    @PostMapping("/parties")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public PartyGetDTO partyRecipe(@RequestBody PartyPostDTO partyPostDTO) {
        // convert API recipe to internal representation
        Party partyInput = DTOMapper.INSTANCE.convertPartyPostDTOtoEntity(partyPostDTO);

        // create recipe
        Party createdParty = partyService.createParty(partyInput);

        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToPartyGetDTO(createdParty);
    }

    // get one party detail
    @GetMapping("/users/{userId}/parties/{partyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PartyGetDTO partyGetDTO (@PathVariable("userId") Long userId, @PathVariable("partyId") Long partyId) {
        Party party = partyService.getPartyById(userId, partyId);
        return DTOMapper.INSTANCE.convertEntityToPartyGetDTO(party);
    }

    // get party list by userId: retrieve the parties the user is in
    @GetMapping("/users/parties/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<PartyGetDTO> getPartiesAUserIsIn (@PathVariable("userId") Long userId) {
        // fetch all parties consisting of a specific user in the internal representation
        List<Party> parties = partyService.getPartiesAUserIsIn(userId);
        List<PartyGetDTO> partyGetDTOs = new ArrayList<>();
        // convert each user to the API representation
        for (Party party : parties) {
            partyGetDTOs.add(DTOMapper.INSTANCE.convertEntityToPartyGetDTO(party));
        }
        return partyGetDTOs;
    }


    // edit party detail
    @PutMapping("/users/{userId}/parties/{partyId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void editParty(@PathVariable("userId") Long userId, @PathVariable("partyId") Long partyId,@RequestBody PartyPutDTO partyPutDTO) {
        Party partyToUpdate = DTOMapper.INSTANCE.convertPartyPutDTOtoEntity(partyPutDTO);
        //check if edit is possible
        partyService.checkEditPartyConditions(userId, partyId, partyToUpdate);
        //check if new users are added to the party. If yes, sent invitation notification.
        List<Long> newAttendants = partyService.findNewAttendantsAdded(partyId,partyToUpdate);
        if (!newAttendants.isEmpty()){
            InvitationNotificationDTO notification = partyService.prepareNotification(userId,partyId,partyToUpdate);
            for (Long userIdOfNewComer: newAttendants){
                simpMessagingTemplate.convertAndSend("/invitation/"+userIdOfNewComer+"/fetch",notification);
            }
        }
        //really change the partyRepository:
        partyService.editParty(partyId, partyToUpdate);

    }

    // (a host) delete a party
    @DeleteMapping("/parties/{partyId}/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void deleteParty(@PathVariable("partyId") Long partyId,@PathVariable("userId") Long userId) {
        // delete party by the given userID from client
        partyService.deleteParty(userId, partyId);
    }

    // (an attendant) quit a party
    @PutMapping("/parties/quitting/{partyId}/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void quitParty(@PathVariable("userId") Long userId, @PathVariable("partyId") Long partyId) {
        // delete party by the given userID from client
        partyService.quitParty(userId, partyId);
    }





}
