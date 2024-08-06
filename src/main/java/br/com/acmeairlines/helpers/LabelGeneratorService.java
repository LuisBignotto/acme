package br.com.acmeairlines.helpers;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

@Service
public class LabelGeneratorService {

    public byte[] generateLabel(Long id, Long userId, String tag, String color, BigDecimal weight, Long flightId) throws IOException, WriterException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        BufferedImage qrCodeImage = generateQRCodeImage(tag);

        PDImageXObject pdImage = PDImageXObject.createFromByteArray(document, bufferedImageToByteArray(qrCodeImage), "QR Code");

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            // header
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 24);
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 770);
            contentStream.showText("ACME AIRLINES");
            contentStream.endText();

            // linha divisora
            contentStream.setLineWidth(1f);
            contentStream.moveTo(50, 760);
            contentStream.lineTo(500, 760);
            contentStream.stroke();

            // textos
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.beginText();
            contentStream.setLeading(20f);
            contentStream.newLineAtOffset(50, 740);
            contentStream.showText("User ID: " + userId);
            contentStream.newLine();
            contentStream.showText("Tag: " + tag);
            contentStream.newLine();
            contentStream.showText("Color: " + color);
            contentStream.newLine();
            contentStream.showText("Weight: " + weight + " kg");
            contentStream.newLine();
            contentStream.showText("Flight ID: " + flightId);
            contentStream.endText();

            // qr code
            contentStream.drawImage(pdImage, 400, 650, 100, 100);

            // linha divisora
            contentStream.moveTo(50, 40);
            contentStream.lineTo(500, 40);
            contentStream.stroke();

            // footer
            contentStream.setFont(PDType1Font.HELVETICA_OBLIQUE, 10);
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 30);
            contentStream.showText("Obrigado por escolher a ACME Airlines. Tenha uma bela viagem!");
            contentStream.endText();
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.save(outputStream);
        document.close();

        return outputStream.toByteArray();
    }

    private BufferedImage generateQRCodeImage(String barcodeText) throws WriterException {
        QRCodeWriter barcodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, 200, 200, hints);

        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    private byte[] bufferedImageToByteArray(BufferedImage image) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", baos);
        return baos.toByteArray();
    }
}