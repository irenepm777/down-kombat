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

