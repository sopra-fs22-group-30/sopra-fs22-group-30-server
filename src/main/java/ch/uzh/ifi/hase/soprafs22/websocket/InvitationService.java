package ch.uzh.ifi.hase.soprafs22.websocket;

import ch.uzh.ifi.hase.soprafs22.repository.IngredientRepository;
import ch.uzh.ifi.hase.soprafs22.repository.PartyRepository;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs22.service.UserService;
import ch.uzh.ifi.hase.soprafs22.websocket.dtoWS.InvitationNameListDTO;
import ch.uzh.ifi.hase.soprafs22.websocket.dtoWS.InvitationNotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class InvitationService {

    private final PartyRepository partyRepository;
    private final IngredientRepository ingredientRepository;
    private final UserRepository userRepository;

    @Autowired
    public InvitationService(@Qualifier("partyRepository") PartyRepository partyRepository, @Qualifier("ingredientRepository") IngredientRepository ingredientRepository, UserService userService, UserRepository userRepository) {
        this.partyRepository = partyRepository;
        this.ingredientRepository = ingredientRepository;
        this.userRepository = userRepository;
    }

    public InvitationNotificationDTO prepareNotification(Message<InvitationNameListDTO> message){
        //search information
        Long partyId = message.getPayload().getPartyId();
        String partyName = partyRepository.findById(partyId).get().getPartyName();
        Long hostId = partyRepository.findById(partyId).get().getPartyHostId();
        String hostName = userRepository.findById(hostId).get().getUsername();
        // form the notification
        InvitationNotificationDTO notification = new InvitationNotificationDTO();
        notification.setPartyId(partyId);
        notification.setPartyName(partyName);
        notification.setHostName(hostName);
        return notification;
    }

    public List<Long> getUserId(Message<InvitationNameListDTO> message){
        List<Long> userIdList = null;
        List<String> partyAttendantsList=message.getPayload().getPartyAttendantsList();
        for(String username : partyAttendantsList){
            userIdList.add(userRepository.findByUsername(username).getId());
        }
        return userIdList;
    }

    public Long getHostId(Message<InvitationNameListDTO> message){
        return partyRepository.findById(message.getPayload().getPartyId()).get().getPartyHostId();
    }
}
