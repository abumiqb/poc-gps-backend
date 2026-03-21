package no.itnow.location.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tracking_sessions",
        uniqueConstraints = @UniqueConstraint(name = "uk_tracking_sessions_session_id", columnNames = "session_id"))
public class ActiveTripSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session_id", nullable = false, length = 36)
    private String sessionId;

    @Column(name = "display_name", nullable = false, length = 100)
    private String displayName;

    @Column(name = "device_id", length = 100)
    private String deviceId;

    @Column(nullable = false)
    private boolean active;

    @Column(name = "started_at", nullable = false)
    private Instant startedAt;

    @Column(name = "last_seen_at", nullable = false)
    private Instant lastSeenAt;

    @Column(name = "ended_at")
    private Instant endedAt;
}
