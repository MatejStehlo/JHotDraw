/*
 * @(#)AttributeKeysFontTest.java
 *
 * JUnit 4 tests for the font-related domain logic of AttributeKeys:
 * AttributeKeys.getFont(Figure) and AttributeKeys.getFontStyle(Figure).
 *
 * These two methods are the piece of domain logic that combines a figure's
 * FONT_FACE/FONT_BOLD/FONT_ITALIC/FONT_SIZE attributes into the effective
 * java.awt.Font used to render/edit text - i.e. the "business rule" behind
 * the font palette feature.
 *
 * Figure is mocked with Mockito so each test exercises a single code path
 * through AttributeKeys alone, without depending on any concrete Figure
 * implementation.
 */
package org.jhotdraw.draw;

import static org.jhotdraw.draw.AttributeKeys.FONT_BOLD;
import static org.jhotdraw.draw.AttributeKeys.FONT_FACE;
import static org.jhotdraw.draw.AttributeKeys.FONT_ITALIC;
import static org.jhotdraw.draw.AttributeKeys.FONT_SIZE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.Font;
import org.jhotdraw.draw.figure.Figure;
import org.junit.Test;

public class AttributeKeysFontTest {

    private Figure figureWith(Font face, boolean bold, boolean italic, Double size) {
        Figure f = mock(Figure.class);
        when(f.get(FONT_FACE)).thenReturn(face);
        when(f.get(FONT_BOLD)).thenReturn(bold);
        when(f.get(FONT_ITALIC)).thenReturn(italic);
        when(f.get(FONT_SIZE)).thenReturn(size);
        return f;
    }

    // ----- best case -----------------------------------------------------

    @Test
    public void getFont_plainFace_returnsFontWithRequestedSize() {
        Font face = new Font(Font.SANS_SERIF, Font.PLAIN, 10);
        Figure f = figureWith(face, false, false, 14d);

        Font result = AttributeKeys.getFont(f);

        assertEquals(Font.SANS_SERIF, result.getFamily());
        assertEquals(Font.PLAIN, result.getStyle());
        assertEquals(14f, result.getSize2D(), 0.001f);
    }

    // ----- getFontStyle: one boundary per style bit combination -----------

    @Test
    public void getFontStyle_neitherBoldNorItalic_isPlain() {
        Figure f = figureWith(new Font(Font.SANS_SERIF, Font.PLAIN, 10), false, false, 12d);
        assertEquals(Font.PLAIN, AttributeKeys.getFontStyle(f));
    }

    @Test
    public void getFontStyle_boldOnly() {
        Figure f = figureWith(new Font(Font.SANS_SERIF, Font.PLAIN, 10), true, false, 12d);
        assertEquals(Font.BOLD, AttributeKeys.getFontStyle(f));
    }

    @Test
    public void getFontStyle_italicOnly() {
        Figure f = figureWith(new Font(Font.SANS_SERIF, Font.PLAIN, 10), false, true, 12d);
        assertEquals(Font.ITALIC, AttributeKeys.getFontStyle(f));
    }

    @Test
    public void getFontStyle_boldAndItalic_combinesBothBits() {
        Figure f = figureWith(new Font(Font.SANS_SERIF, Font.PLAIN, 10), true, true, 12d);
        assertEquals(Font.BOLD | Font.ITALIC, AttributeKeys.getFontStyle(f));
    }

    @Test
    public void getFont_boldAndItalic_derivesCombinedStyle() {
        Figure f = figureWith(new Font(Font.SANS_SERIF, Font.PLAIN, 10), true, true, 18d);

        Font result = AttributeKeys.getFont(f);

        assertEquals(Font.BOLD | Font.ITALIC, result.getStyle());
        assertEquals(18f, result.getSize2D(), 0.001f);
    }

    // ----- boundary cases ---------------------------------------------

    @Test
    public void getFont_noFontFaceAttribute_returnsNullInsteadOfThrowing() {
        Figure f = figureWith(null, false, false, 12d);
        assertNull(AttributeKeys.getFont(f));
    }

    @Test
    public void getFont_zeroSize_isAcceptedAsLowerBoundary() {
        Figure f = figureWith(new Font(Font.SANS_SERIF, Font.PLAIN, 10), false, false, 0d);
        assertEquals(0f, AttributeKeys.getFont(f).getSize2D(), 0.001f);
    }

    @Test(expected = AssertionError.class)
    public void getFont_nullFigure_violatesInvariant() {
        // Passing a null figure is a programming error, never a legitimate
        // input - AttributeKeys.getFont() enforces this with a Java
        // assertion (see AttributeKeys.java), which must be enabled for
        // this test to pass (Maven Surefire runs tests with assertions
        // enabled by default).
        AttributeKeys.getFont(null);
    }

    @Test(expected = AssertionError.class)
    public void getFont_negativeFontSize_violatesInvariant() {
        // A negative FONT_SIZE should never happen - the UI (JavaNumberFormatter
        // in FontToolBar) already constrains it to be >= 0. If it ever does,
        // that is a bug elsewhere, which the assertion in getFont() flags
        // immediately instead of silently producing a nonsensical Font.
        Figure f = figureWith(new Font(Font.SANS_SERIF, Font.PLAIN, 10), false, false, -1d);
        AttributeKeys.getFont(f);
    }
}
