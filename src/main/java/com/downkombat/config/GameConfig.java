package com.downkombat.config;

/**
 * Configuración global del juego.
 * Todas las constantes del motor y combate están aquí.
 */
public class GameConfig {


    // PANTALLA

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;


    // POSICIÓN DEL SUELO

    public static final int GROUND_Y = 650;


    //  JUGADOR

    public static final int PLAYER_MAX_HEALTH = 200;

    public static final double PLAYER_SPEED = 5;


    // ATAQUES

    public static final int ATTACK_DAMAGE = 4;

    public static final int ATTACK_RANGE = 180;

    public static final int KNOCKBACK_FORCE = 35;


    // TIEMPOS DE COMBATE (ms)

    // tiempo entre puñetazos
    public static final int ATTACK_COOLDOWN = 450;

    // tiempo entre especiales
    public static final int SPECIAL_COOLDOWN = 5000;

    // tiempo de invulnerabilidad tras recibir golpe
    public static final int HITSTUN = 300;

    // freeze frame cuando impacta un golpe
    public static final int HIT_FREEZE = 60;

    // flash visual cuando recibes daño
    public static final int DAMAGE_FLASH = 120;


    // FINAL DE RONDA

    // porcentaje de vida para modo crítico
    public static final double CRITICAL_HEALTH_THRESHOLD = 0.2;

}
