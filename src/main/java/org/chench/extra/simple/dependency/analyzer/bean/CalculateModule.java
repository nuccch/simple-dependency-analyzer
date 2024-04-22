package org.chench.extra.simple.dependency.analyzer.bean;

/**
 * @author chench
 * @date 2024.04.22
 */
public class CalculateModule extends Module implements Comparable {
    private int weight = 0;
    private int number = 0;

    public CalculateModule() {
        super();
    }

    public CalculateModule(String name, String pom, int weight) {
        super(name, pom);
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof CalculateModule)) {
            return 1;
        }
        CalculateModule c = (CalculateModule)o;
        return c.getWeight() - this.getWeight();
    }
}
