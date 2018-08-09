# Tests

First load the ```CA-cities.csv``` file into a space called ```GIS Test Data```. Then run each SQL statement and compare the output with the expected output described below.

## Test Geometry From Text Creation

**SQL**

```sql
SELECT ST_AsText(ST_GeomFromText('POINT (-121.895 37.339)')) from "GIS Test Data"."CA-cities" limit 1
```

**Result**

```POINT (-121.895 37.339)```

## Test Geometry Point Creation

**SQL**

```sql
SELECT ST_AsText(ST_Point(-121.895, 37.339)) from "GIS Test Data"."CA-cities" limit 1
```

**Result**

```POINT (-121.895 37.339)```

## Test ST Within Query

**SQL**

```sql
SELECT ST_Within(ST_Point(E, D),
  ST_GeomFromText('POLYGON((-121.95 37.28, -121.94 37.35, -121.84 37.35, -121.84 37.28, -121.95 37.28))')) 
  from "GIS Test Data"."CA-cities" where C = 'San Jose'
```

**Result**

```true```

**SQL**

```sql
SELECT ST_Within(ST_Point(E, D),
  ST_GeomFromText('POLYGON((-121.95 37.28, -121.94 37.35, -121.84 37.35, -121.84 37.28, -121.95 37.28))'))
  from "GIS Test Data"."CA-cities" where C = 'San Francisco'
```

**Result**

```false```

## Test STX Query

**SQL**

```sql
select ST_X(ST_Point(CONVERT_TO_FLOAT(-121.895, 1, 1, 0), 37.339)) from "GIS Test Data"."CA-cities" limit 1
```

**Result**

```-121.895```

## Test STY Query

**SQL**

```sql
select ST_Y(ST_Point(-121.895, CONVERT_TO_FLOAT(37.339, 1, 1, 0))) from "GIS Test Data"."CA-cities" limit 1
```

**Result**

```37.339```

## Test STX & STY Gives NaN For Non-Point Geometry

**SQL**

```sql
select ST_X(ST_GeomFromText('MULTIPOINT((16 64))')) from (VALUES(1))
```

**Result**

```NaN```

**SQL**

```sql
select ST_Y(ST_GeomFromText('MULTIPOINT((16 64))')) from (VALUES(1))
```

**Result**

```NaN```

## Test Intersect Query

**SQL**

```sql
SELECT ST_Intersects(ST_GeomFromText('POINT(0 0)'), ST_GeomFromText('LINESTRING(2 0,0 2)')) from (VALUES(1))
```

**Result**

```false```

**SQL**

```sql
SELECT ST_Intersects(ST_GeomFromText('POINT(0 0)'), ST_GeomFromText('LINESTRING(0 0,0 2)')) from (VALUES(1))
```

**Result**

```true```

## Test Relate Query

**SQL**

```sql
SELECT ST_Relate(ST_GeomFromText('POINT(1 2)'), ST_Buffer(ST_GeomFromText('POINT(1 2)'),2), '0FFFFF212')
  from (VALUES(1))
```

**Result**

```true```

**SQL**

```sql
SELECT ST_Relate(ST_GeomFromText('POINT(1 2)'), ST_Buffer(ST_GeomFromText('POINT(1 2)'),2), '*FF*FF212')
  from (VALUES(1))
```

**Result**

```true```

**SQL**

```sql
SELECT ST_Relate(ST_GeomFromText('POINT(0 0)'), ST_Buffer(ST_GeomFromText('POINT(1 2)'),2), '*FF*FF212')
  from (VALUES(1))
```

**Result**

```false```

## Test Touches Query

**SQL**

```sql
SELECT ST_Touches(ST_GeomFromText('LINESTRING(0 0, 1 1, 0 2)'), ST_GeomFromText('POINT(1 1)')) from (VALUES(1))
```

**Result**

```false```

**SQL**

```sql
SELECT ST_Touches(ST_GeomFromText('LINESTRING(0 0, 1 1, 0 2)'), ST_GeomFromText('POINT(0 2)')) from (VALUES(1))
```

**Result**

```true```

## Test Equals Query

**SQL**

```sql
SELECT ST_Equals(ST_GeomFromText('LINESTRING(0 0, 10 10)'), ST_GeomFromText('LINESTRING(0 0, 5 5, 10 10)'))
  from (VALUES(1))
```

**Result**

```true```

## Test Contains Query

**SQL**

```sql
SELECT ST_Contains(smallc, bigc) As smallcontainsbig, ST_Contains(bigc,smallc) As bigcontainssmall, ST_Contains(bigc, ST_Union(smallc, bigc)) as bigcontainsunion, ST_Equals(bigc, ST_Union(smallc, bigc)) as bigisunion FROM (SELECT ST_Buffer(ST_GeomFromText('POINT(1 2)'), 10) As smallc, ST_Buffer(ST_GeomFromText('POINT(1 2)'), 20) As bigc from (VALUES(1)) ) As foo
```

**Result**

```smallcontainsbig:false, bigcontainssmall:true, bigcontainsunion:true, bigisunion:true```

## Test Overlaps-Crosses-Intersects-Contains Query

**SQL**

```sql
SELECT ST_Overlaps(a,b) As a_overlap_b, ST_Crosses(a,b) As a_crosses_b, ST_Intersects(a, b) As a_intersects_b, ST_Contains(b,a) As b_contains_a
  FROM (SELECT ST_GeomFromText('POINT(1 0.5)') As a, ST_GeomFromText('LINESTRING(1 0, 1 1, 3 5)')  As b 
  FROM (VALUES(1)) ) As foo
```

**Result**

```a_overlap_b:false, a_crosses_b:false, a_intersects_b:true, b_contains_a:true```

## Test Disjoint Query

**SQL**

```sql
SELECT ST_Disjoint(ST_GeomFromText('POINT(0 0)'), ST_GeomFromText('LINESTRING( 2 0, 0 2 )')) from (VALUES(1))
```

**Result**

```true```

**SQL**

```sql
SELECT ST_Disjoint(ST_GeomFromText('POINT(0 0)'), ST_GeomFromText('LINESTRING( 0 0, 0 2 )')) from (VALUES(1))
```

**Result**

```false```

## Test Transfrom Query

**SQL**

```sql
SELECT round(CONVERT_TO_FLOAT(st_x(st_transform(st_geomfromtext('POINT (743238 2967416)'), 2249, 4326)), 1, 1, 0), 13),
  round(CONVERT_TO_FLOAT(st_y(st_transform(st_geomfromtext('POINT (743238 2967416)'), 2249, 4326)), 1, 1, 0), 13) from (VALUES(1))
```

**Result**

```[-71.1776848522251, 42.3902896512902]```
