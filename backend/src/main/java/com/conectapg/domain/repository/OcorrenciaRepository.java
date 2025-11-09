package com.conectapg.domain.repository;

import com.conectapg.domain.model.Ocorrencia;
import com.conectapg.domain.model.Ocorrencia.StatusOcorrencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OcorrenciaRepository extends JpaRepository<Ocorrencia, Long> {

    List<Ocorrencia> findByStatus(StatusOcorrencia status);

    List<Ocorrencia> findByUsuarioId(Long usuarioId);

    @Query("SELECT o FROM Ocorrencia o WHERE o.localizacao LIKE %:localizacao%")
    List<Ocorrencia> findByLocalizacaoContaining(@Param("localizacao") String localizacao);

    @Query("SELECT o FROM Ocorrencia o ORDER BY o.dataCriacao DESC")
    List<Ocorrencia> findAllOrderByDataCriacaoDesc();
}
