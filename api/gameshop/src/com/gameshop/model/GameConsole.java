package com.gameshop.model;

import javax.persistence.*;
import java.sql.Date;
import java.util.Collection;

/**
 * Created by Antoine on 5/6/17.
 */
@Entity
@Table(name = "game_console", schema = "public", catalog = "sr03")
public class GameConsole {
    private String name;
    private Date releaseDate;
    private String model;
    private String description;
    private Integer storage;
    private Collection<Game> gamesByName;

    @Id
    @Column(name = "name", nullable = false, length = -1)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "release_date", nullable = true)
    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Basic
    @Column(name = "model", nullable = true, length = -1)
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Basic
    @Column(name = "description", nullable = true, length = -1)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "storage", nullable = true)
    public Integer getStorage() {
        return storage;
    }

    public void setStorage(Integer storage) {
        this.storage = storage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameConsole that = (GameConsole) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (releaseDate != null ? !releaseDate.equals(that.releaseDate) : that.releaseDate != null) return false;
        if (model != null ? !model.equals(that.model) : that.model != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (storage != null ? !storage.equals(that.storage) : that.storage != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (releaseDate != null ? releaseDate.hashCode() : 0);
        result = 31 * result + (model != null ? model.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (storage != null ? storage.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "gameConsoleByConsole")
    public Collection<Game> getGamesByName() {
        return gamesByName;
    }

    public void setGamesByName(Collection<Game> gamesByName) {
        this.gamesByName = gamesByName;
    }
}
