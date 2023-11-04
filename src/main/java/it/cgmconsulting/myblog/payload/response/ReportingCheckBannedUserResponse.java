package it.cgmconsulting.myblog.payload.response;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data @NotBlank @AllArgsConstructor
public class ReportingCheckBannedUserResponse {

    private LocalDateTime createdAt;
    private int severity;
}
