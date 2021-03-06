// Licensed to the Software Freedom Conservancy (SFC) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The SFC licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

package org.openqa.selenium.support.locators;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Resources;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsDriver;
import org.openqa.selenium.internal.Require;
import org.openqa.selenium.json.Json;
import org.openqa.selenium.json.JsonException;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.openqa.selenium.json.Json.MAP_TYPE;

/**
 * Used for finding elements by their location on a page, rather than their
 * position on the DOM. Elements are returned ordered by their proximity to
 * the last anchor element used for finding them. As an example:
 * <pre>
 *   List<WebElement> elements = driver.findElements(withTagName("p").above(lowest));
 * </pre>
 * Would return all {@code p} elements above the {@link WebElement}
 * {@code lowest} sorted by the proximity to {@code lowest}.
 * <p>
 * Proximity is determined by simply comparing the distance to the center
 * point of each of the elements in turn. For some non-rectangular shapes
 * (such as paragraphs of text that take more than one line), this may lead
 * to some surprising results.
 * <p>
 * In addition, be aware that the relative locators all use the "client
 * bounding rect" of elements to determine whether something is "left",
 * "right", "above" or "below" of another. Given the example:
 * <pre>
 *   +-----+
 *   |  a  |---+
 *   +-----+ b |
 *       +-----+
 * </pre>
 * Where {@code a} partially overlaps {@code b}, {@code b} is none of
 * "above", "below", "left" or "right" of {@code a}. This is because of
 * how these are calculated using the box model. {@code b}'s bounding
 * rect has it's left-most edge to the right of {@code a}'s bounding
 * rect's right-most edge, so it is not considered to be "right" of
 * {@code a}. Similar logic applies for the other directions.
 */
public class RelativeLocator {

