package wtf.n1zamu.utility;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map.Entry;

public class RandomizationUtility<T> {
    private HashMap<T, Double> distribution = new HashMap<>();
    private double distSum;
    private final SecureRandom random = new SecureRandom();

    public void addValue(T value, double distribution) {
        if (this.distribution.get(value) != null) {
            this.distSum -= this.distribution.get(value);
        }

        this.distribution.put(value, distribution);
        this.distSum += distribution;
    }

    public T getRandomValue() {
        double rand = this.random.nextDouble();
        double ratio = 1.0 / this.distSum;
        double tempDist = 0.0;

        for (Entry<T, Double> entry : this.distribution.entrySet()) {
            T i = entry.getKey();
            if (rand / ratio <= (tempDist += entry.getValue())) {
                return i;
            }
        }

        throw new IllegalStateException("[NCases] Ошибка рандомизатора! Общий шанс больше 100!");
    }
}
