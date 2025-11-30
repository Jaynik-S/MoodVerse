package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.*;

import interface_adapter.lock_screen.LockScreenController;
import interface_adapter.lock_screen.LockScreenState;
import interface_adapter.lock_screen.LockScreenViewModel;

public class LockscreenView extends JPanel implements ActionListener, PropertyChangeListener {

    private static final int VERTICAL_GAP = 10;

    private final LockScreenViewModel lockScreenViewModel;
    private final LockScreenController lockScreenController;

    private final JLabel title = new JLabel("MoodVerse");
    private final JLabel subtitle = new JLabel("Enter your password to continue.");
    private final JPasswordField passwordInputField = new JPasswordField(18);
    private final JButton enterButton = new JButton("Enter");

    public LockscreenView(LockScreenViewModel lockscreenViewModel, LockScreenController lockScreenController) {

        this.lockScreenViewModel = lockscreenViewModel;
        this.lockScreenController = lockScreenController;

        this.lockScreenViewModel.addPropertyChangeListener(this);

        // Root background + padding to mirror HomeMenuView
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 248, 255));
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        // Card container
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(24, 32, 24, 32))
        );

        // Typography similar to HomeMenuView title
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font(title.getFont().getFontName(), Font.BOLD, 26));
        title.setForeground(new Color(30, 64, 175));

        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setFont(new Font(subtitle.getFont().getFontName(), Font.PLAIN, 13));
        subtitle.setForeground(new Color(75, 85, 99));

        passwordInputField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordInputField.setMaximumSize(new Dimension(260, 32));
        passwordInputField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(209, 213, 219)),
                BorderFactory.createEmptyBorder(6, 8, 6, 8))
        );
        passwordInputField.setFont(new Font(passwordInputField.getFont().getFontName(), Font.PLAIN, 14));

        // Primary button styling to match New Entry button
        enterButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        enterButton.setFont(new Font(enterButton.getFont().getFontName(), Font.BOLD, 14));
        enterButton.setFocusPainted(false);
        enterButton.setForeground(Color.WHITE);
        enterButton.setUI(new javax.swing.plaf.basic.BasicButtonUI()); // fix for MacOS
        enterButton.setBackground(new Color(37, 99, 235));
        enterButton.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        enterButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Build vertical layout
        card.add(title);
        card.add(Box.createVerticalStrut(6));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(VERTICAL_GAP * 2));
        card.add(passwordInputField);
        card.add(Box.createVerticalStrut(VERTICAL_GAP * 2));
        card.add(enterButton);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        add(card, gbc);

        enterButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource().equals(enterButton)) {
            String password = new String(passwordInputField.getPassword());
            lockScreenController.execute(password);
        }
    }

    /**
     * React to a change in the ViewModel.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final LockScreenState state = (LockScreenState) evt.getNewValue();

        if (state.getError() != null) {
            JOptionPane.showMessageDialog(this, state.getError(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            // Clear error
            state.setError(null);
        }
    }

}