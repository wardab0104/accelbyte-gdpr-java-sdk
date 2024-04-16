/*
 * Copyright (c) 2023 AccelByte Inc. All Rights Reserved
 * This program is made available under the terms of the MIT License.
 */

package net.accelbyte.gdpr.sdk;

import com.google.common.base.Strings;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.accelbyte.gdpr.registered.v1.*;
import net.accelbyte.gdpr.sdk.object.DataGenerationResult;
import net.accelbyte.gdpr.sdk.utils.FileHelper;
import net.accelbyte.gdpr.sdk.utils.HttpHelper;

@Slf4j
public class GDPRService extends GDPRGrpc.GDPRImplBase {

    static GDPRHandler handler;

    public static void SetHandler(GDPRHandler handlerParam){
        handler = handlerParam;
    }

    @Override
    public void dataGeneration(DataGenerationRequest request, StreamObserver<DataGenerationResponse> responseObserver) {
        DataGenerationResponse.Builder responseBuilder = DataGenerationResponse.newBuilder();

        if (Strings.isNullOrEmpty(request.getNamespace()) || Strings.isNullOrEmpty(request.getUserId()) || Strings.isNullOrEmpty(request.getUploadUrl())) {
            log.error("[GDPRService.dataGeneration] empty required payload: namespace, userId or uploadUrl.");
            responseBuilder.setSuccess(false).setMessage("required payload is empty");
            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();
            return;
        }

        if (handler == null) {
            responseBuilder.setSuccess(true);
        } else {
            log.info("[GDPRService.dataGeneration] start executing for namespace [{}] userId [{}]", request.getNamespace(), request.getUserId());
            try {
                DataGenerationResult result = handler.ProcessDataGeneration(request.getNamespace(), request.getUserId(), request.getIsPublisherNamespace());
                if (result == null || result.getData().size() == 0) {
                    log.info("[GDPRService.dataGeneration] result is empty for namespace [{}] userId [{}]", request.getNamespace(), request.getUserId());
                    responseBuilder.setSuccess(true);
                } else {
                    byte[] zipFileBytes = FileHelper.CreateZipFile(result.getData(), request.getNamespace(), request.getUserId());
                    if (zipFileBytes == null) {
                        log.info("[GDPRService.dataGeneration] result is empty for namespace [{}] userId [{}]", request.getNamespace(), request.getUserId());
                        responseBuilder.setSuccess(true);
                    } else {
                        boolean successUpload = HttpHelper.uploadFile(request.getUploadUrl(), zipFileBytes);
                        responseBuilder.setSuccess(successUpload);
                    }
                }
            } catch (Exception ex) {
                log.error("[GDPRService.dataGeneration] error: [{}]", ex.getMessage());
                responseBuilder.setSuccess(false).setMessage(ex.getMessage());
            }
        }

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void dataDeletion(DataDeletionRequest request, StreamObserver<DataDeletionResponse> responseObserver){
        DataDeletionResponse.Builder responseBuilder = DataDeletionResponse.newBuilder();

        if (Strings.isNullOrEmpty(request.getNamespace()) || Strings.isNullOrEmpty(request.getUserId())) {
            log.error("[GDPRService.dataDeletion] empty required payload: namespace or userId.");
            responseBuilder.setSuccess(false).setMessage("required payload is empty");
            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();
            return;
        }

        if (handler == null) {
            responseBuilder.setSuccess(true);
        } else {
            log.info("[GDPRService.dataDeletion] start executing for namespace [{}] userId [{}]", request.getNamespace(), request.getUserId());
            try {
                handler.ProcessDataDeletion(request.getNamespace(), request.getUserId(), request.getIsPublisherNamespace());
                responseBuilder.setSuccess(true);
            }catch (Exception ex) {
                log.error("[GDPRService.dataDeletion] error: [{}]", ex.getMessage());
                responseBuilder.setSuccess(false).setMessage(ex.getMessage());
            }
        }

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void dataRestriction(DataRestrictionRequest request, StreamObserver<DataRestrictionResponse> responseObserver){
        DataRestrictionResponse.Builder responseBuilder = DataRestrictionResponse.newBuilder();

        if (Strings.isNullOrEmpty(request.getNamespace()) || Strings.isNullOrEmpty(request.getUserId())) {
            log.error("[GDPRService.dataRestriction] empty required payload: namespace or userId.");
            responseBuilder.setSuccess(false).setMessage("required payload is empty");
            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();
            return;
        }

        if (handler == null) {
            responseBuilder.setSuccess(true);
        } else {
            log.info("[GDPRService.dataRestriction] start executing for namespace [{}] userId [{}] restrict [{}]",
                    request.getNamespace(), request.getUserId(), request.getRestrict());
            try {
                handler.ProcessDataRestriction(request.getNamespace(), request.getUserId(), request.getRestrict(), request.getIsPublisherNamespace());
                responseBuilder.setSuccess(true);
            }catch (Exception ex) {
                log.error("[GDPRService.dataRestriction] error: [{}]", ex.getMessage());
                responseBuilder.setSuccess(false).setMessage(ex.getMessage());
            }
        }

        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void platformAccountClosure(net.accelbyte.gdpr.registered.v1.PlatformAccountClosureRequest request,
                                       io.grpc.stub.StreamObserver<net.accelbyte.gdpr.registered.v1.PlatformAccountClosureResponse> responseObserver) {
        PlatformAccountClosureResponse.Builder responseBuilder = PlatformAccountClosureResponse.newBuilder();
        if (Strings.isNullOrEmpty(request.getPlatform()) || Strings.isNullOrEmpty(request.getPlatformUserId()) || request.getAccountsList() == null || request.getAccountsList().size() == 0) {
            log.error("[GDPRService.platformAccountClosure] empty required payload: platform or platformUserId or linked accounts.");
            responseBuilder.setSuccess(false).setMessage("required payload is empty");
            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();
            return;
        }

        if (handler == null) {
            responseBuilder.setSuccess(true);
        } else {
            log.info("[GDPRService.platformAccountClosure] start executing for platform [{}]",
                    request.getPlatform());
            try {
                handler.ProcessPlatformAccountClosure(request.getPlatform(), request.getPlatformUserId(), request.getAccountsList());
                responseBuilder.setSuccess(true);
            }catch (Exception ex) {
                log.error("[GDPRService.platformAccountClosure] error: [{}]", ex.getMessage());
                responseBuilder.setSuccess(false).setMessage(ex.getMessage());
            }
        }
        responseObserver.onNext(responseBuilder.build());
        responseObserver.onCompleted();
    }

}
