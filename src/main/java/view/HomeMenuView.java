package view;
import interface_adapter.home_menu.HomeMenuController;
import interface_adapter.home_menu.HomeMenuState;
import interface_adapter.home_menu.HomeMenuViewModel;

import javax.swing.table.*;
import javax.swing.AbstractCellEditor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


// General format of home menu UIs. Will changed based on use cases

public class HomeMenuView extends JPanel implements PropertyChangeListener {
    private final HomeMenuController controller;
    private final HomeMenuViewModel viewModel;
    private final JTable table;
    private final DefaultTableModel model;

    public HomeMenuView(HomeMenuController controller, HomeMenuViewModel viewModel) {
        this.controller = controller;
        this.viewModel = viewModel;
        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());
        setSize(new Dimension(1000, 800));
        setBackground(new Color(245, 248, 255));
        setBorder(BorderFactory.createEmptyBorder(16,16,16,16));

        // Title and New Entry button

        JLabel titleLabel = new JLabel("MoodVerse");
        titleLabel.setFont(new Font(titleLabel.getFont().getFontName(), Font.BOLD, 26));
        titleLabel.setForeground(new Color(30, 64, 175));

        JButton newEntryButton = new JButton("New Entry");
        newEntryButton.setFont(new Font(newEntryButton.getFont().getFontName(), Font.BOLD, 14));
        newEntryButton.setFocusPainted(false);
        newEntryButton.setForeground(Color.BLACK);
        newEntryButton.setBackground(new Color(37, 99, 235));
        newEntryButton.setBorder(BorderFactory.createEmptyBorder(6,14,6,14));
        newEntryButton.addActionListener(e -> {
            controller.newEntry();
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(219, 234, 254));
        topPanel.setBorder(BorderFactory.createEmptyBorder(8, 12, 12,12));
        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(newEntryButton, BorderLayout.EAST);

        this.add(topPanel, BorderLayout.NORTH);

        // Table
        String[] columnNames = {"Titles", "Created", "Updated", "Delete"};
//        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        // Made the table row can be highlighted but can not edit
        model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };


        table = new JTable(model);
        table.setRowHeight(26);
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        StripedTableCellRenderer leftRenderer =
                new StripedTableCellRenderer(SwingConstants.LEFT);
        StripedTableCellRenderer centerRenderer =
                new StripedTableCellRenderer(SwingConstants.CENTER);

        table.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

        table.getColumnModel().getColumn(3).setCellRenderer(new DeleteButtonRenderer());
        table.getColumnModel().getColumn(3).setCellEditor(new DeleteButtonEditor(controller, viewModel));


        // Grid
        table.setShowGrid(true);
        table.setGridColor(new Color(226, 232, 240));

        // Table front and color
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setBackground(Color.PINK);
        tableHeader.setForeground(new Color(55, 61, 81));
        tableHeader.setFont(new Font(tableHeader.getFont().getFontName(), Font.BOLD, 12));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        JPanel tableCard = new JPanel(new BorderLayout());
        tableCard.setBackground(Color.WHITE);
        tableCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(8,8,8,8)
        ));
        tableCard.add(scrollPane, BorderLayout.CENTER);

        add(tableCard, BorderLayout.CENTER);

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

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        refreshTable();
    }
}

class StripedTableCellRenderer extends DefaultTableCellRenderer {

    public StripedTableCellRenderer(int horizontalAlignment) {
        setHorizontalAlignment(horizontalAlignment);
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {

        Component c = super.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);

        if (!isSelected) {
            if (row % 2 == 0) {
                c.setBackground(new Color(250, 252, 255));
            } else {
                c.setBackground(Color.WHITE);
            }
        } else {
            c.setBackground(new Color(219, 234, 254)); // 选中高亮
        }
        return c;
    }
}

// Delete Bottom
class DeleteButtonRenderer extends JButton implements TableCellRenderer {

    public DeleteButtonRenderer() {
        setOpaque(true);
        setForeground(Color.WHITE);
        setBackground(Color.RED);
        setFocusPainted(false);
        setBorder(BorderFactory.createEmptyBorder(4,10,4,10));
        setText("Delete");
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value,
            boolean isSelected, boolean hasFocus,
            int row, int column) {

        if (isSelected) {
            setBackground(new Color(220,38,38));
        }
        else{
            setBackground(new Color(220,68,68));
        }
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
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(239, 68, 68));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(4,10,4,10));
        button.setOpaque(true);
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
        // Save current row
        int viewRow = currentRow;
        JTable table = this.table;

        // stopped editing first, instead of in the end
        fireEditingStopped();

        if (table == null || viewRow < 0) {
            return;
        }

        int modelRow = table.convertRowIndexToModel(viewRow);

        HomeMenuState state = viewModel.getState();

        java.util.List<String> titles = state.getTitles();
        java.util.List<String> created = state.getCreatedDates();
        java.util.List<String> updated = state.getUpdatedDates();
        java.util.List<String> keywords = state.getKeywords();
        java.util.List<String> paths = state.getStoragePaths();

        if (modelRow >= 0 && modelRow < paths.size()) {
            String storagePath = paths.get(modelRow);

            titles.remove(modelRow);
            if (modelRow < created.size()) {
                created.remove(modelRow);
            }
            if (modelRow < updated.size()) {
                updated.remove(modelRow);
            }
            if (modelRow < keywords.size()) {
                keywords.remove(modelRow);
            }
            paths.remove(modelRow);

            DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
            if (modelRow < tableModel.getRowCount()) {
                tableModel.removeRow(modelRow);
            }

            controller.deleteEntry(storagePath);
        }
    }
}