package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import entity.MovieRecommendation;
import entity.SongRecommendation;
import interface_adapter.recommendation_menu.RecommendationMenuController;
import interface_adapter.recommendation_menu.RecommendationMenuState;
import interface_adapter.recommendation_menu.RecommendationMenuViewModel;

/**
 * The View for when the user is viewing a note in the program.
 */
public class RecommendationView extends JPanel implements ActionListener, PropertyChangeListener {
    // minimal height for recommendation view
    private static final int HEIGHT_RECOMMENDATION_MIN = 600;

    // minimal width for recommendation view
    private static final int WIDTH_RECOMMENDATION_MIN = 800;

    private static final int HEIGHT_SCORE = 50;
    private static final int WIDTH_SCORE = 80;

    private static final int HEIGHT_SONG = 167;
    private static final int WIDTH_SONG = 167;
    private static final int BORDER_SONG = 4;
    private static final int WIDTH_LEFT = 600;
    private static final int WIDTH_RIGHT = 600;
    private static final int VERTICAL_SPACING = 6;
    private static final int HEIGHT_MOVIE = 210;
    private static final int WIDTH_MOVIE = 140;

    private RecommendationMenuController recommendationController;

    private final RecommendationMenuViewModel recommendationViewModel;

    private final RecommendationMenuState recommendationState;

    private final JButton backButton = new JButton("Back");
    private JPanel leftList;
    private JPanel rightList;
    public RecommendationView(RecommendationMenuViewModel recommendationViewModel, RecommendationMenuController recommendationController, RecommendationMenuState recommendationState) throws MalformedURLException {

        this.setSize(1000, 800);

        // Prevent the window from being resized smaller than a usable minimum
        this.setMinimumSize(new Dimension(WIDTH_RECOMMENDATION_MIN, HEIGHT_RECOMMENDATION_MIN));

        this.recommendationViewModel = recommendationViewModel;

        this.recommendationController = recommendationController;

        this.recommendationState = recommendationState;

        this.recommendationViewModel.addPropertyChangeListener(this);

        // Do not access recommendation lists here — they are empty until the use-case runs.
        // Create empty containers and populate them when `propertyChange` fires.

        // Top panel with a left-aligned Back button
        final JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(backButton);


        backButton.addActionListener(
                evt -> {
                if (evt.getSource().equals(backButton) && recommendationController != null) {
                    recommendationController.executeBack();
                }
            }
        );

        // Main area: two panels side-by-side
        final JPanel mainPanel = new JPanel(new GridLayout(1, 2, 8, 0));

        // Left panel: Songs
        final JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Songs"));
        leftList = new JPanel();
        leftList.setLayout(new BoxLayout(leftList, BoxLayout.Y_AXIS));
        leftPanel.add(new JScrollPane(leftList), BorderLayout.CENTER);

        // Right panel: Movies
        final JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Movies"));
        rightList = new JPanel();
        rightList.setLayout(new BoxLayout(rightList, BoxLayout.Y_AXIS));
        rightPanel.add(new JScrollPane(rightList), BorderLayout.CENTER);

        // initially empty — populated by `setFields` when recommendations arrive


        // add left and right panel to main area
        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);

