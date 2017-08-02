package com.gameshop.model;

import javax.persistence.*;

/**
 * Created by Antoine on 5/6/17.
 */
@Entity
@Table(name = "game_entity", schema = "public", catalog = "sr03")
public class GameEntity {
    private String id;
    private int idGame;
    private Integer idPurchase;
    private Game gameByIdGame;

    @Id
    @Column(name = "id", nullable = false, length = -1)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "id_game", nullable = false)
    public int getIdGame() {
        return idGame;
    }

    public void setIdGame(int idGame) {
        this.idGame = idGame;
    }

    @Basic
    @Column(name = "id_purchase", nullable = true)
    public Integer getIdPurchase() {
        return idPurchase;
    }

    public void setIdPurchase(Integer idPurchase) {
        this.idPurchase = idPurchase;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameEntity that = (GameEntity) o;

        if (idGame != that.idGame) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (idPurchase != null ? !idPurchase.equals(that.idPurchase) : that.idPurchase != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + idGame;
        result = 31 * result + (idPurchase != null ? idPurchase.hashCode() : 0);
        return result;
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
