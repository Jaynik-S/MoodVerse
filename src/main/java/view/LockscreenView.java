package view;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import interface_adapter.lock_screen.LockScreenController;
import interface_adapter.lock_screen.LockScreenState;
import interface_adapter.lock_screen.LockScreenViewModel;

public class LockscreenView extends JPanel implements ActionListener, PropertyChangeListener {

    private static final int VERTICAL_GAP = 10;

    private final LockScreenViewModel lockScreenViewModel;
    private final LockScreenController lockScreenController;

    private final JLabel title = new JLabel("MoodVerse");
    private final JPasswordField passwordInputField = new JPasswordField(15);
    private final JButton enterButton = new JButton("Enter");

    public LockscreenView(LockScreenViewModel lockscreenViewModel, LockScreenController lockScreenController) {

        this.lockScreenViewModel = lockscreenViewModel;
        this.lockScreenController = lockScreenController;

        this.lockScreenViewModel.addPropertyChangeListener(this);

        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordInputField.setAlignmentX(Component.CENTER_ALIGNMENT);
        passwordInputField.setMaximumSize(passwordInputField.getPreferredSize());
        enterButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(Box.createVerticalGlue());
        this.add(title);
        this.add(Box.createVerticalStrut(VERTICAL_GAP));
        this.add(passwordInputField);
        this.add(Box.createVerticalStrut(VERTICAL_GAP));
        this.add(enterButton);
        this.add(Box.createVerticalGlue());

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