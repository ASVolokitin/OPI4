package ru.lavrent.weblab3.beans;

public interface PointCounterMBean {

    long getMisses();

    long getSuccessHits();

    long getTotalHitAmount();
}
