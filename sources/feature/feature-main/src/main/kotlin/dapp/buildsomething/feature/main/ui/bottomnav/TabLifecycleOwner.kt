package dapp.buildsomething.feature.main.ui.bottomnav

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry

internal class TabLifecycleOwner : LifecycleOwner {

    override val lifecycle: LifecycleRegistry = LifecycleRegistry(this)

    private var parentState: Lifecycle.State = Lifecycle.State.INITIALIZED
    private var isSelected: Boolean = false

    fun updateState(parentState: Lifecycle.State, isSelected: Boolean) {
        this.parentState = parentState
        this.isSelected = isSelected
        sync()
    }

    private fun sync() {
        val targetState = if (isSelected) {
            parentState
        } else {
            minOf(parentState, Lifecycle.State.STARTED)
        }
        lifecycle.currentState = targetState
    }
}
