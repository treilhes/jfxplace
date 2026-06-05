/*
 * Copyright (c) 2016, 2025, Gluon and/or its affiliates.
 * Copyright (c) 2021, 2025, Pascal Treilhes and/or its affiliates.
 * Copyright (c) 2012, 2014, Oracle and/or its affiliates.
 * All rights reserved. Use is subject to license terms.
 *
 * This file is available and licensed under the following license:
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  - Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  - Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the distribution.
 *  - Neither the name of Oracle Corporation and Gluon nor the names of its
 *    contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.treilhes.jfxplace.core.api.ui.dialog;

import java.io.IOException;
import java.net.URL;

import com.treilhes.emc4j.boot.api.platform.EmcPlatform;
import com.treilhes.jfxplace.core.api.instance.ApplicationInstance;
import com.treilhes.jfxplace.core.api.ui.controller.AbstractFxmlWindowController;
import com.treilhes.jfxplace.core.api.ui.dialog.ModalWindow.ButtonID;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Window;

/**
 *
 *
 */
@Deprecated
public abstract class AbstractModalDialog extends AbstractFxmlWindowController {

    private final EmcPlatform appsPlatform;
    private final Window owner;
    private final URL contentFxmlURL;
    private Parent contentRoot;
    private ButtonID clickedButtonID;
    private boolean showDefaultButton;
    private ButtonID defaultButtonID = ButtonID.OK;
    private boolean focusTraversableButtons;

    /*
     * The following members should be considered as 'private'. They are 'protected'
     * only to please the FXML loader.
     */
    @Deprecated
    @FXML
    protected StackPane contentPane;
    @Deprecated
    @FXML
    protected Button okButton;
    @Deprecated
    @FXML
    protected Button cancelButton;
    @Deprecated
    @FXML
    protected Button actionButton;
    @Deprecated
    @FXML
    protected Pane okParent;
    @Deprecated
    @FXML
    protected Pane actionParent;
    @Deprecated
    @FXML
    protected ImageView imageView;
    @Deprecated
    @FXML
    protected Pane imageViewParent;

    /*
     * Public
     */

    // @formatter:off
    @Deprecated
    public AbstractModalDialog(
            ApplicationInstance instance,
            EmcPlatform appsPlatform,
            URL contentFxmlURL,
            Window owner) {
     // @formatter:on
        super(instance, getContainerFxmlURL(appsPlatform));
        this.appsPlatform = appsPlatform;
        this.owner = owner;
        this.contentFxmlURL = contentFxmlURL;
        assert contentFxmlURL != null;
    }

    @Deprecated
    public Parent getContentRoot() {

        if (contentRoot == null) {
            final FXMLLoader loader = new FXMLLoader();

            loader.setController(this);
            loader.setLocation(contentFxmlURL);
            loader.setResources(getI18n().getBundle());
            loader.setClassLoader(getClass().getClassLoader());
            try {
                contentRoot = (Parent) loader.load();
                controllerDidLoadContentFxml();
            } catch (IOException x) {
                contentRoot = null;
                throw new RuntimeException("Failed to load " + contentFxmlURL.getFile(), x); // NOCHECK
            }
        }

        return contentRoot;
    }

    @Deprecated
    public final ButtonID showAndWait() {
//        center();
        clickedButtonID = ButtonID.CANCEL;
        getStage().showAndWait();
        return clickedButtonID;
    }

    @Deprecated
    public final ButtonID show() {
//      center();
      clickedButtonID = ButtonID.CANCEL;
      getStage().show();
      return clickedButtonID;
  }

    @Deprecated
    public String getTitle() {
        return getStage().getTitle();
    }

    @Deprecated
    public void setTitle(String title) {
        getStage().setTitle(title);
    }

    @Deprecated
    public String getOKButtonTitle() {
        return getOKButton().getText();
    }

    @Deprecated
    public void setOKButtonTitle(String title) {
        getOKButton().setText(title);
    }

    @Deprecated
    public String getCancelButtonTitle() {
        return getCancelButton().getText();
    }

