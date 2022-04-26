//package ch.uzh.ifi.hase.soprafs22.websocket;
//
//
//import org.hibernate.annotations.Check;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.util.HtmlUtils;
//
//import java.util.List;
//import java.util.UUID;
//
//
//@Controller
//@RestController
//public class ChecklistController {
//
//    private final SimpMessagingTemplate simpMessagingTemplate;
//    private final ChecklistService checklistService;
//
//    @Autowired
//    ChecklistController(ChecklistService checklistService, SimpMessagingTemplate simpMessagingTemplate) {
//        this.checklistService = checklistService;
//        this.simpMessagingTemplate = simpMessagingTemplate;
//    }
//
//    @MessageMapping("/checklist/{partyId}/incoming")
//    //@SendTo("checklist/{partyId}/outgoing")
//    public void storeAndRedirectMessage(@PathVariable("partyId") Long partyId,ChecklistMessageDTO checklistMessageDTO) {
//        ChecklistGetDTO checklistGetDTO = checklistService.storeAndConvert(partyId,checklistMessageDTO);
//        simpMessagingTemplate.convertAndSend("/checklist/{partyId}/outgoing/" , checklistGetDTO);
//    }
//
//
///*  stomp websocket mappings
//    --------------------------------------------------------------------------------------------------
//    rest mappings   */
//
//
//
//    @GetMapping("/checklist/party/{partyId}")
//    @ResponseStatus(HttpStatus.OK)
//    @ResponseBody
//    public List<ChecklistGetDTO> getAllMessagesSentInLobby(@PathVariable("partyId") Long partyId) {
//        return checklistService.getChecklistInParty(partyId);
//    }
//
//}
