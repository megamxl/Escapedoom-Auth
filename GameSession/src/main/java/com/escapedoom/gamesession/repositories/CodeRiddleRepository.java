package com.escapedoom.gamesession.repositories;

import com.escapedoom.gamesession.data.codeCompiling.CodingLanguage;
import com.escapedoom.gamesession.data.codeCompiling.ConsoleNodeCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CodeRiddleRepository extends JpaRepository<ConsoleNodeCode, Long> {

    Optional<ConsoleNodeCode> findByLogicalIDAndLanguage(Long logicalId, CodingLanguage codingLanguage);

}
