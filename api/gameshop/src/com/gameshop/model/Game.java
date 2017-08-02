package com.gameshop.model;

import javax.persistence.*;
import java.sql.Date;
import java.util.Collection;

/**
 * Created by Antoine on 5/6/17.
 */
@Entity
public class Game {
    private int id;
    private String title;
    private String console;
    private int ageLimit;
    private double price;
    private Date releaseDate;
    private String description;
    private Double rate;
    private Collection<Characterize> characterizesById;
    private GameConsole gameConsoleByConsole;
    private Collection<GameEntity> gameEntitiesById;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "title", nullable = false, length = -1)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "console", nullable = false, length = -1)
    public String getConsole() {
        return console;
    }

    public void setConsole(String console) {
        this.console = console;
    }

    @Basic
    @Column(name = "age_limit", nullable = false)
    public int getAgeLimit() {
        return ageLimit;
    }

    public void setAgeLimit(int ageLimit) {
        this.ageLimit = ageLimit;
    }

    @Basic
    @Column(name = "price", nullable = false, precision = 0)
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Basic
    @Column(name = "release_date", nullable = false)
    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
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
    @Column(name = "rate", nullable = true, precision = 0)
    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Game game = (Game) o;

        if (id != game.id) return false;
        if (ageLimit != game.ageLimit) return false;
        if (Double.compare(game.price, price) != 0) return false;
        if (title != null ? !title.equals(game.title) : game.title != null) return false;
        if (console != null ? !console.equals(game.console) : game.console != null) return false;
        if (releaseDate != null ? !releaseDate.equals(game.releaseDate) : game.releaseDate != null) return false;
        if (description != null ? !description.equals(game.description) : game.description != null) return false;
        if (rate != null ? !rate.equals(game.rate) : game.rate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (console != null ? console.hashCode() : 0);
        result = 31 * result + ageLimit;
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (releaseDate != null ? releaseDate.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (rate != null ? rate.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "gameByIdGame")
    public Collection<Characterize> getCharacterizesById() {
        return characterizesById;
    }

    public void setCharacterizesById(Collection<Characterize> characterizesById) {
        this.characterizesById = characterizesById;
    }

    @ManyToOne
    @JoinColumn(name = "console", referencedColumnName = "name", nullable = false, insertable = false, updatable = false)
    public GameConsole getGameConsoleByConsole() {
        return gameConsoleByConsole;
    }

    public void setGameConsoleByConsole(GameConsole gameConsoleByConsole) {
        this.gameConsoleByConsole = gameConsoleByConsole;
    }

    @OneToMany(mappedBy = "gameByIdGame")
    public Collection<GameEntity> getGameEntitiesById() {
        return gameEntitiesById;
    }

    public void setGameEntitiesById(Collection<GameEntity> gameEntitiesById) {
        this.gameEntitiesById = gameEntitiesById;
    }
}
