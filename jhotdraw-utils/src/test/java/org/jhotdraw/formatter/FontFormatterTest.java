/*
 * @(#)FontFormatterTest.java
 *
 * JUnit 4 tests for FontFormatter, the domain logic that converts between a
 * java.awt.Font and its textual representation for the font-face field in
 * the font palette toolbar.
 *
 * FontFormatter is self-contained (it needs no collaborators), so these
 * tests exercise it directly with no mocks - each test drives a single
 * code path through stringToValue()/valueToString().
 */
package org.jhotdraw.formatter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.Font;
import java.text.ParseException;
import org.junit.Test;

public class FontFormatterTest {

    // ----- best case -----------------------------------------------------

    @Test
    public void stringToValue_genericFamilyName_returnsMappedFont() throws ParseException {
        FontFormatter formatter = new FontFormatter();

        Object value = formatter.stringToValue("serif");

        assertTrue(value instanceof Font);
        assertEquals("Serif", ((Font) value).getFamily());
        assertEquals(Font.PLAIN, ((Font) value).getStyle());
    }

    @Test
    public void stringToValue_genericFamilyName_isCaseInsensitiveAndTrimmed() throws ParseException {
        FontFormatter formatter = new FontFormatter();

        Object value = formatter.stringToValue("  Sans-Serif  ");

        assertTrue(value instanceof Font);
        assertEquals("SansSerif", ((Font) value).getFamily());
    }

    @Test
    public void stringToValue_knownLogicalFontName_isAccepted() throws ParseException {
        // "Dialog" is one of the JDK's built-in logical fonts, guaranteed to
        // be resolvable by Font.decode() on every platform/headless setup.
        // (Font.decode()'s result reports getFamily()=="Dialog"; getFontName()
        // itself can come back as "Dialog.plain" depending on the JDK's font
        // peer, so the family - which is what FontFormatter's own acceptance
        // check falls back to - is the stable thing to assert on.)
        FontFormatter formatter = new FontFormatter();

        Object value = formatter.stringToValue("Dialog");

        assertTrue(value instanceof Font);
        assertEquals("Dialog", ((Font) value).getFamily());
    }

    @Test
    public void valueToString_roundTripsFontName() throws ParseException {
        FontFormatter formatter = new FontFormatter();
        Font font = new Font("Dialog", Font.PLAIN, 12);

        assertEquals(font.getFontName(), formatter.valueToString(font));
    }

    // ----- boundary cases: null / empty / whitespace ----------------------

    @Test
    public void stringToValue_null_allowedByDefault_returnsNull() throws ParseException {
        FontFormatter formatter = new FontFormatter(); // allowsNullValue defaults to true

        assertNull(formatter.stringToValue(null));
    }

    @Test
    public void stringToValue_whitespaceOnly_treatedSameAsNull() throws ParseException {
        FontFormatter formatter = new FontFormatter();

        assertNull(formatter.stringToValue("   "));
    }

    @Test(expected = ParseException.class)
    public void stringToValue_null_rejectedWhenNullNotAllowed() throws ParseException {
        FontFormatter formatter = new FontFormatter(false);

        formatter.stringToValue(null);
    }

    @Test
    public void valueToString_null_allowedByDefault_returnsEmptyString() throws ParseException {
        FontFormatter formatter = new FontFormatter();

        assertEquals("", formatter.valueToString(null));
    }

    @Test(expected = ParseException.class)
    public void valueToString_null_rejectedWhenNullNotAllowed() throws ParseException {
        FontFormatter formatter = new FontFormatter(false);

        formatter.valueToString(null);
    }

    // ----- boundary cases: unknown / invalid font names --------------------

    @Test(expected = ParseException.class)
    public void stringToValue_unknownFontName_rejectedByDefault() throws ParseException {
        // allowsUnknownFont is false by default: Font.decode() falls back to
        // a default logical font for an unrecognised name instead of
        // returning null, so FontFormatter must detect the mismatch itself
        // and reject it.
        FontFormatter formatter = new FontFormatter();

        formatter.stringToValue("NoSuchFontXYZ123");
    }

    @Test
    public void stringToValue_unknownFontName_acceptedWhenAllowed() throws ParseException {
        FontFormatter formatter = new FontFormatter();
        formatter.setAllowsUnknownFont(true);

        Object value = formatter.stringToValue("NoSuchFontXYZ123");

        assertTrue(value instanceof Font);
    }

    // ----- boundary case: wrong value type to valueToString ----------------

    @Test(expected = ParseException.class)
    public void valueToString_nonFontValue_isRejected() throws ParseException {
        FontFormatter formatter = new FontFormatter();

        formatter.valueToString("not a font");
    }

    // ----- boundary case: custom generic-family map ------------------------

    @Test(expected = NullPointerException.class)
    public void stringToValue_clearedGenericFamilies_isAPreExistingDefect() throws ParseException {
        // This is a characterization test, not a desired behavior: it
        // documents a real bug found while writing boundary-case tests.
        // clearGenericFontFamilies() sets the backing map to null instead of
        // an empty map, but stringToValue() unconditionally calls
        // genericFontFamilies.get(...) - so any lookup after clearing throws
        // NullPointerException instead of gracefully falling back to
        // Font.decode()/a ParseException. Out of scope for the font-palette
        // refactor, but recorded here so a fix doesn't silently regress
        // whichever behavior gets chosen.
        FontFormatter formatter = new FontFormatter();
        formatter.clearGenericFontFamilies();

        formatter.stringToValue("serif");
    }
}
