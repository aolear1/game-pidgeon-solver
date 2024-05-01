package trie;


public class Trie {
    private final BasicTrieNode root;

    /**
     * Initializes a trie with the given dictionary.
     * @param dictionary    Array of words to be inserted into the trie
     */
    public Trie(String[] dictionary) {
        root = new BasicTrieNode();
        for (String word : dictionary) {
            insert(word);
        }
    }

    public Trie() {
        root = new BasicTrieNode();
    }

    /**
     * Returns the root of the trie.
     * @return  root of the trie
     */
    public BasicTrieNode getRoot() {
        return root;
    }

    /**
     * Inserts a word into the trie.
     * @param word  word to be inserted
     */
    public void insert(String word) {
        int currentDepth;
        int maxDepth = word.length();
        int child;

        BasicTrieNode currentNode = root;

        for (currentDepth = 0; currentDepth < maxDepth; currentDepth++) {
            child = word.charAt(currentDepth) - 'A';
            currentNode.insertChild(child);

            currentNode = currentNode.children[child];
        }
        //Will set the node to be true only if its length is 3+
        if(currentDepth > 2) {
            currentNode.setWord();
        }

    }

}

