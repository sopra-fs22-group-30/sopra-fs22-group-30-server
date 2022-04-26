//package ch.uzh.ifi.hase.soprafs22.repository;
//
//import ch.uzh.ifi.hase.soprafs22.entity.Party;
//import ch.uzh.ifi.hase.soprafs22.entity.Recipe;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//
//@DataJpaTest
//public class PartyRepositoryIntegrationTest {
//
//  @Autowired
//  private TestEntityManager entityManager;
//
//  @Autowired
//  private PartyRepository partyRepository;
//
//  @Test
//  public void findAll() {
//        // given
//        Party party1 = new Party();
//        party1.setPartyId(1L);
//        party1.setPartyHostId(1l);
//        party1.setPartyName("p1");
//        party1.setRecipeUsedId(1L);
//
//        Party party2 = new Party();
//        party2.setPartyId(2L);
//        party2.setPartyHostId(2l);
//        party2.setPartyName("p2");
//        party2.setRecipeUsedId(1l);
//
//        entityManager.persist(party1);
//        entityManager.flush();
//        entityManager.persist(party2);
//        entityManager.flush();
//
//        // when
//        List<Party> found = partyRepository.findAll();
//
//        // then
//        assertNotNull(found.get(0).getPartyId());
//        assertEquals(found.get(0).getPartyId(), party1.getPartyId());
//        assertEquals(found.get(0).getPartyName(), party1.getPartyName());
//
//        assertNotNull(found.get(1).getPartyId());
//        assertEquals(found.get(1).getPartyId(), party2.getPartyId());
//        assertEquals(found.get(1).getPartyName(), party2.getPartyName());
//
//
//
//  }
//
//    @Test
//    public void findByID_success() {
//        // given
//        Party party = new Party();
//        party.setPartyName("firstname@lastname");
//        party.setPartyHostId(1l);
//        party.setRecipeUsedId(1l);
//        party.setPartyId(1L);
//
//        entityManager.merge(party);
//        entityManager.flush();
//
//        // when
//        Optional<Party> checkParty = partyRepository.findById(party.getPartyId());
//        Party found = null;
//        if (checkParty.isPresent()) {
//            found = checkParty.get();
//        }
//        // then
//        assert found != null;
//        assertNotNull(found.getPartyId());
//        assertEquals(found.getPartyName(), party.getPartyName());
//    }
//}
