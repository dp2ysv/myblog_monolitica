package it.cgmconsulting.myblog.service;

import it.cgmconsulting.myblog.entity.User2;
import it.cgmconsulting.myblog.payload.request.CreateUser2Request;
import it.cgmconsulting.myblog.repository.User2Repository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class User2Service {

    private final User2Repository user2Repository;

    public ResponseEntity<User2> findById(int userId) {
        User2 user2 = user2Repository.findById(userId);
        return new ResponseEntity<>(user2, HttpStatus.OK);
    }

    public User2 createUser(@Valid CreateUser2Request user2){
        return user2Repository.save(fromRequestToUser(user2));
    }
    public User2 fromRequestToUser(CreateUser2Request request) {
        User2 user = new User2();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        return user;
    }


}
