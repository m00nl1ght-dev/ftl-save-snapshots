package net.blerf.ftl.xml;

import javax.xml.bind.annotation.*;
import java.util.List;


@XmlRootElement( name = "imageList" )
@XmlAccessorType( XmlAccessType.FIELD )
public class BackgroundImageList {

	@XmlAttribute( name = "name" )
	private String id;

	@XmlElement( name = "img" )
	private List<BackgroundImage> images;


	public void setImages( List<BackgroundImage> images ) {
		this.images = images;
	}

	public List<BackgroundImage> getImages() {
		return images;
	}

	public void setId( String id ) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return ""+id;
	}
}
