package org.chench.extra.simple.dependency.analyzer;

import org.apache.commons.lang3.StringUtils;
import org.chench.extra.java.util.SimpleOSUtil;
import org.chench.extra.simple.dependency.analyzer.constant.CommonConstant;
import org.chench.extra.simple.dependency.analyzer.service.impl.ProjectServiceImpl;
import org.chench.extra.simple.dependency.analyzer.ui.JTextFieldHintListener;
import org.chench.extra.simple.dependency.analyzer.ui.ZPanel;
import org.chench.extra.simple.dependency.analyzer.util.AppUtil;
import org.chench.extra.simple.dependency.analyzer.util.ModuleHolder;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

/**
 * 图形化启动
 * @author chench
 * @date 2024.04.20
 */
public class UIAppBootstrap {
    final int[] x1 = {0};
    final int[] y1 = {0};
    ProjectServiceImpl projectService = new ProjectServiceImpl();
    public static void main(String[] args) {
        UIAppBootstrap app = new UIAppBootstrap();
        app.start();
    }

    private void start() {
        // 创建 JFrame 实例
        JFrame frame = new JFrame("简单依赖分析器");
        ImageIcon imageIcon = new ImageIcon(getClass().getClassLoader().getResource(CommonConstant.IMAGE_DEFAULT));
        frame.setIconImage(imageIcon.getImage().getScaledInstance(48, 48, Image.SCALE_DEFAULT));
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
        JTextField dirPathText = new JTextField(35);
        dirPathText.setPreferredSize(new Dimension(25, 25));
        dirPathText.setToolTipText("输入需要进行依赖分析的项目完整路径");
        dirPathText.addFocusListener(new JTextFieldHintListener(dirPathText, "输入需要进行依赖分析的项目完整路径"));
        panel.add(dirPathText);

        // 打开目录按钮
        JButton selectDirButton = new JButton("选择路径...");
        selectDirButton.setPreferredSize(new Dimension(90, 25));
        //selectDirButton.requestFocus();
        panel.add(selectDirButton);

        // 从数据库查询上一次保存的路径
        String dirPath = this.projectService.queryPath();
        if (StringUtils.isNotBlank(dirPath)) {
            dirPathText.setText(dirPath);
        }

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
        executeButton.setPreferredSize(new Dimension(80, 25));
        panel.add(executeButton);

        // 构建顺序
        JButton buildOrderButton = new JButton("构建顺序");
        buildOrderButton.setPreferredSize(new Dimension(80, 25));
        panel.add(buildOrderButton);
        panel.validate();

        framePanel.add(panel, BorderLayout.NORTH);
        framePanel.validate();

        // 展示依赖图
        ZPanel zPanel = new ZPanel();
        zPanel.setAutoscrolls(true);
        JScrollPane jScrollPane = new JScrollPane(zPanel);
        jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane.getHorizontalScrollBar().addAdjustmentListener(e -> SwingUtilities.invokeLater(() -> jScrollPane.updateUI()));
        jScrollPane.getVerticalScrollBar().addAdjustmentListener(e -> SwingUtilities.invokeLater(() -> jScrollPane.updateUI()));
        framePanel.add(jScrollPane, BorderLayout.CENTER);

        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                // 鼠标释放
                x1[0] = e.getX();
                y1[0] = e.getY();
            }
        };

        MouseMotionListener mml = new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // 按住鼠标拖动
                int x = e.getX() - x1[0];
                int y = e.getY() - y1[0];
                zPanel.setLocation(zPanel.getX()+x, zPanel.getY()+y);
                x1[0] = e.getX();
                y1[0] = e.getY();

                Rectangle r = new Rectangle(e.getX(), e.getY(), 1, 1);
                ((JPanel) e.getSource()).scrollRectToVisible(r);
                framePanel.updateUI();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                // 鼠标移动
                x1[0] = e.getX();
                y1[0] = e.getY();
            }
        };

        // 点击"选择目录"按钮
        selectDirButton.addActionListener(e -> {
            String oldProjectPath = this.projectService.queryPath();
            if (StringUtils.isNotBlank(oldProjectPath)) {
                oldProjectPath = new File(oldProjectPath).getParentFile().getAbsolutePath();
            }
            String dir = StringUtils.isBlank(oldProjectPath) ? SimpleOSUtil.getUserHome() : oldProjectPath;
            JFileChooser fileChooser = new JFileChooser(dir);
            fileChooser.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    // 只能选择目录，不能选择文件
                    return f.isDirectory();
                }

                @Override
                public String getDescription() {
                    return null;
                }
            });
            // 只选择目录
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = fileChooser.showOpenDialog(frame);
            if (JFileChooser.APPROVE_OPTION == result) {
                File file = fileChooser.getSelectedFile();
                if (file.isFile()) {
                    return;
                }
                dirPathText.setText(file.getAbsolutePath());
                dirPathText.requestFocus();
            }
        });

        // 点击“开始分析”按钮
        executeButton.addActionListener(e -> {
            String dir = dirPathText.getText();
            if (StringUtils.isBlank(dir)) {
                return;
            }

            // 将项目路径保存起来
            this.projectService.savePath(dir);

            String[] ignores = ignoreDirText.getText().split(CommonConstant.SPLIT_CHAR);
            String output = new AppExecutor().execute(dir, ignores);
            if (CommonConstant.IMAGE_EMPTY.equals(output)) {
                zPanel.setCursor(Cursor.getDefaultCursor());
                zPanel.removeMouseMotionListener(mml);
                zPanel.removeMouseListener(ma);
                JOptionPane.showMessageDialog(frame, "当前项目路径不存在模块依赖关系！", "提示", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // 设置鼠标为小手形状
                zPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                zPanel.addMouseMotionListener(mml);
                zPanel.addMouseListener(ma);
            }
            SwingUtilities.invokeLater(() -> {
                zPanel.setImagePath(output);
                jScrollPane.updateUI();
            });
        });

        // 点击“构建顺序”按钮
        buildOrderButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser(SimpleOSUtil.getUserHome());
            // 设置默认文件名
            fileChooser.setSelectedFile(new File(ModuleHolder.getBuildOrderFileName()));
            int result = fileChooser.showSaveDialog(frame);
            try {
                if (JFileChooser.APPROVE_OPTION == result) {
                    File file = fileChooser.getSelectedFile();
                    AppUtil.saveBuildOrderFile(file);
                    JOptionPane.showMessageDialog(frame, "保存文件成功", "提示", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, "保存文件失败", "提示", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void finalSetting(JFrame frame) {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        frame.setSize(screenWidth/2+280, screenHeight/2+150);
        int frameH = frame.getHeight();
        int frameW = frame.getWidth();
        frame.setLocation((screenWidth - frameW) / 2, (screenHeight - frameH) / 2);
        frame.setVisible(true);
        frame.setResizable(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.requestFocus();
    }
}