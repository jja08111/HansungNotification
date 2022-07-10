package com.foundy.presentation.view.keyword

import androidx.lifecycle.ViewModel
import com.foundy.domain.usecase.auth.SignInWithUseCase
import com.foundy.domain.usecase.messaging.SubscribeAllPreviousTopicUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signInWithUseCase: SignInWithUseCase,
    private val subscribeAllPreviousTopicUseCase: SubscribeAllPreviousTopicUseCase
) : ViewModel() {

    fun signInWith(idToken: String) = signInWithUseCase(idToken)

    fun subscribeAllPreviousTopic() = subscribeAllPreviousTopicUseCase()
}