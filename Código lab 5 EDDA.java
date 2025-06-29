import java.util.*;

class BST<Key extends Comparable<Key>, Value> {
    private Node root;

    private class Node {
        Key key;
        Value value;
        Node izq;
        Node der;

        public Node(Key key, Value value) {
            this.key = key;
            this.value = value;
            this.izq = null;
            this.der = null;
        }
    }

    public void put(Key key, Value value) {
        root = push(root, key, value);
    }

    private Node push(Node root, Key key, Value value) {
        if (root == null) root = new Node(key, value);
        else if (key.compareTo(root.key) <= 0) root.izq = push(root.izq, key, value);
        else if (key.compareTo(root.key) > 0) root.der = push(root.der, key, value);
        return root;
    }

    public Value get(Key key) {
        return get(root, key);
    }

    private Value get(Node x, Key key) {
        if (x == null) return null;

        int cmp = key.compareTo(x.key);
        if (cmp < 0) return get(x.izq, key);
        else if (cmp > 0) return get(x.der, key);
        else return x.value;
    }

    public List<Key> keysInRange(Key lo, Key hi) {
        List<Key> result = new ArrayList<>();
        keysInRange(root, result, lo, hi);
        return result;
    }

    private void keysInRange(Node x, List<Key> result, Key lo, Key hi) {
        if (x == null) return;

        int cmplo = lo.compareTo(x.key);
        int cmphi = hi.compareTo(x.key);

        if (cmplo < 0) keysInRange(x.izq, result, lo, hi);
        if (cmplo <= 0 && cmphi >= 0) result.add(x.key);
        if (cmphi > 0) keysInRange(x.der, result, lo, hi);
    }

    public Key successor(Key key) {
        return successor(root, key, null);
    }

    private Key successor(Node x, Key key, Key best) {
        if (x == null) return best;

        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            return successor(x.izq, key, x.key);
        } else {
            return successor(x.der, key, best);
        }
    }
}

class HashST<Key, Value> {
    private int N;
    private Object[] hashTable;
    private Object[] values;
    private boolean[] occupied;

    public HashST() {
        this.N = 97;
        this.hashTable = new Object[N];
        this.values = new Object[N];
        this.occupied = new boolean[N];
    }

    private int hash(Key key) {
        if (key instanceof String) {
            String w = (String) key;
            return w.length() % N;
        } else {
            return Math.abs(key.hashCode()) % N;
        }
    }

    public void put(Key key, Value value) {
        int index = hash(key);
        int originalIndex = index;

        while (occupied[index]) {
            if (hashTable[index].equals(key)) {
                values[index] = value;
                return;
            }
            index = (index + 1) % N;
            if (index == originalIndex) {
                throw new RuntimeException("Tabla hash llena");
            }
        }

        hashTable[index] = key;
        values[index] = value;
        occupied[index] = true;
    }

    public Value get(Key key) {
        int index = hash(key);
        int originalIndex = index;

        while (occupied[index]) {
            if (hashTable[index].equals(key)) {
                return (Value) values[index];
            }
            index = (index + 1) % N;
            if (index == originalIndex) break;
        }
        return null;
    }

    public boolean contains(Key key) {
        return get(key) != null;
    }
}

class Player {
    private String playerName;
    private int wins;
    private int draws;
    private int losses;

    public Player(String playerName) {
        this.playerName = playerName;
        this.wins = 0;
        this.draws = 0;
        this.losses = 0;
    }

    public void addWin() {
        wins++;
    }

    public void addDraw() {
        draws++;
    }

    public void addLoss() {
        losses++;
    }

    public double winRate() {
        int totalGames = wins + draws + losses;
        if (totalGames == 0) return 0;
        return (double) wins / totalGames;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getWins() {
        return wins;
    }

    public int getDraws() {
        return draws;
    }

    public int getLosses() {
        return losses;
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Player player = (Player) obj;
        return playerName.equals(player.playerName);
    }

    public int hashCode() {
        return playerName.hashCode();
    }
}

class Scoreboard {
    private BST<Integer, String> winTree;
    private HashST<String, Player> players;
    private int playedGames;

    public Scoreboard() {
        winTree = new BST<>();
        players = new HashST<>();
        playedGames = 0;
    }

    public void addGameResult(String winnerPlayerName, String looserPlayerName, boolean draw) {
        if (draw) {
            Player winner = players.get(winnerPlayerName);
            Player looser = players.get(looserPlayerName);
            winner.addDraw();
            looser.addDraw();
        } else {
            Player winner = players.get(winnerPlayerName);
            Player looser = players.get(looserPlayerName);

            winner.addWin();
            looser.addLoss();

            winTree.put(winner.getWins(), winner.getPlayerName());
        }
        playedGames++;
    }

    public void registerPlayer(String playerName) {
        if (!players.contains(playerName)) {
            players.put(playerName, new Player(playerName));
        }
    }

    public boolean checkPlayer(String playerName) {
        return players.contains(playerName);
    }

