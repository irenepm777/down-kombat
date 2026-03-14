# down-kombat
DOWN KOMBAT es un videojuego de lucha 2D en tiempo real desarrollado en Java + JavaFX como proyecto final de DAW, aplicando modelado UML, control de versiones con GitFlow y análisis de calidad del código.
diagrama de clases
```mermaid
classDiagram

class Fighter {
  -name : String
  -health : int
  +attack(a: Attack)
  +receiveDamage(dmg: int)
}

class Attack {
  +execute(attacker: Fighter, target: Fighter)
}

class DefaultPunch
class SpecialAttack
class MolarAttack
class NepalShieldAttack
class TransformationAttack
class UberFailAttack

Attack <|-- DefaultPunch
Attack <|-- SpecialAttack
SpecialAttack <|-- MolarAttack
SpecialAttack <|-- NepalShieldAttack
SpecialAttack <|-- TransformationAttack
SpecialAttack <|-- UberFailAttack

class Projectile {
  -speed : int
  +move()
}

class ProjectileManager {
  -projectiles : List~Projectile~
  +update()
}

class Car
class CarManager

Projectile <|-- Car
ProjectileManager --> Projectile
CarManager --> Car

class GameScene {
  +update()
  +render()
}

class FightManager {
  -fighter1 : Fighter
  -fighter2 : Fighter
  +resolveTurn()
}

class CharacterSelectScene
class GameState
class SoundManager {
  +play(sound: String)
}

FightManager --> Fighter
GameScene --> FightManager
GameScene --> SoundManager
CharacterSelectScene --> FighterFactory


```
Diagrama de Casos de Uso

```mermaid
flowchart LR

%% Actores
Jugador([Jugador])
Motor(["Motor del Juego"])
Audio(["Sistema de Audio"])

%% Sistema
subgraph DownKombat
  IniciarPartida([Iniciar Partida])
  SeleccionarPersonaje([Seleccionar Personaje])
  Atacar([Atacar])
  AtaqueEspecial([Ataque Especial])
  GuardarProgreso([Guardar Progreso])
  PausarJuego([Pausar Juego])
  ReproducirSonido([Reproducir Sonido])
end

%% Relaciones actor - casos de uso
Jugador --> IniciarPartida
Jugador --> SeleccionarPersonaje
Jugador --> Atacar
Jugador --> AtaqueEspecial
Jugador --> GuardarProgreso
Jugador --> PausarJuego

Motor --> Atacar
Motor --> AtaqueEspecial

Audio --> ReproducirSonido

%% "include" y "extend" simulados
Atacar -. include .-> ReproducirSonido
AtaqueEspecial -. extend .-> Atacar

```

DIAGRAMA DE SECUENCIA

```mermaid
sequenceDiagram
actor Jugador
participant GameScene
participant FightManager
participant Fighter as Atacante
participant Fighter2 as Objetivo
participant SoundManager

Jugador ->> GameScene: pulsarBotonAtacar()
GameScene ->> FightManager: procesarAtaque()
FightManager ->> Atacante: getAttack()
Atacante ->> Objetivo: execute(ataque)
Objetivo ->> Objetivo: receiveDamage()
FightManager ->> SoundManager: play("hit_sound")

```