    @Deprecated
    public void setCancelButtonTitle(String title) {
        getCancelButton().setText(title);
    }

    @Deprecated
    public String getActionButtonTitle() {
        return getActionButton().getText();
    }

    @Deprecated
    public void setActionButtonTitle(String title) {
        getActionButton().setText(title);
    }

    @Deprecated
    public boolean isOKButtonVisible() {
        return getOKButton().getParent() != null;
    }

    @Deprecated
    public void setOKButtonVisible(boolean visible) {
        if (visible != isOKButtonVisible()) {
            if (visible) {
                assert getOKButton().getParent() == null;
                getOKParent().getChildren().add(getOKButton());
            } else {
                assert getOKButton().getParent() == getOKParent();
                getOKParent().getChildren().remove(getOKButton());
            }
        }
    }

    @Deprecated
    public boolean isActionButtonVisible() {
        return getActionButton().getParent() != null;
    }

    @Deprecated
    public void setActionButtonVisible(boolean visible) {
        if (visible != isActionButtonVisible()) {
            if (visible) {
                assert getActionButton().getParent() == null;
                getActionParent().getChildren().add(getActionButton());
            } else {
                assert getActionButton().getParent() == getActionParent();
                getActionParent().getChildren().remove(getActionButton());
            }
        }
    }

    @Deprecated
    public void setOKButtonDisable(boolean disable) {
        getOKButton().setDisable(disable);
    }

    @Deprecated
    public void setActionButtonDisable(boolean disable) {
        getActionButton().setDisable(disable);
    }

    @Deprecated
    public void setShowDefaultButton(boolean show) {
        showDefaultButton = show;
        updateButtonState();
    }

    @Deprecated
    public void setDefaultButtonID(ButtonID buttonID) {
        defaultButtonID = buttonID;
        updateButtonState();
    }

    @Deprecated
    public boolean isImageViewVisible() {
        return getImageView().getParent() != null;
    }

    @Deprecated
    public void setImageViewVisible(boolean visible) {
        if (visible != isImageViewVisible()) {
            if (visible) {
                assert getImageView().getParent() == null;
                imageViewParent.getChildren().add(getImageView());
            } else {
                assert getImageView().getParent() == imageViewParent;
                imageViewParent.getChildren().remove(getImageView());
            }
        }
    }

    @Deprecated
    public Image getImageViewImage() {
        return getImageView().getImage();
    }

    @Deprecated
    public void setImageViewImage(Image image) {
        getImageView().setImage(image);
    }

    // On Mac the FXML defines the 3 buttons as non focus traversable.
    // However for complex dialogs such a Preferences, Code Skeleton and
    // Preview Background Color we'd better have them focus traversable hence
    // this method.
    @Deprecated
    public void setButtonsFocusTraversable() {
        if (appsPlatform.isMac()) {
            getOKButton().setFocusTraversable(true);
            getCancelButton().setFocusTraversable(true);
            getActionButton().setFocusTraversable(true);
            focusTraversableButtons = true;
        }
    }

    /*
     * To be subclassed
     */

    @Deprecated
    protected abstract void controllerDidLoadContentFxml();

    /*
     * To be subclassed #2
     */
    @Deprecated
    @FXML
    protected abstract void okButtonPressed(ActionEvent e);

    @Deprecated
    @FXML
    protected abstract void cancelButtonPressed(ActionEvent e);

    @Deprecated
    @FXML
    protected abstract void actionButtonPressed(ActionEvent e);

    /*
     * AbstractWindowController
     */

    @Deprecated
    @Override
    protected void controllerDidCreateStage() {
        if (this.owner == null) {
            // Dialog will be appliation modal
            getStage().initModality(Modality.APPLICATION_MODAL);
        } else {
            // Dialog will be window modal
            getStage().initOwner(this.owner);
            getStage().initModality(Modality.WINDOW_MODAL);
        }
    }

    /*
     * AbstractFxmlWindowController
     */

