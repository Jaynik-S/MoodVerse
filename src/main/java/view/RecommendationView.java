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
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
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
 * The View for when the user is viewing recommendations in the program.
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
    private static final int VERTICAL_SPACING = 6;
    private static final int HEIGHT_MOVIE = 210;
    private static final int WIDTH_MOVIE = 140;

    private static final int FIELD_PAD_Y = 6;
    private static final int FIELD_PAD_X = 10;

    private static final float FONT_TITLE = 15f;
    private static final float FONT_SUBTITLE = 13f;
    private static final float FONT_META = 12f;
    private static final float FONT_URL = 11f;
    private static final float FONT_DESC = 12f;
    private static final float FONT_MIN = 10f;

    private RecommendationMenuController recommendationController;

    private final RecommendationMenuViewModel recommendationViewModel;

    private final RecommendationMenuState recommendationState;

    private final JButton backButton = new JButton("Back");
    private JPanel leftList;
    private JPanel rightList;

    public RecommendationView(RecommendationMenuViewModel recommendationViewModel,
                              RecommendationMenuController recommendationController,
                              RecommendationMenuState recommendationState) {

        this.setSize(1000, 800);
        this.setMinimumSize(new Dimension(WIDTH_RECOMMENDATION_MIN, HEIGHT_RECOMMENDATION_MIN));

        this.recommendationViewModel = recommendationViewModel;
        this.recommendationController = recommendationController;
        this.recommendationState = recommendationState;

        this.recommendationViewModel.addPropertyChangeListener(this);

        // Root style consistent with HomeMenuView
        setLayout(new BorderLayout(16, 16));
        setBackground(new Color(245, 248, 255));
        setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        // Top bar with Back button and title
        JPanel topCard = new JPanel(new BorderLayout());
        topCard.setBackground(new Color(219, 234, 254));
        topCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(191, 219, 254)),
                BorderFactory.createEmptyBorder(8, 12, 8, 12))
        );

        styleSecondaryButton(backButton);

        JPanel leftTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftTop.setOpaque(false);
        leftTop.add(backButton);

        topCard.add(leftTop, BorderLayout.WEST);

        backButton.addActionListener(
                evt -> {
                    if (evt.getSource().equals(backButton) && recommendationController != null) {
                        recommendationController.executeBack();
                    }
                }
        );

        // Main two-column layout wrapped in a white card
        JPanel columnsCard = new JPanel(new GridLayout(1, 2, 16, 0));
        columnsCard.setBackground(new Color(245, 248, 255));

        // Songs panel
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(12, 12, 12, 12))
        );

        JLabel songsLabel = new JLabel("Songs");
        songsLabel.setFont(new java.awt.Font(songsLabel.getFont().getFontName(), java.awt.Font.BOLD, 14));
        songsLabel.setForeground(new Color(55, 65, 81));
        leftPanel.add(songsLabel, BorderLayout.NORTH);

        leftList = new JPanel();
        leftList.setLayout(new BoxLayout(leftList, BoxLayout.Y_AXIS));
        leftList.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));
        leftList.setOpaque(false);

        JScrollPane leftScroll = new JScrollPane(leftList);
        leftScroll.setBorder(BorderFactory.createEmptyBorder());
        leftScroll.getViewport().setBackground(Color.WHITE);
        leftPanel.add(leftScroll, BorderLayout.CENTER);

        // Movies panel
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(12, 12, 12, 12))
        );

        JLabel moviesLabel = new JLabel("Movies");
        moviesLabel.setFont(new java.awt.Font(moviesLabel.getFont().getFontName(), java.awt.Font.BOLD, 14));
        moviesLabel.setForeground(new Color(55, 65, 81));
        rightPanel.add(moviesLabel, BorderLayout.NORTH);

        rightList = new JPanel();
        rightList.setLayout(new BoxLayout(rightList, BoxLayout.Y_AXIS));
        rightList.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));
        rightList.setOpaque(false);

        JScrollPane rightScroll = new JScrollPane(rightList);
        rightScroll.setBorder(BorderFactory.createEmptyBorder());
        rightScroll.getViewport().setBackground(Color.WHITE);
        rightPanel.add(rightScroll, BorderLayout.CENTER);

        columnsCard.add(leftPanel);
        columnsCard.add(rightPanel);

        add(topCard, BorderLayout.NORTH);
        add(columnsCard, BorderLayout.CENTER);
    }

    /**
     * Load an image from a URL and scale it to the target size. Returns null on failure.
     */
    private ImageIcon loadAndScale(String imageUrl, int targetW, int targetH) {
        if (imageUrl == null || imageUrl.isEmpty()) return null;
        try {
            URL url = new URL(imageUrl);
            BufferedImage img = ImageIO.read(url);
            if (img == null) return null;
            Image scaled = img.getScaledInstance(targetW, targetH, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        }
        catch (Exception error) {
            return null;
        }
    }

    // invisible border used only for padding (no inner outlines)
    private static void styleReadOnlyField(JTextField field, float fontSize, boolean bold) {
        field.setEditable(false);
        field.setBorder(BorderFactory.createEmptyBorder(FIELD_PAD_Y, FIELD_PAD_X, FIELD_PAD_Y, FIELD_PAD_X));
        field.setOpaque(false);
        field.setFocusable(false);
        int style = bold ? java.awt.Font.BOLD : java.awt.Font.PLAIN;
        field.setFont(field.getFont().deriveFont(style, fontSize));
        field.setForeground(new Color(31, 41, 55));
    }

    private static void styleReadOnlyArea(JTextArea area, float fontSize) {
        area.setEditable(false);
        area.setBorder(BorderFactory.createEmptyBorder(FIELD_PAD_Y, FIELD_PAD_X, FIELD_PAD_Y, FIELD_PAD_X));
        area.setOpaque(false);
        area.setFocusable(false);
        area.setFont(area.getFont().deriveFont(fontSize));
        area.setForeground(new Color(75, 85, 99));
    }

    private static void styleSecondaryButton(JButton button) {
        button.setFont(new java.awt.Font(button.getFont().getFontName(), java.awt.Font.PLAIN, 13));
        button.setFocusPainted(false);
        button.setForeground(new Color(55, 65, 81));
        button.setBackground(new Color(239, 246, 255));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(191, 219, 254)),
                BorderFactory.createEmptyBorder(6, 12, 6, 12))
        );
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    // shrinks a text field's font size until its text fits the available width
    private static void installAutoFontShrink(final JTextField field, final float minSize) {
        field.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                shrinkFontToFit(field, minSize);
            }
        });
        SwingUtilities.invokeLater(() -> shrinkFontToFit(field, minSize));
    }

    private static void shrinkFontToFit(JTextField field, float minSize) {
        String txt = field.getText();
        if (txt == null || txt.isEmpty()) return;

        int available = Math.max(0, field.getWidth() - field.getInsets().left - field.getInsets().right - 4);
        if (available <= 0) return;

        float size = field.getFont().getSize2D();
        java.awt.Font base = field.getFont();

        while (size > minSize) {
            java.awt.Font f = base.deriveFont(size);
            int w = field.getFontMetrics(f).stringWidth(txt);
            if (w <= available) {
                field.setFont(f);
                return;
            }
            size -= 0.5f;
        }
        field.setFont(base.deriveFont(minSize));
    }

    private JPanel createMovieItem(String title, String movieName, String score, String year,
                                   String overview, String imageUrl) {

        JPanel item = new JPanel(new BorderLayout(8, 8));
        item.setOpaque(false);
        item.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235)),
                BorderFactory.createEmptyBorder(6, 6, 6, 6))
        );

        JLabel cover = new JLabel();
        cover.setHorizontalAlignment(JLabel.CENTER);
        ImageIcon movieIcon = loadAndScale(imageUrl, WIDTH_MOVIE - BORDER_SONG * 2, HEIGHT_MOVIE - BORDER_SONG * 2);
        if (movieIcon != null) {
            cover.setIcon(movieIcon);
        }
        else {
            cover.setText("<html><div style='text-align:center;'>No\nImage</div></html>");
        }
        cover.setPreferredSize(new Dimension(WIDTH_MOVIE, HEIGHT_MOVIE));
        cover.setMinimumSize(new Dimension(WIDTH_MOVIE, HEIGHT_MOVIE));
        cover.setMaximumSize(new Dimension(WIDTH_MOVIE, HEIGHT_MOVIE));

        JPanel coverWrap = new JPanel(new BorderLayout());
        coverWrap.setOpaque(false);
        coverWrap.add(cover, BorderLayout.CENTER);
        coverWrap.setMinimumSize(new Dimension(WIDTH_MOVIE, HEIGHT_MOVIE));
        coverWrap.setPreferredSize(new Dimension(WIDTH_MOVIE, HEIGHT_MOVIE));
        coverWrap.setMaximumSize(new Dimension(WIDTH_MOVIE, HEIGHT_MOVIE));

        JPanel details = new JPanel(new BorderLayout(6, 6));
        details.setOpaque(false);

        JPanel topRow = new JPanel(new BorderLayout(6, 6));
        topRow.setOpaque(false);
        topRow.setPreferredSize(new Dimension(0, HEIGHT_SCORE));
        topRow.setMinimumSize(new Dimension(0, HEIGHT_SCORE));
        topRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, HEIGHT_SCORE));

        JTextField titleField = new JTextField(movieName);
        styleReadOnlyField(titleField, FONT_TITLE, true);
        titleField.setPreferredSize(new Dimension(0, HEIGHT_SCORE));
        installAutoFontShrink(titleField, FONT_MIN);

        JPanel smallBoxes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        smallBoxes.setOpaque(false);

        JTextField yearField = new JTextField(year);
        yearField.setPreferredSize(new Dimension(80, HEIGHT_SCORE));
        yearField.setHorizontalAlignment(JTextField.CENTER);
        styleReadOnlyField(yearField, FONT_META, false);

        JTextField ratingField = new JTextField(score);
        ratingField.setPreferredSize(new Dimension(80, HEIGHT_SCORE));
        ratingField.setHorizontalAlignment(JTextField.CENTER);
        styleReadOnlyField(ratingField, FONT_META, false);

        smallBoxes.add(yearField);
        smallBoxes.add(ratingField);

        topRow.add(titleField, BorderLayout.CENTER);
        topRow.add(smallBoxes, BorderLayout.EAST);

        JTextArea descField = new JTextArea(overview);
        descField.setLineWrap(true);
        descField.setWrapStyleWord(true);
        styleReadOnlyArea(descField, FONT_DESC);
        descField.setPreferredSize(new Dimension(200, 120));

        details.add(topRow, BorderLayout.NORTH);
        details.add(descField, BorderLayout.CENTER);

        item.add(coverWrap, BorderLayout.WEST);
        item.add(details, BorderLayout.CENTER);

        int itemHeight = HEIGHT_MOVIE;

        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, itemHeight));
        item.setPreferredSize(new Dimension(0, itemHeight));
        item.setMinimumSize(new Dimension(0, itemHeight));
        item.setAlignmentX(Component.LEFT_ALIGNMENT);

        return item;
    }

    /**
     * Build a song item panel used in the recommendations list.
     */
    private JPanel createSongItem(String title, String songName, String score, String artist,
                                  String year, String url, String imageUrl) throws MalformedURLException {
        JPanel item = new JPanel(new BorderLayout(8, 8));
        item.setOpaque(false);
        item.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235)),
                BorderFactory.createEmptyBorder(8, 8, 8, 8))
        );

        JLabel cover = new JLabel();
        cover.setHorizontalAlignment(JLabel.CENTER);

        ImageIcon songIcon = loadAndScale(imageUrl, WIDTH_SONG - BORDER_SONG * 2, HEIGHT_SONG - BORDER_SONG * 2);
        if (songIcon != null) {
            cover.setIcon(songIcon);
        }
        else {
            cover.setText("<html><div style='text-align:center;'>No\nImage</div></html>");
        }

        cover.setPreferredSize(new Dimension(WIDTH_SONG, HEIGHT_SONG));
        cover.setMinimumSize(new Dimension(WIDTH_SONG, HEIGHT_SONG));
        cover.setMaximumSize(new Dimension(WIDTH_SONG, HEIGHT_SONG));

        JPanel coverWrap = new JPanel(new BorderLayout());
        coverWrap.setOpaque(false);
        coverWrap.add(cover, BorderLayout.CENTER);
        coverWrap.setPreferredSize(new Dimension(WIDTH_SONG, HEIGHT_SONG));
        coverWrap.setMinimumSize(new Dimension(WIDTH_SONG, HEIGHT_SONG));
        coverWrap.setMaximumSize(new Dimension(WIDTH_SONG, HEIGHT_SONG));

        JPanel details = new JPanel(new BorderLayout(6, 6));
        details.setOpaque(false);

        JPanel nameRow = new JPanel(new BorderLayout(6, 6));
        nameRow.setOpaque(false);
        nameRow.setPreferredSize(new Dimension(0, HEIGHT_SCORE));
        nameRow.setMinimumSize(new Dimension(0, HEIGHT_SCORE));
        nameRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, HEIGHT_SCORE));

        JTextField nameField = new JTextField(songName);
        styleReadOnlyField(nameField, FONT_TITLE, true);
        installAutoFontShrink(nameField, FONT_MIN);
        nameField.setPreferredSize(new Dimension(0, HEIGHT_SCORE));
        nameField.setMinimumSize(new Dimension(0, HEIGHT_SCORE));
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, HEIGHT_SCORE));

        JTextField scoreField = new JTextField(score);
        scoreField.setPreferredSize(new Dimension(WIDTH_SCORE, HEIGHT_SCORE));
        scoreField.setMinimumSize(new Dimension(WIDTH_SCORE, HEIGHT_SCORE));
        scoreField.setMaximumSize(new Dimension(Integer.MAX_VALUE, HEIGHT_SCORE));
        scoreField.setHorizontalAlignment(JTextField.CENTER);
        styleReadOnlyField(scoreField, FONT_META, false);

        nameRow.add(nameField, BorderLayout.CENTER);
        nameRow.add(scoreField, BorderLayout.EAST);

        JPanel artistRow = new JPanel(new BorderLayout(6, 6));
        artistRow.setOpaque(false);
        artistRow.setPreferredSize(new Dimension(0, HEIGHT_SCORE));
        artistRow.setMinimumSize(new Dimension(0, HEIGHT_SCORE));
        artistRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, HEIGHT_SCORE));

        JTextField artistField = new JTextField(artist);
        styleReadOnlyField(artistField, FONT_SUBTITLE, false);

        JTextField yearField = new JTextField(year);
        yearField.setPreferredSize(new Dimension(WIDTH_SCORE, HEIGHT_SCORE));
        yearField.setHorizontalAlignment(JTextField.CENTER);
        styleReadOnlyField(yearField, FONT_META, false);

        artistRow.add(artistField, BorderLayout.CENTER);
        artistRow.add(yearField, BorderLayout.EAST);

        JTextField urlField = new JTextField(url);
        styleReadOnlyField(urlField, FONT_URL, false);
        installAutoFontShrink(urlField, FONT_MIN);
        urlField.setForeground(new Color(37, 99, 235));
        urlField.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        urlField.setToolTipText(url);
        urlField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    if (url == null || url.isEmpty()) return;
                    if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                        Desktop.getDesktop().browse(new URI(url));
                    }
                    else {
                        JOptionPane.showMessageDialog(RecommendationView.this,
                                "Opening links is not supported on this platform.",
                                "Not Supported",
                                JOptionPane.WARNING_MESSAGE);
                    }
                }
                catch (Exception error) {
                    JOptionPane.showMessageDialog(RecommendationView.this,
                            "Unable to open link: " + error.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        details.add(nameRow, BorderLayout.NORTH);
        details.add(artistRow, BorderLayout.CENTER);
        details.add(urlField, BorderLayout.SOUTH);

        item.add(coverWrap, BorderLayout.WEST);
        item.add(details, BorderLayout.CENTER);

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
                    leftList.add(createSongItem("Song #1", s1.getSongName(), s1.getPopularityScore(),
                            s1.getArtistName(), s1.getReleaseYear(), s1.getExternalUrl(), s1.getImageUrl()));
                    leftList.add(Box.createRigidArea(new Dimension(0, VERTICAL_SPACING)));
                }
                SongRecommendation s2 = recommendationMenuState.getSongRecommendationTwo();
                if (s2 != null) {
                    leftList.add(createSongItem("Song #2", s2.getSongName(), s2.getPopularityScore(),
                            s2.getArtistName(), s2.getReleaseYear(), s2.getExternalUrl(), s2.getImageUrl()));
                    leftList.add(Box.createRigidArea(new Dimension(0, VERTICAL_SPACING)));
                }
                SongRecommendation s3 = recommendationMenuState.getSongRecommendationThree();
                if (s3 != null) {
                    leftList.add(createSongItem("Song #3", s3.getSongName(), s3.getPopularityScore(),
                            s3.getArtistName(), s3.getReleaseYear(), s3.getExternalUrl(), s3.getImageUrl()));
                    leftList.add(Box.createRigidArea(new Dimension(0, VERTICAL_SPACING)));
                }
                SongRecommendation s4 = recommendationMenuState.getSongRecommendationFour();
                if (s4 != null) {
                    leftList.add(createSongItem("Song #4", s4.getSongName(), s4.getPopularityScore(),
                            s4.getArtistName(), s4.getReleaseYear(), s4.getExternalUrl(), s4.getImageUrl()));
                    leftList.add(Box.createRigidArea(new Dimension(0, VERTICAL_SPACING)));
                }
                SongRecommendation s5 = recommendationMenuState.getSongRecommendationFive();
                if (s5 != null) {
                    leftList.add(createSongItem("Song #5", s5.getSongName(), s5.getPopularityScore(),
                            s5.getArtistName(), s5.getReleaseYear(), s5.getExternalUrl(), s5.getImageUrl()));
                }
            }
            catch (Exception error) {
                leftList.removeAll();
                leftList.add(new JLabel("Unable to load songs"));
            }

            // Populate movies
            try {
                MovieRecommendation m1 = recommendationMenuState.getMovieRecommendationOne();
                if (m1 != null) {
                    rightList.add(createMovieItem("Movie #1", m1.getMovieTitle(), m1.getMovieRating(),
                            m1.getReleaseYear(), m1.getOverview(), m1.getImageUrl()));
                    rightList.add(Box.createRigidArea(new Dimension(0, VERTICAL_SPACING)));
                }
                MovieRecommendation m2 = recommendationMenuState.getMovieRecommendationTwo();
                if (m2 != null) {
                    rightList.add(createMovieItem("Movie #2", m2.getMovieTitle(), m2.getMovieRating(),
                            m2.getReleaseYear(), m2.getOverview(), m2.getImageUrl()));
                    rightList.add(Box.createRigidArea(new Dimension(0, VERTICAL_SPACING)));
                }
                MovieRecommendation m3 = recommendationMenuState.getMovieRecommendationThree();
                if (m3 != null) {
                    rightList.add(createMovieItem("Movie #3", m3.getMovieTitle(), m3.getMovieRating(),
                            m3.getReleaseYear(), m3.getOverview(), m3.getImageUrl()));
                    rightList.add(Box.createRigidArea(new Dimension(0, VERTICAL_SPACING)));
                }
                MovieRecommendation m4 = recommendationMenuState.getMovieRecommendationFour();
                if (m4 != null) {
                    rightList.add(createMovieItem("Movie #4", m4.getMovieTitle(), m4.getMovieRating(),
                            m4.getReleaseYear(), m4.getOverview(), m4.getImageUrl()));
                }
            }
            catch (Exception error) {
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
