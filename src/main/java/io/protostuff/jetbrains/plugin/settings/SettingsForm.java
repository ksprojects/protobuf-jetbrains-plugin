package io.protostuff.jetbrains.plugin.settings;


import com.google.common.collect.Lists;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.CollectionListModel;
import com.intellij.util.Consumer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Kostiantyn Shchepanovskyi
 */
@SuppressWarnings("WeakerAccess")
public class SettingsForm {

    private final CollectionListModel<String> includePathModel;
    private final Project project;
    private JPanel panel;
    private com.intellij.ui.components.JBList includePathList;
    private JButton addButton;
    private JButton removeButton;
    private JLabel includePathsLabel;

    @SuppressWarnings("unchecked")
    public SettingsForm(Project project, ProtobufSettings settings) {
        this.project = project;
        includePathModel = new CollectionListModel<String>(settings.getIncludePaths());
        includePathList.setModel(includePathModel);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
                FileChooser.chooseFile(descriptor, SettingsForm.this.project, null, new Consumer<VirtualFile>() {
                    @Override
                    public void consume(VirtualFile selectedFolder) {
                        String path = selectedFolder.getPath();
                        includePathModel.add(path);
                    }
                });
            }
        });
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = includePathList.getSelectedIndex();
                if (selectedIndex != -1) {
                    includePathModel.removeRow(selectedIndex);
                }
            }
        });
    }

    public JPanel getPanel() {
        return panel;
    }

    public ProtobufSettings getSettings() {
        ProtobufSettings settings = new ProtobufSettings();
        settings.setIncludePaths(Lists.newArrayList(includePathModel.getItems()));
        return settings;
    }

    public void reset(ProtobufSettings source) {
        includePathModel.removeAll();
        includePathModel.add(source.getIncludePaths());
    }

}
