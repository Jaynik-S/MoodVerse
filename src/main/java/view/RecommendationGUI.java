package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class RecommendationGUI {
    // Helper: load an image from URL and scale to given size. Returns null on error.
    private static ImageIcon loadAndScale(String imageUrl, int targetW, int targetH) {
        try {
            BufferedImage img = ImageIO.read(new URL(imageUrl));
            if (img == null) return null;
            Image scaled = img.getScaledInstance(targetW, targetH, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (IOException e) {
            return null;
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Recommendation View");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            // Default window size (w x h)
            frame.setSize(1000, 800);
            // Prevent the window from being resized smaller than a usable minimum
            frame.setMinimumSize(new Dimension(800, 600));
            // Make the window non-resizable (fixed size)
            frame.setResizable(false);
            frame.setLocationRelativeTo(null);

            // Top panel with a left-aligned Back button
            JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JButton backButton = new JButton("Back");
            backButton.addActionListener(e -> frame.dispose());
            topPanel.add(backButton);

            // Main area: two panels side-by-side
            JPanel mainPanel = new JPanel(new GridLayout(1, 2, 8, 0));
               JPanel leftPanel = new JPanel(new BorderLayout());
               leftPanel.setBorder(BorderFactory.createTitledBorder("Songs"));
               JPanel leftList = new JPanel();
               leftList.setLayout(new BoxLayout(leftList, BoxLayout.Y_AXIS));
                // Unrolled song items (5 copies) to avoid a for-loop
                {
                    JPanel item = new JPanel(new BorderLayout(8, 8));
                    item.setBorder(BorderFactory.createTitledBorder("Song #1"));


                    // Cover placeholder (left) -> try loading real image and scale to 120x120
                    JLabel cover = new JLabel();
                    ImageIcon icon = loadAndScale("https://i.scdn.co/image/ab67616d0000b2739fc26020e0f961c076f09145", 120, 120);
                    if (icon != null) {
                        cover.setIcon(icon);
                    } else {
                        cover.setText("<html><div style='text-align:center;'>COVER<br>IMAGE<br>(640x640)</div></html>");
                        cover.setHorizontalAlignment(JLabel.CENTER);
                    }
                    cover.setPreferredSize(new Dimension(120, 120));
                    cover.setMinimumSize(new Dimension(120, 120));
                    cover.setMaximumSize(new Dimension(120, 120));
                    JPanel coverWrap = new JPanel(new BorderLayout());
                    coverWrap.add(cover, BorderLayout.CENTER);
                    coverWrap.setMinimumSize(new Dimension(120, 120));
                    coverWrap.setPreferredSize(new Dimension(120, 120));
                    coverWrap.setMaximumSize(new Dimension(120, 120));
                    coverWrap.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.GRAY),
                        BorderFactory.createEmptyBorder(4, 4, 4, 4)));

                    // Details (right)
                    JPanel details = new JPanel(new BorderLayout(6, 6));
                    JPanel nameRow = new JPanel(new BorderLayout(6, 6));
                    JTextField nameField = new JTextField("Song Name");
                    JTextField scoreField = new JTextField("score");
                    scoreField.setPreferredSize(new Dimension(80, 40));
                    scoreField.setHorizontalAlignment(JTextField.CENTER);
                    scoreField.setEditable(false);
                    nameField.setEditable(false);
                    nameRow.add(nameField, BorderLayout.CENTER);
                    nameRow.add(scoreField, BorderLayout.EAST);

                    JPanel artistRow = new JPanel(new BorderLayout(6, 6));
                    JTextField artistField = new JTextField("Artist");
                    JTextField yearField = new JTextField("Year");
                    yearField.setPreferredSize(new Dimension(80, 40));
                    yearField.setHorizontalAlignment(JTextField.CENTER);
                    artistField.setEditable(false);
                    yearField.setEditable(false);
                    artistRow.add(artistField, BorderLayout.CENTER);
                    artistRow.add(yearField, BorderLayout.EAST);

                    JTextField urlField = new JTextField("https://example.com/track...");
                    urlField.setEditable(false);

                    details.add(nameRow, BorderLayout.NORTH);
                    details.add(artistRow, BorderLayout.CENTER);
                    details.add(urlField, BorderLayout.SOUTH);

                    item.add(coverWrap, BorderLayout.WEST);
                    item.add(details, BorderLayout.CENTER);

                    int itemHeight = 120;
                    item.setMaximumSize(new Dimension(500, itemHeight));
                    item.setPreferredSize(new Dimension(500, itemHeight));
                    item.setAlignmentX(Component.LEFT_ALIGNMENT);
                    leftList.add(item);
                    leftList.add(Box.createRigidArea(new Dimension(0, 6)));
                }
                {
                    JPanel item = new JPanel(new BorderLayout(8, 8));
                    item.setBorder(BorderFactory.createTitledBorder("Song #2"));

                    JLabel cover = new JLabel();
                    ImageIcon icon = loadAndScale("https://i.scdn.co/image/ab67616d0000b2739fc26020e0f961c076f09145", 120, 120);
                    if (icon != null) {
                        cover.setIcon(icon);
                    } else {
                        cover.setText("<html><div style='text-align:center;'>COVER<br>IMAGE<br>(640x640)</div></html>");
                        cover.setHorizontalAlignment(JLabel.CENTER);
                    }
                    JPanel coverWrap = new JPanel(new BorderLayout());
                    coverWrap.add(cover, BorderLayout.CENTER);
                    cover.setPreferredSize(new Dimension(120, 120));
                    cover.setMinimumSize(new Dimension(120, 120));
                    cover.setMaximumSize(new Dimension(120, 120));
                    coverWrap.setMinimumSize(new Dimension(120, 120));
                    coverWrap.setPreferredSize(new Dimension(120, 120));
                    coverWrap.setMaximumSize(new Dimension(120, 120));
                    coverWrap.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.GRAY),
                        BorderFactory.createEmptyBorder(4, 4, 4, 4)));

                    JPanel details = new JPanel(new BorderLayout(6, 6));
                    JPanel nameRow = new JPanel(new BorderLayout(6, 6));
                    JTextField nameField = new JTextField("Song Name");
                    JTextField scoreField = new JTextField("score");
                    scoreField.setPreferredSize(new Dimension(80, 40));
                    scoreField.setHorizontalAlignment(JTextField.CENTER);
                    scoreField.setEditable(false);
                    nameField.setEditable(false);
                    nameRow.add(nameField, BorderLayout.CENTER);
                    nameRow.add(scoreField, BorderLayout.EAST);

                    JPanel artistRow = new JPanel(new BorderLayout(6, 6));
                    JTextField artistField = new JTextField("Artist");
                    JTextField yearField = new JTextField("Year");
                    yearField.setPreferredSize(new Dimension(80, 40));
                    yearField.setHorizontalAlignment(JTextField.CENTER);
                    artistField.setEditable(false);
                    yearField.setEditable(false);
                    artistRow.add(artistField, BorderLayout.CENTER);
                    artistRow.add(yearField, BorderLayout.EAST);

                    JTextField urlField = new JTextField("https://example.com/track...");
                    urlField.setEditable(false);

                    details.add(nameRow, BorderLayout.NORTH);
                    details.add(artistRow, BorderLayout.CENTER);
                    details.add(urlField, BorderLayout.SOUTH);

                    item.add(coverWrap, BorderLayout.WEST);
                    item.add(details, BorderLayout.CENTER);

                    int itemHeight = 120;
                    item.setMaximumSize(new Dimension(Integer.MAX_VALUE, itemHeight));
                    item.setPreferredSize(new Dimension(Short.MAX_VALUE, itemHeight));
                    item.setAlignmentX(Component.LEFT_ALIGNMENT);
                    leftList.add(item);
                    leftList.add(Box.createRigidArea(new Dimension(0, 6)));
                }
                {
                    JPanel item = new JPanel(new BorderLayout(8, 8));
                    item.setBorder(BorderFactory.createTitledBorder("Song #3"));

                    JLabel cover = new JLabel();
                    ImageIcon icon = loadAndScale("https://i.scdn.co/image/ab67616d0000b2739fc26020e0f961c076f09145", 120, 120);
                    if (icon != null) {
                        cover.setIcon(icon);
                    } else {
                        cover.setText("<html><div style='text-align:center;'>COVER<br>IMAGE<br>(640x640)</div></html>");
                        cover.setHorizontalAlignment(JLabel.CENTER);
                    }
                    JPanel coverWrap = new JPanel(new BorderLayout());
                    coverWrap.add(cover, BorderLayout.CENTER);
                    cover.setPreferredSize(new Dimension(120, 120));
                    cover.setMinimumSize(new Dimension(120, 120));
                    cover.setMaximumSize(new Dimension(120, 120));
                    coverWrap.setMinimumSize(new Dimension(120, 120));
                    coverWrap.setPreferredSize(new Dimension(120, 120));
                    coverWrap.setMaximumSize(new Dimension(120, 120));
                    coverWrap.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.GRAY),
                        BorderFactory.createEmptyBorder(4, 4, 4, 4)));

                    JPanel details = new JPanel(new BorderLayout(6, 6));
                    JPanel nameRow = new JPanel(new BorderLayout(6, 6));
                    JTextField nameField = new JTextField("Song Name");
                    JTextField scoreField = new JTextField("score");
                    scoreField.setPreferredSize(new Dimension(80, 40));
                    scoreField.setHorizontalAlignment(JTextField.CENTER);
                    scoreField.setEditable(false);
                    nameField.setEditable(false);
                    nameRow.add(nameField, BorderLayout.CENTER);
                    nameRow.add(scoreField, BorderLayout.EAST);

                    JPanel artistRow = new JPanel(new BorderLayout(6, 6));
                    JTextField artistField = new JTextField("Artist");
                    JTextField yearField = new JTextField("Year");
                    yearField.setPreferredSize(new Dimension(80, 40));
                    yearField.setHorizontalAlignment(JTextField.CENTER);
                    artistField.setEditable(false);
                    yearField.setEditable(false);
                    artistRow.add(artistField, BorderLayout.CENTER);
                    artistRow.add(yearField, BorderLayout.EAST);

                    JTextField urlField = new JTextField("https://example.com/track...");
                    urlField.setEditable(false);

                    details.add(nameRow, BorderLayout.NORTH);
                    details.add(artistRow, BorderLayout.CENTER);
                    details.add(urlField, BorderLayout.SOUTH);

                    item.add(coverWrap, BorderLayout.WEST);
                    item.add(details, BorderLayout.CENTER);

                    int itemHeight = 120;
                    item.setMaximumSize(new Dimension(Integer.MAX_VALUE, itemHeight));
                    item.setPreferredSize(new Dimension(Short.MAX_VALUE, itemHeight));
                    item.setAlignmentX(Component.LEFT_ALIGNMENT);
                    leftList.add(item);
                    leftList.add(Box.createRigidArea(new Dimension(0, 6)));
                }
                {
                    JPanel item = new JPanel(new BorderLayout(8, 8));
                    item.setBorder(BorderFactory.createTitledBorder("Song #4"));

                    JLabel cover = new JLabel();
                    ImageIcon icon = loadAndScale("https://i.scdn.co/image/ab67616d0000b2739fc26020e0f961c076f09145", 120, 120);
                    if (icon != null) {
                        cover.setIcon(icon);
                    } else {
                        cover.setText("<html><div style='text-align:center;'>COVER<br>IMAGE<br>(640x640)</div></html>");
                        cover.setHorizontalAlignment(JLabel.CENTER);
                    }
                    JPanel coverWrap = new JPanel(new BorderLayout());
                    coverWrap.add(cover, BorderLayout.CENTER);
                    cover.setPreferredSize(new Dimension(120, 120));
                    cover.setMinimumSize(new Dimension(120, 120));
                    cover.setMaximumSize(new Dimension(120, 120));
                    coverWrap.setMinimumSize(new Dimension(120, 120));
                    coverWrap.setPreferredSize(new Dimension(120, 120));
                    coverWrap.setMaximumSize(new Dimension(120, 120));
                    coverWrap.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.GRAY),
                        BorderFactory.createEmptyBorder(4, 4, 4, 4)));

                    JPanel details = new JPanel(new BorderLayout(6, 6));
                    JPanel nameRow = new JPanel(new BorderLayout(6, 6));
                    JTextField nameField = new JTextField("Song Name");
                    JTextField scoreField = new JTextField("score");
                    scoreField.setPreferredSize(new Dimension(80, 40));
                    scoreField.setHorizontalAlignment(JTextField.CENTER);
                    scoreField.setEditable(false);
                    nameField.setEditable(false);
                    nameRow.add(nameField, BorderLayout.CENTER);
                    nameRow.add(scoreField, BorderLayout.EAST);

                    JPanel artistRow = new JPanel(new BorderLayout(6, 6));
                    JTextField artistField = new JTextField("Artist");
                    JTextField yearField = new JTextField("Year");
                    yearField.setPreferredSize(new Dimension(80, 40));
                    yearField.setHorizontalAlignment(JTextField.CENTER);
                    artistField.setEditable(false);
                    yearField.setEditable(false);
                    artistRow.add(artistField, BorderLayout.CENTER);
                    artistRow.add(yearField, BorderLayout.EAST);

                    JTextField urlField = new JTextField("https://example.com/track...");
                    urlField.setEditable(false);

                    details.add(nameRow, BorderLayout.NORTH);
                    details.add(artistRow, BorderLayout.CENTER);
                    details.add(urlField, BorderLayout.SOUTH);

                    item.add(coverWrap, BorderLayout.WEST);
                    item.add(details, BorderLayout.CENTER);

                    int itemHeight = 120;
                    item.setMaximumSize(new Dimension(Integer.MAX_VALUE, itemHeight));
                    item.setPreferredSize(new Dimension(Short.MAX_VALUE, itemHeight));
                    item.setAlignmentX(Component.LEFT_ALIGNMENT);
                    leftList.add(item);
                    leftList.add(Box.createRigidArea(new Dimension(0, 6)));
                }
                {
                    JPanel item = new JPanel(new BorderLayout(8, 8));
                    item.setBorder(BorderFactory.createTitledBorder("Song #5"));

                    JLabel cover = new JLabel();
                    ImageIcon icon = loadAndScale("https://i.scdn.co/image/ab67616dDD0000b2739fc26020e0f961c076f09145", 120, 120);
                    if (icon != null) {
                        cover.setIcon(icon);
                    } else {
                        cover.setText("<html><div style='text-align:center;'>COVER<br>IMAGE<br>(640x640)</div></html>");
                        cover.setHorizontalAlignment(JLabel.CENTER);
                    }
                    JPanel coverWrap = new JPanel(new BorderLayout());
                    coverWrap.add(cover, BorderLayout.CENTER);
                    cover.setPreferredSize(new Dimension(120, 120));
                    cover.setMinimumSize(new Dimension(120, 120));
                    cover.setMaximumSize(new Dimension(120, 120));
                    coverWrap.setMinimumSize(new Dimension(120, 120));
                    coverWrap.setPreferredSize(new Dimension(120, 120));
                    coverWrap.setMaximumSize(new Dimension(120, 120));
                    coverWrap.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.GRAY),
                        BorderFactory.createEmptyBorder(4, 4, 4, 4)));

                    JPanel details = new JPanel(new BorderLayout(6, 6));
                    JPanel nameRow = new JPanel(new BorderLayout(6, 6));
                    JTextField nameField = new JTextField("Song Name");
                    JTextField scoreField = new JTextField("score");
                    scoreField.setPreferredSize(new Dimension(80, 40));
                    scoreField.setHorizontalAlignment(JTextField.CENTER);
                    scoreField.setEditable(false);
                    nameField.setEditable(false);
                    nameRow.add(nameField, BorderLayout.CENTER);
                    nameRow.add(scoreField, BorderLayout.EAST);

                    JPanel artistRow = new JPanel(new BorderLayout(6, 6));
                    JTextField artistField = new JTextField("Artist");
                    JTextField yearField = new JTextField("Year");
                    yearField.setPreferredSize(new Dimension(80, 40));
                    yearField.setHorizontalAlignment(JTextField.CENTER);
                    artistField.setEditable(false);
                    yearField.setEditable(false);
                    artistRow.add(artistField, BorderLayout.CENTER);
                    artistRow.add(yearField, BorderLayout.EAST);

                    JTextField urlField = new JTextField("https://example.com/track...");
                    urlField.setEditable(false);

                    details.add(nameRow, BorderLayout.NORTH);
                    details.add(artistRow, BorderLayout.CENTER);
                    details.add(urlField, BorderLayout.SOUTH);

                    item.add(coverWrap, BorderLayout.WEST);
                    item.add(details, BorderLayout.CENTER);

                    int itemHeight = 120;
                    item.setMaximumSize(new Dimension(Integer.MAX_VALUE, itemHeight));
                    item.setPreferredSize(new Dimension(Short.MAX_VALUE, itemHeight));
                    item.setAlignmentX(Component.LEFT_ALIGNMENT);
                    leftList.add(item);
                    leftList.add(Box.createRigidArea(new Dimension(0, 6)));
                }
               leftPanel.add(leftList, BorderLayout.CENTER);

               JPanel rightPanel = new JPanel(new BorderLayout());
               rightPanel.setBorder(BorderFactory.createTitledBorder("Movies"));
               JPanel rightList = new JPanel();
               rightList.setLayout(new BoxLayout(rightList, BoxLayout.Y_AXIS));
                // Unrolled movie items (4 copies) to avoid a for-loop
                {
                    JPanel item = new JPanel(new BorderLayout(8, 8));
                    item.setBorder(BorderFactory.createTitledBorder("Movie #1"));

                    JLabel cover = new JLabel();
                    ImageIcon icon = loadAndScale("https://image.tmdb.org/t/p/original/qweKCtPdnIP2uGp1PgWZyCV7gzj.jpg", 100, 150);
                    if (icon != null) {
                        cover.setIcon(icon);
                    } else {
                        cover.setText("<html><div style='text-align:center;'>COVER<br>IMAGE<br>(847x1271)</div></html>");
                        cover.setHorizontalAlignment(JLabel.CENTER);
                    }
                    cover.setPreferredSize(new Dimension(100, 150));
                    cover.setMinimumSize(new Dimension(100, 150));
                    cover.setMaximumSize(new Dimension(100, 150));
                    JPanel coverWrap = new JPanel(new BorderLayout());
                    coverWrap.add(cover, BorderLayout.CENTER);
                    coverWrap.setPreferredSize(new Dimension(100, 150));
                    coverWrap.setMinimumSize(new Dimension(100, 150));
                    coverWrap.setMaximumSize(new Dimension(100, 150));
                    coverWrap.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.GRAY),
                        BorderFactory.createEmptyBorder(4, 4, 4, 4)));

                    JPanel details = new JPanel(new BorderLayout(6, 6));
                    JPanel topRow = new JPanel(new BorderLayout(6, 6));
                    JTextField titleField = new JTextField("Title");
                    titleField.setEditable(false);
                    JPanel smallBoxes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
                    JTextField yearField = new JTextField("year");
                    yearField.setPreferredSize(new Dimension(80, 40));
                    yearField.setHorizontalAlignment(JTextField.CENTER);
                    yearField.setEditable(false);
                    JTextField ratingField = new JTextField("rating");
                    ratingField.setPreferredSize(new Dimension(80, 40));
                    ratingField.setHorizontalAlignment(JTextField.CENTER);
                    ratingField.setEditable(false);
                    smallBoxes.add(yearField);
                    smallBoxes.add(ratingField);
                    topRow.add(titleField, BorderLayout.CENTER);
                    topRow.add(smallBoxes, BorderLayout.EAST);
                    JTextField descField = new JTextField("Description of the movie");
                    descField.setEditable(false);
                    descField.setPreferredSize(new Dimension(200, 100));
                    details.add(topRow, BorderLayout.NORTH);
                    details.add(descField, BorderLayout.CENTER);
                    item.add(coverWrap, BorderLayout.WEST);
                    item.add(details, BorderLayout.CENTER);
                    int itemHeight = 150;
                    item.setMaximumSize(new Dimension(Integer.MAX_VALUE, itemHeight));
                    item.setPreferredSize(new Dimension(Short.MAX_VALUE, itemHeight));
                    item.setAlignmentX(Component.LEFT_ALIGNMENT);
                    rightList.add(item);
                    rightList.add(Box.createRigidArea(new Dimension(0, 6)));
                }
                {
                    JPanel item = new JPanel(new BorderLayout(8, 8));
                    item.setBorder(BorderFactory.createTitledBorder("Movie #2"));

                    JLabel cover = new JLabel();
                    ImageIcon icon = loadAndScale("https://image.tmdb.org/t/p/original/qweKCtPdnIP2uGp1PgWZyCV7gzj.jpg", 100, 150);
                    if (icon != null) {
                        cover.setIcon(icon);
                    } else {
                        cover.setText("<html><div style='text-align:center;'>COVER<br>IMAGE<br>(847x1271)</div></html>");
                        cover.setHorizontalAlignment(JLabel.CENTER);
                    }
                    cover.setPreferredSize(new Dimension(100, 150));
                    cover.setMinimumSize(new Dimension(100, 150));
                    cover.setMaximumSize(new Dimension(100, 150));
                    JPanel coverWrap = new JPanel(new BorderLayout());
                    coverWrap.add(cover, BorderLayout.CENTER);
                    coverWrap.setPreferredSize(new Dimension(100, 150));
                    coverWrap.setMinimumSize(new Dimension(100, 150));
                    coverWrap.setMaximumSize(new Dimension(100, 150));
                    coverWrap.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.GRAY),
                        BorderFactory.createEmptyBorder(4, 4, 4, 4)));

                    JPanel details = new JPanel(new BorderLayout(6, 6));
                    JPanel topRow = new JPanel(new BorderLayout(6, 6));
                    JTextField titleField = new JTextField("Title");
                    titleField.setEditable(false);
                    JPanel smallBoxes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
                    JTextField yearField = new JTextField("year");
                    yearField.setPreferredSize(new Dimension(80, 40));
                    yearField.setHorizontalAlignment(JTextField.CENTER);
                    yearField.setEditable(false);
                    JTextField ratingField = new JTextField("rating");
                    ratingField.setPreferredSize(new Dimension(80, 40));
                    ratingField.setHorizontalAlignment(JTextField.CENTER);
                    ratingField.setEditable(false);
                    smallBoxes.add(yearField);
                    smallBoxes.add(ratingField);
                    topRow.add(titleField, BorderLayout.CENTER);
                    topRow.add(smallBoxes, BorderLayout.EAST);
                    JTextField descField = new JTextField("Description of the movie");
                    descField.setEditable(false);
                    descField.setPreferredSize(new Dimension(200, 100));
                    details.add(topRow, BorderLayout.NORTH);
                    details.add(descField, BorderLayout.CENTER);
                    item.add(coverWrap, BorderLayout.WEST);
                    item.add(details, BorderLayout.CENTER);
                    int itemHeight = 150;
                    item.setMaximumSize(new Dimension(Integer.MAX_VALUE, itemHeight));
                    item.setPreferredSize(new Dimension(Short.MAX_VALUE, itemHeight));
                    item.setAlignmentX(Component.LEFT_ALIGNMENT);
                    rightList.add(item);
                    rightList.add(Box.createRigidArea(new Dimension(0, 6)));
                }
                {
                    JPanel item = new JPanel(new BorderLayout(8, 8));
                    item.setBorder(BorderFactory.createTitledBorder("Movie #3"));

                    JLabel cover = new JLabel();
                    ImageIcon icon = loadAndScale("https://image.tmdb.org/t/p/original/qwDeKCtPdnIP2uGp1PgWZyCV7gzj.jpg", 100, 150);
                    if (icon != null) {
                        cover.setIcon(icon);
                    } else {
                        cover.setText("<html><div style='text-align:center;'>COVER<br>IMAGE<br>(847x1271)</div></html>");
                        cover.setHorizontalAlignment(JLabel.CENTER);
                    }
                    cover.setPreferredSize(new Dimension(100, 150));
                    cover.setMinimumSize(new Dimension(100, 150));
                    cover.setMaximumSize(new Dimension(100, 150));
                    JPanel coverWrap = new JPanel(new BorderLayout());
                    coverWrap.add(cover, BorderLayout.CENTER);
                    coverWrap.setPreferredSize(new Dimension(100, 150));
                    coverWrap.setMinimumSize(new Dimension(100, 150));
                    coverWrap.setMaximumSize(new Dimension(100, 150));
                    coverWrap.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.GRAY),
                        BorderFactory.createEmptyBorder(4, 4, 4, 4)));

                    JPanel details = new JPanel(new BorderLayout(6, 6));
                    JPanel topRow = new JPanel(new BorderLayout(6, 6));
                    JTextField titleField = new JTextField("Title");
                    titleField.setEditable(false);
                    JPanel smallBoxes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
                    JTextField yearField = new JTextField("year");
                    yearField.setPreferredSize(new Dimension(80, 40));
                    yearField.setHorizontalAlignment(JTextField.CENTER);
                    yearField.setEditable(false);
                    JTextField ratingField = new JTextField("rating");
                    ratingField.setPreferredSize(new Dimension(80, 40));
                    ratingField.setHorizontalAlignment(JTextField.CENTER);
                    ratingField.setEditable(false);
                    smallBoxes.add(yearField);
                    smallBoxes.add(ratingField);
                    topRow.add(titleField, BorderLayout.CENTER);
                    topRow.add(smallBoxes, BorderLayout.EAST);
                    JTextField descField = new JTextField("Description of the movie");
                    descField.setEditable(false);
                    descField.setPreferredSize(new Dimension(200, 100));
                    details.add(topRow, BorderLayout.NORTH);
                    details.add(descField, BorderLayout.CENTER);
                    item.add(coverWrap, BorderLayout.WEST);
                    item.add(details, BorderLayout.CENTER);
                    int itemHeight = 150;
                    item.setMaximumSize(new Dimension(Integer.MAX_VALUE, itemHeight));
                    item.setPreferredSize(new Dimension(Short.MAX_VALUE, itemHeight));
                    item.setAlignmentX(Component.LEFT_ALIGNMENT);
                    rightList.add(item);
                    rightList.add(Box.createRigidArea(new Dimension(0, 6)));
                }
                {
                    JPanel item = new JPanel(new BorderLayout(8, 8));
                    item.setBorder(BorderFactory.createTitledBorder("Movie #4"));

                    JLabel cover = new JLabel();
                    ImageIcon icon = loadAndScale("https://image.tmdb.org/t/p/original/qweKCtPdnIP2uGp1PgWZyCV7gzj.jpg", 100, 150);
                    if (icon != null) {
                        cover.setIcon(icon);
                    } else {
                        cover.setText("<html><div style='text-align:center;'>COVER<br>IMAGE<br>(847x1271)</div></html>");
                        cover.setHorizontalAlignment(JLabel.CENTER);
                    }
                    cover.setPreferredSize(new Dimension(100, 150));
                    cover.setMinimumSize(new Dimension(100, 150));
                    cover.setMaximumSize(new Dimension(100, 150));
                    JPanel coverWrap = new JPanel(new BorderLayout());
                    coverWrap.add(cover, BorderLayout.CENTER);
                    coverWrap.setPreferredSize(new Dimension(100, 150));
                    coverWrap.setMinimumSize(new Dimension(100, 150));
                    coverWrap.setMaximumSize(new Dimension(100, 150));
                    coverWrap.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.GRAY),
                        BorderFactory.createEmptyBorder(4, 4, 4, 4)));

                    JPanel details = new JPanel(new BorderLayout(6, 6));
                    JPanel topRow = new JPanel(new BorderLayout(6, 6));
                    JTextField titleField = new JTextField("Title");
                    titleField.setEditable(false);
                    JPanel smallBoxes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
                    JTextField yearField = new JTextField("year");
                    yearField.setPreferredSize(new Dimension(80, 40));
                    yearField.setHorizontalAlignment(JTextField.CENTER);
                    yearField.setEditable(false);
                    JTextField ratingField = new JTextField("rating");
                    ratingField.setPreferredSize(new Dimension(80, 40));
                    ratingField.setHorizontalAlignment(JTextField.CENTER);
                    ratingField.setEditable(false);
                    smallBoxes.add(yearField);
                    smallBoxes.add(ratingField);
                    topRow.add(titleField, BorderLayout.CENTER);
                    topRow.add(smallBoxes, BorderLayout.EAST);
                    JTextField descField = new JTextField("Description of the movie");
                    descField.setEditable(false);
                    descField.setPreferredSize(new Dimension(200, 100));
                    details.add(topRow, BorderLayout.NORTH);
                    details.add(descField, BorderLayout.CENTER);
                    item.add(coverWrap, BorderLayout.WEST);
                    item.add(details, BorderLayout.CENTER);
                    int itemHeight = 150;
                    item.setMaximumSize(new Dimension(Integer.MAX_VALUE, itemHeight));
                    item.setPreferredSize(new Dimension(Short.MAX_VALUE, itemHeight));
                    item.setAlignmentX(Component.LEFT_ALIGNMENT);
                    rightList.add(item);
                    rightList.add(Box.createRigidArea(new Dimension(0, 6)));
                }
               rightPanel.add(rightList, BorderLayout.CENTER);

            mainPanel.add(leftPanel);
            mainPanel.add(rightPanel);

            frame.getContentPane().add(topPanel, BorderLayout.NORTH);
            frame.getContentPane().add(mainPanel, BorderLayout.CENTER);

            frame.setVisible(true);
        });
    }
}
