package no.itnow.location.service;

import lombok.RequiredArgsConstructor;
import no.itnow.location.entity.ActiveTripSession;
import no.itnow.location.entity.TripCurrentLocation;
import no.itnow.location.entity.TripLocationHistory;
import no.itnow.location.model.LocationRequest;
import no.itnow.location.model.LocationResponse;
import no.itnow.location.repository.TripCurrentLocationRepository;
import no.itnow.location.repository.TripLocationHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final TripCurrentLocationRepository tripCurrentLocationRepository;
    private final TripLocationHistoryRepository tripLocationHistoryRepository;
    private final SessionService sessionService;

    @Transactional
    public LocationResponse save(LocationRequest request) {
        ActiveTripSession session = sessionService.getActiveSessionEntity(request.sessionId());
        Instant now = Instant.now();

        TripCurrentLocation location = tripCurrentLocationRepository.findBySessionId(request.sessionId())
                .orElseGet(TripCurrentLocation::new);

        location.setSessionId(session.getSessionId());
        location.setDisplayName(session.getDisplayName());
        location.setDeviceId(session.getDeviceId());
        location.setLatitude(request.latitude());
        location.setLongitude(request.longitude());
        location.setSpeed(request.speed());
        location.setHeading(request.heading());
        location.setUpdatedAt(now);

        // Oppdaterer siste kjente punkt for turen.
        TripCurrentLocation savedCurrentLocation = tripCurrentLocationRepository.save(location);
        // Lagrer alle punkter fortløpende slik at ruten kan hentes senere.
        tripLocationHistoryRepository.save(toHistory(savedCurrentLocation));

        LocationResponse response = toResponse(savedCurrentLocation);
        sessionService.touch(session);
        return response;
    }

    public List<LocationResponse> getLatest() {
        return tripCurrentLocationRepository.findAllByOrderByUpdatedAtDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public LocationResponse getLatestBySessionId(String sessionId) {
        return tripCurrentLocationRepository.findBySessionId(sessionId)
                .map(this::toResponse)
                .orElse(null);
    }

    public List<LocationResponse> getHistoryBySessionId(String sessionId) {
        return tripLocationHistoryRepository.findAllBySessionIdOrderByUpdatedAtAsc(sessionId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private LocationResponse toResponse(TripCurrentLocation location) {
        return new LocationResponse(
                location.getId(),
                location.getSessionId(),
                location.getDisplayName(),
                location.getDeviceId(),
                location.getLatitude(),
                location.getLongitude(),
                location.getSpeed(),
                location.getHeading(),
                location.getUpdatedAt()
        );
    }

    private LocationResponse toResponse(TripLocationHistory location) {
        return new LocationResponse(
                location.getId(),
                location.getSessionId(),
                location.getDisplayName(),
                location.getDeviceId(),
                location.getLatitude(),
                location.getLongitude(),
                location.getSpeed(),
                location.getHeading(),
                location.getUpdatedAt()
        );
    }

    private TripLocationHistory toHistory(TripCurrentLocation location) {
        TripLocationHistory history = new TripLocationHistory();
        history.setSessionId(location.getSessionId());
        history.setDisplayName(location.getDisplayName());
        history.setDeviceId(location.getDeviceId());
        history.setLatitude(location.getLatitude());
        history.setLongitude(location.getLongitude());
        history.setSpeed(location.getSpeed());
        history.setHeading(location.getHeading());
        history.setUpdatedAt(location.getUpdatedAt());
        return history;
    }
}
