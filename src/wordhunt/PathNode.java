package wordhunt;


import trie.BasicTrieNode;

public class PathNode {
    /*
     * Rep Invariant:
     * - Parent must be a vertex that the child is connected to for all nodes other than the root.
     * - trieNode must be a non-null BasicTrieNode that represents the end of a current path in the trie structure.
     * - The parent of the root node must be null.
     *
     * Abstraction Function:
     * - The WordPath class represents a path in a graph and its associated node in a trie.
     * - Each path can be determined by following parent vertices in the tree until the root has been reached
     * - node is a BasicTrieNode in a trie that represents the node in the Trie corresponding to the path.
     */

    private final PathNode parent;
    private final Tile tile;
    private final BasicTrieNode trieNode;

    /**
     * Creates a new WordPath object with the root node and starting vertex
     * @param tile  Starting vertex
     * @param rootNode  rootNode in a trie stucture
     */
    public PathNode(Tile tile, BasicTrieNode rootNode) {
        parent = null;
        this.tile = tile;
        trieNode = rootNode;
    }

    /**
     * Private contructor to construct children
     * @param currParent parent of the child
     * @param currTile Vertex of the child
     * @param currNode Trie node of the child
     */
    private PathNode(PathNode currParent, Tile currTile, BasicTrieNode currNode) {
        parent = currParent;
        trieNode = currNode;
        tile = currTile;
    }

    /**
     * Creates a new WordPath object with the given vertex and node.
     * @param nextVertex    vertex to add to the path
     * @param newNode    node in a trie representing the end of a word corresponding to the path
     * @return  a new WordPath object with the given vertex and node
     */
    public PathNode getChild(Tile nextTile, BasicTrieNode newNode) {
        return new PathNode(this, nextTile, newNode);
    }

    /**
     * Simple getter for the parent
     * @return Parent of the WordPath node
     */
    public PathNode getParent() {
        return parent;
    }

    /**
     * Simple getter function for the vertex
     * @return Vertex of the current WordPath node
     */
    public Tile getTile() {
        return tile;
    }

    /**
     * Simple getter function for the BasicTrieNode
     * @return BasicTrieNode of the current WordPath node
     */
    public BasicTrieNode getTrieNode() {
        return trieNode;
    }

    /**
     * Works up the tree to construct a word from the characters of each vertex
     * @return A word representing the path of the current node
     */
    public String getWord(boolean QsAsQUs) {
        StringBuilder sb = new StringBuilder();
        PathNode current = this;

        while (current!=null) {
            if (QsAsQUs && current.getTile().name().charAt(0) == 'Q') {
                sb.insert(0, "U");
            }

            sb.insert(0, current.getTile().name());

            current = current.getParent();
        }

        return sb.toString();
    }

    /**
     * Works up the path to see if a possible vertex is repeating
     * @param nextVertex Vertex to check the path for
     * @return True if the vertex is in the path and it would be repeating
     *         False if the vertex is not in the path.
     */
    public boolean isRepeating(Tile nextTile) {
        PathNode current = this;

        while (current != null) {
            if (current.getTile().equals(nextTile)) {
                return true;
            }
            current = current.getParent();
        }

        return false;
    }
}
