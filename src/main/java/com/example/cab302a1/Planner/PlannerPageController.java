package com.example.cab302a1.Planner;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.WeekFields;
import java.util.*;

import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.*;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.validate.ValidationResult;
import net.fortuna.ical4j.validate.ValidationException;
import net.fortuna.ical4j.model.property.Uid;


public class PlannerPageController {
    @FXML private Button prevButton;
    @FXML private Button todayButton;
    @FXML private Button nextButton;
    @FXML private Label monthTitle;
    @FXML private GridPane calendarGrid;

    @FXML private ListView<String> eventsList;

    private YearMonth displayed = YearMonth.now();
    private final Locale locale = Locale.getDefault();
    private final DayOfWeek firstDayOfWeek = WeekFields.of(locale).getFirstDayOfWeek();
    private LocalDate selectedDate = null;

    @FXML
    public void initialize() {
        updateCalendar();
        updateEvents();

        addEvent();

        prevButton.setOnAction(e -> { displayed = displayed.minusMonths(1); updateCalendar(); });
        nextButton.setOnAction(e -> { displayed = displayed.plusMonths(1); updateCalendar(); });
        todayButton.setOnAction(e -> { displayed = YearMonth.now(); updateCalendar(); });
    }

    private void updateCalendar() {
        calendarGrid.getChildren().clear();
        monthTitle.setText(displayed.getMonth().getDisplayName(TextStyle.FULL, locale) + " " + displayed.getYear());

        // Weekday headers
        DayOfWeek dow = firstDayOfWeek;
        for (int i = 0; i < 7; i++) {
            Label lbl = new Label(dow.getDisplayName(TextStyle.SHORT_STANDALONE, locale));
            lbl.getStyleClass().add("weekday-header");
            calendarGrid.add(lbl, i, 0);
            dow = dow.plus(1);
        }

        // First visible date
        LocalDate firstOfMonth = displayed.atDay(1);
        int shift = ((firstOfMonth.getDayOfWeek().getValue() - firstDayOfWeek.getValue()) + 7) % 7;
        LocalDate gridStart = firstOfMonth.minusDays(shift);

        LocalDate today = LocalDate.now();
        for (int i = 0; i < 42; i++) {
            LocalDate date = gridStart.plusDays(i);
            StackPane cell = createDateCell(date, today);
            int col = i % 7;
            int row = (i / 7) + 1;
            calendarGrid.add(cell, col, row);
        }
    }

    private StackPane createDateCell(LocalDate date, LocalDate today) {
        Label lbl = new Label(String.valueOf(date.getDayOfMonth()));
        StackPane cell = new StackPane(lbl);
        cell.getStyleClass().add("date-cell");

        cell.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); // allows cells to expand

        GridPane.setFillWidth(cell, true);
        GridPane.setFillHeight(cell, true);

        if (date.getMonth() != displayed.getMonth()) {
            cell.getStyleClass().add("other-month");
        }
        if (date.equals(today)) {
            cell.getStyleClass().add("today");
        }
        if (date.equals(selectedDate)) {
            cell.getStyleClass().add("selected");
        }

        cell.setOnMouseClicked(e -> {
            selectedDate = date;
            updateCalendar();
            System.out.println("Selected: " + date);
        });

        return cell;
    }


    private void updateEvents(){
        eventsList.getItems().add("No events happening today");
    }

    private void addEvent(){//String event){
        DateTimeFormatter icalFormat = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'");

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime start = ZonedDateTime.now().withHour(10).withMinute(0).withSecond(0);
        ZonedDateTime end = start.plusHours(1);

        // Build ICS content
        StringBuilder sb = new StringBuilder();
        sb.append("BEGIN:VCALENDAR\n");
        sb.append("VERSION:2.0\n");
        sb.append("PRODID:-//My Simple App//EN\n");
        sb.append("CALSCALE:GREGORIAN\n");
        sb.append("BEGIN:VEVENT\n");
        sb.append("UID:").append(UUID.randomUUID()).append("@example.com\n");
        sb.append("DTSTAMP:").append(now.withZoneSameInstant(java.time.ZoneOffset.UTC).format(icalFormat)).append("\n");
        sb.append("DTSTART:").append(start.withZoneSameInstant(java.time.ZoneOffset.UTC).format(icalFormat)).append("\n");
        sb.append("DTEND:").append(end.withZoneSameInstant(java.time.ZoneOffset.UTC).format(icalFormat)).append("\n");
        sb.append("SUMMARY:Team Meeting\n");
        sb.append("DESCRIPTION:Discuss project status\n");
        sb.append("LOCATION:Meeting Room 1\n");
        sb.append("END:VEVENT\n");
        sb.append("END:VCALENDAR\n");

        // Write to file
        try (FileWriter fw = new FileWriter("Events/simple_event.ics")) {
            fw.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("ICS file created");


        //placeholder
        //eventsList.getItems().add(event);
    }
}
