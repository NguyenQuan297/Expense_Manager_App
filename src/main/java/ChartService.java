import javafx.collections.ObservableList;
import javafx.scene.chart.*;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ChartService {

    // ============================
    // 1. PIE CHART
    // ============================
    public PieChart createPieChart(ObservableList<Expense> expenses) {
        PieChart pieChart = new PieChart();
        pieChart.setTitle("Expenses by Category");
        pieChart.setLabelsVisible(true);
        pieChart.setClockwise(true);
        pieChart.setLegendVisible(true);

        Map<String, Double> totals = expenses.stream()
                .collect(Collectors.groupingBy(
                        Expense::getCategory,
                        Collectors.summingDouble(Expense::getDisplayAmount)
                ));

        totals.forEach((cat, amount) ->
                pieChart.getData().add(new PieChart.Data(cat, amount))
        );

        return pieChart;
    }


    // ============================
    // 2. STACKED BAR CHART (MONTHLY)
    // ============================
    public StackedBarChart<String, Number> createStackedBarChart(ObservableList<Expense> expenses) {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Month");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Total Amount");

        StackedBarChart<String, Number> chart = new StackedBarChart<>(xAxis, yAxis);
        chart.setTitle("Monthly Expenses");

        Map<String, Map<String, Double>> groupedData =
                expenses.stream().collect(Collectors.groupingBy(
                        Expense::getCategory,
                        Collectors.groupingBy(
                                e -> e.getDate().substring(0, 7), // YYYY-MM
                                Collectors.summingDouble(Expense::getDisplayAmount)
                        )));

        groupedData.forEach((category, monthData) -> {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(category);

            monthData.forEach((month, amount) ->
                    series.getData().add(new XYChart.Data<>(month, amount))
            );

            chart.getData().add(series);
        });

        return chart;
    }


    // ============================
    // 3. BAR CHART (CATEGORY TOTALS)
    // ============================
    public BarChart<String, Number> createBarChart(ObservableList<Expense> expenses) {

        Map<String, Double> totals = expenses.stream()
                .collect(Collectors.groupingBy(
                        Expense::getCategory,
                        Collectors.summingDouble(Expense::getDisplayAmount)
                ));

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Category");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Amount");

        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle("Total Spending by Category");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Totals");

        totals.forEach((category, sum) ->
                series.getData().add(new XYChart.Data<>(category, sum))
        );

        chart.getData().add(series);
        return chart;
    }


    // ============================
    // 4. LINE CHART (TIME TREND)
    // ============================
    public LineChart<String, Number> createLineChart(ObservableList<Expense> expenses) {

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Date");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Amount");

        LineChart<String, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle("Expense Trend Over Time");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Daily Spending");

        // Sort theo ngày để chart đẹp hơn
        expenses.stream()
                .sorted((a, b) -> a.getDate().compareTo(b.getDate()))
                .forEach(e -> series.getData().add(new XYChart.Data<>(e.getDate(), e.getAmount())));

        chart.getData().add(series);
        return chart;
    }


    // ============================
    // 5. AREA CHART (VARIATION)
    // ============================
    public AreaChart<String, Number> createAreaChart(ObservableList<Expense> expenses) {

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Date");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Amount");

        AreaChart<String, Number> chart = new AreaChart<>(xAxis, yAxis);
        chart.setTitle("Expense Area Distribution");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Daily Spending");

        expenses.stream()
                .sorted((a, b) -> a.getDate().compareTo(b.getDate()))
                .forEach(e -> series.getData().add(new XYChart.Data<>(e.getDate(), e.getAmount())));

        chart.getData().add(series);
        return chart;
    }


    // ============================
    // 6. SCATTER CHART
    // ============================
    public ScatterChart<String, Number> createScatterChart(ObservableList<Expense> expenses) {

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Date");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Amount");

        ScatterChart<String, Number> chart = new ScatterChart<>(xAxis, yAxis);
        chart.setTitle("Expense Scatter Plot");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Expenses");

        expenses.forEach(e ->
                series.getData().add(new XYChart.Data<>(e.getDate(), e.getAmount()))
        );

        chart.getData().add(series);
        return chart;
    }


    // ============================
    // 7. DONUT CHART
    // ============================
    public StackPane createDonutChart(ObservableList<Expense> expenses) {
        PieChart pie = createPieChart(expenses);

        Circle hole = new Circle(60, Color.WHITE);
        hole.setStroke(Color.WHITE);

        StackPane pane = new StackPane();
        pane.getChildren().addAll(pie, hole);

        return pane;
    }
}
