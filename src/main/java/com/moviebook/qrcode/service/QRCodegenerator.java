package com.moviebook.qrcode.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Service
public class QRCodegenerator {

    private static final Logger LOG=LoggerFactory.getLogger(QRCodegenerator.class);

    public static byte[] getQRCodeImage(String text, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageConfig con = new MatrixToImageConfig( 0xFF000002 , 0xFFFFC041 ) ;

        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream,con);
        byte[] pngData = pngOutputStream.toByteArray();
        return pngData;
    }


    public String generateQRcodeString(String s){
        String medium="https://rahul26021999.medium.com/";
//        String github="https://github.com/rahul26021999";

        byte[] image = new byte[0];
        try {

            // Generate and Return Qr Code in Byte Array
            image = getQRCodeImage(medium,250,250);
            String qrcode = Base64.getEncoder().encodeToString(image);
            return qrcode;

            // Generate and Save Qr Code Image in static/image folder
//            QRCodeGenerator.generateQRCodeImage(github,250,250,QR_CODE_IMAGE_PATH);

        } catch (WriterException | IOException e) {
            LOG.error("error in QR Code generation");
            e.printStackTrace();
            return null;
        }
        // Convert Byte Array into Base64 Encode String



    }
}
