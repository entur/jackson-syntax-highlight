package org.entur.jackson.jsh;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.entur.jackson.jsh.AnsiSyntaxHighlight;

public class AnsiSyntaxHighlightTest {

	@Test
	public void testBackground() {
		assertTrue(AnsiSyntaxHighlight.isBackground(AnsiSyntaxHighlight.BACKGROUND_CYAN));
	}
}
