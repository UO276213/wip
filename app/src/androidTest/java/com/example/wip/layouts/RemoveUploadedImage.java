package com.example.wip.layouts;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.GrantPermissionRule;

import com.example.wip.R;
import com.example.wip.ui.layouts.activities.MainActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RemoveUploadedImage {
    /**
     * AVISO: A veces las pruebas no pasan a la primera. Hay que comenzar desde la ventana
     * donde se eligen las comunidades. Para que funcionen los tests hay que haber dado
     * en recordar elección cuando se piden permisos. Cada ejeccución puede verse afectada por pruebas anteriores.
     */

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Rule
    public GrantPermissionRule mGrantPermissionRule =
            GrantPermissionRule.grant(
                    "android.permission.READ_EXTERNAL_STORAGE");

    @Test
    public void removeUploadedImage() {
        ViewInteraction materialButton = onView(
                allOf(withId(R.id.search), withText("Buscar"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction materialTextView = onView(
                allOf(withId(R.id.nombre_fiesta), withText("Fiestas de San Sebastián 2023"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.RelativeLayout")),
                                        1),
                                0),
                        isDisplayed()));
        materialTextView.perform(click());

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.btnFav),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.scrollView),
                                        0),
                                1)));
        appCompatImageButton.perform(scrollTo(), click());

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.btnAddImage),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.scrollView),
                                        0),
                                2)));
        appCompatImageButton2.perform(scrollTo(), click());

        pressBack();

        ViewInteraction materialTextView2 = onView(
                allOf(withId(R.id.nombre_fiesta), withText("Fiestas de San Sebastián 2023"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.RelativeLayout")),
                                        1),
                                0),
                        isDisplayed()));
        materialTextView2.perform(click());

        ViewInteraction imageView = onView(
                allOf(withId(R.id.grid_item_image),
                        withParent(withParent(withId(R.id.recyclerView))),
                        isDisplayed()));
        imageView.check(matches(isDisplayed()));

        ViewInteraction appCompatImageView = onView(
                allOf(withId(R.id.grid_item_image),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.recyclerView),
                                        0),
                                0)));
        appCompatImageView.perform(scrollTo(), click());

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.deleteBtn),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction imageView2 = onView(
                allOf(withId(R.id.grid_item_image),
                        withParent(withParent(withId(R.id.recyclerView))),
                        isDisplayed()));
        imageView2.check(doesNotExist());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
