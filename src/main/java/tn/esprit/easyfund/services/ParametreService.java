package tn.esprit.easyfund.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.easyfund.repositories.IOfferRepositories;
import tn.esprit.easyfund.repositories.IparametreRepository;

@AllArgsConstructor
@Service
public class ParametreService {
    private final IparametreRepository iparametreRepository;
    private final IOfferRepositories offerRepositories;




}
