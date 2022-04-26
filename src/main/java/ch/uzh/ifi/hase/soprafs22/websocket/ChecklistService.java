//package ch.uzh.ifi.hase.soprafs22.websocket;
//
//import ch.uzh.ifi.hase.soprafs22.entity.User;
//import ch.uzh.ifi.hase.soprafs22.repository.IngredientRepository;
//import ch.uzh.ifi.hase.soprafs22.repository.PartyRepository;
//import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
//import ch.uzh.ifi.hase.soprafs22.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//@Transactional
//public class ChecklistService {
//
//    private final PartyRepository partyRepository;
//    private final IngredientRepository ingredientRepository;
//    private final UserRepository userRepository;
//
//    @Autowired
//    public ChecklistService(@Qualifier("partyRepository") PartyRepository partyRepository, @Qualifier("ingredientRepository") IngredientRepository ingredientRepository, UserService userService, UserRepository userRepository) {
//        this.partyRepository = partyRepository;
//        this.ingredientRepository = ingredientRepository;
//        this.userRepository = userRepository;
//    }
//
//    public ChecklistGetDTO storeAndConvert(Long partyId, ChecklistMessageDTO checklistMessageDTO) {
//        Long takerId = checklistMessageDTO.getTakerId();
//        ingredient
//        Optional<User> checkedUser = userRepository.findById(takerId);
//        if (checkedUser.isEmpty()) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
//        }
//        String takerName = checkedUser.get().getUsername();
//
//        ingredientRepository
//        return DTOMapper.INSTANCE.convertMessageToChatMessageDTO(message);
//    }
//
//    public List<ChecklistMessageDTO> getChecklistInParty(Long partyId) {
//        List<ChecklistMessageDTO> takers = groupRepository.findOrderedByTimestampLobbyMessages(lobbyId);
//        return takers.stream().map(DTOMapper.INSTANCE::convertMessageToChatMessageDTO).collect(Collectors.toList());
//    }
//}
