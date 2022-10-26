package com.moviebook.qrcode.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QRCodeGenerateParams {
    @Setter
    @Getter
    private String requestId;
    private String requestUrl;
}