  private static final Json JSON = new Json();
  private static final String FIND_ELEMENTS;
  static {
    try {
      String location = String.format(
        "/%s/%s",
        RelativeLocator.class.getPackage().getName().replace(".", "/"),
        "findElements.js");

      URL url = RelativeLocator.class.getResource(location);

      String rawFunction = Resources.toString(url, StandardCharsets.UTF_8);
      FIND_ELEMENTS = String.format("return (%s).apply(null, arguments);", rawFunction);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
  private static final int CLOSE_IN_PIXELS = 160;

  /**
   * Start of a relative locator, finding elements by tag name.
   */
  public static RelativeBy withTagName(String tagName) {
    Require.nonNull("Tag name to look for", tagName);
    return new RelativeBy(By.tagName(tagName));
  }

  /**
   * Start of a relative locator, finding elements by tag name.
   */
  public static RelativeBy byXPath(String xpathexpression){
    Require.nonNull("xPath to search for", xpathexpression);
    return new RelativeBy(By.xpath(xpathexpression));
  }

  public static class RelativeBy extends By {
    private final Object root;
    private final List<Map<String, Object>> filters;

    private RelativeBy(Object rootLocator) {
      this(rootLocator, ImmutableList.of());
    }

    private RelativeBy(Object rootLocator, List<Map<String, Object>> filters) {
      if (rootLocator instanceof By) {
        assertLocatorCanBeSerialized(rootLocator);
        rootLocator = asAtomLocatorParameter(rootLocator);
      } else if (rootLocator instanceof Map) {
        if (((Map<?, ?>) rootLocator).keySet().size() != 1) {
          throw new IllegalArgumentException(
            "Root locators as find element payload must only have a single key: " + rootLocator);
        }
      } else if (!(rootLocator instanceof WebElement)) {
        throw new IllegalArgumentException("Root locator must be an element or a locator: " + rootLocator);
      }

      this.root = Require.nonNull("Root locator", rootLocator);
      this.filters = ImmutableList.copyOf(Require.nonNull("Filters", filters));
    }

    public RelativeBy above(WebElement element) {
      Require.nonNull("Element to search above of", element);
      return simpleDirection("above", element);
    }

    public RelativeBy above(By locator) {
      Require.nonNull("Locator", locator);
      assertLocatorCanBeSerialized(locator);
      return simpleDirection("above", locator);
    }

    public RelativeBy below(WebElement element) {
      Require.nonNull("Element to search below of", element);
      return simpleDirection("below", element);
    }

    public RelativeBy below(By locator) {
      Require.nonNull("Locator", locator);
      assertLocatorCanBeSerialized(locator);
      return simpleDirection("below", locator);
    }

    public RelativeBy toLeftOf(WebElement element) {
      Require.nonNull("Element to search to left of", element);
      return simpleDirection("left", element);
    }

    public RelativeBy toLeftOf(By locator) {
      Require.nonNull("Locator", locator);
      assertLocatorCanBeSerialized(locator);
      return simpleDirection("left", locator);
    }

    public RelativeBy toRightOf(WebElement element) {
      Require.nonNull("Element to search to right of", element);
      return simpleDirection("right", element);
    }

    public RelativeBy toRightOf(By locator) {
      Require.nonNull("Locator", locator);
      assertLocatorCanBeSerialized(locator);
      return simpleDirection("right", locator);
    }

    public RelativeBy near(WebElement element) {
      Require.nonNull("Element to search near", element);
      return near(element, CLOSE_IN_PIXELS);
    }

    public RelativeBy near(WebElement element, int atMostDistanceInPixels) {
      Require.nonNull("Element to search near", element);
      Require.positive("Distance", atMostDistanceInPixels);

      return near((Object) element, atMostDistanceInPixels);
    }

    public RelativeBy near(By locator) {
      Require.nonNull("Locator", locator);
      return near((Object) locator, CLOSE_IN_PIXELS);
    }

    public RelativeBy near(By locator, int atMostDistanceInPixels) {
      Require.nonNull("Locator", locator);
      Require.positive("Distance", atMostDistanceInPixels);

      return near((Object) locator, atMostDistanceInPixels);
    }

    private RelativeBy near(Object locator, int atMostDistanceInPixels) {
      Require.nonNull("Locator", locator);
      Require.positive("Distance", atMostDistanceInPixels);

      return new RelativeBy(
        root,
        amend(ImmutableMap.of(
          "kind", "near",
          "args", ImmutableList.of(asAtomLocatorParameter(locator), atMostDistanceInPixels))));
    }

    @Override
    public List<WebElement> findElements(SearchContext context) {
      JavascriptExecutor js = extractJsExecutor(context);

      @SuppressWarnings("unchecked")
      List<WebElement> elements = (List<WebElement>) js.executeScript(FIND_ELEMENTS, this.toJson());
      return elements;
    }

    private RelativeBy simpleDirection(String direction, Object locator) {
      Require.nonNull("Direction to search in", direction);
      Require.nonNull("Locator", locator);

      return new RelativeBy(
        root,
        amend(ImmutableMap.of(
          "kind", direction,
          "args", ImmutableList.of(asAtomLocatorParameter(locator)))));

    }

    private List<Map<String, Object>> amend(Map<String, Object> toAdd) {
      return ImmutableList.<Map<String, Object>>builder()
        .addAll(filters)
        .add(toAdd)
        .build();
    }

    private JavascriptExecutor extractJsExecutor(SearchContext context) {
      if (context instanceof JavascriptExecutor) {
        return (JavascriptExecutor) context;
      }

      Object current = context;
      while (current instanceof WrapsDriver) {
        WebDriver driver = ((WrapsDriver) context).getWrappedDriver();
        if (driver instanceof JavascriptExecutor) {
          return (JavascriptExecutor) driver;
        }
        current = driver;
      }

      throw new IllegalArgumentException("Cannot find elements, since the context cannot execute JS: " + context);
    }

    private Map<String, Object> toJson() {
      return ImmutableMap.of(
        "relative", ImmutableMap.of(
          "root", root,
          "filters", filters));
    }
  }

  private static Object asAtomLocatorParameter(Object object) {
    if (object instanceof WebElement) {
      return object;
    }

    if (!(object instanceof By)) {
      throw new IllegalArgumentException("Expected locator to be either an element or a By: " + object);
    }

    assertLocatorCanBeSerialized(object);

    Map<String, Object> raw = JSON.toType(JSON.toJson(object), MAP_TYPE);

    if (!(raw.get("using") instanceof String)) {
      throw new JsonException("Expected JSON encoded form of locator to have a 'using' field. " + raw);
    }
    if (!raw.containsKey("value")) {
      throw new JsonException("Expected JSON encoded form of locator to have a 'value' field: " + raw);
    }

    return ImmutableMap.of((String) raw.get("using"), raw.get("value"));
  }

  private static void assertLocatorCanBeSerialized(Object locator) {
    Require.nonNull("Locator", locator);

    Class<?> clazz = locator.getClass();

    while (!clazz.equals(Object.class)) {
      try {
        clazz.getDeclaredMethod("toJson");
        return;
      } catch (NoSuchMethodException e) {
        // Do nothing. Continue with the loop
      }
      clazz = clazz.getSuperclass();
    }

    throw new IllegalArgumentException(
      "Locator must be serializable to JSON using a `toJson` method. " + locator);
  }
}
