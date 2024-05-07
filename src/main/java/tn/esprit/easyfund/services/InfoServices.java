package tn.esprit.easyfund.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.easyfund.repositories.IInfoRepository;

@Service
@AllArgsConstructor
public class InfoServices {
    private final IInfoRepository infoRepository;
}
