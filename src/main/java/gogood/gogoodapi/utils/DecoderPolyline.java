package gogood.gogoodapi.utils;

import java.util.ArrayList;
import java.util.List;

public class DecoderPolyline {
    public static List<double[]> decodePolyline(String encoded) {
        List<double[]> polyline = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int[] resultLat = decodeChunk(encoded, index);
            lat += resultLat[0];
            index = resultLat[1];

            int[] resultLng = decodeChunk(encoded, index);
            lng += resultLng[0];
            index = resultLng[1];

            double latitude = lat * 1e-5;
            double longitude = lng * 1e-5;
            polyline.add(new double[]{latitude, longitude});
        }

        return polyline;
    }

    private static int[] decodeChunk(String encoded, int index) {
        int result = 0, shift = 0;
        int b;
        do {
            b = encoded.charAt(index++) - 63;
            result |= (b & 0x1f) << shift;
            shift += 5;
        } while (b >= 0x20);

        int dcoord = (result & 1) != 0 ? ~(result >> 1) : (result >> 1);
        return new int[]{dcoord, index};
    }
}
