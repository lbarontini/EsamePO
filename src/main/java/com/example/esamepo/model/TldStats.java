package com.example.esamepo.model;

/**
 * model class for json output of the /stats route
 * @see com.example.esamepo.controller.Endpoint
 */
public class TldStats {
    TldDescription min, max;
    float average;

    public TldStats(TldDescription min, TldDescription max, float average) {
        this.min = min;
        this.max = max;
        this.average = average;
    }

    public TldDescription getMin() {
        return min;
    }

    public void setMin(TldDescription min) {
        this.min = min;
    }

    public TldDescription getMax() {
        return max;
    }

    public void setMax(TldDescription max) {
        this.max = max;
    }

    public float getAverage() {
        return average;
    }

    public void setAverage(float average) {
        this.average = average;
    }
}
