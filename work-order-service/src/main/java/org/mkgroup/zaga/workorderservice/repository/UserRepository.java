package org.mkgroup.zaga.workorderservice.repository;

import java.util.UUID;

import org.mkgroup.zaga.workorderservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

}

