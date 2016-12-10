/**
 * Classe que representa um mapa de cidades.
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
public class Mapa {

    // cada ligação eh uma linha do arquivo
    private final List<Ligacao> ligacoes;
    // cidades origem
    private final List<Cidade> cidades;
    // pilha que armazena as rotas possiveis para cada busca
    private final Pilha pilha;
    // número de cidades intermediárias e a distância máxima desejada pelo usuario em cada busca.
    private int num_intermed, distancia;
    // flag usada para personalizar a saída da busca quando não existe rota com os parâmtros do usuário.
    private boolean existeRota;

    /**
     * Objeto formado por ligações entre cidades.
     */
    public Mapa() {
        ligacoes = new ArrayList<>();
        cidades = new ArrayList<>();
        pilha = new Pilha();
    }

    /**
     * Função utilizada para setar os parametros que devem ser usados na condição de saída do trabalho descrito.
     *
     * @param num_intermed Número de cidades intermediárias informado pelo usuário.
     * @param distancia Distância máxima informada pelo usuário.
     */
    public void setParametros(final int num_intermed, final int distancia) {
        limpaPilha();
        existeRota = false;
        this.num_intermed = num_intermed;
        this.distancia = distancia;
    }

    public boolean existeRota() {
        return existeRota;
    }

    /**
     * Função que esvazia a pilha.
     */
    public void limpaPilha() {
        pilha.esvazia();
    }

    /**
     * @return A distância total de uma rota.
     */
    public int getDistancia() {
        List<String> rota = pilha.getPilha();
        String origem, destino;
        int distanciaRota = 0, size = rota.size();
        for (int i = 0; i < size - 1; i++) {
            origem = rota.get(i);
            destino = rota.get(i + 1);
            distanciaRota += getLigacao(origem, destino).getDistancia();
        }
        return distanciaRota;
    }

    /**
     * @param origem Cidade origem da ligação.
     * @param destino Cidade destino da ligação.
     * @return A ligação do mapa entre a origem e o destino.
     */
    public Ligacao getLigacao(final String origem, final String destino) {
        Iterator<Ligacao> iterator = ligacoes.iterator();
        while (iterator.hasNext()) {
            Ligacao ligacao = iterator.next();
            if (ligacao.getOrigem().toString().equals(origem)
                    && ligacao.getDestino().toString().equals(destino)) {
                return ligacao;
            }
        }
        return null;
    }

    /**
     * Função utilizada para encontrar todas as rotas entre a origem e o destino.
     *
     * @param origem Cidade de início das rotas.
     * @param destino Cidade destino das rotas.
     */
    public void busca(final Cidade origem, final Cidade destino) {
        if (origem.equals(destino)) {
            if (!pilha.contains(origem.toString())) {
                pilha.insere(origem.toString());
            }
            int distanciaRota = getDistancia();
            // o trecho deve conter o número de cidades intermediárias + (origem e destino)
            if (pilha.size() == (num_intermed + 2) && distanciaRota <= distancia) {
                existeRota = true;
                System.out.print("O trecho de rodovia (");
                pilha.imprime();
                System.out.println(") passa por " + num_intermed + " cidades intermediárias.\n A distância da origem "
                        + "e do destino é de " + distanciaRota + " km, que é inferior a " + distancia + " km.");
            }
            return;
        }
        List<Cidade> destinos = getCidade(origem.toString()).getDestinos();
        int cont = destinos.size();
        for (int i = 0; i < cont; i++) {
            if (!pilha.contains(origem.toString())) {
                pilha.insere(origem.toString());
            }
            busca(destinos.get(i), destino);
            pilha.remove();
        }
        if (pilha.isEmpty()) {
            System.out.println("Não há rota disponível");
        }
    }

    /**
     * Função utilizada para inserir uma ligação entre cidades.
     *
     * @param ligacao A ligação entre cidades a ser inserida.
     */
    public void insereLigacao(final Ligacao ligacao) {
        ligacoes.add(ligacao);
        if (!cidades.contains(ligacao.getOrigem())) {
            cidades.add(ligacao.getOrigem());
        } else if (!cidades.contains(ligacao.getDestino())) {
            cidades.add(ligacao.getDestino());
        }
    }

    /**
     * @param nome Nome da cidade desejada.
     * @return Se existe uma cidade com o nome igual ao parâmetro - retorna a cidade, else - null.
     */
    public Cidade getCidade(final String nome) {
        Iterator<Cidade> iterator = cidades.iterator();
        while (iterator.hasNext()) {
            Cidade cidade = iterator.next();
            if (nome.equals(cidade.toString())) {
                return cidade;
            }
        }
        return null;
    }

    /**
     * @param cidade Uma cidade.
     * @return Se a cidade está no mapa - true, else - false.
     */
    public boolean cidadeExiste(final Cidade cidade) {
        return cidades.contains(cidade);
    }

    /**
     * Função utilizada para ler o mapa, como ele vem do arquivo.
     */
    public void leMapa() {
        Iterator<Ligacao> iterator = ligacoes.iterator();
        while (iterator.hasNext()) {
            Ligacao next = iterator.next();
            System.out.println(next.getOrigem().toString() + ", " + next.getDestino().toString()
                    + ", " + next.getDistancia());
        }
    }

    /**
     * Função utilizada para ler as cidades, imprime também a quantidade de destinos diretos que ela tem.
     */
    public void leCidades() {
        Iterator<Cidade> iterator = cidades.iterator();
        while (iterator.hasNext()) {
            Cidade cidade = iterator.next();
            System.out.println(cidade.toString() + " -> " + cidade.getDestinos().size() + " destinos");
        }
    }

}
