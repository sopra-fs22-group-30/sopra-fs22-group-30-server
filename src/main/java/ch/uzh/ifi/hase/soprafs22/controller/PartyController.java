package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.entity.Party;
import ch.uzh.ifi.hase.soprafs22.entity.Recipe;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.rest.dto.party.PartyGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.party.PartyPostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.party.PartyPutDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.recipe.RecipeGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.recipe.RecipePostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.recipe.RecipePutDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.user.UserGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.user.UserPutDTO;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs22.service.PartyService;
import ch.uzh.ifi.hase.soprafs22.service.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PartyController {

    private final PartyService partyService;

    PartyController(PartyService partyService) {
        this.partyService = partyService;
    }

    // get all parties
    @GetMapping("/parties")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<PartyGetDTO> getAllParties() {
        // fetch all users in the internal representation
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

    // edit party detail
    @PutMapping("/users/{userId}/parties/{partyId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void editParty(@PathVariable("userId") Long userId, @PathVariable("partyId") Long partyId,@RequestBody PartyPutDTO partyPutDTO) {

        // updates the user identified by the given ID with the given data by the client
        Party partyToUpdate = DTOMapper.INSTANCE.convertPartyPutDTOtoEntity(partyPutDTO);
        partyService.editParty(userId, partyId, partyToUpdate);

    }

    // (a host) delete a party
    @DeleteMapping("/parties/{partyId}/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void deleteParty(@PathVariable("partyId") Long partyId,@PathVariable Long userId) {
        // delete party by the given userID from client
        partyService.deleteParty(userId, partyId);
    }

    // (an attendant) quit a party
    @PutMapping("/parties/quitting/{partyId}/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void quitParty(@PathVariable Long userId, @PathVariable Long partyId) {
        // delete party by the given userID from client
        partyService.quitParty(userId, partyId);
    }





}
