package net.blerf.ftl.xml;

import javax.xml.bind.annotation.*;
import java.util.List;


@XmlRootElement(name = "nameList")
@XmlAccessorType(XmlAccessType.FIELD)
public class CrewNameList {

	@XmlAttribute
	private String race;  // FTL ignores race.

	@XmlAttribute
	private String sex;

	@XmlElement(name = "name")
	private List<CrewName> names;

	@XmlAccessorType(XmlAccessType.FIELD)
	public static class CrewName {

		@XmlAttribute(required = false)
		public String shortName;

		@XmlValue
		public String name;
	}

	public void setRace( String race ) {
		this.race = race;
	}

	public String getRace() {
		return race;
	}

	public void setSex( String sex ) {
		this.sex = sex;
	}

	public String getSex() {
		return sex;
	}

	public void setNames( List<CrewName> names ) {
		this.names = names;
	}

	public List<CrewName> getNames() {
		return names;
	}
}
