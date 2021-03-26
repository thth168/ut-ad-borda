package utadborda.application.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utadborda.application.Entities.Matching_Session;
import utadborda.application.Entities.UAB_User;
import utadborda.application.services.DAO.MatchRepo;
import utadborda.application.services.DAO.MatchingSessionRepo;
import utadborda.application.services.MatchSessionService;

import java.util.List;

@Service
public class MatchSessionServiceImpl implements MatchSessionService {
    private MatchingSessionRepo matchingSessionRepo;
    private MatchRepo matchRepo;

    @Autowired
    public MatchSessionServiceImpl(
        MatchingSessionRepo matchingSessionRepo,
        MatchRepo matchRepo
    ) {
        this.matchingSessionRepo = matchingSessionRepo;
        this.matchRepo = matchRepo;
    }

    @Override
    public List<UAB_User> listUsers() {
        Matching_Session matching_session = matchingSessionRepo.getFirstByIdNotNull();
        return matching_session.getUsers();
    }
}
