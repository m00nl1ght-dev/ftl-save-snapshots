package dev.m00nl1ght.ftl.autosaves;

import net.blerf.ftl.parser.save.SavedGameState;

import java.util.ArrayList;
import java.util.List;

public class SavedGame {

    private final List<SavedGameState> snapshots = new ArrayList<>();
    private final int seed;
    private final String ship;
    private final String name;

    public SavedGame(SavedGameState save) {
        this.seed = save.getSectorTreeSeed();
        this.name = save.getPlayerShipName();
        this.ship = save.getPlayerShipBlueprintId();
    }

    public boolean hasSnapshot(int sectorId) {
        return snapshots.stream().anyMatch(s -> s.getSectorNumber() == sectorId);
    }

    public boolean hasSnapshot(int sectorId, int beaconId) {
        return snapshots.stream().anyMatch(s -> s.getSectorNumber() == sectorId && s.getCurrentBeaconId() == beaconId);
    }

    public void addSnapshot(SavedGameState save) {
        if (save.getSectorTreeSeed() != seed) throw new IllegalArgumentException("seed mismatch");
        if (snapshots.contains(save)) return;
        snapshots.add(save);
    }

    public List<SavedGameState> getSnapshots() {
        return snapshots;
    }

    public String getName() {
        return name;
    }

    public String getShip() {
        return ship;
    }

    public int getSeed() {
        return seed;
    }

}
