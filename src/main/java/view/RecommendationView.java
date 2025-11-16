package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.*;

import interface_adapter.recommendation.RecommendationController;
import interface_adapter.recommendation.RecommendationState;
import interface_adapter.recommendation.RecommendationViewModel;

/**
 * The View for when the user is viewing a note in the program.
 */
public class RecommendationView extends JPanel implements ActionListener, PropertyChangeListener {
    // minimal height for recommendation view
    private static final int HEIGHT_RECOMMENDATION_MIN = 600;

    // minimal width for recommendation view
    private static final int WIDTH_RECOMMENDATION_MIN = 800;

    private static final int HEIGHT_SCORE = 40;
    private static final int WIDTH_SCORE = 80;

    private static final int HEIGHT_SONG = 140;
    private static final int WIDTH_SONG = 120;
    private static final int BORDER_SONG = 4;
    private static final int WIDTH_LEFT = 500;
    private static final int VERTICAL_SPACING = 6;

    private final RecommendationController recommendationController;

    private final RecommendationViewModel recommendationViewModel;

    private final JButton backButton = new JButton("Back");

    public RecommendationView(RecommendationViewModel recommendationViewModel, RecommendationController recommendationController) {

        // Prevent the window from being resized smaller than a usable minimum
        this.setMinimumSize(new Dimension(WIDTH_RECOMMENDATION_MIN, HEIGHT_RECOMMENDATION_MIN));

        this.recommendationViewModel = recommendationViewModel;

        this.recommendationController = recommendationController;

        this.recommendationViewModel.addPropertyChangeListener(this);

        // Top panel with a left-aligned Back button
        final JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(backButton);

        // TODO: Event call for back button.
        backButton.addActionListener(
                evt -> {
                if (evt.getSource().equals(backButton)) {
                        // recommendationController.goBack();
                }
            }
        );

        // Main area: two panels side-by-side
        final JPanel mainPanel = new JPanel(new GridLayout(1, 2, 8, 0));

        // Left panel: Songs
        final JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Songs"));
        final JPanel leftList = new JPanel();
        leftList.setLayout(new BoxLayout(leftList, BoxLayout.Y_AXIS));

        // First song panel
        final JPanel itemOne = createSongItem("Song #1", "Song Name", "score", "Artist", "Year", "https://example.com/track...");
        leftList.add(itemOne);
        leftList.add(Box.createRigidArea(new Dimension(0, VERTICAL_SPACING)));

        // Second song panel
        final JPanel itemTwo = createSongItem("Song #2", "Song Name", "score", "Artist", "Year", "https://example.com/track...");
        leftList.add(itemTwo);
        leftList.add(Box.createRigidArea(new Dimension(0, VERTICAL_SPACING)));

        // Third song panel
        final JPanel itemThree = createSongItem("Song #3", "Song Name", "score", "Artist", "Year", "https://example.com/track...");
        leftList.add(itemThree);
        leftList.add(Box.createRigidArea(new Dimension(0, VERTICAL_SPACING)));

        // Forth song panel
        final JPanel itemFour = createSongItem("Song #4", "Song Name", "score", "Artist", "Year", "https://example.com/track...");
        leftList.add(itemFour);
        leftList.add(Box.createRigidArea(new Dimension(0, VERTICAL_SPACING)));

        // Fifth song panel
        final JPanel itemFive = createSongItem("Song #5", "Song Name", "score", "Artist", "Year", "https://example.com/track...");
        leftList.add(itemFive);

        // Right panel: Movies
        final JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Movies"));
        final JPanel rightList = new JPanel();
        rightList.setLayout(new BoxLayout(rightList, BoxLayout.Y_AXIS));

        // add left and right panel to main area
        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);
    }

    /**
     * Build a song item panel used in the recommendations list.
     */
    private JPanel createSongItem(String title, String songName, String score, String artist, String year, String url) {
        final JPanel item = new JPanel(new BorderLayout(8, 8));
        item.setBorder(BorderFactory.createTitledBorder(title));

        final JLabel cover = new JLabel("<html><div style='text-align:center;'>COVER<br>IMAGE<br>(640x640)</div></html>");
        cover.setHorizontalAlignment(JLabel.CENTER);
        final JPanel coverWrap = new JPanel(new BorderLayout());
        coverWrap.add(cover, BorderLayout.CENTER);
        coverWrap.setPreferredSize(new Dimension(WIDTH_SONG, HEIGHT_SONG));
        coverWrap.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(BORDER_SONG, BORDER_SONG, BORDER_SONG, BORDER_SONG)));

        // Details
        final JPanel details = new JPanel(new BorderLayout(6, 6));

        final JPanel nameRow = new JPanel(new BorderLayout(6, 6));
        final JTextField nameField = new JTextField(songName);
        final JTextField scoreField = new JTextField(score);
        scoreField.setPreferredSize(new Dimension(WIDTH_SCORE, HEIGHT_SCORE));
        scoreField.setHorizontalAlignment(JTextField.CENTER);
        scoreField.setEditable(false);
        nameField.setEditable(false);
        nameRow.add(nameField, BorderLayout.CENTER);
        nameRow.add(scoreField, BorderLayout.EAST);

        final JPanel artistRow = new JPanel(new BorderLayout(6, 6));
        final JTextField artistField = new JTextField(artist);
        final JTextField yearField = new JTextField(year);
        yearField.setPreferredSize(new Dimension(WIDTH_SCORE, HEIGHT_SCORE));
        yearField.setHorizontalAlignment(JTextField.CENTER);
        artistField.setEditable(false);
        yearField.setEditable(false);
        artistRow.add(artistField, BorderLayout.CENTER);
        artistRow.add(yearField, BorderLayout.EAST);

        final JTextField urlField = new JTextField(url);
        urlField.setEditable(false);

        details.add(nameRow, BorderLayout.NORTH);
        details.add(artistRow, BorderLayout.CENTER);
        details.add(urlField, BorderLayout.SOUTH);

        item.add(coverWrap, BorderLayout.WEST);
        item.add(details, BorderLayout.CENTER);

        item.setMaximumSize(new Dimension(WIDTH_LEFT, HEIGHT_SONG));
        item.setPreferredSize(new Dimension(WIDTH_LEFT, HEIGHT_SONG));
        item.setAlignmentX(Component.LEFT_ALIGNMENT);

        return item;
    }

}

