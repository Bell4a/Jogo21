import java.util.Scanner;

public class correcao {

    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);

        int[] baralho = new int[52]; 
        boolean[] cartasUsadas = new boolean[52]; 

        int moedas = 0; 
        int partidasJogadas = 0;
        int partidasGanhadas = 0;
        int partidasPerdidas = 0;
        int conseguiu21 = 0;

        int somaCartas = 0;
        boolean partidaAtiva = false;

        int opcao = 0;

        while (opcao != 3) {
            System.out.println("\n|    * JOGO 21 *    |");
            System.out.println("1 - Virar carta");
            System.out.println("2 - Parar partida");
            System.out.println("3 - Encerrar jogo");
            System.out.print("Escolha uma opcao: ");
            opcao = teclado.nextInt();

            if (opcao == 1) {
                // CORREÇÃO: Só inicializa a nova partida se o jogador de fato quiser jogar!
                if (!partidaAtiva) {
                    gerarBaralho(baralho);
                    embaralharBaralho(baralho);
                    resetarCartasUsadas(cartasUsadas);
                    somaCartas = 0;
                    partidaAtiva = true;
                }

                System.out.print("Informe a posicao da carta (0 a 51): ");
                int posicao = teclado.nextInt();

                while (posicao < 0 || posicao > 51 || cartasUsadas[posicao]) {
                    System.out.println("Essa posicao ja foi escolhida ou está fora dos limites! Escolha outra.");
                    System.out.print("Informe a posicao da carta (0 a 51): ");
                    posicao = teclado.nextInt();
                }

                cartasUsadas[posicao] = true;
                int cartaSorteada = retirarCarta(baralho, posicao);
                System.out.println("Carta virada: " + cartaSorteada);

                somaCartas = calcularSoma(somaCartas, cartaSorteada);
                System.out.println("Soma atual: " + somaCartas);

                int status = verificarFimPartida(somaCartas);

                if (status == 1) {
                    System.out.println("Você Venceu! (Exatamente 21 pontos)");
                    moedas += 200;
                    partidasJogadas++;
                    partidasGanhadas++;
                    conseguiu21++;
                    partidaAtiva = false;
                } else if (status == 2) {
                    System.out.println("Você Perdeu! (Passou de 21 pontos)");
                    moedas -= 100;
                    partidasJogadas++;
                    partidasPerdidas++;
                    partidaAtiva = false;
                }

            } else if (opcao == 2) {
                // Proteção: Só permite parar se houver uma partida ativa rodando
                if (partidaAtiva) {
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

                    partidaAtiva = false;
                } else {
                    System.out.println("Nao ha nenhuma partida em andamento para parar!");
                }

            } else if (opcao == 3) {
                System.out.println("\nINFORMAÇÕES FINAIS DO JOGO:");
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

    // --- Subprogramas mantidos idênticos aos seus (estão ótimos!) ---
    public static void gerarBaralho(int[] baralho) {
        int indice = 0;
        for (int valor = 1; valor <= 13; valor++) {
            for (int i = 0; i < 4; i++) {
                baralho[indice] = valor;
                indice++;
            }
        }
    }

    public static void embaralharBaralho(int[] baralho) {
        for (int i = 0; i < 100; i++) { 
            int pos1 = (int) (Math.random() * 52);
            int pos2 = (int) (Math.random() * 52);

            int aux = baralho[pos1];
            baralho[pos1] = baralho[pos2];
            baralho[pos2] = aux;
        }
    }

    public static int retirarCarta(int[] baralho, int posicao) {
        return baralho[posicao];
    }

    public static int calcularSoma(int somaAtual, int carta) {
        int pontosCarta;

        if (carta > 10) {
            pontosCarta = 10;
        } else if (carta == 1 && somaAtual == 10) {
            pontosCarta = 11;
        } else {
            pontosCarta = carta;
        }

        return somaAtual + pontosCarta;
    }

    public static int verificarFimPartida(int soma) {
        if (soma == 21) {
            return 1;
        } else if (soma > 21) {
            return 2;
        }
        return 0;
    }

    public static int calcularMoedasParada(int soma) {
        if (soma == 19 || soma == 20) {
            return 100;
        } else if (soma == 17 || soma == 18) {
            return 50;
        } else {
            return 0;
        }
    }

    public static void resetarCartasUsadas(boolean[] cartasReveladas) {
        for (int i = 0; i < cartasReveladas.length; i++) {
            cartasReveladas[i] = false;
        }
    }
}