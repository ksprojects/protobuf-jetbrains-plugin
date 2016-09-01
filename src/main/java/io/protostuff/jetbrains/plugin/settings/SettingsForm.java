package io.protostuff.jetbrains.plugin.settings;


import com.google.common.collect.Lists;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.ui.CollectionListModel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Kostiantyn Shchepanovskyi
 */
@SuppressWarnings("WeakerAccess")
public class SettingsForm {

    private final CollectionListModel<String> includePathModel;
    private final List<String> includePathListList;
    private final Project project;
    private JPanel panel;
    private com.intellij.ui.components.JBList includePathList;
    private JButton addButton;
    private JButton removeButton;
    private JLabel includePathsLabel;

    @SuppressWarnings("unchecked")
    public SettingsForm(Project project, ProtobufSettings settings) {
        this.project = project;
        List<String> internalIncludePathList = new ArrayList<>();
        internalIncludePathList.addAll(settings.getIncludePaths());
        includePathListList = Collections.unmodifiableList(internalIncludePathList);
        includePathModel = new CollectionListModel<>(internalIncludePathList, true);
        includePathList.setModel(includePathModel);
        addButton.addActionListener(e -> {
            FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
            FileChooser.chooseFile(descriptor, this.project, null, selectedFolder -> {
                String path = selectedFolder.getPath();
                includePathModel.add(path);
            });
        });
        removeButton.addActionListener(e -> {
            int selectedIndex = includePathList.getSelectedIndex();
            if (selectedIndex != -1) {
                includePathModel.removeRow(selectedIndex);
            }
        });
    }

    public JPanel getPanel() {
        return panel;
    }

    public ProtobufSettings getSettings() {
        ProtobufSettings settings = new ProtobufSettings();
        settings.setIncludePaths(Lists.newArrayList(includePathListList));
        return settings;
    }

    public void reset(ProtobufSettings source) {
        includePathModel.removeAll();
        includePathModel.add(source.getIncludePaths());
    }

}
