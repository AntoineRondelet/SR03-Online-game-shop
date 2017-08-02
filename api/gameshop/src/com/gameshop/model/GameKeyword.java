package com.gameshop.model;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by Antoine on 5/6/17.
 */
@Entity
@Table(name = "game_keyword", schema = "public", catalog = "sr03")
public class GameKeyword {
    private String word;
    private Collection<Characterize> characterizesByWord;

    @Id
    @Column(name = "word", nullable = false, length = -1)
    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameKeyword that = (GameKeyword) o;

        if (word != null ? !word.equals(that.word) : that.word != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return word != null ? word.hashCode() : 0;
    }

    @OneToMany(mappedBy = "gameKeywordByWord")
    public Collection<Characterize> getCharacterizesByWord() {
        return characterizesByWord;
    }

    public void setCharacterizesByWord(Collection<Characterize> characterizesByWord) {
        this.characterizesByWord = characterizesByWord;
    }
}
