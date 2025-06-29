public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Scoreboard scoreboard = new Scoreboard();

        System.out.println("=== CONNECT FOUR ===");
        System.out.println("Bienvenido al juego Conecta 4!");

        while (true) {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Registrar jugador");
            System.out.println("2. Jugar partida");
            System.out.println("3. Ver estadísticas");
            System.out.println("4. Salir");
            System.out.print("Seleccione una opción: ");

            int opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    System.out.print("Ingrese nombre del jugador: ");
                    String nombre = scanner.nextLine();
                    scoreboard.registerPlayer(nombre);
                    System.out.println("Jugador " + nombre + " registrado exitosamente.");
                    break;

                case 2:
                    System.out.print("Ingrese nombre del Jugador A (X): ");
                    String playerA = scanner.nextLine();
                    System.out.print("Ingrese nombre del Jugador B (O): ");
                    String playerB = scanner.nextLine();


                    if (!scoreboard.checkPlayer(playerA)) {
                        System.out.println("Jugador A no está registrado. Registrándolo...");
                        scoreboard.registerPlayer(playerA);
                    }
                    if (!scoreboard.checkPlayer(playerB)) {
                        System.out.println("Jugador B no está registrado. Registrándolo...");
                        scoreboard.registerPlayer(playerB);
                    }


                    Game game = new Game(playerA, playerB);
                    String winner = game.play();


                    if (winner.equals("")) {

                        scoreboard.addGameResult(playerA, playerB, true);
                    } else {

                        String loser = winner.equals(playerA) ? playerB : playerA;
                        scoreboard.addGameResult(winner, loser, false);
                    }
                    break;

                case 3:
                    System.out.print("Ingrese nombre del jugador para ver estadísticas: ");
                    String playerName = scanner.nextLine();
                    if (scoreboard.checkPlayer(playerName)) {
                        System.out.println("Jugador " + playerName + " encontrado en el sistema.");
                        System.out.println("(Función de estadísticas detalladas pendiente de implementar)");
                    } else {
                        System.out.println("Jugador no encontrado.");
                    }
                    break;

                case 4:
                    System.out.println("¡Gracias por jugar Connect Four!");
                    scanner.close();
                    return;

                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        }
    }
}
