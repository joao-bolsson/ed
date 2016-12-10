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
import java.util.Scanner;

/**
 *
 * @author João Bolsson (jvmarques@inf.ufsm.br)
 * @since 16, 07 Dez.
 */
public class TFinalJava {

    private static Scanner ler;
    private final Mapa mapa;

    /**
     * Construtor padrão.
     *
     * @param lines Linhas do arquivo lido.
     */
    public TFinalJava(final List<String> lines) {
        mapa = new Mapa();

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
    }

    private void interage() {
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
                if (!mapa.existeRota()) {
                    System.out.println("Não existe trecho de rodovia que passa por " + num_intermed + " cidades "
                            + "intermediárias cuja distância  seja inferior a " + distancia + " km.");
                }
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
                TFinalJava trabalho = new TFinalJava(lines);
                trabalho.interage();
            }
        } catch (final IOException e) {
            System.out.println("ERRO: " + e.getMessage());
        }
    }

}
