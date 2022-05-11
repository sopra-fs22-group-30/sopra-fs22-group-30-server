package ch.uzh.ifi.hase.soprafs22.websocket;


import ch.uzh.ifi.hase.soprafs22.websocket.dtoWS.ChecklistGetDTO;
import ch.uzh.ifi.hase.soprafs22.websocket.dtoWS.ChecklistMessageDTO;
import ch.uzh.ifi.hase.soprafs22.websocket.dtoWS.InvitationNotificationDTO;
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
public class InvitationController {

    private final InvitationService invitationService;

    @Autowired
    InvitationController(InvitationService invitationService) {
        this.invitationService = invitationService;
    }

    @MessageMapping("/invitation/fetch")
    @SendTo("/invitation/fetch")
    public InvitationNotificationDTO redirectMessage(Message<InvitationNotificationDTO> message) {
        return message.getPayload();
    }

    /*  stomp websocket mappings
    --------------------------------------------------------------------------------------------------
    rest mappings   */

//    @GetMapping("/invitation/party/{partyId}")
//    @ResponseStatus(HttpStatus.OK)
//    @ResponseBody
//    public List<ChecklistGetDTO> getAllCheckInParty(@PathVariable("partyId") Long partyId) {
//        return checklistService.getChecklistInParty(partyId);
//    }


}
