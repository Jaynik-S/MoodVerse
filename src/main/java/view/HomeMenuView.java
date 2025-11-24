package view;
import interface_adapter.home_menu.HomeMenuController;
import interface_adapter.home_menu.HomeMenuState;
import interface_adapter.home_menu.HomeMenuViewModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.AbstractCellEditor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


// General format of home menu UIs. Will changed based on use cases

public class HomeMenuView extends JPanel {
    private final HomeMenuController controller;
    private final HomeMenuViewModel viewModel;
    private final JTable table;
    private final DefaultTableModel model;

    public HomeMenuView(HomeMenuController controller, HomeMenuViewModel viewModel) {
        this.controller = controller;
        this.viewModel = viewModel;
        this.setLayout(new BorderLayout());
        this.setSize(new Dimension(1000, 800));

        // Title and New Entry button

        JLabel titleLabel = new JLabel("MoodVerse");
        titleLabel.setFont(new Font(titleLabel.getFont().getFontName(), Font.BOLD, 24));

        JButton newEntryButton = new JButton("New Entry");
        newEntryButton.addActionListener(e -> {
            controller.newEntry();
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(newEntryButton, BorderLayout.EAST);

        // Background color
        topPanel.setBackground(Color.CYAN);

        //Space between top and table
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        this.add(topPanel, BorderLayout.NORTH);

        // Table
        String[] columnNames = {"Titles", "Created", "Update", ""};
//        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        // Made the table row can be highlighted but can not edit
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };


        table = new JTable(model);

        table.getColumnModel().getColumn(3).setCellRenderer(new DeleteButtonRenderer());
        table.getColumnModel().getColumn(3).setCellEditor(new DeleteButtonEditor(controller, viewModel));


        // Grid
        table.setShowGrid(true);
        table.setGridColor(Color.black);

        // Table front and color
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setBackground(Color.lightGray);
        tableHeader.setForeground(Color.black);
        tableHeader.setFont(new Font(tableHeader.getFont().getFontName(), Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        this.add(scrollPane, BorderLayout.CENTER);

        // Fake data table for demo
//        initDummyData(); // removed for actual use
        refreshTable();

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());

                HomeMenuState state = viewModel.getState();

                // Open Entry
                if (row >= 0 && col == 0) {

                    java.util.List<String> paths = state.getStoragePaths();
                    if (row < paths.size()) {
                        String storagePath = paths.get(row);
                        controller.openEntry(storagePath);
                    }
                }
            }
        });

    }

    private void initDummyData() {
        HomeMenuState state = viewModel.getState();
        state.setTitles(List.of("Beach Day", "Winter Night"));
        state.setCreatedDates(List.of("2025-09-09", "2025-07-10"));
        state.setUpdatedDates(List.of("2025-09-10", "2025-07-11"));
        state.setKeywords(List.of("happy, sunny", "None"));
//        state.setEntryIDs(List.of(1, 2));

        state.setStoragePaths(List.of(
                "data/entries/entry1.json",
                "data/entries/entry2.json"
        ));

        viewModel.setState(state);
    }

    private void refreshTable() {
        HomeMenuState state = viewModel.getState();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        List<String> titles = state.getTitles();
        List<String> createdDates = state.getCreatedDates();
        List<String> updatedDates = state.getUpdatedDates();

        int n = titles.size();
        for (int i = 0; i < n; i++) {
            String createdToShow = i < createdDates.size() ? createdDates.get(i) : "";
            String updateToShow = i < updatedDates.size() ? updatedDates.get(i) : "";
            model.addRow(new Object[]{titles.get(i), createdToShow, updateToShow, "Delete"});
        }
    }
}

// Delete Bottom
class DeleteButtonRenderer extends JButton implements TableCellRenderer {

    public DeleteButtonRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value,
            boolean isSelected, boolean hasFocus,
            int row, int column) {
        setText("Delete");
        return this;
    }
}

class DeleteButtonEditor extends AbstractCellEditor
        implements TableCellEditor, ActionListener {

    private final JButton button = new JButton("Delete");
    private int currentRow;
    private JTable table;

    private final HomeMenuController controller;
    private final HomeMenuViewModel viewModel;

    public DeleteButtonEditor(HomeMenuController controller, HomeMenuViewModel viewModel) {
        this.controller = controller;
        this.viewModel = viewModel;
        button.addActionListener(this);
    }

    @Override
    public Component getTableCellEditorComponent(
            JTable table, Object value,
            boolean isSelected, int row, int column) {
        this.table = table;
        this.currentRow = row;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return "Delete";
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int modelRow = table.convertRowIndexToModel(currentRow);

        HomeMenuState state = viewModel.getState();
        java.util.List<String> paths = state.getStoragePaths();
        if (modelRow < paths.size()) {
            String storagePath = paths.get(modelRow);
            controller.deleteEntry(storagePath);
        }

        fireEditingStopped();
    }
}

