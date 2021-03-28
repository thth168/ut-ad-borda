package utadborda.application.RestControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import utadborda.application.Entities.Restaurant;
import utadborda.application.services.DAO.MatchingSessionRepo;
import utadborda.application.services.MatchSessionService;
import utadborda.application.services.RestaurantService;

import java.util.List;

@RestController
public class MatchController {
    MatchSessionService matchSessionService;

    @Autowired
    MatchController(
        MatchSessionService matchSessionService
    ) {
        this.matchSessionService = matchSessionService;
    }
}
