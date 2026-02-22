package dapp.buildsomething.repository.solana.internal.core.instruction

import dapp.buildsomething.repository.solana.internal.core.AccountMeta
import dapp.buildsomething.repository.solana.internal.core.PublicKey

interface Instruction {
    val data: ByteArray
    val keys: List<AccountMeta>
    val programId: PublicKey
}
