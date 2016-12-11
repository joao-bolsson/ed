typedef struct grafo Grafo;
typedef struct vertice Vertice;

/*funções para criação/remoção de um grafo*/
Grafo * grafo_cria();
void grafo_libera(Grafo * grafo);

/*funções para manipulação dos vértices de um grafo*/
int grafo_qtd_vertices(Grafo * grafo);
Vertice ** grafo_busca_vertices_saida(Grafo * grafo, Vertice * vertice, int * cont);
Vertice ** grafo_busca_vertices_entrada(Grafo * grafo, Vertice * vertice, int * cont);

/*funções para manipulação das arestas do grafo*/
void grafo_insere_aresta(Grafo * grafo, Vertice * vertice1, Vertice * vertice2, int peso);
int grafo_busca_aresta(Grafo * grafo, Vertice * vertice1, Vertice * vertice2);

/*funções para criação de vértices*/
Vertice * grafo_cria_vertice(Grafo * grafo, const char nome[]);
Vertice * grafo_retorna_vertice(Grafo * grafo, int indice);

/*funções para obtenção dos dados de vértices*/
char * grafo_retorna_nome(Vertice * vertice);

/*função para impressão tabulada a partir de um vértice*/
void grafo_imprime_tab(Grafo * grafo, Vertice * vertice);