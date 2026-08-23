/*
 * @(#)FontChooserHandlerTest.java
 *
 * JUnit 4 tests for FontChooserHandler - the use-case class of the font
 * palette feature: it applies the font chosen in a JFontChooser popup to the
 * current figure selection, keeps the popup in sync with the selection, and
 * records an undoable edit.
 *
 * All collaborators (DrawingEditor, DrawingView, Drawing, Figure,
 * JFontChooser, JPopupMenu) are Mockito mocks, so every test drives a single
 * code path through FontChooserHandler itself.
 */
package org.jhotdraw.gui.action;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.swing.JPopupMenu;
import javax.swing.undo.UndoableEdit;
import org.jhotdraw.draw.AttributeKey;
import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.figure.TextHolderFigure;
import org.jhotdraw.gui.JFontChooser;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class FontChooserHandlerTest {

    private static final AttributeKey<Font> KEY = AttributeKeys.FONT_FACE;

    private DrawingEditor editor;
    private DrawingView view;
    private Drawing drawing;
    private JFontChooser fontChooser;
    private JPopupMenu popupMenu;

    @Before
    public void setUp() {
        editor = mock(DrawingEditor.class);
        view = mock(DrawingView.class);
        drawing = mock(Drawing.class);
        fontChooser = mock(JFontChooser.class);
        popupMenu = mock(JPopupMenu.class);

        when(editor.isEnabled()).thenReturn(true);
        when(editor.getActiveView()).thenReturn(view);
        when(view.getDrawing()).thenReturn(drawing);
        when(view.isEnabled()).thenReturn(true);
    }

    private FontChooserHandler newHandler() {
        return new FontChooserHandler(editor, KEY, fontChooser, popupMenu);
    }

    // ----- best case: applying a chosen font -------------------------------

    @Test
    public void propertyChange_selectedFont_appliesFontToEverySelectedFigure() {
        Figure figure1 = mock(Figure.class);
        Figure figure2 = mock(Figure.class);
        Set<Figure> selection = new LinkedHashSet<>();
        selection.add(figure1);
        selection.add(figure2);
        when(view.getSelectedFigures()).thenReturn(selection);
        when(view.getSelectionCount()).thenReturn(selection.size());
        Font chosen = new Font(Font.SANS_SERIF, Font.BOLD, 18);
        when(fontChooser.getSelectedFont()).thenReturn(chosen);

        FontChooserHandler handler = newHandler();
        handler.propertyChange(new PropertyChangeEvent(fontChooser, JFontChooser.SELECTED_FONT_PROPERTY, null, chosen));

        for (Figure figure : selection) {
            verify(figure).willChange();
            verify(figure).set(KEY, chosen);
            verify(figure).changed();
        }
        verify(editor).setDefaultAttribute(KEY, chosen);
        verify(drawing).fireUndoableEditHappened(any(UndoableEdit.class));
    }

    @Test
    public void actionPerformed_approveSelection_appliesFontAndHidesPopup() {
        Figure figure = mock(Figure.class);
        when(view.getSelectedFigures()).thenReturn(Collections.singleton(figure));
        when(view.getSelectionCount()).thenReturn(1);
        Font chosen = new Font(Font.SANS_SERIF, Font.PLAIN, 11);
        when(fontChooser.getSelectedFont()).thenReturn(chosen);

        FontChooserHandler handler = newHandler();
        handler.actionPerformed(new ActionEvent(fontChooser, ActionEvent.ACTION_PERFORMED, JFontChooser.APPROVE_SELECTION));

        verify(figure).set(KEY, chosen);
        verify(popupMenu).setVisible(false);
    }

    // ----- boundary: cancelling must not touch any figure -----------------

    @Test
    public void actionPerformed_cancelSelection_doesNotApplyFontButStillHidesPopup() {
        Figure figure = mock(Figure.class);
        when(view.getSelectedFigures()).thenReturn(Collections.singleton(figure));
        when(view.getSelectionCount()).thenReturn(1);

        FontChooserHandler handler = newHandler();
        handler.actionPerformed(new ActionEvent(fontChooser, ActionEvent.ACTION_PERFORMED, JFontChooser.CANCEL_SELECTION));

        verify(figure, never()).set(eq(KEY), any());
        verify(popupMenu).setVisible(false);
    }

    // ----- boundary: empty selection ---------------------------------------

    @Test
    public void propertyChange_selectedFont_withEmptySelection_stillUpdatesDefaultAttributeWithoutTouchingFigures() {
        when(view.getSelectedFigures()).thenReturn(Collections.emptySet());
        when(view.getSelectionCount()).thenReturn(0);
        Font chosen = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
        when(fontChooser.getSelectedFont()).thenReturn(chosen);

        FontChooserHandler handler = newHandler();
        handler.propertyChange(new PropertyChangeEvent(fontChooser, JFontChooser.SELECTED_FONT_PROPERTY, null, chosen));

        // No figure to touch, but the toolbar's "default font for new
        // figures" must still be updated and the (no-op) edit still fired.
        verify(editor).setDefaultAttribute(KEY, chosen);
        verify(drawing).fireUndoableEditHappened(any(UndoableEdit.class));
    }

    // ----- best case / boundary: keeping the chooser in sync with the selection -----

    @Test
    public void updateEnabledState_selectionContainsTextHolderFigure_syncsChooserToItsFont() {
        Font figureFont = new Font(Font.SERIF, Font.ITALIC, 24);
        TextHolderFigure textFigure = mock(TextHolderFigure.class);
        when(textFigure.getFont()).thenReturn(figureFont);
        when(view.getSelectedFigures()).thenReturn(Collections.singleton((Figure) textFigure));
        when(view.getSelectionCount()).thenReturn(1);

        newHandler(); // constructor calls updateEnabledState()

        verify(fontChooser).setEnabled(true);
        verify(popupMenu).setEnabled(true);
        verify(fontChooser).setSelectedFont(figureFont);
    }

    @Test
    public void updateEnabledState_noSelection_disablesChooserAndNeverTouchesSelectedFont() {
        when(view.getSelectedFigures()).thenReturn(Collections.emptySet());
        when(view.getSelectionCount()).thenReturn(0);

        newHandler();

        verify(fontChooser).setEnabled(false);
        verify(popupMenu).setEnabled(false);
        verify(fontChooser, never()).setSelectedFont(any());
    }

    @Test
    public void updateEnabledState_selectionWithoutTextHolderFigure_leavesChooserFontUntouched() {
        // A figure that is not a TextHolderFigure (e.g. a plain shape) must
        // not be asked for a font.
        Figure nonTextFigure = mock(Figure.class);
        when(view.getSelectedFigures()).thenReturn(Collections.singleton(nonTextFigure));
        when(view.getSelectionCount()).thenReturn(1);

        newHandler();

        verify(fontChooser).setEnabled(true);
        verify(fontChooser, never()).setSelectedFont(any());
    }

    // ----- business rule: the recorded edit must correctly undo/redo -------

    @Test
    public void appliedFont_undoRestoresPreviousAttributes_redoReappliesChosenFont() {
        Figure figure = mock(Figure.class);
        Object restoreData = new Object();
        when(figure.getAttributesRestoreData()).thenReturn(restoreData);
        when(view.getSelectedFigures()).thenReturn(Collections.singleton(figure));
        when(view.getSelectionCount()).thenReturn(1);
        Font chosen = new Font(Font.SANS_SERIF, Font.BOLD, 20);
        when(fontChooser.getSelectedFont()).thenReturn(chosen);

        FontChooserHandler handler = newHandler();
        handler.propertyChange(new PropertyChangeEvent(fontChooser, JFontChooser.SELECTED_FONT_PROPERTY, null, chosen));

        ArgumentCaptor<UndoableEdit> captor = ArgumentCaptor.forClass(UndoableEdit.class);
        verify(drawing).fireUndoableEditHappened(captor.capture());
        UndoableEdit edit = captor.getValue();

        assertTrue("a freshly fired edit must be undoable", edit.canUndo());
        edit.undo();
        verify(figure).restoreAttributesTo(restoreData);

        assertTrue("after undo, the edit must be redoable", edit.canRedo());
        edit.redo();
        // set(KEY, chosen) must now have happened twice: once when the font
        // was first applied, once again on redo.
        verify(figure, times(2)).set(KEY, chosen);
    }

    // ----- Java assertion: FontChooserHandler must not be handed a null key -----

    @Test(expected = AssertionError.class)
    public void constructor_nullAttributeKey_violatesInvariant() {
        // A null attribute key would be a wiring bug (see ButtonFactory,
        // the only production caller), never a legitimate runtime input -
        // enforced with an assertion in the constructor (see
        // FontChooserHandler.java), not an exception.
        new FontChooserHandler(editor, null, fontChooser, popupMenu);
    }

    // Sanity check that the invariant tests above aren't vacuously true
    // because assertions happen to be disabled in this build (`assert`
    // statements are no-ops unless the JVM is run with -ea; Maven Surefire
    // enables them by default, but this catches it if that ever changes).
    @Test
    public void assertionsAreEnabledInThisTestRun() {
        boolean assertionsEnabled = false;
        assert assertionsEnabled = true; // executes only if -ea is active
        assertTrue("assertions are disabled - the *_violatesInvariant tests above would pass vacuously",
                assertionsEnabled);
    }
}
