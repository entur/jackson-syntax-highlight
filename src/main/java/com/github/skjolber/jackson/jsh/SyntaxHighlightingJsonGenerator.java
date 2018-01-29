package com.github.skjolber.jackson.jsh;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Stack;

import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.util.JsonGeneratorDelegate;

/**
 * Syntax highlighting {@linkplain JsonGenerator} wrapper.  
 * 
 */

public class SyntaxHighlightingJsonGenerator extends JsonGeneratorDelegate implements SyntaxHighlighter {

	/*
	 * Implementation note: The indent and value is triggered in the same
	 * method call, so the indenter must be rigged so that they write the 
	 * correct color after the whitespace. 
	 * 
	 */
	
	protected Stack<SyntaxHighlighter> stack = new Stack<>();
	protected SyntaxHighlightingPrettyPrinter prettyPrinter;
	protected SyntaxHighlighterObjectIndenter objectIndenter = new SyntaxHighlighterObjectIndenter(this);
	protected SyntaxHighlighterArrayIndenter arrayIndenter = new SyntaxHighlighterArrayIndenter(this);
	
	protected SyntaxHighlighterResolver resolver;

	public SyntaxHighlightingJsonGenerator(JsonGenerator d) {
		this(d, new DefaultSyntaxHighlighter());
	}

	public SyntaxHighlightingJsonGenerator(JsonGenerator d, SyntaxHighlighter syntaxHighlighter) {
		this(d, new DefaultSyntaxHighlighterResolver(syntaxHighlighter));
	}

	public SyntaxHighlightingJsonGenerator(JsonGenerator d, SyntaxHighlighterResolver resolver) {
		super(d, false);

		this.resolver = resolver;
		this.prettyPrinter = new SyntaxHighlightingPrettyPrinter(this, objectIndenter, arrayIndenter);
		setPrettyPrinter(prettyPrinter);

		// resolve default instance now
		pushSyntaxHighlighter(resolver.forFieldName(null, d.getOutputContext()));
	}

	protected SyntaxHighlighter getSyntaxHighlighter() {
		if(!stack.isEmpty()) {
			return stack.peek();
		}
		return null;
	}

	protected void pushSyntaxHighlighter(SyntaxHighlighter syntaxHighlighter) {
		if(syntaxHighlighter == null) {
			throw new IllegalArgumentException();
		}
		stack.push(syntaxHighlighter);
	}

	@Override
	public void writeFieldName(String name) throws IOException {
		pushSyntaxHighlighter(resolver.forFieldName(name, delegate.getOutputContext()));

		objectIndenter.setValueColor(forFieldName());

		super.writeFieldName(name);

		objectIndenter.clearValueColor();
	}

	@Override
	public void writeFieldName(SerializableString name) throws IOException {
		pushSyntaxHighlighter(resolver.forFieldName(name.getValue(), delegate.getOutputContext()));

		objectIndenter.setValueColor(forFieldName());

		super.writeFieldName(name);

		objectIndenter.clearValueColor();
	}

	@Override
	public void writeFieldId(long id) throws IOException {
		pushSyntaxHighlighter(resolver.forFieldName(Long.toString(id), delegate.getOutputContext()));

		objectIndenter.setValueColor(forFieldName());

		super.writeFieldId(id);

		objectIndenter.clearValueColor();
	}

	@Override
	public void writeArray(int[] array, int offset, int length) throws IOException {
		arrayIndenter.setValueColor(forNumber());
		
		super.writeArray(array, offset, length);
		
		arrayIndenter.clearValueColor();

	}

	@Override
	public void writeArray(long[] array, int offset, int length) throws IOException {
		arrayIndenter.setValueColor(forNumber());
		
		super.writeArray(array, offset, length);
		
		arrayIndenter.clearValueColor();
	}

	@Override
	public void writeArray(double[] array, int offset, int length) throws IOException {
		arrayIndenter.setValueColor(forNumber());
		
		super.writeArray(array, offset, length);
		
		arrayIndenter.clearValueColor();
	}

	@Override
	public void writeBinary(Base64Variant b64variant, byte[] data, int offset, int len) throws IOException {
		prettyPrinter.setValueColor(forBinary());
		super.writeBinary(b64variant, data, offset, len);
	}

