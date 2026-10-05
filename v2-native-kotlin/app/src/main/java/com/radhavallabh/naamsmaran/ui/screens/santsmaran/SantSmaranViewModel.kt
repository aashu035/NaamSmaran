package com.radhavallabh.naamsmaran.ui.screens.santsmaran

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.radhavallabh.naamsmaran.data.santsmaran.SantSmaranRepository
import com.radhavallabh.naamsmaran.domain.engine.SantSmaranPageBuilder
import com.radhavallabh.naamsmaran.domain.model.SantPage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SantSmaranUiState(
    val isLoading: Boolean = true,
    val title: String = "",
    val pages: List<SantPage> = emptyList(),
    val errorMessage: String? = null,
    /** Optional: advance one page every ~6 seconds. Off by default — paging is manual. */
    val autoAdvance: Boolean = false,
    /** Optional: show every entry as a list instead of the one-card-at-a-time pager. */
    val listView: Boolean = false
)

@HiltViewModel
class SantSmaranViewModel @Inject constructor(
    private val repository: SantSmaranRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SantSmaranUiState())
    val uiState: StateFlow<SantSmaranUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                val content = repository.load()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        title = content.title,
                        pages = SantSmaranPageBuilder.build(content)
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = "सूची खुल नहीं सकी: ${e.message}")
                }
            }
        }
    }

    fun toggleAutoAdvance() = _uiState.update { it.copy(autoAdvance = !it.autoAdvance) }

    fun setAutoAdvance(enabled: Boolean) = _uiState.update { it.copy(autoAdvance = enabled) }

    fun toggleListView() = _uiState.update { it.copy(listView = !it.listView) }

    fun setListView(enabled: Boolean) = _uiState.update { it.copy(listView = enabled) }
}
