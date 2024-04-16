/*
 * Copyright (c) 2023 AccelByte Inc. All Rights Reserved
 * This program is made available under the terms of the MIT License.
 */

package net.accelbyte.gdpr.sdk;

import net.accelbyte.gdpr.registered.v1.AccountInfo;
import net.accelbyte.gdpr.sdk.object.DataGenerationResult;

import java.util.List;

public interface GDPRHandler {

    /**
     * Process data generation for the requested user.
     *
     * The concrete implementation should put the data result into "data" property inside DataGenerationResult object.
     * The "data" property (inside DataGenerationResult object) was in form of Map&lt;ModuleId, byte[]&gt;,
     * this allows the concrete implementation to categorized multiple data based on the modules they have.
     * Example:
     *  Map&lt;ModuleId, byte[]&gt; = {
     *    entitlement: entitlementData,
     *    wallet: walletData,
     *    transaction: transactionData
     *  }
     *
     * @param namespace             namespace of user
     * @param userId                user id
     * @param isPublisherNamespace  indicate whether the "namespace" is a publisher namespace or game namespace
     * @return DataGenerationResult object
     */
    DataGenerationResult ProcessDataGeneration(String namespace, String userId, boolean isPublisherNamespace);

    /**
     * Process data deletion for the requested user.
     *
     * @param namespace             namespace of user
     * @param userId                user id
     * @param isPublisherNamespace  indicate whether the "namespace" is a publisher namespace or game namespace
     */
    void ProcessDataDeletion(String namespace, String userId, boolean isPublisherNamespace);

    /**
     * Process data restriction for the requested user.
     *
     * This process will ensure the personal data associated with disabled account
     * ceased to be available to other users.
     *
     * @param namespace             namespace of user
     * @param userId                user id
     * @param restrict              restrict or not
     * @param isPublisherNamespace  indicate whether the "namespace" is a publisher namespace or game namespace
     */
    void ProcessDataRestriction(String namespace, String userId, boolean restrict, boolean isPublisherNamespace);

    /**
     * Handle 3rd party platform account closure.
     *
     * @param platform        3rd party platform
     * @param platformUserId  3rd party platform account id
     * @param accounts        linked AGS accounts in all namespaces(including publisher namespace)
     */
    void ProcessPlatformAccountClosure(String platform, String platformUserId, List<AccountInfo> accounts);
}
