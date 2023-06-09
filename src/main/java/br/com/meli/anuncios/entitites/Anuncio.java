package br.com.meli.anuncios.entitites;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Anuncio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private double precoBase;

    private double precoPromocional;

    public Anuncio() {

    }

    public Anuncio(Long id, String name, double precoBase, double precoPromocional) {
    }

    //private List<Visualizacao> visualizacoes;
}
