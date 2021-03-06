package com.foundy.presentation.view.common

import com.foundy.domain.model.Notice
import com.foundy.domain.usecase.favorite.AddFavoriteNoticeUseCase
import com.foundy.domain.usecase.favorite.IsFavoriteNoticeUseCase
import com.foundy.domain.usecase.favorite.ReadFavoriteListUseCase
import com.foundy.domain.usecase.favorite.RemoveFavoriteNoticeUseCase
import com.foundy.presentation.model.NoticeItemUiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AssistedFactory
interface NoticeItemUiStateCreatorFactory {
    fun create(
        viewModelScope: CoroutineScope,
        dispatcher: CoroutineDispatcher = Dispatchers.Main,
        triggerCollection: Boolean = true
    ): NoticeItemUiStateCreator
}

class NoticeItemUiStateCreator @AssistedInject constructor(
    readFavoriteListUseCase: ReadFavoriteListUseCase,
    private val addFavoriteNoticeUseCase: AddFavoriteNoticeUseCase,
    private val removeFavoriteNoticeUseCase: RemoveFavoriteNoticeUseCase,
    private val isFavoriteNoticeUseCase: IsFavoriteNoticeUseCase,
    @Assisted private val viewModelScope: CoroutineScope,
    @Assisted private val dispatcher: CoroutineDispatcher = Dispatchers.Main,
    @Assisted triggerCollection: Boolean
) {

    init {
        if (triggerCollection) {
            viewModelScope.launch(dispatcher) {
                readFavoriteListUseCase().collect()
            }
        }
    }

    /**
     * Favorite에 대한 상태를 가진 [NoticeItemUiState]를 [notice]로부터 생성한다.
     */
    fun create(notice: Notice): NoticeItemUiState {
        return NoticeItemUiState(
            notice,
            onClickFavorite = { isFavorite ->
                viewModelScope.launch(dispatcher) {
                    if (isFavorite) {
                        addFavoriteNoticeUseCase(notice)
                    } else {
                        removeFavoriteNoticeUseCase(notice)
                    }
                }
            },
            isFavorite = { isFavoriteNoticeUseCase(notice) }
        )
    }
}
