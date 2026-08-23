package org.jhotdraw.samples.svg.gui.bdd;

import static org.assertj.core.api.Assertions.assertThat;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.AfterScenario;
import com.tngtech.jgiven.annotation.ScenarioState;
import org.assertj.swing.fixture.FrameFixture;
import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.figure.TextFigure;

/**
 * BDD "Then" steps: AssertJ domain assertions on the real figure that the
 * real toolbar UI was driven against.
 */
public class ThenFontToolBarUi extends Stage<ThenFontToolBarUi> {

    @ScenarioState
    TextFigure figure;
    @ScenarioState
    FrameFixture window;

    public ThenFontToolBarUi the_figures_font_size_should_be(double expectedSize) {
        assertThat(figure.get(AttributeKeys.FONT_SIZE)).isEqualTo(expectedSize);
        assertThat(AttributeKeys.getFont(figure).getSize2D()).isEqualTo((float) expectedSize);
        return self();
    }

    @AfterScenario
    public void closeTheWindow() {
        if (window != null) {
            window.cleanUp();
        }
    }
}
