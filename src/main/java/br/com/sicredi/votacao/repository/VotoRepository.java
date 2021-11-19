package br.com.sicredi.votacao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.sicredi.votacao.model.Voto;

@Repository
public interface VotoRepository extends JpaRepository<Voto, Long> {
	
	public List<Voto> findBySessaoPautaIdAndSessaoId(Long idPauta, Long idSessao);

	public Optional<Voto> findBySessaoPautaIdAndSessaoIdAndId(Long idPauta, Long idSessao, Long id);

	public Optional<Voto> findBySessaoPautaIdAndSessaoIdAndCpfAssociado(Long idPauta, Long idSessao, String cpfAssociado);

}
