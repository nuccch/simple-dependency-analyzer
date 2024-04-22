package org.chench.extra.simple.dependency.analyzer;

import org.apache.commons.lang3.StringUtils;
import org.chench.extra.simple.dependency.analyzer.constant.CommonConstant;
import org.chench.extra.simple.dependency.analyzer.ui.JTextFieldHintListener;
import org.chench.extra.simple.dependency.analyzer.ui.ZPanel;

import javax.swing.*;
import java.awt.*;

/**
 * 图形化启动
 * @author chench
 * @date 2024.04.20
 */
public class UIAppBootstrap {
    public static void main(String[] args) {
        UIAppBootstrap app = new UIAppBootstrap();
        app.start();
    }

    private void start() {
        // 创建 JFrame 实例
        JFrame frame = new JFrame("简单依赖分析器");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        frame.add(panel, BorderLayout.CENTER);

        // 布局组件
        placeComponents(frame, panel);

        // 全局设置
        finalSetting(frame);
    }

    private void placeComponents(JFrame frame, JPanel framePanel) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        // 项目路径
        JLabel dirPathLabel = new JLabel("项目路径:");
        panel.add(dirPathLabel);
        JTextField dirPathText = new JTextField(50);
        dirPathText.setPreferredSize(new Dimension(25, 25));
        dirPathText.setToolTipText("输入需要进行依赖分析的项目完整路径");
        dirPathText.addFocusListener(new JTextFieldHintListener(dirPathText, "输入需要进行依赖分析的项目完整路径"));
        panel.add(dirPathText);

        // 忽略目录名
        JLabel ignoreDirLabel = new JLabel("忽略目录名:");
        panel.add(ignoreDirLabel);
        JTextField ignoreDirText = new JTextField(20);
        ignoreDirText.setPreferredSize(new Dimension(25, 25));
        ignoreDirText.setToolTipText("多个名称使用英文逗号分隔");
        ignoreDirText.addFocusListener(new JTextFieldHintListener(ignoreDirText, "多个名称使用英文逗号分隔"));
        panel.add(ignoreDirText);

        // 开始分析按钮
        JButton executeButton = new JButton("开始分析");
        executeButton.setPreferredSize(new Dimension(100, 25));
        panel.add(executeButton);
        panel.validate();

        framePanel.add(panel, BorderLayout.NORTH);
        framePanel.validate();

        // 展示依赖图
        ZPanel zPanel = new ZPanel();
        JScrollPane jScrollPane = new JScrollPane(zPanel);
        jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane.getHorizontalScrollBar().addAdjustmentListener(e -> SwingUtilities.invokeLater(() -> jScrollPane.updateUI()));
        jScrollPane.getVerticalScrollBar().addAdjustmentListener(e -> SwingUtilities.invokeLater(() -> jScrollPane.updateUI()));
        framePanel.add(jScrollPane, BorderLayout.CENTER);

        executeButton.addActionListener(e -> {
            String dir = dirPathText.getText();
            if (StringUtils.isBlank(dir)) {
                return;
            }

            String[] ignores = ignoreDirText.getText().split(CommonConstant.SPLIT_CHAR);
            String output = new AppExecutor().execute(dir, ignores);
            SwingUtilities.invokeLater(() -> {
                zPanel.setImagePath(output);
            });
        });
    }

    private void finalSetting(JFrame frame) {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        frame.setSize(screenWidth/2+200, screenHeight/2+100);
        int frameH = frame.getHeight();
        int frameW = frame.getWidth();
        frame.setLocation((screenWidth - frameW) / 2, (screenHeight - frameH) / 2);
        frame.setVisible(true);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}