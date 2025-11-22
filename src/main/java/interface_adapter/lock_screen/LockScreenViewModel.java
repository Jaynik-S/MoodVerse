package interface_adapter.lock_screen;

import interface_adapter.ViewModel;

public class LockScreenViewModel extends ViewModel<LockScreenState> {
    public LockScreenViewModel() {
        super("lockscreen");
        setState(new LockScreenState());
    }
}

