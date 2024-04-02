package tn.esprit.easyfund.services;

import org.springframework.stereotype.Service;
import tn.esprit.easyfund.entities.TransactionType;

import java.util.Map;
@Service
public interface TransactionAnalysisService {
    Map<String, Object> getTransactionTypeDistribution(Long accountId);

    Map<String, Object> exploreTransactionRelationships();
}
