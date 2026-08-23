package org.jhotdraw.samples.svg.gui.bdd;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ScenarioState;
import javax.swing.JFrame;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.jhotdraw.draw.AttributeKeys;
import org.jhotdraw.draw.DefaultDrawing;
import org.jhotdraw.draw.DefaultDrawingEditor;
import org.jhotdraw.draw.DefaultDrawingView;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.figure.TextFigure;
import org.jhotdraw.samples.svg.gui.FontToolBar;

/**
 * BDD "Given" steps that drive the REAL Swing {@link FontToolBar} - not a
 * mock - inside a real, visible {@link JFrame}, automated with AssertJ-Swing.
 * Complements {@code FontPaletteScenariosTest} (jhotdraw-gui), which tests
 * the same user story at the domain/use-case level with mocks.
 */
public class GivenFontToolBarUi extends Stage<GivenFontToolBarUi> {

    @ScenarioState
    DefaultDrawingEditor editor;
    @ScenarioState
    DefaultDrawingView view;
    @ScenarioState
    Drawing drawing;
    @ScenarioState
    TextFigure figure;
    @ScenarioState
    FrameFixture window;

    public GivenFontToolBarUi the_real_font_toolbar_is_showing_in_a_window() {
        editor = new DefaultDrawingEditor();
        drawing = new DefaultDrawing();
        view = new DefaultDrawingView();
        view.setDrawing(drawing);
        editor.add(view);
        editor.setActiveView(view);

        JFrame frame = GuiActionRunner.execute(() -> {
            JFrame f = new JFrame("Font palette BDD scenario");
            FontToolBar toolBar = new FontToolBar();
            // FontToolBar must already have a parent before setEditor() is
            // called: setEditor() drives JDisclosureToolBar.setDisclosureState(),
            // which dereferences getParent() and throws NullPointerException
            // if the toolbar is still unparented.
            f.getContentPane().add(toolBar);
            toolBar.setEditor(editor);
            f.setSize(360, 240);
            return f;
        });

        window = new FrameFixture(frame);
        window.show();
        return self();
    }

    public GivenFontToolBarUi a_text_figure_is_selected_with_font_size(double size) {
        figure = new TextFigure();
        figure.setText("Hello");
        figure.set(AttributeKeys.FONT_SIZE, size);
        drawing.add(figure);
        view.addToSelection(figure);
        return self();
    }
}
