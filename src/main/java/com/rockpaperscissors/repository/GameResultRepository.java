package com.rockpaperscissors.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rockpaperscissors.entity.GameResult;

@Repository
public interface GameResultRepository extends JpaRepository<GameResult, Long> {
    List<GameResult> findByUserIdOrderByPlayedAtDesc(Long userId);
}