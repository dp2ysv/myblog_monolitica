package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.User2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface User2Repository extends JpaRepository<User2, Integer> {
    User2 findById(int id);


}
