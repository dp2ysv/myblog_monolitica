package it.cgmconsulting.myblog.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreateUser2Request {

    @NotBlank
    private String email;
    @NotBlank
    private String username;
    @NotBlank
    private String password;

}