    @Deprecated
    @Override
    public void controllerDidLoadFxml() {
        assert contentPane != null;
        assert okButton != null;
        assert cancelButton != null;
        assert actionButton != null;
        assert imageView != null;
        assert okParent != null;
        assert actionParent != null;
        assert imageViewParent != null;
        assert okButton.getParent() == okParent;
        assert actionButton.getParent() == actionParent;
        assert imageView.getParent() == imageViewParent;

        final EventHandler<ActionEvent> callUpdateButtonID = e -> updateButtonID(e);
        okButton.addEventHandler(ActionEvent.ACTION, callUpdateButtonID);
        cancelButton.addEventHandler(ActionEvent.ACTION, callUpdateButtonID);
        actionButton.addEventHandler(ActionEvent.ACTION, callUpdateButtonID);

        contentPane.getChildren().add(getContentRoot());

        // By default, action button and image view are not visible
        setActionButtonVisible(false);
        setImageViewVisible(false);

        // Setup default state and focus
        updateButtonState();

        // Size everything
        getStage().sizeToScene();
    }

    @Deprecated
    @Override
    public void onCloseRequest() {
        // Closing the window is equivalent to clicking the Cancel button
        cancelButtonPressed(null);
    }

    @Deprecated
    @Override
    public void onFocus() {
    }

    /*
     * Private
     */
    private static URL getContainerFxmlURL(EmcPlatform appsPlatform) {
        final String fxmlName;

        if (appsPlatform.isWindows()) {
            fxmlName = "AbstractModalDialogW.fxml";
        } else {
            fxmlName = "AbstractModalDialogM.fxml";
        }

        return AbstractModalDialog.class.getResource(fxmlName);
    }

    private Button getOKButton() {
        getRoot(); // Force fxml loading
        return okButton;
    }

    private Button getCancelButton() {
        getRoot(); // Force fxml loading
        return cancelButton;
    }

    private Button getActionButton() {
        getRoot(); // Force fxml loading
        return actionButton;
    }

    private Pane getOKParent() {
        getRoot(); // Force fxml loading
        return okParent;
    }

    private Pane getActionParent() {
        getRoot(); // Force fxml loading
        return actionParent;
    }

    private ImageView getImageView() {
        getRoot(); // Force fxml loading
        return imageView;
    }

    private void updateButtonID(ActionEvent t) {
        assert t != null;

        final Object source = t.getSource();
        if (source == getCancelButton()) {
            clickedButtonID = ButtonID.CANCEL;
        } else if (source == getOKButton()) {
            clickedButtonID = ButtonID.OK;
        } else if (source == getActionButton()) {
            clickedButtonID = ButtonID.ACTION;
        } else {
            throw new IllegalArgumentException("Bug"); // NOCHECK
        }
    }

    private void updateButtonState() {
        getOKButton().setDefaultButton(false);
        getCancelButton().setDefaultButton(false);
        getActionButton().setDefaultButton(false);

        // To stick to OS specific "habits" we set a default button on Mac as on
        // Win and Linux we use focus to mark a button as the default one (then
        // you can tab navigate from a button to another, something which is
        // disabled on Mac.
        // However on Mac and for complex dialogs (Preferences, Code Skeleton,
        // Background Color) we apply the Win/Linux scheme: buttons are focus
        // traversable and there's no default one. The user needs to press Space
        // to take action with the focused button. We take this approach because
        // complex dialogs contain editable field that take focus and that
        // interferes with a button set as default one.
        // See DTL-5333.
        if (showDefaultButton) {
            switch (defaultButtonID) {
            case OK:
                if (appsPlatform.isMac() && !focusTraversableButtons) {
                    getOKButton().setDefaultButton(true);
                } else {
                    getOKButton().requestFocus();
                }
                break;
            case CANCEL:
                if (appsPlatform.isMac() && !focusTraversableButtons) {
                    getCancelButton().setDefaultButton(true);
                } else {
                    getCancelButton().requestFocus();
                }
                break;
            case ACTION:
                if (appsPlatform.isMac() && !focusTraversableButtons) {
                    getActionButton().setDefaultButton(true);
                } else {
                    getActionButton().requestFocus();
                }
                break;
            }
        }
    }
}
