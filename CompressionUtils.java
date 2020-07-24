package compress;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class CompressionUtils {

    private static final Logger LOG = Logger.getLogger(CompressionUtils.class.getName());

    public static void alternative(String inputString) {
        try {
            // Encode a String into bytes
            byte[] input = inputString.getBytes("UTF-8");

            // Compress the bytes
            byte[] output = new byte[100];
            Deflater compresser = new Deflater();
            compresser.setInput(input);
            compresser.finish();
            int compressedDataLength = compresser.deflate(output);
            compresser.end();
            String compressedInputString = new String(output, 0, compressedDataLength, "UTF-8");
            System.out.println("compressedInputString " + compressedInputString);


            // Decompress the bytes
            Inflater decompresser = new Inflater();
            decompresser.setInput(output, 0, compressedDataLength);
            byte[] result = new byte[100];
            int resultLength = decompresser.inflate(result);
            decompresser.end();

            // Decode the bytes into a String
            String outputString = new String(result, 0, resultLength, "UTF-8");
            System.out.println("decompressedOutputString " + outputString);

        } catch (java.io.UnsupportedEncodingException ex) {
            // handle
        } catch (java.util.zip.DataFormatException ex) {
            // handle
        }
    }

    public static byte[] compress(byte[] data) throws IOException {

        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        deflater.finish();
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer); // returns the generated code... index
            outputStream.write(buffer, 0, count);
        }
        outputStream.close();

        byte[] output = outputStream.toByteArray();
        LOG.info("Original Length: " + data.length / 1024 + " Kb " + "  Original String: " + new String(data, StandardCharsets.UTF_8)
                + "\n" + Base64.getEncoder().encodeToString(data));
        LOG.info("Compressed Length: " + output.length / 1024 + " Kb " + " Compressed String: " + new String(output, StandardCharsets.UTF_8)
                + "\n" + Base64.getEncoder().encodeToString(output));
        LOG.info("\n\n\n\n\n\n");
        return output;

    }

    public static byte[] decompress(byte[] data) throws IOException, DataFormatException {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!inflater.finished()) {
            int count = inflater.inflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        outputStream.close();
        byte[] output = outputStream.toByteArray();
        LOG.info("Original: " + data.length + " Original (Compressed) String value: " + new String(data, StandardCharsets.UTF_8)
                + "\n" + Base64.getEncoder().encodeToString(data));
        LOG.info("Decompressed: " + output.length + " Decompressed String value: " + new String(output, StandardCharsets.UTF_8)
                + "\n" + Base64.getEncoder().encodeToString(output));
        LOG.info("\n\n\n\n\n\n");
        return output;

    }


    public static void main(String[] args) throws IOException, DataFormatException {
        byte[] output = CompressionUtils.compress("aaaaaaaaaaaaaaabbbbbbbbbbbbbcccccccccccacacacacacacacacacacacaooooooooooplmkuytgbcd90-1zx709".getBytes());
        CompressionUtils.decompress(output);

        output = CompressionUtils.compress("111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111".getBytes());
        CompressionUtils.decompress(output);

        CompressionUtils.alternative("aaaaaaaaaaaaaaabbbbbbbbbbbbbcccccccccccacacacacacacacacacacacaooooooooooplmkuytgbcd90-1zx709");
        CompressionUtils.alternative("111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");


    }

}
