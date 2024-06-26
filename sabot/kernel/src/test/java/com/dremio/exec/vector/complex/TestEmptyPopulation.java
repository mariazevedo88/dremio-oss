/*
 * Copyright (C) 2017-2019 Dremio Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dremio.exec.vector.complex;

import static com.dremio.TestBuilder.listOf;
import static com.dremio.TestBuilder.mapOf;

import com.dremio.BaseTestQuery;
import org.apache.arrow.vector.UInt4Vector;
import org.apache.arrow.vector.complex.EmptyValuePopulator;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TestEmptyPopulation extends BaseTestQuery {

  private UInt4Vector offsets;
  private EmptyValuePopulator populator;

  @Before
  public void initialize() {
    offsets = new UInt4Vector("offsets", allocator);
    offsets.allocateNew();
    offsets.set(0, 0);
    offsets.setValueCount(1);
    Assert.assertTrue("offsets must have one value", offsets.getValueCount() == 1);
    populator = new EmptyValuePopulator(offsets);
  }

  @After
  public void cleanup() {
    offsets.clear();
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testNegativeValuesThrowException() {
    populator.populate(-1);
  }

  @Test
  public void testZeroHasNoEffect() {
    populator.populate(0);
    Assert.assertTrue("offset must have one value", offsets.getValueCount() == 1);
  }

  @Test
  public void testEmptyPopulationWorks() {
    populator.populate(1);
    Assert.assertEquals("offset must have valid size", 2, offsets.getValueCount());
    Assert.assertEquals("value must match", 0, offsets.get(1));

    offsets.set(1, 10);
    populator.populate(2);
    Assert.assertEquals("offset must have valid size", 3, offsets.getValueCount());
    Assert.assertEquals("value must match", 10, offsets.get(1));

    offsets.set(2, 20);
    populator.populate(5);
    Assert.assertEquals("offset must have valid size", 6, offsets.getValueCount());
    for (int i = 2; i <= 5; i++) {
      Assert.assertEquals(String.format("value at index[%s] must match", i), 20, offsets.get(i));
    }

    populator.populate(0);
    Assert.assertEquals("offset must have valid size", 1, offsets.getValueCount());
    Assert.assertEquals("value must match", 0, offsets.get(0));
  }

  @Test
  public void testRepeatedScalarEmptyFirst() throws Exception {
    final String query = "select * from cp.\"vector/complex/repeated-scalar-empty-first.json\"";

    testBuilder()
        .sqlQuery(query)
        .ordered()
        .baselineColumns("a")
        .baselineValues(listOf())
        .baselineValues(listOf(1L))
        .baselineValues(listOf(2L))
        .go();
  }

  @Test
  public void testRepeatedScalarEmptyLast() throws Exception {
    final String query = "select * from cp.\"vector/complex/repeated-scalar-empty-last.json\"";

    testBuilder()
        .sqlQuery(query)
        .ordered()
        .baselineColumns("a")
        .baselineValues(listOf(1L))
        .baselineValues(listOf(2L))
        .baselineValues(listOf())
        .go();
  }

  @Test
  public void testRepeatedScalarEmptyInBetween() throws Exception {
    final String query = "select * from cp.\"vector/complex/repeated-scalar-empty-between.json\"";

    testBuilder()
        .sqlQuery(query)
        .ordered()
        .baselineColumns("a")
        .baselineValues(listOf(1L))
        .baselineValues(listOf())
        .baselineValues(listOf(2L))
        .go();
  }

  @Test
  public void testRepeatedListEmptyFirst() throws Exception {
    final String query = "select * from cp.\"vector/complex/repeated-list-empty-first.json\"";

    testBuilder()
        .sqlQuery(query)
        .ordered()
        .baselineColumns("a")
        .baselineValues(listOf())
        .baselineValues(listOf(listOf(1L)))
        .baselineValues(listOf(listOf(2L)))
        .go();
  }

  @Test
  public void testRepeatedListEmptyLast() throws Exception {
    final String query = "select * from cp.\"vector/complex/repeated-list-empty-last.json\"";

    testBuilder()
        .sqlQuery(query)
        .ordered()
        .baselineColumns("a")
        .baselineValues(listOf(listOf(1L)))
        .baselineValues(listOf(listOf(2L)))
        .baselineValues(listOf())
        .go();
  }

  @Test
  public void testRepeatedListEmptyBetween() throws Exception {
    final String query = "select * from cp.\"vector/complex/repeated-list-empty-between.json\"";

    testBuilder()
        .sqlQuery(query)
        .ordered()
        .baselineColumns("a")
        .baselineValues(listOf(listOf(1L)))
        .baselineValues(listOf())
        .baselineValues(listOf(listOf(2L)))
        .go();
  }

  @Test
  public void testRepeatedMapEmptyFirst() throws Exception {
    final String query = "select * from cp.\"vector/complex/repeated-map-empty-first.json\"";

    testBuilder()
        .sqlQuery(query)
        .ordered()
        .baselineColumns("a")
        .baselineValues(listOf())
        .baselineValues(listOf(mapOf("b", 1L)))
        .baselineValues(listOf(mapOf("b", 2L)))
        .go();
  }

  @Test
  public void testRepeatedMapEmptyLast() throws Exception {
    final String query = "select * from cp.\"vector/complex/repeated-map-empty-last.json\"";

    testBuilder()
        .sqlQuery(query)
        .ordered()
        .baselineColumns("a")
        .baselineValues(listOf(mapOf("b", 1L)))
        .baselineValues(listOf(mapOf("b", 2L)))
        .baselineValues(listOf())
        .go();
  }

  @Test
  public void testRepeatedMapEmptyBetween() throws Exception {
    final String query = "select * from cp.\"vector/complex/repeated-map-empty-between.json\"";

    testBuilder()
        .sqlQuery(query)
        .ordered()
        .baselineColumns("a")
        .baselineValues(listOf(mapOf("b", 1L)))
        .baselineValues(listOf())
        .baselineValues(listOf(mapOf("b", 2L)))
        .go();
  }

  @Test
  public void testMapEmptyFirst() throws Exception {
    final String query = "select * from cp.\"vector/complex/map-empty-first.json\"";

    testBuilder()
        .sqlQuery(query)
        .ordered()
        .baselineColumns("a", "c")
        .baselineValues(null, 1L)
        .baselineValues(mapOf("b", 1L), null)
        .baselineValues(mapOf("b", 2L), null)
        .go();
  }

  @Test
  public void testMapEmptyLast() throws Exception {
    final String query = "select * from cp.\"vector/complex/map-empty-last.json\"";

    testBuilder()
        .sqlQuery(query)
        .ordered()
        .baselineColumns("a", "c")
        .baselineValues(mapOf("b", 1L), null)
        .baselineValues(mapOf("b", 2L), null)
        .baselineValues(null, 1L)
        .go();
  }

  @Test
  public void testMapEmptyBetween() throws Exception {
    final String query = "select * from cp.\"vector/complex/map-empty-between.json\"";

    testBuilder()
        .sqlQuery(query)
        .ordered()
        .baselineColumns("a", "c")
        .baselineValues(mapOf("b", 1L), null)
        .baselineValues(null, 1L)
        .baselineValues(mapOf("b", 2L), null)
        .go();
  }

  @Test
  public void testMultiLevelRepeatedListEmptyFirst() throws Exception {
    final String query = "select * from cp.\"vector/complex/multi-repeated-list-empty-first.json\"";

    testBuilder()
        .sqlQuery(query)
        .ordered()
        .baselineColumns("a")
        .baselineValues(listOf())
        .baselineValues(listOf(listOf(listOf(1L), listOf(3L)), listOf(listOf(5L, 7L))))
        .baselineValues(listOf(listOf(listOf(2L), listOf(4L))))
        .go();
  }

  @Test
  public void testMultiLevelRepeatedListEmptyLast() throws Exception {
    final String query = "select * from cp.\"vector/complex/multi-repeated-list-empty-last.json\"";

    testBuilder()
        .sqlQuery(query)
        .ordered()
        .baselineColumns("a")
        .baselineValues(listOf(listOf(listOf(1L), listOf(3L)), listOf(listOf(5L, 7L))))
        .baselineValues(listOf(listOf(listOf(2L), listOf(4L))))
        .baselineValues(listOf())
        .go();
  }

  @Test
  public void testMultiLevelRepeatedListEmptyBetween() throws Exception {
    final String query =
        "select * from cp.\"vector/complex/multi-repeated-list-empty-between.json\"";

    testBuilder()
        .sqlQuery(query)
        .ordered()
        .baselineColumns("a")
        .baselineValues(listOf(listOf(listOf(1L), listOf(3L)), listOf(listOf(5L, 7L))))
        .baselineValues(listOf())
        .baselineValues(listOf(listOf(listOf(2L), listOf(4L))))
        .go();
  }

  @Test
  public void testMultiLevelRepeatedListWithMultipleEmpties() throws Exception {
    final String query = "select * from cp.\"vector/complex/multi-repeated-list-multi-empty.json\"";

    testBuilder()
        .sqlQuery(query)
        .ordered()
        .baselineColumns("a")
        .baselineValues(listOf())
        .baselineValues(listOf(listOf(listOf(1L), listOf(3L)), listOf(listOf())))
        .baselineValues(listOf(listOf(listOf(2L), listOf()), listOf()))
        .go();
  }
}
