package dapp.buildsomething.repository.solana.internal.core.instruction

import dapp.buildsomething.repository.solana.internal.core.Constants.TokenProgramID
import dapp.buildsomething.repository.solana.internal.core.PublicKey

class SplTransferInstruction @JvmOverloads constructor(
    from: PublicKey,
    to: PublicKey,
    mint: PublicKey,
    owner: PublicKey,
    amount: Long,
    decimals: Int,
    signers: List<PublicKey> = emptyList(),
) : TokenTransferInstruction(from, to, mint, owner, amount, decimals, signers) {
    override val programId: PublicKey = TokenProgramID
}
