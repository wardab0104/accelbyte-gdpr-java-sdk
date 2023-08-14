/*
 * Copyright (c) 2023 AccelByte Inc. All Rights Reserved
 * This program is made available under the terms of the MIT License.
 */

package net.accelbyte.gdpr.sdk;

import net.accelbyte.gdpr.sdk.object.DataGenerationResult;

public interface GDPRHandler {

    /**
     * Process data generation for the requested user.
     *
     * The concrete implementation should put the data result into "data" property inside DataGenerationResult object.
     * The "data" property (inside DataGenerationResult object) was in form of Map<ModuleId, byte[]>,
     * this allows the concrete implementation to categorized multiple data based on the modules they have.
     * Example:
     *  Map<ModuleId, byte[]> = {
     *    entitlement: entitlementData,
     *    wallet: walletData,
     *    transaction: transactionData
     *  }
     *
     * @param namespace namespace of user
     * @param userId    user id
     * @return DataGenerationResult object
     */
    DataGenerationResult ProcessDataGeneration(String namespace, String userId);

    /**
     * Process data deletion for the requested user.
     *
     * @param namespace namespace of user
     * @param userId    user id
     */
    void ProcessDataDeletion(String namespace, String userId);
}
