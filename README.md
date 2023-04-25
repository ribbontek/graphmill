# Graphmill

An experimental & simple charting library that creates 2d charts using Kotlin without any 3rd party OpenSource Java libraries like JFreeChart

## Chart functionality available:

- BarChart2d
- LineChart2d
- PieChart2d
- ScatterChart2d (WIP)
- NetworkChart2d (Not Started)

## Examples:

![Bar Chart](examples/barchart.jpg)
![Line Chart](examples/linechart.jpg)
![Pie Chart](examples/piechart.jpg)
![Scatter Chart](examples/scatterchart.jpg)

## Build the Project

```shell
./gradlew clean build
```

## Format the Project with KtLint

```shell
./gradlew ktlintFormat
```

## Version the Project with Axion

Tagging a new release (ensure to have appropriate permissions in GitHub)

```shell
./gradlew release -Prelease.disableChecks
```

Use the `-Prelease.scope` flag to indicate incremental level, i.e. `-Prelease.scope=incrementMajor` for a major release

Test it out locally with `-Prelease.dryRun`

Versioning starts from `1.0.0` by default