package wordhunt;

import trie.BasicTrieNode;
import trie.Trie;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class WordHuntSolver {
    private String wordhunt;
    private String anagram;
    private int[][] neighbours;

    public Tile[] tiles;
    private final Trie trie;
    public Map<String, PathNode> map;

    public int rowLen, colLen;
    boolean[][] amGraph;

    public WordHuntSolver() {
        map = new HashMap<>();
        trie = new Trie();
        File file = new File("CollinsDictionairy.txt");

        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                trie.insert(scanner.nextLine());
            }

        } catch (FileNotFoundException e) {
            System.out.println("CollinsDoctionairy.txt not found.");
        }
    }

    public void updateAnagrams(String anagrams) throws IllegalArgumentException {

        for (int pos = 0; pos < anagrams.length(); pos++) {
            if (!Character.isAlphabetic(anagrams.charAt(pos))) {
                throw new IllegalArgumentException("Only alphabetical strings");
            }
        }

        this.anagram = anagrams.toUpperCase();
        this.tiles = new Tile[anagram.length()];
        for (int pos = 0; pos < anagram.length(); pos++) {
            tiles[pos] = new Tile(anagram.charAt(pos), pos, anagram.length());
        }
    }

    public void updateWordHunt(String wordhunt, int RowLength) throws IllegalArgumentException {

        for (int pos = 0; pos < wordhunt.length(); pos++) {
            if (!Character.isAlphabetic(wordhunt.charAt(pos))) {
                throw new IllegalArgumentException("Only alphabetical strings");
            }
        }

        if (wordhunt.length() % RowLength != 0) {
            throw new IllegalArgumentException("Must be a square or rectangle");
        }

        rowLen = RowLength;
        colLen = wordhunt.length() / rowLen;

        this.wordhunt = wordhunt.toUpperCase();
        this.tiles = new Tile[wordhunt.length()];

        for (int pos = 0; pos < wordhunt.length(); pos++) {
            tiles[pos] = new Tile(wordhunt.charAt(pos), pos, rowLen+1);
        }

        amGraph = new boolean[wordhunt.length()][wordhunt.length()];

        for (int row = 0; row < wordhunt.length(); row++) {
            for (int col = 0; col < wordhunt.length(); col++) {
                amGraph[row][col] = isNeighbour(row,col);
            }
        }
    }

    private boolean isNeighbour(int position, int pos2) {
        return     Math.abs((position / rowLen) - (pos2 / rowLen)) < 2
                && Math.abs((position % rowLen) - (pos2 % rowLen)) < 2
                && pos2 != position;
    }

    public List<String> solveWordHunt() {
        return solver(false);
    }

    public List<String> solveAnagrams() {
        return solver(true);
    }


    private List<String> solver(boolean anagrams) {
        if(map != null) {
            map.clear();
        }

        String target = anagrams ? anagram : wordhunt;

        boolean[] visited = new boolean[target.length()];
        List<Character> started = new ArrayList<>();

        for (int pos = 0; pos < target.length(); pos++) {
            char startingChar = target.charAt(pos);
            if (started.contains(startingChar)) {
                continue;
            }
            started.add(startingChar);
            Arrays.fill(visited, false);
            PathNode start = new PathNode(tiles[pos], trie.getRoot().getChild(startingChar, !anagrams));
            DFS(visited, start, map, pos, anagrams);
        }

        return map.keySet().stream()
                .sorted(Comparator.comparingInt(String::length).reversed())
                .toList();
    }


    public void DFS(boolean[] visited, PathNode node, Map<String,PathNode> map, int pos, boolean anagrams) {

        visited[pos] = true;

        if (node.getTrieNode().isWord()) {
            map.put(node.getWord(!anagrams), node);
        }

        if (node.getTrieNode().hasChildren()) {

            List<Integer> neighbours = getNeighbours(visited, anagrams, pos);
            for(Integer val : neighbours) {
                BasicTrieNode nextNode = node.getTrieNode().getChild(tiles[val].letter(), !anagrams);
                if (nextNode != null) {
                    DFS(visited, node.getChild(tiles[val], nextNode), map, val, anagrams);
                }
            }
        }

        visited[pos] = false;
    }

    List<Integer> getNeighbours(boolean[] visited, boolean anagram, int position) {
        List<Integer> neighbours = new ArrayList<>(visited.length);

        if(anagram) {
            for (int pos = 0; pos < visited.length; pos++) {
                if(!visited[pos]) {
                    neighbours.add(pos);
                }
            }
        } else {
            for (int col = 0; col < wordhunt.length(); col++) {
                if (amGraph[position][col] && !visited[col]) {
                    neighbours.add(col);
                }
            }
        }

        return neighbours;
    }



    public static void main(String[] args) {
        WordHuntSolver player = new WordHuntSolver();

        player.updateWordHunt("ABCDEFGHIKLMNOPR", 4);

        List<String> word3 = player.solveWordHunt();

        for (String word: word3) {
            System.out.println(word);
        }


    }


}
