package com.ui.layouts.workspace;

import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import com.ui.components.common.Divider;

// ESTILO DEL PANEL PARTIDO, USA EL DIVISOR PERSONALIZADO
public class WorkspaceSplit extends BasicSplitPaneUI {
    
    // CREA EL DIVISOR PERSONALIZADO EN VEZ DEL POR DEFECTO
    @Override
    public BasicSplitPaneDivider createDefaultDivider() {
        return new Divider(this);
    }
}