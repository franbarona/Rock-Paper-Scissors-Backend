package com.rockpaperscissors.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rockpaperscissors.entity.GameMatch;

@Repository
public interface GameMatchRepository extends JpaRepository<GameMatch, Long> {
    List<GameMatch> findTop5ByUserIdOrderByPlayedAtDesc(Long userId);
}