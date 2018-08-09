package com.dremio.gis;

//import javax.inject.Inject;
//
//import com.dremio.exec.expr.SimpleFunction;
//import com.dremio.exec.expr.annotations.FunctionTemplate;
//import com.dremio.exec.expr.annotations.Output;
//import com.dremio.exec.expr.annotations.Param;

public class UDFs {
//    @FunctionTemplate(name = "st_geomfromtext", scope = FunctionTemplate.FunctionScope.SIMPLE,
//            nulls = FunctionTemplate.NullHandling.NULL_IF_NULL)
//    public static class STGeomFromText implements SimpleFunction {
//        @Param
//        org.apache.arrow.vector.holders.VarCharHolder input;
//
//        @Output
//        org.apache.arrow.vector.holders.VarBinaryHolder out;
//
//        @Inject
//        io.netty.buffer.ArrowBuf buffer;
//
//        public void setup() {
//        }
//
//        public void eval() {
//            String wktText = StringFunctionHelpers.toStringFromUTF8(input.start, input.end,
//                    input.buffer);
//
//            com.esri.core.geometry.ogc.OGCGeometry geom;
//
//            geom = com.esri.core.geometry.ogc.OGCGeometry.fromText(wktText);
//
//            java.nio.ByteBuffer pointBytes = geom.asBinary();
//
//            int outputSize = pointBytes.remaining();
//            buffer = out.buffer = buffer.reallocIfNeeded(outputSize);
//            out.start = 0;
//            out.end = outputSize;
//            buffer.setBytes(0, pointBytes);
//        }
//    }
//
//    @FunctionTemplate(name = "st_astext", scope = FunctionTemplate.FunctionScope.SIMPLE,
//            nulls = FunctionTemplate.NullHandling.NULL_IF_NULL)
//    public static class STAsText implements SimpleFunction {
//        @Param
//        org.apache.arrow.vector.holders.VarBinaryHolder geom1Param;
//
//        @Output
//        org.apache.arrow.vector.holders.VarCharHolder out;
//
//        @Inject
//        io.netty.buffer.ArrowBuf buffer;
//
//        public void setup() {
//        }
//
//        public void eval() {
//            com.esri.core.geometry.ogc.OGCGeometry geom1 = com.esri.core.geometry.ogc.OGCGeometry
//                    .fromBinary(geom1Param.buffer.nioBuffer(geom1Param.start, geom1Param.end - geom1Param.start));
//
//            String geomWKT = geom1.asText();
//
//            int outputSize = geomWKT.getBytes().length;
//            buffer = out.buffer = buffer.reallocIfNeeded(outputSize);
//            out.start = 0;
//            out.end = outputSize;
//            buffer.setBytes(0, geomWKT.getBytes());
//        }
//    }
}
