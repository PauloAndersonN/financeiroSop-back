package com.example.financasSop.service;

import com.example.financasSop.model.TipoOrcamento;
import com.example.financasSop.repository.TipoOrcamentoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TipoOrcamentoService implements CommandLineRunner {

    private final TipoOrcamentoRepository repository;

    public TipoOrcamentoService(TipoOrcamentoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        criarSeNaoExistir("Obra de Edificação");
        criarSeNaoExistir("Obra de Rodovias");
        criarSeNaoExistir("Outros");
    }

    private void criarSeNaoExistir(String nome) {
        repository.findByNome(nome).orElseGet(() -> {
            TipoOrcamento t = new TipoOrcamento();
            t.setNome(nome);
            return repository.save(t);
        });
    }

    public List<TipoOrcamento> listar() {
        return repository.findAll(Sort.by("nome").ascending());
    }
}

