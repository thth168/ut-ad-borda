package utadborda.application.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utadborda.application.Entities.OpeningHours;
import utadborda.application.services.DAO.OpeningHoursRepo;
import utadborda.application.services.OpeningHoursService;

@Service
public class OpeningHoursImpl implements OpeningHoursService {
    private OpeningHoursRepo openingHoursRepo;
    @Autowired
    public OpeningHoursImpl(
        OpeningHoursRepo openingHoursRepo
    ){
        this.openingHoursRepo = openingHoursRepo;
    }

    @Override
    public OpeningHours addOpeningHours(OpeningHours openingHours) {
        return openingHoursRepo.save(openingHours);
    }
}
