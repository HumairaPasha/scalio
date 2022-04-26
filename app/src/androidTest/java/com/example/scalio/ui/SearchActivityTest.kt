package com.example.scalio.ui

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.scalio.R
import com.example.scalio.ui.search.SearchActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class SearchActivityTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<SearchActivity> =
        ActivityScenarioRule(SearchActivity::class.java)

    @Test
    fun testListIsDisplayedOnClick() {
        onView(withId(R.id.login_field)).perform(typeText("test"))
        onView(withId(R.id.submit))
            .perform(click())
        Thread.sleep(5000)
        onView(withId(R.id.users_list_view))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

    }

    @Test
    fun testMessageDisplayedOnInvalidQuery() {
        onView(withId(R.id.login_field)).perform(typeText("%4@"))
        onView(withId(R.id.submit))
            .perform(click())
        Thread.sleep(5000)
        onView(withId(R.id.error_message))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

    }
}