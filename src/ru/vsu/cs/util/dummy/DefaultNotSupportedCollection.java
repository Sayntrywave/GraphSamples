/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.vsu.cs.util.dummy;

import java.util.Collection;
import java.util.Iterator;

/**
 * Интерфейс, реализующий системный Collection&lt;T&gt;, в виде методов с
 * исключениями "Not supported yet."
 *
 * @param <T>
 */
public interface DefaultNotSupportedCollection<T> extends Collection<T> {

    @Override
    default int size() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    default boolean isEmpty() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    default boolean contains(Object o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    default Iterator<T> iterator() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    default Object[] toArray() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    default <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    default boolean add(T e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    default boolean remove(Object o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    default boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    default boolean addAll(Collection<? extends T> c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    default boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    default boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    default void clear() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
