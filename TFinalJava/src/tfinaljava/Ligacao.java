/**
 * Classe que representa uma ligação entre cidades.
 */
package tfinaljava;

/**
 *
 * @author João Bolsson (jvmarques@inf.ufsm.br)
 * @since 16, 10 Dez.
 */
public class Ligacao {

    private final Cidade origem, destino;
    private final int distancia;

    /**
     * Uma ligação entre uma cidade de origem e um destino.
     *
     * @param origem Cidade de origem.
     * @param destino Cidade de destino.
     * @param distancia Distância entre a origem e o destino.
     */
    public Ligacao(final Cidade origem, final Cidade destino, final int distancia) {
        this.origem = origem;
        this.destino = destino;
        this.distancia = distancia;
    }

    /**
     * @return Cidade de origem da ligação.
     */
    public Cidade getOrigem() {
        return origem;
    }

    /**
     * @return Cidade de destino da ligação.
     */
    public Cidade getDestino() {
        return destino;
    }

    /**
     * @return Distância da ligação.
     */
    public int getDistancia() {
        return distancia;
    }
}
