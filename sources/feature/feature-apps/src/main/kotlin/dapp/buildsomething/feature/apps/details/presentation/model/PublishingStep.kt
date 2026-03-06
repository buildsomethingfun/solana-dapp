package dapp.buildsomething.feature.apps.details.presentation.model

enum class PublishingStep {
    CreatingRelease,
    RequestingPayment,
    SigningPayment,
    ConfirmingBuild,
    Building,
    GeneratingLegal,
    GeneratingScreenshots,
    GeneratingIcon,
    CreatingPublisherNft,
    SigningPublisherNft,
    CreatingAppNft,
    SigningAppNft,
    CreatingReleaseNft,
    SigningReleaseNft,
    PreparingAttestation,
    SigningAttestation,
    Publishing,
    Done,
}
