package de.fhbielefeld.pmdungeon.game.characters;

import com.badlogic.gdx.ai.pfa.GraphPath;
import de.fhbielefeld.pmdungeon.game.GameWorld;
import de.fhbielefeld.pmdungeon.game.dungeon.tiles.Tile;

public class AiInputComponent implements InputComponent {

    private final GameWorld gameWorld;

    public AiInputComponent(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }

    @Override
    public void update(Character character) {
        GraphPath<Tile> path = gameWorld.getDungeon().findPath(character.currentTile(), gameWorld.getHero().currentTile());

    }
}
