package com.dicoding.finalsoccermatches.presentation


import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.swipeLeft
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import android.view.ViewGroup
import com.dicoding.finalsoccermatches.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun mainActivityTest() {
        Thread.sleep(3000)

        val tabView = onView(
            allOf(
                withContentDescription("Next"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.tabLayout),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        tabView.perform(click())

        val viewPager = onView(
            allOf(
                withId(R.id.viewPager),
                childAtPosition(
                    allOf(
                        withId(R.id.main_layout),
                        childAtPosition(
                            withId(R.id.main_container),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        viewPager.perform(swipeLeft())

        val bottomNavigationItemView = onView(
            allOf(
                withId(R.id.teams), withContentDescription("Teams"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottom_navigation),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView.perform(click())

        val bottomNavigationItemView2 = onView(
            allOf(
                withId(R.id.favorites), withContentDescription("Favorites"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.bottom_navigation),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView2.perform(click())

        val tabView2 = onView(
            allOf(
                withContentDescription("Teams"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.tabLayout),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        tabView2.perform(click())

        val viewPager2 = onView(
            allOf(
                withId(R.id.viewPager),
                childAtPosition(
                    allOf(
                        withId(R.id.main_layout),
                        childAtPosition(
                            withId(R.id.main_container),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        viewPager2.perform(swipeLeft())
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
