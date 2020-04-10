
import java.awt.Component;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.swing.JOptionPane;

public class OperationsInBase64 {

    public static void main(String[] args) {
        String messageData = JOptionPane.showInputDialog((Component) null, "Ingresa el valor a convertir");
        String response = decode(messageData);
        System.out.println(response);
    }

    public static String encode(String message) {
        String messageEncode = Base64.getEncoder().encodeToString(message.getBytes(StandardCharsets.UTF_8));
        return messageEncode;
    }

    public static String decode(String message) {
        byte[] messageDecode = Base64.getDecoder().decode(message);
        return new String(messageDecode, StandardCharsets.UTF_8);
    }

    public static String getXmlUnzipString(String xmlString) {
        String location;
        if (xmlString.length() < 50) {
            location = "Base64 incorrecto";
            return location;
        } else {
            location = "C:\\EDDI05_Unzip\\";
            String fileName = "EDDI05.zip";
            String fileType = ".XML";
            String cdfi = "";
            String file = "";
            String dirRoot = System.getProperty("user.home");
            String path = dirRoot + File.separator + fileName;
            File dir = new File(location);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            byte[] decodedBytes = Base64.getDecoder().decode(xmlString);

            try {
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(path), 1024);
                Throwable var12 = null;

                try {
                    out.write(decodedBytes);
                } catch (Throwable var27) {
                    var12 = var27;
                    throw var27;
                } finally {
                    if (out != null) {
                        if (var12 != null) {
                            try {
                                out.close();
                            } catch (Throwable var26) {
                                var12.addSuppressed(var26);
                            }
                        } else {
                            out.close();
                        }
                    }

                }
            } catch (IOException var30) {
                System.out.println(var30.toString());
            }

            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                FileInputStream fis = new FileInputStream(path);
                ZipInputStream zis = new ZipInputStream(fis);
                ZipEntry ze = zis.getNextEntry();
                InputStreamReader isr = new InputStreamReader(zis);
                BufferedReader reader = new BufferedReader(isr);
                String outFileName = location + ze.getName();
                FileWriter fw = new FileWriter(outFileName);
                String line;
                if (ze.getName().endsWith(fileType)) {
                    while ((line = reader.readLine()) != null) {
                        baos.write(line.getBytes());
                    }

                    cdfi = baos.toString();
                    fw.flush();
                    fw.close();
                }

                ZipEntry salida = null;

                label148:
                while (true) {
                    do {
                        if ((salida = zis.getNextEntry()) == null) {
                            zis.closeEntry();
                            reader.close();
                            break label148;
                        }
                    } while (!salida.getName().endsWith(fileType));

                    while ((line = reader.readLine()) != null) {
                        baos.write(line.getBytes());
                    }

                    cdfi = baos.toString();
                    fw.flush();
                    fw.close();
                    file = salida.getName();
                }
            } catch (IOException var28) {
                var28.printStackTrace();
            }

            String responseData = cdfi + " / NameFileXml: " + file;
            return responseData;
        }
    }
}
