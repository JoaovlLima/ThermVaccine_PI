package com.thermvaccine.service;

import java.time.LocalDateTime;
import java.util.List;
import com.thermvaccine.model.Lote;
import com.thermvaccine.model.Usuario;
import com.thermvaccine.model.Vacina;
import com.thermvaccine.repository.LoteRepository;

public class LoteService {

    private final LoteRepository loteRepository;

    public LoteService() {
        this.loteRepository = new LoteRepository();
    }

    public void exibirDados(Lote lote){
        //System.out.printf("ID: %d\n", this.lote.getId());
        System.out.printf("Fabricante: %s\n", lote.getFabricante());
        System.out.printf("Validade: %s\n", lote.getValidade());
        System.out.printf("Usuario: %s\n", lote.getUsuario().getNome());
        System.out.printf("Vacina: %s\n", lote.getVacina().getNome());
        //talvez pegar apenas o nome do usuario e vacina, em vez de printar eles inteiramente?

    }

    // CREATE
    public void criar(int quantidade, String fabricante, LocalDateTime validade, LocalDateTime data_descongelamento,
         Usuario usuario, Vacina vacina) {
        Lote lote = new Lote(quantidade, fabricante, validade, data_descongelamento, usuario, vacina);

        List<Lote> lotes = loteRepository.listar();
        lotes.add(lote);
        loteRepository.salvar(lotes);
    }

    // READ ALL
    public List<Lote> listar() {
        return loteRepository.listar();
    }

    // READ BY VACINA
    public List<Lote> listarPorVacina(String idVacina) {
        return loteRepository.listar().stream()
                .filter(l -> l.getVacina().getId().equals(idVacina))
                .toList();
    }

    // UPDATE - somente quantidade
    public void atualizarQuantidade(String id, int novaQuantidade) {
        Lote lote = loteRepository.findById(id);

        if (lote == null) {
            System.out.println("Lote não encontrado.");
            return;
        }

        lote.setQuantidade(novaQuantidade);
        loteRepository.editar(lote);
    }

}