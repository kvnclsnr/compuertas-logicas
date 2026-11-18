package com.ui.history;

import java.util.ArrayList;
import java.util.List;

public class UndoRedoManager {
    private static final int DEFAULT_MAX_CAPACITY = 200;
    
    private final List<CanvasCommand> history = new ArrayList<>();
    private final List<UndoRedoListener> listeners = new ArrayList<>();
    
    private int currentIndex = 0;
    private int savedIndex = 0;
    private boolean executing = false;
    private final int maxCapacity;
    
    public UndoRedoManager() {
        this(DEFAULT_MAX_CAPACITY);
    }
    
    public UndoRedoManager(int maxCapacity) {
        this.maxCapacity = Math.max(10, maxCapacity);
    }
    
    public boolean canUndo() {
        return currentIndex > 0;
    }
    
    public boolean canRedo() {
        return currentIndex < history.size();
    }
    
    public boolean isExecuting() {
        return executing;
    }
    
    public synchronized void push(CanvasCommand command) {
        if (command == null || executing) {
            return;
        }
        pushInternal(command);
        notifyListeners();
    }
    
    private void pushInternal(CanvasCommand command) {
        // Truncate redo history beyond current index
        if (currentIndex < history.size()) {
            if (savedIndex > currentIndex) {
                savedIndex = -1;
            }
            history.subList(currentIndex, history.size()).clear();
        }
        
        history.add(command);
        currentIndex++;
        
        // Enforce capacity limit
        if (history.size() > maxCapacity) {
            history.remove(0);
            currentIndex--;
            if (savedIndex > 0) {
                savedIndex--;
            } else if (savedIndex == 0) {
                savedIndex = -1;
            }
        }
    }
    
    public synchronized void execute(CanvasCommand command) {
        if (command == null || executing) {
            return;
        }
        executing = true;
        try {
            command.redo();
            pushInternal(command);
        } finally {
            executing = false;
            notifyListeners();
        }
    }
    
    public synchronized void undo() {
        if (!canUndo() || executing) {
            return;
        }
        
        executing = true;
        try {
            currentIndex--;
            CanvasCommand command = history.get(currentIndex);
            command.undo();
        } finally {
            executing = false;
            notifyListeners();
        }
    }
    
    public synchronized void redo() {
        if (!canRedo() || executing) {
            return;
        }
        
        executing = true;
        try {
            CanvasCommand command = history.get(currentIndex);
            command.redo();
            currentIndex++;
        } finally {
            executing = false;
            notifyListeners();
        }
    }
    
    public boolean hasUnsavedChanges() {
        return currentIndex != savedIndex;
    }
    
    public synchronized void markSaved() {
        savedIndex = currentIndex;
        notifyListeners();
    }
    
    public synchronized void reset() {
        history.clear();
        currentIndex = 0;
        savedIndex = 0;
        notifyListeners();
    }
    
    public int getUndoCount() {
        return currentIndex;
    }
    
    public int getRedoCount() {
        return history.size() - currentIndex;
    }
    
    public void addListener(UndoRedoListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    public void removeListener(UndoRedoListener listener) {
        listeners.remove(listener);
    }
    
    private void notifyListeners() {
        for (UndoRedoListener listener : new ArrayList<>(listeners)) {
            try {
                listener.historyChanged();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
