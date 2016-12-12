#include <stdio.h>
#include <stdlib.h>
#include "grafo.h"

#define TAM_STR 50

void imprime_tab(Grafo * grafo, Vertice * vertice);
void imprime_tab_(Grafo * grafo, Vertice * vertice1, Vertice * vertice2, int prof);
int encontra_ligacao(Grafo * grafo, Vertice * vertice_origem, Vertice * vertice_destino);
void encontra_mais_popular(Grafo * grafo);

void imprime_tab(Grafo * grafo, Vertice * vertice) {
    imprime_tab_(grafo, vertice, NULL, 0);
}

void imprime_tab_(Grafo * grafo, Vertice * vertice1, Vertice * vertice2, int prof) {
    static int tab = 0;

    int x;
    for (x = 0; x < tab; x++) {
        printf("   ");
    }
    if (vertice2 != NULL) {
        printf("%s -- %s (%d)\n", grafo_retorna_nome(vertice2), grafo_retorna_nome(vertice1), grafo_busca_aresta(grafo, vertice2, vertice1));
    }

    if (prof > 3) {
        return;
    }

    tab += 1;
    int num_filhos = 0;
    Vertice ** filhos = grafo_busca_vertices_saida(grafo, vertice1, &num_filhos);
    int i = 0;
    for (i = 0; i < num_filhos; i++) {
        imprime_tab_(grafo, filhos[i], vertice1, prof + 1);
    }

    tab -= 1;
}

void encontra_mais_popular(Grafo * grafo) {
    int indice_vertice;
    int maior_escore = -1;
    Vertice * vertice_escolhido = NULL;
    for (indice_vertice = 0; indice_vertice < grafo_qtd_vertices(grafo); indice_vertice++) {
        int cont = 0;
        Vertice * v = grafo_retorna_vertice(grafo, indice_vertice);
        Vertice ** vertices_entrada = grafo_busca_vertices_entrada(grafo, v, &cont);
        int j = 0;
        int escore_total = 0;
        for (j = 0; j < cont; j++) {
            int escore = grafo_busca_aresta(grafo, vertices_entrada[j], v);
            if (escore != -1) {
                escore_total += escore;
            }
        }
        if (escore_total > maior_escore) {
            maior_escore = escore_total;
            vertice_escolhido = v;

        }
    }
    printf("Vertice mais popular: %s com %d pontos.\n", grafo_retorna_nome(vertice_escolhido), maior_escore);

}

int encontra_ligacao(Grafo * grafo, Vertice * vertice_origem, Vertice * vertice_destino) {

    int cont = 0;

    Vertice ** vertices_saida = grafo_busca_vertices_saida(grafo, vertice_origem, &cont);
    int i;
    for (i = 0; i < cont; i++) {
        if (vertices_saida[i] == vertice_destino) {
            return 1;
        }
        int resp = encontra_ligacao(grafo, vertices_saida[i], vertice_destino);
        if (resp) {
            return 1;
        }
    }
    return 0;
}

int main() {
    
    char cidadeA[TAM_STR], cidadeB[TAM_STR], distancia[TAM_STR];
    
    FILE *arq;
    int unsigned k = 0;
    arq = fopen("/home/joao/CC/ED/TFinal/dist/Debug/GNU-Linux/grafo.txt", "r");
    if (arq == NULL) {
        printf("Erro, nao foi possivel abrir o arquivo\n");
    } else {
        while ((fscanf(arq, "%s %s %s\n", cidadeA, cidadeB, distancia)) != EOF) {
            printf("%s %s %s\n", cidadeA, cidadeB, distancia);
            k++;
        }
        fclose(arq);
    }
    
    Grafo * grafo = grafo_cria();
    Vertice * vJoao = grafo_cria_vertice(grafo, "Joao");
    Vertice * vMaria = grafo_cria_vertice(grafo, "Maria");
    Vertice * vJose = grafo_cria_vertice(grafo, "Jose");
    Vertice * vAna = grafo_cria_vertice(grafo, "Ana");
    Vertice * vCesar = grafo_cria_vertice(grafo, "Cesar");
    Vertice * vCarlos = grafo_cria_vertice(grafo, "Carlos");

    grafo_insere_aresta(grafo, vJoao, vMaria, 1);
    grafo_insere_aresta(grafo, vJoao, vJose, 1);
    grafo_insere_aresta(grafo, vJoao, vAna, 1);

    grafo_insere_aresta(grafo, vMaria, vJose, 1);
    grafo_insere_aresta(grafo, vMaria, vAna, 1);
    grafo_insere_aresta(grafo, vJose, vAna, 1);
    grafo_insere_aresta(grafo, vJose, vCarlos, 1);

    grafo_insere_aresta(grafo, vCesar, vCarlos, 10);

    int cont = 0;

    Vertice ** vertices = grafo_busca_vertices_saida(grafo, vJoao, &cont);
    int i;
    printf("Os vertices de saida do vertice %s sao:\n", grafo_retorna_nome(vJoao));
    for (i = 0; i < cont; i++) {
        printf("%s\n", grafo_retorna_nome(vertices[i]));
    }

    encontra_mais_popular(grafo);

    Vertice * vOrigem = vJoao;
    Vertice * vDestino = vCarlos;
    int resp = encontra_ligacao(grafo, vOrigem, vDestino);
    if (resp) {
        printf("%s e %s estao conectados\n", grafo_retorna_nome(vOrigem), grafo_retorna_nome(vDestino));
    } else {
        printf("%s e %s nao estao conectados\n", grafo_retorna_nome(vOrigem), grafo_retorna_nome(vDestino));
    }
    vDestino = vCesar;
    resp = encontra_ligacao(grafo, vOrigem, vDestino);
    if (resp) {
        printf("%s e %s estao conectados\n", grafo_retorna_nome(vOrigem), grafo_retorna_nome(vDestino));
    } else {
        printf("%s e %s nao estao conectados\n", grafo_retorna_nome(vOrigem), grafo_retorna_nome(vDestino));
    }
    grafo_libera(grafo);

    return 0;
}