import wordhunt.PathNode;
import wordhunt.WordHuntSolver;
import wordhunt.Tile;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GUI extends JFrame {

    private DefaultListModel<String> listModel;
    private JList<String> wordList;
    private WordHuntSolver wordHuntSolver;

    public GUI(List<String> words, WordHuntSolver solver) {

        super("Anagrams Solver");

        // Initialize the WordHuntSolver
        this.wordHuntSolver = solver;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // INITIALIZE COMPONENTS

        //List model as a JScrollPane to display solved words
        listModel = new DefaultListModel<>();
        for (String word : words) {
            listModel.addElement(word);
        }

        wordList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(wordList);
        Font largerFont = new Font(wordList.getFont().getName(), Font.PLAIN, 20); // Adjust the size as needed
        wordList.setFont(largerFont);


        // Create a panel as a barrier that can display the tiles
        JPanel barrierPanel = new JPanel();
        barrierPanel.setBackground(new Color(34, 139, 34));  // Forest Green
        barrierPanel.setPreferredSize(new Dimension(600, 600));

        // Set up layout
        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        container.add(scrollPane, BorderLayout.CENTER);
        container.add(barrierPanel, BorderLayout.EAST);

        //Set up listener to track user interaction with the solved words
        ListListener selectionListener = new ListListener(barrierPanel, false);
        wordList.addListSelectionListener(selectionListener);


        // Create a menu to have buttons that update the wordhunt and anagram boards
        JMenuBar menuBar = new JMenuBar();
        JMenu optionsMenu = new JMenu("Options");
        JMenuItem updateAnagramsItem = new JMenuItem("Update Anagrams");
        JMenuItem updateWordhuntItem = new JMenuItem("Update Wordhunt");

        updateAnagramsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateAnagrams();
                updateTilesAnagram(barrierPanel, "");
                selectionListener.updateFlag(true);
            }
        });

        updateWordhuntItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                updateWordhunt();
                updateTilesWordhunt(barrierPanel);
                selectionListener.updateFlag(false);
            }
        });

        optionsMenu.add(updateAnagramsItem);
        optionsMenu.add(updateWordhuntItem);
        menuBar.add(optionsMenu);
        setJMenuBar(menuBar);
        setVisible(true);
    }

    // Method to update tiles in the barrier panel
    private void updateTilesAnagram(JPanel panel, String word) {
        if (wordHuntSolver != null && wordHuntSolver.tiles != null) {
            panel.removeAll(); // Clear existing tiles before adding new ones

            // Use GridBagLayout for barrierPanel
            panel.setLayout(new GridBagLayout());

            int spacing = 10;
            GridBagConstraints gbc = new GridBagConstraints();

            for (int pos = 0; pos < wordHuntSolver.tiles.length; pos++) {
                gbc.gridx = pos;
                gbc.gridy = 0; // Use the index as the gridy value
                gbc.insets = new Insets(0, spacing/2, spacing*2, spacing/2); // Set left spacing for each tile

                panel.add(wordHuntSolver.tiles[pos], gbc);
            }

            int wordLen = word.length();
            for (int letter = 0; letter < wordLen; letter++) {
                char let = word.charAt(letter);
                gbc.gridx = letter;
                gbc.gridy = 1; // Use the index as the gridy value
                gbc.insets = new Insets(spacing*2, spacing/2, 0, spacing/2); // Set left spacing for each tile

                panel.add(new Tile(let, 0,wordHuntSolver.tiles.length+1), gbc);
            }


            panel.revalidate();
            panel.repaint();
        }
    }

    private void updateTilesWordhunt(JPanel panel) {
        if (wordHuntSolver != null && wordHuntSolver.tiles != null) {
            panel.removeAll();
            panel.setLayout(new GridBagLayout());

            int spacing = 5;
            GridBagConstraints gbc = new GridBagConstraints();

            for (int col = 0; col < wordHuntSolver.colLen; col++) {
                for(int row = 0; row < wordHuntSolver.rowLen; row++) {
                    gbc.gridx = row;
                    gbc.gridy = col;
                    gbc.insets = new Insets(spacing,spacing,spacing,spacing);

                    panel.add(wordHuntSolver.tiles[col*wordHuntSolver.rowLen + row], gbc);
                }
            }

            panel.revalidate();
            panel.repaint();
        }
    }

    // Method to update anagrams using user input
    private void updateAnagrams() {

        while(true) {
            String userInput = JOptionPane.showInputDialog(this, "Enter a new set of anagrams:");
            if (userInput != null && !userInput.isEmpty()) {
                try {
                    wordHuntSolver.updateAnagrams(userInput.toUpperCase());
                    List<String> updatedWords = wordHuntSolver.solveAnagrams();
                    updateWordList(updatedWords);
                    break;
                } catch (IllegalArgumentException e) {
                    JOptionPane.showMessageDialog(this, "Must enter only alphabetical characters");
                    continue;
                }
            }
            break;
        }
    }

    private void updateWordhunt() {
        while (true) {
            String userInput = JOptionPane.showInputDialog(this, "Enter the WordHunt characters from left to right then top to bottom:");
            String userInput2 = JOptionPane.showInputDialog(this, "Enter the row width:");
            if (userInput != null && !userInput.isEmpty()) {
                try {
                    wordHuntSolver.updateWordHunt(userInput.toUpperCase(), Integer.parseInt(userInput2));
                    List<String> updatedWords = wordHuntSolver.solveWordHunt();
                    updateWordList(updatedWords);
                } catch (IllegalArgumentException e) {
                    if (e.getMessage().equals("Must be a square or rectangle")) {
                        JOptionPane.showMessageDialog(this, "Please enter a string representing a complete square or rectangle.");
                    } else if (e.getMessage().equals("Only alphabetical strings")) {
                        JOptionPane.showMessageDialog(this, "Must enter a string of only alphabetical characters.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Please enter the row width as a single number");
                    }
                    continue;
                }
            }
            break;
        }
    }

    // Helper method to update the word list
    private void updateWordList(List<String> updatedWords) {
        listModel.clear();
        for (String word : updatedWords) {
            listModel.addElement(word);
        }
    }

    public static void main(String[] args) {
        WordHuntSolver player = new WordHuntSolver();
        player.updateAnagrams("");

        List<String> words = player.solveAnagrams();

        SwingUtilities.invokeLater(() -> {
            GUI gui = new GUI(words, player);
        });
    }

    public class ListListener implements ListSelectionListener {

        private JPanel panel;
        private int lastIndex = 0;
        private boolean lastBool = false;
        private boolean anagram;
        public ListListener(JPanel panel, boolean anagram) {
            this.panel = panel;
            this.anagram = anagram;
        }

        public void updateFlag(boolean data) {
            this.anagram = data;
        }
        @Override
        public void valueChanged(ListSelectionEvent listSelectionEvent) {
            if(lastBool) {
                lastBool = false;
                return;
            }
            if (listModel.isEmpty()) {
                return;
            }
            for (int pos = 0; pos < wordHuntSolver.tiles.length; pos++) {
                wordHuntSolver.tiles[pos].setIconWood();
            }
            lastBool = listSelectionEvent.getValueIsAdjusting();
            int index;
            if (listSelectionEvent.getLastIndex() == lastIndex) {
                index = listSelectionEvent.getFirstIndex();
                lastIndex = listSelectionEvent.getFirstIndex();
            } else {
                index = listSelectionEvent.getLastIndex();
                lastIndex = listSelectionEvent.getLastIndex();
            }
            String word = listModel.get(index);
            PathNode current = wordHuntSolver.map.get(word);

            while (current != null) {
                current.getTile().setIconYellow();
                current = current.getParent();
                if (!anagram && current.getParent() == null) {
                    current.getTile().setIconOrange();
                    current = current.getParent();
                }
            }

            if (anagram) {
                updateTilesAnagram(panel, word);
            } else {
                updateTilesWordhunt(panel);
            }
        }
    }
}
