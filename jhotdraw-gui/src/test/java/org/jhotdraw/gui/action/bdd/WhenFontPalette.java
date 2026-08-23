package org.jhotdraw.gui.action.bdd;

import static org.mockito.Mockito.verify;
import org.mockito.Mockito;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ScenarioState;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import javax.swing.JPopupMenu;
import javax.swing.undo.UndoableEdit;
import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.action.AttributeToggler;
import org.jhotdraw.gui.JFontChooser;
import org.jhotdraw.gui.action.FontChooserHandler;
import org.mockito.ArgumentCaptor;

public class WhenFontPalette extends Stage<WhenFontPalette> {

    @ScenarioState
    DrawingEditor editor;
    @ScenarioState
    Drawing drawing;
    @ScenarioState
    JFontChooser fontChooser;
    @ScenarioState
    JPopupMenu popupMenu;

    @ScenarioState
    FontChooserHandler handler;
    @ScenarioState
    Font chosenFont;
    @ScenarioState
    UndoableEdit lastEdit;

    public WhenFontPalette the_user_chooses_font_$_size_$_and_confirms(String family, int size) {
        chosenFont = new Font(family, Font.PLAIN, size);
        Mockito.when(fontChooser.getSelectedFont()).thenReturn(chosenFont);
        handler = new FontChooserHandler(editor, AttributeKeys.FONT_FACE, fontChooser, popupMenu);

        handler.actionPerformed(new ActionEvent(fontChooser, ActionEvent.ACTION_PERFORMED, JFontChooser.APPROVE_SELECTION));

        captureLastEdit();
        return self();
    }

    public WhenFontPalette the_user_cancels_the_font_chooser() {
        handler = new FontChooserHandler(editor, AttributeKeys.FONT_FACE, fontChooser, popupMenu);

        handler.actionPerformed(new ActionEvent(fontChooser, ActionEvent.ACTION_PERFORMED, JFontChooser.CANCEL_SELECTION));
        return self();
    }

    public WhenFontPalette the_user_undoes_the_last_change() {
        lastEdit.undo();
        return self();
    }

    public WhenFontPalette the_font_toolbar_is_shown() {
        handler = new FontChooserHandler(editor, AttributeKeys.FONT_FACE, fontChooser, popupMenu);
        return self();
    }

    public WhenFontPalette the_user_clicks_the_bold_button() {
        AttributeToggler<Boolean> toggler = new AttributeToggler<>(
                editor, AttributeKeys.FONT_BOLD, Boolean.TRUE, Boolean.FALSE);
        toggler.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "bold"));
        return self();
    }

    private void captureLastEdit() {
        ArgumentCaptor<UndoableEdit> captor = ArgumentCaptor.forClass(UndoableEdit.class);
        verify(drawing).fireUndoableEditHappened(captor.capture());
        lastEdit = captor.getValue();
    }
}
