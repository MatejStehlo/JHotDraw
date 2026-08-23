package org.jhotdraw.gui.action.bdd;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ScenarioState;
import java.awt.Font;
import java.util.Set;
import javax.swing.JPopupMenu;
import org.jhotdraw.draw.AttributeKey;
import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.gui.JFontChooser;

public class ThenFontPalette extends Stage<ThenFontPalette> {

    @ScenarioState
    Set<Figure> selectedFigures;
    @ScenarioState
    Font chosenFont;
    @ScenarioState
    JFontChooser fontChooser;
    @ScenarioState
    JPopupMenu popupMenu;

    public ThenFontPalette every_selected_figure_should_have_the_chosen_font() {
        assertThat(chosenFont).isNotNull();
        for (Figure figure : selectedFigures) {
            verify(figure).set(AttributeKeys.FONT_FACE, chosenFont);
        }
        return self();
    }

    public ThenFontPalette the_figures_font_should_be_unchanged() {
        for (Figure figure : selectedFigures) {
            verify(figure, never()).set(any(AttributeKey.class), any());
        }
        return self();
    }

    public ThenFontPalette the_font_chooser_popup_should_be_hidden() {
        verify(popupMenu).setVisible(false);
        return self();
    }

    public ThenFontPalette the_font_controls_should_be_enabled() {
        verify(fontChooser).setEnabled(true);
        verify(popupMenu).setEnabled(true);
        return self();
    }

    public ThenFontPalette the_font_controls_should_be_disabled() {
        verify(fontChooser).setEnabled(false);
        verify(popupMenu).setEnabled(false);
        return self();
    }

    public ThenFontPalette the_font_chooser_should_display_the_figures_current_font(Font expectedFont) {
        verify(fontChooser).setSelectedFont(expectedFont);
        return self();
    }

    public ThenFontPalette every_selected_figures_bold_attribute_should_become(boolean expectedBold) {
        for (Figure figure : selectedFigures) {
            verify(figure).set(AttributeKeys.FONT_BOLD, expectedBold);
        }
        return self();
    }

    public ThenFontPalette the_undo_should_restore_the_figures_previous_attributes() {
        for (Figure figure : selectedFigures) {
            verify(figure).restoreAttributesTo(any());
        }
        return self();
    }
}
