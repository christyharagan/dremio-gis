# Dremio GIS

Provides GIS UDFs for Dremio. Is a direct port of [https://github.com/k255/drill-gis](https://github.com/k255/drill-gis)

## Build and install

```sh
mvn package

cp target/dremio-gis-0.0.3-SNAPSHOT.jar [DREMIO-INSTALL-PATH]/jars/3rdparty
```

This project depends on two libraries that Dremio doesn't come with. These dependency jars will also need to be copied to the ```/jars/3rdparty``` location. Specifically, they're the ```com.esri.geometry:esri-geometry-api``` and ```org.osgeo:proj4j``` libraries. Where these are downloaded will be system dependent. For example, on a Mac, when run by a user, they will be copied into the ```~/.m2/``` directory.

Then restart Dremio

## Usage

To see example usage, look at the [tests](./tests/TESTS.MD)
