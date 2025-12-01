package app;

import interface_adapter.home_menu.HomeMenuPresenter;
import use_case.verify_password.RenderEntriesUserDataInterface;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Map;

/**
 * Listens for the HomeMenu view being shown and refreshes the entries table.
 */
public class HomeMenuRefreshListener implements PropertyChangeListener {

    private final String homeViewName;
    private final RenderEntriesUserDataInterface entriesDataAccess;
    private final HomeMenuPresenter presenter;

    public HomeMenuRefreshListener(String homeViewName,
                                   RenderEntriesUserDataInterface entriesDataAccess,
                                   HomeMenuPresenter presenter) {
        this.homeViewName = homeViewName;
        this.entriesDataAccess = entriesDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (!"state".equals(evt.getPropertyName())) {
            return;
        }
        Object newValue = evt.getNewValue();
        if (!(newValue instanceof String) || !homeViewName.equals(newValue)) {
            return;
        }
        if (entriesDataAccess == null || presenter == null) {
            return;
        }
        try {
            List<Map<String, Object>> entries = entriesDataAccess.getAll();
            presenter.presentEntriesFromData(entries);
        }
        catch (Exception error) {
            presenter.presentError("Failed to load entries: " + error.getMessage());
        }
    }
}

