package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;

import entity.MovieRecommendation;
import entity.SongRecommendation;
import interface_adapter.home_menu.HomeMenuViewModel;
import interface_adapter.recommendation_menu.RecommendationMenuState;
import interface_adapter.recommendation_menu.RecommendationMenuViewModel;
import interface_adapter.recommendation.RecommendationState;
import interface_adapter.recommendation_menu.RecommendationMenuController;
import interface_adapter.z_old_note.NoteController;
import interface_adapter.z_old_note.NoteState;

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

    private static final int HEIGHT_SONG = 120;
    private static final int WIDTH_SONG = 120;
    private static final int BORDER_SONG = 4;
    private static final int WIDTH_LEFT = 500;
    private static final int VERTICAL_SPACING = 6;
    private static final int HEIGHT_MOVIE = 150;
    private static final int WIDTH_MOVIE = 100;

    private final RecommendationMenuController recommendationController;

    private final RecommendationMenuViewModel recommendationViewModel;

    private final RecommendationMenuState recommendationState;

    private final NewDocumentMenuViewModel newDocumentMenuViewModel;

    private final JButton backButton = new JButton("Back");

    public RecommendationView(RecommendationMenuViewModel recommendationViewModel, RecommendationMenuController recommendationController, RecommendationMenuState recommendationState, NewDocumentMenuViewModel newDocumentMenuViewModel) throws MalformedURLException {
        this.newDocumentMenuViewModel = newDocumentMenuViewModel;

        this.setSize(1000, 800);

        // Prevent the window from being resized smaller than a usable minimum
        this.setMinimumSize(new Dimension(WIDTH_RECOMMENDATION_MIN, HEIGHT_RECOMMENDATION_MIN));

        this.recommendationViewModel = recommendationViewModel;

        this.recommendationController = recommendationController;

        this.recommendationState = recommendationState;

        this.recommendationViewModel.addPropertyChangeListener(this);

        final List<SongRecommendation> songRecommendations = recommendationViewModel.getSongRecommendations();

        final SongRecommendation songOne = songRecommendations.get(0);
        final SongRecommendation songTwo = songRecommendations.get(1);
        final SongRecommendation songThree = songRecommendations.get(2);
        final SongRecommendation songFour = songRecommendations.get(3);
        final SongRecommendation songFive = songRecommendations.get(4);

        final List<MovieRecommendation> movieRecommendations =  recommendationViewModel.getMovieRecommendations();

        final MovieRecommendation movieOne = movieRecommendations.get(0);
        final MovieRecommendation movieTwo = movieRecommendations.get(1);
        final MovieRecommendation movieThree = movieRecommendations.get(2);
        final MovieRecommendation movieFour = movieRecommendations.get(3);

        // Top panel with a left-aligned Back button
        final JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(backButton);


        backButton.addActionListener(
                evt -> {
                if (evt.getSource().equals(backButton)) {
                    recommendationController.executeBack();
                    recommendationViewModel.setState(newDocumentMenuViewModel.getState());
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
        final JPanel leftOne = createSongItem("Song #1", songOne.getSongName(), songOne.getPopularityScore(), songOne.getArtistName(), songOne.getReleaseYear(), songOne.getExternalUrl(), songOne.getImageUrl());
        leftList.add(leftOne);
        leftList.add(Box.createRigidArea(new Dimension(0, VERTICAL_SPACING)));

        // Second song panel
        final JPanel leftTwo = createSongItem("Song #2", songTwo.getSongName(), songTwo.getPopularityScore(), songTwo.getArtistName(), songTwo.getReleaseYear(), songTwo.getExternalUrl(), songTwo.getImageUrl());
        leftList.add(leftTwo);
        leftList.add(Box.createRigidArea(new Dimension(0, VERTICAL_SPACING)));

        // Third song panel
        final JPanel leftThree = createSongItem("Song #3", songThree.getSongName(), songThree.getPopularityScore(), songThree.getArtistName(), songThree.getReleaseYear(), songThree.getExternalUrl(), songThree.getImageUrl());
        leftList.add(leftThree);
        leftList.add(Box.createRigidArea(new Dimension(0, VERTICAL_SPACING)));

        // Forth song panel
        final JPanel leftFour = createSongItem("Song #4", songFour.getSongName(), songFour.getPopularityScore(), songFour.getArtistName(), songFour.getReleaseYear(), songFour.getExternalUrl(), songFour.getImageUrl());
        leftList.add(leftFour);
        leftList.add(Box.createRigidArea(new Dimension(0, VERTICAL_SPACING)));

        // Fifth song panel
        final JPanel leftFive = createSongItem("Song #5", songFive.getSongName(), songFive.getPopularityScore(), songFive.getArtistName(), songFive.getReleaseYear(), songFive.getExternalUrl(), songFive.getImageUrl());
        leftList.add(leftFive);

        // Right panel: Movies
        final JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Movies"));
        final JPanel rightList = new JPanel();
        rightList.setLayout(new BoxLayout(rightList, BoxLayout.Y_AXIS));

        // First movie panel
        final JPanel rightOne = createMovieItem("Movie #1", movieOne.getMovieTitle(), movieOne.getMovieRating(), movieOne.getReleaseYear(), movieOne.getOverview(), movieOne.getImageUrl());
        rightList.add(rightOne);
        rightList.add(Box.createRigidArea(new Dimension(0, VERTICAL_SPACING)));

        // Second movie panel
        final JPanel rightTwo = createMovieItem("Movie #2", movieTwo.getMovieTitle(), movieTwo.getMovieRating(), movieTwo.getReleaseYear(), movieTwo.getOverview(), movieTwo.getImageUrl());
        rightList.add(rightTwo);
        rightList.add(Box.createRigidArea(new Dimension(0, VERTICAL_SPACING)));

        // Third movie panel
        final JPanel rightThree = createMovieItem("Movie #3", movieThree.getMovieTitle(), movieThree.getMovieRating(), movieThree.getReleaseYear(), movieThree.getOverview(), movieThree.getImageUrl());
        rightList.add(rightThree);
        rightList.add(Box.createRigidArea(new Dimension(0, VERTICAL_SPACING)));

        // Fourth movie panel
        final JPanel rightFour = createMovieItem("Movie #4", movieFour.getMovieTitle(), movieFour.getMovieRating(), movieFour.getReleaseYear(), movieFour.getOverview(), movieFour.getImageUrl());
        rightList.add(rightFour);


        // add left and right panel to main area
        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);
    }

    /**
     * Load an image from a URL and scale it to the target size. Returns null on failure.
     */
    private ImageIcon loadAndScale(String imageUrl, int targetW, int targetH) {
        if (imageUrl == null || imageUrl.isEmpty()) return null;
        try {
            final URL url = new URL(imageUrl);
            final BufferedImage img = ImageIO.read(url);
            if (img == null) return null;
            final Image scaled = img.getScaledInstance(targetW, targetH, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (Exception e) {
            return null;
        }
    }

    private JPanel createMovieItem(String title, String movieName, String score, String year, String overview, String imageUrl) {

        final JPanel item = new JPanel(new BorderLayout(8, 8));
        item.setBorder(BorderFactory.createTitledBorder(title));

        JLabel cover = new JLabel();
        cover.setHorizontalAlignment(JLabel.CENTER);
        // try to load image from URL and scale to cover area (WIDTH_MOVIE x HEIGHT_MOVIE minus border)
        ImageIcon movieIcon = loadAndScale(imageUrl, WIDTH_MOVIE - BORDER_SONG * 2, HEIGHT_MOVIE - BORDER_SONG * 2);
        if (movieIcon != null) {
            cover.setIcon(movieIcon);
        } else {
            cover.setText("<html><div style='text-align:center;'>COVER<br>IMAGE<br>(847x1271)</div></html>");
        }
        cover.setPreferredSize(new Dimension(WIDTH_MOVIE, HEIGHT_MOVIE));
        cover.setMinimumSize(new Dimension(WIDTH_MOVIE, HEIGHT_MOVIE));
        cover.setMaximumSize(new Dimension(WIDTH_MOVIE, HEIGHT_MOVIE));
        JPanel coverWrap = new JPanel(new BorderLayout());
        coverWrap.add(cover, BorderLayout.CENTER);
        coverWrap.setMinimumSize(new Dimension(WIDTH_MOVIE, HEIGHT_MOVIE));
        coverWrap.setPreferredSize(new Dimension(WIDTH_MOVIE, HEIGHT_MOVIE));
        coverWrap.setMaximumSize(new Dimension(WIDTH_MOVIE, HEIGHT_MOVIE));
        coverWrap.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(4, 4, 4, 4)));

        final JPanel details = new JPanel(new BorderLayout(6, 6));
        final JPanel topRow = new JPanel(new BorderLayout(6, 6));
        JTextField titleField = new JTextField(movieName);
        titleField.setEditable(false);
        JPanel smallBoxes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        JTextField yearField = new JTextField(year);
        yearField.setPreferredSize(new Dimension(80, 40));
        yearField.setHorizontalAlignment(JTextField.CENTER);
        yearField.setEditable(false);
        JTextField ratingField = new JTextField(score);
        ratingField.setPreferredSize(new Dimension(80, 40));
        ratingField.setHorizontalAlignment(JTextField.CENTER);
        ratingField.setEditable(false);
        smallBoxes.add(yearField);
        smallBoxes.add(ratingField);
        topRow.add(titleField, BorderLayout.CENTER);
        topRow.add(smallBoxes, BorderLayout.EAST);
        JTextField descField = new JTextField(overview);
        descField.setEditable(false);
        descField.setPreferredSize(new Dimension(200, 120));

        details.add(topRow, BorderLayout.NORTH);
        details.add(descField, BorderLayout.CENTER);
        item.add(coverWrap, BorderLayout.WEST);
        item.add(details, BorderLayout.CENTER);

        int itemHeight = HEIGHT_MOVIE;

        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, itemHeight));
        item.setPreferredSize(new Dimension(Short.MAX_VALUE, itemHeight));
        item.setAlignmentX(Component.LEFT_ALIGNMENT);


        return item;
    }


        /**
         * Build a song item panel used in the recommendations list.
         */
    private JPanel createSongItem(String title, String songName, String score, String artist, String year, String url, String imageUrl) throws MalformedURLException {
        final JPanel item = new JPanel(new BorderLayout(8, 8));
        item.setBorder(BorderFactory.createTitledBorder(title));


        final JLabel cover = new JLabel();
        cover.setHorizontalAlignment(JLabel.CENTER);
        // try to load song image and scale to the cover area
        ImageIcon songIcon = loadAndScale(imageUrl, WIDTH_SONG - BORDER_SONG * 2, HEIGHT_SONG - BORDER_SONG * 2);
        if (songIcon != null) {
            cover.setIcon(songIcon);
        } else {
            cover.setText("<html><div style='text-align:center;'>COVER<br>IMAGE<br>(640x640)</div></html>");
        }
        // enforce fixed size for song covers
        cover.setPreferredSize(new Dimension(WIDTH_SONG, HEIGHT_SONG));
        cover.setMinimumSize(new Dimension(WIDTH_SONG, HEIGHT_SONG));
        cover.setMaximumSize(new Dimension(WIDTH_SONG, HEIGHT_SONG));
        final JPanel coverWrap = new JPanel(new BorderLayout());
        coverWrap.add(cover, BorderLayout.CENTER);
        coverWrap.setPreferredSize(new Dimension(WIDTH_SONG, HEIGHT_SONG));
        coverWrap.setMinimumSize(new Dimension(WIDTH_SONG, HEIGHT_SONG));
        coverWrap.setMaximumSize(new Dimension(WIDTH_SONG, HEIGHT_SONG));
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

    @Override
    public void actionPerformed(ActionEvent evt) {
        System.out.println("Click " + evt.getActionCommand());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final RecommendationMenuState state = (RecommendationMenuState) evt.getNewValue();
        setFields(state);
        if (state.getError() != null) {
            JOptionPane.showMessageDialog(this, state.getError(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void setFields(RecommendationMenuState recommendationMenuState) {
        
    }

}

