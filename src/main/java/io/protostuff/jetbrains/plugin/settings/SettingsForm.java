package io.protostuff.jetbrains.plugin.settings;

import com.google.common.collect.Lists;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.ui.CollectionListModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jetbrains.annotations.Nullable;

/**
 * Plugin settings form.
 *
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

    /**
     * Create new {@link SettingsForm} instance.
     *
     * @param settings is null if settings dialog runs without a project.
     */
    @SuppressWarnings("unchecked")
    public SettingsForm(@Nullable Project project, @Nullable ProtobufSettings settings) {
        this.project = project;
        List<String> internalIncludePathList = new ArrayList<>();
        if (settings != null) {
            internalIncludePathList.addAll(settings.getIncludePaths());
        }
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
        if (settings == null) {
            addButton.setEnabled(false);
            removeButton.setEnabled(false);
        }
    }

    public JPanel getPanel() {
        return panel;
    }

    /**
     * Returns a copy of settings contained in the form.
     */
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
