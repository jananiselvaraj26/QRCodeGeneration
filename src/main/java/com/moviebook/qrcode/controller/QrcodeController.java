package com.moviebook.qrcode.controller;

import com.amazonaws.AmazonServiceException;
import com.moviebook.qrcode.model.QRCodeGenerateParams;
import com.moviebook.qrcode.service.QRCodegenerator;
import com.moviebook.qrcode.service.S3BucketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/storage/")
public class QrcodeController {
    private static final Logger LOG = LoggerFactory.getLogger(QrcodeController.class);

   @Autowired
   S3BucketService s3BucketService;


   @Autowired
    QRCodegenerator qrCodegenerator;
    @PostMapping("/uploadFile")
    public String uploadFile(@RequestPart(value = "file") MultipartFile file) {
        return "";//this.s3BucketService.save(file);
    }

    @PostMapping("/generateCode")
    public ResponseEntity<QRCodeGenerateParams> generateQRCode(@RequestBody QRCodeGenerateParams request){
        QRCodeGenerateParams qrCodeGenerateParams=new QRCodeGenerateParams();
        qrCodeGenerateParams.setRequestId(request.getRequestId());
        try{
            String qrCodeImage= qrCodegenerator.generateQRcodeString(qrCodeGenerateParams.getRequestId());
            if(qrCodeImage!=null){
                try {
                    qrCodeGenerateParams.setRequestUrl(this.s3BucketService.save(qrCodeImage,qrCodeGenerateParams.getRequestId()));
                    return new ResponseEntity<QRCodeGenerateParams>(qrCodeGenerateParams, HttpStatus.OK);
                }
                catch(AmazonServiceException e){
                    LOG.error("Error in QRCode upload to s3 bucket");
                    return new ResponseEntity<QRCodeGenerateParams>((QRCodeGenerateParams) null,HttpStatus.NO_CONTENT);
                }

            }
            else{
                LOG.error("Error in QRCode generation");
                return new ResponseEntity<QRCodeGenerateParams>((QRCodeGenerateParams) null,HttpStatus.NO_CONTENT);
            }

        }
        catch(Exception e){
            LOG.error("Error in QRCode generation");
            return new ResponseEntity<QRCodeGenerateParams>((QRCodeGenerateParams) null,HttpStatus.INTERNAL_SERVER_ERROR);
        }




    }





//    @DeleteMapping("/deleteFile")
//    public String deleteFile(@RequestPart(value = "url") String fileUrl) {
//        return this.qrCodeService.(fileUrl);
//    }
}
