package org.chench.extra.simple.dependency.analyzer.util;

import org.chench.extra.simple.dependency.analyzer.bean.CalculateEdge;
import org.chench.extra.simple.dependency.analyzer.bean.Edge;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chench
 * @date 2024.04.21
 */
public abstract class EdgeUtil {
    public static List<CalculateEdge> wrapCalculateEdge(List<Edge> edges) {
        return edges.stream().map(edge -> new CalculateEdge(edge.getStart(), edge.getEnd()))
                .collect(Collectors.toList());
    }
}