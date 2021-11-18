package br.com.sicredi.votacao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.sicredi.votacao.model.Sessao;

@Repository
public interface SessaoRepository extends JpaRepository<Sessao, Long> {
	
	public List<Sessao> findByPautaId(Long idPauta);

	public Optional<Sessao> findByIdAndPautaId(Long id, Long idPauta);
}
