package com.ui.layouts.workspace;

import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import com.ui.components.common.Divider;

public class WorkspaceSplit extends BasicSplitPaneUI {
    // UTILIZA EL DIVISOR PERSONALIZADO PARA EL PANEL DE TRABAJO
    @Override
    public BasicSplitPaneDivider createDefaultDivider() {
        return new Divider(this);
    }
}