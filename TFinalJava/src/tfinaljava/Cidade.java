/**
 * Classe que representa uma cidade.
 */
package tfinaljava;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author João Bolsson (jvmarques@inf.ufsm.br)
 * @since 16, 10 Dez.
 */
public class Cidade {

    private final String nome;
    private final List<Cidade> destinos;

    /**
     * Uma cidade com nome e com uma lista de destinos diretos (saídas).
     *
     * @param nome Nome da cidade.
     */
    public Cidade(final String nome) {
        this.nome = nome;
        this.destinos = new ArrayList<>();
    }

    /**
     * Insere um novo destino para a cidade (uma nova saída).
     *
     * @param destino Cidade de destino.
     */
    public void insereDestino(final Cidade destino) {
        destinos.add(destino);
    }

    @Override
    public String toString() {
        return nome;
    }

    /**
     * @return Lista de destinos da cidade.
     */
    public List<Cidade> getDestinos() {
        return destinos;
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof Cidade)) { // o objeto nao eh uma cidade
            return false;
        }

        if (obj == this) { // o objeto eh exatamente igual a este
            return true;
        }

        if (obj.toString().equals(nome)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 47;
        hash = 90 * hash + Objects.hashCode(nome);
        return hash;
    }
}
