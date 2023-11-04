package it.cgmconsulting.myblog.entity;

import it.cgmconsulting.myblog.entity.common.CreationUpdate;
import it.cgmconsulting.myblog.entity.common.ReportingStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


import java.util.Objects;

@Entity
@Getter @Setter @NoArgsConstructor @ToString
public class Reporting extends CreationUpdate {

    @EmbeddedId
    private ReportingId reportingId;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "reason", referencedColumnName = "reason", nullable = false),
            @JoinColumn(name = "start_date", referencedColumnName = "startDate", nullable = false)
    })
    private Reason reason;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // colui che segnala un commento

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 18)
    private ReportingStatus status = ReportingStatus.OPEN;

    private String note; // ad uso e consumo del moderatore

    public Reporting(ReportingId reportingId, Reason reason, User user) {
        this.reportingId = reportingId;
        this.reason = reason;
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reporting reporting = (Reporting) o;
        return Objects.equals(reportingId, reporting.reportingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reportingId);
    }
}