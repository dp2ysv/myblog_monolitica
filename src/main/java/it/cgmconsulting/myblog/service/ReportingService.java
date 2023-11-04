package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.*;
import it.cgmconsulting.myblog.entity.common.ReportingStatus;
import it.cgmconsulting.myblog.exception.ResourceNotFoundException;
import it.cgmconsulting.myblog.payload.request.ReportingCloseRequest;
import it.cgmconsulting.myblog.payload.request.ReportingRequest;
import it.cgmconsulting.myblog.payload.request.ReportingUpdateRequest;
import it.cgmconsulting.myblog.payload.response.ReportingCheckBannedUserResponse;
import it.cgmconsulting.myblog.repository.CommentRepository;
import it.cgmconsulting.myblog.repository.ReportingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReportingService {

    private final ReportingRepository reportingRepository;
    private final CommentRepository commentRepository;
    private final ReasonService reasonService;

    public ResponseEntity<?> createReporting(ReportingRequest request, UserDetails userDetails){
        // Prima di salvare una segnalazione verificare che non esista un'altra segnalazione per quel commento
        User user = (User) userDetails;
        Comment c = getCommentById(request.getCommentId());
        if(user.getId() == c.getAuthor().getId())
            return new ResponseEntity("You cannot report yourself !", HttpStatus.BAD_REQUEST);
        ReportingId reportingId = new ReportingId(c);
        if(reportingRepository.existsById(reportingId))
            return new ResponseEntity("Reporting for comment "+c.getId()+" already present", HttpStatus.BAD_REQUEST);
        // Creazione della segnalazione
        Reason reason = reasonService.getValidReason(request.getReason());
        Reporting rep = new Reporting(reportingId, reason, user);
        reportingRepository.save(rep);
        return new ResponseEntity("Comment "+rep.getReportingId().getComment().getId()+" has been reported", HttpStatus.CREATED);
    }


    @Transactional
    public ResponseEntity<?> updateReporting(ReportingUpdateRequest request) {
        Comment c = getCommentById(request.getCommentId());
        Reporting rep = getReportingById(new ReportingId(c));
        if(request.getReason() != null && !request.getReason().isBlank() && !rep.getReason().getReasonId().getReason().equals(request.getReason().toLowerCase())){
            Reason reason = reasonService.getValidReason(request.getReason());
            rep.setReason(reason);
        }
        rep.setNote(request.getNote());
        rep.setStatus(ReportingStatus.IN_PROGRESS);

        return new ResponseEntity("Reporting is now in progress status", HttpStatus.OK);

    }

    @Transactional
    public ResponseEntity<?> closeReporting(ReportingCloseRequest request){
        if(!ReportingStatus.valueOf(request.getStatus()).equals(ReportingStatus.CLOSED_WITH_BAN) &&
                !ReportingStatus.valueOf(request.getStatus()).equals(ReportingStatus.CLOSED_WITHOUT_BAN))
            return new ResponseEntity("Incorrect Status", HttpStatus.BAD_REQUEST);
        Comment c = getCommentById(request.getCommentId());
        Reporting rep = getReportingById(new ReportingId(c));
        // Su reporting fare settare lo  status IN_PROGRESS -> CLOSED_WITH_BAN oppure CLOSED_WITHOUT_BAN
        // Se chiudo con CLOSED_WITH_BAN:
        //      1) devo censurare il commento
        //      2) disabiltare l'utente
        if(ReportingStatus.valueOf(request.getStatus()).equals(ReportingStatus.CLOSED_WITH_BAN)){
            c.setCensored(true);
            c.getAuthor().setEnabled(false);
        }
        // Che la segnalazione sia chiusa con o senza ban aggiornarla col nuovo Status
        rep.setStatus(ReportingStatus.valueOf(request.getStatus()));
        rep.setNote(request.getNote());
        // salvare il tutto
        return new ResponseEntity("Reporting has benn closed", HttpStatus.OK);
    }

    public LocalDateTime checkUser(User user){
        // verificare tra tutti i reporting chiusi con ban se lo user autore del commento esiste
        List<ReportingCheckBannedUserResponse> list = reportingRepository.checkBannedUser(user.getId(), ReportingStatus.CLOSED_WITH_BAN);
        LocalDateTime createdAtOlder = null;
        LocalDateTime bannedUntil;
        int severities = 0;
        for(ReportingCheckBannedUserResponse r : list){
            // se sì, recuperare la severity della reason per la quale è stato bannato
            // sommare la severity alla data di creazione della segnalazione
            bannedUntil = r.getCreatedAt().plusDays(r.getSeverity());
            if(bannedUntil.isAfter(LocalDateTime.now())) {
                severities += r.getSeverity();
                if(createdAtOlder == null || createdAtOlder.isAfter(r.getCreatedAt()))
                    createdAtOlder = r.getCreatedAt();
            }
        }
        if(createdAtOlder != null)
            createdAtOlder = createdAtOlder.plusDays(severities);
        return createdAtOlder;
    }



    protected Reporting getReportingById(ReportingId id) {
        return reportingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reporting", "comment", id));
    }

    protected Comment getCommentById(int id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "comment", id));
    }


}
