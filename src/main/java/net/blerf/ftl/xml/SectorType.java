package net.blerf.ftl.xml;

import javax.xml.bind.annotation.*;
import java.util.List;


@XmlRootElement(name = "sectorType")
@XmlAccessorType(XmlAccessType.FIELD)
public class SectorType {

	@XmlAttribute(name = "name")
	private String id;

	@XmlElement(name = "sector")
	public List<String> sectorIds;

	public void setId( String id ) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setSectorIds( List<String> sectorIds ) {
		this.sectorIds = sectorIds;
	}

	public List<String> getSectorIds() {
		return sectorIds;
	}
}
