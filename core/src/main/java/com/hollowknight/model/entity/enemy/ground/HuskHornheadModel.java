package com.hollowknight.model.entity.enemy.ground;

import com.hollowknight.model.entity.enemy.EnemyModel;


public class HuskHornheadModel extends EnemyModel {

    public enum HHState { PATROL, REST, CHARGE }

    private HHState hhState     = HHState.PATROL;
    private float   restTimer   = 0f;
    private float   chargeSpeed = 400f;


    private static final float PATROL_DURATION = 2.5f;
    private static final float REST_DURATION   = 1.2f;

    public HuskHornheadModel(float x, float y) {
        super(x, y, 36f, 36f, 40, 110f, EnemyType.HUSK_HORNHEAD);
        this.detectionRange = 200f;
    }

    public HHState getHHState()           { return hhState; }
    public void    setHHState(HHState s)  { hhState = s; }
    public float   getRestTimer()         { return restTimer; }
    public void    setRestTimer(float t)  { restTimer = t; }
    public float   getChargeSpeed()       { return chargeSpeed; }
    public float   getPatrolDuration()    { return PATROL_DURATION; }
    public float   getRestDuration()      { return REST_DURATION; }
}
