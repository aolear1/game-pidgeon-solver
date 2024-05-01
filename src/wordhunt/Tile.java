package wordhunt;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Tile extends JLabel{
    private char letter;
    private int id;

    private ImageIcon wood,yellow,orange;

    public Tile(char letter, int id, int totalDivisor) {
        this.letter = letter;
        this.id = id;

        setLayout(new BorderLayout());
        int tileSize = 600 / totalDivisor - 20;
        setPreferredSize(new Dimension(tileSize, tileSize)); // Set the preferred size of the tile

        try {
            Image woodImage = ImageIO.read(new File("wood.png"));
            Image yellowWood = ImageIO.read(new File("yellow.png"));
            Image orangeWood = ImageIO.read(new File("orange.png"));
            int scaled = (int)( tileSize * 1.2);

            Image scaledWoodImage = woodImage.getScaledInstance(scaled, scaled, Image.SCALE_SMOOTH);
            Image scaledYellowWood = yellowWood.getScaledInstance(scaled, scaled, Image.SCALE_SMOOTH);
            Image scaledOrangeWood = orangeWood.getScaledInstance(scaled,scaled,scaled);

            wood = new ImageIcon(scaledWoodImage);
            yellow = new ImageIcon(scaledYellowWood);
            orange = new ImageIcon(scaledOrangeWood);

            setText(String.valueOf(letter));
            setFont(new Font(Font.SANS_SERIF, Font.BOLD, (int) (tileSize * 0.4)));
            setIcon(wood);

            setHorizontalTextPosition(JLabel.CENTER);
            setVerticalTextPosition(JLabel.CENTER);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setIconYellow() {
        setIcon(yellow);
    }
    public void setIconWood() {
        setIcon(wood);
    }
    public void setIconOrange() {
        setIcon(orange);
    }

    public String name() {
        return String.valueOf(this.letter);
    }

    public char letter() {
        return letter;
    }
    public int getID() {
        return id;
    }
}
