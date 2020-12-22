package org.mkgroup.zaga.authorizationservice.repository;

import org.mkgroup.zaga.authorizationservice.model.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingRepository extends JpaRepository<Setting, Long>{

}
