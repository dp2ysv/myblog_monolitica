package it.cgmconsulting.myblog.controller;

import it.cgmconsulting.myblog.entity.User2;
import it.cgmconsulting.myblog.payload.request.CreateUser2Request;
import it.cgmconsulting.myblog.service.User2Service;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user2")

@RequiredArgsConstructor
public class User2Controller {

    private final User2Service user2Service;

    @GetMapping("/{userId}")
    public ResponseEntity<User2> getUser2(@PathVariable @Min(1) int userId){
        return user2Service.findById(userId);
    }


        // analizzare i dati (capire se i dati forniti sono corretti)
        // trasformare l'input del controller in un User2
        // persistere = salvare nel DB lo User2 creato
        // restituire lo user2 create al controller (all'interno di una response entity)
        @PostMapping("")
        public ResponseEntity<User2> createUser(@RequestBody @Valid CreateUser2Request createUser2Request){
            User2 newUser = user2Service.createUser(createUser2Request);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        }



}

