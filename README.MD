# Dremio GIS

Provides GIS UDFs for Dremio. Is a direct port of [https://github.com/k255/drill-gis](https://github.com/k255/drill-gis)

## Build and install

```sh
mvn package

cp target/dremio-gis-0.0.3-SNAPSHOT.jar [DREMIO-INSTALL-PATH]/jars/3rdparty
```

Then restart Dremio

## Usage

To see example usage, look at the [tests](./tests/TESTS.MD)
