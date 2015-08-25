/*
 * Copyright (C) 2004 - 2014 Brian McCallister
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.skife.jdbi.v2;

import org.junit.Test;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ConcreteStatementContextTest {


    @Test(expected = IllegalArgumentException.class)
    public void testShouldNotBeAbleToCombineGeneratedKeysAndConcurrentUpdatable() throws Exception {
        final ConcreteStatementContext context =
                new ConcreteStatementContext(Collections.<String, Object>emptyMap(), new MappingRegistry());

        context.setReturningGeneratedKeys(true);
        context.setConcurrentUpdatable(true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testShouldNotBeAbleToCombineConcurrentUpdatableAndGeneratedKeys() throws Exception {
        final ConcreteStatementContext context =
                new ConcreteStatementContext(Collections.<String, Object>emptyMap(), new MappingRegistry());

        context.setConcurrentUpdatable(true);
        context.setReturningGeneratedKeys(true);
    }

    private static class Foo {
    }

    private static class FooMapper implements ResultSetMapper<Foo> {
        @Override
        public Foo map(int index, ResultSet r, StatementContext ctx) throws SQLException {
            return null;
        }
    }

    @Test
    public void testMapperForDelegatesToRegistry() {
        ResultSetMapper mapper = new FooMapper();

        MappingRegistry registry = new MappingRegistry();
        registry.add(mapper);

        final ConcreteStatementContext context =
                new ConcreteStatementContext(Collections.<String, Object>emptyMap(), registry);

        assertThat(context.mapperFor(Foo.class), equalTo(mapper));
    }
}