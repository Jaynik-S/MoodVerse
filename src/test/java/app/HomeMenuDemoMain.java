package app;

import javax.swing.*;

import interface_adapter.home_menu.HomeMenuViewModel;
import interface_adapter.home_menu.HomeMenuController;
import view.HomeMenuView;

public class HomeMenuDemoMain {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HomeMenuViewModel viewModel = new HomeMenuViewModel();
            HomeMenuController controller = new HomeMenuController();

            HomeMenuView view = new HomeMenuView(controller, viewModel);

            JFrame frame = new JFrame("MoodVerse - Home Menu Demo");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(view);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
