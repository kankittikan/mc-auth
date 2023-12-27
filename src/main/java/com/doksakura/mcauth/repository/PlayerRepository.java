package com.doksakura.mcauth.repository;

import com.doksakura.mcauth.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, String> {
    Player findByEmail(String email);
}