    public Player[] winRange(int lo, int hi) {
        List<Integer> keys = winTree.keysInRange(hi, lo);
        List<Player> result = new ArrayList<>();

        for (Integer wins : keys) {
            String playerName = winTree.get(wins);
            Player player = players.get(playerName);
            result.add(player);
        }

        return result.toArray(new Player[0]);
    }

    public Player[] winSuccessor(int wins) {
        Integer successor = winTree.successor(wins);
        if (successor == null) {
            return new Player[0];
        }

        String playerName = winTree.get(successor);
        Player player = players.get(playerName);
        return new Player[]{player};
    }
}

class ConnectFour {
    private char[][] grid;
    private char currentSymbol;

    public ConnectFour() {
        grid = new char[7][6];
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 6; j++) {
                grid[i][j] = ' ';
            }
        }
        currentSymbol = 'X';
    }

    public boolean makeMove(int z) {
        if (z < 0 || z >= 7) return false;

        for (int row = 5; row >= 0; row--) {
            if (grid[z][row] == ' ') {
                grid[z][row] = currentSymbol;
                currentSymbol = (currentSymbol == 'X') ? 'O' : 'X';
                return true;
            }
        }
        return false;
    }

    public String isGameOver() {

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 4; col++) {
                char symbol = grid[col][row];
                if (symbol != ' ' &&
                        grid[col][row] == symbol &&
                        grid[col+1][row] == symbol &&
                        grid[col+2][row] == symbol &&
                        grid[col+3][row] == symbol) {
                    return String.valueOf(symbol);
                }
            }
        }


        for (int col = 0; col < 7; col++) {
            for (int row = 0; row < 3; row++) {
                char symbol = grid[col][row];
                if (symbol != ' ' &&
                        grid[col][row] == symbol &&
                        grid[col][row+1] == symbol &&
                        grid[col][row+2] == symbol &&
                        grid[col][row+3] == symbol) {
                    return String.valueOf(symbol);
                }
            }
        }


        for (int col = 0; col < 4; col++) {
            for (int row = 3; row < 6; row++) {
                char symbol = grid[col][row];
                if (symbol != ' ' &&
                        grid[col][row] == symbol &&
                        grid[col+1][row-1] == symbol &&
                        grid[col+2][row-2] == symbol &&
                        grid[col+3][row-3] == symbol) {
                    return String.valueOf(symbol);
                }
            }
        }


        for (int col = 0; col < 4; col++) {
            for (int row = 0; row < 3; row++) {
                char symbol = grid[col][row];
                if (symbol != ' ' &&
                        grid[col][row] == symbol &&
                        grid[col+1][row+1] == symbol &&
                        grid[col+2][row+2] == symbol &&
                        grid[col+3][row+3] == symbol) {
                    return String.valueOf(symbol);
                }
            }
        }


        boolean full = true;
        for (int col = 0; col < 7; col++) {
            if (grid[col][0] == ' ') {
                full = false;
                break;
            }
        }

        if (full) return "DRAW";
        return "IN_PROGRESS";
    }

    public void printBoard() {
        System.out.println("  0 1 2 3 4 5 6");
        for (int row = 0; row < 6; row++) {
            System.out.print(row + " ");
            for (int col = 0; col < 7; col++) {
                System.out.print(grid[col][row] + " ");
            }
            System.out.println();
        }
    }

    public char getCurrentSymbol() {
        return currentSymbol;
    }
}


class Game {
    private String status;
    private String winnerPlayerName;
    private String playerNameA;
    private String playerNameB;
    private ConnectFour connectFour;

    public Game(String playerNameA, String playerNameB) {
        this.playerNameA = playerNameA;
        this.playerNameB = playerNameB;
        this.status = "IN_PROGRESS";
        this.winnerPlayerName = "";
        this.connectFour = new ConnectFour();
    }

    public String play() {
        Scanner scanner = new Scanner(System.in);

        while (status.equals("IN_PROGRESS")) {
            connectFour.printBoard();

            String currentPlayer = (connectFour.getCurrentSymbol() == 'X') ? playerNameA : playerNameB;
            System.out.println("Turno de " + currentPlayer + " (" + connectFour.getCurrentSymbol() + ")");
            System.out.print("Ingrese columna (0-6): ");

            int column = scanner.nextInt();

            if (connectFour.makeMove(column)) {
                String gameResult = connectFour.isGameOver();

                if (gameResult.equals("X")) {
                    status = "VICTORY";
                    winnerPlayerName = playerNameA;
                } else if (gameResult.equals("O")) {
                    status = "VICTORY";
                    winnerPlayerName = playerNameB;
                } else if (gameResult.equals("DRAW")) {
                    status = "DRAW";
                    winnerPlayerName = "";
                }
            } else {
                System.out.println("Movimiento inválido.");
            }
        }

        connectFour.printBoard();

        if (status.equals("VICTORY")) {
            System.out.println("¡Ganó " + winnerPlayerName + "!");
            return winnerPlayerName;
        } else {
            System.out.println("¡Empate!");
            return "";
        }
    }
}
