package no.itnow.location.repository;

import no.itnow.location.entity.TripCurrentLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

// Lagrer bare siste kjente posisjon for hver aktive tur.
public interface TripCurrentLocationRepository extends JpaRepository<TripCurrentLocation, Long> {

    Optional<TripCurrentLocation> findBySessionId(String sessionId);

    List<TripCurrentLocation> findAllByOrderByUpdatedAtDesc();

    void deleteBySessionId(String sessionId);
}
