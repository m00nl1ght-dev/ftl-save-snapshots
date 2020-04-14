package net.blerf.ftl.constants;

import net.blerf.ftl.parser.save.SavedGameParser.CrewType;
import net.blerf.ftl.parser.save.SavedGameParser.StationDirection;
import net.blerf.ftl.parser.save.SavedGameParser.SystemType;

import java.util.List;


public interface FTLConstants {

	// ShipState constants.

	int getMaxReservePoolCapacity();


	// SystemState constants.

	/**
	 * Returns the bonus system bars produced by a Battery system.
	 *
	 * @param batterySystemCapacity the capacity of the system itself (its level)
	 */
	int getBatteryPoolCapacity( int batterySystemCapacity );

	int getMaxIonizedBars();


	// CrewState constants.

	List<CrewType> getCrewTypes();

	int getMasteryIntervalPilot( CrewType race );
	int getMasteryIntervalEngine( CrewType race );
	int getMasteryIntervalShield( CrewType race );
	int getMasteryIntervalWeapon( CrewType race );
	int getMasteryIntervalRepair( CrewType race );
	int getMasteryIntervalCombat( CrewType race );


	// System-related constants.

	List<SystemType> getSystemTypes();

	int getDefaultSystemRoomSlotSquare( SystemType systemType );

	StationDirection getDefaultSystemRoomSlotDirection( SystemType systemType );
}
