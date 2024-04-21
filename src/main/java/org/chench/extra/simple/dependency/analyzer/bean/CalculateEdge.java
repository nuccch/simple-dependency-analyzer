package org.chench.extra.simple.dependency.analyzer.bean;

/**
 * @author chench
 * @date 2024.04.21
 */
public class CalculateEdge extends Edge {
    private boolean checked = false; // 标志该节点是否被检查过

    public CalculateEdge() {
        super();
    }

    public CalculateEdge(String start, String end) {
        super(start, end);
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}