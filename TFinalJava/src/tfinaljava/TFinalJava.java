/**
 *
 * Encontre pelo menos um trecho de rodovia que passe por X cidades intermediárias em que a distância que separa a
 * cidade de origem e a cidade de destino seja inferior a Y km.
 * A resposta deve ser uma das seguintes opções:
 * "O trecho de rodovia (xxx,xxx,...., xxx) passa por X cidades intermediárias. A distância da origem e do destino é de
 * xxx km, que é inferior a Y km."
 * "Não existe trecho de rodovia que passa por  X cidades intermediárias cuja distância  seja inferior a Y km."  *
 * O número de cidades intermediárias (X) e a distância (Y) são parâmetros a serem fornecidos pelo usuário. O mapa deve
 * ser carregado a partir de um arquivo texto.
 */
package tfinaljava;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 *
 * @author João Bolsson (jvmarques@inf.ufsm.br)
 * @since 16, 07 Dez.
 */
public class TFinalJava {

    private static Scanner ler;

    /**
     *
     * @param args the command line arguments.
     */
    public static void main(String[] args) {
        ler = new Scanner(System.in);
        System.out.printf("Informe o caminho do arquivo mapa:\n");
        String path = ler.nextLine();

        try {
            try (FileReader file = new FileReader("/home/joao/CC/ED/TFinalJava/src/tfinaljava/grafo.txt")) {
                BufferedReader lerArq = new BufferedReader(file);

                String linha = lerArq.readLine();

                // lista com as linhas do arquivo
                List<String> lines = new ArrayList<>();
                System.out.println("Lendo o arquivo");
                while (linha != null) {
                    lines.add(linha);
                    linha = lerArq.readLine();
                }
                System.out.println("Arquivo lido");

                Mapa mapa = new Mapa();

                System.out.println("Inserindo no mapa");
                Iterator<String> iterator = lines.iterator();
                while (iterator.hasNext()) {
                    String next = iterator.next();
                    String[] lineSplit = next.split(", ");
                    // origem
                    Cidade cidade = new Cidade(lineSplit[0]);

                    if (mapa.cidadeExiste(cidade)) { // se está no mapa, pega ela
                        cidade = mapa.getCidade(lineSplit[0]);
                    }
                    // destino
                    Cidade destino = new Cidade(lineSplit[1]);
                    // distancia
                    int dist = Integer.parseInt(lineSplit[2]);

                    // insere um novo destino para a cidade
                    cidade.insereDestino(destino);

                    // nova ligação entre cidades
                    Ligacao ligacao = new Ligacao(cidade, destino, dist);

                    // insere a ligação no mapa
                    mapa.insereLigacao(ligacao);
                }
                System.out.println("Mapa completo");
                System.out.println("Lendo o mapa");
                mapa.leMapa();
                System.out.println("Lendo cidades");
                mapa.leCidades();
                System.out.println("Cidades lidas");
                boolean parar = false;
                while (!parar) {
                    System.out.println("==== GERANDO ROTAS ====");
                    System.out.print("Origem: ");
                    Cidade origem = mapa.getCidade(ler.next());
                    System.out.print("Destino: ");
                    Cidade destino = mapa.getCidade(ler.next());
                    System.out.print("Num cidades intermediárias: ");
                    int num_intermed = Integer.parseInt(ler.next());
                    System.out.print("Distância: ");
                    int distancia = Integer.parseInt(ler.next());
                    if (origem != null && destino != null) {
                        mapa.setParametros(num_intermed, distancia);
                        mapa.busca(origem, destino);
                    } else {
                        System.out.println("ERRO: uma das cidades não existe no mapa.");
                    }
                    boolean validChar = false;
                    while (!validChar) {
                        System.out.print("Fazer outra busca? [s/n]");
                        String continuar = ler.next();
                        if (continuar.equalsIgnoreCase("s")) {
                            validChar = true;
                        } else if (continuar.equalsIgnoreCase("n")) {
                            validChar = true;
                            parar = true;
                        }
                    }

                }

            }
        } catch (final IOException e) {
            System.out.println("ERRO: " + e.getMessage());
        }
    }

    public static class Pilha {

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

    private static class Mapa {

        // cada ligação eh uma linha do arquivo
        private final List<Ligacao> ligacoes;
        // cidades origem
        private final List<Cidade> cidades;
        // pilha que armazena as rotas possiveis para cada busca
        private final Pilha pilha;
        // número de cidades intermediárias e a distância máxima desejada pelo usuario em cada busca.
        private int num_intermed, distancia;

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
            this.num_intermed = num_intermed;
            this.distancia = distancia;
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
        private void busca(final Cidade origem, final Cidade destino) {
            if (origem.equals(destino)) {
                if (!pilha.contains(origem.toString())) {
                    pilha.insere(origem.toString());
                }
                int distanciaRota = getDistancia();
                // o trecho deve conter o número de cidades intermediárias + (origem e destino)
                if (pilha.size() == (num_intermed + 2) && distanciaRota <= distancia) {
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
        private void leCidades() {
            Iterator<Cidade> iterator = cidades.iterator();
            while (iterator.hasNext()) {
                Cidade cidade = iterator.next();
                System.out.println(cidade.toString() + " -> " + cidade.getDestinos().size() + " destinos");
            }
        }

    }

    private static class Ligacao {

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

    private static class Cidade {

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
        private List<Cidade> getDestinos() {
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

}
