package org.jhotdraw.gui.action.bdd;

import static org.mockito.Mockito.mock;
import org.mockito.Mockito;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ScenarioState;
import java.awt.Font;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.swing.JPopupMenu;
import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.figure.TextHolderFigure;
import org.jhotdraw.gui.JFontChooser;

/**
 * BDD "Given" steps for the user story:
 * "As a user I want to be able to change font and its properties".
 * <p>
 */
public class GivenFontPalette extends Stage<GivenFontPalette> {

    @ScenarioState
    DrawingEditor editor;
    @ScenarioState
    DrawingView view;
    @ScenarioState
    Drawing drawing;
    @ScenarioState
    JFontChooser fontChooser;
    @ScenarioState
    JPopupMenu popupMenu;
    @ScenarioState
    Set<Figure> selectedFigures = new LinkedHashSet<>();

    public GivenFontPalette the_font_palette_is_connected_to_the_drawing_editor() {
        editor = mock(DrawingEditor.class);
        view = mock(DrawingView.class);
        drawing = mock(Drawing.class);
        fontChooser = mock(JFontChooser.class);
        popupMenu = mock(JPopupMenu.class);
        Mockito.when(editor.isEnabled()).thenReturn(true);
        Mockito.when(editor.getActiveView()).thenReturn(view);
        Mockito.when(view.getDrawing()).thenReturn(drawing);
        Mockito.when(view.isEnabled()).thenReturn(true);
        applySelectionToView();
        return self();
    }

    public GivenFontPalette a_text_figure_is_selected_with_font(Font currentFont) {
        TextHolderFigure figure = mock(TextHolderFigure.class);
        Mockito.when(figure.getFont()).thenReturn(currentFont);
        selectedFigures.add(figure);
        applySelectionToView();
        return self();
    }

    public GivenFontPalette $_text_figures_are_selected(int count) {
        for (int i = 0; i < count; i++) {
            TextHolderFigure figure = mock(TextHolderFigure.class);
            Mockito.when(figure.getFont()).thenReturn(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
            selectedFigures.add(figure);
        }
        applySelectionToView();
        return self();
    }

    public GivenFontPalette a_figure_is_selected_whose_bold_attribute_is(boolean bold) {
        Figure figure = mock(Figure.class);
        Mockito.when(figure.get(AttributeKeys.FONT_BOLD)).thenReturn(bold);
        selectedFigures.add(figure);
        applySelectionToView();
        return self();
    }

    public GivenFontPalette no_figure_is_selected() {
        applySelectionToView();
        return self();
    }

    private void applySelectionToView() {
        Mockito.when(view.getSelectedFigures()).thenReturn(new LinkedHashSet<>(selectedFigures));
        Mockito.when(view.getSelectionCount()).thenReturn(selectedFigures.size());
    }
}
