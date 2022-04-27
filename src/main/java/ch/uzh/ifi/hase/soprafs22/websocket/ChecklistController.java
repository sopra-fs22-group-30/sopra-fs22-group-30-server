package ch.uzh.ifi.hase.soprafs22.websocket;


import org.hibernate.annotations.Check;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.util.List;
import java.util.UUID;


@Controller
@RestController
public class ChecklistController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChecklistService checklistService;

    @Autowired
    ChecklistController(ChecklistService checklistService, SimpMessagingTemplate simpMessagingTemplate) {
        this.checklistService = checklistService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/checklist/{partyId}/fetch")
    @SendTo("/checklist/{partyId}/fetch")
    public String storeAndRedirectMessage(@DestinationVariable("partyId") Long partyId, Message<ChecklistMessageDTO> message) {
        ChecklistMessageDTO checklistMessageDTO = message.getPayload();
        ChecklistGetDTO checklistGetDTO = checklistService.storeAndConvert(102L,checklistMessageDTO);
        //simpMessagingTemplate.convertAndSend("/checklist/outgoing", checklistGetDTO);
        //simpMessagingTemplate.convertAndSend("/checklist/outgoing/"+checklistGetDTO.getPartyId().toString() , checklistGetDTO);
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
