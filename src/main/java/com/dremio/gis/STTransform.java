/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dremio.gis;

import javax.inject.Inject;

import org.osgeo.proj4j.CRSFactory;
import org.osgeo.proj4j.CoordinateTransform;

import com.dremio.exec.expr.SimpleFunction;
import com.dremio.exec.expr.annotations.FunctionTemplate;
import com.dremio.exec.expr.annotations.Output;
import com.dremio.exec.expr.annotations.Param;
import com.dremio.exec.expr.annotations.Workspace;

@FunctionTemplate(name = "st_transform", scope = FunctionTemplate.FunctionScope.SIMPLE,
  nulls = FunctionTemplate.NullHandling.NULL_IF_NULL)
public class STTransform implements SimpleFunction {
  @Param
  org.apache.arrow.vector.holders.VarBinaryHolder geom1Param;

  @Param
  org.apache.arrow.vector.holders.NullableIntHolder sridSrcParam;

  @Param
  org.apache.arrow.vector.holders.NullableIntHolder sridTgtParam;

  @Workspace
  CoordinateTransform transform;

  @Workspace
  CRSFactory crsFactory;

  @Workspace
  int sridTgt;

  @Output
  org.apache.arrow.vector.holders.VarBinaryHolder out;

  @Inject
  io.netty.buffer.ArrowBuf buffer;

  public void setup() {
    int sridSrc = sridSrcParam.value;
    sridTgt = sridTgtParam.value;

    org.osgeo.proj4j.CoordinateReferenceSystem srcCrs =
        new org.osgeo.proj4j.CRSFactory().createFromName("EPSG:" + sridSrc);

    org.osgeo.proj4j.CoordinateReferenceSystem tgtCrs =
        new org.osgeo.proj4j.CRSFactory().createFromName("EPSG:" + sridTgt);

    transform = new org.osgeo.proj4j.BasicCoordinateTransform(srcCrs, tgtCrs);
  }

  public void eval() {
    com.esri.core.geometry.ogc.OGCGeometry geomSrc = com.esri.core.geometry.ogc.OGCGeometry
        .fromBinary(geom1Param.buffer.nioBuffer(geom1Param.start, geom1Param.end - geom1Param.start));

    org.osgeo.proj4j.ProjCoordinate result = new org.osgeo.proj4j.ProjCoordinate();
    com.esri.core.geometry.SpatialReference sr = com.esri.core.geometry.SpatialReference.create(sridTgt);
    java.nio.ByteBuffer geomBytes = null;

    if(geomSrc != null && geomSrc.geometryType().equals("Point")){
      com.esri.core.geometry.ogc.OGCPoint pointGeom = (com.esri.core.geometry.ogc.OGCPoint) geomSrc;
      result = transform.transform(new org.osgeo.proj4j.ProjCoordinate(pointGeom.X(), pointGeom.Y()), result);

      geomBytes = new com.esri.core.geometry.ogc.OGCPoint(
          new com.esri.core.geometry.Point(result.x, result.y), sr).asBinary();
    } else {
      com.esri.core.geometry.Geometry esriGeom = geomSrc.getEsriGeometry();
      com.esri.core.geometry.MultiVertexGeometry vertexGeom = 
          com.esri.core.geometry.VertexGeomAccessor.getVertexGeometry(esriGeom);
      for (int i = 0; i < vertexGeom.getPointCount(); i++){
        com.esri.core.geometry.Point point = vertexGeom.getPoint(i);
        result = transform.transform(new org.osgeo.proj4j.ProjCoordinate(point.getX(), point.getY()), result);
        point.setXY(result.x, result.y);
        vertexGeom.setPoint(i, point);
      }

      com.esri.core.geometry.ogc.OGCGeometry tGeom = 
          com.esri.core.geometry.ogc.OGCGeometry.createFromEsriGeometry(esriGeom, sr);
      geomBytes = tGeom.asBinary();
    }

    int outputSize = geomBytes.remaining();
    buffer = out.buffer = buffer.reallocIfNeeded(outputSize);
    out.start = 0;
    out.end = outputSize;
    buffer.setBytes(0, geomBytes);
  }
}
