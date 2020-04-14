package dev.m00nl1ght.ftl.autosaves;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import net.blerf.ftl.parser.DataManager;
import net.blerf.ftl.parser.DefaultDataManager;
import net.blerf.ftl.parser.save.SavedGameParser;
import net.blerf.ftl.parser.save.SavedGameState;
import net.vhati.modmanager.core.FTLUtilities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FTLSaveSnapshots extends Application {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String DEFAULT_SAVE_DIR = "My Games/FasterThanLight/";
    private static final File OPTIONS_FILE = new File(".", "options.dat");

    private final SavedGameParser parser = new SavedGameParser();
    private final Map<Integer, SavedGame> savedGames = new HashMap<>();

    private final Config config = new Config(new File("config.txt"));
    private final File tempFile = new File("temp-buffer");
    private File savesDir, dataDir, currentSav, snapshotDir, importDir;
    private volatile SnapshotMode snapshotMode = SnapshotMode.BEACON;
    private boolean isLegacy = false;
    private long lastWatchEvent = -1L;

    private TableView<SavedGame> gameTable;
    private TableView<SavedGameState> snapshotTable;
    private Button snapButton, loadButton, delButton;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("FTL Savegame Snapshots");

        config.load();
        snapshotMode = config.SNAPSHOT_MODE == null ? SnapshotMode.BEACON : SnapshotMode.valueOf(config.SNAPSHOT_MODE);
        Runtime.getRuntime().addShutdownHook(new Thread(this::onExit));

        dataDir = config.DATA_DIR == null ? null : new File(config.DATA_DIR);
        if (dataDir == null || !FTLUtilities.isDatsDirValid(dataDir)) {
            LOGGER.info("Attempting to locate FTL data directory automatically...");
            dataDir = FTLUtilities.findDatsDir();
        }

        if (dataDir == null || !FTLUtilities.isDatsDirValid(dataDir)) {
            LOGGER.error( "The FTL data directory is invalid: " + dataDir);
            final DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Select your FTL installation directory");
            dataDir = directoryChooser.showDialog(primaryStage);
            if (!FTLUtilities.isDatsDirValid(dataDir)) onError("Invalid data directory", true);
        }

        try {
            LOGGER.info("Loading FTL data from: " + dataDir);
            DefaultDataManager dataManager = new DefaultDataManager(dataDir);
            DataManager.setInstance(dataManager);
            dataManager.setDLCEnabledByDefault(true);
        } catch ( Exception e ) {
            LOGGER.error("Error parsing FTL resources", e);
        }

        savesDir = config.SAVES_DIR == null ? null : new File(config.SAVES_DIR);
        if (savesDir == null || !loadFromSavesDir()) {
            LOGGER.info("Attempting to locate FTL saves directory automatically...");
            savesDir = FTLUtilities.findUserDataDir();
        }

        if (savesDir == null || !loadFromSavesDir()) {
            LOGGER.error( "The FTL saves directory is invalid: " + savesDir);
            final DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Select your FTL saves directory");
            savesDir = directoryChooser.showDialog(primaryStage);
            if (!loadFromSavesDir()) onError("Invalid save directory", true);
        }

        LOGGER.info("Watching saves directory: " + savesDir);
        new FileWatcher(currentSav, this::onSaveFileChanged).start();

        gameTable = new TableView<>();
        gameTable.setPrefSize(500, 800);
        gameTable.setPlaceholder(new Label("No savegames loaded."));
        gameTable.getSelectionModel().selectedItemProperty().addListener(this::changeGameSel);
        final TableColumn<SavedGame, String> gameShipColumn = new TableColumn<>("Ship");
        final TableColumn<SavedGame, String> gameNameColumn = new TableColumn<>("Name");
        gameShipColumn.setCellValueFactory(FXTableHelper::gameShipValue);
        gameNameColumn.setCellValueFactory(FXTableHelper::gameNameValue);
        gameTable.getColumns().add(gameShipColumn);
        gameTable.getColumns().add(gameNameColumn);
        gameTable.getItems().addAll(savedGames.values());

        snapshotTable = new TableView<>();
        snapshotTable.setPrefSize(1000, 800);
        snapshotTable.setPlaceholder(new Label("No savegame selected."));
        snapshotTable.getSelectionModel().selectedItemProperty().addListener(this::changeSaveSel);
        final TableColumn<SavedGameState, Number> saveTimestampColumn = new TableColumn<>("Timestamp");
        final TableColumn<SavedGameState, Number> saveSectorColumn = new TableColumn<>("Sector");
        final TableColumn<SavedGameState, Number> saveBeaconColumn = new TableColumn<>("Beacon");
        final TableColumn<SavedGameState, Number> saveHullColumn = new TableColumn<>("Hull");
        final TableColumn<SavedGameState, Number> saveScrapColumn = new TableColumn<>("Scrap");
        final TableColumn<SavedGameState, Number> saveFuelColumn = new TableColumn<>("Fuel");
        saveTimestampColumn.setCellValueFactory(FXTableHelper::saveTimestampValue);
        saveTimestampColumn.setCellFactory(FXTableHelper.SaveTimestampCell::new);
        saveSectorColumn.setCellValueFactory(FXTableHelper::saveSectorValue);
        saveBeaconColumn.setCellValueFactory(FXTableHelper::saveBeaconValue);
        saveHullColumn.setCellValueFactory(FXTableHelper::saveHullValue);
        saveScrapColumn.setCellValueFactory(FXTableHelper::saveScrapValue);
        saveFuelColumn.setCellValueFactory(FXTableHelper::saveFuelValue);
        snapshotTable.getColumns().add(saveTimestampColumn);
        snapshotTable.getColumns().add(saveSectorColumn);
        snapshotTable.getColumns().add(saveBeaconColumn);
        snapshotTable.getColumns().add(saveHullColumn);
        snapshotTable.getColumns().add(saveScrapColumn);
        snapshotTable.getColumns().add(saveFuelColumn);
        saveTimestampColumn.setPrefWidth(150);
        saveSectorColumn.setPrefWidth(75);
        saveBeaconColumn.setPrefWidth(75);
        saveHullColumn.setPrefWidth(75);
        saveScrapColumn.setPrefWidth(75);
        saveFuelColumn.setPrefWidth(75);

        final ComboBox<SnapshotMode> modeSelect = new ComboBox<>();
        modeSelect.getItems().addAll(SnapshotMode.values());
        modeSelect.getSelectionModel().select(snapshotMode);
        modeSelect.getSelectionModel().selectedItemProperty().addListener(this::changeModeSel);
        modeSelect.setPrefWidth(500);

        snapButton = new Button("Take Snapshot Now");
        snapButton.setOnAction(this::onManualSnapshot);
        snapButton.setPrefWidth(500);
        snapButton.setDisable(currentSav == null || !currentSav.exists());

        loadButton = new Button("Load Snapshot");
        loadButton.setOnAction(this::onLoadSnapshot);
        loadButton.setPrefWidth(300);
        loadButton.setDisable(true);

        delButton = new Button("Delete Snapshot");
        delButton.setOnAction(this::onDeleteSnapshot);
        delButton.setPrefWidth(300);
        delButton.setDisable(true);

        final HBox pane = new HBox(10);
        final VBox rowA = new VBox(10);
        final VBox rowB = new VBox(10);
        final HBox ctrl = new HBox(10);
        pane.getChildren().addAll(rowA, rowB);
        rowA.getChildren().addAll(gameTable, modeSelect, snapButton);
        rowB.getChildren().addAll(snapshotTable, ctrl);
        ctrl.getChildren().addAll(loadButton, delButton);
        pane.setPadding(new Insets(10, 10, 10, 10));
        rowA.setPrefSize(500, 800);
        rowB.setPrefSize(1000, 800);

        final Scene scene = new Scene(pane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void onDeleteSnapshot(ActionEvent actionEvent) {
        final SavedGame game = gameTable.getSelectionModel().getSelectedItem();
        final SavedGameState sel = snapshotTable.getSelectionModel().getSelectedItem();
        if (sel == null || game == null) return;
        if (!game.getSnapshots().remove(sel)) return;
        final File file = new File(snapshotDir, sel.getSectorTreeSeed() + "/" + sel.getTimestamp());
        if (file.exists()) file.delete();
        updateSnapshotList();
        if (game.getSnapshots().isEmpty()) {
            savedGames.remove(game.getSeed());
            updateGameList();
        }
    }

    private void onLoadSnapshot(ActionEvent actionEvent) {
        final SavedGame game = gameTable.getSelectionModel().getSelectedItem();
        final SavedGameState sel = snapshotTable.getSelectionModel().getSelectedItem();
        if (sel == null || game == null) return;
        final File file = new File(snapshotDir, sel.getSectorTreeSeed() + "/" + sel.getTimestamp());
        if (file.exists()) {
            Alert msg = new Alert(Alert.AlertType.CONFIRMATION, "If the game is currently running, please return to the main menu before loading a snapshot.");
            msg.setTitle("FTL Savegame Snapshots");
            msg.showAndWait();
            if (msg.getResult() == ButtonType.OK) {
                if (!currentSav.exists() || currentSav.renameTo(currentSav)) {
                    try {
                        Files.copy(file.toPath(), currentSav.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        onError("Failed to load snapshot: " + e.getMessage(), false);
                    }
                } else {
                    onError("Failed to load snapshot: Savefile is open in FTL (please return to main menu and try again)", false);
                }
            }
        } else {
            onError("Snapshot file not found: " + file.getName(), false);
        }
    }

    private void onManualSnapshot(ActionEvent actionEvent) {
        if (currentSav.exists()) takeSnapshot(currentSav, System.currentTimeMillis(), SnapshotMode.ALWAYS);
    }

    private void changeGameSel(ObservableValue<? extends SavedGame> obs, SavedGame old, SavedGame val) {
        if (snapshotTable != null) {
            snapshotTable.getItems().clear();
            if (val != null) snapshotTable.getItems().addAll(val.getSnapshots());
        }
    }

    private void changeSaveSel(ObservableValue<? extends SavedGameState> obs, SavedGameState old, SavedGameState val) {
        if (val == null) {
            delButton.setDisable(true);
            loadButton.setDisable(true);
        } else {
            delButton.setDisable(false);
            loadButton.setDisable(false);
        }
    }

    @SuppressWarnings("TypeParameterExtendsFinalClass")
    private void changeModeSel(ObservableValue<? extends SnapshotMode> obs, SnapshotMode old, SnapshotMode val) {
        snapshotMode = val;
    }

    private void addSaveToList(SavedGameState save) {
        final SavedGame savedGame = getOrCreateSavedGameFor(save);
        savedGame.addSnapshot(save);
        updateSnapshotList();
    }

    private SavedGame getOrCreateSavedGameFor(SavedGameState save) {
        SavedGame sg = savedGames.get(save.getSectorTreeSeed());
        if (sg != null) return sg; else sg = new SavedGame(save);
        savedGames.put(save.getSectorTreeSeed(), sg);
        updateGameList();
        return sg;
    }

    private void updateGameList() {
        if (gameTable != null) {
            gameTable.getItems().clear();
            gameTable.getItems().addAll(savedGames.values());
        }
    }

    private void updateSnapshotList() {
        if (snapshotTable != null) {
            final SavedGame sel = gameTable.getSelectionModel().getSelectedItem();
            snapshotTable.getItems().clear();
            if (sel != null) snapshotTable.getItems().addAll(sel.getSnapshots());
        }
    }

    private boolean takeSnapshot(File file, long timestamp) {
        return takeSnapshot(file, timestamp, snapshotMode);
    }

    private boolean takeSnapshot(File file, long timestamp, SnapshotMode mode) {
        if (mode == SnapshotMode.DISABLED) return false;
        try {
            Files.copy(file.toPath(), tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            final SavedGameState loaded = parser.readSavedGame(tempFile);
            final int sectorId = loaded.getSectorNumber();
            final int beaconId = loaded.getCurrentBeaconId();
            final int seed = loaded.getSectorTreeSeed();
            if (mode == SnapshotMode.SECTOR && getOrCreateSavedGameFor(loaded).hasSnapshot(sectorId)) return false;
            if (mode == SnapshotMode.BEACON && getOrCreateSavedGameFor(loaded).hasSnapshot(sectorId, beaconId)) return false;
            LOGGER.info("Taking snapshot of new save state: " + file.getName());
            final File seedDir = new File(savesDir, "snapshots/" + seed);
            File targetFile = new File(seedDir, String.valueOf(timestamp));
            while (targetFile.exists()) targetFile = new File(seedDir, String.valueOf(timestamp++));
            seedDir.mkdirs();
            Files.copy(tempFile.toPath(), targetFile.toPath());
            loaded.setTimestamp(timestamp);
            addSaveToList(loaded);
            return true;
        } catch (IOException e) {
            LOGGER.error("Failed to read save <" + file.getName() + "> ", e);
            onError("Failed to read save <" + file.getName() + "> " + e, false);
            return false;
        }
    }

    private void loadSaveFromFile(File file) {
        try {
            LOGGER.info("Reading existing snapshot: " + file.getName());
            final long timestamp = Long.parseLong(file.getName());
            final SavedGameState loaded = parser.readSavedGame(file);
            loaded.setTimestamp(timestamp);
            addSaveToList(loaded);
        } catch (IOException e) {
            LOGGER.error("Failed to read save <" + file.getName() + "> ", e);
            onError("Failed to read save <" + file.getName() + "> " + e, false);
        }
    }

    private boolean loadFromSavesDir() {
        if (savesDir == null || !savesDir.isDirectory()) return false;
        final File profOld = new File(savesDir, "prof.sav");
        final File profAE = new File(savesDir, "ae_prof.sav");
        currentSav = new File(savesDir, "continue.sav");
        snapshotDir = new File(savesDir, "snapshots");
        importDir = new File(savesDir, "snapshots/import");
        if (!profAE.isFile()) {
            if (profOld.isFile()) {
                isLegacy = true;
            } else {
                return false;
            }
        }

        if (snapshotDir.isDirectory()) {
            for (File dir : Objects.requireNonNull(snapshotDir.listFiles())) {
                if (dir.isDirectory() && !dir.equals(importDir)) {
                    final File[] files = Objects.requireNonNull(dir.listFiles());
                    if (files.length == 0) dir.delete();
                    else for (File sav : files) loadSaveFromFile(sav);
                }
            }
        } else {
            snapshotDir.mkdirs();
        }

        if (importDir.isDirectory()) {
            for (File file : Objects.requireNonNull(importDir.listFiles())) {
                if (file.isDirectory()) {
                    final File[] files = Objects.requireNonNull(file.listFiles());
                    if (files.length == 0) file.delete();
                    else for (File sav : files) {
                        if (sav.isFile() && sav.getName().endsWith(".sav")) {
                            final long timestamp = sav.lastModified();
                            takeSnapshot(sav, timestamp > 0L ? timestamp : System.currentTimeMillis(), SnapshotMode.BEACON);
                            sav.delete();
                        }
                    }
                } else if (file.isFile() && file.getName().endsWith(".sav")) {
                    final long timestamp = file.lastModified();
                    takeSnapshot(file, timestamp > 0L ? timestamp : System.currentTimeMillis(), SnapshotMode.BEACON);
                    file.delete();
                }
            }
        } else {
            importDir.mkdirs();
        }

        if (currentSav.isFile()) {
            final long timestamp = currentSav.lastModified();
            takeSnapshot(currentSav, timestamp > 0L ? timestamp : System.currentTimeMillis());
        }

        return true;
    }

    @SuppressWarnings("BusyWait")
    private void onSaveFileChanged() {
        if (System.currentTimeMillis() - lastWatchEvent < 500) return;

        if (currentSav.exists()) {
            while (!currentSav.renameTo(currentSav)) try {Thread.sleep(200);} catch (InterruptedException ignored) {}
            Platform.runLater(this::onSaveFileChangedBuffered);
        } else {
            Platform.runLater(() -> snapButton.setDisable(true));
        }

        lastWatchEvent = System.currentTimeMillis();
    }

    private void onSaveFileChangedBuffered() {
        LOGGER.info("The current save file changed, considering snapshot");
        snapButton.setDisable(false);
        takeSnapshot(currentSav, System.currentTimeMillis());
    }

    static void onError(String str, boolean fatal) {
        Alert msg = new Alert(Alert.AlertType.ERROR, str, ButtonType.CLOSE);
        msg.setTitle("FTL Savegame Snapshots");
        msg.showAndWait();
        if (fatal) System.exit(0);
    }

    private void onExit() {
        tempFile.delete();
        config.SAVES_DIR = savesDir.getAbsolutePath();
        config.DATA_DIR = dataDir.getAbsolutePath();
        config.SNAPSHOT_MODE = snapshotMode.name();
        config.save();
    }

    enum SnapshotMode {
        DISABLED("Auto-snapshot disabled"),
        SECTOR("Auto-snapshot when entering new sector"),
        BEACON("Auto-snapshot when arriving at new beacon"),
        ALWAYS("Auto-snapshot whenever the game saves (experimental)");

        private final String text;
        SnapshotMode(String text) {this.text = text;}

        @Override
        public String toString() {
            return text;
        }
    }

}
