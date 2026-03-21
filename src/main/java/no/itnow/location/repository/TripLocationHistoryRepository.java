package no.itnow.location.repository;

import no.itnow.location.entity.TripLocationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Lagrer alle posisjonspunkter for en tur slik at rute og historikk kan vises senere.
public interface TripLocationHistoryRepository extends JpaRepository<TripLocationHistory, Long> {

    List<TripLocationHistory> findAllBySessionIdOrderByUpdatedAtAsc(String sessionId);
}
