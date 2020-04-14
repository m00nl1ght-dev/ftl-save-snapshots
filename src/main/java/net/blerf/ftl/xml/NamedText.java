package net.blerf.ftl.xml;

import javax.xml.bind.annotation.*;


/**
 * One of the "text" tags in lookup files.
 */
@XmlRootElement( name = "text" )
@XmlAccessorType( XmlAccessType.FIELD )
public class NamedText {

	@XmlAttribute( name = "name" )
	private String id;

	@XmlValue
	private String text;


	public void setId( String id ) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setText( String s ) {
		text = s;
	}

	public String getText() {
		return text;
	}
}
