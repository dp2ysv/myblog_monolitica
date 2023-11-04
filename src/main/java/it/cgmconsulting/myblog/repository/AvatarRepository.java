package it.cgmconsulting.myblog.repository;

import it.cgmconsulting.myblog.entity.Avatar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvatarRepository extends JpaRepository<Avatar, Integer> {
}
