package org.chench.extra.simple.dependency.analyzer.bean;

/**
 * 两个节点组成的边
 * @author chench
 * @date 2024.04.20
 */
public class Edge {
    private String start;
    /** start依赖 */
    private String end;

    public Edge() {

    }

    public Edge(String start, String end) {
        this.start = start;
        this.end = end;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("[").append(start).append(",").append(end).append("]").toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Edge)) {
            return false;
        }

        Edge other = (Edge) obj;
        return this.start.equals(other.getStart()) && this.end.equals(other.getEnd());
    }
}