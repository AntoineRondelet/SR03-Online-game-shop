package com.gameshop.model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by Antoine on 5/6/17.
 */
public class CharacterizePK implements Serializable {
    private String word;
    private int idGame;

    @Column(name = "word", nullable = false, length = -1)
    @Id
    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @Column(name = "id_game", nullable = false)
    @Id
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

        CharacterizePK that = (CharacterizePK) o;

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
}
