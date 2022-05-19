package ch.uzh.ifi.hase.soprafs22.websocket;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;



@Controller
@RestController
public class NewRecipeController {
    @MessageMapping("/recipeUpdating/fetch")
    @SendTo("/recipeUpdating/fetch")
    public String storeAndRedirectMessage() {
        return "message";
    }
}
