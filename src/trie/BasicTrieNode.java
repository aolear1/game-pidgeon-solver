package trie;

public class BasicTrieNode {
    /*
     * Rep Invariant:
     * - Each BasicTrieNode in the trie must have an array children of size 26 (for each letter of the alphabet) or null.
     * - The isWord field of each BasicTrieNode must correctly indicate whether the node represents the end of a word in the trie.
     *
     * Abstraction Function:
     * - Each BasicTrieNode represents a node in the trie, and its children array represents the child nodes corresponding to each letter of the alphabet.
     * - The isWord field of a BasicTrieNode represents whether the node is the end of a word in the trie.
     */
    public BasicTrieNode[] children;
    private boolean isWord = false;


    public BasicTrieNode() {
        //children array left uninitialized to save space
    }

    /**
     * Inserts a child node at the given position in the children array.
     * @param pos   position [0,25] in the children array to insert the child node
     *              corresponding to the letter of the child where 0 = 'A', 1 = 'B'...
     */
    public void insertChild(int pos) {
        //will only initialize  BasicTrieNode[] children to insert a child
        if (children == null) {
            children = new BasicTrieNode[26];
        }

        if (children[pos] == null) {
            children[pos] = new BasicTrieNode();
        }
    }

    /**
     * Checks whether the node has children.
     * @return  true if the node has children, false otherwise
     */
    public boolean hasChildren() {
        return children != null;
    }

    /**
     * Gets the isWord flag for the node
     * @return boolean indicating whether the node represents the end of a word or not
     */
    public boolean isWord() {
        return isWord;
    }

    /**
     * Sets the isWord flag for the node to be true
     */
    public void setWord() {
        this.isWord = true;
    }

    /**
     * Returns the child of the node for an inputted character position.
     * @param position Character to get the child of. Must be an upper case
     *                 alphabetical character ('A','Z')
     * @param QsAsQUs Boolean flag to indicate whether 'Q' is to be treated as a 'QU'
     * @return the child of the current node t the inputted position. If no node
     *         exists, returns null
     */
    public BasicTrieNode getChild(char position, boolean QsAsQUs) {
        if (this.hasChildren()) {
            if (QsAsQUs && position == 'Q') {
                if (children['Q' - 'A'] == null) {
                    return null;
                }
                return children[position-'A'].getChild('U', true);
            }
            return children[position - 'A'];
        }
        return null;
    }

}
