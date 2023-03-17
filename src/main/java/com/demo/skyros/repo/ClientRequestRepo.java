package com.demo.skyros.repo;

import com.demo.skyros.entity.ClientRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRequestRepo extends JpaRepository<ClientRequestEntity, Long> {
}
