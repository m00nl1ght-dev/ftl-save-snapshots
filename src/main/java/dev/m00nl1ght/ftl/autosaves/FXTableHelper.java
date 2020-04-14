package dev.m00nl1ght.ftl.autosaves;

import com.sun.javafx.binding.IntegerConstant;
import com.sun.javafx.binding.LongConstant;
import com.sun.javafx.binding.StringConstant;
import javafx.beans.value.ObservableIntegerValue;
import javafx.beans.value.ObservableLongValue;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import net.blerf.ftl.parser.save.SavedGameState;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FXTableHelper {

    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("dd.MM.YY HH:mm:ss");

    private FXTableHelper() {}

    public static ObservableValue<String> gameShipValue(CellDataFeatures<SavedGame, String> cellDataFeatures) {
        return StringConstant.valueOf(cellDataFeatures.getValue().getShip());
    }

    public static ObservableValue<String> gameNameValue(CellDataFeatures<SavedGame, String> cellDataFeatures) {
        return StringConstant.valueOf(cellDataFeatures.getValue().getName());
    }

    public static ObservableLongValue saveTimestampValue(CellDataFeatures<SavedGameState, Number> cellDataFeatures) {
        return LongConstant.valueOf(cellDataFeatures.getValue().getTimestamp());
    }

    public static ObservableIntegerValue saveSectorValue(CellDataFeatures<SavedGameState, Number> cellDataFeatures) {
        return IntegerConstant.valueOf(cellDataFeatures.getValue().getSectorNumber() + 1);
    }

    public static ObservableIntegerValue saveBeaconValue(CellDataFeatures<SavedGameState, Number> cellDataFeatures) {
        return IntegerConstant.valueOf(cellDataFeatures.getValue().getCurrentBeaconId());
    }

    public static ObservableIntegerValue saveHullValue(CellDataFeatures<SavedGameState, Number> cellDataFeatures) {
        return IntegerConstant.valueOf(cellDataFeatures.getValue().getPlayerShip().getHullAmt());
    }

    public static ObservableIntegerValue saveScrapValue(CellDataFeatures<SavedGameState, Number> cellDataFeatures) {
        return IntegerConstant.valueOf(cellDataFeatures.getValue().getPlayerShip().getScrapAmt());
    }

    public static ObservableIntegerValue saveFuelValue(CellDataFeatures<SavedGameState, Number> cellDataFeatures) {
        return IntegerConstant.valueOf(cellDataFeatures.getValue().getPlayerShip().getFuelAmt());
    }

    public static class SaveTimestampCell extends TableCell<SavedGameState, Number> {

        public SaveTimestampCell(TableColumn<SavedGameState, Number> column) {
            super();
        }

        @Override
        protected void updateItem(Number item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
            } else {
                setText(timeFormat.format(new Date(item.longValue())));
            }
        }

    }

}
