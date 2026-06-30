package TrabalhoFinal;

import java.util.Scanner;

public class SoMain {



static boolean posicaoJaUsada(){
 return false;
}

    public static void main(String[] args) {
        
        Scanner scanner = new Scanner(System.in);

        int[] baralho         = new int[52];
        int[] posicoesUsadas  = new int[52];
        int[] cartasRetiradas = new int[52];
        int[] dadosPartida    = new int[2]; // [0]=qtdCartas, [1]=soma

        // Estatísticas globais
        int totalPartidas    = 0;
        int totalVitorias21  = 0; // exatamente 21 pontos
        int totalMoedas      = 0;
        int partidasVencidas = 0; // moedas >= 50
        int partidasPerdidas = 0; // moedas < 50

        // Inicializa primeira partida
        iniciarNovaPartida(baralho, posicoesUsadas, dadosPartida);

        int opcao = 0;

        while (opcao != 3) {
            exibirMenu();
            opcao = scanner.nextInt();

            // ── Opção 1: Virar carta ─────────────────────────────────────────────
            if (opcao == 1) {
                int posicao;

                // Garante posição não repetida na mesma partida
                do {
                    System.out.print("  Informe a posicao (0 a 51): ");
                    posicao = scanner.nextInt();
                    if (posicaoJaUsada(posicoesUsadas, dadosPartida[0], posicao)) {
                        System.out.println("  >> Posicao ja utilizada nesta partida! Escolha outra.");
                    }
                } while (posicaoJaUsada(posicoesUsadas, dadosPartida[0], posicao));

                // Registra a posição usada e a carta retirada
                posicoesUsadas[dadosPartida[0]] = posicao;
                int valorCarta = retirarCarta(baralho, posicao);
                cartasRetiradas[dadosPartida[0]] = valorCarta;
                dadosPartida[0]++;

                // Recalcula a soma com a regra do Às
                dadosPartida[1] = calcularSoma(cartasRetiradas, dadosPartida[0]);

                int valorExibido = (valorCarta >= 11) ? 10 : valorCarta;
                System.out.println("  >> Carta virada: " + valorCarta
                        + " (vale " + valorExibido + " pontos)");
                System.out.println("  >> Soma atual: " + dadosPartida[1] + " pontos");

                // Verifica o resultado da jogada
                int resultado = verificarPartida(dadosPartida[1]);

                if (resultado == 1) {
                    System.out.println("  *** PARABENS! Voce fez 21 pontos e VENCEU! (+200 moedas) ***");
                    totalMoedas += 200;
                    totalVitorias21++;
                    totalPartidas++;
                    partidasVencidas++;
                    iniciarNovaPartida(baralho, posicoesUsadas, dadosPartida);

                } else if (resultado == -1) {
                    System.out.println("  *** Voce ULTRAPASSOU 21 pontos e PERDEU! (-100 moedas) ***");
                    totalMoedas -= 100;
                    totalPartidas++;
                    partidasPerdidas++;
                    iniciarNovaPartida(baralho, posicoesUsadas, dadosPartida);
                }
                // resultado == 0: partida continua

            // ── Opção 2: Parar partida ───────────────────────────────────────────
            } else if (opcao == 2) {
                int soma   = dadosPartida[1];
                int moedas = calcularMoedasParada(soma);

                System.out.println("  >> Partida encerrada voluntariamente.");
                System.out.println("  >> Soma final: " + soma + " pontos");
                System.out.println("  >> Moedas desta partida: " + moedas);

                totalMoedas += moedas;
                totalPartidas++;

                if (moedas >= 50) {
                    partidasVencidas++;
                } else {
                    partidasPerdidas++;
                }

                iniciarNovaPartida(baralho, posicoesUsadas, dadosPartida);

            // ── Opção 3: Encerrar jogo ───────────────────────────────────────────
            } else if (opcao == 3) {
                System.out.println();
                System.out.println("========================================");
                System.out.println("         RESUMO DO JOGO                ");
                System.out.println("========================================");
                System.out.println("  Partidas jogadas      : " + totalPartidas);
                System.out.println("  Partidas vencidas     : " + partidasVencidas
                        + "  (moedas >= 50)");
                System.out.println("  Partidas perdidas     : " + partidasPerdidas
                        + "  (moedas < 50)");
                System.out.println("  Vezes com 21 exatos   : " + totalVitorias21);
                System.out.println("  Total de moedas       : " + totalMoedas);
                System.out.println("========================================");
                System.out.println("  Obrigado por jogar! Ate a proxima.");
                System.out.println("========================================");
            }
        }

        scanner.close();
    }
}
