/*
 * Copyright (c) 2023 AccelByte Inc. All Rights Reserved
 * This program is made available under the terms of the MIT License.
 */

package net.accelbyte.gdpr.sdk.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

@Slf4j
public class HttpHelper {

    public static boolean uploadFile(String uploadUrl, byte[] data) throws IOException {
        HttpClient httpClient = HttpClients.custom().build();
        HttpPut put = new HttpPut(uploadUrl);
        HttpEntity entity = EntityBuilder.create()
                .setBinary(data)
                .build();
        put.setEntity(entity);
        put.setHeader("Content-Type","application/zip");

        HttpResponse response = httpClient.execute(put);
        if (response.getStatusLine().getStatusCode() == 200) {
            log.debug("Success upload file to uploadUrl [{}]", uploadUrl);
            return true;
        } else {
            log.debug("Failed upload file to uploadUrl [{}]. Response code [{}]", uploadUrl, response.getStatusLine().getStatusCode());
            return false;
        }
    }
}
