package org.jhotdraw.samples.svg.gui.bdd;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ScenarioState;
import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.jhotdraw.draw.gui.JAttributeTextField;

/**
 * BDD "When" steps: real, robot-driven interaction with the font toolbar's
 * Swing widgets (as opposed to calling production methods directly).
 */
public class WhenFontToolBarUi extends Stage<WhenFontToolBarUi> {

    @ScenarioState
    FrameFixture window;

    public WhenFontToolBarUi the_user_types_a_new_font_size_and_presses_enter(String newSize) {
        window.robot().waitForIdle();

        // The face field and the size field are both JAttributeTextField
        // instances; FontToolBar.addFaceField()/addSizeControls() give them
        // distinct column counts (2 and 1 respectively - see FontToolBar.java),
        // which is the only reliable way to tell them apart without a
        // component name.
        JAttributeTextField<?> sizeFieldComponent = window.robot().finder().find(
                new GenericTypeMatcher<JAttributeTextField>(JAttributeTextField.class) {
                    @Override
                    protected boolean isMatching(JAttributeTextField component) {
                        return component.getColumns() == 1;
                    }
                });

        // Blocks until a real focus-gained event actually fires, instead of
        // firing a click and hoping the window manager honored it in time -
        // selectAll()/enterText() alone use EDT-side focus requests, which
        // are not reliably enough for the following key events to land on
        // the right component.
        window.robot().focusAndWaitForFocusGain(sizeFieldComponent);

        JTextComponentFixture sizeField = new JTextComponentFixture(window.robot(), sizeFieldComponent);
        sizeField.selectAll();
        sizeField.enterText(newSize);
        sizeField.pressAndReleaseKeys(java.awt.event.KeyEvent.VK_ENTER);
        window.robot().waitForIdle();
        return self();
    }
}
