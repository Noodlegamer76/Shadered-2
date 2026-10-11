package com.noodlegamer76.shadered.client.renderer.assimp;

import com.noodlegamer76.shadered.client.assimp.AssimpModel;
import com.noodlegamer76.shadered.client.assimp.anim.Animator;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public class RenderableModel {
    private final List<AssimpModel> model = new ArrayList<>();
    public Matrix4f modelMatrix;
    public Animator animator;
    private final boolean viewMatrix;

    public RenderableModel() {
        this(true);
    }

    public RenderableModel(boolean viewMatrix) {
        this.viewMatrix = viewMatrix;
    }

    public boolean shouldUseViewMatrix() {
        return viewMatrix;
    }

    public void add(AssimpModel model) {
        this.model.add(model);
    }

    public List<AssimpModel> getModel() {
        return model;
    }
}
