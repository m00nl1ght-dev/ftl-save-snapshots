package net.blerf.ftl.parser.save;

public class StartingCrewState {

    private String name = "Frank";
    private SavedGameParser.CrewType race = SavedGameParser.CrewType.HUMAN;


    public StartingCrewState() {
    }

    public void setName( String s ) { name = s; }
    public String getName() { return name; }

    public void setRace( SavedGameParser.CrewType race ) { this.race = race; }
    public SavedGameParser.CrewType getRace() { return race; }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append( String.format( "Name: %s\n", name ) );
        result.append( String.format( "Race: %s\n", race.getId() ) );
        return result.toString();
    }
}
