import java.util.Scanner;

public class Jogo21 {

    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);

        // Baralho original e quais cartas foram usadas
        int[] baralho = new int[52]; // Baralho original (13 * 4)
        boolean[] cartasUsadas = new boolean[52]; // Confere se a carta já foi usada na partida

        // Variáveis para controlar as partida
        int moedas = 0; 
        int partidasJogadas = 0;
        int partidasGanhadas = 0; // Ganhou pelo menos 50 moedas na rodada
        int partidasPerdidas = 0; // Não conseguiu 50 moedas na rodada
        int conseguiu21 = 0; // Vai contar quantas vezes o jogador conseguiu exatamente 21 pontos

        // Controle de fluxo do jogo
        int somaCartas = 0; // Soma dos números das cartas
        boolean partidaAtiva = false; // Indica se a partida está em andamento

        int opcao = 0; // opção escolhida no menu do switch case

        while (opcao != 3) {
            // Se não há uma partida ativa, inicializa uma nova
            if (!partidaAtiva) {
                gerarBaralho(baralho);
                embaralharBaralho(baralho);
                resetarCartasUsadas(cartasUsadas);
                somaCartas = 0;
                partidaAtiva = true;
            }

            // Exibição do Menu
            System.out.println("|    * JOGO 21 *    |");
            System.out.println("1 - Virar carta");
            System.out.println("2 - Parar partida");
            System.out.println("3 - Encerrar jogo");
            System.out.print("Escolha uma opcao: ");
            opcao = teclado.nextInt();

            if (opcao == 1) {
                System.out.print("Informe a posicao da carta (0 a 51): ");
                int posicao = teclado.nextInt();

                // Valida se a carta já foi escolhida nesta rodada
                while (cartasUsadas[posicao]) {
                    System.out.println("Essa posicao ja foi escolhida! Escolha outra.");
                    System.out.print("Informe a posicao da carta (0 a 51): ");
                    posicao = teclado.nextInt();
                }

                // Marca a carta como usada
                cartasUsadas[posicao] = true;
                int cartaSorteada = retirarCarta(baralho, posicao);
                System.out.println("Carta virada: " + cartaSorteada);

                // Calcula e atualiza a soma com base nas regras do 21
                somaCartas = calcularSoma(somaCartas, cartaSorteada);
                System.out.println("Soma atual: " + somaCartas);

                // Verifica o status do jogo após a jogada
                int status = verificarFimPartida(somaCartas);

                if (status == 1) { // Exatamente 21
                    System.out.println("Você Venceu! (Exatamente 21 pontos)");
                    moedas += 200;
                    partidasJogadas++;
                    partidasGanhadas++;
                    conseguiu21++;
                    partidaAtiva = false; // Acaba e reinicia outra partida
                } else if (status == 2) { // Passou de 21
                    System.out.println("Você Perdeu! (Passou de 21 pontos)");
                    moedas -= 100;
                    partidasJogadas++;
                    partidasPerdidas++;
                    partidaAtiva = false;
                }

            } else if (opcao == 2) {
                // Jogador decidiu parar
                partidasJogadas++;
                int moedasGanhas = calcularMoedasParada(somaCartas);
                moedas += moedasGanhas;

                System.out.println("Partida encerrada pelo jogador. Pontuacao final: " + somaCartas);
                System.out.println("Moedas ganhas: " + moedasGanhas);

                if (moedasGanhas >= 50) {
                    partidasGanhadas++;
                } else {
                    partidasPerdidas++;
                }

                partidaAtiva = false; // Força reinício

            } else if (opcao == 3) {
                // Encerrar o jogo e exibir estatísticas
                System.out.println("INFORMAÇÕES FINAIS DO JOGO:");
                System.out.println("Quantidade de partidas jogadas: " + partidasJogadas);
                System.out.println("Quantidade de partidas vencidas: " + partidasGanhadas);
                System.out.println("Quantidade de partidas perdidas: " + partidasPerdidas);
                System.out.println("Quantidade de vezes que conseguiu exatamente 21 pontos: " + conseguiu21);
                System.out.println("Numero de moedas total acumulado: " + moedas);
                System.out.println("Fim do jogo");
            }
        }
        teclado.close();
    }

    // Subprograma para gerar o baralho (1 a 13, quatro vezes cada)
    public static void gerarBaralho(int[] baralho) {
        int indice = 0;
        for (int valor = 1; valor <= 13; valor++) {
            for (int i = 0; i < 4; i++) {
                baralho[indice] = valor;
                indice++;
            }
        }
    }

    // Subprograma para embaralhar o baralho (mínimo 50 trocas)
    public static void embaralharBaralho(int[] baralho) {
        for (int i = 0; i < 100; i++) { 
            int pos1 = (int) (Math.random() * 52);
            int pos2 = (int) (Math.random() * 52);

            int temp = baralho[pos1];
            baralho[pos1] = baralho[pos2];
            baralho[pos2] = temp;
        }
    }

    // Subprograma para retirar uma carta
    public static int retirarCarta(int[] baralho, int posicao) {
        return baralho[posicao];
    }

    // Subprograma para calcular a soma de pontos da partida
    public static int calcularSoma(int somaAtual, int carta) {
        int pontosCarta;

        // Cartas 11, 12 e 13 valem 10 pontos
        if (carta > 10) {
            pontosCarta = 10;
        } else if (carta == 1 && somaAtual == 10) {
            // Regra especial: se tem 10 pontos e tira 1, o Ás vale 11 para fechar 21
            pontosCarta = 11;
        } else {
            pontosCarta = carta;
        }

        return somaAtual + pontosCarta;
    }

    // Subprograma para verificar se a partida terminou
    // Retorna: 0 se continua, 1 se ganhou (21), 2 se perdeu (passou de 21)
    public static int verificarFimPartida(int soma) {
        if (soma == 21) {
            return 1;
        } else if (soma > 21) {
            return 2;
        }
        return 0;
    }

    // Subprograma auxiliar para calcular moedas ao parar voluntariamente
    public static int calcularMoedasParada(int soma) {
        if (soma == 19 || soma == 20) {
            return 100;
        } else if (soma == 17 || soma == 18) {
            return 50;
        } else {
            return 0;
        }
    }

    // Subprograma auxiliar para limpar o histórico de cartas viradas na rodada
    public static void resetarCartasUsadas(boolean[] cartasReveladas) {
        for (int i = 0; i < cartasReveladas.length; i++) {
            cartasReveladas[i] = false;
        }
    }
}