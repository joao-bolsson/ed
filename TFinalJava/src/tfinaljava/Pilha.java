/**
 * Classe que representa uma pilha de Strings.
 */
package tfinaljava;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author João Bolsson (jvmarques@inf.ufsm.br)
 * @since 16, 10 Dez.
 */
public class Pilha {

    private final List<String> rota;

    /**
     * Pilha de cidades.
     */
    public Pilha() {
        rota = new ArrayList<>();
    }

    /**
     * @param cidade Nome da cidade
     * @return Se existe uma cidade com o nome do parâmetro na pilha - true, else - false.
     */
    public boolean contains(final String cidade) {
        return rota.contains(cidade);
    }

    /**
     * Insere um novo nome de cidade na pilha.
     *
     * @param nomeCidade Nome da cidade.
     */
    public void insere(final String nomeCidade) {
        rota.add(nomeCidade);
    }

    /**
     * Desempilha o topo.
     */
    public void remove() {
        rota.remove(rota.size() - 1);
    }

    /**
     * Imprime a pilha.
     */
    public void imprime() {
        Iterator<String> iterator = rota.iterator();
        while (iterator.hasNext()) {
            String cidade = iterator.next();
            String seta = " -> ";
            if (!iterator.hasNext()) {
                seta = "";
            }
            System.out.print(cidade + seta);
        }
    }

    /**
     * Limpa a pilha.
     */
    public void esvazia() {
        rota.clear();
    }

    /**
     * @return Se a pilha está vazia - true, else - false.
     */
    public boolean isEmpty() {
        return rota.isEmpty();
    }

    /**
     * @return Tamanho da pilha.
     */
    public int size() {
        return rota.size();
    }

    /**
     * @return A lista usada na pilha.
     */
    public List<String> getPilha() {
        return rota;
    }

}