	@Override
	public int writeBinary(Base64Variant b64variant, InputStream data, int dataLength) throws IOException {
		prettyPrinter.setValueColor(forBinary());
		return super.writeBinary(b64variant, data, dataLength);

	}

	@Override
	public void writeNumber(short v) throws IOException {
		prettyPrinter.setValueColor(forNumber());
		super.writeNumber(v);
	}

	@Override
	public void writeNumber(int v) throws IOException {
		prettyPrinter.setValueColor(forNumber());
		super.writeNumber(v);
	}

	@Override
	public void writeNumber(long v) throws IOException {
		prettyPrinter.setValueColor(forNumber());
		super.writeNumber(v);
	}

	@Override
	public void writeNumber(BigInteger v) throws IOException {
		prettyPrinter.setValueColor(forNumber());
		super.writeNumber(v);
	}

	@Override
	public void writeNumber(double v) throws IOException, UnsupportedOperationException {
		prettyPrinter.setValueColor(forNumber());
		super.writeNumber(v);
	}

	@Override
	public void writeNumber(float v) throws IOException {
		prettyPrinter.setValueColor(forNumber());
		super.writeNumber(v);
	}

	@Override
	public void writeNumber(BigDecimal v) throws IOException {
		prettyPrinter.setValueColor(forNumber());
		super.writeNumber(v);
	}

	@Override
	public void writeNumber(String encodedValue) throws IOException, UnsupportedOperationException {
		prettyPrinter.setValueColor(forNumber());
		super.writeNumber(encodedValue);

	}

	@Override
	public void writeBoolean(boolean state) throws IOException {
		prettyPrinter.setValueColor(forBoolean());
		super.writeBoolean(state);
	}

	@Override
	public void writeNull() throws IOException {
		prettyPrinter.setValueColor(forNull());
		super.writeNull();
	}

	@Override
	public void writeString(String text) throws IOException {
		prettyPrinter.setValueColor(forString());
		super.writeString(text);
	}

	@Override
	public void writeString(Reader reader, int len) throws IOException {
		prettyPrinter.setValueColor(forString());
		delegate.writeString(reader, len);
	}

	@Override
	public void writeString(char[] text, int offset, int len) throws IOException {
		prettyPrinter.setValueColor(forString());
		delegate.writeString(text, offset, len);
	}

	@Override
	public void writeString(SerializableString text) throws IOException {
		prettyPrinter.setValueColor(forString());
		delegate.writeString(text);
	}

	@Override
	public void writeUTF8String(byte[] text, int offset, int length) throws IOException {
		prettyPrinter.setValueColor(forString());
		delegate.writeUTF8String(text, offset, length);
	}

	protected void popSyntaxHighlighter() {
		stack.pop();
	}
	
	protected SyntaxHighlighter popPreviousSyntaxHighlighter() {
		return stack.remove(stack.size() - 1 - 1);
	}

	@Override
	public String forCurlyBrackets() {
		return getColorOrReset(getSyntaxHighlighter().forCurlyBrackets());
	}

	@Override
	public String forSquareBrackets() {
		return getColorOrReset(getSyntaxHighlighter().forSquareBrackets());
	}

	@Override
	public String forColon() {
		return getColorOrReset(getSyntaxHighlighter().forColon());
	}

	@Override
	public String forComma() {
		return getColorOrReset(getSyntaxHighlighter().forComma());
	}

	@Override
	public String forWhitespace() {
		return getColorOrReset(getSyntaxHighlighter().forWhitespace());
	}

	@Override
	public String forFieldName() {
		return getColorOrReset(getSyntaxHighlighter().forFieldName());
	}

	@Override
	public String forNumber() {
		return getColorOrReset(getSyntaxHighlighter().forNumber());
	}

	@Override
	public String forString() {
		return getColorOrReset(getSyntaxHighlighter().forString());
	}

	@Override
	public String forBinary() {
		return getColorOrReset(getSyntaxHighlighter().forBinary());
	}

	@Override
	public String forBoolean() {
		return getColorOrReset(getSyntaxHighlighter().forBoolean());
	}

	@Override
	public String forNull() {
		return getColorOrReset(getSyntaxHighlighter().forNull());
	}

	protected static String getColorOrReset(String value) {
		if(value != null) {
			return value;
		}
		return Hightlight.SANE;
	}

}
