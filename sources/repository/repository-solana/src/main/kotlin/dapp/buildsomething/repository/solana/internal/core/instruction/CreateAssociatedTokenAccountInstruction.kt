package dapp.buildsomething.repository.solana.internal.core.instruction

import dapp.buildsomething.repository.solana.internal.core.AccountMeta
import dapp.buildsomething.repository.solana.internal.core.Constants.AssociatedTokenProgramID
import dapp.buildsomething.repository.solana.internal.core.Constants.SystemProgram
import dapp.buildsomething.repository.solana.internal.core.Constants.SysvarRentAddress
import dapp.buildsomething.repository.solana.internal.core.Constants.TokenProgramID
import dapp.buildsomething.repository.solana.internal.core.PublicKey

class CreateAssociatedTokenAccountInstruction(
    payer: PublicKey,
    associatedToken: PublicKey,
    owner: PublicKey,
    mint: PublicKey,
) : Instruction {

    override val data: ByteArray = byteArrayOf(0)

    override val keys: List<AccountMeta> = listOf(
        AccountMeta.signerAndWritable(payer),
        AccountMeta.writable(associatedToken),
        AccountMeta(owner),
        AccountMeta(mint),
        AccountMeta(SystemProgram),
        AccountMeta(TokenProgramID),
        AccountMeta(SysvarRentAddress),
    )

    override val programId: PublicKey = AssociatedTokenProgramID
}
