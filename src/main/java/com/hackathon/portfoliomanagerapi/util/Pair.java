package com.hackathon.portfoliomanagerapi.util;

import lombok.Getter;

@Getter
public class Pair<K, V> {
    K key;
    V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return "{" + key + ": " + value + "}";
    }
}