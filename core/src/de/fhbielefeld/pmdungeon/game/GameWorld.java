package de.fhbielefeld.pmdungeon.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import de.fhbielefeld.pmdungeon.game.characters.Character;
import de.fhbielefeld.pmdungeon.game.characters.*;
import de.fhbielefeld.pmdungeon.game.dungeon.Dungeon;
import de.fhbielefeld.pmdungeon.game.dungeon.dungeonconverter.DungeonConverter;
import de.fhbielefeld.pmdungeon.game.interactable.Chest;
import de.fhbielefeld.pmdungeon.game.interactable.Interactable;

import java.util.ArrayList;
import java.util.List;

public class GameWorld implements Disposable {

    private final SpriteBatch batch;
    private final List<Character> characterList = new ArrayList<>();
    private final List<Interactable> interactables = new ArrayList<>();
    private Dungeon dungeon;
    private Character hero;

    public GameWorld(SpriteBatch batch) {
        this.batch = batch;

        setupDungeon();
        setupCharacters();
        setupLoot();
    }

    private void setupDungeon() {
        DungeonConverter dungeonConverter = new DungeonConverter();
        dungeon = dungeonConverter.dungeonFromJson("simple_dungeon.json");
    }

    private void setupCharacters() {
        hero = new MaleKnight(new PlayerInputComponent(), this);
        hero.setPosition(dungeon.getStartingLocation());
        characterList.add(hero);

        Character imp = new Imp(null, this);
        imp.setPosition(dungeon.getRandomLocationInDungeon());
        characterList.add(imp);

        Character bigDemon = new BigDemon(null, this);
        bigDemon.setPosition(dungeon.getBossStartingLocation());
        characterList.add(bigDemon);
    }

    private void setupLoot() {
        interactables.add(new Chest(dungeon.getRandomLocationInDungeon()));
    }

    public void update() {
        for (Interactable interactable : interactables) {
            interactable.update();
        }
        List<Character> deadCharacters = new ArrayList<>();
        for (Character character : characterList) {
            character.update();
            if (character.isDead()) deadCharacters.add(character);
        }
        characterList.removeAll(deadCharacters);
    }

    public void render() {
        dungeon.renderFloor(batch);
        for (Interactable interactable : interactables) {
            interactable.render(batch);
        }
        for (Character character : characterList) {
            character.render();
        }
        dungeon.renderWalls(batch);
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public Dungeon getDungeon() {
        return dungeon;
    }

    public Character getHero() {
        return hero;
    }

    public List<Character> getCharacterList() {
        return characterList;
    }

    public List<Interactable> getInteractables() {
        return interactables;
    }

    @Override
    public void dispose() {
        dungeon.dispose();
        for (Character character : characterList) {
            character.dispose();
        }
        for (Interactable interactable : interactables) {
            interactable.dispose();
        }
    }
}