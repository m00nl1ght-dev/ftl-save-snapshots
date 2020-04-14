package net.blerf.ftl.parser;

import java.io.*;
import java.util.regex.Pattern;


public class Parser {

	public static final Pattern PATTERN = Pattern.compile("(^|\n)(.+)");

	private byte[] intbuf = new byte[4];

	/**
	 * Reads a little-endian int from a stream, as a boolean.
	 */
	protected boolean readBool( InputStream in ) throws IOException {
		int i = readInt( in );

		if( !(i==1 || i==0 || i==-1) ) { // m00nl1ght: added -1 as valid input, because sometimes that happens?!
			throw new IOException( "Not a bool: "+ i );
		}

		return i == 1;
	}

	/**
	 * Writes a boolean to a stream as a little-endian int.
	 */
	protected void writeBool( OutputStream out, boolean b ) throws IOException {
		writeInt( out, (b ? 1 : 0) );
	}

	/**
	 * Reads a little-endian int from a stream.
	 */
	protected int readInt( InputStream in ) throws IOException {
		int numRead = 0;
		int offset = 0;
		while ( offset < intbuf.length && (numRead = in.read( intbuf, offset, intbuf.length )) >= 0 ) {
			offset += numRead;
		}
		if ( offset < intbuf.length ) {
			throw new IOException( "End of stream reached before reading enough bytes for an int" );
		}

		int v = 0;
		for ( int i = 0; i < intbuf.length; i++ ) {
			v |= (((int)intbuf[i]) & 0xff) << (i*8);
		}

		return v;
	}

	/**
	 * Writes a little-endian int to a stream.
	 */
	protected void writeInt( OutputStream out, int value ) throws IOException {
		for ( int i = 0; i < intbuf.length; i++ ) {
			intbuf[i] = (byte)(value >> (i*8));
		}

		out.write( intbuf );
	}

	/**
	 * Reads a little-endian int length + ascii string from a stream.
	 */
	protected String readString( InputStream in ) throws IOException {
		int length = readInt( in );

		// Avoid allocating a rediculous array size.
		// But InputStreams don't universally track position/size.
		// And available() might only mean blocking, not the end.
		// So try some special cases...
		if ( in instanceof FileInputStream ) {
			FileInputStream fin = (FileInputStream)in;
			long position = fin.getChannel().position();
			if ( position + length  > fin.getChannel().size() ) {
				throw new IOException( String.format( "Expected string length (%d) would extend beyond the end of the stream, from current position (%d)", length, position ) );
			}
		}
		else {
			// Call available on streams that really end.
			int remaining = -1;
			if ( in instanceof ByteArrayInputStream ) {
				remaining = ((ByteArrayInputStream)in).available();
			}
			if ( remaining != -1 && length > remaining ) {
				throw new IOException( String.format( "Expected string length (%d) would extend beyond the end of the stream", length ) );
			}
		}

		int numRead = 0;
		int offset = 0;
		byte[] strBytes = new byte[length];
		while ( offset < strBytes.length && (numRead = in.read(strBytes, offset, strBytes.length)) >= 0 )
			offset += numRead;

		if ( offset < strBytes.length ) {
			throw new IOException( String.format( "End of stream reached before reading enough bytes for string of length %d", length ) );
		}

		return new String( strBytes, "US-ASCII" );
	}

	protected void writeString( OutputStream out, String str ) throws IOException {
		writeInt( out, str.length() );
		out.write( str.getBytes( "US-ASCII" ) );
	}
}
