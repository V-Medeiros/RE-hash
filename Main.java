import java.util.Random;

public class Main {
    public static void main(String[] args) {
        final int POSICOES = 100;
        final int ELEMENTOS = 100;
        final int LIMITE_OCUPACAO = POSICOES * 90 / 100;

        int[] matriz = new int[POSICOES];
        // Uma marca separada permite armazenar inclusive o numero zero.
        boolean[] ocupada = new boolean[POSICOES];
        int[] maisColidiram = new int[3];
        int[] maioresColisoes = new int[3];
        for (int i = 0; i < 3; i++) {
            maioresColisoes[i] = -1;
        }

        Random random = new Random(1090);
        int inseridos = 0;
        int bloqueados = 0;
        int totalColisoes = 0;

        for (int i = 0; i < ELEMENTOS; i++) {
            // Intervalo mantido do exercicio anterior: 0 a 100000, inclusive.
            int valor = random.nextInt(100001);

            // A verificacao ocorre ANTES de acessar a tabela para inserir.
            if (inseridos >= LIMITE_OCUPACAO) {
                bloqueados++;
            } else {
                int posicao = valor % POSICOES;
                int colisoesElemento = 0;

                // Rehash por sondagem linear: h(valor, tentativa) =
                // (valor % POSICOES + tentativa) % POSICOES.
                // Como a ocupacao esta abaixo de 90%, existe posicao livre.
                while (ocupada[posicao]) {
                    colisoesElemento++;
                    posicao = (posicao + 1) % POSICOES;
                }

                matriz[posicao] = valor;
                ocupada[posicao] = true;
                inseridos++;
                totalColisoes += colisoesElemento;

                // Mantem manualmente os tres maiores numeros de colisoes.
                // Em empates, permanece primeiro quem foi inserido antes.
                int lugar = 0;
                while (lugar < 3 && maioresColisoes[lugar] >= colisoesElemento) {
                    lugar++;
                }
                if (lugar < 3) {
                    for (int j = 2; j > lugar; j--) {
                        maioresColisoes[j] = maioresColisoes[j - 1];
                        maisColidiram[j] = maisColidiram[j - 1];
                    }
                    maioresColisoes[lugar] = colisoesElemento;
                    maisColidiram[lugar] = valor;
                }
            }
        }

        System.out.println("Semente: 1090");
        System.out.println("Intervalo: 0 a 100000 (inclusive)");
        System.out.println("Rehash: sondagem linear");
        System.out.println("Elementos gerados: " + ELEMENTOS);
        System.out.println("Posicoes da matriz: " + POSICOES);
        System.out.println("Limite de ocupacao: " + LIMITE_OCUPACAO + " (90%)");
        System.out.println("Elementos inseridos: " + inseridos);
        System.out.println("Insercoes bloqueadas pelo limite: " + bloqueados);
        // Cada posicao ocupada visitada durante a insercao conta uma colisao.
        System.out.println("Total de colisoes na insercao: " + totalColisoes);
        System.out.println("Tres elementos que mais colidiram:");
        for (int i = 0; i < 3; i++) {
            System.out.println((i + 1) + ". Valor " + maisColidiram[i]
                    + ": " + maioresColisoes[i] + " colisoes");
        }

        System.out.print("Posicoes livres (indices de 0 a 99): ");
        int livres = 0;
        for (int i = 0; i < POSICOES; i++) {
            if (!ocupada[i]) {
                if (livres > 0) {
                    System.out.print(", ");
                }
                System.out.print(i);
                livres++;
            }
        }
        System.out.println();
        System.out.println("Total de posicoes livres: " + livres);

        System.out.println("Teste de busca dos tres elementos:");
        for (int i = 0; i < 3; i++) {
            int valor = maisColidiram[i];
            int posicao = valor % POSICOES;
            int sondagens = 0;
            boolean encontrou = false;
            boolean continuar = true;

            // Repete a mesma sequencia de posicoes usada para inserir.
            // Para ao encontrar o valor, uma posicao livre ou apos uma volta.
            while (continuar && sondagens < POSICOES) {
                sondagens++;
                if (!ocupada[posicao]) {
                    continuar = false;
                } else {
                    if (matriz[posicao] == valor) {
                        encontrou = true;
                        continuar = false;
                    } else {
                        posicao = (posicao + 1) % POSICOES;
                    }
                }
            }

            if (encontrou) {
                System.out.println("Valor " + valor + " encontrado na posicao "
                        + posicao + " apos " + sondagens + " sondagens");
            } else {
                System.out.println("Valor " + valor + " nao encontrado apos "
                        + sondagens + " sondagens");
            }
        }
    }
}
