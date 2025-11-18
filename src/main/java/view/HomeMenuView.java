package view;
import interface_adapter.home_menu.HomeMenuController;
import interface_adapter.home_menu.HomeMenuState;
import interface_adapter.home_menu.HomeMenuViewModel;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

// General format of home menu UIs. Will changed based on use cases

public class HomeMenuView extends JPanel {
    private final HomeMenuController controller;
    private final HomeMenuViewModel viewModel;
    private final JTable table;

    public HomeMenuView(HomeMenuController controller, HomeMenuViewModel viewModel) {
        this.controller = controller;
        this.viewModel = viewModel;
        this.setLayout(new BorderLayout());

        // Title and New Entry button

        JLabel titleLabel = new JLabel("MoodVerse");
        titleLabel.setFont(new Font(titleLabel.getFont().getFontName(), Font.BOLD, 24));
        JButton newEntryButton = new JButton("New Entry");

        newEntryButton.addActionListener(e -> {
            // TODO: change based on use case interactor
            controller.newEntry();
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(newEntryButton, BorderLayout.EAST);

        this.add(topPanel, BorderLayout.NORTH);

        // Table
        String[] columnNames = {"Titles", "Date", "Tags"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);

        this.add(new JScrollPane(table), BorderLayout.CENTER);

        // Fake data for demo
        initDummyData();
        refreshTable();

    }

    private void initDummyData() {
        HomeMenuState state = viewModel.getState();
        state.setTitles(List.of("Beach Day", "Winter Night"));
        state.setDates(List.of("9/10/25", "7/11/25"));
        state.setTags(List.of("happy, sunny", "None"));
        viewModel.setState(state);
    }

    private void refreshTable() {
        HomeMenuState state = viewModel.getState();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        List<String> titles = state.getTitles();
        List<String> dates = state.getDates();
        List<String> tags = state.getTags();

        int n = titles.size();
        for (int i = 0; i < n; i++) {
            model.addRow(new Object[]{titles.get(i), dates.get(i), tags.get(i)});
        }
    }
}

//create empty boarder