        // Attach panels to this view
        this.setLayout(new BorderLayout());
        this.add(topPanel, BorderLayout.NORTH);
        this.add(mainPanel, BorderLayout.CENTER);
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
        // Fix the height of the top row so it matches the other fixed rows
        topRow.setPreferredSize(new Dimension(0, HEIGHT_SCORE));
        topRow.setMinimumSize(new Dimension(0, HEIGHT_SCORE));
        topRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, HEIGHT_SCORE));

        JTextField titleField = new JTextField(movieName);
        titleField.setEditable(false);
        // make the title visually prominent (kept same font size)
        titleField.setFont(titleField.getFont().deriveFont(16f));
        // ensure title field uses the same row height
        titleField.setPreferredSize(new Dimension(0, HEIGHT_SCORE));

        JPanel smallBoxes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        JTextField yearField = new JTextField(year);
        // match the score row height so components align vertically
        yearField.setPreferredSize(new Dimension(80, HEIGHT_SCORE));
        yearField.setHorizontalAlignment(JTextField.CENTER);
        yearField.setEditable(false);
        JTextField ratingField = new JTextField(score);
        ratingField.setPreferredSize(new Dimension(80, HEIGHT_SCORE));
        ratingField.setHorizontalAlignment(JTextField.CENTER);
        ratingField.setEditable(false);
        smallBoxes.add(yearField);
        smallBoxes.add(ratingField);
        topRow.add(titleField, BorderLayout.CENTER);
        topRow.add(smallBoxes, BorderLayout.EAST);
        JTextArea descField = new JTextArea(overview);
        descField.setLineWrap(true);
        descField.setWrapStyleWord(true);
        descField.setEditable(false);
        descField.setPreferredSize(new Dimension(200, 120));

        details.add(topRow, BorderLayout.NORTH);
        details.add(descScroll, BorderLayout.CENTER);
        item.add(coverWrap, BorderLayout.WEST);
        item.add(details, BorderLayout.CENTER);

        int itemHeight = HEIGHT_MOVIE;

        // Allow the movie item to expand horizontally with the view while
        // keeping a fixed height. Preferred width is left flexible (0)
        // and maximum width is very large so BoxLayout will stretch it.
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, itemHeight));
        item.setPreferredSize(new Dimension(0, itemHeight));
        item.setMinimumSize(new Dimension(0, itemHeight));
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
        // Fix the height of this row so controls inside don't change layout
        // when the view is resized. Width remains flexible.
        nameRow.setPreferredSize(new Dimension(0, HEIGHT_SCORE));
        nameRow.setMinimumSize(new Dimension(0, HEIGHT_SCORE));
        nameRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, HEIGHT_SCORE));
        final JTextField nameField = new JTextField(songName);
        // make the title visually prominent
        nameField.setFont(nameField.getFont().deriveFont(16f));
        // ensure the nameField exactly matches the fixed row height
        nameField.setPreferredSize(new Dimension(0, HEIGHT_SCORE));
        nameField.setMinimumSize(new Dimension(0, HEIGHT_SCORE));
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, HEIGHT_SCORE));
        final JTextField scoreField = new JTextField(score);
        scoreField.setPreferredSize(new Dimension(WIDTH_SCORE, HEIGHT_SCORE));
        scoreField.setMinimumSize(new Dimension(WIDTH_SCORE, HEIGHT_SCORE));
        scoreField.setMaximumSize(new Dimension(Integer.MAX_VALUE, HEIGHT_SCORE));
        scoreField.setHorizontalAlignment(JTextField.CENTER);
        scoreField.setEditable(false);
        nameField.setEditable(false);
        nameRow.add(nameField, BorderLayout.CENTER);
        nameRow.add(scoreField, BorderLayout.EAST);

        final JPanel artistRow = new JPanel(new BorderLayout(6, 6));
        // Fix the height of the artist row to match the score row height
        artistRow.setPreferredSize(new Dimension(0, HEIGHT_SCORE));
        artistRow.setMinimumSize(new Dimension(0, HEIGHT_SCORE));
        artistRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, HEIGHT_SCORE));
        final JTextField artistField = new JTextField(artist);
        final JTextField yearField = new JTextField(year);
        yearField.setPreferredSize(new Dimension(WIDTH_SCORE, HEIGHT_SCORE));
        yearField.setHorizontalAlignment(JTextField.CENTER);
        artistField.setEditable(false);
        yearField.setEditable(false);
        artistRow.add(artistField, BorderLayout.CENTER);
        artistRow.add(yearField, BorderLayout.EAST);

        final JTextField urlField = new JTextField(url);
        // make URL read-only but clickable
        urlField.setEditable(false);
        urlField.setForeground(Color.BLUE);
        urlField.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        urlField.setToolTipText(url);
        urlField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    if (url == null || url.isEmpty()) return;
                    if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                        Desktop.getDesktop().browse(new URI(url));
                    } else {
                        JOptionPane.showMessageDialog(RecommendationView.this, "Opening links is not supported on this platform.", "Not Supported", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(RecommendationView.this, "Unable to open link: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        details.add(nameRow, BorderLayout.NORTH);
        details.add(artistRow, BorderLayout.CENTER);
        details.add(urlField, BorderLayout.SOUTH);

        item.add(coverWrap, BorderLayout.WEST);
        item.add(details, BorderLayout.CENTER);

        // Make song items horizontally flexible so they follow the list width
        // instead of using a fixed WIDTH_LEFT. Height remains fixed.
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, HEIGHT_SONG));
        item.setPreferredSize(new Dimension(0, HEIGHT_SONG));
        item.setMinimumSize(new Dimension(0, HEIGHT_SONG));
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
        SwingUtilities.invokeLater(() -> {
            leftList.removeAll();
            rightList.removeAll();

            // Populate songs (safely, skip any null entries)
            try {
                SongRecommendation s1 = recommendationMenuState.getSongRecommendationOne();
                if (s1 != null) {
                    leftList.add(createSongItem("Song #1", s1.getSongName(), s1.getPopularityScore(), s1.getArtistName(), s1.getReleaseYear(), s1.getExternalUrl(), s1.getImageUrl()));
                    leftList.add(Box.createRigidArea(new Dimension(0, VERTICAL_SPACING)));
                }
                SongRecommendation s2 = recommendationMenuState.getSongRecommendationTwo();
                if (s2 != null) {
                    leftList.add(createSongItem("Song #2", s2.getSongName(), s2.getPopularityScore(), s2.getArtistName(), s2.getReleaseYear(), s2.getExternalUrl(), s2.getImageUrl()));
                    leftList.add(Box.createRigidArea(new Dimension(0, VERTICAL_SPACING)));
                }
                SongRecommendation s3 = recommendationMenuState.getSongRecommendationThree();
                if (s3 != null) {
                    leftList.add(createSongItem("Song #3", s3.getSongName(), s3.getPopularityScore(), s3.getArtistName(), s3.getReleaseYear(), s3.getExternalUrl(), s3.getImageUrl()));
                    leftList.add(Box.createRigidArea(new Dimension(0, VERTICAL_SPACING)));
                }
                SongRecommendation s4 = recommendationMenuState.getSongRecommendationFour();
                if (s4 != null) {
                    leftList.add(createSongItem("Song #4", s4.getSongName(), s4.getPopularityScore(), s4.getArtistName(), s4.getReleaseYear(), s4.getExternalUrl(), s4.getImageUrl()));
                    leftList.add(Box.createRigidArea(new Dimension(0, VERTICAL_SPACING)));
                }
                SongRecommendation s5 = recommendationMenuState.getSongRecommendationFive();
                if (s5 != null) {
                    leftList.add(createSongItem("Song #5", s5.getSongName(), s5.getPopularityScore(), s5.getArtistName(), s5.getReleaseYear(), s5.getExternalUrl(), s5.getImageUrl()));
                }
            } catch (Exception e) {
                // If creating song items fails, show a simple fallback label
                leftList.removeAll();
                leftList.add(new JLabel("Unable to load songs"));
            }

            // Populate movies
            try {
                MovieRecommendation m1 = recommendationMenuState.getMovieRecommendationOne();
                if (m1 != null) {
                    rightList.add(createMovieItem("Movie #1", m1.getMovieTitle(), m1.getMovieRating(), m1.getReleaseYear(), m1.getOverview(), m1.getImageUrl()));
                    rightList.add(Box.createRigidArea(new Dimension(0, VERTICAL_SPACING)));
                }
                MovieRecommendation m2 = recommendationMenuState.getMovieRecommendationTwo();
                if (m2 != null) {
                    rightList.add(createMovieItem("Movie #2", m2.getMovieTitle(), m2.getMovieRating(), m2.getReleaseYear(), m2.getOverview(), m2.getImageUrl()));
                    rightList.add(Box.createRigidArea(new Dimension(0, VERTICAL_SPACING)));
                }
                MovieRecommendation m3 = recommendationMenuState.getMovieRecommendationThree();
                if (m3 != null) {
                    rightList.add(createMovieItem("Movie #3", m3.getMovieTitle(), m3.getMovieRating(), m3.getReleaseYear(), m3.getOverview(), m3.getImageUrl()));
                    rightList.add(Box.createRigidArea(new Dimension(0, VERTICAL_SPACING)));
                }
                MovieRecommendation m4 = recommendationMenuState.getMovieRecommendationFour();
                if (m4 != null) {
                    rightList.add(createMovieItem("Movie #4", m4.getMovieTitle(), m4.getMovieRating(), m4.getReleaseYear(), m4.getOverview(), m4.getImageUrl()));
                }
            } catch (Exception e) {
                rightList.removeAll();
                rightList.add(new JLabel("Unable to load movies"));
            }

            leftList.revalidate();
            leftList.repaint();
            rightList.revalidate();
            rightList.repaint();
        });
    }

    public void setRecommendationController(RecommendationMenuController recommendationController) {
        this.recommendationController = recommendationController;
    }

}
