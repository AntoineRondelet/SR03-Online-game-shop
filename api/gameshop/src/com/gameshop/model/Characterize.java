package com.gameshop.model;

import javax.persistence.*;

/**
 * Created by Antoine on 5/6/17.
 */
@Entity
@IdClass(CharacterizePK.class)
public class Characterize {
    private String word;
    private int idGame;
    private GameKeyword gameKeywordByWord;
    private Game gameByIdGame;

    @Id
    @Column(name = "word", nullable = false, length = -1)
    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @Id
    @Column(name = "id_game", nullable = false)
    public int getIdGame() {
        return idGame;
    }

    public void setIdGame(int idGame) {
        this.idGame = idGame;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Characterize that = (Characterize) o;

        if (idGame != that.idGame) return false;
        if (word != null ? !word.equals(that.word) : that.word != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = word != null ? word.hashCode() : 0;
        result = 31 * result + idGame;
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "word", referencedColumnName = "word", nullable = false, insertable = false, updatable = false)
    public GameKeyword getGameKeywordByWord() {
        return gameKeywordByWord;
    }

    public void setGameKeywordByWord(GameKeyword gameKeywordByWord) {
        this.gameKeywordByWord = gameKeywordByWord;
    }

    @ManyToOne
    @JoinColumn(name = "id_game", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    public Game getGameByIdGame() {
        return gameByIdGame;
    }

    public void setGameByIdGame(Game gameByIdGame) {
        this.gameByIdGame = gameByIdGame;
    }
}
