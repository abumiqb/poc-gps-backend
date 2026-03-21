package no.itnow.location.repository;

import no.itnow.location.entity.ActiveTripSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

// Lagrer aktive turer og lar oss slå opp på den offentlige sessionId-en.
public interface ActiveTripSessionRepository extends JpaRepository<ActiveTripSession, Long> {

    Optional<ActiveTripSession> findBySessionId(String sessionId);

    List<ActiveTripSession> findAllByActiveTrueOrderByStartedAtDesc();
}
