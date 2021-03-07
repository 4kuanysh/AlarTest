package kz.kuanysh.alartest.presentation.data_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.kuanysh.alartest.domain.model.Data
import kz.kuanysh.alartest.domain.usecase.GetDataUseCase
import kz.kuanysh.alartest.domain.usecase.LogoutUseCase
import kz.kuanysh.alartest.utils.Event
import kz.kuanysh.alartest.utils.Result

private const val INIT_PAGE = 1

class DataListViewModel(
    private val code: String,
    private val getDataUseCase: GetDataUseCase,
    private val logoutUseCase: LogoutUseCase,
) : ViewModel() {

    private val _dataListUIState = MutableLiveData<DataListUI>()
    val dataListUIState: LiveData<DataListUI> get() = _dataListUIState

    private var currentPage = INIT_PAGE

    fun dispatch(action: DataListAction) {
        when (action) {
            is DataListAction.UpdateData -> updateData()
            is DataListAction.LoadNextData -> loadNextPage()
            is DataListAction.ItemClicked -> onItemClicked(action.item)
            is DataListAction.Logout -> logout()
        }
    }

    init {
        dispatch(DataListAction.UpdateData)
    }

    private fun updateData() {
        viewModelScope.launch {
            _dataListUIState.value = DataListUI.DataUpdating
            currentPage = INIT_PAGE
            _dataListUIState.value = when (val result = getDataUseCase(code, currentPage)) {
                is Result.Success -> DataListUI.DataUpdated(result.data.items)
                is Result.Error -> DataListUI.Error
            }
        }
    }

    private fun loadNextPage() {
        viewModelScope.launch {
            _dataListUIState.value = DataListUI.NextDataLoading
            currentPage++
            _dataListUIState.value = when (val result = getDataUseCase(code, currentPage)) {
                is Result.Success -> DataListUI.NextDataLoaded(result.data.items)
                is Result.Error -> DataListUI.Error
            }
        }
    }

    private fun onItemClicked(item: Data.Item) {
        _dataListUIState.value = DataListUI.OpenDetails(Event(item))
    }

    private fun logout() {
        logoutUseCase()
        _dataListUIState.value = DataListUI.Logout
    }

}

sealed class DataListAction {
    object UpdateData : DataListAction()
    object LoadNextData : DataListAction()
    object Logout : DataListAction()
    data class ItemClicked(val item: Data.Item) : DataListAction()
}

sealed class DataListUI {
    object DataUpdating : DataListUI()
    object NextDataLoading : DataListUI()
    data class DataUpdated(val items: List<Data.Item>) : DataListUI()
    data class NextDataLoaded(val items: List<Data.Item>) : DataListUI()
    object Error : DataListUI()
    data class OpenDetails(val item: Event<Data.Item>) : DataListUI()
    object Logout : DataListUI()
}