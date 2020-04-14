package dev.m00nl1ght.ftl.autosaves;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

public class Config {

    private static final Logger LOGGER = LogManager.getLogger();

    private final File file;

    public String SAVES_DIR;
    public String DATA_DIR;
    public String SNAPSHOT_MODE;

    public Config(File file) {
        this.file = file;
    }

    public void load() {
        if (file.exists())
        try (final BufferedReader reader = new BufferedReader(new FileReader(file))) {
            SAVES_DIR = reader.readLine(); if (SAVES_DIR.isEmpty()) SAVES_DIR = null;
            DATA_DIR = reader.readLine(); if (DATA_DIR.isEmpty()) DATA_DIR = null;
            SNAPSHOT_MODE = reader.readLine(); if (SNAPSHOT_MODE.isEmpty()) SNAPSHOT_MODE = null;
        } catch (IOException e) {
            LOGGER.error("Failed to load config", e);
            FTLSaveSnapshots.onError("Failed to read config: " + e.getMessage(), false);
        }
    }

    public void save() {
        if (file.exists()) file.delete();
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            if (SAVES_DIR != null) writer.write(SAVES_DIR); writer.newLine();
            if (DATA_DIR != null) writer.write(DATA_DIR); writer.newLine();
            if (SNAPSHOT_MODE != null) writer.write(SNAPSHOT_MODE); writer.newLine();
            writer.flush();
        } catch (IOException e) {
            LOGGER.error("Failed to save config", e);
            FTLSaveSnapshots.onError("Failed to save config: " + e.getMessage(), false);
        }
    }

}
