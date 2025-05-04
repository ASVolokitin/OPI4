package ru.lavrent.weblab3.beans;

import javax.management.NotificationBroadcasterSupport;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

@Named("areaCalculatorBean")
@ApplicationScoped
public class AreaCalculator extends NotificationBroadcasterSupport implements AreaCalculatorMBean {

    private double area;

    public synchronized double getArea() {
        return area;
    }

    @Override
    public synchronized double calculateArea(float r) {
        double area = 1.5 * (r * r / 2) + (Math.PI * r / 16);
        this.area = area;
        return area;
    }

    // public synchronized void printArea() {
    // System.out.println(String.format("Current area = %f", calculateArea()));
    // }
}
