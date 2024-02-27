package tn.esprit.easyfund.services.interfaces;

import tn.esprit.easyfund.entities.MicroCredit;

import java.util.List;


public interface IMicroCreditServices{

 public MicroCredit addCredit(MicroCredit u);
 public List<MicroCredit> findAllCredits();
 public MicroCredit findCreditById(long id);
 void archiveCredit(long id);
 public MicroCredit updateCredit(MicroCredit c);

 public MicroCredit updateStatus(long id);

}
