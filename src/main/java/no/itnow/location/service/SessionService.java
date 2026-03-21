package no.itnow.location.service;

import lombok.RequiredArgsConstructor;
import no.itnow.location.entity.ActiveTripSession;
import no.itnow.location.model.SessionCreateRequest;
import no.itnow.location.model.SessionResponse;
import no.itnow.location.repository.ActiveTripSessionRepository;
import no.itnow.location.repository.TripCurrentLocationRepository;
import no.itnow.location.repository.TripLocationHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final ActiveTripSessionRepository activeTripSessionRepository;
    private final TripCurrentLocationRepository tripCurrentLocationRepository;
    private final TripLocationHistoryRepository tripLocationHistoryRepository;

    @Transactional
    public SessionResponse create(SessionCreateRequest request) {
        Instant now = Instant.now();
        ActiveTripSession session = new ActiveTripSession();
        session.setSessionId(UUID.randomUUID().toString());
        session.setDisplayName(StringUtils.capitalize(request.displayName().trim()));
        session.setDeviceId(request.deviceId());
        session.setActive(true);
        session.setStartedAt(now);
        session.setLastSeenAt(now);

        return toResponse(activeTripSessionRepository.save(session));
    }

    public List<SessionResponse> getActiveSessions() {
        return activeTripSessionRepository.findAllByActiveTrueOrderByStartedAtDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public SessionResponse endSession(String sessionId) {
        ActiveTripSession session = getActiveSessionEntity(sessionId);
        session.setActive(false);
        session.setEndedAt(Instant.now());
        session.setLastSeenAt(session.getEndedAt());
        // Fjerner live-markøren, men beholder historikken for senere visning av rute.
        tripCurrentLocationRepository.deleteBySessionId(sessionId);
        return toResponse(activeTripSessionRepository.save(session));
    }

    public ActiveTripSession getActiveSessionEntity(String sessionId) {
        ActiveTripSession session = activeTripSessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Session not found"));

        if (!session.isActive()) {
            throw new ResponseStatusException(NOT_FOUND, "Session is not active");
        }

        return session;
    }

    public void touch(ActiveTripSession session) {
        session.setLastSeenAt(Instant.now());
        activeTripSessionRepository.save(session);
    }

    @Transactional
    public void reset() {
        tripLocationHistoryRepository.deleteAll();
        tripCurrentLocationRepository.deleteAll();
        activeTripSessionRepository.deleteAll();
    }

    private SessionResponse toResponse(ActiveTripSession session) {
        return new SessionResponse(
                session.getId(),
                session.getSessionId(),
                session.getDisplayName(),
                session.getDeviceId(),
                session.isActive(),
                session.getStartedAt(),
                session.getLastSeenAt(),
                session.getEndedAt()
        );
    }
}
