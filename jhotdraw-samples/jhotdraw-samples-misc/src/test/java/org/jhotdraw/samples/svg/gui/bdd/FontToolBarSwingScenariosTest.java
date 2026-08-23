package org.jhotdraw.samples.svg.gui.bdd;

import com.tngtech.jgiven.junit.ScenarioTest;
import org.junit.Test;

/**
 * BDD scenario for the user story:
 * <p>
 * "As a user I want to be able to change font and its properties."
 * <p>
 * Unlike {@code FontPaletteScenariosTest} (jhotdraw-gui, which mocks every
 * collaborator), this scenario drives the REAL Swing {@code FontToolBar} in
 * a real, visible window using AssertJ-Swing - an actual robot performs the
 * keystrokes a user would. This opens a window and takes over the mouse/
 * keyboard, so it is only run when explicitly requested.
 */
public class FontToolBarSwingScenariosTest extends ScenarioTest<GivenFontToolBarUi, WhenFontToolBarUi, ThenFontToolBarUi> {

    @Test
    public void changing_the_font_size_in_the_real_toolbar_resizes_the_selected_figure() {
        given().the_real_font_toolbar_is_showing_in_a_window()
                .and().a_text_figure_is_selected_with_font_size(12d);

        when().the_user_types_a_new_font_size_and_presses_enter("24");

        then().the_figures_font_size_should_be(24d);
    }
}
