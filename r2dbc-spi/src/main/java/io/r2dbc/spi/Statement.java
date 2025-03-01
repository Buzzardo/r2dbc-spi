/*
 * Copyright 2017-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.r2dbc.spi;

import org.reactivestreams.Publisher;

/**
 * A statement that can be executed multiple times in a prepared and optimized way.
 *
 * @see Result
 * @see Row
 * @see Blob
 * @see Clob
 */
public interface Statement {

    /**
     * Save the current binding and create a new one.
     *
     * @return this {@link Statement}
     * @throws IllegalStateException if the statement is parametrized and not all parameter values are provided
     */
    Statement add();

    /**
     * Bind a value.
     *
     * @param identifier the identifier to bind to
     * @param value      the value to bind
     * @return this {@link Statement}
     * @throws IllegalArgumentException  if {@code identifier} or {@code value} is {@code null}
     * @throws IndexOutOfBoundsException if the parameter index is out of range
     * @deprecated Use {@link #bind(String, Object)} instead
     */
    @Deprecated
    default Statement bind(Object identifier, Object value) {
        if (identifier instanceof Integer) {
            return bind(((Integer) identifier).intValue(), value);
        }

        return bind((String) identifier, value);
    }

    /**
     * Bind a value to an index.  Indexes are zero-based.
     *
     * @param index the index to bind to
     * @param value the value to bind
     * @return this {@link Statement}
     * @throws IllegalArgumentException  if {@code value} is {@code null}
     * @throws IndexOutOfBoundsException if the parameter index is out of range
     */
    Statement bind(int index, Object value);

    /**
     * Bind a value.  The default implementation of this method calls {@link #bind(Object, Object)} to allow SPI change in a less-breaking way.
     *
     * @param name  the name of identifier to bind to
     * @param value the value to bind
     * @return this {@link Statement}
     * @throws IllegalArgumentException  if {@code name} or {@code value} is {@code null}
     * @throws IndexOutOfBoundsException if the parameter index is out of range
     */
    default Statement bind(String name, Object value) {
        return bind((Object) name, value);
    }

    /**
     * Bind a value to an index.  Indexes are zero-based.
     *
     * @param index the index to bind to
     * @param value the value to bind
     * @return this {@link Statement}
     * @throws IndexOutOfBoundsException if the parameter index is out of range
     */
    default Statement bind(int index, boolean value) {
        return bind(index, (Boolean) value);
    }

    /**
     * Bind a value to an index.  Indexes are zero-based.
     *
     * @param index the index to bind to
     * @param value the value to bind
     * @return this {@link Statement}
     * @throws IndexOutOfBoundsException if the parameter index is out of range
     */
    default Statement bind(int index, byte value) {
        return bind(index, (Byte) value);
    }

    /**
     * Bind a value to an index.  Indexes are zero-based.
     *
     * @param index the index to bind to
     * @param value the value to bind
     * @return this {@link Statement}
     * @throws IndexOutOfBoundsException if the parameter index is out of range
     */
    default Statement bind(int index, char value) {
        return bind(index, (Character) value);
    }

    /**
     * Bind a value to an index.  Indexes are zero-based.
     *
     * @param index the index to bind to
     * @param value the value to bind
     * @return this {@link Statement}
     * @throws IndexOutOfBoundsException if the parameter index is out of range
     */
    default Statement bind(int index, double value) {
        return bind(index, (Double) value);
    }

    /**
     * Bind a value to an index.  Indexes are zero-based.
     *
     * @param index the index to bind to
     * @param value the value to bind
     * @return this {@link Statement}
     * @throws IndexOutOfBoundsException if the parameter index is out of range
     */
    default Statement bind(int index, float value) {
        return bind(index, (Float) value);
    }

    /**
     * Bind a value to an index.  Indexes are zero-based.
     *
     * @param index the index to bind to
     * @param value the value to bind
     * @return this {@link Statement}
     * @throws IndexOutOfBoundsException if the parameter index is out of range
     */
    default Statement bind(int index, int value) {
        return bind(index, (Integer) value);
    }

    /**
     * Bind a value to an index.  Indexes are zero-based.
     *
     * @param index the index to bind to
     * @param value the value to bind
     * @return this {@link Statement}
     * @throws IndexOutOfBoundsException if the parameter index is out of range
     */
    default Statement bind(int index, long value) {
        return bind(index, (Long) value);
    }

    /**
     * Bind a value to an index.  Indexes are zero-based.
     *
     * @param index the index to bind to
     * @param value the value to bind
     * @return this {@link Statement}
     * @throws IndexOutOfBoundsException if the parameter index is out of range
     */
    default Statement bind(int index, short value) {
        return bind(index, (Short) value);
    }

    /**
     * Bind a {@code null} value.
     *
     * @param identifier the identifier to bind to
     * @param type       the type of null value
     * @return this {@link Statement}
     * @throws IllegalArgumentException if {@code identifier} or {@code type} is {@code null}
     * @deprecated Use {@link #bindNull(String, Class)} instead
     */
    @Deprecated
    default Statement bindNull(Object identifier, Class<?> type) {
        if (identifier instanceof Integer) {
            return bindNull(((Integer) identifier).intValue(), type);
        }

        return bindNull((String) identifier, type);
    }

    /**
     * Bind a {@code null} value.
     *
     * @param index the index to bind to
     * @param type  the type of null value
     * @return this {@link Statement}
     * @throws IllegalArgumentException  if {@code type} is {@code null}
     * @throws IndexOutOfBoundsException if the parameter index is out of range
     */
    Statement bindNull(int index, Class<?> type);

    /**
     * Bind a {@code null} value.  The default implementation of this method calls {@link #bindNull(Object, Class)} to allow SPI change in a less-breaking way.
     *
     * @param name the name of identifier to bind to
     * @param type the type of null value
     * @return this {@link Statement}
     * @throws IllegalArgumentException if {@code name} or {@code type} is {@code null}
     */
    default Statement bindNull(String name, Class<?> type) {
        return bindNull((Object) name, type);
    }

    /**
     * Executes one or more SQL statements and returns the {@link Result}s.
     *
     * @return the {@link Result}s, returned by each statement
     * @throws IllegalStateException if the statement is parametrized and not all parameter values are provided
     */
    Publisher<? extends Result> execute();

    /**
     * Configures {@link Statement} to return the generated values from any rows created by this {@link Statement} in the {@link Result} returned from {@link #execute()}.  If no columns are specified,
     * implementations are free to choose which columns will be returned.  If called multiple times, only the columns requested in the final invocation will be returned.
     * <p>
     * The default implementation of this method is a no op.
     *
     * @param columns the names of the columns to return
     * @return this {@code Statement}
     * @throws IllegalArgumentException if {@code columns}, or any item in {@code columns} is {@code null}
     */
    default Statement returnGeneratedValues(String... columns) {
        Assert.requireNonNull(columns, "columns must not be null");
        return this;
    }

    /**
     * Configures {@link Statement} to retrieve a fixed number of rows when fetching results from a query instead deriving fetch size from back pressure.  If called multiple times, only the fetch
     * size configured in the final invocation will be applied.  If the value specified is zero, then the hint is ignored.
     * <p>
     * The default implementation of this method is a no op and the default value is zero.
     *
     * @param rows the number of rows to fetch
     * @return this {@code Statement}
     */
    default Statement fetchSize(int rows) {
        return this;
    }

}
