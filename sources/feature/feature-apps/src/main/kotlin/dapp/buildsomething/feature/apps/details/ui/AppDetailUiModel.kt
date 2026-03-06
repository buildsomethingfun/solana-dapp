package dapp.buildsomething.feature.apps.details.ui

import dapp.buildsomething.repository.something.internal.api.model.AppStatus

internal data class AppDetailUiModel(
    val id: String,
    val name: String,
    val description: String?,
    val status: AppStatus,
    val deployedUrl: String?,
    val androidPackage: String?,
    val appMintAddress: String?,
    val publisherMintAddress: String?,
    val iconUrl: String?,
    val screenshots: List<String>,
    val licenseUrl: String?,
    val privacyPolicyUrl: String?,
    val copyrightUrl: String?,
    val createdAt: String,
    val updatedAt: String,
)
