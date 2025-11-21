package interface_adapter.home_menu;
import use_case.create_entry.CreateEntryInputBoundary;
import use_case.load_entry.LoadEntryInputBoundary;
import use_case.load_entry.LoadEntryInputData;
import use_case.delete_entry.DeleteEntryInputBoundary;
import use_case.delete_entry.DeleteEntryInputData;

public class HomeMenuController {

    private final CreateEntryInputBoundary createEntryUseCase;
    private final LoadEntryInputBoundary loadEntryUseCase;
    private final DeleteEntryInputBoundary deleteEntryUseCase;

    public HomeMenuController(CreateEntryInputBoundary createEntryUseCase,
                              LoadEntryInputBoundary loadEntryUseCase,
                              DeleteEntryInputBoundary deleteEntryUseCase) {
        //Base on 1.create_entry and 2.load_entry use casel
        this.createEntryUseCase = createEntryUseCase;
        this.loadEntryUseCase = loadEntryUseCase;
        this.deleteEntryUseCase = deleteEntryUseCase;
    }

    public HomeMenuController() {
        //Only Used for HomeMenuDemoMain
        this.createEntryUseCase = null;
        this.loadEntryUseCase = null;
        this.deleteEntryUseCase = null;
    }

    public void newEntry(){
        System.out.println("New Entry Clicked");
        if (createEntryUseCase != null) {
            createEntryUseCase.execute();
        }
    }

    public void openEntry(String  storagePath){
        System.out.println("Open Entry Clicked" + storagePath);
        if (loadEntryUseCase != null) {
            LoadEntryInputData inputData = new LoadEntryInputData(storagePath);
            loadEntryUseCase.execute(inputData);
        }
    }

    public void deleteEntry(String  storagePath) {
        System.out.println("Delete Entry Clicked" + storagePath);

        if (deleteEntryUseCase != null) {
            DeleteEntryInputData inputData = new DeleteEntryInputData(storagePath);
            deleteEntryUseCase.execute(inputData);
        }
    }
}



