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
                    if (origem != null) {
                        mapa.limpaPilha();
                        mapa.busca(origem);
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

        public Pilha() {
            rota = new ArrayList<>();
        }

        public boolean contains(final String cidade) {
            return rota.contains(cidade);
        }

        public void insere(final String nomeCidade) {
            rota.add(nomeCidade);
        }

        public void remove() {
            rota.remove(rota.size() - 1);
        }

        public void imprime() {
            Iterator<String> iterator = rota.iterator();
            while (iterator.hasNext()) {
                String cidade = iterator.next();
                String seta = " -> ";
                if (!iterator.hasNext()) {
                    seta = "\n";
                }
                System.out.print(cidade + seta);
            }
        }

        public void esvazia() {
            rota.clear();
        }

    }

    private static class Mapa {

        // cada ligação eh uma linha do arquivo
        private final List<Ligacao> ligacoes;
        // cidades origem
        private final List<Cidade> cidades;
        // pilha que armazena as rotas possiveis para cada busca
        private final Pilha pilha;

        public Mapa() {
            ligacoes = new ArrayList<>();
            cidades = new ArrayList<>();
            pilha = new Pilha();
        }

        public void limpaPilha() {
            pilha.esvazia();
        }

        private void busca(final Cidade cidade) {
            if (cidade.toString().equals("H")) {
                if (!pilha.contains(cidade.toString())) {
                    pilha.insere(cidade.toString());
                }
                pilha.imprime();
                return;
            }
            List<Cidade> destinos = getCidade(cidade.toString()).getDestinos();
            int cont = destinos.size();
            for (int i = 0; i < cont; i++) {
                if (!pilha.contains(cidade.toString())) {
                    pilha.insere(cidade.toString());
                }
                busca(destinos.get(i));
                pilha.remove();
            }
        }

        public void insereLigacao(final Ligacao ligacao) {
            ligacoes.add(ligacao);
            if (!cidades.contains(ligacao.getOrigem())) {
                cidades.add(ligacao.getOrigem());
            } else if (!cidades.contains(ligacao.getDestino())) {
                cidades.add(ligacao.getDestino());
            }
        }

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

        public boolean cidadeExiste(final Cidade cidade) {
            return cidades.contains(cidade);
        }

        public void leMapa() {
            Iterator<Ligacao> iterator = ligacoes.iterator();
            while (iterator.hasNext()) {
                Ligacao next = iterator.next();
                System.out.println(next.getOrigem().toString() + ", " + next.getDestino().toString()
                        + ", " + next.getDistancia());
            }
        }

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

        public Ligacao(final Cidade origem, final Cidade destino, final int distancia) {
            this.origem = origem;
            this.destino = destino;
            this.distancia = distancia;
        }

        public Cidade getOrigem() {
            return origem;
        }

        public Cidade getDestino() {
            return destino;
        }

        public int getDistancia() {
            return distancia;
        }

    }

    private static class Cidade {

        private final String nome;
        private final List<Cidade> destinos;

        public Cidade(final String nome) {
            this.nome = nome;
            this.destinos = new ArrayList<>();
        }

        public void insereDestino(final Cidade destino) {
            destinos.add(destino);
        }

        @Override
        public String toString() {
            return nome;
        }

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
