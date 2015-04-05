package com.example.daniel.myapplication;

/**
 * Created by Daniel on 4/5/2015.
 */
public class BulletBehavior extends ShootBehavior {
    public BulletBehavior(MainGamePanel m){
        super(m);
    }
    @Override
    public void shoot(float x, float y){
        if(cooldown <= 0){
            cooldown = 10;
            m.addShot(new Shot(null, x - 10, y - 30f,-1));
        }else{
            cooldown--;
        }
    }
}
