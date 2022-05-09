package ch.uzh.ifi.hase.soprafs22.websocket;


import ch.uzh.ifi.hase.soprafs22.websocket.dtoWS.ChecklistGetDTO;
import ch.uzh.ifi.hase.soprafs22.websocket.dtoWS.ChecklistMessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RestController
public class ChecklistController {

    private final ChecklistService checklistService;

    @Autowired
    ChecklistController(ChecklistService checklistService) {
        this.checklistService = checklistService;
    }

    @MessageMapping("/checklist/{partyId}/fetch")
    @SendTo("/checklist/{partyId}/fetch")
    public String storeAndRedirectMessage(@DestinationVariable("partyId") Long partyId, Message<ChecklistMessageDTO> message) {
        ChecklistMessageDTO checklistMessageDTO = message.getPayload();
        checklistService.storeAndConvert(partyId,checklistMessageDTO);
        return "fetch";
    }

/*  stomp websocket mappings
    --------------------------------------------------------------------------------------------------
    rest mappings   */

    @GetMapping("/checklist/party/{partyId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<ChecklistGetDTO> getAllCheckInParty(@PathVariable("partyId") Long partyId) {
        return checklistService.getChecklistInParty(partyId);
    }

}
