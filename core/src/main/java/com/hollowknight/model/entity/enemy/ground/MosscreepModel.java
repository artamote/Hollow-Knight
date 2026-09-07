package com.hollowknight.model.entity.enemy.ground;

import com.hollowknight.model.entity.enemy.EnemyModel;


public class MosscreepModel extends EnemyModel {
    public MosscreepModel(float x, float y) {
        super(x, y, 30f, 24f, 12, 70f, EnemyType.MOSSCREEP);
    }
}