package com.foundy.hansungnotification

import android.content.Context
import android.content.pm.ActivityInfo
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import com.foundy.domain.usecase.auth.IsSignedInUseCase
import com.foundy.domain.usecase.keyword.AddKeywordUseCase
import com.foundy.domain.usecase.keyword.ReadKeywordListUseCase
import com.foundy.domain.usecase.keyword.RemoveKeywordUseCase
import com.foundy.domain.usecase.keyword.ValidateKeywordUseCase
import com.foundy.domain.usecase.messaging.SubscribeToUseCase
import com.foundy.domain.usecase.messaging.UnsubscribeFromUseCase
import com.foundy.domain.usecase.notice.HasSearchResultUseCase
import com.foundy.hansungnotification.fake.*
import com.foundy.hansungnotification.utils.RetryTestRule
import com.foundy.presentation.R
import com.foundy.presentation.view.keyword.KeywordActivity
import com.foundy.presentation.view.keyword.KeywordActivityViewModel
import com.foundy.presentation.view.keyword.KeywordFragmentViewModel
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
class KeywordActivityTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Rule
    @JvmField
    val retryRule = RetryTestRule()

    private val fakeAuthRepository = FakeAuthRepositoryImpl()
    private val fakeKeywordRepository = FakeKeywordRepositoryImpl()
    private val fakeMessagingRepository = FakeMessagingRepositoryImpl()
    private val fakeNoticeRepository = FakeNoticeRepositoryImpl()

    @BindValue
    val keywordActivityViewModel = KeywordActivityViewModel(
        IsSignedInUseCase(fakeAuthRepository),
    )

    @BindValue
    val keywordFragmentViewModel = KeywordFragmentViewModel(
        ReadKeywordListUseCase(fakeKeywordRepository),
        AddKeywordUseCase(fakeKeywordRepository),
        RemoveKeywordUseCase(fakeKeywordRepository),
        SubscribeToUseCase(fakeMessagingRepository),
        UnsubscribeFromUseCase(fakeMessagingRepository),
        HasSearchResultUseCase(fakeNoticeRepository),
        ValidateKeywordUseCase()
    )

    lateinit var context: Context

    @Before
    fun setUp() {
        hiltRule.inject()
        context = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun showLoginFragment_ifNotSignedIn() = runTest {
        fakeAuthRepository.setSignedIn(false)

        launchActivity<KeywordActivity>()

        onView(withId(R.id.loginFragment)).check(matches(isDisplayed()))
    }

    @Test
    fun showKeywordFragment_ifSignedIn() = runTest {
        fakeAuthRepository.setSignedIn(true)

        launchActivity<KeywordActivity>()

        onView(withId(R.id.keywordFragment)).check(matches(isDisplayed()))
    }

    @Test
    fun navControllerWorksWithoutCrash_whenRecreateIfLoggedIn() {
        fakeAuthRepository.setSignedIn(true)
        val scenario = launchActivity<KeywordActivity>()

        scenario.onActivity {
            it.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }

        onView(withId(R.id.keywordFragment)).check(matches(isDisplayed()))
    }
}