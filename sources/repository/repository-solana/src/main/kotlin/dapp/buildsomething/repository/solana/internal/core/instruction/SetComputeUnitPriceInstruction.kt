package dapp.buildsomething.repository.solana.internal.core.instruction

import dapp.buildsomething.repository.solana.internal.core.AccountMeta
import dapp.buildsomething.repository.solana.internal.core.Constants.ComputeBudgetProgramID
import dapp.buildsomething.repository.solana.internal.core.PublicKey

data class SetComputeUnitPriceInstruction(
    val microLamports: Long,
) : Instruction {
    override val data: ByteArray = ByteArray(9).apply {
        this[0] = 3
        for (i in 0 until 8) {
            this[i + 1] = ((microLamports shr (i * 8)) and 0xFF).toByte()
        }
    }

    override val keys: List<AccountMeta> = emptyList()

    override val programId: PublicKey = ComputeBudgetProgramID
}
