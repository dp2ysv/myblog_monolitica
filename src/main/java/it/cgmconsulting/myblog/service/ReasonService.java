package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.Post;
import it.cgmconsulting.myblog.entity.Reason;
import it.cgmconsulting.myblog.entity.ReasonId;
import it.cgmconsulting.myblog.exception.ResourceNotFoundException;
import it.cgmconsulting.myblog.payload.request.ReasonRequest;
import it.cgmconsulting.myblog.repository.ReasonRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReasonService {

    private final ReasonRepository reasonRepository;

    public ResponseEntity<?> addReason(ReasonRequest request) {
        String reasonName  = request.getReason().toLowerCase();
        if(reasonRepository.existsByReasonIdReason(reasonName))
            return new ResponseEntity("Reason already present", HttpStatus.BAD_REQUEST);
        ReasonId raesonId = new ReasonId(reasonName, request.getStartDate());
        Reason reason = new Reason(raesonId, request.getSeverity());
        reasonRepository.save(reason);
        return new ResponseEntity("New Reason added", HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<?> updateReason(ReasonRequest request) {
        String reasonName = request.getReason().toLowerCase();
        //recupero la reason da aggiornare
        Reason oldReason= getValidReason(reasonName);
        // istanzio la nuova reason
        Reason newReason= new Reason(new ReasonId(reasonName, request.getStartDate()), request.getSeverity());
        // verifico che la severity dela vecchia reason NON coincida con quella della nuova reaason altrimenti non avrebbe senso aggiornare
        if(oldReason.getSeverity() == newReason.getSeverity())
            return new ResponseEntity<>("Same severity", HttpStatus.BAD_REQUEST);
        // setto l'endDate dell vecchia reason al giorno prima rispetto allo starDate della request
        oldReason.setEndDate(request.getStartDate().minusDays(1));
        // salvo la nuova reason (la vecchia reason viene aggoirnata grazie al @Transactional)
        reasonRepository.save(newReason);
        return new ResponseEntity<>("Reason updated", HttpStatus.OK);
    }

    public ResponseEntity<?> getValidReasons(){
        List<Reason> reasons = reasonRepository.findByEndDateIsNullOrderBySeverityDesc();
        List<String> validReasons = reasons.stream().map(r -> r.getReasonId().getReason()).collect(Collectors.toList());
        return new ResponseEntity(validReasons, HttpStatus.OK);
    }

    protected Reason getValidReason(String reason) {
        return reasonRepository.getValidReason(reason)
                .orElseThrow(() -> new ResourceNotFoundException("Reason", "reason", reason));
    }

    @Transactional
    public ResponseEntity<?> removeReason(String reason) {
        Reason r = getValidReason(reason);
        r.setEndDate(LocalDate.now());
        return new ResponseEntity("Reason "+reason+" no more valid", HttpStatus.OK);
    }
}
