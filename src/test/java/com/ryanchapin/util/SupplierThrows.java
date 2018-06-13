package com.ryanchapin.util;

@FunctionalInterface
public interface SupplierThrows<T> {

   public T get() throws Exception;

}
