package com.danyazero.cs1;

import com.danyazero.cs1.model.BatchGenerator;
import com.danyazero.cs1.utils.ArrayNormalizer;
import com.danyazero.cs1.utils.CombinedTausworthe;
import com.danyazero.cs1.utils.HistogramUtility;
import com.danyazero.cs1.utils.LinearGenerator;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.function.BiConsumer;

public class HelloApplication extends Application {
    private static final int BIN_COUNT = 10;
    private static final int BATCH_SIZE = 100;

    private final HashMap<String, BatchGenerator> generators = new HashMap<>();
    private BarChart<String, Number> barChart;

    @Override
    public void start(Stage stage) {

        var xAxis = new CategoryAxis();
        var yAxis = new NumberAxis();

        xAxis.setLabel("Bin");
        yAxis.setLabel("Frequency");

        barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Random Sequence Histogram");
        barChart.setAnimated(false);


        var strategyGroup = new ToggleGroup();

        var firstStrategy = new RadioButton("Linear");
        firstStrategy.setToggleGroup(strategyGroup);
        firstStrategy.setSelected(true);
        var linearGenerator = new LinearGenerator(5_153);
        generators.put("Linear", linearGenerator);


        var secondStrategy = new RadioButton("Tausworthe");
        secondStrategy.setToggleGroup(strategyGroup);
        var tauswortheSeeds = linearGenerator.generate(3);
        generators.put("Tausworthe", new CombinedTausworthe(tauswortheSeeds[0], tauswortheSeeds[1], tauswortheSeeds[2]));

        var sizeInput = new TextField();
        sizeInput.setPromptText("Enter sequence size...");
        sizeInput.setText("10000");
        var sizeInputLabel = new Label("Sequence Size:");
        var sizeInputBox = new HBox(sizeInputLabel, sizeInput);

        var binsCountInput = new TextField();
        binsCountInput.setPromptText("Enter number of bins...");
        binsCountInput.setText("20");
        var binsCountInputLabel = new Label("Bins Count:");
        var binsCountInputBox = new HBox(binsCountInputLabel, binsCountInput);

        var batchSizeInput = new TextField();
        batchSizeInput.setPromptText("Enter batch size...");
        batchSizeInput.setText("100");
        var batchSizeInputLabel = new Label("Batch Size:");
        var batchSizeInputBox = new HBox(batchSizeInputLabel, batchSizeInput);

        var generateButton = new Button("Generate");
        generateButton.setOnAction(event -> {
            var selected = (RadioButton) strategyGroup.getSelectedToggle();
            var selectedGenerator = generators.get(selected.getText());
            if (selectedGenerator != null) {

                selectedGenerator.setBatchSize(Integer.parseInt(batchSizeInput.getText()));
                var size = Integer.parseInt(sizeInput.getText());
                var bins = Integer.parseInt(binsCountInput.getText());
                new Thread(() -> runGenerator(selectedGenerator, size, bins)).start();
            }
        });
        var inputBox = new VBox(sizeInputBox, batchSizeInputBox, binsCountInputBox);

        var controls = new HBox(new VBox(firstStrategy, secondStrategy), inputBox, generateButton);

        var root = new VBox(controls, barChart);

        var scene = new Scene(root, 800, 600);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();


    }

    public void runGenerator(BatchGenerator selectedGenerator, int size, int binSize) {
        BiConsumer<int[], BatchGenerator> batchCallback = (array, generator) -> {
            double[] normalizedArray = ArrayNormalizer.apply(array, generator);
            int[] histogram = HistogramUtility.build(normalizedArray, binSize);

            Platform.runLater(() -> {
                updateHistogram(histogram);
            });
        };
        selectedGenerator.setBatchCallback(batchCallback);

        selectedGenerator.generate(size);
    }

    private void updateHistogram(int[] histogram) {
        barChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Distribution");

        for (int i = 0; i < histogram.length; i++) {
            String binLabel = String.format("%.1f–%.1f", (double) i / histogram.length, (double) (i + 1) / histogram.length);
            series.getData().add(new XYChart.Data<>(binLabel, histogram[i]));
        }

        barChart.getData().add(series);
    }

}
