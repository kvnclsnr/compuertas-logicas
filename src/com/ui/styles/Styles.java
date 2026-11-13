package com.ui.styles;

import java.awt.Color;
import java.awt.Font;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

public final class Styles {

    public static void setBorder(JComponent component, Border border) {
        component.setBorder(border);
    }
    
    public static void setBorder(JComponent component, Color color) {
        Border currentBorder = component.getBorder();
        Border lineBorder = BorderFactory.createLineBorder(color);
        
        if (currentBorder instanceof CompoundBorder) {
            CompoundBorder compoundBorder = (CompoundBorder) currentBorder;
            
            if (compoundBorder.getOutsideBorder() instanceof LineBorder
            ||  compoundBorder.getOutsideBorder() instanceof MatteBorder
            ) {
                currentBorder = compoundBorder.getInsideBorder();
            }
        }
        
        else if (currentBorder instanceof LineBorder || currentBorder instanceof MatteBorder) {
            currentBorder = null;
        }
        
        component.setBorder(currentBorder == null
            ? lineBorder
            : BorderFactory.createCompoundBorder(lineBorder, currentBorder)
        );
    }
    
    public static void setBorder(JComponent component, int top, int left, int bottom, int right, Color color) {
        Border currentBorder = component.getBorder();
        Border matteBorder = BorderFactory.createMatteBorder(top, left, bottom, right, color);
        
        if (currentBorder instanceof CompoundBorder) {
            CompoundBorder compoundBorder = (CompoundBorder) currentBorder;
            
            if (compoundBorder.getOutsideBorder() instanceof LineBorder
            ||  compoundBorder.getOutsideBorder() instanceof MatteBorder
            ) {
                currentBorder = compoundBorder.getInsideBorder();
            }
        }
        
        else if (currentBorder instanceof LineBorder || currentBorder instanceof MatteBorder) {
            currentBorder = null;
        }
        
        component.setBorder(currentBorder == null
            ? matteBorder
            : BorderFactory.createCompoundBorder(matteBorder, currentBorder)
        );
    }
    
    public static void setBorder(JComponent component, int vertical, int horizontal, Color color) {
        Border currentBorder = component.getBorder();
        Border matteBorder = BorderFactory.createMatteBorder(vertical, horizontal, vertical, horizontal, color);
        
        if (currentBorder instanceof CompoundBorder) {
            CompoundBorder compoundBorder = (CompoundBorder) currentBorder;
            
            if (compoundBorder.getOutsideBorder() instanceof LineBorder
            ||  compoundBorder.getOutsideBorder() instanceof MatteBorder
            ) {
                currentBorder = compoundBorder.getInsideBorder();
            }
        }
        
        else if (currentBorder instanceof LineBorder || currentBorder instanceof MatteBorder) {
            currentBorder = null;
        }
        
        component.setBorder(currentBorder == null
            ? matteBorder
            : BorderFactory.createCompoundBorder(matteBorder, currentBorder)
        );
    }
    
    public static void setPadding(JComponent component, int top, int left, int bottom, int right) {
        Border currentBorder = component.getBorder();
        Border padding = BorderFactory.createEmptyBorder(top, left, bottom, right);
        
        if (currentBorder instanceof CompoundBorder) {
            CompoundBorder compoundBorder = (CompoundBorder) currentBorder;
            
            if (compoundBorder.getInsideBorder() instanceof EmptyBorder) {
                currentBorder = compoundBorder.getOutsideBorder();
            }
        }
        
        else if (currentBorder instanceof EmptyBorder) {
            currentBorder = null;
        }
        
        component.setBorder(currentBorder == null
            ? padding
            : BorderFactory.createCompoundBorder(currentBorder, padding)
        );
    }
    
    public static void setPadding(JComponent component, int vertical, int horizontal) {
        Border currentBorder = component.getBorder();
        Border padding = BorderFactory.createEmptyBorder(vertical, horizontal, vertical, horizontal);
        
        if (currentBorder instanceof CompoundBorder) {
            CompoundBorder compoundBorder = (CompoundBorder) currentBorder;
            
            if (compoundBorder.getInsideBorder() instanceof EmptyBorder) {
                currentBorder = compoundBorder.getOutsideBorder();
            }
        }
        
        else if (currentBorder instanceof EmptyBorder) {
            currentBorder = null;
        }
        
        component.setBorder(currentBorder == null
            ? padding
            : BorderFactory.createCompoundBorder(currentBorder, padding)
        );
    }
    
    public static void setPadding(JComponent component, int pad) {
        Border currentBorder = component.getBorder();
        Border padding = BorderFactory.createEmptyBorder(pad, pad, pad, pad);
        
        if (currentBorder instanceof CompoundBorder) {
            CompoundBorder compoundBorder = (CompoundBorder) currentBorder;
            
            if (compoundBorder.getInsideBorder() instanceof EmptyBorder) {
                currentBorder = compoundBorder.getOutsideBorder();
            }
        }
        
        else if (currentBorder instanceof EmptyBorder) {
            currentBorder = null;
        }
        
        component.setBorder(currentBorder == null
            ? padding
            : BorderFactory.createCompoundBorder(currentBorder, padding)
        );
    }
    
    public static void setBackground(JComponent component, Color color) {
        component.setBackground(color);
    }
    
    public static void setForeground(JComponent component, Color color) {
        component.setForeground(color);
    }
    
    public static void setFont(JComponent component, float size, boolean bold) {
        int style = bold ? Font.BOLD : Font.PLAIN;
        component.setFont(component.getFont().deriveFont(style, size));
    }
    
    public static void setFont(JComponent component, float size) {
        component.setFont(component.getFont().deriveFont(Font.PLAIN, size));
    }
    
    public static void setOpaque(JComponent component, boolean opaque) {
        component.setOpaque(opaque);
    }
    
    public static void setBorderPainted(AbstractButton button, boolean painted) {
        button.setBorderPainted(painted);
    }
    
    public static void setFocusPainted(AbstractButton button, boolean painted) {
        button.setFocusPainted(painted);
    }
}