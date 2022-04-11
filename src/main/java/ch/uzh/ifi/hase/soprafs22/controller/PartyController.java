package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.entity.Party;
import ch.uzh.ifi.hase.soprafs22.entity.Recipe;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.rest.dto.party.PartyGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.party.PartyPostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.recipe.RecipeGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.recipe.RecipePostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.recipe.RecipePutDTO;
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




}
