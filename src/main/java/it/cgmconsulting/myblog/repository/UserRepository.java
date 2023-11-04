package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.Content;
import it.cgmconsulting.myblog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    boolean existsByEmailOrUsername(String email, String username);


  //  List<User> findAllByUsernameOrEmail(String username, String email);

}
