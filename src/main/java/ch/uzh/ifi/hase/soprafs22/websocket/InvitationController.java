package ch.uzh.ifi.hase.soprafs22.websocket;


import ch.uzh.ifi.hase.soprafs22.websocket.dtoWS.ChecklistGetDTO;
import ch.uzh.ifi.hase.soprafs22.websocket.dtoWS.ChecklistMessageDTO;
import ch.uzh.ifi.hase.soprafs22.websocket.dtoWS.InvitationNameListDTO;
import ch.uzh.ifi.hase.soprafs22.websocket.dtoWS.InvitationNotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RestController
public class InvitationController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final InvitationService invitationService;

    @Autowired
    InvitationController(InvitationService invitationService, SimpMessagingTemplate simpMessagingTemplate) {
        this.invitationService = invitationService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }


    //Version 1: original version
    @MessageMapping("/invitation/fetch")
    public void invitationNotification(Message<InvitationNameListDTO> message) {
        InvitationNotificationDTO notification = invitationService.prepareNotification(message);
        List<Long> userIdList = invitationService.getUserId(message);
        for (Long userId: userIdList){
            simpMessagingTemplate.convertAndSend("/invitation/"+userId+"/fetch",notification);
        }

    }


//    //Version 2: try SendTo
//    @MessageMapping("/invitation/fetch")
//    @SendTo("/invitation/fetch")
//    public InvitationNotificationDTO invitationNotification(Message<InvitationNameListDTO> message) {
//        InvitationNotificationDTO notification = invitationService.prepareNotification(message);
//        return notification;
//    }

//    //Version 3:  only sent to host
//    @MessageMapping("/invitation/fetch")
//    public void invitationNotification(Message<InvitationNameListDTO> message) {
//        InvitationNotificationDTO notification = invitationService.prepareNotification(message);
//        Long hostId = invitationService.getHostId(message);
//        simpMessagingTemplate.convertAndSend("/invitation/"+hostId+"/fetch",notification);
//    }

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
