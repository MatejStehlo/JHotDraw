package org.jhotdraw.gui.action.bdd;

import com.tngtech.jgiven.junit.ScenarioTest;
import java.awt.Font;
import org.junit.Test;

/**
 * BDD scenarios for the user story:
 * <p>
 * "As a user I want to be able to change font and its properties."
 * <p>
 */
public class FontPaletteScenariosTest extends ScenarioTest<GivenFontPalette, WhenFontPalette, ThenFontPalette> {

    @Test
    public void changing_the_font_applies_it_to_the_selected_figure() {
        given().the_font_palette_is_connected_to_the_drawing_editor()
                .and().a_text_figure_is_selected_with_font(new Font(Font.SANS_SERIF, Font.PLAIN, 12));

        when().the_user_chooses_font_$_size_$_and_confirms(Font.SERIF, 24);

        then().every_selected_figure_should_have_the_chosen_font()
                .and().the_font_chooser_popup_should_be_hidden();
    }

    @Test
    public void changing_the_font_applies_it_to_every_selected_figure() {
        given().the_font_palette_is_connected_to_the_drawing_editor()
                .and().$_text_figures_are_selected(3);

        when().the_user_chooses_font_$_size_$_and_confirms(Font.MONOSPACED, 16);

        then().every_selected_figure_should_have_the_chosen_font();
    }

    @Test
    public void cancelling_the_font_chooser_leaves_the_figures_font_unchanged() {
        given().the_font_palette_is_connected_to_the_drawing_editor()
                .and().a_text_figure_is_selected_with_font(new Font(Font.SANS_SERIF, Font.PLAIN, 12));

        when().the_user_cancels_the_font_chooser();

        then().the_figures_font_should_be_unchanged()
                .and().the_font_chooser_popup_should_be_hidden();
    }

    @Test
    public void undoing_a_font_change_restores_the_figures_previous_attributes() {
        given().the_font_palette_is_connected_to_the_drawing_editor()
                .and().a_text_figure_is_selected_with_font(new Font(Font.SANS_SERIF, Font.PLAIN, 12));

        when().the_user_chooses_font_$_size_$_and_confirms(Font.SERIF, 24)
                .and().the_user_undoes_the_last_change();

        then().the_undo_should_restore_the_figures_previous_attributes();
    }

    @Test
    public void font_controls_are_disabled_when_nothing_is_selected() {
        given().the_font_palette_is_connected_to_the_drawing_editor()
                .and().no_figure_is_selected();

        when().the_font_toolbar_is_shown();

        then().the_font_controls_should_be_disabled();
    }

    @Test
    public void font_controls_reflect_the_selected_figures_current_font() {
        Font existingFont = new Font(Font.SERIF, Font.ITALIC, 24);
        given().the_font_palette_is_connected_to_the_drawing_editor()
                .and().a_text_figure_is_selected_with_font(existingFont);

        when().the_font_toolbar_is_shown();

        then().the_font_controls_should_be_enabled()
                .and().the_font_chooser_should_display_the_figures_current_font(existingFont);
    }

    @Test
    public void clicking_bold_turns_bold_on_for_a_plain_figure() {
        given().the_font_palette_is_connected_to_the_drawing_editor()
                .and().a_figure_is_selected_whose_bold_attribute_is(false);

        when().the_user_clicks_the_bold_button();

        then().every_selected_figures_bold_attribute_should_become(true);
    }

    @Test
    public void clicking_bold_again_turns_bold_off() {
        given().the_font_palette_is_connected_to_the_drawing_editor()
                .and().a_figure_is_selected_whose_bold_attribute_is(true);

        when().the_user_clicks_the_bold_button();

        then().every_selected_figures_bold_attribute_should_become(false);
    }
}
